package org.gdbtesting.validation;

import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.driver.GremlinManager;
import com.baidu.hugegraph.structure.graph.Vertex;
import com.baidu.hugegraph.structure.gremlin.Result;
import com.baidu.hugegraph.structure.gremlin.ResultSet;
import org.gdbtesting.connection.GremlinConnection;
import org.gdbtesting.hugegraph.HugeGraphConnection;

import java.util.Arrays;
import java.util.Iterator;

public class ValidateHugeGraphOutside {
    public static void main(String[] args) {
        GremlinConnection connection = new HugeGraphConnection("0.11.2", "conf/remote-hugegraph.properties");
        try{
            /** create schem */
            // property
            connection.getHugespecial().schema().propertyKey("married").asBoolean().ifNotExist().create();

            // vertex
            connection.getHugespecial().schema().vertexLabel("person").properties("married").nullableKeys("married").create();
            connection.getHugespecial().schema().indexLabel("personbymarried").onV("person").by("married").shard().ifNotExist().create();

            GraphManager graph = connection.getHugespecial().graph();

            /** create graph data */
            Vertex person1 = new Vertex("person").property("married", true);
            Vertex person2 = new Vertex("person").property("married", false);
            Vertex person3 = new Vertex("person").property("married", false);
            Vertex person4 = new Vertex("person").property("married", false);


            graph.addVertices(Arrays.asList(person1, person2, person3, person4));

            GremlinManager gremlin = connection.getHugespecial().gremlin();

            String query0 = "g.V().has('person', 'married', outside(true, true))";
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


        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }

    }
}
