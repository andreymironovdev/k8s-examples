```bash
cd deployment
```

**Create deployment:**

```bash
kubectl apply -f deployment.yaml
# we have 1 deployment
kubectl get deploy
# we have 1 replica set
kubectl get rs
# we have 2 pod
kubectl get pod
```

**Update deployment:**

```bash
kubectl set image deployment my-deployment nginx=nginx:1.13
# watch pods and see that pods are being creating 1 by 1
kubectl get pod -w
# see 2 replica sets, one of which has 0 current pods and it exists for rollback needs
kubectl get rs
# rollback deployment to previous revision
kubectl rollout undo deployment my-deployment --to-revision=0
# or
kubectl rollout undo deployment my-deployment
# rollback deployment to revision before previous
kubectl rollout undo deployment my-deployment --to-revision=1
```

**Delete deployment:**

```bash
kubectl delete deployment --all
```