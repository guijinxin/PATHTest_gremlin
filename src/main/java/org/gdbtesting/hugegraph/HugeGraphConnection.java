package org.gdbtesting.hugegraph;


import org.apache.hugegraph.driver.*;

import org.apache.hugegraph.structure.gremlin.ResultSet;
import org.gdbtesting.connection.GremlinConnection;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Iterator;


public class HugeGraphConnection extends GremlinConnection {

    public HugeClient getHugeClient() {
        return hugeClient;
    }

    private HugeClient hugeClient;

    public void setup(){
        try {
            HugeClient client = HugeClient.builder(
                    "http://localhost:8080",
                    "hugegraph"
            ).build();

            GraphManager graph = hugeClient.graph();
            System.out.println(graph.graph());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            // connect 1
            /*cluster = Cluster.open("/mnt/g/gdbtesting/src/main/resources/conf/hugegraph.yaml");
            client = cluster.connect();
            setClient(client);
            setCluster(cluster);*/

            HugeClient hugeClient = HugeClient.builder("http://127.0.0.1:8080", "hugegraph").build();
            hugespecial = hugeClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HugeGraphConnection(String version, String filename) {
        super(version, "HugeGraph", filename);
    }

    public static void main(String[] args) {
        HugeGraphConnection connection = new HugeGraphConnection("1.7.0", "conf/hugegraph.yaml");
        HugeClient hugegraph = connection.getHugespecial();
        GremlinManager gremlin = hugegraph.gremlin();

        String query1 = "g.E().outV().repeat(__.both()).times(1).where(__.outE().count().is(lt(-3))).count()"
               ;


        String query2 = " g.V().match(__.as('start1').repeat(__.inE('el0')).times(1).as('m1')).select('m1').hasLabel('el0','el2').hasLabel('el1')" ;
        try {
            org.apache.hugegraph.structure.gremlin.ResultSet hugeResult = gremlin.gremlin(query1).execute();
            org.apache.hugegraph.structure.gremlin.ResultSet hugeResult1 = gremlin.gremlin(query2).execute();
            Iterator<org.apache.hugegraph.structure.gremlin.Result> huresult = hugeResult.iterator();
            Iterator<org.apache.hugegraph.structure.gremlin.Result> huresult1 = hugeResult1.iterator();
            while (huresult.hasNext() || huresult1.hasNext()) {
                if (huresult.hasNext()) {
                    Object object = huresult.next().getObject();
                    System.out.println(object);
                }
                if (huresult1.hasNext()) {
                    Object object1 = huresult1.next().getObject();
                    System.out.println(object1);
                }
            }
//            huresult.forEachRemaining(result -> {
//                Object object = result.getObject();
//                System.out.println(object);
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
