package org.gdbtesting.arcadedb;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.gdbtesting.connection.GremlinConnection;

import java.util.List;

import static java.lang.Double.POSITIVE_INFINITY;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class ArcadedbConnection extends GremlinConnection {

    public ArcadedbConnection(String version, String filename) {
        super(version, "ArcadeDB", filename);
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
            String file = this.getClass().getClassLoader().getResource("conf/arcade.yaml").getPath();
            cluster = Cluster.open(file);
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
        org.gdbtesting.arcadedb.ArcadedbConnection test = new org.gdbtesting.arcadedb.ArcadedbConnection("0.5.3", "conf/remote-arcade.properties");
        GraphTraversalSource g = test.getG();

        g.E().drop().iterate();
        g.V().drop().iterate();

        Vertex bob = g.addV("person").property("name", "Bob").next();
        Vertex alex = g.addV("person").property("age", 0.29027268300579956).next();
        Vertex jhon = g.addV("person").property("age", 2.94858941E8).next();
        Vertex alice = g.addV("person").property("age", POSITIVE_INFINITY).next();
        Vertex book = g.addV("person").property("name", "book1").next();
        g.V(alex).property("name","Alex").iterate();
        //Vertex alice = g.addV("person").property("age", POSITIVE_INFINITY ).next();

        Edge edge1 = g.addE("knows").from(bob).to(alice).property("d", 0.9,"C",55).next();
        Edge edge2 = g.addE("write").from(alice).to(book).property("d", 0.94461).next();
        //g.E(edge2.id()).property("d", 0.94461).iterate();
        //System.out.println(POSITIVE_INFINITY);
        String query1 = "g.V().order().by(asc)";

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
