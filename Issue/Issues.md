## Issue1. NoIndexException in hugegraph
title: Two query behaviors that produce the same result are inconsistent.
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```gremlin
g.E().has('ep4', lt(0.32696354)).and(__.hasLabel('el2')).count()

g.E().has('ep4', lt(0.32696354)).match(__.as('start1').and(__.hasLabel('el2')).as('m1')).select('m1').count()
```
### Actual behavior
+ The first query thrown a exception: ```org.apache.hugegraph.exception.NoIndexException: Don't accept query based on properties [ep4] that are not indexed in label 'el2', may not match secondary/range condition```
+ The second query returned the results normally.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results.
+ file: 1HugeGraph-graphdata1.txt

## Issue2. IllegalArgumentException in hugeGraph
title:
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```gremlin
g.E().bothV().where(__.out('el0').count().is(gte(-3))).count()

g.E().match(__.as('start0').repeat(__.bothV()).times(1).as('m0')).select('m0').match(__.as('start1').where(__.out('el0').count().is(gte(-3))).as('m1')).select('m1').count()
```
### Actual behavior
+ The first query thrown a exception: ```java.lang.IllegalArgumentException: Not a legal range: [0, -3]```
+ The second query returned the results normally.
+ We replaced the ```bothV()``` step in the second query with ```match(__.as('start0').repeat(__.bothV()).times(1).as('m0')).select('m0')```, and moved the```where (...)``` step into the ```match()```step. Both of these operations should not affect the result.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results and were consistent with the results of the second query.
error messages:
``` 
java.lang.IllegalArgumentException: Not a legal range: [0, -3]

db0 second: 200
```
+ file: 1HugeGraph-graphdata1.txt
## Issue3. logic bug in hugeGraph
title: match()-step returns false results
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```gremlin
g.E().outV().both().match(__.as('start').where(__.outE().count().is(lt(-3))).as('end')).select('end').count()
-- 0
g.E().outV().repeat(__.both()).times(1).where(__.outE().count().is(lt(-3))).count()
-- 305
```
### Actual behavior
+ The first query returns: 0
+ The second query returns: 305
+ We replaced the ```repeat(__.both()).times(1)``` step in the second query with ```both()```, and moved the```where (...)``` step into the ```match()```step. Both of these operations should not affect the result.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results.
+ file: 
  + 4HugeGraph-graphdata1.txt
  + 4schema-out.txt

## Issue4. Exception in hugeGraph
title: Unexpected NumberFormatException for filter-step
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```gremlin
g.E().where(__.has('ep5', lt(true)))

g.E().match(__.as('start1').where(__.has('ep5', lt(true))).as('m1')).select('m1')
```
### Actual behavior
+ The first query thrown a exception: ```java.lang.NumberFormatException: Character t is neither a decimal digit number, decimal point, nor "e" notation exponential mark.```
+ The second query returned the results normally.
+ We moved the```where (...)``` step in first query into the ```match()```step, which should not affect the result.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results and were consistent with the results of the second query.

error message : 
```
first: java.lang.NumberFormatException: Character t is neither a decimal digit number, decimal point, nor "e" notation exponential mark.

second:9
```
+ file:
  + 2HugeGraph-create.txt
  + 2schema-out.txt

reference: janusGraph returns: 9

## Issue5: java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
title: Unexpected ArrayIndexOutOfBoundsException for filter-step and repeat()-step
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```
g.V().has('vp4', lt('')).repeat(__.out('el2')).emit().times(1).count()
g.V().match(__.as('start').has('vp4', lt('')).union(__.out('el2')).as('m')).select('m').count()
```
### Actual behavior
+ The first query thrown a exception: ```java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0```
+ The second query returned the results normally.
+ We moved the```has(...)``` step and ```repeat()```step in first query into the ```match()```step and replace the ```repeat()``` with ```union()``` step, which should not affect the result.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results and were consistent with the results of the second query.

error messages: java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0

second query: 0
+ file
  + 3HugeGraph-creat.txt
  + 3schema-out.txt
## Issue6: NoIndexException in HugeGraph, triggered by match()
title: NoIndexException triggered by match()
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```gremlin
g.V().has('vp4', neq('J2O')).has('vl1', 'vp2',gte(false)).has('vp2').has('vl0', 'vp3',gt(4592737712018141718)).out().count()

g.V().has('vp4', neq('J2O')).has('vl1', 'vp2',gte(false)).match(__.as('start0').has('vp2').has('vl0', 'vp3',gt(4592737712018141718)).repeat(__.out()).times(1).as('m0')).select('m0').count()
```
### Actual behavior
+ The first query returned the results normally.
+ The second query thrown a exception: ```org.apache.hugegraph.exception.NoIndexException: Don't accept query based on properties [vp4, vp2] that are not indexed in label 'vl1', may not match secondary/range/not-equal condition```
### 
+ We moved the```has(...)``` step and ```out()```step in first query into the ```match()```step and replace the ```out()``` with ```repeat()``` step, which should not affect the result.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results and were consistent with the results of the first query.
+ I have implemented a fuzzing tool and am using it to test HugeGraph. To reduce the burden on developers, I have simplified the test cases as much as possible and isolated irrelevant information. I hope this work can be helpful for further improving the stability of HugeGraph.

