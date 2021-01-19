This repository contains different code samples that are related to the usage of Apace Cassandra&trade; and/or DataStax Enterprise (DSE).

The code is organized as below,
* [`java-driver-4.x/Search Using Mapper`](src/main/java/com/madhavan/demos/mapper/) - An example to demonstrate the usage of DataStax Java Driver v4.x to perform DSE search queries using `solr_query` field.
* [`java-driver-4.x/Schema Builder & Geospatial Types`](src/main/java/com/madhavan/demos/schemabuilder/CreateTableWithSchemaBuilder.java) - An example to demonstrate the usage of Schema Builder with DataStax Enterprise&trade; Geospatial types such as `PointType`, `LineStringType` and `PolygonType`.

## Setup and Running
### Prerequisites
The prerequisites required for this example to run are,
* JDK 8+
* A DataStax Enterprise (DSE) cluster with Search workload enabled
### Running
1. Update the `application.conf` to provide the details of your cluster hosts
2. To run this application use the following command: Compile using `mvn clean compile`.
3. By running the `ExecuteSolrQueryUsingMapper` class one should see similar output. 1st result is by leveraging the partition key and 2nd result is by leveraging the search index created on `value` non-primary column,
   ```
   [main] INFO  c.d.o.d.i.c.DefaultMavenCoordinates - DataStax Java driver for Apache Cassandra(R) (com.datastax.oss:java-driver-core)  version 4.9.0
   [s0-admin-0] INFO  c.d.o.d.internal.core.time.Clock - Using native clock for microsecond precision
   Result is: Example [id=5, value=5]
   Search using @Query result is: Example [id=5, value=5]
   Search using @Select(customWhereClause) result is: Example [id=5, value=5]
   ----DONE----
   ```
4. By running the `CreateTableWithSchemaBuilder` class one should see the table created as follows:
   ```
   CREATE TABLE IF NOT EXISTS test.test_schema_builder (
    pk1 int PRIMARY KEY,
    c1_pointtype 'org.apache.cassandra.db.marshal.PointType',
    c2_linestringtype 'org.apache.cassandra.db.marshal.LineStringType',
    c2_polygontype 'org.apache.cassandra.db.marshal.PolygonType'
   );
   ```
---
