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

public class ValidateHugeGraphOrder {
    public static void main(String[] args) {
        GremlinConnection connection = new HugeGraphConnection("0.11.2", "conf/remote-hugegraph.properties");
        try {
            /** create schem */
            // property
            connection.getHugespecial().schema().propertyKey("name").asText().ifNotExist().create();
            connection.getHugespecial().schema().propertyKey("price").asDouble().ifNotExist().create();

            // vertex
            connection.getHugespecial().schema().vertexLabel("product").properties("name", "price").nullableKeys("name", "price").create();
            connection.getHugespecial().schema().indexLabel("productbyname").onV("product").by("name").shard().ifNotExist().create();
            connection.getHugespecial().schema().indexLabel("productbyprice").onV("product").by("price").shard().ifNotExist().create();

            GraphManager graph = connection.getHugespecial().graph();

            /** create graph data */
            Vertex product1 = new Vertex("product").property("name", "product1").property("price", 3.254615);
            Vertex product2 = new Vertex("product").property("name", "product2").property("price", 4.5688);

            graph.addVertices(Arrays.asList(product1, product2));

            GremlinManager gremlin = connection.getHugespecial().gremlin();
            String query = "g.V().order().by(asc)";
            System.out.println("query: " + query);
            try{
                ResultSet hugeResult = gremlin.gremlin(query).execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            }catch(Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
