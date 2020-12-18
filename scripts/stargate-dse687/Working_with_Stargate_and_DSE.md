<center><h1> Hooking up Stargate and DataStax Enterprise&trade; </h1></center>
<!--
![alt text](https://github.com/stargate/website/blob/master/assets/images/stargate-profile.png "Stargate")
-->
<center>
<table cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td><img src="https://github.com/stargate/website/blob/master/assets/images/stargate-profile.png" alt="Stargate" width="250"/></td>
    <td> ❤️❤️❤️ </td>
    <td><img src="https://www.datastax.com/sites/default/files/2020-07/Enterprise_Logo.png" alt="DataStax Enterprise" width="250"></td>
  </tr>
</table>
</center>
<!--
<p align="center">
  <img src="https://github.com/stargate/website/blob/master/assets/images/stargate-profile.png" alt="Stargate" width="250" style="vertical-align:middle"/>
   ❤️❤️❤️ 
  <img src="https://www.datastax.com/sites/default/files/2020-07/Enterprise_Logo.png" alt="DataStax Enterprise" width="250" style="vertical-align:middle">
</p>
-->
<!--
|:---:|:---:|:---:|
<img src="https://github.com/stargate/website/blob/master/assets/images/stargate-profile.png" alt="Stargate" width="250" style="vertical-align:middle"/>| ❤️❤️❤️ |<img src="https://www.datastax.com/sites/default/files/2020-07/Enterprise_Logo.png" alt="DataStax Enterprise" width="250" style="vertical-align:middle">
-->

# Stargate
[Stargate](stargate.io) is an open source data gateway that sits between your app and your databases. Stargate brings together an API platform and data request coordination code into one OSS project. Stargate abstracts Cassandra-specific concepts entirely from app developers and supports different API options, removing barriers of entry for new software developers. Watch [this introduction video](https://youtu.be/2ltVf2EscmM) about Stargate to get excited about!

<p align="center"><a href="https://youtu.be/2ltVf2EscmM" target="_blank"><img src="https://github.com/stargate/website/blob/master/assets/images/stargate-profile.png" alt="Super power Stargate" width="240" height="240"/></a></p>

<!--
[![Super power Stargate](https://github.com/stargate/website/blob/master/assets/images/stargate-profile.png)](https://youtu.be/2ltVf2EscmM)
[![Super power Stargate](http://img.youtube.com/vi/2ltVf2EscmM/0.jpg)](https://youtu.be/2ltVf2EscmM)

<iframe width="560" height="315" src="https://www.youtube.com/embed/2ltVf2EscmM" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
-->

Stargate offers the following right out of the box when it got GA'd on December 09, 2020:
1. JSON API: [Save and search schemaless JSON documents](https://stargate.io/docs/stargate/1.0/developers-guide/api_ref/openapi_rest_ref.html)
2. REST API: [Serve a RESTful API from any Cassandra database](https://stargate.io/docs/stargate/1.0/developers-guide/api_ref/openapi_rest_ref.html)
3. GraphQL API: [Serve a GraphQL API from any Cassandra database](https://stargate.io/docs/stargate/1.0/developers-guide/graphql-using.html)
4. CQL API: [Use the native Cassandra Query Language to access your data in Cassandra](https://stargate.io/docs/stargate/1.0/developers-guide/cql-using.html)

**_NOTE_**: This page provides you with information for interacting with DataStax Astra&trade; DevOps & Data APIs to perform a wide variety of operations. This is in continuation to my [first blog covering Getting Started with DataStax Astra&trade; and accessing data using REST & GraphQL APIs](./Working_with_DataStax_Astra.md).

Astra provides the following features out of the box,
* **5 Gig Free-Forever Tier**: Launch a database in the cloud with a few clicks, no credit card required
* **No Operations**: Eliminate the overhead to install, operate, and scale Cassandra
* **Powerful APIs**: Out-of-the-box REST and GraphQL endpoints and browser based CQL shell
* **Cloud-Native**: Powered by our [open-source Kubernetes Operator for Cassandra](https://www.datastax.com/dev/kubernetes)
* **Zero Lock-In**: Deploy on AWS or GCP or Azure and keep compatibility with open-source Cassandra
* **Global Scale**: Put your data where you need it without compromising performance, availability or accessibility

<details>
    <summary>Try Stargate which is available out of the box in DataStax Astra&trade; now!</summary>

    ## Useful Links
    * [Try for free!](https://astra.datastax.com/register)
    * [Migrate your application(s) to Astra](https://www.datastax.com/blog/2020/07/take-flight-live-and-free-migration-your-apache-cassandra-apps)
</details>

---
|Try Astra&trade; For Free|Jump start with sample apps|Migrate Your Application(s) to Astra&trade;|
|:---|:---|:---|
|https://astra.datastax.com/register|https://docs.astra.datastax.com/docs/sample-apps|https://www.datastax.com/blog/2020/07/take-flight-live-and-free-migration-your-apache-cassandra-apps|
---

See all these in a video by clicking [here](https://youtu.be/BWYKQHXfpsg)!

## Agenda
Today's agenda is to demonstrate how to hook up Stargate with DataStax Enterprise&trade; (DSE) using:
* Docker
* Stand-alone

## Running Stargate and DSE&trade; using Docker

### Prerequisites
1. Download the included [`start_stargate_dse687_cluster.sh` script](./start_stargate_dse687_cluster.sh) and [`stargatedse68.yml` Docker compose file](./stargatedse68.yml) to your local machine
2. Grant execute access by running `chmod +x start_stargate_dse687_cluster.sh` command
3. Make sure Docker is running. [Get Docker and install](https://docs.docker.com/get-docker/), as applicable
3. Run `./start_stargate_dse687_cluster.sh`

### Executing

You should notice output as below. Your authToken *will* be different from below output,

   ```java
   $ ./start_stargate_dse687_cluster.sh 
   Starting DSE 6.8.7....
   Creating network "stargate-dse687_backend" with the default driver
   Creating backend-1 ... done
   
   Starting Stargate...
   backend-1 is up-to-date
   Creating stargate ... done
   Sleeping for 60 seconds...
   
   Waiting for Stargate to start up...
   {"authToken":"7fad80fc-c4a0-4054-b367-1eff9b68c22b"}
   Done!
   ```

Your Docker dashboard will appear as below,

   ![alt text](assets/stargate_dse687_docker_dashboard.png "Example Docker dashboard output")

Now we can get into `cqlsh` by executing `docker exec -it backend-1 cqlsh` and you should see output like below,

   ```java
   $ docker exec -it backend-1 cqlsh
   Connected to backend at 127.0.0.1:9042.
   [cqlsh 6.8.0 | DSE 6.8.7 | CQL spec 3.4.5 | DSE protocol v2]
   Use HELP for help.
   cqlsh>
   ```

Other helpful commands to inspect both Stargate (`stargate`) & DSE `6.8.7` (`backend-1`) container logs are,
* `docker-compose --file stargatedse68.yml logs -f stargate`
* `docker-compose --file stargatedse68.yml logs -f backend-1`

You might want to *stop* the Docker containers once the work is completed by executing `docker stop stargate backend-1` as it is resource intensive.

*That's all voila!! ✨ You've successfully hooked up Stargate with DataStax Enterprise&trade; (DSE) using Docker!*

## Running Stargate with a stand-alone tarball installation of DSE&trade;

### Prerequisites
1. Download DSE `6.8.7` tarball either using cURL or from downloads.datastax.com/#enterprise website
2. Download Stargate jars `stargate-jars.zip` from https://github.com/stargate/stargate/releases/download/v1.0.0/stargate-jars.zip

### Executing

Define a custom logging location as applicable or else use the default logging locations,

   ```java
   mkdir -p <path_to>/dse-6.8.7/logs
   ```

Edit the `<path_to>/dse-6.8.7/bin/dse-env.sh` file by adding the following to specify custom logging location or else DSE process will fail to start,

   ```java
   export CASSANDRA_LOG_DIR="<path_to>/dse-6.8.7/logs"
   ```

Make adjustments to `cassandra.yaml` file as applicable to modify the default locations. See [resources section at the bottom of this post](https://github.com/msmygit/dse-titbits/blob/master/scripts/stargate-dse687/Working_with_Stargate_and_DSE.md#resources-for-further-reading) for references. I'm going to change my cluster name (`cluster_name` property value at `cassandra.yaml` file) to `stargate` for this demonstration.

Start up the DSE process by running `<path_to>/dse-6.8.7/bin/dse cassandra` command and wait until you see a *similar* informational logging line,

   ```java
   ...
   INFO  [DSE main thread] 2020-12-11 20:39:35,312  DseDaemon.java:818 - DSE startup complete.
   ```

Verify by running `<path_to>/dse-6.8.7/bin/cqlsh` command which will display an output similar to below,

   ```java
   $ cqlsh
   Connected to stargate at 127.0.0.1:9042.
   [cqlsh 5.0.1 | DSE 6.8.7 | CQL spec 3.4.5 | DSE protocol v2]
   Use HELP for help.
   cqlsh>
   ```

Now, it is time to start the Stargate by running the below command,

   ```java
   stargate-1.1.0-jars/starctl --cluster-name stargate --cluster-seed 127.0.0.1 --dse --dc dc1 --rack rack2 --listen 127.0.0.2 --cluster-version 6.8
   ```

This will break and the startup will fail unless I am able to provision an additional loopback address for running Stargate at `127.0.0.2`.

No sweat 😅. Our Stargate developers from DataStax have provided directions for us to get going. Refer to [resources section at the bottom of this post](https://github.com/msmygit/dse-titbits/blob/master/scripts/stargate-dse687/Working_with_Stargate_and_DSE.md#resources-for-further-reading) on how to achieve that.

I am going to pick the pre-built Docker image route as opposed to adding an additional loopback by following the below steps: 

   ```java
   $ docker pull stargateio/stargate-dse-68:v1.0.0
   v1.0.0: Pulling from stargateio/stargate-dse-68
   Digest: sha256:3fd3d40eccd03ebf9f28934d5cc3340e7fdabddc3ddc052d09b2ced5e30bc64b
   Status: Image is up to date for stargateio/stargate-dse-68:v1.0.0
   docker.io/stargateio/stargate-dse-68:v1.0.0
   ```

Now let's start Stargate by running the following command,

   ```java
   docker run --name stargate1 -d stargateio/stargate-dse-68:v1.0.0 --cluster-name stargate --cluster-seed 127.0.0.1 --cluster-version 6.8 --listen 127.0.0.2 --dse --dc dc1 --rack rack2
   ```

### Resources for further reading,
* [Docker image for Stargate and a DSE 6.8 backend](https://hub.docker.com/r/stargateio/stargate-dse-68)
* [Docker image for DataStax Enterprise&trade;](https://hub.docker.com/r/datastax/dse-server)
* [Installing DataStax Enterprise 6.8 using the binary tarball](https://docs.datastax.com/en/install/6.8/install/installTARdse.html)
* [Starting and Stopping DataStax Enterprise as a stand-alone process](https://docs.datastax.com/en/dse/6.8/dse-admin/datastax_enterprise/operations/startStop/startDseStandalone.html)
* [Changing DSE logging location](https://docs.datastax.com/en/dse/6.8/dse-admin/datastax_enterprise/config/chgLogLocations.html)
* [Default file locations for DSE tarball installation](https://docs.datastax.com/en/install/6.8/install/dseTarLoc.html)
* [Stargate `startctl` command reference](https://stargate.io/docs/stargate/1.0/developers-guide/install/starctl.html)
* [Stargate Dev Guide for running locally](https://github.com/stargate/stargate/blob/master/DEV_GUIDE.md)
* [Stargate Quickstart APIs](https://stargate.io/docs/stargate/1.0/quickstart/quickstart.html)

---