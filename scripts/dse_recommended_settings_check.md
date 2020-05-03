# Simple shell script to check recommended kernel settings on a RHEL server
This page will give a simple, quick and dirty shell script, that could be customized for the environment where it is run, to check for various [DataStax recommended production kernel settings](https://docs.datastax.com/en/landing_page/doc/landing_page/recommendedSettings.html) on a RHEL server, run by a linux system administrator with `root` level access.

```
#!/bin/bash
# Script to do recommended settings check across DSE servers

dev_servers="
sxn1da0009
sxn1da0020
sxn1da0029
"

si_servers="
sxn1da0010
sxn1da0011
sxn1da0021
sxn1da0022
sxn1da0030
sxn1da0031
"

root_check_dse_prod_settings() {
  server=$1

  commands=(
    "file /etc/localtime"
    "systemctl status ntpdate"
    "sysctl net.ipv4.tcp_keepalive_time net.ipv4.tcp_keepalive_probes net.ipv4.tcp_keepalive_intvl net.core.rmem_max net.core.wmem_max net.core.rmem_default net.core.wmem_default net.core.optmem_max net.ipv4.tcp_rmem net.ipv4.tcp_wmem"
    "cat /etc/sysctl.conf"
    "cat /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor"
    "cat /sys/class/block/sda/queue/rotational"
    "cat /sys/class/block/sda/queue/read_ahead_kb"
    "cat /proc/sys/vm/zone_reclaim_mode"
    "grep cassandra /etc/security/limits.conf"
    "grep vm.max_map_count /etc/sysctl.conf"
    "swapon"
    "grep vm.swappiness /etc/sysctl.conf"
    "cat /sys/kernel/mm/transparent_hugepage/defrag"
    )

  for cmd in "${commands[@]}"; do
    echo 
    echo 
    echo "## COMMAND: $cmd"
    ssh root@$server -- $cmd 2>&1 | tail +16
    echo "## END COMMAND: $cmd"
    echo 
    echo 
  done
}

success_count=0
success_servers=
error_count=0
error_servers=
for i in $dev_servers $si_servers; do
  echo "# Server: $i"

  server=$i.customer_name.com

  if root_check_dse_prod_settings $server; then
    echo
    echo "SUCCESS: command finished on $server"

    success_count=$((success_count + 1))
    success_servers="$success_servers\n$server"
  else
    echo
    echo "ERROR: command failed on $server"

    error_count=$((error_count + 1))
    error_servers="$error_servers\n$server"
  fi

  echo
  echo
  echo

done

echo "Success count: $success_count"
echo -e "Success servers: $success_servers"
echo
echo
echo "Error count: $error_count"
echo -e "Error servers: $error_servers"

echo
```
---