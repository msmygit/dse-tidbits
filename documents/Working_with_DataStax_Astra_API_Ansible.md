<center><h1> Working with DataStax Astra&trade; DevOps & Data APIs with Ansible </h1></center>

# DataStax Astra&trade;
[Astra](https://www.datastax.com/products/datastax-astra) is a Cloud-Native, NoSQL, Scale-Out, Distributed, Hybrid Database-as-a-Service built on Apache Cassandra&trade; brought to you by DataStax&trade;. Astra simplifies cloud-native Cassandra application development. It reduces deployment time from weeks to minutes, removing the biggest obstacle to using Cassandra, which is behind many of the most heavily used applications in the world.

**_NOTE_**: This page provides you with information for interacting with DataStax Astra&trade; DevOps & Data APIs to perform a wide variety of operations. This is in continuation to my [first blog covering Getting Started with DataStax Astra&trade; and accessing data using REST & GraphQL APIs](./Working_with_DataStax_Astra.md).

Astra provides the following features out of the box,
* **5 Gig Free-Forever Tier**: Launch a database in the cloud with a few clicks, no credit card required
* **No Operations**: Eliminate the overhead to install, operate, and scale Cassandra
* **Powerful APIs**: Out-of-the-box REST and GraphQL endpoints and browser based CQL shell
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

## Agenda
Today's agenda is to demonstrate leveraging the Astra&trade; DevOps & Data APIs from Ansible. With just one single playbook, we will be demonstrating the following DevOps & Data operations:
1. Create a token to use the Astra DevOps API endpoints
2. Return supported regions and availability for a given user and org combination
3. Create a new Astra database
4. Find the created Astra database using its ID
5. Obtain the secure connect bundle for the created Astra database using its ID
6. Add a keyspace in the created Astra database
7. Create a token to use the Data API for the Astra database using its ID
8. Return all keyspaces in the Astra database using its ID
9. Add a table in the created Astra database
10. Return all tables in the Astra database using its ID
11. Resize the already created Astra database (this is only supported for non-free tier Astra DBs)
12. Park the already created Astra database
13. Unpark the already parked Astra database

## Running the entire end-to-end operations

### Prerequisites
1. Download and setup [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
2. Create a [Service Account](https://docs.astra.datastax.com/docs/creating-a-new-service-account-for-your-database#create-your-service-account)
3. Copy the [Service Account Credentials](https://docs.astra.datastax.com/docs/creating-a-new-service-account-for-your-database#copying-your-service-account-secret) containing the `clientId`, `clientName` and `clientSecret` information
5. Download the [`astra_api.yml` file](./astra_playground/astra_api.yml) to your Ansible control machine

#### Run the `astra_api.yml` Ansible playbook
Run the below command from the Ansible control machine's terminal to see all the actions being done sequentially. You'll be asked to provide answers for the questions one by one which are required to execute the playbook,
   ```java
   ansible-playbook astra_api.yml
   ```
Optionally, one could pass in the answers via command-line by running the following command. I've left the password to be provided in an interactive mode here,
   ```java
   ansible-playbook astra_api.yml --extra-vars 'clientName=<your_client_name> clientId=<your_client_Id> clientSecret=<your_client_secret> astra_db_name=<name_for_your_db> astra_db_keyspace=<name_for_your_keyspace> astra_db_cloudProvider=<public_cloud_provider_name_AZURE_GCP_AWS> astra_db_tier=<astra_db_tier> astra_db_capacityUnits=<capacity_unit_in_numeric> astra_db_region=<your_astra_db_region> astra_db_username=<your_astra_username> astra_db_tableName=<name_for_the_table>'
   ```
*That's all voila!! ✨ You've successfully performed many operations leveraging Astra&trade; DevOps & Data APIs to spin up a free-tier DB*

You would notice output similar to the below post your playbook has completed successfully. I have shortened both the debug & supported regions information from the below output for brevity. Also, we would have skipped resizing the created Astra&trade; DB here during this run as this operation is not supported with the free tier.
   ```java
   $ ansible-playbook astra_api.yml --extra-vars 'clientName=latest-free-tier clientId=9b906ab8-0f88-4b6d-80d3-67b16730375f clientSecret=f2bab25c-52aa-4f4c-ac52-b4205df2b993 astra_db_name=astra astra_db_keyspace=astra astra_db_cloudProvider=gcp astra_db_tier=developer astra_db_capacityUnits=1 astra_db_region=us-east1 astra_db_username=astra astra_db_keyspaceName=astra astra_db_tableName=astra'
   [WARNING]: provided hosts list is empty, only localhost is available. Note that the implicit localhost does not match 'all'
   Enter your Astra database password?:    
   PLAY [localhost]    ****************************************************************************************************************************************************   ******************************   
   TASK [Create a token to use the Astra DevOps API endpoints]    ***************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print the authentication token for debugging]    ***********************************************************************************************************************************************
   ok: [localhost] => {
       "astra_devops_api_auth_token.json.token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJsYmJUcDRObzdfUHBJLUxOWFZJbUFmY1pWQWs3TFNwdnY5Z0RFVF80M2ZrIn0.eyJqdGkiOiI1NjkyNjg3MS1kZGM2LTRiY2YtOTU4Zi0zNzgzMWE2NDNiYWMiLCJleHAiOjE2MDU1MTU0ODIsIm5iZiI6MCwiaWF0IjoxNjA1NDc5NDgyLCJpc3MiOiJodHRwczovL2F1dGguY2xvdWQuZGF0YXN0YXguY29tL2F1dGgvcmVhbG1zL0Nsb3VkVXNlcnMiLCJhdWQiOiI5OWIwNmFiOC0wZjg4LTRiNmQtODBkMy02N2IxNjczMDM3NWYiLCJzdWIiOiJhMzUyOTVjYi00MWI4LTQxYmItYmIxYS0wMDliM2VkM2ZkZTYiLCJ0eXAiOiJJRCIsImF6cCI6Ijk5YjA2YWI4LTBmODgtNGI2ZC04MGQzLTY3YjE2NzMwMzc1ZiIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjQ4ODI3NWQ3LTAzZGQtNDQzYi1hODg1LTBjMDI4NjZjNzYxZSIsImFjciI6IjEiLCJvcmdOYW1lIjoibGF0ZXN0LWZyZWUtdGllciIsImNsaWVudElkIjoiOTliMDZhYjgtMGY4OC00YjZkLTgwZDMtNjdiMTY3MzAzNzVmIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRIb3N0IjoiMTguMTg5LjI1Mi4xNTIiLCJvcmdFbWFpbCI6Im1hZGhhdmFuXzVrQHlhaG9vLmNvbSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyIyOTlhY2M0MC1mMWFhLTRkNjYtODFlYy0xY2U2NGUxMDIwZTEvU2VydmljZUFjY291bnQiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJvcmciOiIyOTlhY2M0MC1mMWFhLTRkNjYtODFlYy0xY2U2NGUxMDIwZTEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtOTliMDZhYjgtMGY4OC00YjZkLTgwZDMtNjdiMTY3MzAzNzVmIiwiY2xpZW50QWRkcmVzcyI6IjE4LjE4OS4yNTIuMTUyIn0.tQOJdt5dNQLmmO7HdOzFQJn3Gw8rsN-RvohX8xli91JYIDziZSkc6ET9FejzcdSk_L_3ggETy5U1wjsu-LKhV7LCVNatFNnJJ75KP0GGNGLWJo2rIN9MkNVGBGwVMQ9-Rj1QmAFf2Gch8BDS7c1fY09mQCCi-ntScEPLe0a20R5enGQdiC0Q5XHy0zyPKhCip-2GujyeZhNadGBmArYL0UnzpVgdfceC75fFAKboiEWUZQt0R1FDXcrVcKeIcpMOv14JX3KthkwJTAhMsN_5Qen2qoZjt55coMEbMW7JYKNxnM7m_qa9xKli7bZ8jWVoRwNvHbmCKwHAdbJkq-2ztA"
   }   
   TASK [Returns supported regions and availability for a given user and org combination]    ************************************************************************************************************
   ok: [localhost]   
   TASK [Print the supported region and availability for a given user and org combination response for debugging]    ************************************************************************************
   ok: [localhost] => {
       "supported_regions_for_user_org": {
           "changed": false,
           "connection": "Close",
           "content_type": "application/json; charset=UTF-8",
           "cookies": {},
           "cookies_string": "",
           "date": "Sun, 15 Nov 2020 22:31:22 GMT",
           "elapsed": 0,
           "failed": false,
           "json": [
               {
                   "capacityUnitsLimit": 1,
                   "capacityUnitsUsed": 0,
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
                       "costPerNetworkGbCents": 0
                   },
                   "databaseCountLimit": 1,
                   "databaseCountUsed": 0,
                   "defaultStoragePerCapacityUnitGb": 10,
                   "description": "Free tier: Try Astra with no obligation. Get 5 GB of storage, free forever.",
                   "region": "us-east1",
                   "regionContinent": "Moncks Corner, South Carolina",
                   "regionDisplay": "Moncks Corner, South Carolina",
                   "tier": "developer"
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
                       "costPerNetworkGbCents": 3
                   },
                   "databaseCountLimit": 50,
                   "databaseCountUsed": 0,
                   "defaultStoragePerCapacityUnitGb": 10,
                   "description": "3 vCPU, 12GB DRAM, 10GB Storage",
                   "region": "us-east-1",
                   "regionContinent": "US East (N. Virginia)",
                   "regionDisplay": "US East (N. Virginia)",
                   "tier": "A5"
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
                       "costPerNetworkGbCents": 11
                   },
                   "databaseCountLimit": 50,
                   "databaseCountUsed": 0,
                   "defaultStoragePerCapacityUnitGb": 500,
                   "description": "12 vCPU, 48 GB DRAM, 500GB Storage",
                   "region": "westus2",
                   "regionContinent": "West US 2",
                   "regionDisplay": "West US 2",
                   "tier": "C10"
               },
               ...
               {
                   "capacityUnitsLimit": 100,
                   "capacityUnitsUsed": 0,
                   "cloudProvider": "GCP",
                   "cost": {
                       "costPerDayCents": 23664,
                       "costPerDayMRCents": 27216,
                       "costPerDayParkedCents": 4776,
                       "costPerHourCents": 986,
                       "costPerHourMRCents": 1134,
                       "costPerHourParkedCents": 199,
                       "costPerMinCents": 16.43,
                       "costPerMinMRCents": 18.89,
                       "costPerMinParkedCents": 3.31,
                       "costPerMonthCents": 709920,
                       "costPerMonthMRCents": 816480,
                       "costPerMonthParkedCents": 143280,
                       "costPerNetworkGbCents": 3
                   },
                   "databaseCountLimit": 50,
                   "databaseCountUsed": 0,
                   "defaultStoragePerCapacityUnitGb": 1000,
                   "description": "48 vCPU, 192GB DRAM, 1500GB Storage",
                   "region": "europe-north1",
                   "regionContinent": "Hamina, Finland",
                   "regionDisplay": "Hamina, Finland",
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
           "x_envoy_upstream_service_time": "76"
       }
   }   
   TASK [Create a new Astra database]    ****************************************************************************************************************************************************   ************
   ok: [localhost]   
   TASK [Print the create Astra database response for debugging]    *************************************************************************************************************************************
   ok: [localhost] => {
       "create_astra_db_response.location": "https://api.astra.datastax.com/v2/c17bdf46-ebc9-41c9-93ad-357d6e83afbe"
   }   
   TASK [Find the created Astra database using its ID]    ***********************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print the Find the created Astra database using its ID response for debugging]    **************************************************************************************************************
   ok: [localhost] => {
       "find_astra_db_by_id_response": {
           "attempts": 3,
           "changed": false,
           "connection": "Close",
           "content_length": "2503",
           "content_type": "application/json; charset=UTF-8",
           "cookies": {},
           "cookies_string": "",
           "date": "Sun, 15 Nov 2020 22:32:26 GMT",
           "elapsed": 0,
           "failed": false,
           "json": {
               "availableActions": [
                   "park",
                   "getCreds",
                   "resetPassword",
                   "terminate",
                   "addKeyspace",
                   "removeKeyspace",
                   "addTable"
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
                   "costPerNetworkGbCents": 0
               },
               "cqlshUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.apps.astra.datastax.com/cqlsh",
               "creationTime": "2020-11-15T22:31:24Z",
               "dataEndpointUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.apps.astra.datastax.com/api/rest",
               "grafanaUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
               "graphqlUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.apps.astra.datastax.com/api/graphql",
               "id": "c17bdf46-ebc9-41c9-93ad-357d6e83afbe",
               "info": {
                   "capacityUnits": 1,
                   "cloudProvider": "GCP",
                   "datacenters": [
                       {
                           "capacityUnits": 1,
                           "cloudProvider": "GCP",
                           "id": "c17bdf46-ebc9-41c9-93ad-357d6e83afbe",
                           "name": "dc-1",
                           "region": "us-east1",
                           "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20201115%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20201115T223226Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=bb7ba187b01632f8e6ef7f30077a2d9d82ad2f2226246d94fce23c13a8d7af34",
                           "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20201115%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20201115T223226Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=28031e821746ed6fa88a365fcafa232fcbbc599bd03d9e2e898a67ccb35672ac",
                           "tier": "developer"
                       }
                   ],
                   "keyspace": "astra",
                   "keyspaces": [
                       "astra"
                   ],
                   "name": "astra",
                   "region": "us-east1",
                   "tier": "developer",
                   "user": "astra"
               },
               "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
               "ownerId": "a35295cb-41b8-41bb-bb1a-009b3ed3fde6",
               "status": "ACTIVE",
               "storage": {
                   "nodeCount": 1,
                   "replicationFactor": 1,
                   "totalStorage": 5
               },
               "studioUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.studio.astra.datastax.com",
               "terminationTime": "0001-01-01T00:00:00Z"
           },
           "msg": "OK (2503 bytes)",
           "redirected": false,
           "server": "istio-envoy",
           "status": 200,
           "strict_transport_security": "max-age=63072000; include-subdomains",
           "url": "https://api.astra.datastax.com/v2/databases/c17bdf46-ebc9-41c9-93ad-357d6e83afbe",
           "x_envoy_upstream_service_time": "6"
       }
   }   
   TASK [Obtain the secure connect bundle for the created Astra database using its ID]    ***************************************************************************************************************
   ok: [localhost]   
   TASK [Print the obtain the secure connect bundle for the created Astra database response for debugging]    *******************************************************************************************
   ok: [localhost] => {
       "secure_connect_bundle_url": {
           "changed": false,
           "connection": "Close",
           "content_length": "884",
           "content_type": "application/json; charset=UTF-8",
           "cookies": {},
           "cookies_string": "",
           "date": "Sun, 15 Nov 2020 22:32:26 GMT",
           "elapsed": 0,
           "failed": false,
           "json": {
               "downloadURL": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20201115%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20201115T223227Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=4017b9c09c652b71bc61d65b2069f86f70ac8a4b7dfc487e0ba65b99f010d60f",
               "downloadURLInternal": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20201115%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20201115T223227Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=0f805048d400e6706a0aaa012187a62d304fb89d73463072937415fd7629fc61"
           },
           "msg": "OK (884 bytes)",
           "redirected": false,
           "server": "istio-envoy",
           "status": 200,
           "strict_transport_security": "max-age=63072000; include-subdomains",
           "url": "https://api.astra.datastax.com/v2/databases/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/secureBundleURL",
           "x_envoy_upstream_service_time": "37"
       }
   }   
   TASK [Add a keyspace in the created Astra database]    ***********************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print the created Astra database keyspace name response for debugging]    **********************************************************************************************************************
   ok: [localhost] => {
       "create_keyspace_response": {
           "changed": false,
           "connection": "Close",
           "content_length": "0",
           "cookies": {},
           "cookies_string": "",
           "date": "Sun, 15 Nov 2020 22:32:27 GMT",
           "elapsed": 0,
           "failed": false,
           "msg": "OK (0 bytes)",
           "redirected": false,
           "server": "istio-envoy",
           "status": 201,
           "strict_transport_security": "max-age=63072000; include-subdomains",
           "url": "https://api.astra.datastax.com/v2/databases/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/keyspaces/astra",
           "x_envoy_upstream_service_time": "61"
       }
   }   
   TASK [Create a token to use the Data API for the Astra database using its ID]    *********************************************************************************************************************
   ok: [localhost]   
   TASK [Print the create data auth token in Astra database response for debugging]    ******************************************************************************************************************
   ok: [localhost] => {
       "astra_data_api_auth_token": {
           ...
           "date": "Sun, 15 Nov 2020 22:32:30 GMT",
           "elapsed": 2,
           "failed": false,
           "json": {
               "authToken": "..."
           },
           ...
       }
   }   
   TASK [Return all keyspaces in the Astra database using its ID]    ************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print the return all keyspaces in Astra database response for debugging]    ********************************************************************************************************************
   ok: [localhost] => {
       "return_all_keyspaces": {
           ...
           "json": [
               "astra",
               "data_endpoint_auth",
               "datastax_sla",
               "system",
               "system_auth",
               "system_backups",
               "system_distributed",
               "system_schema",
               "system_traces"
           ],
           ...
       }
   }   
   TASK [Add a table in the created Astra database]    **************************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print the created Astra database table name response for debugging]    *************************************************************************************************************************
   ok: [localhost] => {
       "create_table_response": {
           ...
           "date": "Sun, 15 Nov 2020 22:32:35 GMT",
           "elapsed": 3,
           "failed": false,
           "json": {
               "success": true
           },
           ...
       }
   }   
   TASK [Return all tables in the Astra database using its ID]    ***************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print the return all tables response for debugging]    *****************************************************************************************************************************************
   ok: [localhost] => {
       "return_all_tables": {
           ...
           "date": "Sun, 15 Nov 2020 22:32:36 GMT",
           "elapsed": 0,
           "failed": false,
           "json": [
               "astra"
           ],
           ...
       }
   }   
   TASK [Resize the already created Astra database]    **************************************************************************************************************************************************
   skipping: [localhost]   
   TASK [Print resize the already created Astra database response for debugging]    *********************************************************************************************************************
   skipping: [localhost]   
   TASK [Park the already created Astra database]    ****************************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print park the already created Astra database response for debugging]    ***********************************************************************************************************************
   ok: [localhost] => {
       "park_astra_db": {
           ...
       }
   }   
   TASK [Find the parked Astra database using its ID]    ************************************************************************************************************************************************
   ...
   ok: [localhost]   
   TASK [Print the find the parked Astra database using its ID response for debugging]    ***************************************************************************************************************
   ok: [localhost] => {
       "find_parked_astra_db_by_id_response": {
           ...
           "date": "Sun, 15 Nov 2020 22:35:10 GMT",
           "elapsed": 0,
           "failed": false,
           "json": {
               "availableActions": [
                   "unpark",
                   "getCreds",
                   "terminate"
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
                   "costPerNetworkGbCents": 0
               },
               "cqlshUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.apps.astra.datastax.com/cqlsh",
               "creationTime": "2020-11-15T22:31:24Z",
               "dataEndpointUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.apps.astra.datastax.com/api/rest",
               "grafanaUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.dashboard.astra.datastax.com/d/cloud/dse-cluster-condensed?refresh=30s&orgId=1&kiosk=tv",
               "graphqlUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.apps.astra.datastax.com/api/graphql",
               "id": "c17bdf46-ebc9-41c9-93ad-357d6e83afbe",
               "info": {
                   "capacityUnits": 1,
                   "cloudProvider": "GCP",
                   "datacenters": [
                       {
                           "capacityUnits": 1,
                           "cloudProvider": "GCP",
                           "id": "c17bdf46-ebc9-41c9-93ad-357d6e83afbe",
                           "name": "dc-1",
                           "region": "us-east1",
                           "secureBundleInternalUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/secure-connect-internal-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20201115%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20201115T223510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=72f15e92fdc15f8afddb6573add05a62990f1c019f0f6b6df1cf1890cd6499b4",
                           "secureBundleUrl": "https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/c17bdf46-ebc9-41c9-93ad-357d6e83afbe/secure-connect-astra.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20201115%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20201115T223510Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=e1be0edc28af44b392a3d85a44400c62fae3dbe928d48016ad115100133c563f",
                           "tier": "developer"
                       }
                   ],
                   "keyspace": "astra",
                   "keyspaces": [
                       "astra"
                   ],
                   "name": "astra",
                   "region": "us-east1",
                   "tier": "developer",
                   "user": "astra"
               },
               "orgId": "299acc40-f1aa-4d66-81ec-1ce64e1020e1",
               "ownerId": "a35295cb-41b8-41bb-bb1a-009b3ed3fde6",
               "status": "PARKED",
               "storage": {
                   "nodeCount": 1,
                   "replicationFactor": 1,
                   "totalStorage": 5
               },
               "studioUrl": "https://c17bdf46-ebc9-41c9-93ad-357d6e83afbe-us-east1.studio.astra.datastax.com",
               "terminationTime": "0001-01-01T00:00:00Z"
           },
           ...
       }
   }   
   TASK [Unpark the already created Astra database]    **************************************************************************************************************************************************
   ok: [localhost]   
   TASK [Print park the already created Astra database response for debugging]    ***********************************************************************************************************************
   ok: [localhost] => {
       "unpark_astra_db": {
           ...
       }
   }   
   PLAY RECAP    ****************************************************************************************************************************************************   ************************************
   localhost                  : ok=26   changed=0    unreachable=0    failed=0    skipped=2    rescued=0    ignored=0      
   $
   ```

You should be seeing the result as below,

   ![alt text](astra_playground/astra_api_demo_1.png "Example summary page of the created Astra&trade; free tier DB")

   ![alt text](astra_playground/astra_api_demo_2.png "Example CQL Console screen output of the created Astra&trade; keyspace & table")

### Resources for further reading,
* [Using the Astra&trade; Data API Reference](https://docs.astra.datastax.com/reference#datastax-astra-data-api)
* [Using the Astra&trade; DevOps API Reference](https://docs.astra.datastax.com/reference#datastax-astra-devops-api)
* [Supported Astra&trade; DB service tiers](https://docs.astra.datastax.com/docs/service-tier-options)

---