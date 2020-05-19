<center> <h1>DataStax Environment Setup - DataStax Enterprise </h1></center>

# Root Access Directions
Tarball installs are more involved as they require many steps that are usually automated by the packaged versions. This includes setting up data directories, creating user accounts, and installing init scripts. One nice feature is that they enable installations of multiple versions of DSE side by side. `6.0.12` version is used as an example in this document.

1. Installation location
   
   ```bash
   mkdir /opt/datastax
   ```

2. Download & extract DSE tarball
   
   ```bash
   mkdir /opt/datastax/repo
   cd /opt/datastax/repo
   curl --user DSA_email_address:downloads_key --location --remote-name https://downloads.datastax.com/enterprise/dse-6.0.12-bin.tar.gz
   cd /opt/datastax
   tar -xvzpf /opt/datastax/repo/dse-6.0.12-bin.tar.gz
   ```
   **Note**: Passing `--user` parameters is now optional
   
3. Symlink to `dse` version - helper symlink should multiple versions be downloaded later
   
   ```bash
   ln -s /opt/datastax/dse-6.0.12 /opt/datastax/dse
   ```

4. User account
   
   ```bash
   useradd --home /opt/datastax --user-group --create-home --system --shell /bin/bash cassandra
   ```

5. Configure Disks
   
   1. Create a GPT partition taking up the entire disk
   2. Format data devices with XFS filesystem
      
      ```bash
      mkfs.xfs -s size=4096 /dev/sdb1
      mkfs.xfs -s size=4096 /dev/sdc1
      ```
      
   3. Mount at the appropriate location /{dvl,int}/frt/data{0,1}
      
      ```bash
      tail /etc/fstab
      /dev/sdb1               /dvl/frt/data0          xfs     defaults 0 0
      /dev/sdc1               /dvl/frt/data1          xfs     defaults 0 0
      ```

6. Data directory creation and permissions
   
   ```bash
   mkdir -p /dvl/frt/data0/cassandra/{data,commitlog,saved_caches,hints,cdc_raw}
   mkdir -p /dvl/frt/data0/log/{cassandra,spark}
   
   mkdir -p /dvl/frt/data1/spark/{rdd,worker}
   mkdir -p /dvl/frt/data1/dsefs
   mkdir -p /dvl/frt/data1/solr

   mkdir -p /opt/datastax/log/cassandra
   ```
   
7. Permissions
   
   ```bash
   chown -Rv cassandra:cassandra /opt/datastax
   chmod -Rv 0755 /opt/datastax
   
   chown -Rv cassandra:cassandra /dvl/frt/data{0,1}
   chmod -Rv 0755 /dvl/frt/data{0,1}/
   ```
   
