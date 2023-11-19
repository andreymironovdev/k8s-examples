```bash
cd resources
```

**Create deployment:**

```bash
kubectl apply -f deployment.yaml
```

> **_NOTE:_**  If pod exceeds cpu limit then pod will be throttled

# QoS classes

## Guaranteed

* `limits = requests`
* Such pods are least likely to face eviction

## Burstable

* not `Guaranteed`
* at least one container has request or limit
* such pods have the 2nd priority after `Guaranteed` pods

## Best Effort

* not `Guaranteed` and not `Burstable`
* such pods have the least priority  in case of some eviction needs


**Try request more cpu than cluster has:**

```bash
kubectl patch deployments.apps my-deployment --patch '{"spec": {"template": {"spec": {"containers": [{"name": "nginx", "resources": {"requests": {"cpu": 100}, "limits": {"cpu": 100}}}]}}}}'
# see warning in events:
# Warning  FailedScheduling  117s  default-scheduler  0/1 nodes are available: 1 Insufficient cpu. preemption: 0/1 nodes are available: 1 No preemption victims found for incoming pod..
kubectl get pods | grep Pending | head -n 1 | awk '{print $1}' | xargs kubectl describe pod
```