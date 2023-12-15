```bash
cd service
```

**Create service and deploy:**

```bash
kubectl apply -f service.yaml
kubectl apply -f deployment.yaml
kubectl apply -f configmap.yaml
# show service
kubectl get svc
# show pods
kubectl get pod -o wide --show-labels
# show endpoints created for service - we see pods ip list as endpoints
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
curl my-service
#my-deployment-6cbf9b77-jgz9j
curl my-service
#my-deployment-6cbf9b77-p9nfj
curl my-service
#my-deployment-6cbf9b77-p9nfj
```

** Check my-service iptables rules:**
```bash
# get shell to node
ssh minikube
# get iptables rules and grep by service name
sudo iptables --table nat -S | grep my-service
# all incoming tcp traffic to service ip address and 80 port should be forwarded to chain KUBE-SVC-FXIYY6OHUSNBITIX
-A KUBE-SERVICES -d 10.96.214.16/32 -p tcp -m comment --comment "default/my-service cluster IP" -m tcp --dport 80 -j KUBE-SVC-FXIYY6OHUSNBITIX
# traffic from this chain should be forwarded to KUBE-SEP-XMGGUADT5FSWRZCF with probability = 0.5
-A KUBE-SVC-FXIYY6OHUSNBITIX -m comment --comment "default/my-service -> 10.244.0.8:80" -m statistic --mode random --probability 0.50000000000 -j KUBE-SEP-XMGGUADT5FSWRZCF
# and then to first pod's socket
-A KUBE-SEP-XMGGUADT5FSWRZCF -p tcp -m comment --comment "default/my-service" -m tcp -j DNAT --to-destination 10.244.0.8:80
# ... or forwarded to KUBE-SEP-OEOUGIR6FMEG2S6N
-A KUBE-SVC-FXIYY6OHUSNBITIX -m comment --comment "default/my-service -> 10.244.1.4:80" -j KUBE-SEP-OEOUGIR6FMEG2S6N
# and then to second pod's socket
-A KUBE-SEP-OEOUGIR6FMEG2S6N -p tcp -m comment --comment "default/my-service" -m tcp -j DNAT --to-destination 10.244.1.4:80
```