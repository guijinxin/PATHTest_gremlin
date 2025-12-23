package org.gdbtesting.arabgodb;

import com.arangodb.ArangoGraph;
import com.arangodb.tinkerpop.gremlin.structure.ArangoDBGraph;
import com.arangodb.tinkerpop.gremlin.utils.ArangoDBConfigurationBuilder;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.gdbtesting.connection.GremlinConnection;

import java.util.ArrayList;
import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class ArangodbConnection extends GremlinConnection {

    public ArangodbConnection(String version, String filename) {
        super(version, "ArangoDB", filename);
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
            String file = this.getClass().getClassLoader().getResource("conf/arango.yaml").getPath();
            cluster = Cluster.open(file);
            client = cluster.connect();
            setClient(client);
            setCluster(cluster);

            // connect 2
            /*g = traversal().withRemote(DriverRemoteConnection.using(cluster, "g"));
            setG(g);
            setGraph(g.getGraph());*/

            /*ArangoDBConfigurationBuilder builder = new ArangoDBConfigurationBuilder();
            builder.arangoHosts("127.0.0.1:8529")
                    .arangoUser("root")
                    .arangoPassword("123456")
                    .dataBase("tinkerpop");
            builder.graph("graph").
                    shouldPrefixCollectionNamesWithGraphName(false);


            BaseConfiguration conf = builder.build();
            Graph graph1 = GraphFactory.open(conf);
            GraphTraversalSource gts = new GraphTraversalSource(graph1);*/
            /*AbstractConfiguration.setDefaultListDelimiter(' ');
            Configuration config = new PropertiesConfiguration("/mnt/g/GraphSoftWare/newarango-gremlin-server-3.4.10/conf/modern-arangodb.properties");
            ArangoDBGraph graph = ArangoDBGraph.open(config);
            GraphTraversalSource g = new GraphTraversalSource(graph);
            setG(g);
            setGraph(g.getGraph());*/

            g = traversal().withRemote(DriverRemoteConnection.using(cluster, "g"));
            setG(g);
            setGraph(g.getGraph());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        org.gdbtesting.arabgodb.ArangodbConnection test = new org.gdbtesting.arabgodb.ArangodbConnection("0.5.3", "conf/remote-arango.properties");
        GraphTraversalSource g = test.getG();

        g.E().drop().iterate();
        g.V().drop().iterate();

        Vertex Ironman = g.addV("vl1").property("name", "Tony").property("ATK",100.00).next();
        Vertex Superman = g.addV("vl1").property("name", "Clark").property("ATK",Double.POSITIVE_INFINITY).next();
        Vertex Ironman2 = g.addV("vl1").property("name", "Tony3").property("ATK",100.00).next();
        Vertex Superman2 = g.addV("vl1").property("name", "Clark3").property("ATK",Double.POSITIVE_INFINITY).next();
        //List<Result> results = g.V().hasLabel("vl1").toList();
        //Vertex Moly = g.addV("student").property("grade", 9).next();
        //Vertex notebook = g.addV("homework").property("subject", "Math").next();
        /*ArrayList<Object> addpro = new ArrayList<>();
        addpro.add("A");
        addpro.add(10);
        addpro.add("B");
        addpro.add("500");*/

        Edge edge1 = g.addE("el0").from(Ironman).to(Superman).property("C",0).property("B",10).next();
        g.E(edge1.id()).property("A",0.8).iterate();

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