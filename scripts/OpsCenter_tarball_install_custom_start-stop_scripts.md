# OpsCenter Tarball Installation Custom Start/Stop Scripts

Currently [_Customize scripts for starting and stopping DataStax Enterprise_](https://docs.datastax.com/en/opscenter/6.8/opsc/configure/opscStartStopScripts.html) for OpsCenter tarball installation doesn’t provide any information for customers/users to properly leverage the scripts. Something like below would be helpful to get started with.

I would start modifying the _**start**_ script something like below,
* https://docs.datastax.com/en/dse/6.8/dse-admin/datastax_enterprise/operations/startStop/startDseStandalone.html
  ```
  #!/bin/sh

  # modify startup with relevant flag, i.e. add -k for analytics, -s for search
  DSE_START="/path/to/tarball/installation/bin/dse cassandra"

  sudo $DSE_START
  # add some wait seconds?
  RESULT=$?
  if [ $RESULT -eq 0 ]
  then
      # startup successful
      exit $RESULT
  else
      # something went wrong starting DSE
      echo >&2 "ERROR - Failed to start DSE, error code [$RESULT]. Check system.log for details."
      exit $RESULT
  fi
  ```

 I would start modifying the _**stop**_ script something like below,
 * https://docs.datastax.com/en/dse/6.8/dse-admin/datastax_enterprise/operations/startStop/stopDseNode.html
   ```
   #!/bin/sh
   NODETOOL=/path/to/nodetool
   DSE_STOP="/path/to/tarball/installation/bin/dse cassandra-stop"
   #One could go fancier like DSE_STOP=kill -9 `ps -ef|grep -v grep|grep cassandra|grep dse|cut -d" " -f4`

   sudo $NODETOOL drain
   #add wait for couple seconds?
   RESULT=$?
   if [ $RESULT -ne 0 ]
   then
       # something went wrong with drain
       echo >&2 "ERROR - nodetool drain failed with error code [$RESULT]... attempting to stop DSE..."
   fi

   sudo $DSE_STOP
   RESULT=$?
   if [ $RESULT -eq 0 ]
   then
       # shutdown successful
       exit $RESULT
   else
       # something went wrong stopping DSE
       echo >&2 "ERROR - Failed to stop DSE, error code [$RESULT]"
       exit $RESULT
   fi
   ```
---   