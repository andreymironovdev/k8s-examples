# K8s Components Scheme

```mermaid
classDiagram
    direction LR
    class Etcd {
        Key-value storage for all cluster configs and manifests
    }
    class ApiServer {
        The only component which makes requests to Etcd
    }
    class ControllerManager{
        * The set of multiple controllers:
        Node controller
        Pods controller
            ...
        * Garbage collector
    }
    class Scheduler{
        maps pods to nodes by pod manifest
        and field "node-name",
        according to several rules:
        * QoS
        * Affinity-anti-affinity
        * resources
        * priority
    }
    class Kubelet{
        Creates podes on node, eligible for probes.
        Posts pod's status in ApiServer.
        Each node have its own Kubelet. 
        It's system service, not a docker container.
    }
    class Kube-proxy{
        Eligible for network rules on node and service implementation.
        Each node have its own Kube-proxy. 
        It's system service, not a docker container.
    }
```

# User Settings

**Enable kubectl autocomplete in zsh**

```bash
# Add the following to your .zshrc file
echo 'source <(kubectl completion zsh)' >>~/.zshrc
# Apply changes
source ~/.zshrc
```

**Configure minikube on Mac M1 with qemu and socket_vmnet instead of docker to prevent issues with ingress:**
```bash
# install and start socket_vmnet
brew install socket_vmnet
HOMEBREW=$(which brew) && sudo ${HOMEBREW} services start socket_vmnet
#restart minikue cluster
minikube delete
minikube start --driver qemu --network socket_vmnet
```