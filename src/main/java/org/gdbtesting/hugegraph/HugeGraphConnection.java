package org.gdbtesting.hugegraph;

import com.baidu.hugegraph.HugeGraph;
import com.baidu.hugegraph.driver.*;
import com.baidu.hugegraph.structure.constant.T;
import com.baidu.hugegraph.structure.graph.Path;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.gdbtesting.connection.GremlinConnection;
import com.baidu.hugegraph.HugeFactory;
import org.gdbtesting.tinkergraph.TinkerGraphConnection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class HugeGraphConnection extends GremlinConnection {

    public HugeClient getHugeClient() {
        return hugeClient;
    }

    private HugeClient hugeClient;

    public void setup(){
        try {
            HugeClientBuilder builder = new HugeClientBuilder("http://localhost:8080", "hugegraph");
            HugeClient hugeClient = new HugeClient(builder);
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
        HugeGraphConnection connection = new HugeGraphConnection("0.11.2", "conf/hugegraph.yaml");
        HugeClient hugegraph = connection.getHugespecial();

        /*hugegraph.schema().propertyKey("location").asText().ifNotExist().create();
        hugegraph.schema().propertyKey("mood").asText().ifNotExist().create();
        hugegraph.schema().indexLabel("locationindex").onE("read1").by("length4").shard().ifNotExist().create();
        hugegraph.schema().edgeLabel("read1").sourceLabel("rope1").targetLabel("rope1").properties("location", "mood").ifNotExist().create();
        hugegraph.schema().edgeLabel("read2").sourceLabel("rope2").targetLabel("rope2").properties("location", "mood").ifNotExist().create();
        hugegraph.schema().edgeLabel("read3").sourceLabel("rope3").targetLabel("rope3").properties("location", "mood").ifNotExist().create();
        hugegraph.schema().edgeLabel("read4").sourceLabel("rope4").targetLabel("rope4").properties("location", "mood").ifNotExist().create();

        hugegraph.schema().propertyKey("length1").asInt().ifNotExist().create();
        hugegraph.schema().propertyKey("length2").asInt().ifNotExist().create();
        hugegraph.schema().propertyKey("length3").asInt().ifNotExist().create();
        hugegraph.schema().propertyKey("length4").asInt().ifNotExist().create();


        hugegraph.schema().vertexLabel("rope1").properties("length1").nullableKeys("length1").create();
        hugegraph.schema().vertexLabel("rope2").properties("length2").nullableKeys("length2").create();
        hugegraph.schema().vertexLabel("rope3").properties("length3").nullableKeys("length3").create();
        hugegraph.schema().vertexLabel("rope4").properties("length4").nullableKeys("length4").create();

        hugegraph.schema().indexLabel("ropebylength1").onV("rope1").by("length1").shard().ifNotExist().create();
        hugegraph.schema().indexLabel("ropebylength2").onV("rope2").by("length2").shard().ifNotExist().create();
        hugegraph.schema().indexLabel("ropebylength3").onV("rope3").by("length3").shard().ifNotExist().create();
        hugegraph.schema().indexLabel("ropebylength4").onV("rope4").by("length4").shard().ifNotExist().create();

        GraphManager graph = hugegraph.graph();

        com.baidu.hugegraph.structure.graph.Vertex rope1 = new com.baidu.hugegraph.structure.graph.Vertex("rope1").property("length1", 546);
        com.baidu.hugegraph.structure.graph.Vertex rope2 = new com.baidu.hugegraph.structure.graph.Vertex("rope2").property("length2", 12368578);
        com.baidu.hugegraph.structure.graph.Vertex rope3 = new com.baidu.hugegraph.structure.graph.Vertex("rope3").property("length3", 1);
        com.baidu.hugegraph.structure.graph.Vertex rope4 = new com.baidu.hugegraph.structure.graph.Vertex("rope4").property("length4", 47568);

        graph.addVertices(Arrays.asList(rope1, rope2, rope3, rope4));*/

        GremlinManager gremlin = hugegraph.gremlin();

        //String query0 = "g.V().has('length', between(-433,2678).or(gte(500))).count()";

        //String query1 = "g.V().outE('read2','read3','read4').hasLabel('read3','read2')";
        //java.lang.IllegalStateException: Illegal key 'LABEL' with more than one value "g.V().outE('el4','el3','el6','el5').hasNot('ep6').hasLabel('el0','el2','el3','el5')"

        //String query1 = "g.V().inE('read2','read3','read4').has('location','8672904391940353816')";
        //"g.V().inE('el0','el2','el1').has('ep2',0.5323262).inV()"0
        //"g.V().inE('el0','el2','el1','el4','el3').has('ep1', outside(-1596127769,-1118851916)).inV()"
        //ep2 float.
        //ep1 int.
        //java.lang.ClassCastException: java.util.Arrays$ArrayList cannot be cast to com.baidu.hugegraph.backend.id.Id

        String query3 = "g.V().has('vp1', inside(0.24070676216155018,4.13998472E8)).inE('el0','el4').outV().not(__.values('vp1'))";
        String query1 = "g.V().or(__.order().by(asc)).has('vp1',0.5755699003757928).inE('el4').outV()";
        String query5 = "g.V().both('el0','el2','el1','el4','el3').has('vp1',0.11551311473911341).order().by(desc).and(__.both('el0','el2','el1','el4'))";
        //vp1: DOUBLE
        //java.util.concurrent.ExecutionException: org.apache.tinkerpop.gremlin.driver.exception.ResponseException: java.lang.NumberFormatException
        //huge, janus good
        //java.util.concurrent.ExecutionException: org.apache.tinkerpop.gremlin.driver.exception.ResponseException: Character I is neither a decimal digit number, decimal point, nor "e" notation exponential mark.


        String query6 = "g.V().and(__.count()).has('vp0', between('0.9856602131667698','')).where(__.both('el2','el1','el4')).where(__.order().by(asc))";
        //java.lang.IllegalArgumentException: Can't compare between 1 >= 0.9856602131667698 and 1 <
        //vp0 ,String


        String query7 = "g.E().order().by(asc).has('el3', 'ep0',gt(-5412887541077074472).or(gt(-3254932318483141829).and(lt(5105389143100659920).or(neq(5536331743981638845)).and(lt(5465745263204545746))).or(between(774759410783949765,5586137265780110837)))).order().by(asc)";
        //"g.E().order().by(asc).has('ep1')"
        //janus orient good
        //class org.apache.tinkerpop.gremlin.arcadedb.structure.ArcadeEdge cannot be cast to class java.lang.Comparable
        System.out.println("query0 : " + query1);
        try {
            com.baidu.hugegraph.structure.gremlin.ResultSet hugeResult = gremlin.gremlin(query1).execute();
            Iterator< com.baidu.hugegraph.structure.gremlin.Result> huresult = hugeResult.iterator();
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
