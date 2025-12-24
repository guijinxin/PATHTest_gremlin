package org.gdbtesting.janusgraph;

import org.apache.tinkerpop.gremlin.util.ser.GraphBinaryMessageSerializerV1;  // 新包（3.7+）
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;  // 这个通常不变
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;  // JanusGraph 自定义 registry
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;  // 来自 janusgraph-core 1.1.0
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;  // 来自 janusgraph-core
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.gdbtesting.connection.GremlinConnection;
import org.janusgraph.core.ConfiguredGraphFactory;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.graphdb.database.StandardJanusGraph;


import java.net.URL;
import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class JanusGraphConnection extends GremlinConnection {

    public JanusGraphConnection(String version, String filename) {
        super(version, "JanusGraph", filename);
    }

 /*   public void setup(){
        String file = this.getClass().getClassLoader().getResource("conf/janusgraph.yaml").getPath();
        org.apache.tinkerpop.gremlin.groovy.loaders.SugarLoader.load();
        graph = JanusGraphFactory.open(file);
        g = graph.traversal();
        System.out.println(g.V());
    }*/

    public void connect() {
        try {
            // 构建带 JanusGraph 自定义类型支持的 registry
            TypeSerializerRegistry registry = TypeSerializerRegistry.build()
                    .addRegistry(JanusGraphIoRegistry.instance())
                    .create();

            // 使用 registry 构造 GraphBinary 序列化器
            GraphBinaryMessageSerializerV1 serializer = new GraphBinaryMessageSerializerV1(registry);

            cluster = Cluster.build()
                    .addContactPoint("localhost")
                    .port(8185)  // 你之前 JanusGraph 的端口
                    .serializer(serializer)
                    .create();

            client = cluster.connect();
            setClient(client);
            setCluster(cluster);

            g = traversal().withRemote(DriverRemoteConnection.using(cluster, "g"));
            setG(g);
            setGraph(g.getGraph());

            System.out.println("JanusGraph 连接成功！");
            System.out.println("测试: " + g.V().limit(1).toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JanusGraphConnection test = new JanusGraphConnection("1.1.0", "conf/remote-janusgraph.properties");
        GraphTraversalSource g = test.getG();
        Vertex Ironman = g.addV("Hero").property("name", "Tony").property("ATK",100.00).next();
        Vertex Superman = g.addV("Hero").property("name", "Clark").property("ATK",Double.POSITIVE_INFINITY).next();
        Vertex Moly = g.addV("student").property("grade", 9).next();
        Vertex notebook = g.addV("homework").property("subject", "Math").next();
        Edge edge1 = g.addE("write").from(Moly).to(notebook).property("date","0.8").next();

        String query1 = "g.V().where(__.out('el1').count().is(gt(-5)))";

        try{
            List<Result> results = test.getClient().submit(query1).all().get();
            System.out.println(results.size());
            for (Result r : results) {
                System.out.println(r);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        g.E().drop().iterate();
        g.V().drop().iterate();
        System.out.println(g.V().count().next());

        System.exit(0);
    }
}
