package org.gdbtesting.tinkergraph;
import org.apache.tinkerpop.gremlin.util.ser.GraphBinaryMessageSerializerV1;  // 正确包路径（3.7.3）
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;  // 只在需要自定义 registry 时用（这里不需要）
import org.apache.tinkerpop.gremlin.driver.Cluster;
//import org.apache.tinkerpop.gremlin.driver.RequestOptions;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.gdbtesting.connection.GremlinConnection;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
/**
 * @author Yingying Zheng
 * @date 2021/2/22
 */
public class TinkerGraphConnection extends GremlinConnection {

    private TinkerGraph graph;

    public TinkerGraphConnection(String version){
        super(version, "TinkerGraph");
    }

    public TinkerGraphConnection(String version, String filename){
        super(version, "TinkerGraph", filename);
    }


    public void connect(){
        try {
            // TinkerGraph 无自定义类型，直接使用默认 GraphBinary 序列化器（性能最佳）
            GraphBinaryMessageSerializerV1 serializer = new GraphBinaryMessageSerializerV1();

            cluster = Cluster.build()
                    .addContactPoint("localhost")
                    .port(8184)
                    .serializer(serializer)
                    .create();

            client = cluster.connect();
            setClient(client);
            setCluster(cluster);

            g = traversal().withRemote(DriverRemoteConnection.using(cluster, "g"));
            setG(g);
            setGraph(g.getGraph());

            System.out.println("TinkerGraph 连接成功！");
            System.out.println("顶点数量测试: " + g.V().count().next());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TinkerGraph getGraph() {
        return graph;
    }

    public static void main(String[] args) {

        TinkerGraphConnection connection = new TinkerGraphConnection("");
        GraphTraversalSource g = connection.getG();
        String query1 = "g.E().order().by(asc).has('ep4',0.49562305).outV().both('el0','el2','el1').repeat(__.in('el3')).emit().times(2).where(__.bothE('el2','el1','el3').count().is(lt(886129397034095534))).count()";
        String query2 = "g.E().match(__.as('start0').order().by(asc).as('m0')).select('m0').match(__.as('start1').has('ep4',0.49562305).as('m1')).select('m1').outV().as('start2').emit().repeat(__.both('el0','el2','el1').as('a0')).times(3).where(__.path().from('start2').unfold().count().is(eq(2))).select(last, 'a0').as('start3').union(repeat(__.in('el3').as('a1')).times(1).simplePath().path().select(last, 'a1'), repeat(__.in('el3').as('a1')).times(1).cyclicPath().path().select(last, 'a1'), __.in('el3').in('el3')).where(__.bothE('el2','el1','el3').count().is(lt(886129397034095534))).count()";

        //g.V().has('vp1', inside(0.24070676216155018,4.13998472E8)).inE('el0','el4').outV().not(__.values('vp1'))

        try{
            List<Result> results = connection.getClient().submit(query1).all().get();
            System.out.println(results.size());
            for (Result r : results) {
                System.out.println(r.toString());
            }

            List<Result> results2 = connection.getClient().submit(query2).all().get();
            System.out.println(results2.size());
            for (Result r : results2) {
                System.out.println(r.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.exit(0);
    }

}
