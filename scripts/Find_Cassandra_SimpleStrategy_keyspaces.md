# How-To Determine Keyspaces with SimpleStrategy

**_NOTE:_** This page will showcase how to determine if there are keyspaces in a cluster with SimpleStrategy replication.

```
SELECT * FROM system_schema.keyspaces WHERE replication CONTAINS 'org.apache.cassandra.locator.SimpleStrategy' ALLOW FILTERING;
```

* Related Content
  * Script to adjust keyspace replication settings can be found [here](https://github.com/DataStax-Toolkit/cassandra-dse-helper-scripts/tree/master/adjust-keyspaces)

---