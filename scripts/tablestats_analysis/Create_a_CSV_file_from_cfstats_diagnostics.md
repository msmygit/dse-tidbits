<center><h1> Create a CSV file from cfstats diagnostics </h1></center>

# About
A diagnostics tarball created by [DataStax Diagnostics Collection script](https://github.com/DataStax-Toolkit/diagnostic-collection) or by [DataStax OpsCenter](https://docs.datastax.com/en/opscenter/6.8/opsc/online_help/opscCollectingDiagnosticData_t.html) contains *cfstats* file, which is created from [`nodetool tablestats`](https://docs.datastax.com/en/dse/6.8/dse-admin/datastax_enterprise/tools/nodetool/toolsTablestats.html) command, and contains all the table statistics.

This script parses *cfstats* file using excerpts from [DataStax `sperf`](https://github.com/DataStax-Toolkit/sperf) and convert statistics across the nodes to CSV format. This was tested using the `Python 3.8.2` version.

# Prerequisite
1. [python 3](https://www.python.org/downloads/)
2. Download [`tablestats.py`](./tablestats.py) & [`cfstats.py`](./cfstats.py) scripts to your working directory
3. You should see the following directory structure in the downloaded diagnostics:
     ```
     ./-
      |- nodes
          |- 10.0.10.01
              |- conf
              |- driver
              |- ...
          |- 10.0.10.02
              |- conf
              |- ...
          |- 10.0.10.03
          |- ...
     ```

# Usage
1. Run the following from the working directory where it has the diagnostics tarball and the scripts:
   ```
   python3 tablestats.py /path/to/diagnostics/nodes/directory > your_filename.csv
   ```
2. The generated `your_filename.csv` file will have duplicates and that can be removed by running the following:
   ```
   sort your_filename.csv | uniq > your_dups_removed_filename.csv
   ```
3. From the generated CSV file, please find the the below row and move it to the header:
   ```
   keyspace,table,sstable_size_...
   ```
4. Save the file and you are done