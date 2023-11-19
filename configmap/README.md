```bash
cd configmap
```

**Create config map and deployment:**

```bash
kubectl apply -f configmap.yaml
kubectl apply -f deployment.yaml
# if we see that configmap file is broken and we need to restart deployment then:
# change and apply configmap
kubectl apply -f configmap.yaml
# restart deployment
kubectl rollout restart deployment my-deployment
# go into pod to see file from configmap
kubectl exec -it $POD_NAME bash
# we're inside pod, go to config file
ls /etc/nginx/conf.d -lsa
# and we see symlinks
#total 12
#4 drwxrwxrwx 3 root root 4096 Nov 19 11:16 .
#4 drwxr-xr-x 3 root root 4096 May  1  2018 ..
#4 drwxr-xr-x 2 root root 4096 Nov 19 11:16 ..2023_11_19_11_16_31.1619109092
#0 lrwxrwxrwx 1 root root   32 Nov 19 11:16 ..data -> ..2023_11_19_11_16_31.1619109092
#0 lrwxrwxrwx 1 root root   19 Nov 19 11:16 default.conf -> ..data/default.conf
```

**Change configmap:**
```bash
EDITOR=nano kubectl edit configmaps my-configmap
kubectl exec -it $POD_NAME bash
ls /etc/nginx/conf.d -lsa
# and underlying file has been changed
#total 12
#4 drwxrwxrwx 3 root root 4096 Nov 19 12:02 .
#4 drwxr-xr-x 3 root root 4096 May  1  2018 ..
#4 drwxr-xr-x 2 root root 4096 Nov 19 12:02 ..2023_11_19_12_02_58.2471324339
#0 lrwxrwxrwx 1 root root   32 Nov 19 12:02 ..data -> ..2023_11_19_12_02_58.2471324339
#0 lrwxrwxrwx 1 root root   19 Nov 19 11:16 default.conf -> ..data/default.conf
```

> **_NOTE:_**  If we set /etc as mount path, then K8s replaces ALL contents of /etc by config map contents

**Check that configmap was applied correctly**
```bash
kubectl port-forward $POD_NAME 8080:80
curl localhost:8080
# and we see pod name
```

**Delete cm and deploy:**
```bash
kubectl delete cm --all
kubectl delete deploy --all
```