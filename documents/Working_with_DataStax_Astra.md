<center><h1> Working with DataStax Astra&trade; </h1></center>

# DataStax Astra
[Astra](https://www.datastax.com/products/datastax-astra) is a Cloud-Native, NoSQL, Scale-Out, Distributed, Hybrid Database-as-a-Service built on Apache Cassandra&trade; brought to you by DataStax&trade;. Astra simplifies cloud-native Cassandra application development. It reduces deployment time from weeks to minutes, removing the biggest obstacle to using Cassandra, which is behind many of the most heavily used applications in the world.

**_NOTE_**: This page provides you with information interacting with DataStax Astra using variety of methods.

Astra provides the following features out of the box,
* **10 Gig Free Tier**: Launch a database in the cloud with a few clicks, no credit card required
* **No Operations**: Eliminate the overhead to install, operate, and scale Cassandra
* **Powerful APIs**: Out-of-the-box REST and GraphQL endpoints and browser CQL shell
* **Cloud-Native**: Powered by our [open-source Kubernetes Operator for Cassandra](https://www.datastax.com/dev/kubernetes)
* **Zero Lock-In**: Deploy on AWS or GCP (or Azure [coming shortly...]) and keep compatibility with open-source Cassandra
* **Global Scale**: Put your data where you need it without compromising performance, availability or accessibility

<details>
    <summary>Try Astra now!</summary>

    ## Useful Links
    * [Try for free!](https://astra.datastax.com/register)
    * [Migrate your application(s) to Astra](https://www.datastax.com/blog/2020/07/take-flight-live-and-free-migration-your-apache-cassandra-apps)
</details>

---
|Try Astra For Free|Jump start with sample apps|Migrate Your Application(s) to Astra|
|:---|:---|:---|
|https://astra.datastax.com/register|https://docs.astra.datastax.com/docs/sample-apps|https://www.datastax.com/blog/2020/07/take-flight-live-and-free-migration-your-apache-cassandra-apps|
---

## Agenda
1. Creating the tables & loading data using [NoSQLBench](docs.nosqlbench.io)
2. Loading data into Astra tables using [DataStax Bulk Loader (aka DSBulk)](https://docs.datastax.com/en/dsbulk/doc/dsbulk/dsbulkAbout.html)
3. Migrating data from one to another Astra table using [DataStax Spark Cassandra Connector](https://docs.datastax.com/en/driver-matrix/doc/driver_matrix/common/driverMatrix.html#driverMatrix__sparkCassandraConnector)
4. Demonstration of using [Storaged Attached Index (SAI)](https://docs.datastax.com/en/storage-attached-index/6.8/sai/saiTOC.html)
5. Working with Astra using REST API
6. Working with Astra using GraphQL API

## Creating tables & loadin data using [NoSQLBench](docs.nosqlbench.io)

### Prerequisites
1. Download and setup [NoSQLBench](https://github.com/nosqlbench/nosqlbench/blob/main/DOWNLOADS.md#getting-nosqlbench)
2. [Astra database created](https://docs.astra.datastax.com/docs/creating-your-astra-database)
3. [Secure connect bundle downloaded for the Astra database](https://docs.astra.datastax.com/docs/obtaining-database-credentials)
4. You Astra database password stored in a file named `passfile.txt`
5. Download the [`astra.yaml` file](astra_playground/astra.yaml) to your local machine

#### Create the tables
Run the below command from a terminal to create the schema in the Astra database,
   ```java
   java -jar nb.jar run driver=cql workload="/path/to/astra.yaml" showcql=true format=readout tags=phase:schema keyspace=<your_astra_keyspace_name> secureconnectbundle="/path/to/secure-connect-<astra-db>.zip" username=<username> passfile=passfile.txt -v --progress console:1s
   ```
#### Load data into the table
Run the below command from a terminal to load data into the `<your_astra_keyspace_name>.users` table,
   ```java
   java -jar nb.jar run driver=cql workload="/path/to/astra.yaml" showcql=true format=readout tags=phase:rampup keyspace=<your_astra_keyspace_name> secureconnectbundle="/path/to/secure-connect-<astra-db>.zip" username=<username> passfile=passfile.txt -v --progress console:1s cycles=100
   ```

## Loading data into Astra `users` table using DataStax Bulk Loader

*Tip*: Refer to [DSBulk references](./datastax_bulk_loader.md) for additional information.

### Prerequisites
1. Download and setup [DSBulk](https://downloads.datastax.com/#bulk-loader)
2. Download the [`astra.csv` file](astra_playground/astra.csv) to your local machine
3. Download the [sample DSBulk configuration file `astra_dsbulk.conf`](astra_playground/astra_dsbulk.conf) and configure with your details

Run the below command from a terminal to load 5 records into the `<your_astra_keyspace_name>.users` table,
   ```java
   ./dsbulk load -f /path/to/astra_dsbulk.conf --log.verbosity 2
   ```

## Migrating data from `users` to `users1` Astra table using [DataStax Spark Cassandra Connector](https://docs.datastax.com/en/driver-matrix/doc/driver_matrix/common/driverMatrix.html#driverMatrix__sparkCassandraConnector)

### Prerequisites
1. [Download](https://spark.apache.org/downloads.html) Source Code of Apache Spark v2.4.6 in your environment and [build](http://spark.apache.org/docs/latest/building-spark.html) it (or) use the pre-built version as you see fit
   
   *Tip*: I had to perform the following,
   ```bash
   wget --no-check-certificate https://mirrors.sonic.net/apache/spark/spark-2.4.6/spark-2.4.6.tgz
   tar -xvf spark-2.4.6.tgz
   ./spark-2.4.6/build/mvn -DskipTests clean package
   ```

### Starting the Spark shell
Run the following command to start the Spark shell

```bash
bin/spark-shell --packages com.datastax.spark:spark-cassandra-connector_2.11:2.5.1 --files /tmp/secure-connect-<astra-db>.zip --conf spark.cassandra.connection.timeoutMS=10000 --conf spark.dse.continuousPagingEnabled=false
```

### Reading data from `<your_astra_keyspace_name>.users` table
Run the following command to being reading data from `<your_astra_keyspace_name>.users` table,
```bash
val df = spark
  .read
  .format("org.apache.spark.sql.cassandra")
  .options(Map(
    "keyspace" -> "<your_astra_keyspace_name>",
    "table" -> "users",
    "spark.cassandra.connection.config.cloud.path" -> "secure-connect-<astra-db>.zip",
    "spark.cassandra.auth.username" -> "<username>",
    "spark.cassandra.auth.password" -> "<password>"
  ))
  .load
```

### Writing data into `<your_astra_keyspace_name>.users1` table
We'll use the dataframe `df` from the earlier step to write the data into the `<your_astra_keyspace_name>.users` table by running the following command,
```bash
df.write
  .format("org.apache.spark.sql.cassandra")
  .options(Map(
    "keyspace" -> "<your_astra_keyspace_name>",
    "table" -> "users1",
    "spark.cassandra.connection.config.cloud.path" -> "secure-connect-<astra-db>.zip",
    "spark.cassandra.auth.username" -> "<username>",
    "spark.cassandra.auth.password" -> "<password>"
  ))
  .save
```

*That's all voila!! ✨ You've successfully migrated the data between two tables in Astra*

### Run a validation to check the counts between the two tables
Run the below command to count the records in the two Astra tables,
```bash
val usersCount = df.count
val users1Count = spark.read.format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "<your_astra_keyspace_name>", "table" -> "users1", "spark.cassandra.connection.config.cloud.path" -> "secure-connect-<astra-db>.zip",  "spark.cassandra.auth.username" -> "<username>", "spark.cassandra.auth.password" -> "<password>")).load.count
```

You should results as below,
```bash
scala> val usersCount = df.count
srcCount: Long = 105

scala> val users1Count = spark.read.format...
users1Count: Long = 105
```

### Resources for further reading,
* [Spark Cassandra Connector Configuration Reference](https://github.com/datastax/spark-cassandra-connector/blob/b2.5/doc/reference.md)
* [Advanced Apache Cassandra&trade; Analytics DataStax Blog](https://www.datastax.com/blog/2020/05/advanced-apache-cassandra-analytics-now-open-all)

## Demonstration of using [Storaged Attached Index (SAI)](https://docs.datastax.com/en/storage-attached-index/6.8/sai/saiTOC.html)
Even though both our Astra tables looks the same, there is one minor difference between them. `users1` table has a SAI index on the `designation` non-primary key column. We can fetch results from `users1` table without leveraging the primary key columns. You can inspect the SAI index by describing the table `DESCRIBE TABLE users1;` CQL command from the CQL Console on the Astra UI and you should see as below,
```bash
CREATE CUSTOM INDEX <your_astra_keyspace_name>_users1_designation_idx ON <your_astra_keyspace_name>.users1 (designation) USING 'StorageAttachedIndex';
```
Let's query `users1` table using this index by running the following,
```bash
SELECT * FROM <your_astra_keyspace_name>.users1 WHERE designation = 'fifty';
```
You should be seeing the result as below,
TODO: Attach screenshot

Now let's use the same exact query but, search for a mixed case value for designation column `Fifty`. Expected out: No records fetched. Why? We purposefully created the SAI index without extra option when we originally created the schema.
```bash
SELECT * FROM <your_astra_keyspace_name>.users1 WHERE designation = 'Fifty';
```
TODO: Attach screenshot

Let's drop the index and recreate with additional options to allow for case insensitive search capability,
```bash
DROP INDEX IF EXISTS <your_astra_keyspace_name>.<your_astra_keyspace_name>_users1_designation_idx;

CREATE CUSTOM INDEX <your_astra_keyspace_name>_users1_designation_idx ON <your_astra_keyspace_name>.users1 (designation) USING 'StorageAttachedIndex' WITH OPTIONS = {'normalize': 'true', 'case_sensitive': 'false'};
```
*Tip:* Refer to [CQL documentation for creating a Storage Attached Index with options](https://docs.datastax.com/en/dse/6.8/cql/cql/cql_reference/cql_commands/cqlCreateCustomIndex.html#cqlCreateCustomIndex__cqlCreateCustomIndexOptions).

Now let's execute the same query and this time around we will see our record back successfully,
```bash
SELECT * FROM <your_astra_keyspace_name>.users1 WHERE designation = 'Fifty';
```
TODO: Attach screenshot 🎓➡️ 🔥⬅️ 👨‍🏫 📅 

## Working with Astra using REST APIs
Lets access Astra using the out-of-the-box REST API that is available for developers.

For this I'll be using simple cURL that is available on my terminal witout having to install any additional software. I'll also go ahead set environment variables to ease with my REST APIs. To simplify, I'll use the same request identifier for all my API queries.

*Tip:* You can use free UUID generator website like [this](https://www.uuidgenerator.net/) or use your own methods.

```bash
export RESTURL="https://{database-id}-{location}.apps.astra.datastax.com/api/rest"
export ASTRAUSR="<username>"
export ASTRAPWD="<password>"
export ASTRAREQID="<generated-UUID>"
export ASTRAKEYSPACE="<your_astra_keyspace_name>"
```

* `database-id` & `location` information can be fetched from Astra console -> Summary tab -> Connection Details
* Use the `username` and `password` that you used when creating the Astra database. *Tip:* You can reset password in case you've forgotten it
* Use the generated UUID to use it for `ASTRAREQID` variable
* Refer to [Astra online documentation](https://docs.astra.datastax.com/docs/getting-started-with-datastax-astra) for more details on REST APIs

### Generating an authorization token
Generating an authorization token is the first step in using REST APIs to work with our Astra database. Run the following to generate one,
```bash
curl -w '\n' --request POST \
  --url "${RESTURL}"/v1/auth \
  --header 'accept: */*' \
  --header 'content-type: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --data '{"username":"'"${ASTRAUSR}"'","password":"'"${ASTRAPWD}"'"}'
```

You'll receive an output similar to below,
```bash
{"authToken":"00922613-8823-4249-8f6a-9bbdabf2d240"}
```

Take the token and export it to a new environment variable,
```bash
export ASTRATKN="<your_authorization_token>"
```

### Fetching existing keyspaces from the database using REST API
To fetch existing keyspaces from our Astra database run the following command,
```bash
curl -w "\n" --request GET \
  --url "${RESTURL}"/v1/keyspaces \
  --header 'accept: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --header "x-cassandra-token: ${ASTRATKN}"
```

### Creating a table in the database using REST API
Here we will create a table named `products` using REST API in our Astra database by running the following command,
```bash
curl -w "\n" --request POST \
  --url ${RESTURL}/v1/keyspaces/${ASTRAKEYSPACE}/tables \
  --header 'accept: */*' \
  --header 'content-type: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --header "x-cassandra-token: ${ASTRATKN}" \
  --data '{"name":"products","ifNotExists":true,"columnDefinitions":
  [ {"name":"id","typeDefinition":"uuid","static":false},
    {"name":"name","typeDefinition":"text","static":false},
    {"name":"description","typeDefinition":"text","static":false},
    {"name":"price","typeDefinition":"decimal","static":false},
    {"name":"created","typeDefinition":"timestamp","static":false}],"primaryKey":
    {"partitionKey":["id"]},"tableOptions":{"defaultTimeToLive":0}}' 
```

You will see an output `{"success":true}` and this command will create `products` table with following schema,
```bash
CREATE TABLE <your_astra_keyspace_name>.products (
    id uuid PRIMARY KEY,
    created timestamp,
    description text,
    name text,
    price decimal
) WITH additional_write_policy = 'NONE'
    AND bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'enabled': 'false'}
    AND crc_check_chance = 1.0
    AND default_time_to_live = 0
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0    AND min_index_interval = 128
    AND nodesync = {'enabled': 'true', 'incremental': 'true'}
    AND read_repair = 'BLOCKING'
    AND speculative_retry = 'NONE';
```

### Adding rows to `products` table using REST API
Let's go ahead and add a row to the newly created `products` table using REST API,
```bash
curl -w '\n' --request POST \
  --url ${RESTURL}/v1/keyspaces/${ASTRAKEYSPACE}/tables/products/rows \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --header "x-cassandra-token: ${ASTRATKN}" \
  --data '{"columns":[
{"name":"id","value":"e9b6c02d-0604-4bab-a3ea-6a7984654631"},
{"name":"name","value":"Heavy Lift Arms"},
{"name":"description","value":"Heavy lift arms capable of lifting 1,250 lbs of weight per arm. Sold as a set."},
{"name":"price","value":"4199.99"},
{"name":"created","value":"2020-07-31 09:48:31.020+0040"}]}'
```

You'll see the output similar to `{"success":true,"rowsModified":1}` and we can see the data in our table,
```bash
<username>@cqlsh:<your_astra_keyspace_name>> SELECT * FROM products;

 id                                   | created                         | description                                                                    | name           | price
--------------------------------------+---------------------------------+--------------------------------------------------------------------------------+-----------------+-------------
 e9b6c02d-0604-4bab-a3ea-6a7984654631 | 2020-07-31 09:08:31.020000+0000 | Heavy lift arms capable of lifting 1,250 lbs of weight per arm. Sold as a set. | Heavy Lift Arms | 4199.990000

(1 rows)
```

### Reterive rows from `products` table using REST API
We will fetch the newly inserted record from the `products` table by running the following command,
```bash
curl -w '\n' --request GET \
  --url ${RESTURL}/v1/keyspaces/${ASTRAKEYSPACE}/tables/products/rows/e9b6c02d-0604-4bab-a3ea-6a7984654631 \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --header "x-cassandra-token: ${ASTRATKN}"
```

You will see output as below,
```bash
{"rows":[{"created":"2020-07-31T09:08:31.02Z","description":"Heavy lift arms capable of lifting 1,250 lbs of weight per arm. Sold as a set.","id":"e9b6c02d-0604-4bab-a3ea-6a7984654631","name":"Heavy Lift Arms","price":"4199.990000"}],"_count":1}
```

## Working with Astra using GraphQL APIs
Lets access Astra using the out-of-the-box REST API that is available for developers. We will continue to use the same authorization token and request ID that we created before.

We will go ahead and set a new environment variable as below,
```bash
export GRAPHQL="https://{database-id}-{location}.apps.astra.datastax.com/api/graphql"
```

### Adding rows to `products` table using GraphQL API
By running the following command we will be adding a row to `products` table,
```bash
curl -w '\n' --request POST \
  --url ${GRAPHQL} \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --header "x-cassandra-token: ${ASTRATKN}" \
  --data-raw '{"query":"mutation {products: insertProducts(value:{id:\"65cad0df-4fc8-42df-90e5-4effcd221ef7\"\n name:\"Arm Spec A1\" description:\"Powerful Robot Arm Spec A.\"price: \"9999.99\" created: \"2020-07-31T18:25:43.511Z\"}){value {name description price created}}}","variables":{}}'
```

### Retrieve rows from `products` table using GraphQL
We will now fetch the record that we inserted by running the following command,
```bash
curl -w '\n' --request POST \
--location ${GRAPHQL} \
--header 'accept: */*'  \
--header 'content-type: application/json' \
--header 'x-cassandra-request-id: ${ASTRAREQID}' \
--header "X-Cassandra-Token: ${ASTRATKN}" \
--header 'Content-Type: application/json' \
--data-raw '{"query":"query {products(value: {id:\"65cad0df-4fc8-42df-90e5-4effcd221ef7\"}) {values {id name price description created}}}"}'
```
You'll see the output as follows,
```bash
{"data":{"products":{"values":[{"created":"2020-07-31T18:25:43.511Z","description":"Powerful Robot Arm Spec A.","id":"65cad0df-4fc8-42df-90e5-4effcd221ef7","name":"Arm Spec A1","price":"9999.99"}]}}}
```

Now run the below query via the CQL Console on the Astra console to view the data,
```bash
<username>@cqlsh:<your_astra_keyspace_name>> SELECT * FROM <your_astra_keyspace_name>.products WHERE id = 65cad0df-4fc8-42df-90e5-4effcd221ef7;
```

### Updating rows in `products` table using GraphQL
Now that we've our record in the table, let's try updating it by running the following command,
```bash
curl -w '\n' --request POST \
  --location ${GRAPHQL} \
  --header 'accept: */*' \
  --header 'content-type: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --header "X-Cassandra-Token: ${ASTRATKN}" \
  --header 'Content-Type: application/json' \
  --data-raw '{"query":"mutation {\n products: updateProducts(value: {\n id:\"65cad0df-4fc8-42df-90e5-4effcd221ef7\"\n name:\"Arm Spec A3 [Newly Updated]\"\n description:\"Powerful Robot Arm Spec A3.\"\n price: \"19999.99\"\n created: \"2012-04-23T18:25:43.511Z\"\n}) {\n value {\n id\n name\n description\n price\n created\n }\n }\n}","variables":{}}'
```

Re-run the select command in the CQL Console to see the newly updated data. We should see the `name` column now have a new value.

### Delete rows from `product` table using GraphQL
We will go ahead and delete the record from `products` table with its primary key `65cad0df-4fc8-42df-90e5-4effcd221ef7` by running the following command,
```bash
curl -w '\n' --location --request POST ${GRAPHQL} \
  --header 'accept: */*' \
  --header 'content-type: application/json' \
  --header 'x-cassandra-request-id: ${ASTRAREQID}' \
  --header "X-Cassandra-Token: ${ASTRATKN}" \
  --header 'Content-Type: application/json' \
  --data-raw '{"query":"mutation {  products: deleteProducts(value: { id:\"65cad0df-4fc8-42df-90e5-4effcd221ef7\" }) {value { id } } }","variables":{}}'
```

---

## Other Astra Tips

### Run the CQL Console in full screen
Today you would access CQL Console from the Astra GUI and here is a little trick to spin that off and view full screen so you can continue to work at something else on the Astra GUI.
```bash
https://{database-id}-{location}.apps.astra.datastax.com/cqlsh
```

### Run the Health console in full screen
To get a full screen health tab, click the "Cycle View Mode" 🖥️ button in the upper right of the Grafana frame on the health tab. Then, press 'esc' to exit Kiosk mode, next, click the "share dashboard" button in the upper right, and copy the URL to the clipboard. Finally, open a new tab with that URL.

### Simple example to showcase different roles
Astra supports managing database access to [implement seperation of duties](https://docs.datastax.com/en/security/6.8/security/grantAuthPer.html#grantAuthPer). Here is a simple example demonstration to create two separate roles, one with read only access and the other that has superior powers. All these things will be done at the CQL Console.

```bash
# Create couple different roles
superadmin@cqlsh:astra1> CREATE ROLE readonly;
superadmin@cqlsh:astra1> CREATE ROLE updater;
superadmin@cqlsh:astra1> CREATE ROLE creator;

# Grant permissions for the roles
superadmin@cqlsh:astra1> GRANT DESCRIBE, SELECT ON KEYSPACE astra1 TO readonly;
superadmin@cqlsh:astra1> GRANT UPDATE ON KEYSPACE astra1 TO updater;
superadmin@cqlsh:astra1> GRANT CREATE ON ALL TABLES IN KEYSPACE astra1 TO creator;

# Create actual users
superadmin@cqlsh:astra1> CREATE ROLE foo WITH LOGIN = true AND PASSWORD = 'foo';
superadmin@cqlsh:astra1> CREATE ROLE bar WITH LOGIN = true AND PASSWORD = 'bar';

# Assign roles to actual users
superadmin@cqlsh:astra1> GRANT readonly TO foo;
superadmin@cqlsh:astra1> GRANT creator TO bar;
superadmin@cqlsh:astra1> GRANT updater TO bar;

# Switch to 'bar'
superadmin@cqlsh:astra1> LOGIN bar;
Password:
bar@cqlsh:astra1> INSERT INTO astra1.table1(a , b) VALUES (2,'2');
bar@cqlsh:astra1>

# Switch to 'foo' and see what can 'foo' do in here,
bar@cqlsh:astra1> LOGIN foo;
Password:
foo@cqlsh:astra> INSERT INTO astra1.table1(a , b) VALUES (1,'1');
Unauthorized: Error from server: code=2100 [Unauthorized] message="User foo has no UPDATE permission on <table astra1.table1> or any of its parents"

foo@cqlsh:astra1> SELECT * FROM astra1.table1;
 a | b
---+---
1  | '1'
(1 rows)
```
This example clearly demonstrates `foo` only has a sub-set of permissions (`SELECT`) of what `bar` had in here (`CREATE` & `UPDATE`).

---