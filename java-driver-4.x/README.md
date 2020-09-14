This repository contains different code samples that are related to the usage of Apace Cassandra&trade; and/or DataStax Enterprise (DSE).

The code is organized as below,
* [`java-driver-4.x/Search Using Mapper`](src/main/java/com/madhavan/demos/mapper/) - An example to demonstrate the usage of DataStax Java Driver v4.x to perform DSE search queries using `solr_query` field.

## Setup and Running
### Prerequisites
The prerequisites required for this example to run are,
* JDK 8+
* A DataStax Enterprise (DSE) cluster with Search workload enabled
### Running
1. Update the `application.conf` to provide the details of your cluster hosts
2. To run this application use the following command: Compile using `mvn clean compile` and run the `ExecuteSolrQueryUsingMapper` class.
3. One should see similar output. 1st result is by leveraging the partition key and 2nd result is by leveraging the search index created on `value` non-primary column,
   ```
   [main] INFO  c.d.o.d.i.c.DefaultMavenCoordinates - DataStax Java driver for Apache Cassandra(R) (com.datastax.oss:java-driver-core)  version 4.9.0
   [s0-admin-0] INFO  c.d.o.d.internal.core.time.Clock - Using native clock for microsecond precision
   Result is: Example [id=5, value=5]
   Search result is: Example [id=5, value=5]
   ----DONE----
   ```
---