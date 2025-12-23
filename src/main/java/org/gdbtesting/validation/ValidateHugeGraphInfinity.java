package org.gdbtesting.validation;

import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import org.gdbtesting.connection.GremlinConnection;
import org.gdbtesting.hugegraph.HugeGraphConnection;

import java.util.Arrays;

public class ValidateHugeGraphInfinity {
    public static void main(String[] args) {
        GremlinConnection connection = new HugeGraphConnection("0.11.2", "conf/remote-hugegraph.properties");
        try{
            /** create schem */
            // property
            connection.getHugespecial().schema().propertyKey("name").asText().ifNotExist().create();
            connection.getHugespecial().schema().propertyKey("price").asDouble().ifNotExist().create();

            // vertex
            connection.getHugespecial().schema().vertexLabel("person").properties("name").nullableKeys("name").create();
            connection.getHugespecial().schema().indexLabel("personbyname").onV("person").by("name").shard().ifNotExist().create();
            connection.getHugespecial().schema().vertexLabel("product").properties("name","price").nullableKeys("name","price").create();
            connection.getHugespecial().schema().indexLabel("productbyname").onV("product").by("name").shard().ifNotExist().create();
            connection.getHugespecial().schema().indexLabel("productbyprice").onV("product").by("price").shard().ifNotExist().create();

            // edge
            connection.getHugespecial().schema().edgeLabel("buy").sourceLabel("person").targetLabel("product").ifNotExist().create();

            GraphManager graph = connection.getHugespecial().graph();

            /** create graph data */
            Vertex nancy = new Vertex("person").property("name", "nancy");
            Vertex product1 = new Vertex("product").property("name", "product1").property("price", 3.254615);
            Vertex product2 = new Vertex("product").property("name", "product2").property("price", new Double(1029.98 / 0));

            graph.addVertices(Arrays.asList(nancy, product1, product2));

            Edge edge1 = new Edge("buy").source(nancy).target(product1);
            Edge edge2 = new Edge("read").source(nancy).target(product2);
            graph.addEdges(Arrays.asList(edge1, edge2));

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }

    }
}
