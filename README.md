# Grand

Grand is a tool for finding logic bugs in Gremlin-Based Graph Database Systems (GDBs). We refer to logic bugs as those bugs that GDBs return an unexpected result (e.g., incorrect query result or unexpected error) without crashing the GDBs for a given query. 

Grand operates in the following three phases:

1. Graph database generation: The goal of this phase is to generate a populated graph database for each target GDB. Specially, Grand first randomly generates the graph schema to define the types of vertices and edges. Then, the detailed vertices and edges can be randomly generated according to the generated graph schema. Finally, the generated database will be written into target GDBs.
2. Gremlin query generation: This phase is aimed to generate syntactically correct and valid Gremlin queries. We first construct a traversal model for Gremlin APIs, and then generate Gremlin queries based on the constructed traversal model.
3. Differential Testing: Grand executes the generated Gremlin queries and validates the query results by differential testing.  

 
# Getting Started

## Requirements

- Java 8
- Maven 3 
- Operate System: Linux
- The GDBs that you want to test 
  
We take three GDBs, [JanusGraph](https://janusgraph.org), [TinkerGraph](https://github.com/tinkerpop/blueprints/wiki/tinkergraph), and [HugeGraph](https://hugegraph.github.io/hugegraph-doc/), as an example to show how to use Grand locally. 

## Setting GDBs

1. Download GDBs
- Download JanusGraph server (version 0.5.3) from its [official website](https://github.com/JanusGraph/janusgraph/releases/download/v0.5.3/janusgraph-0.5.3.zip), unzip it to the folder `/opt/`, and replace `janusgraph-0.5.3/conf/gremlin-server/gremlin-server.yaml` with `Grand/gdbs/janusgraph/gremlin-server.yaml` we offered.
- Download TinkerGraph server (version 3.4.10) from its [official website](https://archive.apache.org/dist/tinkerpop/3.4.10/apache-tinkerpop-gremlin-server-3.4.10-bin.zip), unzip it to the folder `/opt/`, and replace `apache-tinkerpop-gremlin-server-3.4.10/conf/gremlin-server.yaml` with `Grand/gdbs/tinkergraph/gremlin-server.yaml` we offered.
- Download HugeGraph server (version 0.11.2) from its [official website](https://github.com/hugegraph/hugegraph/releases/download/v0.11.2/hugegraph-0.11.2.tar.gz), unzip it to the folder `/opt/`, and replace `hugegraph-0.11.2/conf/hugegraph.properties` with `Grand/gdbs/hugegraph/hugegraph.properties` we offered.

2. Starting GDBs

The following commands start JanusGraph, TinkerGraph and HugeGraph respectively. Note that, when using Hugegraph for the first time, you need to create a folder first.

```
/opt/janusgraph-0.5.3/bin/gremlin-server.sh start 
/opt/apache-tinkerpop-gremlin-server-3.4.10/bin/gremlin-server.sh start 
mkdir /opt/hugegraph-0.11.2/logs
/opt/hugegraph-0.11.2/bin/start-hugegraph.sh
```

The outputs of these commands are:

```
Server started 118286.
Server started 118471.
Starting HugeGraphServer...
Connecting to HugeGraphServer (http://127.0.0.1:8080/graphs)....OK
Started [pid 119706]
```

## Running Grand


1. Compile

The following commands create a JAR (`Grand-1.0-SNAPSHOT.jar`) for Grand:

```
cd gdbtesting
mvn package -DskipTests
```

2. Run with script

To simplify the test procedure, you can use our script to run Grand with the JAR we offered (or using the JAR you have compiled `Grand/gdbtesting/target/Grand-1.0-SNAPSHOT.jar`). Note that, you should put the script (i.e., `run.sh`), the JAR file (i.e., `Grand-1.0-SNAPSHOT.jar`), and the configuration files (i.e., `Grand/conf/` provided by us) in the same folder (e.g., `Grand/`). 

```
bash run.sh
```

The parameters in our script are as follows. You can modify our script and change these parameters as needed.

- `QueryDepth`, the max length of the query we generated, e.g, 5.
- `VerMaxNum`, the maximum number of the Vertex in the generated graph, e.g., 100.
- `EdgeMaxNum`, the maximum number of the Edge in the generated graph, e.g., 200.
- `EdgeLabelNum`, the maximum number of the edge label in the generated graph, e.g., 20.
- `VerLabelNum`, the maximum number of the vertex label in the generated graph, e.g., 10.
- `QueryNum`, the number of query generated in a test round, e.g., 10.
- `RepeatTimes`, the test rounds, e.g., 1.

The outputs of this command include three parts.

- Restart JanusGraph and HugeGraph. 

Due to the graph schema created in JansuGraph and HugeGraph could not be modified, we should restart them before testing.

```
Server stopped [121515]
Server started 121742.
no crontab for root
The HugeGraphServer monitor has been closed
Killing HugeGraphServer(pid 119706)...OK
Initializing HugeGraph Store...
2022-05-10 10:35:50 548   [main] [INFO ] com.baidu.hugegraph.cmd.InitStore [] - Init graph with config file: conf/hugegraph.properties
main dict load finished, time elapsed 794 ms
model load finished, time elapsed 29 ms.
2022-05-10 10:35:51 1893  [main] [INFO ] com.baidu.hugegraph.HugeGraph [] - Graph 'hugegraph' has been initialized
2022-05-10 10:35:51 1893  [main] [INFO ] com.baidu.hugegraph.HugeGraph [] - Close graph standardhugegraph[hugegraph]
2022-05-10 10:35:52 1907  [Thread-1] [INFO ] com.baidu.hugegraph.HugeGraph [] - HugeGraph is shutting down
Initialization finished.
Starting HugeGraphServer...
Connecting to HugeGraphServer (http://127.0.0.1:8080/graphs)....OK
Started [pid 121894]
waiting......
```

- Generate databases and queries.

These information have been recorded in logs. We will introduce them later.

```
createGraphSchema
creatGraphData
generate query
```

- Setup database and execute queries in each GDBs.

```
setup Graph
setup HugeGraph in 843ms
query 0 in 944ms
query 1 in 85ms
query 2 in 70ms
query 3 in 76ms
query 4 in 44ms
query 5 in 49ms
...
query 97 in 30ms
query 99 in 17ms
query HugeGraph in 3912ms
setup JanusGraph in 6266ms
query 1 in 52ms
query 2 in 31ms
query 3 in 28ms
query 4 in 15ms
query 5 in 31ms
query 6 in 35ms
query 7 in 58ms
query 8 in 55ms
...
query 99 in 28ms
query JanusGraph in 31926ms
setup TinkerGraph in 625ms
query 0 in 27ms
query 1 in 2ms
query 2 in 5ms
...
query 96 in 4ms
query 97 in 4ms
query 99 in 3ms
query TinkerGraph in 512ms
```

- Check result.

`Exist difference!` means there exist potential bugs. The last output means test round and parameters. These discrepancies are also recorded in logs. 

```
check result
Exist difference!
1 times: 5 100 200 20 20 100
```

# Detailed Description
 
## Claims
1. Grand can randomly generate databases
2. Grand can generate syntactically correct and valid Gremlin queries
3. Grand can finding bugs via differential testing


## Logs

Grand stores logs in the `log-*` directory, including:
- `schema.txt`, records the schema information of generated graph.
- `xxx-graphdata.txt`, records the graph data information of each GDB.
- `xxx-Map.txt`, records the mapping information of vertices and edges of each GDB.
- `xxx-*.txt`, records the result of each query for a GDB (e.g., `JanusGraph-1.txt` records each query and its return results).
- `check-*.txt`, records the result comparison of GDBs.
- `result-*.txt`, records the serial number of queries that have different results from GDBs.

The graph schema, graph data and Gremlin queries, which Grand randomly generated, are all recorded in files, i.e., `schema.txt`, `xxx-graphdata.txt` and `xxx-*.txt`, respectively.

Grand compares results for each query and records the comparison results in the file `check-*.txt`. For example, for a generated query `g.V().outE('el2').count()` (`Query 1`), the outputs of three GDBs are as follows. Among them, `db0` represents HugeGraph, `db1` represents JansuGraph, and `db2` represents TinkerGraph.

```
========================Query 1=======================
db0: n:[200]
db1: n:[200]
db2: n:[200]
```

If there exist different outputs for a query, the file `result-*.txt` will record the query number. 

```
query2: false
query10: false
query13: false
query25: false
```

In this case, the `query 2`, `query 10`, `query 13` and `query 25` may trigger potential bugs in GDBs. Therefore, we need to analyze them carefully to make sure whether they can trigger a true bug. Actually, there are many duplicated discrepancies, so we need to manually filter duplicated bugs before reporting them.   

## Manually investigating the observed discrepancies
For each discrepancy reported by Grand, we manually reproduce and analyze it, to verify whether it is a real logic bug.

1. Simplify.

Grand may generate a complex query to reveal a discrepancy, which makes it challenging to identify its root cause. So, we manually simplify the query to a simple one, which can trigger the same discrepancy. 

2. Investigate.

A discrepancy in Grand cannot tell which GDBs are buggy. So, we manually investigate the expected result of the query in the discrepancy, and then identify which GDBs are buggy.

3. Filter.

Third, Grand may report different discrepancies for the same bug. We need to understand the root causes of observed discrepancies, and identify whether some discrepancies are caused by the same bug. We only report unique bugs to developers.

**An Example**

We will discuss how we found the true bug in [HugeGraph#1586](https://github.com/apache/incubator-hugegraph/issues/1586).

Firstly, we randomly generate a query.

```
g.V().has('vp0', not(between(423,-23))).and(outside(663210698,-1025389545)))
```

After executing this query in target GDBs (i.e., HugeGraph, JansuGraph and TinkerGraph), we found this query was recorded in the log `result.txt` (i.e., `query10: false`). 

We then look at the outputs of the `Query 10` in the log `check.txt`. We found:

```
db0: v:[2, 4, 23, 52, 2, 4]
db1: v:[2, 4, 23, 52]
db2: v:[2, 4, 23, 52]
```

We identify `Hugegraph` might buggy because its query result is different from the other two GDBs. 

Next, we try to simplify this query to a simple one. Specially, we delete the last Gremlin API call, execute the new query in HugeGraph and check whether the same discrepancy exists. If a discrepancy exists, we then continue the above procedure untill the discrepancy does not exist. By this way, We find the following query is the simple one to trigger the discrepancy.  

```
g.V().has('vp0', not(between(423,-23)))
```

After that, we try to understand the root causes of this observed discrepancy, and find that there are redundant results due to HugeGraph ignores dedup them. 

## Found Bugs
Grand has found 21 logic bugs in six popular and widely-used GDBs, i.e., [Neo4j](https://neo4j.com), [JanusGraph](https://janusgraph.org), [TinkerGraph](https://github.com/tinkerpop/blueprints/wiki/tinkergraph), [Arcadedb](https://arcadedb.com/), [Orientdb](https://www.orientdb.org/), and [HugeGraph](https://hugegraph.github.io/hugegraph-doc/). Among them, developers have confirmed 18 bugs, and fixed 7 bugs.

**Bug List**

| ID | GDB  |  Issue                                                                    | Status         | 
| -- | --------- | ------------------------------------------------------------------------ | -------------- | 
| 1  | HugeGraph | [HugeGraph-1586](https://github.com/apache/incubator-hugegraph/issues/1586) | Confirmed |
| 2  | HugeGraph | [HugeGraph-1595](https://github.com/apache/incubator-hugegraph/issues/1595) | Confirmed |
| 3  | HugeGraph | [HugeGraph-1575](https://github.com/apache/incubator-hugegraph/issues/1575) | Fixed |
| 4  | HugeGraph | [HugeGraph-1573](https://github.com/apache/incubator-hugegraph/issues/1573) | Confirmed |
| 5  | HugeGraph | [HugeGraph-1582](https://github.com/apache/incubator-hugegraph/issues/1582) | Confirmed |
| 6  | HugeGraph | [HugeGraph-1579](https://github.com/apache/incubator-hugegraph/issues/1579) | Confirmed |
| 7  | HugeGraph | [HugeGraph-1734](https://github.com/apache/incubator-hugegraph/issues/1734) | Confirmed |
| 8  | HugeGraph | [HugeGraph-1735](https://github.com/apache/incubator-hugegraph/issues/1735) | Fixed |
| 9  | HugeGraph | [HugeGraph-1736](https://github.com/apache/incubator-hugegraph/issues/1736) | Fixed |
| 10  | JanusGraph | [JanusGraph-2751](https://github.com/JanusGraph/janusgraph/issues/2751) | Fixed |
| 11  | JanusGraph | [JanusGraph-2773](https://github.com/JanusGraph/janusgraph/issues/2773) | Fixed |
| 12  | JanusGraph | [JanusGraph-2768](https://github.com/JanusGraph/janusgraph/discussions/2768) | Confirmed |
| 13  | TinkerGraph | [TinkerGraph-2604](https://issues.apache.org/jira/browse/TINKERPOP-2604) | Confirmed |
| 14  | TinkerGraph | [TinkerGraph-2603](https://issues.apache.org/jira/browse/TINKERPOP-2603) | Fixed |
| 15  | TinkerGraph | [TinkerGraph-2694](https://issues.apache.org/jira/browse/TINKERPOP-2694) | Confirmed |
| 16  | Neo4j | [Neo4j-2607](https://github.com/neo4j/neo4j/issues/2607) | Fixed |
| 17  | Neo4j | [Neo4j-2606](https://github.com/neo4j/neo4j/issues/2606) | Confirmed |
| 18  | Neo4j | [Neo4j-12827](https://github.com/neo4j/neo4j/issues/12827) | Pending |
| 19  | Arcadedb | [Arcadedb-289](https://github.com/ArcadeData/arcadedb/issues/289) | Confirmed |
| 20  | Arcadedb | [Arcadedb-290](https://github.com/ArcadeData/arcadedb/issues/290) |  |
| 21  | Orientdb | [Orientdb-9754](https://github.com/orientechnologies/orientdb/issues/9754) | Pending |





