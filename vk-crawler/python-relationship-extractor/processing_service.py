import re
import nltk
import json
import logging
from navec import Navec
from slovnet import Syntax
from razdel import sentenize, tokenize
from dostoevsky.tokenization import UDBaselineTokenizer
from dostoevsky.models import FastTextSocialNetworkModel

meaningful_tags = ['positive', 'negative']

# init models:
sentenizer = FastTextSocialNetworkModel(tokenizer=UDBaselineTokenizer())


class SimpleExtractor:

    def __init__(self, path_to_navec_data, path_to_syntax_data):
        self.navec = Navec.load(path_to_navec_data)
        self.syntax = Syntax.load(path_to_syntax_data).navec(self.navec)

    def is_values_lower(self, source, target):
        for v in source:
            for d in list(target):
                if v > d:
                    return False
        return True

    def extract_sentnces_with_names(self, text):
        text = re.sub("\\s+", " ", text)

        if not text:
            return {}

        # syntax extraction:
        chunk = []
        for sent in sentenize(text):
            tokens = [_ for _ in nltk.word_tokenize(sent.text)]
            chunk.append(tokens)

        markup = next(self.syntax.map(chunk))

        words = [token.text for token in markup.tokens]
        deps = []
        for token in markup.tokens:
            source = int(token.head_id) - 1
            target = int(token.id) - 1
            if source >= 0 and source != target:  # skip root, loops
                deps.append([source, target, token.rel])

        # get from sentence only obj which starts with upper case:
        obj_to_connections = {}
        for dep in deps:
            obj = words[dep[- 2]]
            if 'obj' in dep and obj[0].isupper():
                obj_to_connections[obj] = dep[:len(dep) - 2]

        # cut init sentence to feed to semantic neural net:
        for key, value in obj_to_connections.items():
            for dep in deps:
                dep = dep[:len(dep) - 2]
                is_intesected = set(value).intersection(set(dep))
                if is_intesected and self.is_values_lower(value, is_intesected) and dep[0] == value[0]:
                    value.extend(dep)

        # distinct:
        for key, value in obj_to_connections.items():
            obj_to_connections[key] = list(set(value))

        # transfrom data to dict: obj -> sentence piece
        words_from_sentence = text.split(" ")
        for key, value in obj_to_connections.items():
            final_slice = ''
            for v in value:
                final_slice += words_from_sentence[v] + ' '

            obj_to_connections[key] = final_slice

        return obj_to_connections

    def extract_relations_from_docs(self, text):
        try:
            # apply semantic analysis to sentences with named entity and get result dict:
            obj_to_sentence = self.extract_sentnces_with_names(text)

            sentences = obj_to_sentence.values()
            prediction_results = sentenizer.predict(sentences)

            target = {}
            for sentence, sentiment in zip(obj_to_sentence.keys(), prediction_results):
                target[sentence] = self.relation_to_string_naive(sentiment)

            return json.dumps(target, ensure_ascii=False)
        except Exception as e:
            logging.error(e)
            return json.dumps({}, ensure_ascii=False)

    def relation_to_string_naive(self, result_map):
        result_map = dict((key, value)
                          for (key, value) in result_map.items() if key in meaningful_tags)

        relation_to_process = dict(
            [max(result_map.items(), key=lambda k_v: k_v[1])]
        )

        key = list(relation_to_process.keys())[0]
        value = list(relation_to_process.values())[0]

        relation = None
        relation_prefix = None

        if value >= 0.8:
            relation_prefix = 'Максимально'
        elif value < 0.8 and value >= 0.2:
            relation_prefix = 'Хорошенько так'
        elif value < 0.2 and value >= 0.1:
            relation_prefix = 'Слегка'
        else:
            return 'Скорее всего всё равно'

        if (key == 'positive'):
            relation = 'любит'
        elif (key == 'negative'):
            relation = 'не нравится'

        return f'{relation_prefix} {relation}'
