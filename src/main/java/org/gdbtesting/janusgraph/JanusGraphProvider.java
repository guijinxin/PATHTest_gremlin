package org.gdbtesting.janusgraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.gdbtesting.GraphDB;
import org.gdbtesting.Randomly;
import org.gdbtesting.gremlin.GremlinGraphProvider;
import org.gdbtesting.gremlin.GraphGlobalState;
import org.gdbtesting.gremlin.gen.*;
import org.gdbtesting.gremlin.query.GraphTraversalGenerator;
import org.gdbtesting.janusgraph.gen.JanusGraphAddVertexGeneration;


import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class JanusGraphProvider extends GremlinGraphProvider {



    private static final GraphDB DB = GraphDB.JANUSGRAPH;
    private JanusGraphConnection connection;
    private GraphTraversalSource g;

    public Graph getGraph() {
        return graph;
    }

    public JanusGraphProvider(GraphGlobalState globalState) {
        super(globalState);
    }

    private Graph graph;

    private enum Action {
        ADD_VERTEX_PROPERTY,
        /*ADD_EDGE_PROPERTY,
        ALTER_EDGE_PROPERTY,
        ALTER_VERTEX_PROPERTY,
        DROP_VERTEX,
        DROP_EDGE*/;

    }

    private int mapActions(Action a){
        Randomly r = state.getRandomly();
        int number = 0;
        switch (a){
            case ADD_VERTEX_PROPERTY:
                number = r.getInteger(0, (int) state.getVerticesMaxNum());
                break;
            /*case ADD_EDGE_PROPERTY:
                number = r.getInteger(0, (int) state.getEdgesMaxNum());
                break;
            case ALTER_VERTEX_PROPERTY:
                number = r.getInteger(0, 8);
                break;
            case ALTER_EDGE_PROPERTY:
                number = r.getInteger(0, 9);
                break;
            case DROP_EDGE:
                number = r.getInteger(0, 3);
                break;
            case DROP_VERTEX:
                number = r.getInteger(0, 5);
                break;*/
            default:
                throw new AssertionError(a);
        }
        return number;
    }

    public void executeActions(){
        Action[] actions = Action.values();
        int number = 0;
        for(Action a : actions){
            number = mapActions(a);
            if(number != 0){
                executeAction(a, number);
            }
        }
    }

    public void executeAction(Action a, int number){
        switch (a){
            case ADD_VERTEX_PROPERTY:
                addVertexAndProperty(number);
                break;
            /*case ADD_EDGE_PROPERTY:
                addEdgeAndProperty(number);
                break;
            case ALTER_VERTEX_PROPERTY:
                alterVertexProperty(number);
                break;
            case ALTER_EDGE_PROPERTY:
                alterEdgeProperty(number);
                break;
            case DROP_EDGE:
                drop(number, "edge");
                break;
            case DROP_VERTEX:
                drop(number, "vertex");
                break;*/

            default:
                throw new AssertionError(a);
        }
    }

    public void addVertexAndProperty(int number){
        JanusGraphAddVertexGeneration addV = new JanusGraphAddVertexGeneration(state);
        addV.addVertices(number);
    }

    public void addEdgeAndProperty(int number){
        GraphAddEdgeAndPropertyGenerator addE = new GraphAddEdgeAndPropertyGenerator(state);
        addE.generateEdgesAndProperties(number);
    }

    public void alterVertexProperty(int number){
        GraphAlterVertexPropertyGeneration avpg = new GraphAlterVertexPropertyGeneration(state);
        for(int i = 0; i < number; i++){
            avpg.alterVertexProperty();
        }
    }

    public void alterEdgeProperty(int number){
        GraphAlterEdgePropertyGenerator aepg = new GraphAlterEdgePropertyGenerator(state);
        for(int i = 0; i < number; i++){
            aepg.alterEdgeProperty();
        }
    }

    public void drop(int number, String type){
        GraphDropGenerator dvg = new GraphDropGenerator(state);
        switch (type){
            case "edge":
                for(int i = 0; i < number; i++){
                    dvg.dropEdge();
                }
            case "vertex":
                for(int i = 0; i < number; i++){
                    dvg.dropVertex();
                }
        }
    }





    public String getDBMSName() {
        return DB.toString();
    }


    public void generateGraph() throws Exception {
        createGraph();
        createGraphSchema();
        //generate graph data
        executeActions();
    }


    public void generateRandomlyTest(){
        GraphTraversalGenerator gtg = new GraphTraversalGenerator(state);
        gtg.generateRandomlyTraversal();
    }



    public void createGraph() {
        connection = new JanusGraphConnection("0.5.3", "conf/remote-janusgraph.properties");
        state.setConnection(connection);
        g = connection.getG();
        if(g.getGraph() != null) {
            GraphDropGenerator dropGenerator = new GraphDropGenerator(state);
            dropGenerator.dropAllVertex();
            //g = traversal().withEmbedded(TinkerGraph.open());
        }
        graph = connection.getGraph();
    }

    public GraphDB getDB() {
        return DB;
    }

    public JanusGraphConnection getConnection() {
        return connection;
    }

    public String getVersion() {
        return version;
    }
}
