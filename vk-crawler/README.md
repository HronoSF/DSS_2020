**Description:** <br><br>
Автоматический агрегатор данных с ВК (используя АПИ ВК) 
(тексты из пуб. Страниц пользователей, постов в группах заданных администратором) + прослойка с ElasticSearch + микросервисы с нейронками выдающими краткую суть  текстов, пары персонаж - отношение,+ Клиент поиска постов и визуализации графа взаимо отношений пользователей к людям (вася не любит петю и дашу но обажает машу)

**For local startup:**
1. docker-compose -f docker-compose-infra.yml up -d
2. docker-compose -f docker-compose-ui.yml up -d --build
3. docker-compose -f docker-compose-services.yml up -d --build

<br>**Project infrastructure:**<br><br>
![alt text](./readme-data/infra.jpg)
