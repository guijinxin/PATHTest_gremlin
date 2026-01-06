### Issue1. NoIndexException in hugegraph
```gremlin
g.E().has('ep4', lt(0.32696354)).and(__.hasLabel('el2')).count()

g.E().has('ep4', lt(0.32696354)).match(__.as('start1').and(__.hasLabel('el2')).as('m1')).select('m1').count()
```
error messages:
```
db0 first : org.apache.hugegraph.exception.NoIndexException: Don't accept query based on properties [ep4] that are not indexed in label 'el2', may not match secondary/range condition
db0 second: 11
```
+ file: 1HugeGraph-graphdata1.txt

### Issue2. IllegalArgumentException in hugeGraph
```gremlin
g.E().bothV().where(__.out('el0').count().is(gte(-3))).count()

g.E().match(__.as('start0').bothV().as('m0')).select('m0').match(__.as('start1').where(__.out('el0').count().is(gte(-1))).as('m1')).select('m1').count()
```
error messages:
``` 
java.lang.IllegalArgumentException: Not a legal range: [0, -3]

db0 second: 200
```
+ file: 1HugeGraph-graphdata1.txt
### Issue3. logic bug in hugeGraph
```gremlin
g.E().outV().both().match(__.as('start1').where(__.outE().count().is(lt(-3))).as('m1')).select('m1').count()
-- 0
g.E().outV().repeat(__.both()).times(1).where(__.outE().count().is(lt(-3))).count()
-- 98
```
+ file: 2HugeGraph-graphdata1.txt

### Issue4. Exception in hugeGraph
```gremlin
g.E().where(__.has('ep5', lt(true)))

g.E().match(__.as('start1').where(__.has('ep5', lt(true))).as('m1')).select('m1')
```

error message : 
```
first: java.lang.NumberFormatException: Character t is neither a decimal digit number, decimal point, nor "e" notation exponential mark.

second:9
```
+ file:
  + 2HugeGraph-create.txt
  + 2schema-out.txt

reference: janusGraph returns: 9

### Issue5: java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
g.V().has('vp4', lt('')).repeat(__.out('el2')).emit().times(1).count()
g.V().match(__.as('start').has('vp4', lt('')).union(__.out('el2')).as('m')).select('m').count()

error messages: java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0

second query: 0
+ file
  + 3HugeGraph-creat.txt
  + 3schema-out.txt
### Issue6: NoIndexException in HugeGraph, triggered by match()
```gremlin
g.V().has('vp4', neq('J2O')).has('vl1', 'vp2',gte(false)).has('vp2').has('vl0', 'vp3',gt(4592737712018141718)).out().count()

g.V().has('vp4', neq('J2O')).has('vl1', 'vp2',gte(false)).match(__.as('start0').has('vp2').repeat(__.out()).times(1).has('vl0', 'vp3',gt(4592737712018141718)).as('m0')).count()
```

first query: 0

error messages: org.apache.hugegraph.exception.NoIndexException: Don't accept query based on properties [vp4, vp2] that are not indexed in label 'vl1', may not match secondary/range/not-equal condition

+ file
  + 3HugeGraph-creat.txt
  + 3schema-out.txt
### Issue7: IllegalStateException in hugeGraph
```gremlin
g.V().inE('el0').hasLabel('el0','el2').hasLabel('el1')

g.V().match(__.as('start1').repeat(__.inE('el0')).times(1).as('m1')).select('m1').hasLabel('el0','el2').hasLabel('el1')
```

error messages:  java.lang.IllegalStateException: Illegal key 'LABEL' with more than one value: [1, 3]

second query: return empty result.

+ file
  + 3HugeGraph-creat.txt
  + 3schema-out.txt