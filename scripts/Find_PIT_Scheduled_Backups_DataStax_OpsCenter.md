# DataStax OpsCenter - How to find pit or scheduled backups?

**_NOTE:_** This page will show steps on how to find the scheduled & point-in-time backup jobs information from OpsCenter tables using [CQL](https://docs.datastax.com/en/dse/6.8/cql/).

For retrieving scheduled backups info use,
```
SELECT blobAsText(key), blobAsText(column1), blobAsText(value) FROM settings WHERE key = textAsBlob('global-scheduled-jobs');
```
For retrieving point-in-time (aka PIT) backups info use,
```
SELECT blobAsText(key), blobastext(column1), blobastext(value) FROM settings WHERE key = textAsBlob('global-pit-backup');
```
---