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
