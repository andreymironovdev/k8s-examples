# Secret types:
## generic
## docker-re
## generic

```bash
cd secrets
```

**Create secret:**

```bash
kubectl create secret generic test --from-literal=test1=asdf
kubectl get secret test -o yaml
```

**Create deployment:**

```bash
kubectl apply -f deployment.yaml
# view env variables
kubectl exec -it my-deployment-59d9d545ff-6nxwc bash
echo $TEST
# foo
echo $TEST_1
# asdf
```
