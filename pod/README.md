```bash
cd pod
```

**Create pod:**

```bash
kubectl apply -f ./pod.yaml
```

**Get pods:**

```bash
kubectl get pod
```

**Scale pod:**

```bash
cp pod.yaml pod2.yaml
# change the name value in pod2.yml by hands, yq or python script
kubectl apply -f ./pod2.yaml
```

**Update image version in pod:**

```bash
EDITOR=nano kubectl edit pod my-first-pod
```

**Check the result:**

```bash
kubectl describe pod my-first-pod
```

**Clear the cluster:**
```bash
kubectl delete pod --all
```