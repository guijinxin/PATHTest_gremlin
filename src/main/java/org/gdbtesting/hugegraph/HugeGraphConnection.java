package org.gdbtesting.hugegraph;


import org.apache.hugegraph.driver.*;

import org.gdbtesting.connection.GremlinConnection;

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

        String query1 = "g.V().repeat(__.in('el1')).emit().times(2).inE('el0','el2','el1').order().by(asc)";

        System.out.println("query0 : " + query1);
        try {
            org.apache.hugegraph.structure.gremlin.ResultSet hugeResult = gremlin.gremlin(query1).execute();
            Iterator<org.apache.hugegraph.structure.gremlin.Result> huresult = hugeResult.iterator();
            huresult.forEachRemaining(result -> {
                Object object = result.getObject();
                System.out.println(object);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
