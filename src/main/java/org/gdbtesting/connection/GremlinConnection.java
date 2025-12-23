package org.gdbtesting.connection;

import com.baidu.hugegraph.driver.HugeClient;
import org.apache.tinkerpop.gremlin.driver.*;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.gdbtesting.GraphDBConnection;


import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class GremlinConnection implements GraphDBConnection {

    protected Client client;
    protected GraphTraversalSource g;
    protected Cluster cluster;
    protected String version;
    protected String database;
    //protected RequestOptions options;
    protected Graph graph;
    protected HugeClient hugespecial;
    private String filename;

    public GremlinConnection(String version, String database, String filename){
        this.version = version; this.database = database;
        this.filename = filename;
        hugespecial = null;
        connect();
    }

    public GremlinConnection(String version, String database) {
        this.version = version;
        this.database = database;
        hugespecial = null;
        connect();
    }

    public void connect(){
        System.out.println("skip");
    }

    // set request timeout
    /*public GraphTraversalSource getGWithRequestTimeout(long l){
        return g.with(Tokens.ARGS_EVAL_TIMEOUT, l);
    }*/


    // not suggest
    /*public List<Result> getRequest(String query){
        if(client == null || g == null){
            connect();
        }
        List<Result> results = null;
        try {
            results = client.submit(query, options).all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally{
            return results;
        }
    }*/

    public void close(){
        try {
            g.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client getClient() {
        return client;
    }

    public GraphTraversalSource getG() {
        return g;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public String getFilename() {
        return filename;
    }

    public String getVersion() {
        return version;
    }

    public String getDatabase() {
        return database;
    }

    public HugeClient getHugespecial() {return hugespecial;}

    public void setG(GraphTraversalSource g) {
        this.g = g;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String getDatabaseVersion() throws Exception {
        return getDatabase() + "::" + getVersion();
    }
}
