import os
import json
import razdel
import lexrank
import logging
from lexrank import LexRank
from lexrank.mappings.stopwords import STOPWORDS

logging.basicConfig(level=logging.DEBUG)

# read data set:
logging.info("Reading data set")
result = []
path_to_data = os.getcwd()+"/data"
for filename in os.listdir(path_to_data):
    with open(os.path.join(path_to_data, filename), 'r', encoding='utf_8') as r:
        for line in r:
            result.append(json.loads(line))

# tokenize:
sentences = [
    [s.text for s in razdel.sentenize(r["text"])] for r in result]

logging.info("Building LexRank...")
lxr = LexRank(sentences, stopwords=STOPWORDS['ru'])
logging.info("LexRank sucessfully builded!")


def predict_lex_rank(text, summary_size=1, threshold=None):
    sentences = [s.text for s in razdel.sentenize(text)]
    prediction = lxr.get_summary(
        sentences, summary_size=summary_size, threshold=threshold)
    prediction = " ".join(prediction)
    return prediction
