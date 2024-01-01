# Stateful Set

* group of stateful pods
* k8s guarantees pods uniqueness, order of creation
* state is persisted by PVC template
* usage - RDMS, Message Brokers - PostgreSQL, MySQL, Kafka, RabbitMQ, ...

# Practice

```bash
kubectl apply -f .
# watch that rabbitmq pods are created
kubectl get pod -w
```