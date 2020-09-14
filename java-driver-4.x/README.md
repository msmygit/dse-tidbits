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

---