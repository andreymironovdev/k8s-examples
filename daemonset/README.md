# Daemon Set

* creates pod on every cluster's node
* usage - monitorings, network plugins

## Taints and Tolerations

Each node can be tainted with some key-value label and to prevent podes to be scheduled on it:
```bash
# <EFFECT> = NoSchedule, PreferNoSchedule or NoExecute
kubectl taint node <NODE_NAME> <KEY>=<VALUE>:<EFFECT>
```
We can add tolerations to every pod to prevent applying tainting politics for it:
```yaml
spec:
  template:
    spec:
      tolerations:
      - key: <KEY1>
        operator: Exists
        effect: NoSchedule
      - key: <KEY2>
        value: <VALUE2>
        effect: NoExecute
```

## Practice
**Check daemon set:**
```bash
cd daemonset
# start minikube with 1 control plane node and 2 worker nodes
minikube start --nodes=3
# taint all nodes
kubectl taint node minikube testTaintKeyOnControlPlane:NoSchedule
kubectl taint node minikube-m02 testTaintKeyOnWorker1:NoSchedule
kubectl taint node minikube-m03 testTaintKeyOnWorker2:NoSchedule
# create daemonset
kubectl apply -f daemonset.yaml
# check that pods were created only on nodes minikube, minikube-m02 because of daemonset tolerations config
kubectl get pods -n default -o wide
# get prometheus metrics from pod on control plane node
minikube ssh
curl -X GET <POD_IP>:9100/metrics
## HELP go_gc_duration_seconds A summary of the pause duration of garbage collection cycles.
## TYPE go_gc_duration_seconds summary
#go_gc_duration_seconds{quantile="0"} 0
#go_gc_duration_seconds{quantile="0.25"} 0
#...
```

Every node can be **tainted** to disable scheduling for pods on it:
```yaml

```
And every pod can be 