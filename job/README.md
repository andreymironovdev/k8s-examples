# Job

Useful for data migration

```bash
cd job
```

**Check job with successful status:**

```bash
kubectl apply -f job-success.yaml
kubectl get pods
#job-success-2r6v8   0/1     Completed   0          6s
kubectl logs job-success-2r6v8 
#Tue Jan  2 14:29:15 UTC 2024
#Hello World!
```

Completed pods need to cleared periodically to prevent docker daemon problems

**Check job with error status:**

```bash
kubectl apply -f job-error.yaml
kubectl get pods | grep job-error
#job-error-4tb5s     0/1     Error       0          35s
#job-error-p2vjk     0/1     Error       0          17s
kubectl describe jobs.batch job-error
#  Warning  BackoffLimitExceeded  0s    job-controller  Job has reached the specified backoff limit
```

**Check job infinite:**

```bash
kubectl apply -f job-infinite.yaml
kubectl get pods -w
# k8s waits for 60s and then sends sigterm signal. Then if pod is alive after 30s k8s sends sigkill
#job-infinite-2brpb   1/1     Running     0          37s
#job-infinite-2brpb   1/1     Terminating   0          60s
#job-infinite-2brpb   1/1     Terminating   0          60s
#job-infinite-2brpb   0/1     Terminating   0          90s
#job-infinite-2brpb   0/1     Terminating   0          90s
#job-infinite-2brpb   0/1     Terminating   0          90s
#job-infinite-2brpb   0/1     Terminating   0          90s
kubectl describe jobs.batch job-infinite
#  Warning  DeadlineExceeded  2m16s  job-controller  Job was active longer than specified deadline
```
