**Build docker image with a sample ktor app, which has endpoints for uploading files to mounted volume /data and for downloading them:**
```bash
cd pv-pvc/ktor-sample
gradle clean build
docker build -t ktor-sample:1.0 .
minikube image load ktor-sample:1.0
# check that image was loaded
minikube ssh
docker image ls | grep ktor
# ktor-sample                                          1.0       f9cd0f48bac8   2 hours ago     411MB
exit
```

**Create pv, pvc, deployment, service and ingress:**

```bash
cd pv-pvc
kubectl apply -f pv.yaml
kubectl apply -f pvc.yaml
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl apply -f ingress.yaml
```

```bash
# check saving files to pv:
curl -X POST -F file=@pv.yaml --resolve "ktor-sample.info:80:$( minikube ip )" ktor-sample.info/upload
curl -X GET --resolve "ktor-sample.info:80:$( minikube ip )" ktor-sample.info/download/pv.yaml
# and we see file content
#---
#apiVersion: v1
#kind: PersistentVolume
#metadata:
#  name: my-pv
#spec:
#  accessModes:
#    - ReadWriteOnce
#  capacity:
#    storage: 2Gi
#  hostPath:
#    path: /data/my-pv/
#...

# find file inside node
minikube ssh
cat /tmp/hostpath-provisioner/default/my-claim/pv.yaml
# recreate deployment and see that file is still present
kubectl delete deployment ktor-sample-deploy
kubectl apply -f deployment.yaml
curl -X GET --resolve "ktor-sample.info:80:$( minikube ip )" ktor-sample.info/download/pv.yaml
```