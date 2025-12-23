package org.gdbtesting.janusgraph;


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

    public void connect(){
        try {
            // connect 1
            String file = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            file = file.substring(0, file.lastIndexOf("target")+6);
            String exactfile = System.getProperty("user.dir")+"/conf/janusgraph.yaml";
            cluster = Cluster.open(exactfile);
            client = cluster.connect();
            setClient(client);
            setCluster(cluster);

            // connect 2
            g = traversal().withRemote(DriverRemoteConnection.using(cluster, "g"));
            setG(g);
            setGraph(g.getGraph());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JanusGraphConnection test = new JanusGraphConnection("0.5.3", "conf/remote-janusgraph.properties");
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

        System.exit(0);
    }
}
