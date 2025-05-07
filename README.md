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


# Scale a replica set named 'foo' to 3
kubectl scale --replicas=3 rs/foo

# Scale a resource identified by type and name specified in "foo.yaml" to 3
kubectl scale --replicas=3 -f foo.yaml

# If the deployment named mysql's current size is 2, scale mysql to 3
kubectl scale --current-replicas=2 --replicas=3 deployment/mysql

# Scale multiple replication controllers
kubectl scale --replicas=5 rc/example1 rc/example2 rc/example3

# Scale stateful set named 'web' to 3
kubectl scale --replicas=3 statefulset/web
Options

# open port
kubectl port-forward pod-name 5433:5432

# rollout history
kubectl rollout history deployment/song-service-deployment


# revert rollout 
kubectl rollout undo deployment/song-service-deployment --to-revision=2kubectl rollout undo deployment/song-service-deployment

kubectl rollout undo deployment/song-service-deployment

# update image 
kubectl set image deployment/song-service-deployment song-service-pod=song-service:0.1  

# install manifest files
helm install full-coral ./chart

# get generated manifest
helm get manifest full-coral

# uninstall chart
helm uninstall full-coral


# get charts
helm list --all --all-namespaces

https://jiminbyun.medium.com/how-to-manage-environment-variables-in-helm-charts-a-comprehensive-guide-eac379703099
