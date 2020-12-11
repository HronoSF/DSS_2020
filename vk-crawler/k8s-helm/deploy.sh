#!/bin/bash

eval helm repo add elastic https://helm.elastic.co
eval helm repo update

tput setaf 1
echo $'\nCreating cluster\n'
tput sgr0
eval kind create cluster --name vk-crawler --config kind-setup.yaml || exit 1

tput setaf 1
echo $'\nInstalling Istio\n'
tput sgr0
eval istioctl install -f istio-setup.yaml || exit 1

tput setaf 1
echo $'\nLabeling default namespace as injectable for Istio\n'
tput sgr0
eval kubectl label namespace default istio-injection=enabled || exit 1

tput setaf 1
echo $'\nCreating Secret for Kiali\n'
tput sgr0
eval kubectl apply -f kiali-secret.yaml || exit 1

tput setaf 2
echo $'\nCreating Elasticsearch\n'
tput sgr0
eval helm install elasticsearch elastic/elasticsearch --namespace default --set imageTag=7.7.0 --set replicas=1 &>/dev/null || exit 1

tput setaf 1
while [[ $(kubectl get pods elasticsearch-master-0 -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}') != "True" ]]; do
  echo $'\nWaiting for Elasticsearch to be in status Ready...'
  echo $'WARNING: if ES init goes more than 10 minutes - smth broke, contact author or checkout logs of Elasticsearch and update deploy.sh'
  sleep 15
done

deploy() {
  tput setaf 2
  echo $'\nDeploying' "$1"
  tput sgr0
  eval helm install "$1" "${PWD}/$1" &>/dev/null || exit 1
  sleep 30
}

deploy java-crawler
tput setaf 1

while [[ $(test $(echo $(kubectl exec elasticsearch-master-0 -c elasticsearch -- curl -o /dev/null -s -w "%{http_code}\n" http://localhost:9200/wall_posts)) != $(echo 200) && echo false || echo true) != true ]]; do

  if [[ $(kubectl get pods $(kubectl get pod -l app.kubernetes.io/instance=java-crawler -o jsonpath="{.items[0].metadata.name}") -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}') == "True" ]]; then
    echo $'\nEs refused to crawler. Turning down app. Try to redeploy application.'
    eval sh ./delete.sh
    eval exit 1
  fi

  echo $'\nWait for Java-Crawler to init...'
  sleep 10
done

tput setaf 0

deploy python-relationship-extractor-service
deploy python-summarization-service
deploy proxy-api
deploy proxy-auth
deploy angular-admin-ui
deploy angular-client-ui
deploy java-search-engine
deploy java-data-processing
