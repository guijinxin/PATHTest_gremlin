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

public class ValidateHugeGraphPredicate {
    public static void main(String[] args) {
        GremlinConnection connection = new HugeGraphConnection("0.11.2", "conf/remote-hugegraph.properties");
        try {
            /** create schem */
            // property
            connection.getHugespecial().schema().propertyKey("length").asInt().ifNotExist().create();

            // vertex
            connection.getHugespecial().schema().vertexLabel("rope").properties("length").nullableKeys("length").create();
            connection.getHugespecial().schema().indexLabel("ropebylength").onV("rope").by("length").shard().ifNotExist().create();

            GraphManager graph = connection.getHugespecial().graph();

            /** create graph data */
            Vertex rope1 = new Vertex("rope").property("length", 546);
            Vertex rope2 = new Vertex("rope").property("length", 12368578);
            Vertex rope3 = new Vertex("rope").property("length", 1);
            Vertex rope4 = new Vertex("rope").property("length", 47568);


            graph.addVertices(Arrays.asList(rope1, rope2, rope3, rope4));

            GremlinManager gremlin = connection.getHugespecial().gremlin();

            String query0 = "g.V().has('length', not(between(423,-23)))";
            System.out.println("query0 : " + query0);
            try {
                ResultSet hugeResult = gremlin.gremlin(query0).execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            String query1 = "g.V().has('rope', 'length', outside(423,-23))";
            System.out.println("query1 : " + query1);
            try {
                ResultSet hugeResult = gremlin.gremlin(query1).execute();
                Iterator<Result> huresult = hugeResult.iterator();
                huresult.forEachRemaining(result -> {
                    Object object = result.getObject();
                    System.out.println(object);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
