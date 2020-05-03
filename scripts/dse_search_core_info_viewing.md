# How to view DSE search core related information?

Below are couple ways to view DSE search core related information as shown below,

**CQLSH way:**
```
DESCRIBE ACTIVE SEARCH INDEX SCHEMA ON keyspace_name.table_name;
DESCRIBE ACTIVE SEARCH INDEX CONFIG ON keyspace_name.table_name;

DESCRIBE PENDING SEARCH INDEX SCHEMA ON keyspace_name.table_name;
DESCRIBE PENDING SEARCH INDEX CONFIG ON keyspace_name.table_name;
```

  _Documentation References:_
  * https://docs.datastax.com/en/dse/6.8/cql/cql/cql_reference/cqlsh_commands/cqlshDescribeSearchIndex.html
  * https://docs.datastax.com/en/dse/6.8/cql/cql/cql_reference/cql_commands/cqlAlterSearchIndexConfig.html
  * https://docs.datastax.com/en/dse/6.8/cql/cql/cql_reference/cql_commands/cqlAlterSearchIndexSchema.html

**The way to do it via [DataStax Studio](https://docs.datastax.com/en/landing_page/doc/landing_page/current.html#Studio) is as below:**

*_NOTE_:* The user role with which the below command is executed requires [SELECT permission](https://docs.datastax.com/en/dse/6.8/cql/cql/cql_reference/cql_commands/cqlGrant.html) on `solr_admin.solr_resources` table for viewing the data.
```
SELECT blobAsText(resource_value) AS resource FROM solr_admin.solr_resources WHERE core_name = 'keyspace_name.table_name' AND resource_name = '$resource_name';
```
  where,

  * `$resource_name` is either `schema.xml` or `solrconfig.xml` for pending, and `schema.xml.bak` and `solrconfig.xml.bak` for active

**DSETool Way:**
```
dsetool get_core_schema keyspace_name.table_name current=true
dsetool get_core_schema keyspace_name.table_name current=false

dsetool get_core_config keyspace_name.table_name current=true
dsetool get_core_config keyspace_name.table_name current=true
```
  _Documentation References:_
  * https://docs.datastax.com/en/dse/6.8/dse-dev/datastax_enterprise/tools/dsetool/dsetoolGet_core_schema.html
  * https://docs.datastax.com/en/dse/6.8/dse-dev/datastax_enterprise/tools/dsetool/dsetoolGet_core_config.html
  * https://docs.datastax.com/en/dse/6.8/cql/cql/cql_using/search_index/indexMgmt.html

---  