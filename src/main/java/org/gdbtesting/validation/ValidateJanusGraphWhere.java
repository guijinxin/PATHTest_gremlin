package org.gdbtesting.validation;

import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.gdbtesting.connection.GremlinConnection;
import org.gdbtesting.janusgraph.JanusGraphConnection;

import java.util.List;

public class ValidateJanusGraphWhere {

    public static void main(String[] args) {
        GremlinConnection connection = new JanusGraphConnection("0.5.3", "conf/remote-janusgraph.properties");
        GraphTraversalSource g = connection.getG();
        Vertex bob = g.addV("person").property("name", "Bob").next();
        Vertex alice = g.addV("person").property("name", "Alice").next();
        Vertex book = g.addV("book").property("name", "book1").next();
        Edge edge1 = g.addE("knows").from(bob).to(alice).next();
        Edge edge2 = g.addE("read").from(alice).to(book).next();
        g.E(edge2).property("duration", 246.7968768650599669).iterate();
        String query = "g.E().where(values('duration').sum().is(inside(223.7968768650599669,325.9464473371709095)))";
        System.out.println("query: " + query);
        try{
            List<Result> results = connection.getClient().submit(query).all().get();
            for (Result r : results) {
                System.out.println(r);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
    }
}
