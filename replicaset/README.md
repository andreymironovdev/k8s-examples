```bash
cd replicaset
```

**Create pod:**

```bash
kubectl apply -f ./replicaset.yaml
```

**Get pods:**

```bash
kubectl get rs
kubectl get pod --show-labels
```

**Scale replicaset**
```bash
kubectl scale replicaset my-relpicaset --replicas=3
```

**Replica set holds the configured number of pods even if we try to delete pod or add pod with the same label:**

```bash
# try delete pod
kubectl get pod | grep my-replicaset | head -n 1 | awk '{print $1}' | xargs kubectl delete pod
# new pod was created
kubectl get pod
# try add pod
kubectl apply -f pod.yaml
# my-pod was deleted
kubectl get pod
```

**Update image version**

```bash
kubectl set image rs my-replicaset nginx=nginx:1.13
# no pod restarts
kubectl get pod
# old version is found
kubectl get pod | grep my-replicaset | head -n 1 | awk '{print $1}' | xargs kubectl describe pod
# delete one pod
kubectl get pod | grep my-replicaset | head -n 1 | awk '{print $1}' | xargs kubectl delete pod
# new pod has new image version
kubectl get pod --sort-by=.status.startTime | tail -n 1 | awk '{print $1}' | xargs kubectl describe pod | grep Image
# so replicaset holds preconfigured number of pods even after template updating, so we need to manually delete pods to apply changes
```

**Clear the cluster:**
```bash
kubectl delete rs --all
```