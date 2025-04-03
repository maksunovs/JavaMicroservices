# Java Microservices course

## Useful links:
### Kubernetes:
#### https://www.digitalocean.com/community/tutorials/an-introduction-to-kubernetes
#### https://kubernetes.io/docs/concepts/overview/components/
#### https://medium.com/devops-mojo/kubernetes-objects-resources-overview-introduction-understanding-kubernetes-objects-24d7b47bb018
#### https://kubernetes.io/docs/concepts/workloads/controllers/
#### https://bluexp.netapp.com/blog/cvo-blg-kubernetes-persistent-volume-claims-explained
#### https://vocon-it.com/2018/12/20/kubernetes-local-persistent-volumes/
#### https://kubernetes.io/docs/concepts/workloads/controllers/statefulset/

## Guides
### Namespaces

- Create namespace

``kubectl create -f ./manifest/namespace/k8s-program-namespace.yaml``

or

``kubectl create namespace k8s-program-namespace.yaml``

- List namespace

``kubectl get namespaces``

- delete namespace

``kubectl delete namespaces k8s-program``


StatefulSets do not provide any guarantees on the termination of pods when a StatefulSet is deleted. To achieve ordered and graceful termination of the pods in the StatefulSet, it is possible to scale the StatefulSet down to 0 prior to deletion.


kubectl config set-context --current --namespace=k8s-program   


kubectl exec -n k8s-program --stdin --tty song-service-deployment-66654d64cf-9nbjl -- /bin/bash

kubectl exec -i -t  song-service-deployment-test-74d9d986f9-9qnzv -- /bin/sh  

