<center><h1> Working with DataStax Astra&trade; DevOps & Data APIs with Ansible </h1></center>

# DataStax Astra&trade;
[Astra](https://www.datastax.com/products/datastax-astra) is a Cloud-Native, NoSQL, Scale-Out, Distributed, Hybrid Database-as-a-Service built on Apache Cassandra&trade; brought to you by DataStax&trade;. Astra simplifies cloud-native Cassandra application development. It reduces deployment time from weeks to minutes, removing the biggest obstacle to using Cassandra, which is behind many of the most heavily used applications in the world.

**_NOTE_**: This page provides you with information for interacting with DataStax Astra&trade; DevOps & Data APIs to perform a wide variety of operations. This is in continuation to my [first blog covering Getting Started with DataStax Astra&trade; and accessing data using REST & GraphQL APIs](./Working_with_DataStax_Astra.md).

Astra provides the following features out of the box,
* **$25 USD/mo Free-Forever Tier**: Launch a serverless database in the cloud with a few clicks, no credit card required. 25.00 USD /monthly = ~ 30 M Writes, 5M reads, 40GB storage (&ast;benchmarked on a N. American GCP DC)
* **No Operations**: Eliminate the overhead to install, operate, and scale Cassandra
* **Powerful APIs**: Out-of-the-box REST, Document and GraphQL endpoints and browser based CQL shell
* **Cloud-Native**: Powered by our [open-source Kubernetes Operator for Cassandra](https://www.datastax.com/dev/kubernetes)
* **Zero Lock-In**: Deploy on AWS or GCP or Azure and keep compatibility with open-source Cassandra
* **Global Scale**: Put your data where you need it without compromising performance, availability or accessibility

<details>
    <summary>Try Astra now!</summary>

    ## Useful Links
    * [Try for free!](https://astra.datastax.com/register)
    * [Migrate your application(s) to Astra](https://www.datastax.com/blog/2020/07/take-flight-live-and-free-migration-your-apache-cassandra-apps)
</details>

---
|Try Astra For Free|Jump start with sample apps|Migrate Your Application(s) to Astra&trade;|
|:---|:---|:---|
|https://astra.datastax.com/register|https://docs.astra.datastax.com/docs/sample-apps|https://www.datastax.com/blog/2020/07/take-flight-live-and-free-migration-your-apache-cassandra-apps|
---

See all these in a video by clicking [here](https://youtu.be/Gi2agdmH8qo)!

## Agenda
Today's agenda is to demonstrate leveraging the Astra&trade; DevOps & Data APIs from Ansible. With just one single playbook, we will be demonstrating the following DevOps & Data operations:
1. Create a token to use the Astra DevOps API endpoints (this is only supported for [classic Astra DBs](https://docs.datastax.com/en/astra/docs/classic-databases.html))
2. Return supported regions and availability for a given user and org combination
3. Create a new Astra database & creates a keyspace on the created DB
4. Find the created Astra database using its ID
5. Obtain the secure connect bundle for the created Astra database using its ID
6. Create a token to use the Data API for the Astra database using its ID
7. Return all keyspaces in the Astra database using its ID
8. Add a table in the created Astra database
9. Return all tables in the Astra database using its ID
10. Resize the already created Astra database (this is only supported for non-free tier [classic Astra DBs](https://docs.datastax.com/en/astra/docs/classic-databases.html))
11. Park the already created Astra database (this is only supported for [classic Astra DBs](https://docs.datastax.com/en/astra/docs/classic-databases.html))
12. Unpark the already parked Astra database (this is only supported for [classic Astra DBs](https://docs.datastax.com/en/astra/docs/classic-databases.html))

## Running the entire end-to-end operations

### Prerequisites
1. Download and setup [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
2. Create an [Application Token](https://docs.datastax.com/en/astra/docs/manage-application-tokens.html). I am going to use `API Admin Svc Account` role here to generate the token.
3. Download the [`astra_api.yml` file](./astra_playground/astra_api.yml) to your Ansible control machine

#### Run the `astra_api.yml` Ansible playbook
Run the below command from the Ansible control machine's terminal to see all the actions being done sequentially. You'll be asked to provide answers for the questions one by one which are required to execute the playbook,
   ```java
   ansible-playbook astra_api.yml
   ```
Optionally, one could pass in the answers via command-line by running the following command.
   ```java
   ansible-playbook astra_api.yml --extra-vars 'astra_devops_api_auth_token=<your_iam_token> astra_db_name=<name_for_your_db> astra_db_keyspace=<name_for_your_keyspace> astra_db_cloudProvider=<public_cloud_provider_name_AZURE_GCP_AWS> astra_db_tier=<astra_db_tier> astra_db_capacityUnits=<capacity_unit_in_numeric> astra_db_region=<your_astra_db_region> astra_db_username=<your_astra_username> astra_db_tableName=<name_for_the_table>'
   ```
*That's all voila!! ✨ You've successfully performed many operations leveraging Astra&trade; DevOps & Data APIs to spin up a free-tier DB*

You would notice output similar to the below post your playbook has completed successfully. I have shortened both the debug & supported regions information from the below output for brevity. Also, we would have skipped resizing the created Astra&trade; DB here during this run as this operation is not supported with the free tier.
   ```java
   $ ansible-playbook astra_api.yml --extra-vars 'astra_devops_api_auth_token=AstraCS:CHANGE:ME astra_db_name=astra astra_db_keyspace=astra astra_db_cloudProvider=GCP astra_db_tier=serverless astra_db_capacityUnits=1 astra_db_region=us-east1 astra_db_username=astra astra_db_tableName=astra'
   [WARNING]: provided hosts list is empty, only localhost is available. Note that the implicit localhost does not match 'all'    
   PLAY [localhost] **************************************************************************************************************************************************************************************************
   META: ran handlers
   
   TASK [Print the authentication token for debugging] ***************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:32
   ok: [localhost] => {
       "astra_devops_api_auth_token": "AstraCS:llafqviyGJuakMHUNmCnwjtY:2af620d8a06421663af77bc07bc506e14f7a6938c31ced53703f70d2274bf5b4"
   }
   
   TASK [Returns supported regions and availability for a given user and org combination] ****************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:37
   <127.0.0.1> ESTABLISH LOCAL CONNECTION FOR USER: ansible
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862462.0482016-12315-237620935213853 `" && echo ansible-tmp-1615862462.0482016-12315-237620935213853="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862462.0482016-12315-237620935213853 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpqsmt1hax TO /home/ansible/.ansible/tmp/ansible-tmp-1615862462.0482016-12315-237620935213853/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862462.0482016-12315-237620935213853/ /home/ansible/.ansible/tmp/ansible-tmp-1615862462.0482016-12315-237620935213853/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862462.0482016-12315-237620935213853/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862462.0482016-12315-237620935213853/ > /dev/null 2>&1 && sleep 0'
   ok: [localhost] => {
       "changed": false,
       "connection": "Close",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:41:03 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/availableRegions",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": [
           {
               "capacityUnitsLimit": 100,
               "capacityUnitsUsed": 1,
               "cloudProvider": "GCP",
               "cost": {
                   "costPerDayCents": 0,
                   "costPerDayMRCents": 0,
                   "costPerDayParkedCents": 0,
                   "costPerHourCents": 0,
                   "costPerHourMRCents": 0,
                   "costPerHourParkedCents": 0,
                   "costPerMinCents": 0,
                   "costPerMinMRCents": 0,
                   "costPerMinParkedCents": 0,
                   "costPerMonthCents": 0,
                   "costPerMonthMRCents": 0,
                   "costPerMonthParkedCents": 0,
                   "costPerNetworkGbCents": 0,
                   "costPerReadGbCents": 0,
                   "costPerWrittenGbCents": 0
               },
               "databaseCountLimit": 50,
               "databaseCountUsed": 1,
               "defaultStoragePerCapacityUnitGb": 10,
               "description": "Free tier: Try Astra with no obligation. Get 5 GB of storage, free forever.",
               "region": "us-east1",
               "regionContinent": "North America",
               "regionDisplay": "Moncks Corner, South Carolina",
               "tier": "developer"
           },
           {
               "capacityUnitsLimit": 100,
               "capacityUnitsUsed": 2,
               "cloudProvider": "GCP",
               "cost": {
                   "costPerDayCents": 0,
                   "costPerDayMRCents": 0,
                   "costPerDayParkedCents": 0,
                   "costPerHourCents": 0,
                   "costPerHourMRCents": 0,
                   "costPerHourParkedCents": 0,
                   "costPerMinCents": 0,
                   "costPerMinMRCents": 0,
                   "costPerMinParkedCents": 0,
                   "costPerMonthCents": 0,
                   "costPerMonthMRCents": 0,
                   "costPerMonthParkedCents": 0,
                   "costPerNetworkGbCents": 0,
                   "costPerReadGbCents": 0.1,
                   "costPerWrittenGbCents": 0.1
               },
               "databaseCountLimit": 50,
               "databaseCountUsed": 2,
               "defaultStoragePerCapacityUnitGb": 500,
               "description": "An elastic database that scales with your workload.",
               "region": "us-east1",
               "regionContinent": "North America",
               "regionDisplay": "US East (South Carolina)",
               "tier": "serverless"
           },
           {
               "capacityUnitsLimit": 100,
               "capacityUnitsUsed": 0,
               "cloudProvider": "AWS",
               "cost": {
                   "costPerDayCents": 192,
                   "costPerDayMRCents": 0,
                   "costPerDayParkedCents": 72,
                   "costPerHourCents": 8,
                   "costPerHourMRCents": 0,
                   "costPerHourParkedCents": 3,
                   "costPerMinCents": 0.13,
                   "costPerMinMRCents": 0,
                   "costPerMinParkedCents": 0.05,
                   "costPerMonthCents": 5760,
                   "costPerMonthMRCents": 0,
                   "costPerMonthParkedCents": 2160,
                   "costPerNetworkGbCents": 3,
                   "costPerReadGbCents": 0,
                   "costPerWrittenGbCents": 0
               },
               "databaseCountLimit": 50,
               "databaseCountUsed": 0,
               "defaultStoragePerCapacityUnitGb": 10,
               "description": "3 vCPU, 12GB DRAM, 10GB Storage",
               "region": "us-east-1",
               "regionContinent": "North America",
               "regionDisplay": "US East (N. Virginia)",
               "tier": "A5"
           },
           {
               "capacityUnitsLimit": 100,
               "capacityUnitsUsed": 0,
               "cloudProvider": "AWS",
               "cost": {
                   "costPerDayCents": 5400,
                   "costPerDayMRCents": 7032,
                   "costPerDayParkedCents": 1992,
                   "costPerHourCents": 225,
                   "costPerHourMRCents": 293,
                   "costPerHourParkedCents": 83,
                   "costPerMinCents": 3.75,
                   "costPerMinMRCents": 4.88,
                   "costPerMinParkedCents": 1.38,
                   "costPerMonthCents": 162000,
                   "costPerMonthMRCents": 210960,
                   "costPerMonthParkedCents": 59760,
                   "costPerNetworkGbCents": 11,
                   "costPerReadGbCents": 0,
                   "costPerWrittenGbCents": 0
               },
               "databaseCountLimit": 50,
               "databaseCountUsed": 0,
               "defaultStoragePerCapacityUnitGb": 500,
               "description": "12 vCPU, 48 GB DRAM, 500GB Storage",
               "region": "ap-southeast-1",
               "regionContinent": "Asia",
               "regionDisplay": "Asia Pacific (Singapore)",
               "tier": "C10"
           },
           {
               "capacityUnitsLimit": 100,
               "capacityUnitsUsed": 0,
               "cloudProvider": "AZURE",
               "cost": {
                   "costPerDayCents": 5400,
                   "costPerDayMRCents": 6216,
                   "costPerDayParkedCents": 1992,
                   "costPerHourCents": 225,
                   "costPerHourMRCents": 259,
                   "costPerHourParkedCents": 83,
                   "costPerMinCents": 3.75,
                   "costPerMinMRCents": 4.31,
                   "costPerMinParkedCents": 1.38,
                   "costPerMonthCents": 162000,
                   "costPerMonthMRCents": 186480,
                   "costPerMonthParkedCents": 59760,
                   "costPerNetworkGbCents": 11,
                   "costPerReadGbCents": 0,
                   "costPerWrittenGbCents": 0
               },
               "databaseCountLimit": 50,
               "databaseCountUsed": 0,
               "defaultStoragePerCapacityUnitGb": 500,
               "description": "12 vCPU, 48 GB DRAM, 500GB Storage",
               "region": "eastus",
               "regionContinent": "North America",
               "regionDisplay": "East US",
               "tier": "C10"
           },
           {
               "capacityUnitsLimit": 100,
               "capacityUnitsUsed": 0,
               "cloudProvider": "AZURE",
               "cost": {
                   "costPerDayCents": 30768,
                   "costPerDayMRCents": 27216,
                   "costPerDayParkedCents": 6144,
                   "costPerHourCents": 1282,
                   "costPerHourMRCents": 1134,
                   "costPerHourParkedCents": 256,
                   "costPerMinCents": 21.36,
                   "costPerMinMRCents": 18.89,
                   "costPerMinParkedCents": 4.26,
                   "costPerMonthCents": 923040,
                   "costPerMonthMRCents": 816480,
                   "costPerMonthParkedCents": 184320,
                   "costPerNetworkGbCents": 11,
                   "costPerReadGbCents": 0,
                   "costPerWrittenGbCents": 0
               },
               "databaseCountLimit": 50,
               "databaseCountUsed": 0,
               "defaultStoragePerCapacityUnitGb": 1000,
               "description": "48 vCPU, 192GB DRAM, 1500GB Storage",
               "region": "northeurope",
               "regionContinent": "Europe",
               "regionDisplay": "North Europe",
               "tier": "D40"
           }
       ],
       "msg": "OK (unknown bytes)",
       "redirected": false,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "transfer_encoding": "chunked",
       "url": "https://api.astra.datastax.com/v2/availableRegions",
       "x_envoy_upstream_service_time": "101"
   }
   
   TASK [Print the supported region and availability for a given user and org combination response for debugging] ****************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:51
   ok: [localhost] => {
       "supported_regions_for_user_org": {
           "changed": false,
           "connection": "Close",
           "content_type": "application/json; charset=UTF-8",
           "cookies": {},
           "cookies_string": "",
           "date": "Tue, 16 Nov 2021 02:41:03 GMT",
           "elapsed": 0,
           "failed": false,
           "json": [
               {
                   "capacityUnitsLimit": 100,
                   "capacityUnitsUsed": 1,
                   "cloudProvider": "GCP",
                   "cost": {
                       "costPerDayCents": 0,
                       "costPerDayMRCents": 0,
                       "costPerDayParkedCents": 0,
                       "costPerHourCents": 0,
                       "costPerHourMRCents": 0,
                       "costPerHourParkedCents": 0,
                       "costPerMinCents": 0,
                       "costPerMinMRCents": 0,
                       "costPerMinParkedCents": 0,
                       "costPerMonthCents": 0,
                       "costPerMonthMRCents": 0,
                       "costPerMonthParkedCents": 0,
                       "costPerNetworkGbCents": 0,
                       "costPerReadGbCents": 0,
                       "costPerWrittenGbCents": 0
                   },
                   "databaseCountLimit": 50,
                   "databaseCountUsed": 1,
                   "defaultStoragePerCapacityUnitGb": 10,
                   "description": "Free tier: Try Astra with no obligation. Get 5 GB of storage, free forever.",
                   "region": "us-east1",
                   "regionContinent": "North America",
                   "regionDisplay": "Moncks Corner, South Carolina",
                   "tier": "developer"
               },
               {
                   "capacityUnitsLimit": 100,
                   "capacityUnitsUsed": 0,
                   "cloudProvider": "AZURE",
                   "cost": {
                       "costPerDayCents": 30768,
                       "costPerDayMRCents": 27216,
                       "costPerDayParkedCents": 6144,
                       "costPerHourCents": 1282,
                       "costPerHourMRCents": 1134,
                       "costPerHourParkedCents": 256,
                       "costPerMinCents": 21.36,
                       "costPerMinMRCents": 18.89,
                       "costPerMinParkedCents": 4.26,
                       "costPerMonthCents": 923040,
                       "costPerMonthMRCents": 816480,
                       "costPerMonthParkedCents": 184320,
                       "costPerNetworkGbCents": 11,
                       "costPerReadGbCents": 0,
                       "costPerWrittenGbCents": 0
                   },
                   "databaseCountLimit": 50,
                   "databaseCountUsed": 0,
                   "defaultStoragePerCapacityUnitGb": 1000,
                   "description": "48 vCPU, 192GB DRAM, 1500GB Storage",
                   "region": "northeurope",
                   "regionContinent": "Europe",
                   "regionDisplay": "North Europe",
                   "tier": "D40"
               }
           ],
           "msg": "OK (unknown bytes)",
           "redirected": false,
           "server": "istio-envoy",
           "status": 200,
           "strict_transport_security": "max-age=63072000; include-subdomains",
           "transfer_encoding": "chunked",
           "url": "https://api.astra.datastax.com/v2/availableRegions",
           "x_envoy_upstream_service_time": "101"
       }
   }
   
   TASK [Create a new Astra database] ********************************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:56
   <127.0.0.1> ESTABLISH LOCAL CONNECTION FOR USER: ansible
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862463.6773598-12347-216446744691173 `" && echo ansible-tmp-1615862463.6773598-12347-216446744691173="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862463.6773598-12347-216446744691173 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpzu3h02t0 TO /home/ansible/.ansible/tmp/ansible-tmp-1615862463.6773598-12347-216446744691173/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862463.6773598-12347-216446744691173/ /home/ansible/.ansible/tmp/ansible-tmp-1615862463.6773598-12347-216446744691173/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862463.6773598-12347-216446744691173/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862463.6773598-12347-216446744691173/ > /dev/null 2>&1 && sleep 0'
   ok: [localhost] => {
       "changed": false,
       "connection": "Close",
       "content_length": "0",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:41:04 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": {
                   "capacityUnits": 1,
                   "cloudProvider": "AWS",
                   "keyspace": "astra",
                   "name": "astra",
                   "region": "us-east-1",
                   "tier": "serverless"
               },
               "body_format": "json",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "POST",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "201"
               ],
               "timeout": 60,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "location": "https://api.astra.datastax.com/v2/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "msg": "OK (0 bytes)",
       "redirected": false,
       "server": "istio-envoy",
       "status": 201,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases",
       "x_envoy_upstream_service_time": "722"
   }
   
   TASK [Print the create Astra database response for debugging] *****************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:80
   ok: [localhost] => {
       "create_astra_db_response.location": "https://api.astra.datastax.com/v2/84be8c7b-5241-4eb1-99d0-2414d7b5908f"
   }
   
   TASK [Find the created Astra database using its ID] ***************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:85
   <127.0.0.1> ESTABLISH LOCAL CONNECTION FOR USER: ansible
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862465.0487792-12378-227713809236666 `" && echo ansible-tmp-1615862465.0487792-12378-227713809236666="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862465.0487792-12378-227713809236666 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpz_ixk216 TO /home/ansible/.ansible/tmp/ansible-tmp-1615862465.0487792-12378-227713809236666/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862465.0487792-12378-227713809236666/ /home/ansible/.ansible/tmp/ansible-tmp-1615862465.0487792-12378-227713809236666/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862465.0487792-12378-227713809236666/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862465.0487792-12378-227713809236666/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (100 retries left).Result was: {
       "attempts": 1,
       "changed": false,
       "connection": "Close",
       "content_length": "1876",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:41:05 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "pending",
                       "secureBundleMigrationProxyInternalUrl": "pending",
                       "secureBundleMigrationProxyUrl": "pending",
                       "secureBundleUrl": "pending",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "PENDING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (1876 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "18"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862495.5911498-12378-62209845564881 `" && echo ansible-tmp-1615862495.5911498-12378-62209845564881="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862495.5911498-12378-62209845564881 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmp9lmarubu TO /home/ansible/.ansible/tmp/ansible-tmp-1615862495.5911498-12378-62209845564881/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862495.5911498-12378-62209845564881/ /home/ansible/.ansible/tmp/ansible-tmp-1615862495.5911498-12378-62209845564881/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862495.5911498-12378-62209845564881/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862495.5911498-12378-62209845564881/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (99 retries left).Result was: {
       "attempts": 2,
       "changed": false,
       "connection": "Close",
       "content_length": "1881",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:41:35 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "pending",
                       "secureBundleMigrationProxyInternalUrl": "pending",
                       "secureBundleMigrationProxyUrl": "pending",
                       "secureBundleUrl": "pending",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "INITIALIZING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (1881 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "23"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862526.0510643-12378-90019488300863 `" && echo ansible-tmp-1615862526.0510643-12378-90019488300863="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862526.0510643-12378-90019488300863 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmp99ccdwmn TO /home/ansible/.ansible/tmp/ansible-tmp-1615862526.0510643-12378-90019488300863/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862526.0510643-12378-90019488300863/ /home/ansible/.ansible/tmp/ansible-tmp-1615862526.0510643-12378-90019488300863/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862526.0510643-12378-90019488300863/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862526.0510643-12378-90019488300863/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (98 retries left).Result was: {
       "attempts": 3,
       "changed": false,
       "connection": "Close",
       "content_length": "1881",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:42:06 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "pending",
                       "secureBundleMigrationProxyInternalUrl": "pending",
                       "secureBundleMigrationProxyUrl": "pending",
                       "secureBundleUrl": "pending",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "INITIALIZING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (1881 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "17"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862556.5543232-12378-120217430349100 `" && echo ansible-tmp-1615862556.5543232-12378-120217430349100="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862556.5543232-12378-120217430349100 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpra6cckqk TO /home/ansible/.ansible/tmp/ansible-tmp-1615862556.5543232-12378-120217430349100/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862556.5543232-12378-120217430349100/ /home/ansible/.ansible/tmp/ansible-tmp-1615862556.5543232-12378-120217430349100/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862556.5543232-12378-120217430349100/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862556.5543232-12378-120217430349100/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (97 retries left).Result was: {
       "attempts": 4,
       "changed": false,
       "connection": "Close",
       "content_length": "3551",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:42:36 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024236Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=75f20092db56a1329d7c2b64e95f41609da8e8b7aa143b2208858cbc72934773",
                       "secureBundleMigrationProxyInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024236Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=d694a76a1c9f617cc8d1f2200098d2dbbc02ce367518b4647ff3cc84316d75e1",
                       "secureBundleMigrationProxyUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024236Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=5d6394a72444419441213d6d1c5990dc0c2a91a7fecf72649126dc2ebaa18e24",
                       "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024236Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=ba5a824a5114b9c497abc67a24649fa6614c07961e18538af354cc59c99c4b7a",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "INITIALIZING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (3551 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "20"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862587.0020266-12378-195157918612417 `" && echo ansible-tmp-1615862587.0020266-12378-195157918612417="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862587.0020266-12378-195157918612417 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpn88dcf3s TO /home/ansible/.ansible/tmp/ansible-tmp-1615862587.0020266-12378-195157918612417/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862587.0020266-12378-195157918612417/ /home/ansible/.ansible/tmp/ansible-tmp-1615862587.0020266-12378-195157918612417/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862587.0020266-12378-195157918612417/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862587.0020266-12378-195157918612417/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (96 retries left).Result was: {
       "attempts": 5,
       "changed": false,
       "connection": "Close",
       "content_length": "3551",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:43:07 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024307Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=aa16fdf6cc86a54ec7de4bca6c98d2c043ee76d55e6b564bd5ada01c393ef047",
                       "secureBundleMigrationProxyInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024307Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=df80e3ebb94e072549852b43818ff5d28d4ee86ebbbd21b6597df312f2c92452",
                       "secureBundleMigrationProxyUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024307Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=0a30fe52a5d9465da5ef7a6f499618d943c511bddb8b47b9364cbeb0860d374d",
                       "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024307Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=e3cf54c5ebaf5e61fd042f918a8d748d034241d53b9112daba80c305a6570279",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "INITIALIZING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (3551 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "20"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862617.506969-12378-127115193295382 `" && echo ansible-tmp-1615862617.506969-12378-127115193295382="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862617.506969-12378-127115193295382 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmp95f2v6ea TO /home/ansible/.ansible/tmp/ansible-tmp-1615862617.506969-12378-127115193295382/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862617.506969-12378-127115193295382/ /home/ansible/.ansible/tmp/ansible-tmp-1615862617.506969-12378-127115193295382/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862617.506969-12378-127115193295382/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862617.506969-12378-127115193295382/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (95 retries left).Result was: {
       "attempts": 6,
       "changed": false,
       "connection": "Close",
       "content_length": "3551",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:43:37 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024337Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=1a2361e53f38f2c0ecb988db5167b77fe8c9e4dfb389f1895cea1b626c151f64",
                       "secureBundleMigrationProxyInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024337Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=466a13b10423b00973ac891865833ff58663cf313ce48ec394b785020432b382",
                       "secureBundleMigrationProxyUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024337Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=52523b2a0a6b9b016c25a309185a26396cd1944436917a4aad082c47818a6075",
                       "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024337Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=4ca887eb5a9951e5dfb4a245f6243849f59bbe1db9106d0baf21ca5ba139702f",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "INITIALIZING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (3551 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "20"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862647.948856-12378-71352178387322 `" && echo ansible-tmp-1615862647.948856-12378-71352178387322="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862647.948856-12378-71352178387322 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpl0ddszv5 TO /home/ansible/.ansible/tmp/ansible-tmp-1615862647.948856-12378-71352178387322/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862647.948856-12378-71352178387322/ /home/ansible/.ansible/tmp/ansible-tmp-1615862647.948856-12378-71352178387322/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862647.948856-12378-71352178387322/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862647.948856-12378-71352178387322/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (94 retries left).Result was: {
       "attempts": 7,
       "changed": false,
       "connection": "Close",
       "content_length": "3551",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:44:08 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024408Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=f3dd58b838a9c359dd1f7935fa8cf8f545ca3a31801f756b30c098585c66edde",
                       "secureBundleMigrationProxyInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024408Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=7bf06812efe85ed5d9a6b24b7b804d27548baffe54a80bdabcca771e271d2b92",
                       "secureBundleMigrationProxyUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024408Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=ce05c42742221dfefd2b9bc4d17e35a41ff70604d28669e0ec3861dcf4ad1948",
                       "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024408Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=fc50fd2bdbf5d7e2b3b85cbfd6b981ab6f0fc3cf50878954f4c6b9de2493bd42",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "INITIALIZING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (3551 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "16"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862678.7953622-12378-239521781191306 `" && echo ansible-tmp-1615862678.7953622-12378-239521781191306="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862678.7953622-12378-239521781191306 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpux3zbu6a TO /home/ansible/.ansible/tmp/ansible-tmp-1615862678.7953622-12378-239521781191306/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862678.7953622-12378-239521781191306/ /home/ansible/.ansible/tmp/ansible-tmp-1615862678.7953622-12378-239521781191306/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862678.7953622-12378-239521781191306/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862678.7953622-12378-239521781191306/ > /dev/null 2>&1 && sleep 0'
   FAILED - RETRYING: Find the created Astra database using its ID (93 retries left).Result was: {
       "attempts": 8,
       "changed": false,
       "connection": "Close",
       "content_length": "3551",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:44:39 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024439Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=90d6df01e79fa2ee0a0ca9e85707e56d71a19acd3e46dbae4a485f734f10ca34",
                       "secureBundleMigrationProxyInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024439Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=4bdee8414c35bc9361d3b998d266e29bbfa871269447fdf2292c7157ebeb79ac",
                       "secureBundleMigrationProxyUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024439Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=fb090f450002127b48d38d4256ce4bed010e6de0b5fed595916a1fddb617e149",
                       "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024439Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=24261b38064eebcfbcdfea5c4436b7b338ee91019e3561d00c0240584641fd6a",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "INITIALIZING",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (3551 bytes)",
       "redirected": false,
       "retries": 101,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "20"
   }
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862709.2874615-12378-167780263095559 `" && echo ansible-tmp-1615862709.2874615-12378-167780263095559="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862709.2874615-12378-167780263095559 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmplml1d6nc TO /home/ansible/.ansible/tmp/ansible-tmp-1615862709.2874615-12378-167780263095559/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862709.2874615-12378-167780263095559/ /home/ansible/.ansible/tmp/ansible-tmp-1615862709.2874615-12378-167780263095559/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862709.2874615-12378-167780263095559/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862709.2874615-12378-167780263095559/ > /dev/null 2>&1 && sleep 0'
   ok: [localhost] => {
       "attempts": 9,
       "changed": false,
       "connection": "Close",
       "content_length": "3613",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:45:09 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "availableActions": [
               "getCreds",
               "terminate",
               "addKeyspace",
               "suspend"
           ],
           "cost": {
               "costPerDayCents": 0,
               "costPerDayMRCents": 0,
               "costPerDayParkedCents": 0,
               "costPerHourCents": 0,
               "costPerHourMRCents": 0,
               "costPerHourParkedCents": 0,
               "costPerMinCents": 0,
               "costPerMinMRCents": 0,
               "costPerMinParkedCents": 0,
               "costPerMonthCents": 0,
               "costPerMonthMRCents": 0,
               "costPerMonthParkedCents": 0,
               "costPerNetworkGbCents": 0,
               "costPerReadGbCents": 0.1,
               "costPerWrittenGbCents": 0.1
           },
           "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
           "creationTime": "2021-03-16T02:41:04Z",
           "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
           "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
           "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "info": {
               "capacityUnits": 1,
               "cloudProvider": "AWS",
               "datacenters": [
                   {
                       "capacityUnits": 1,
                       "cloudProvider": "AWS",
                       "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                       "name": "dc-1",
                       "region": "us-east-1",
                       "regionClassification": "standard",
                       "regionZone": "na",
                       "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=d9743afc8a0b7f1ee8dc7a143fd5f83bb173a545289ceb66fbb8e290f350e8a6",
                       "secureBundleMigrationProxyInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=a94209c8527dd0f69ba74c6633d86e34d5bd797419620dae8de53551981291f1",
                       "secureBundleMigrationProxyUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=05eb5bdebd120cf772a70aa29e74aa5ca8ba6aad0fd07a02205c30ecd916747f",
                       "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=2e287628ac2c39399852ea2c7999be830452cfee4d76242bec29d6358ad4284e",
                       "tier": "serverless"
                   }
               ],
               "keyspace": "astra",
               "keyspaces": [
                   "astra"
               ],
               "name": "astra",
               "region": "us-east-1",
               "tier": "serverless"
           },
           "metrics": {
               "errorsTotalCount": 0,
               "liveDataSizeBytes": 0,
               "readRequestsTotalCount": 0,
               "writeRequestsTotalCount": 0
           },
           "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
           "ownerId": "llafqviyGJuakMHUNmCnwjtY",
           "status": "ACTIVE",
           "storage": {
               "nodeCount": 3,
               "replicationFactor": 1,
               "totalStorage": 5,
               "usedStorage": 2
           },
           "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
           "terminationTime": "0001-01-01T00:00:00Z"
       },
       "msg": "OK (3613 bytes)",
       "redirected": false,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
       "x_envoy_upstream_service_time": "19"
   }
   
   TASK [Print the Find the created Astra database using its ID response for debugging] ******************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:104
   ok: [localhost] => {
       "find_astra_db_by_id_response": {
           "attempts": 9,
           "changed": false,
           "connection": "Close",
           "content_length": "3613",
           "content_type": "application/json; charset=UTF-8",
           "cookies": {},
           "cookies_string": "",
           "date": "Tue, 16 Nov 2021 02:45:09 GMT",
           "elapsed": 0,
           "failed": false,
           "json": {
               "availableActions": [
                   "getCreds",
                   "terminate",
                   "addKeyspace",
                   "suspend"
               ],
               "cost": {
                   "costPerDayCents": 0,
                   "costPerDayMRCents": 0,
                   "costPerDayParkedCents": 0,
                   "costPerHourCents": 0,
                   "costPerHourMRCents": 0,
                   "costPerHourParkedCents": 0,
                   "costPerMinCents": 0,
                   "costPerMinMRCents": 0,
                   "costPerMinParkedCents": 0,
                   "costPerMonthCents": 0,
                   "costPerMonthMRCents": 0,
                   "costPerMonthParkedCents": 0,
                   "costPerNetworkGbCents": 0,
                   "costPerReadGbCents": 0.1,
                   "costPerWrittenGbCents": 0.1
               },
               "cqlshUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/cqlsh",
               "creationTime": "2021-03-16T02:41:04Z",
               "dataEndpointUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest",
               "grafanaUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
               "graphqlUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/graphql",
               "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
               "info": {
                   "capacityUnits": 1,
                   "cloudProvider": "AWS",
                   "datacenters": [
                       {
                           "capacityUnits": 1,
                           "cloudProvider": "AWS",
                           "id": "84be8c7b-5241-4eb1-99d0-2414d7b5908f",
                           "name": "dc-1",
                           "region": "us-east-1",
                           "regionClassification": "standard",
                           "regionZone": "na",
                           "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=d9743afc8a0b7f1ee8dc7a143fd5f83bb173a545289ceb66fbb8e290f350e8a6",
                           "secureBundleMigrationProxyInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=a94209c8527dd0f69ba74c6633d86e34d5bd797419620dae8de53551981291f1",
                           "secureBundleMigrationProxyUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=05eb5bdebd120cf772a70aa29e74aa5ca8ba6aad0fd07a02205c30ecd916747f",
                           "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024509Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=2e287628ac2c39399852ea2c7999be830452cfee4d76242bec29d6358ad4284e",
                           "tier": "serverless"
                       }
                   ],
                   "keyspace": "astra",
                   "keyspaces": [
                       "astra"
                   ],
                   "name": "astra",
                   "region": "us-east-1",
                   "tier": "serverless"
               },
               "metrics": {
                   "errorsTotalCount": 0,
                   "liveDataSizeBytes": 0,
                   "readRequestsTotalCount": 0,
                   "writeRequestsTotalCount": 0
               },
               "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
               "ownerId": "llafqviyGJuakMHUNmCnwjtY",
               "status": "ACTIVE",
               "storage": {
                   "nodeCount": 3,
                   "replicationFactor": 1,
                   "totalStorage": 5,
                   "usedStorage": 2
               },
               "studioUrl": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.studio.astra.datastax.com",
               "terminationTime": "0001-01-01T00:00:00Z"
           },
           "msg": "OK (3613 bytes)",
           "redirected": false,
           "server": "istio-envoy",
           "status": 200,
           "strict_transport_security": "max-age=63072000; include-subdomains",
           "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f",
           "x_envoy_upstream_service_time": "19"
       }
   }
   
   TASK [Obtain the secure connect bundle for the created Astra database using its ID] *******************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:109
   <127.0.0.1> ESTABLISH LOCAL CONNECTION FOR USER: ansible
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862709.886404-12668-146287622496375 `" && echo ansible-tmp-1615862709.886404-12668-146287622496375="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862709.886404-12668-146287622496375 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpbomtv_ky TO /home/ansible/.ansible/tmp/ansible-tmp-1615862709.886404-12668-146287622496375/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862709.886404-12668-146287622496375/ /home/ansible/.ansible/tmp/ansible-tmp-1615862709.886404-12668-146287622496375/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862709.886404-12668-146287622496375/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862709.886404-12668-146287622496375/ > /dev/null 2>&1 && sleep 0'
   ok: [localhost] => {
       "changed": false,
       "connection": "Close",
       "content_length": "1811",
       "content_type": "application/json; charset=UTF-8",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:45:10 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Authorization": "Bearer CHANGE_ME",
                   "Content-Type": "application/json"
               },
               "http_agent": "ansible-httpget",
               "method": "POST",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secureBundleURL",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "downloadURL": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=3575a25b299c083aa29e003824f93c13f2989bbd2bcf74f0ad56266055cd65de",
           "downloadURLInternal": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=48bf632e04c94902e262b8dff883a98f8c08981fcde9a2c63b2b513cddbc9595",
           "downloadURLMigrationProxy": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=3f11c35897e1d9ca1cf98c3b52506022a5b8cf5882eb3f75b21bff3703e5feb7",
           "downloadURLMigrationProxyInternal": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=ad717250221f98596082c15460f4931dc1e5cf9bec41713e95c96213f07577fa"
       },
       "msg": "OK (1811 bytes)",
       "redirected": false,
       "server": "istio-envoy",
       "status": 200,
       "strict_transport_security": "max-age=63072000; include-subdomains",
       "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secureBundleURL",
       "x_envoy_upstream_service_time": "19"
   }
   
   TASK [Print the obtain the secure connect bundle for the created Astra database response for debugging] ***********************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:125
   ok: [localhost] => {
       "secure_connect_bundle_url": {
           "changed": false,
           "connection": "Close",
           "content_length": "1811",
           "content_type": "application/json; charset=UTF-8",
           "cookies": {},
           "cookies_string": "",
           "date": "Tue, 16 Nov 2021 02:45:10 GMT",
           "elapsed": 0,
           "failed": false,
           "json": {
               "downloadURL": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=3575a25b299c083aa29e003824f93c13f2989bbd2bcf74f0ad56266055cd65de",
               "downloadURLInternal": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=48bf632e04c94902e262b8dff883a98f8c08981fcde9a2c63b2b513cddbc9595",
               "downloadURLMigrationProxy": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=3f11c35897e1d9ca1cf98c3b52506022a5b8cf5882eb3f75b21bff3703e5feb7",
               "downloadURLMigrationProxyInternal": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secure-connect-proxy-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20210316%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20210316T024510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=ad717250221f98596082c15460f4931dc1e5cf9bec41713e95c96213f07577fa"
           },
           "msg": "OK (1811 bytes)",
           "redirected": false,
           "server": "istio-envoy",
           "status": 200,
           "strict_transport_security": "max-age=63072000; include-subdomains",
           "url": "https://api.astra.datastax.com/v2/databases/84be8c7b-5241-4eb1-99d0-2414d7b5908f/secureBundleURL",
           "x_envoy_upstream_service_time": "19"
       }
   }
   
   TASK [Return all keyspaces in the Astra database using its ID] ****************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:130
   <127.0.0.1> ESTABLISH LOCAL CONNECTION FOR USER: ansible
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862710.521295-12699-224197554951227 `" && echo ansible-tmp-1615862710.521295-12699-224197554951227="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862710.521295-12699-224197554951227 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpjad3rxw9 TO /home/ansible/.ansible/tmp/ansible-tmp-1615862710.521295-12699-224197554951227/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862710.521295-12699-224197554951227/ /home/ansible/.ansible/tmp/ansible-tmp-1615862710.521295-12699-224197554951227/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862710.521295-12699-224197554951227/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862710.521295-12699-224197554951227/ > /dev/null 2>&1 && sleep 0'
   ok: [localhost] => {
       "changed": false,
       "connection": "close",
       "content_length": "365",
       "content_type": "application/json",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:45:11 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Content-Type": "application/json",
                   "X-Cassandra-Token": "AstraCS:llafqviyGJuakMHUNmCnwjtY:2af620d8a06421663af77bc07bc506e14f7a6938c31ced53703f70d2274bf5b4"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v1/keyspaces",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": [
           "37333334333565612d336261662d343830612d383664662d313562306634313937393466_astra2",
           "37333334333565612d336261662d343830612d383664662d313562306634313937393466_data_endpoint_auth",
           "37333334333565612d336261662d343830612d383664662d313562306634313937393466_datastax_sla",
           "astra",
           "data_endpoint_auth",
           "datastax_sla",
           "system",
           "system_auth",
           "system_schema",
           "system_traces"
       ],
       "msg": "OK (365 bytes)",
       "redirected": false,
       "server": "nginx/1.19.7",
       "status": 200,
       "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v1/keyspaces",
       "vary": "Accept-Encoding"
   }
   
   TASK [Print the return all keyspaces in Astra database response for debugging] ************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:146
   ok: [localhost] => {
       "return_all_keyspaces": {
           "changed": false,
           "connection": "close",
           "content_length": "365",
           "content_type": "application/json",
           "cookies": {},
           "cookies_string": "",
           "date": "Tue, 16 Nov 2021 02:45:11 GMT",
           "elapsed": 0,
           "failed": false,
           "json": [
               "37333334333565612d336261662d343830612d383664662d313562306634313937393466_astra2",
               "37333334333565612d336261662d343830612d383664662d313562306634313937393466_data_endpoint_auth",
               "37333334333565612d336261662d343830612d383664662d313562306634313937393466_datastax_sla",
               "astra",
               "data_endpoint_auth",
               "datastax_sla",
               "system",
               "system_auth",
               "system_schema",
               "system_traces"
           ],
           "msg": "OK (365 bytes)",
           "redirected": false,
           "server": "nginx/1.19.7",
           "status": 200,
           "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v1/keyspaces",
           "vary": "Accept-Encoding"
       }
   }
   
   TASK [Add a table in the created Astra database] ******************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:155
   <127.0.0.1> ESTABLISH LOCAL CONNECTION FOR USER: ansible
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862711.1620762-12730-61242887509202 `" && echo ansible-tmp-1615862711.1620762-12730-61242887509202="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862711.1620762-12730-61242887509202 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpkz38f6ip TO /home/ansible/.ansible/tmp/ansible-tmp-1615862711.1620762-12730-61242887509202/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862711.1620762-12730-61242887509202/ /home/ansible/.ansible/tmp/ansible-tmp-1615862711.1620762-12730-61242887509202/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862711.1620762-12730-61242887509202/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862711.1620762-12730-61242887509202/ > /dev/null 2>&1 && sleep 0'
   ok: [localhost] => {
       "changed": false,
       "connection": "close",
       "content_length": "17",
       "content_type": "application/json",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:45:12 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": {
                   "columnDefinitions": [
                       {
                           "name": "pk1",
                           "static": false,
                           "typeDefinition": "int"
                       },
                       {
                           "name": "pk2",
                           "static": false,
                           "typeDefinition": "text"
                       },
                       {
                           "name": "ck1",
                           "static": false,
                           "typeDefinition": "double"
                       },
                       {
                           "name": "ck2",
                           "static": false,
                           "typeDefinition": "date"
                       },
                       {
                           "name": "c1",
                           "static": false,
                           "typeDefinition": "timeuuid"
                       }
                   ],
                   "ifNotExists": true,
                   "name": "astra",
                   "primaryKey": {
                       "clusteringKey": [
                           "ck1",
                           "ck2"
                       ],
                       "partitionKey": [
                           "pk1",
                           "pk2"
                       ]
                   },
                   "tableOptions": {
                       "clusteringExpression": [
                           {
                               "column": "ck1",
                               "order": "ASC"
                           },
                           {
                               "column": "ck2",
                               "order": "DESC"
                           }
                       ],
                       "defaultTimeToLive": 0
                   }
               },
               "body_format": "json",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Content-Type": "application/json",
                   "X-Cassandra-Token": "AstraCS:llafqviyGJuakMHUNmCnwjtY:2af620d8a06421663af77bc07bc506e14f7a6938c31ced53703f70d2274bf5b4"
               },
               "http_agent": "ansible-httpget",
               "method": "POST",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "201"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v2/schemas/keyspaces/astra/tables",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "name": "astra"
       },
       "msg": "OK (17 bytes)",
       "redirected": false,
       "server": "nginx/1.19.7",
       "status": 201,
       "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v2/schemas/keyspaces/astra/tables"
   }
   
   TASK [Print the created Astra database table name response for debugging] *****************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:180
   ok: [localhost] => {
       "create_table_response": {
           "changed": false,
           "connection": "close",
           "content_length": "17",
           "content_type": "application/json",
           "cookies": {},
           "cookies_string": "",
           "date": "Tue, 16 Nov 2021 02:45:12 GMT",
           "elapsed": 0,
           "failed": false,
           "json": {
               "name": "astra"
           },
           "msg": "OK (17 bytes)",
           "redirected": false,
           "server": "nginx/1.19.7",
           "status": 201,
           "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v2/schemas/keyspaces/astra/tables"
       }
   }
   
   TASK [Return all tables in the Astra database using its ID] *******************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:185
   <127.0.0.1> ESTABLISH LOCAL CONNECTION FOR USER: ansible
   <127.0.0.1> EXEC /bin/sh -c 'echo ~ansible && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '( umask 77 && mkdir -p "` echo /home/ansible/.ansible/tmp `"&& mkdir "` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862712.3793921-12761-185389435951155 `" && echo ansible-tmp-1615862712.3793921-12761-185389435951155="` echo /home/ansible/.ansible/tmp/ansible-tmp-1615862712.3793921-12761-185389435951155 `" ) && sleep 0'
   Using module file /home/ansible/.local/lib/python3.6/site-packages/ansible/modules/net_tools/basics/uri.py
   <127.0.0.1> PUT /home/ansible/.ansible/tmp/ansible-local-12307lv7yr_6c/tmpq8rfev8c TO /home/ansible/.ansible/tmp/ansible-tmp-1615862712.3793921-12761-185389435951155/AnsiballZ_uri.py
   <127.0.0.1> EXEC /bin/sh -c 'chmod u+x /home/ansible/.ansible/tmp/ansible-tmp-1615862712.3793921-12761-185389435951155/ /home/ansible/.ansible/tmp/ansible-tmp-1615862712.3793921-12761-185389435951155/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c '/usr/bin/python3 /home/ansible/.ansible/tmp/ansible-tmp-1615862712.3793921-12761-185389435951155/AnsiballZ_uri.py && sleep 0'
   <127.0.0.1> EXEC /bin/sh -c 'rm -f -r /home/ansible/.ansible/tmp/ansible-tmp-1615862712.3793921-12761-185389435951155/ > /dev/null 2>&1 && sleep 0'
   ok: [localhost] => {
       "changed": false,
       "connection": "close",
       "content_length": "548",
       "content_type": "application/json",
       "cookies": {},
       "cookies_string": "",
       "date": "Tue, 16 Nov 2021 02:45:12 GMT",
       "elapsed": 0,
       "invocation": {
           "module_args": {
               "attributes": null,
               "backup": null,
               "body": null,
               "body_format": "raw",
               "client_cert": null,
               "client_key": null,
               "content": null,
               "creates": null,
               "delimiter": null,
               "dest": null,
               "directory_mode": null,
               "follow": false,
               "follow_redirects": "safe",
               "force": false,
               "force_basic_auth": false,
               "group": null,
               "headers": {
                   "Accept": "application/json",
                   "Content-Type": "application/json",
                   "X-Cassandra-Token": "AstraCS:llafqviyGJuakMHUNmCnwjtY:2af620d8a06421663af77bc07bc506e14f7a6938c31ced53703f70d2274bf5b4"
               },
               "http_agent": "ansible-httpget",
               "method": "GET",
               "mode": null,
               "owner": null,
               "regexp": null,
               "remote_src": null,
               "removes": null,
               "return_content": false,
               "selevel": null,
               "serole": null,
               "setype": null,
               "seuser": null,
               "src": null,
               "status_code": [
                   "200"
               ],
               "timeout": 30,
               "unix_socket": null,
               "unsafe_writes": null,
               "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v2/schemas/keyspaces/astra/tables",
               "url_password": null,
               "url_username": null,
               "use_proxy": true,
               "validate_certs": true
           }
       },
       "json": {
           "data": [
               {
                   "columnDefinitions": [
                       {
                           "name": "pk1",
                           "static": false,
                           "typeDefinition": "int"
                       },
                       {
                           "name": "pk2",
                           "static": false,
                           "typeDefinition": "varchar"
                       },
                       {
                           "name": "ck1",
                           "static": false,
                           "typeDefinition": "double"
                       },
                       {
                           "name": "ck2",
                           "static": false,
                           "typeDefinition": "date"
                       },
                       {
                           "name": "c1",
                           "static": false,
                           "typeDefinition": "timeuuid"
                       }
                   ],
                   "keyspace": "astra",
                   "name": "astra",
                   "primaryKey": {
                       "clusteringKey": [
                           "ck1",
                           "ck2"
                       ],
                       "partitionKey": [
                           "pk1",
                           "pk2"
                       ]
                   },
                   "tableOptions": {
                       "clusteringExpression": [
                           {
                               "column": "ck1",
                               "order": "ASC"
                           },
                           {
                               "column": "ck2",
                               "order": "DESC"
                           }
                       ],
                       "defaultTimeToLive": 0
                   }
               }
           ]
       },
       "msg": "OK (548 bytes)",
       "redirected": false,
       "server": "nginx/1.19.7",
       "status": 200,
       "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v2/schemas/keyspaces/astra/tables",
       "vary": "Accept-Encoding"
   }
   
   TASK [Print the return all tables response for debugging] *********************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:202
   ok: [localhost] => {
       "return_all_tables": {
           "changed": false,
           "connection": "close",
           "content_length": "548",
           "content_type": "application/json",
           "cookies": {},
           "cookies_string": "",
           "date": "Tue, 16 Nov 2021 02:45:12 GMT",
           "elapsed": 0,
           "failed": false,
           "json": {
               "data": [
                   {
                       "columnDefinitions": [
                           {
                               "name": "pk1",
                               "static": false,
                               "typeDefinition": "int"
                           },
                           {
                               "name": "pk2",
                               "static": false,
                               "typeDefinition": "varchar"
                           },
                           {
                               "name": "ck1",
                               "static": false,
                               "typeDefinition": "double"
                           },
                           {
                               "name": "ck2",
                               "static": false,
                               "typeDefinition": "date"
                           },
                           {
                               "name": "c1",
                               "static": false,
                               "typeDefinition": "timeuuid"
                           }
                       ],
                       "keyspace": "astra",
                       "name": "astra",
                       "primaryKey": {
                           "clusteringKey": [
                               "ck1",
                               "ck2"
                           ],
                           "partitionKey": [
                               "pk1",
                               "pk2"
                           ]
                       },
                       "tableOptions": {
                           "clusteringExpression": [
                               {
                                   "column": "ck1",
                                   "order": "ASC"
                               },
                               {
                                   "column": "ck2",
                                   "order": "DESC"
                               }
                           ],
                           "defaultTimeToLive": 0
                       }
                   }
               ]
           },
           "msg": "OK (548 bytes)",
           "redirected": false,
           "server": "nginx/1.19.7",
           "status": 200,
           "url": "https://84be8c7b-5241-4eb1-99d0-2414d7b5908f-us-east-1.apps.astra.datastax.com/api/rest/v2/schemas/keyspaces/astra/tables",
           "vary": "Accept-Encoding"
       }
   }
   
   TASK [Resize the already created Astra database] ******************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:207
   skipping: [localhost] => {
       "changed": false,
       "skip_reason": "Conditional result was False"
   }
   
   TASK [Print resize the already created Astra database response for debugging] *************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:230
   ok: [localhost] => {
       "resize_astra_db": {
           "changed": false,
           "skip_reason": "Conditional result was False",
           "skipped": true
       }
   }
   
   TASK [Park the already created Astra database] ********************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:236
   skipping: [localhost] => {
       "changed": false,
       "skip_reason": "Conditional result was False"
   }
   
   TASK [Print park the already created Astra database response for debugging] ***************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:254
   ok: [localhost] => {
       "park_astra_db": {
           "changed": false,
           "skip_reason": "Conditional result was False",
           "skipped": true
       }
   }
   
   TASK [Find the parked Astra database using its ID] ****************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:259
   skipping: [localhost] => {
       "changed": false,
       "skip_reason": "Conditional result was False"
   }
   
   TASK [Print the find the parked Astra database using its ID response for debugging] *******************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:280
   ok: [localhost] => {
       "find_parked_astra_db_by_id_response": {
           "changed": false,
           "skip_reason": "Conditional result was False",
           "skipped": true
       }
   }
   
   TASK [Unpark the already created Astra database] ******************************************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:285
   skipping: [localhost] => {
       "changed": false,
       "skip_reason": "Conditional result was False"
   }
   
   TASK [Print park the already created Astra database response for debugging] ***************************************************************************************************************************************
   task path: /home/ansible/astra_api/astra_api.yml:303
   ok: [localhost] => {
       "unpark_astra_db": {
           "changed": false,
           "skip_reason": "Conditional result was False",
           "skipped": true
       }
   }
   META: ran handlers
   META: ran handlers
   
   PLAY RECAP ********************************************************************************************************************************************************************************************************
   localhost                  : ok=19   changed=0    unreachable=0    failed=0    skipped=4    rescued=0    ignored=0      
   $
   ```

You should be seeing the result as below,

   ![alt text](astra_playground/astra_api_demo_1.png "Example summary page of the created Astra&trade; free tier DB")

   ![alt text](astra_playground/astra_api_demo_2.png "Example CQL Console screen output of the created Astra&trade; keyspace & table")

### Resources for further reading,
* [Using the Astra&trade; DevOps & Data APIs Reference](https://docs.astra.datastax.com/reference#datastax-astra-data-api)
* [Supported Astra&trade; DB service tiers](https://docs.astra.datastax.com/docs/service-tier-options)

---