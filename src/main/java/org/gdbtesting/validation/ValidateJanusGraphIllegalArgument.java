package org.gdbtesting.validation;

import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.gdbtesting.connection.GremlinConnection;
import org.gdbtesting.janusgraph.JanusGraphConnection;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ValidateJanusGraphIllegalArgument {

    public static void main(String[] args){
        GremlinConnection connection = new JanusGraphConnection("0.5.3", "conf/remote-janusgraph.properties");
        GraphTraversalSource g = connection.getG();
        Vertex bob = g.addV("person").property("name", "Bob").next();
        Vertex alice = g.addV("person").property("name", "Alice").next();
        Vertex book = g.addV("book").property("name", "book1").next();
        Edge edge1 = g.addE("knows").from(bob).to(alice).next();
        Edge edge2 = g.addE("write").from(alice).to(book).next();
        g.E(edge2).property("duration", new Float(0.94461)).iterate();
        String query1 = "g.E().inV().outE('write').has('duration', eq(0.8446159).or(outside(0.22740966,0.029379308)))";
        System.out.println("query1: " + query1);
        try{
            List<Result> results = connection.getClient().submit(query1).all().get();
            for (Result r : results) {
                System.out.println(r);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String query2 = "g.E().inV().outE('write').has('duration', eq(0.8446159).or(not(outside(0.22740966,0.029379308))))";
        System.out.println("query2: " + query2);
        try{
            List<Result> results = connection.getClient().submit(query2).all().get();
            for (Result r : results) {
                System.out.println(r);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
    }


}
