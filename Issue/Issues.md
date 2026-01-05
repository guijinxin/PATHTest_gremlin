### Issue1. NoIndexException in hugegraph
```gremlin
g.V().has('vp3').inE('el1','el3').has('el0', 'ep2',gte(8438332450117128934)).count()

g.V().has('vp3').inE('el1','el3').has('el0', 'ep2',gte(8438332450117128934)).match(__.as('start2').count().as('m2')).select('m2').count()
```
error messages:
```
db0 first : org.apache.hugegraph.exception.NoIndexException: Don't accept query based on properties [ep4] that are not indexed in label 'el2', may not match secondary/range condition
db0 second: 11
```
+ file: HugeGraph-graphdata1.txt

### Issue2. IllegalArgumentException in hugeGraph
```gremlin
g.E().bothV().where(__.out('el0','el3').count().is(gte(-6738426904345399545))).count()

g.E().match(__.as('start0').bothV().as('m0')).select('m0').match(__.as('start1').where(__.out('el0','el3').count().is(gte(-6738426904345399545))).as('m1')).select('m1').count()
```
error messages:
``` 
java.lang.IllegalArgumentException: Not a legal range: [0, -6738426904345399296]

db0 second: 200
```
+ file: HugeGraph-graphdata1.txt
### Issue3. logic bug in hugeGraph
```gremlin
g.E().outV().both('el2','el1').match(__.as('start1').where(__.outE('el0','el3').count().is(lt(-3))).as('m1')).select('m1').count()
-- 0
g.E().outV().repeat(__.both('el2','el1')).emit().times(1).where(__.outE('el0','el3').count().is(lt(-3))).count()
-- 103
```
+ file: HugeGraph-graphdata1.txt