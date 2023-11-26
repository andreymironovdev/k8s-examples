```bash
cd namespace
```

**Create namespace:**

```bash
kubectl apply -f ./
# get created pods
kubectl get pods -n my-namespace
```

```bash
# get all pods among all namespaces
kubectl get pod -A
```