first query: 0

error messages: org.apache.hugegraph.exception.NoIndexException: Don't accept query based on properties [vp4, vp2] that are not indexed in label 'vl1', may not match secondary/range/not-equal condition

+ file
  + 3HugeGraph-creat.txt
  + 3schema-out.txt
## Issue7: IllegalStateException in hugeGraph
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```gremlin
g.V().inE('el0').hasLabel('el0','el2').hasLabel('el1')

g.V().match(__.as('start1').repeat(__.inE('el0')).times(1).as('m1')).select('m1').hasLabel('el0','el2').hasLabel('el1')
```
### Actual behavior
+ The first query thrown a exception: ```java.lang.IllegalStateException: Illegal key 'LABEL' with more than one value: [1, 3]```
+ The second query returned the results normally.
+ We replaced the ```inE('el0')``` step in the first query with ```match(__.as('start1').repeat(__.inE('el0')).times(1).as('m1')).select('m1')```, which should not affect the result.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results and were consistent with the results of the second query.
+ This issue was previously resolved in https://github.com/apache/incubator-hugegraph/pull/1737#issue-1109290490, but it has occurred again.

error messages:  java.lang.IllegalStateException: Illegal key 'LABEL' with more than one value: [1, 3]

second query: return empty result.

+ file
  + 3HugeGraph-creat.txt
  + 3schema-out.txt

## Issue8: logic bug in janusGraph
title: mathch()-step returns false results

- Version: 1.1.0
- Storage Backend: inmemory
- Mixed Index Backend: none
- Link to discussed bug: 
- Expected Behavior:
  When i execute ```g.V().order().by(asc).has('vp3',0.62307286)``` and ```g.V().order().by(asc).match(__.as('start0').has('vp3',0.62307286).as('m0')).select('m0')```. Both queries need returns the same results.
  
  When i execute both queries on HugeGraph, they return same results and met the expectations.
- Current Behavior:
  The first query returns: ```result{object=v[32896] class=org.apache.tinkerpop.gremlin.structure.util.detached.DetachedVertex}```
  The second query returns: empty results.
- Steps to Reproduce:
  1. connect to a empty graph
  2. Insert the target node into the graph and execute two queries above
```
GraphTraversalSource g = test.getG();
g.E().drop().iterate();
g.V().drop().iterate();
g.addV().property("vp3", 0.62307286).next();
String query1 = "g.V().has('vp3',0.62307286).count()";
String query2 = "g.V().order().by(asc).match(__.as('start0').has('vp3',0.62307286).as('m0')).select('m0')" ;
try{
    List<Result> results = test.getClient().submit(query1).all().get();
    for (Result r : results) {
        System.out.println(r);
    }
    System.out.println("=============");
    List<Result> results1 = test.getClient().submit(query2).all().get();

    for (Result r : results1) {
        System.out.println(r);
    }
}catch (Exception e){
    e.printStackTrace();
}
```
  3. The first query returns ```result{object=v[32896] class=org.apache.tinkerpop.gremlin.structure.util.detached.DetachedVertex}```, the second returns empty results.


```gremlin
g.V().order().by(asc).has('vp3',0.62307286)

g.V().order().by(asc).match(__.as('start0').has('vp3',0.62307286).as('m0')).select('m0')
```

```gremlin
first result: result{object=v[32896] class=org.apache.tinkerpop.gremlin.structure.util.detached.DetachedVertex}

second result: empty
```

+ file
  + 4JanusGraph-create.txt
  + 4schema-out.txt

ref: After we further investigate the graph data, we found that the query should return the first result.
    When I execute those two queries in hugeGraph, both return the first result

## Issue9: logic bug in TinkerGraph
```gremlin
g.V().has('vp3',0.62307286)
```
it returns:
```shell
empty result
```
+ file
  + 4TinkerGraph-create.txt
  + 4schema-out.txt
ref: When we execute the same query in hugeGraph, it returns the inserted node.

## Issue10: logic bug in HugeGraph
title:
- Server Version: 1.7.0
- Backend: RocksDB x nodes
- OS: 192 CPUs, 256 G RAM, Ubuntu 22.04
- Data Size:  50 vertices, 100 edges
### Expected behavior
For the two query below, the same result should be returned:
```gremlin
g.V().both('el2','el1','el3').inE('el2').where(__.bothV().count().is(outside(-1435889948263879801,-3366956858553110955))).count()

g.V().repeat(__.both('el2','el1','el3')).times(1).inE('el2').match(__.as('start').where(__.bothV().count().is(outside(-1435889948263879801,-3366956858553110955))).as('end')).select('end').count()
```
the first query returns: ```0```
the second query returns: ```99```

### Actual behavior
+ The first query returns: 0
+ The second query returns: 99
+ We replaced the ```both('el2','el1','el3')``` step in the first query with ```repeat(__.both('el2','el1','el3')).times(1)```, and moved the```where (...)``` step into the ```match()```step. Both of these operations should not affect the result.
+ When I executed these two queries on Janusgraph and Tinkerpop, they both returned the same results.
+ file
  + 4HugeGraph-create.txt
  + 4schema-out.txt

ref: When we execute the same query in JanusGraph and TinkerGraph, both queries in both GDBMSs returns 99.