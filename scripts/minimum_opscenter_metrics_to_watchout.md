# What all metrics/graphs at the minimum should I configure in OpsCenter?

_**NOTE:**_ This page will list a minimal set of metrics/graphs that one should configure in OpsCenter as a start when managing a DSE cluster.

The below would be a good list to start with,
* Active Alerts
* Cluster Health
* Storage Capacity
* Read & Write Request Latency
* Read & Write Requests
* OS: Disk Latency, Load, CPU Iowait, Memory Free
* If Search is enabled, Search: Core Size, Read Latency, Timeouts
* Heap Used
* Data Size
* Compactions Pending
* Dropped Messages: Mutations, Reads
* JVM G1 Old Collection Count & Time
* JVM G1 Young Collection Count & Time
* Native Clients
* Hints on Disk, Hint Dispatcher Active, Hint Dispatcher Completed
* If NodeSync is enabled, NodeSync related,
  * TP: Read Range NodeSync Active
  * NodeSync: Uncompleted Pages, Failed Pages

Reference: [OpsCenter Dashboard Performance Metrics](https://docs.datastax.com/en/opscenter/6.8/opsc/online_help/opscMetricsReference.html)
---