package org.gdbtesting.orientdb;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.gdbtesting.connection.GremlinConnection;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;

import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class OrientdbConnection extends GremlinConnection {

    public OrientdbConnection(String version, String filename) {
        super(version, "OrientDB", filename);
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
            String file = this.getClass().getClassLoader().getResource("conf/orient.yaml").getPath();
            cluster = Cluster.open(file);
            client = cluster.connect();
            setClient(client);
            setCluster(cluster);

            //OrientGraphFactory factory = new OrientGraphFactory("remote:localhost/demodb","root","123456789");
            //Graph graph = factory.getNoTx();
            OrientGraph graph = OrientGraph.open("remote:localhost/demodb","root","123456789");
            g = graph.traversal();
            GraphTraversalSource clean = traversal().withRemote(DriverRemoteConnection.using(cluster, "g"));
            clean.E().drop().iterate();
            clean.V().drop().iterate();
            // connect 2
            //g = traversal().withRemote(DriverRemoteConnection.using(cluster, "g"));
            setG(g);
            setGraph(g.getGraph());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        org.gdbtesting.orientdb.OrientdbConnection test = new org.gdbtesting.orientdb.OrientdbConnection("0.5.3", "conf/remote-orient.properties");
        GraphTraversalSource g = test.getG();
        Graph graph= test.getGraph();

        g.E().drop().iterate();
        g.V().drop().iterate();

        Vertex Ironman = g.addV("Hero").property("name", "Tony").property("ATK",100.00).next();
        Vertex Superman = g.addV("Hero").property("name", "Clark").property("ATK",Double.POSITIVE_INFINITY).next();
        Edge edge = Ironman.addEdge("Kill",Superman,"Time","Today");
        Vertex Moly = g.addV("student").property("grade", 9).next();
        Vertex notebook = g.addV("homework").property("subject", "Math").next();
        Edge newedge = g.addE("write").from(Moly).to(notebook).property("date","0.8").next();
        g.V(Moly).property("Class","5").iterate();

        String query1 = "g.V()";

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