# Ingress

metadata.annotations - extends basic ingress functions set

```bash
cd ingress
```

**Start ingress controller:**
```bash
minikube addons enable ingress
# wait for ingress controller pod to have Running status
kubectl get pods -n ingress-nginx
# NAME                                        READY   STATUS      RESTARTS   AGE
# ingress-nginx-admission-create-2wxpr        0/1     Completed   0          8m17s
# ingress-nginx-admission-patch-h7jhg         0/1     Completed   1          8m17s
# ingress-nginx-controller-6cc5ccb977-qtvmn   1/1     Running     0          8m17s
```

**Create ingress:**

```bash
kubectl apply -f ./configmap.yaml
kubectl apply -f ./deployment.yaml
kubectl apply -f ./service.yaml
kubectl apply -f ./ingress.yaml
# check ingress internal nginx config
kubectl exec -it -n ingress-nginx ingress-nginx-controller-6cc5ccb977-txz6c -- /bin/bash
cat /etc/nginx/nginx.conf
# ...
# server {
#               server_name hello-world.info ;
# ...
# check the ingress without modifying /etc/hosts
curl --resolve "hello-world.info:80:$( minikube ip )" -i http://hello-world.info
# check the ingress with modifying /etc/hosts
sudo nano /etc/hosts
# insert $( minikube ip ) hello-world.info mapping and save file
# open browser and go to hello-world.info
```
