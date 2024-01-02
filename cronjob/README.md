# CronJob

* failedJobsHistoryLimit - how many failed executions to keep
* successfulJobsHistoryLimit - how many successful executions to keep
* concurrencyPolicy - what to do, if previous job keeps working
* startingDeadlineSeconds - (optional) deadline, after which skipped job won't be scheduled for execution

It is useful to set `concurrencyPolicy = Forbid, startingDeadlineSeconds = 0.5 * <CRON_INTERVAL>`

**Cron job without startingDeadlineSeconds and with execution time > cron interval will schedule immediately after previous execution:**
```bash
cd cronjob
kubectl apply -f cronjob-without-starting-deadline.yaml
kubectl get pod -w
#first-cronjob-28403472-kwrd4   0/1     Pending   0          0s
#first-cronjob-28403472-kwrd4   0/1     Pending   0          0s
#first-cronjob-28403472-kwrd4   0/1     ContainerCreating   0          0s
#first-cronjob-28403472-kwrd4   1/1     Running             0          3s
#first-cronjob-28403472-kwrd4   0/1     Completed           0          72s
#first-cronjob-28403472-kwrd4   0/1     Completed           0          73s
#first-cronjob-28403472-kwrd4   0/1     Completed           0          74s
#first-cronjob-28403472-kwrd4   0/1     Completed           0          74s
#first-cronjob-28403473-snqvr   0/1     Pending             0          0s
#first-cronjob-28403473-snqvr   0/1     Pending             0          0s
#first-cronjob-28403473-snqvr   0/1     ContainerCreating   0          0s
#first-cronjob-28403473-snqvr   1/1     Running             0          4s
#first-cronjob-28403473-snqvr   0/1     Completed           0          75s
```

**Cron job with startingDeadlineSeconds and with execution time > cron interval will execute only by cron:**
```bash
cd cronjob
kubectl apply -f cronjob-without-starting-deadline.yaml
kubectl get pod -w
#cronjob-with-starting-deadline-28403479-8kg6x   0/1     Pending   0          0s
#cronjob-with-starting-deadline-28403479-8kg6x   0/1     Pending   0          0s
#cronjob-with-starting-deadline-28403479-8kg6x   0/1     ContainerCreating   0          0s
#cronjob-with-starting-deadline-28403479-8kg6x   1/1     Running             0          4s
#cronjob-with-starting-deadline-28403479-8kg6x   0/1     Completed           0          74s
#cronjob-with-starting-deadline-28403479-8kg6x   0/1     Completed           0          75s
#cronjob-with-starting-deadline-28403479-8kg6x   0/1     Completed           0          76s
#cronjob-with-starting-deadline-28403479-8kg6x   0/1     Completed           0          76s
```