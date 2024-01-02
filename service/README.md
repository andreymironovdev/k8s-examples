# Service

Firstly, create an application:
```bash
cd service
kubectl apply -f deployment.yaml
kubectl apply -f configmap.yaml
```

## ClusterIP

Service of this type is reachable only from within the cluster and exposed through a cluster internal IP.

**Create service and check it from within pod:**

```bash
kubectl apply -f service-cluster-ip.yaml
# show service
kubectl get svc
# show pods
kubectl get pod -o wide --show-labels
# show endpoints created for service - we see pods ip:port list as endpoints
kubectl get ep
# let's test the service. To do that let's create the pod with curl, and curl my-service inside it
docker build -t image-with-curl -f Dockerfile .
# load image to minikube
minikube image load image-with-curl
# check that image exists
minikube image ls | grep image-with-curl
# run the pod in interactive mode with bash terminal
kubectl run -it --rm --image=image-with-curl --image-pull-policy=Never bash
# curl my-service inside pod multiple times to see that service propagates requests to different pods
curl service-cluster-ip
#my-deployment-6cbf9b77-jgz9j
curl service-cluster-ip
#my-deployment-6cbf9b77-p9nfj
curl service-cluster-ip
#my-deployment-6cbf9b77-p9nfj
```

**Check service-cluster-ip iptables rules:**

```bash
# get shell to node
ssh minikube
# get iptables rules and grep by service name
sudo iptables --table nat -S | grep service-cluster-ip
# all incoming tcp traffic to service ip address and 80 port should be forwarded to chain KUBE-SVC-G5J5PKZPNOPXI3Y7
-A KUBE-SERVICES -d <SERVICE_CLUSTER_IP>/32 -p tcp -m comment --comment "default/service-cluster-ip cluster IP" -m tcp --dport 80 -j KUBE-SVC-G5J5PKZPNOPXI3Y7
# traffic from this chain should be forwarded to KUBE-SEP-UHUQBQXIMDXIA2A7 with probability = 0.5
-A KUBE-SVC-G5J5PKZPNOPXI3Y7 -m comment --comment "default/service-cluster-ip -> <FIRST_POD_IP>:80" -m statistic --mode random --probability 0.50000000000 -j KUBE-SEP-UHUQBQXIMDXIA2A7
# and then to first pod's socket
-A KUBE-SEP-UHUQBQXIMDXIA2A7 -p tcp -m comment --comment "default/service-cluster-ip" -m tcp -j DNAT --to-destination <FIRST_POD_IP>:80
# ... or forwarded to KUBE-SEP-OEOUGIR6FMEG2S6N
-A KUBE-SVC-G5J5PKZPNOPXI3Y7 -m comment --comment "default/service-cluster-ip -> <SECOND_POD_IP>:80" -j KUBE-SEP-PP3SQISGYDFZZD2X
# and then to second pod's socket
-A KUBE-SEP-PP3SQISGYDFZZD2X -p tcp -m comment --comment "default/service-cluster-ip" -m tcp -j DNAT --to-destination <SECOND_POD_IP>:80
```

**check service-cluster-ip from within node:**
```bash
minikube ssh
nslookup service-cluster-ip
# ** server can't find service-cluster-ip: SERVFAIL
curl <SERVICE_CLUSTER_IP>
#my-deployment-55fd5f7c4f-vs4g7
```

## Headless

No cluster IP is allocated, no load-balancing and no proxying is specified. If selector is specified in service manifest,
then DNS A or AAAA record is added for each pod backed by service.

**Service DNS name resolves to all pods IP addresses from within the pod:**
```bash
kubectl apply -f service-headless.yaml
kubectl run -it --rm --image=image-with-curl --image-pull-policy=Never bash
nslookup service-headless
#Name:   service-headless.default.svc.cluster.local
#Address: <FIRST_POD_IP>
#Name:   service-headless.default.svc.cluster.local
#Address: <SECOND_POD_IP>
```

## NodePort

Every node in the cluster will configure itself to listen on a port specified in the service manifest and will forward
traffic to service endpoints.

```bash
kubectl apply -f service-node-port.yaml
# check that node listens on a configured port
minikube ssh
curl localhost:30007
# my-deployment-55fd5f7c4f-g545k
```

**Service DNS name also resolves to its Cluster IP from within the pod:**
```bash
kubectl run -it --rm --image=image-with-curl --image-pull-policy=Never bash
nslookup service-node-port
#Name:   service-node-port.default.svc.cluster.local
#Address: <SERVICE_CLUSTER_IP>
```
