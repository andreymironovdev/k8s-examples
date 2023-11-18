```bash
cd probes
```

**Create deployment:**

```bash
kubectl apply -f deployment.yaml
```

**Ruin liveness probe:**

```bash
EDITOR=nano kubectl edit deployment my-deployment
# livenessProbe: /123
# save deployment
# see pod restarts
kubectl get pods -w
```

**Delete deployment:**

```bash
kubectl delete deployment --all
```