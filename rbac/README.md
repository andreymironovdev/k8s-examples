# RBAC

## ServiceAccount

K8s authentication object.

## Role

Bounded to specific namespace. Describes actions on k8s entities within namespace. 

## RoleBinding

Connects `Role` and authorization entities - inner (`ServiceAccount`) or outer from LDAP or another 
authentication service (`User`, `Group`).

## ClusterRole

Same as `Role`, but is not bounded to any namespace and describes actions on cluster objects - pv, ... .

ClusterRole can be added in `RoleBinding` and its authorities will be applied to a specific namespace.

## ClusterRoleBinding

Same as `RoleBinding`, but connects ClusterRole with authentication objects

## Practice

```bash
cd rbac
kubectl apply -f .
# get services in default namespace from 'user' service account
kubectl get service --as=system:serviceaccount:default:user
# get services in kube-system namespace from 'user' service account
kubectl get service --as=system:serviceaccount:default:user -n kube-system
#Error from server (Forbidden): services is forbidden: User "system:serviceaccount:default:user" cannot list resource "services" in API group "" in the namespace "kube-system"
kubectl delete service kubernetes --as=system:serviceaccount:default:user
#Error from server (Forbidden): services "kubernetes" is forbidden: User "system:serviceaccount:default:user" cannot delete resource "services" in API group "" in the namespace "default"
```