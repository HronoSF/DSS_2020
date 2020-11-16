#!/bin/bash

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
eval helm install elasticsearch elastic/elasticsearch --namespace default --set imageTag=7.6.2 --set replicas=1 &>/dev/null || exit 1

tput setaf 1
while [[ $(kubectl get pods elasticsearch-master-0 -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}') != "True" ]]; do
  echo $'\nWaiting for Elasticsearch to be ready...'
  sleep 15
done

deploy() {
  tput setaf 2
  echo $'\nDeploying' "$1"
  tput sgr0
  eval helm install "$1" "${PWD}/$1" &>/dev/null || exit 1
  sleep 30
}

# TODO: update services deploy logic to resolve startup conflicts:
#for D in *; do
#  if [ -d "${D}" ]; then
#    tput setaf 2
#    echo $'\nDeploying' "${D}"
#    tput sgr0
#    eval helm install "${D}" "${PWD}/${D}" &>/dev/null || exit 1
#    sleep 15
#  fi
#done

deploy java-crawler
deploy python-relationship-extractor-service
deploy python-summarization-service
deploy proxy-api
deploy proxy-auth
deploy angular-admin-ui
deploy angular-client-ui
deploy java-search-engine
deploy java-data-processing
