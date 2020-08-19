# [DataStax Bulk Loader](https://docs.datastax.com/en/dsbulk/doc/index.html) References

* DataStax Blogs,
  * [Introducing DataStax Bulk Loader - 08-May-2018](https://www.datastax.com/2018/05/introducing-datastax-bulk-loader)

* DataStax Academy Developer Blogs,
  * [DSBulk - Invoking loading, unloading, and counting operations programmatically in your Java applications!](https://www.datastax.com/blog/2020/08/open-source-ftw-new-tools-apache-cassandratm)
  * [DSBulk Introduction and Loading](https://academy.datastax.com/content/datastax-bulk-loader-introduction-and-loading)
  * [DSBulk More Loading](https://academy.datastax.com/content/datastax-bulk-loader-more-loading)
  * [DSBulk Common Settings](https://academy.datastax.com/content/datastax-bulk-loader-common-settings)
  * [Loading data into DataStax Astra using DSBulk](https://github.com/DataStax-Examples/dsbulk-to-astra)

* Distributed Data Show
  * [Episode 49: Bulk Loading with Brian Hess](https://www.youtube.com/watch?v=CAH7Mlg_rVI)

* DataStax Documentation
  * [DSBulk Reference](https://docs.datastax.com/en/dsbulk/doc/)
  * [DSBulk Install Docs](https://docs.datastax.com/en/dsbulk/doc/dsbulk/install/dsbulkInstall.html)
  * [DSBulk Release Notes](https://docs.datastax.com/en/dsbulk/doc/dsbulk/releaseNotes/RelNotesdsbulk.html)

* Other Titbits
  * Get DSBulk unload/load commands for a given keyspace (& it’s tables) to copy over data from source and destination. Assumptions: There is no requirement to maintain `TTL` & `WRITETIME` (life span) and we understand that the source cluster is taking live traffic and there could be data written post the copy over operation in DSBulk,
    * (_**NOTE:**_ I used `localhost` for the source and `127.0.0.1` for the destination, but one can tweak those to their cluster configuration and also add username and password to access the same, as required.)
    ```
    ./dsbulk unload -header false -verbosity 0 -h localhost -query "SELECT keyspace_name, table_name FROM system_schema.tables WHERE keyspace_name = 'my_example_keyspace'" | awk -F, '{printf("\ndsbulk unload -h localhost -k %s -t %s | dsbulk load -h 127.0.0.1 -k %s -t %s\n", $1, $2, $1, $2);}'
    ```
  * Let’s say if we’re performing a simple & straightforward DSBulk unload/load operation and notice the below scanned tombstone exception (click expand to view the stacktrace),
    ```
    dsbulk unload -h IP -k ks_source -t tbl_name | dsbulk load -h IP -k ks_dest -t tbl_name
    ```
  * When dealing with data that has too many tombstones, we could turn off [executor continuous paging](https://docs.datastax.com/en/dsbulk/doc/dsbulk/reference/executorOptions.html#executorOptions__executorOptionsexecutorcontinuouspagingenabled-true-false-kNH0qQNc). When Continuous Paging is on, [`schema.splits`](https://docs.datastax.com/en/dsbulk/doc/dsbulk/reference/executorOptions.html#executorOptions__executorOptionsexecutorcontinuouspagingenabled-true-false-kNH0qQNc) can help (should be set to high values), when Continuous Paging is off, [page sizes](https://docs.datastax.com/en/dsbulk/doc/dsbulk/reference/driverOptions.html#driverOptions__datastaxJavaDriverAdvancedContinuousPagingPageSize) should help (should be set to smaller values).
    ```
    dsbulk unload -h IP -k ks_source -t tbl_name --executor.continuousPaging.enabled false| dsbulk load -h IP -k ks_dest -t tbl_name --executor.continuousPaging.enabled false
    ```
    we might see tombstone errors like these,
    ```
    com.datastax.dsbulk.executor.api.exception.BulkExecutionException: Statement execution failed: TokenRangeReadStatement{table=digsupchain_qa_stage.discp_supply_commits,range=(457426640312032821,1353117933348808081]} (An unexpected error occurred server side on /10.175.181.34:9042: org.apache.cassandra.db.filter.TombstoneOverwhelmingException: Scanned over 200001 tombstones during query ‘SELECT...
    ...
    WHERE token(facility_location) > 457426640312032821 AND token(facility_location) <= 1353117933348808081 ‘
    (last scanned row partion key was ((NFC_GENCO), MM81X, DAO, 9a1a5c0e-cd55-3f9d-8eec-64c18aa3a73d)); query aborted)
    at com.datastax.dsbulk.executor.api.internal.subscription.ResultSubscription.toErrorPage(ResultSubscription.java:509)
    at com.datastax.dsbulk.executor.api.internal.subscription.ResultSubscription.lambda$fetchNextPage$5(ResultSubscription.java:360)
    at com.datastax.dsbulk.executor.api.internal.subscription.ResultSubscription$1.onFailure(ResultSubscription.java:579) [4 skipped]
    at com.datastax.driver.core.ContinuousPagingQueue.complete(ContinuousPagingQueue.java:311) [6 skipped]
    at com.datastax.driver.core.ContinuousPagingQueue.enqueueOrCompletePending(ContinuousPagingQueue.java:196)
    Caused by: com.datastax.driver.core.exceptions.ServerError: An unexpected error occurred server side on /10.175.181.34:9042: org.apache.cassandra.db.filter.TombstoneOverwhelmingException: Scanned over 200001 tombstones during query ‘SELECT
    ...
    at com.datastax.driver.core.Responses$Error.asException(Responses.java:113)
    at com.datastax.driver.core.ContinuousPagingQueue.onResponse(ContinuousPagingQueue.java:138)
    at com.datastax.driver.core.MultiResponseRequestHandler.setResult(MultiResponseRequestHandler.java:740)
    at com.datastax.driver.core.MultiResponseRequestHandler.onSet(MultiResponseRequestHandler.java:499)
    at com.datastax.driver.core.Connection$Dispatcher.channelRead0(Connection.java:1091)
    Suppressed: java.lang.Exception: #block terminated with an error
    at com.datastax.dsbulk.engine.UnloadWorkflow.execute(UnloadWorkflow.java:145) [2 skipped]
    at com.datastax.dsbulk.engine.DataStaxBulkLoader$WorkflowThread.run(DataStaxBulkLoader.java:168)
    ```
  * Starting with [DSBulk version `1.5.0`](https://docs.datastax.com/en/dsbulk/doc/dsbulk/releaseNotes/RelNotesdsbulk.html?hl=graph#RNdsbulk150__150changes), we can load DSE Graph data swiftly into DataStax Enterprise (DSE).
---    