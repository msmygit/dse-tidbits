# DataStax Enterprise Environment Sizing Example

**_NOTE:_** This page will showcase an example of environment sizing done with DSE 6.7 on Azure and with default STCS. DSE 6.8 & Big Node will change these sizing.

|Items \ DSE Environment|Development|Pre-production - DC1|Pre-production - DC2|Production - DC1|Production - DC2|
|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|Azure Region|South Central US|South Central US|South Central US|South Central US|South Central US|
|Data Center Count|Data Center 1|Data Center 1|Data Center 2|Data Center 1|Data Center 2|
|Node Count|3|6|3|9|3|
|DSE Version|6.7.latest|6.7.latest|6.7.latest|6.7.latest|6.7.latest|
|DataStax Agent Version|6.8.latest|6.8.latest|6.8.latest|6.8.latest|6.8.latest|
|Installation Type|Package Install|Package Install|Package Install|Package Install|Package Install|
|Workload Type|C* + Analytics|C*|C* + Analytics|C*|C* + Analytics|
|Instance Type|D8s v3|D8s v3|D8s v3|D16s v3 / E16s v3|D16s v3 / E16s v3|
|Replication Factor|3|3|1|3|1|
|Managed OS Disk Type|1 * P10 Premium SSD|1 * P10 Premium SSD|1 * P10 Premium SSD|1 * P10 Premium SSD|1 * P10 Premium SSD|
|Managed Data Disk Type|2 * P30 Premium SSD (RAID-0)|2 * P30 Premium SSD (RAID-0)|2 * P30 Premium SSD (RAID-0)|2 * P30 Premium SSD (RAID-0)|2 * P30 Premium SSD (RAID-0)|
|Compaction Strategy|SizeTieredCompactionStrategy|SizeTieredCompactionStrategy|SizeTieredCompactionStrategy|SizeTieredCompactionStrategy|SizeTieredCompactionStrategy|
|Actual Data Size (TB) per node|1.25|1.25|1.25|1.25|1.25|
|Raw Data per node (TB) (Actual Data / RF)|0.4166666667|0.4166666667|1.25|0.4166666667|1.25|
|Total Capacity for the DC (TB) (Node Count * Raw Data Size)|3.75|7.5|3.75|11.25|3.75|
|Consistency Levels for read/write|LOCAL_QUORUM|LOCAL_QUORUM|LOCAL_ONE|LOCAL_QUORUM|LOCAL_ONE|

---

|Items \ OpsCenter Env.|Development & Pre-production|Production|
|:---:|:---:|:---:|
|OpsCenter Version|6.8.latest|6.8.latest|
|Installation Type|Package Install|Package Install|
|Active/Passive Failover|Yes, 2 VMs dedicated for Active and Passive OpsCenter setup|Yes, 2 VMs dedicated for Active/Passive OpsCenter setup|
|Daemon VM Instance Type|D8s v3|D16s v3|
|Managed OS Disk Type (OpsC Daemon & Storage nodes)|1 * P4 Premium SSD|1 * P4 Premium SSD|
|OpsCenter Storage - DSE Cluster node count|3|3|
|Managed Data Disk Type|1 * P15 Premium SSD|1 * P20 Premium SSD|
|Replication Factor|3|3|

---