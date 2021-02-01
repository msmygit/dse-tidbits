import csv
import os
import re
from pathlib import Path
from collections import OrderedDict
import fileinput
from cfstats import Parser

def read_file(input):
    with fileinput.input(files=(input)) as f:
        parser = Parser()
        for line in f:
            #print(line)
            parser.capture_line(line.rstrip("\n"))
        return parser.parsed

if __name__ == '__main__':
    import sys

    if len(sys.argv) < 2:
        print("[usage] python3 tablestats.py <diagnostics_dir>")
        sys.exit(-1)

    # Another way to scan the directory
    #with os.scandir(sys.argv[1]) as nodes:
    #    for node in nodes:
    #        print(node.name)

    nodes = []
    stats = []
    entries = Path(sys.argv[1])
    for entry in entries.iterdir():
        if entry.is_dir():
            #print('entry.name: ' + entry.name)
            #print(entry.name)
            nodes.append(entry.name)

    # Print the nodes array
    #print('---')
    #print(nodes)

    # Find cfstats files
    cfstats_files = []
    # Update `cfstats` to `tablestats` if applicable
    for cfstats_file in Path(sys.argv[1]).glob('**/cfstats'):
        #print(cfstats_file)
        cfstats_files.append(cfstats_file)
        stats.append(read_file(cfstats_file))

    # Check if at least one cfstats file exists in the diag collected
    if len(cfstats_files) == 0:
        print("cfstats files not found")
        sys.exit(-1)

    # Output
    writer = csv.writer(sys.stdout, lineterminator='\n')
    # header
    props = ['sstable_size', 'sstable_count', 'read_count', 'write_count',
             'max_partition_size', 'num_partitions', 'max_tombstones']
    header = ["keyspace", "table"]
    for p in props:
        for n in nodes:
            header.append(p + '_' + n)
    writer.writerow(header)

    # body
    for f in cfstats_files:
        ks_names = list(read_file(f))
        #print(ks_names)
        #print('-------------')
        for ks in ks_names:
            tables = list(stats[0][ks])
            #print(tables)
            #print('-------------')
            for t in tables:
                row = [ks, t]
                #print(row)
                for s in stats:
                    row.append(s[ks][t]['Space used (live)'])
                for s in stats:
                    row.append(s[ks][t]['SSTable count'])
                for s in stats:
                    row.append(s[ks][t]['Local read count'])
                for s in stats:
                    row.append(s[ks][t]['Local write count'])
                for s in stats:
                    row.append(s[ks][t]['Compacted partition maximum bytes'])
                for s in stats:
                    #Update the below to 'Number of partitions (estimate)' for DSE > 5.1
                    row.append(s[ks][t]['Number of keys (estimate)'])
                for s in stats:
                    row.append(s[ks][t]['Maximum tombstones per slice (last five minutes)'])
                writer.writerow(row)