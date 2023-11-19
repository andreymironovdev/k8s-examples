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
