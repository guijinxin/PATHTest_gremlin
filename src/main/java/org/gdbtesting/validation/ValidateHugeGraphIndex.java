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

public class ValidateHugeGraphIndex {

    public static void main(String[] args) {
        GremlinConnection connection = new HugeGraphConnection("0.11.2", "conf/remote-hugegraph.properties");
        try{
            /** create schem */
            // property
            connection.getHugespecial().schema().propertyKey("name").asText().ifNotExist().create();
            connection.getHugespecial().schema().propertyKey("age").asInt().ifNotExist().create();

            // vertex
            connection.getHugespecial().schema().vertexLabel("person").properties("name","age").nullableKeys("name","age").create();
            connection.getHugespecial().schema().indexLabel("personbyname").onV("person").by("name").shard().ifNotExist().create();
            connection.getHugespecial().schema().indexLabel("personbyage").onV("person").by("age").shard().ifNotExist().create();

            GraphManager graph = connection.getHugespecial().graph();

            /** create graph data */
            Vertex person1 = new Vertex("person").property("name", "person1").property("age", 16);
            Vertex person2 = new Vertex("person").property("name", "person2").property("age", 15);
            Vertex person3 = new Vertex("person").property("name", "person3").property("age", 23);
            Vertex person4 = new Vertex("person").property("name", "person4").property("age", 46);


            graph.addVertices(Arrays.asList(person1, person2, person3, person4));

            GremlinManager gremlin = connection.getHugespecial().gremlin();

            String query0 = "g.V().has('person', 'name', neq('person1'))";
            System.out.println("query0 : " + query0);
            try{
                ResultSet hugeResult = gremlin.gremlin(query0).execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            }catch(Exception e){
                e.printStackTrace();
            }

            String query1 = "g.V().has('person', 'age', lt(30)).has('name', is(neq('person1')))";
            System.out.println("query1 : " + query1);
            try{
                ResultSet hugeResult = gremlin.gremlin(query1).execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            }catch(Exception e){
                e.printStackTrace();
            }

            String query2 = "g.V().where(values('name').is(neq('person1')))";
            System.out.println("query2 : " + query2);
            try{
                ResultSet hugeResult = gremlin.gremlin(query2).execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            }catch(Exception e){
                e.printStackTrace();
            }

            String query3 = "g.V().has('person', 'age', lt(30)).where(values('name').is(neq('person1')))";
            System.out.println("query3 : " + query3);
            try{
                ResultSet hugeResult = gremlin.gremlin(query3).execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }

    }
}