8. Tune kernel to match [recommended production settings](https://docs.datastax.com/en/dse/6.0/dse-admin/datastax_enterprise/config/configRecommendedSettings.html)

9. Setup symlink for init script
   
   ```bash
   ln -s /opt/datastax/dse/bin/dse.init /etc/init.d/dse
   ```

10. Setup sudo entries for managing dse service (start, stop, restart, status)
   
   **/etc/sudoers.d/dse** - Create this file with permissions 0400
   ```bash
   # Setup a command alias for DSE services
   Cmnd_Alias DSE_SERVICES = /sbin/service dse status, /sbin/service dse start, /sbin/service dse stop, /sbin/service dse restart, /sbin/systemctl start dse.service, /sbin/systemctl stop dse.service, /sbin/systemctl status dse.service, /sbin/systemctl restart dse.service

   # Define rules for the cassandra user
   cassandra ALL= NOPASSWD: DSE_SERVICES
   ```

11. Make path for PID file and set ownership to `cassandra` user
   
   ```bash
   mkdir /var/run/dse
   chown cassandra:cassandra /var/run/dse
   ```

# Configure DSE
1. Edit the init script
   
   **`bin/dse.init`** _ADD & CHANGE THE FOLLOWING LINE_
   ```bash
   export JAVA_HOME=<your_java_home>
   DSE_DEFAULT="/opt/datastax/dse/resources/dse/conf/dse.default"
   ```

2. Edit the `dse-env.sh` script
   
   **`bin/dse-env.sh`** _ADD THE FOLLOWING LINE_
   ```bash
   export JAVA_HOME=<your_java_home>
   ```

3. Edit DSE configuration files

   **`resources/dse/conf/dse.default`** file - _for the sake of brevity we are only going to list options that require changes_
   
   ```bash
   # NOTICE: See also /etc/dse/cassandra/cassandra-env.sh

   # EXTRA_CLASSPATH provides the means to extend Cassandra's classpath with
   # additional libraries.  It is formatted as a colon-delimited list of
   # class directories and/or jar files.  For example, to enable the
   # JMX-to-web bridge install libmx4j-java and uncomment the following.
   #EXTRA_CLASSPATH="/usr/share/java/mx4j-tools.jar"

   # Enable the DSE Graph service on this node
   GRAPH_ENABLED=0

   # Start the node in DSE Search mode
   SOLR_ENABLED=1

   # Start the node in Spark mode
   SPARK_ENABLED=1

   # Determine install root location (applicable for standalone installer or
   # Debian/RedHat packages; not used in tarball installs)
   if [ -d /usr/share/dse ]; then
       export DSE_HOME=/usr/share/dse
   fi
   # Set this if you've installed to a location different from the default
   DSE_HOME=/opt/datastax/dse

   # Location of log output
   OUTPUT_FILE="/opt/datastax/log/cassandra/output.log"

   # Configuration directory
   CASSANDRA_CONF=$DSE_HOME/resources/cassandra/conf
   CASSANDRA_ENV_FILE=$CASSANDRA_CONF/cassandra-env.sh

   # Set the PID file location here
   PIDFILE=/var/run/$NODEID/$NODEID.pid

   # Where do Hadoop log files go? This will override the default
   #HADOOP_LOG_DIR=

   # Where do Tomcat log files go? This will override the default
   #TOMCAT_LOGS=

   # The user to use for the service
   CASSANDRA_USER=cassandra

   # The group to use for the service
   CASSANDRA_GROUP=cassandra

   # Set this if you've installed DSE into a different location from the default
   # (Note: this refers to DSE Spark, not open-source Spark)
   SPARK_HOME=/opt/datastax/dse/resources/spark

   # Spark configuration files location
   SPARK_CONF_DIR=$SPARK_HOME/conf

   # Uncomment and increase/decrease if you want longer/shorter waits checking if the service is up
   # WAIT_FOR_START=3
   # Uncomment and decrease/increase if you want finer/coarser grained waits while checking if the service is up
   # WAIT_FOR_START_SLEEP=5
   ```
   
   **`resources/dse/conf/dse.yaml`** - _for the sake of brevity we are only going to list options that require changes_
   ```yaml
   # Memory limit for DSE In-Memory tables as a maximum in MB.  When not set,
   # max_memory_to_lock_fraction is used.  The max_memory_to_lock_fraction
   # value is ignored if max_memory_to_lock_mb is set to a non-zero value.
   # Specify max_memory_to_lock_fraction or max_memory_to_lock_mb, not both.
   max_memory_to_lock_mb: 64
   
   ##########################
   # Authentication options
   #
   # These options are used if the authenticator option in cassandra.yaml is set to
   # com.datastax.bdp.cassandra.auth.DseAuthenticator
   authentication_options:
     enabled: true
     default_scheme: internal
   #     other_schemes:
   #     scheme_permissions: false
   #     allow_digest_with_kerberos: true
   #     plain_text_without_ssl: warn
   #     transitional_mode: disabled
   
   ##########################
   # Role management options
   #
   # These options are used when the role_manager option in cassandra.yaml is set to
   # com.datastax.bdp.cassandra.auth.DseRoleManager
   #
   # mode can be one of:
   #   internal - the granting and revoking of roles is managed internally
   #              using the GRANT ROLE and REVOKE ROLE statements
   #   ldap - the granting and revoking of roles is managed by an external
   #          LDAP server configured using the ldap_options.
   role_management_options:
     mode: internal
   
   ##########################
   # Authorization options
   #
   # These options are used if the authorization option in cassandra.yaml is set to
   # com.datastax.bdp.cassandra.auth.DseAuthorizer
   #
   # The enabled option controls whether the DseAuthorizer will perform authorization. If
   # set to true authorization is performed, if set to false it is not.
   # When not set, enabled is false.
   #
   # The transitional_mode option allows the DseAuthorizer to operate in a transitional
   # mode during setup of authorization in a cluster. This can be one of the following values:
   #   disabled   - transitional mode is disabled, all connections must provide valid credentials and
   #                map to a login-enabled role
   #   normal     - allow all connections that provide credentials, permissions can be granted to
   #                resources but are not enforced
   #   strict     - permissions can be granted to resources and are enforced on
   #                authenticated users. They are not enforced against anonymous
   #                users
   #
   # allow_row_level_security - To use row level security, set to true for the entire system.
   #                            Use the same setting on all nodes.
   authorization_options:
     enabled: true
     transitional_mode: disabled
     allow_row_level_security: false
   
   # Controls the physical resources that can be used by Spark applications on this node.
   # cores_total is the number of cores and and memory_total is total system memory that you can assign to all executors
   # that are run by the work pools on this node. The values can be absolute (exact number of cores) or the
   # memory size (use metric suffixes like M for mega, and G for giga) or a fraction of physical cores reported by the OS,
   # and fraction of available memory, where available memory is calculated as: total physical memory - DSE max heap size.
   # cores_total and memory_total replace initial_spark_worker_resources option which was used in earlier DSE versions.
   # The default 0.7 for cores and memory corresponds to the default value of initial_spark_worker_resources 0.7.
   # DSE does not support setting Spark Worker cores and memory through environment variables SPARK_WORKER_CORES
   # and SPARK_WORKER_MEMORY.
   resource_manager_options:
     worker_options:
       cores_total: 8
       memory_total: 32G
        
       workpools:
         - name: alwayson_sql
           cores: 2
           memory: 8G
   ##########################
   # # The directory to store search index data. Each DSE Search index is stored under
   # # a solrconfig_data_dir/keyspace.table directory.
   # # Default is a solr.data directory inside Cassandra data directory, or as specified
   # # by the dse.solr.data.dir system property.
   solr_data_dir: /dvl/frt/data1/solr
   ##########################
   # DSE File System (DSEFS) options
   dsefs_options:
     enabled:
     work_dir: /dvl/frt/data1/dsefs/work
     data_directories:
       - dir: /dvl/frt/data1/dsefs/data
         storage_weight: 1.0
          min_free_space: 104857600
   ```
   
4. Edit Cassandra configuration files
   
   Note: Update all references to `localhost` and `127.0.0.1` with the node's local IP.
   
   **`resources/cassandra/conf/cassandra.yaml`** - _for the sake of brevity we are only going to list options that require changes_
   ```yaml
   # The name of the cluster. This is mainly used to prevent machines in
   # one logical cluster from joining another.
   cluster_name: "DVL_FORT"
   
   # The number of tokens randomly assigned to this node on the ring.
   # The higher the token count is relative to other nodes, the larger the proportion of data
   # that this node will store. You probably want all nodes to have the same number
   # of tokens assuming they have equal hardware capability.
   #
   # If not set, the default value is 1 token for backward compatibility
   # and will use the initial_token as described below.
   #
   # Specifying initial_token will override this setting on the node's initial start.
   # On subsequent starts, this setting will apply even if initial token is set.
   #
   # If you already have a cluster with 1 token per node, and want to migrate to
   # multiple tokens per node, see http://wiki.apache.org/cassandra/Operations
   num_tokens: 8
   
   # Triggers automatic allocation of num_tokens tokens for this node. The allocation
   # algorithm attempts to choose tokens in a way that optimizes replicated load over
   # the nodes in the datacenter for the specified DC-level replication factor.
   #
   # The load assigned to each node will be close to proportional to its number of
   # vnodes.
   #
   # Supported only with the Murmur3Partitioner.
   allocate_tokens_for_local_replication_factor: 3
   
   # Directory to store hints.
   # If not set, the default directory is $DSE_HOME/data/hints.
   hints_directory: /dvl/frt/data0/cassandra/hints
   
   # Directories where the database should store data on disk. The data
   # is spread evenly across the directories, subject to the granularity of
   # the configured compaction strategy.
   # If not set, the default directory is $DSE_HOME/data/data.
   data_file_directories:
     - /dvl/frt/data0/cassandra/data
   
   # Commit log directory. When running on magnetic HDD, this directory should be on a
   # separate spindle than the data directories.
   # If not set, the default directory is $DSE_HOME/data/commitlog.
   commitlog_directory: /dvl/frt/data0/cassandra/commitlog
   
   # CommitLogSegments are moved to this directory on flush if cdc_enabled: true and the
   # segment contains mutations for a CDC-enabled table. This directory should be placed on a
   # separate spindle than the data directories. If not set, the default directory is
   # $DSE_HOME/data/cdc_raw.
   cdc_raw_directory: /dvl/frt/data0/cassandra/cdc_raw
   
   # Saved caches directory.
   # If not set, the default directory is $DSE_HOME/data/saved_caches
   saved_caches_directory: /dvl/frt/data0/cassandra/saved_caches
   
   # Any class that implements the SeedProvider interface and has a
   # constructor that takes a Map<String, String> of parameters is valid.
   seed_provider:
       # Addresses of hosts that are deemed contact points.
       # Database nodes use this list of hosts to find each other and learn
       # the topology of the ring. You _must_ change this if you are running
       # multiple nodes!
       - class_name: org.apache.cassandra.locator.SimpleSeedProvider
         parameters:
             # seeds is actually a comma-delimited list of addresses.
             # Ex: "<ip1>,<ip2>,<ip3>"
             - seeds: "127.0.0.1"
   
   # The strategy for optimizing disk read.
   # Possible values are:
   # ssd (for solid state disks, the default). When not set, the default is ssd.
   # spinning (for spinning disks)
   disk_optimization_strategy: ssd
   
   # Address or interface to bind to and tell other nodes to connect to.
   # You _must_ change this address or interface to enable multiple nodes to communicate!
   #
   # Set listen_address OR listen_interface, not both.
   #
   # When not set (blank), InetAddress.getLocalHost() is used. This
   # will always do the Right Thing _if_ the node is properly configured
   # (hostname, name resolution, etc), and the Right Thing is to use the
   # address associated with the hostname (it might not be).
   #
   # Setting listen_address to 0.0.0.0 is always wrong.
   #
   listen_address: localhost
   
   # The address or interface to bind the native transport server to.
   #
   # Set native_transport_address OR native_transport_interface, not both.
   #
   # Leaving native_transport_address blank has the same effect as on listen_address
   # (i.e. it will be based on the configured hostname of the node).
   #
   # Note that unlike listen_address, you can specify 0.0.0.0, but you must also
   # set native_transport_broadcast_address to a value other than 0.0.0.0.
   #
   # For security reasons, you should not expose this port to the internet.  Firewall it if needed.
   native_transport_address: localhost

   # endpoint_snitch -- A class that implements the IEndpointSnitch interface. The database uses the
   # snitch to locate nodes and route requests. Use only snitch implementations that are bundled with DSE.
   #
   # THE DATABASE WILL NOT ALLOW YOU TO SWITCH TO AN INCOMPATIBLE SNITCH
   # AFTER DATA IS INSERTED INTO THE CLUSTER.  This would cause data loss.
   # This means that if you start with the default SimpleSnitch, which
   # locates every node on "rack1" in "datacenter1", your only options
   # if you need to add another datacenter are GossipingPropertyFileSnitch
   # (and the older PFS).  From there, if you want to migrate to an
   # incompatible snitch like Ec2Snitch you can do it by adding new nodes
   # under Ec2Snitch (which will locate them in a new "datacenter") and
   # decommissioning the old nodes.
   endpoint_snitch: GossipingPropertyFileSnitch
   ```
   
   **`resources/cassandra/conf/jvm.options`** - _for the sake of brevity we are only going to list options that require changes_
   ```bash
   # Heap size is automatically calculated by cassandra-env based on this
   # formula: max(min(1/2 ram, 1024MB), min(1/4 ram, 8GB))
   # That is:
   # - calculate 1/2 ram and cap to 1024MB
   # - calculate 1/4 ram and cap to 8192MB
   # - pick the max
   #
   # For production use you may wish to adjust this for your environment.
   # If that's the case, uncomment the -Xmx and Xms options below to override the
   # automatic calculation of JVM heap memory.
   #
   # It is recommended to set min (-Xms) and max (-Xmx) heap sizes to
   # the same value to avoid stop-the-world GC pauses during resize, and
   # so that we can lock the heap in memory on startup to prevent any
   # of it from being swapped out.
   -Xms31G
   -Xmx31G
   
   # For systems with > 8 cores, the default ParallelGCThreads is 5/8 the number of logical cores.
   # Otherwise equal to the number of cores when 8 or less.
   # Machines with > 10 cores should try setting these to <= full cores.
   -XX:ParallelGCThreads=32
   # By default, ConcGCThreads is 1/4 of ParallelGCThreads.
   # Setting both to the same value can reduce STW durations.
   -XX:ConcGCThreads=32
   
   -Xloggc:/opt/datastax/log/cassandra/gc.log
   ```
   
   **REMOVE** `resources/cassandra/conf/cassandra-topology.properties`
   
   **`resources/cassandra/conf/cassandra-rackdc.properties`**
   ```properties
   # These properties are used with GossipingPropertyFileSnitch and will
   # indicate the rack and dc for this node
   dc=atl1
   rack=rack1
   ```
   
   **`resources/cassandra/conf/logback.xml`** - run the following command in `vi` to replace all instances of `${cassandra.logdir}` with the full path.
   ```vi
   :%s/${cassandra.logdir}/\/opt\/datastax\/log\/cassandra/g
   ```

5. Edit Spark configuration files
   
   **`resources/spark/conf/spark-env.sh`**
   ```bash
   # This is a base directory for Spark Worker work files.
   export SPARK_WORKER_DIR="/dvl/frt/data1/spark/worker"

   # SPARK_EXECUTOR_DIRS is the temporary location for executors, external shuffle service and
   # cluster deployed Spark applications
   export SPARK_EXECUTOR_DIRS="/dvl/frt/data1/spark/rdd"

   # This is a base directory for Spark Worker logs.
   export SPARK_WORKER_LOG_DIR="/opt/datastax/log/spark/worker"

   # This is a base directory for Spark Master logs.
   export SPARK_MASTER_LOG_DIR="/opt/datastax/log/spark/master"

   # This is a base directory for Spark AlwaysOn SQL logs.
   export ALWAYSON_SQL_LOG_DIR="/opt/datastax/log/spark/alwayson_sql"
   ```
   
6. Configure `cassandra` user helpers
   
   **`~/.bash_profile`**
   ```bash
   export PS1="\u@\h \w$ "
   export PATH=$PATH:/opt/datastax/dse/bin
   
   . /opt/datastax/dse/resources/dse/conf/dse.default
   . /opt/datastax/dse/resources/spark/conf/spark-env.sh
   ```
7. Start DSE
   
   ```bash
   sudo service dse start
   ```

# Configure Authentication Roles
1. Change super user password
   
   The `cassandra` superuser account is hard-coded to perform authorization at consistency level `QUORUM`. This account should **never** be used outside of emergency situations. As a precaution we recommend changing the default password to prevent usage by unauthorized users. The default password is `cassandra`.
   
   ```bash
   cqlsh -u cassandra
   Password: 
   Connected to Test Cluster at 127.0.0.1:9042.
   [cqlsh 5.0.1 | DSE 6.0.12 | CQL spec 3.4.5 | DSE protocol v2]
   Use HELP for help.
   cassandra@cqlsh> ALTER ROLE cassandra WITH PASSWORD = 'DontUseThisAccount';
   ```

2. Create a role for DBA users. In this case the role will be used for logging in and not assigning permissions to other users.

   ```bash
   cassandra@cqlsh> CREATE ROLE dba WITH SUPERUSER = true AND LOGIN = true AND PASSWORD = 'DBAAccount';
   cassandra@cqlsh> LOGIN dba
   Password: 
   dba@cqlsh> 
   ```
   
3. Create a role for Developer users. In this case the role will be used for logging in and not assigning permissions to other users. Given this environment is for development purposes we open all permissions to this role.
   
   ```bash
   dba@cqlsh> CREATE ROLE developer WITH SUPERUSER = false AND LOGIN = true AND PASSWORD = 'DSERocks';
   dba@cqlsh> GRANT ALTER, CREATE, DESCRIBE, DROP, MODIFY, SELECT ON ALL KEYSPACES TO developer;
   ```

4. Grant `developer` role access to search indices, tables, etc.,

   ```bash
   GRANT EXECUTE ON REMOTE OBJECT DseClientTool TO developer;
   GRANT EXECUTE ON REMOTE OBJECT DseResourceManager TO developer;
   GRANT SELECT ON system.size_estimates TO developer;
   GRANT SELECT ON "HiveMetaStore".sparkmetastore TO developer;
   GRANT MODIFY ON "HiveMetaStore".sparkmetastore TO developer;
   GRANT CREATE ON ANY WORKPOOL TO developer;
   GRANT MODIFY ON ANY SUBMISSION TO developer;
   GRANT ALL PERMISSIONS ON ALL SEARCH INDICES TO developer;
   GRANT ALL PERMISSIONS ON SEARCH KEYSPACE keyspace_name TO DEVELOPER;
   ```

   Reference:
   - [Controlling access to search indexes](https://docs.datastax.com/en/dse/6.0/dse-admin/datastax_enterprise/security/secSearchIndexPermissions.html)
   - [Authentication and Authorization](https://docs.datastax.com/en/dse/6.0/dse-admin/datastax_enterprise/security/secConfigAuthTOC.html)