package org.gdbtesting.validation;

import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.driver.GremlinManager;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import com.baidu.hugegraph.structure.gremlin.Result;
import com.baidu.hugegraph.structure.gremlin.ResultSet;
import org.gdbtesting.connection.GremlinConnection;
import org.gdbtesting.hugegraph.HugeGraphConnection;

import java.util.Arrays;
import java.util.Iterator;

public class ValidateHugeGraphBetween {

    public static void main(String[] args) {
        GremlinConnection connection = new HugeGraphConnection("0.11.2", "conf/remote-hugegraph.properties");
        try{
            /** create schem */
            // property
            connection.getHugespecial().schema().propertyKey("name").asText().ifNotExist().create();
            connection.getHugespecial().schema().propertyKey("location").asText().ifNotExist().create();
            connection.getHugespecial().schema().propertyKey("mood").asText().ifNotExist().create();

            // vertex
            connection.getHugespecial().schema().vertexLabel("person").properties("name").nullableKeys("name").create();
            connection.getHugespecial().schema().indexLabel("personbyname").onV("person").by("name").shard().ifNotExist().create();
            connection.getHugespecial().schema().vertexLabel("book").properties("name").nullableKeys("name").create();
            connection.getHugespecial().schema().indexLabel("bookbyname").onV("book").by("name").shard().ifNotExist().create();
            // edge
            connection.getHugespecial().schema().edgeLabel("read").sourceLabel("person").targetLabel("book").properties("location", "mood").ifNotExist().create();
            connection.getHugespecial().schema().indexLabel("readByLocation").onE("read").by("location").shard().ifNotExist().create();
            connection.getHugespecial().schema().indexLabel("readByMood").onE("read").by("mood").search().ifNotExist().create();

            GraphManager graph = connection.getHugespecial().graph();

            /** create graph data */
            Vertex nancy = new Vertex("person").property("name", "nancy");
            Vertex book1 = new Vertex("book").property("name", "book1");
            Vertex book2 = new Vertex("book").property("name", "book2");
            Vertex book3 = new Vertex("book").property("name", "book3");
            graph.addVertices(Arrays.asList(nancy, book1, book2, book3));

            Edge edge1 = new Edge("read").source(nancy).target(book1).property("location", "location1").property("mood", "good");
            Edge edge2 = new Edge("read").source(nancy).target(book2).property("location", "location2").property("mood", "bad");
            Edge edge3 = new Edge("read").source(nancy).target(book3).property("location", "location3").property("mood", "good");
            graph.addEdges(Arrays.asList(edge1, edge2, edge3));

            GremlinManager gremlin = connection.getHugespecial().gremlin();
            System.out.println("query 1: g.E().has('read', 'location', between('location1', 'location2'))");
            try{
                ResultSet hugeResult = gremlin.gremlin("g.E().has('read', 'location', between('location1', 'location2'))").execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            }catch(Exception e){
                e.printStackTrace();
            }

            System.out.println("query 2: g.E().has('read', 'mood', between('good', 'good'))");
            try{
                ResultSet hugeResult1 = gremlin.gremlin("g.E().has('read', 'mood', between('good', 'good'))").execute();
                Iterator<Result> huresult1 = hugeResult1.iterator();
                huresult1.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }

    }

}
