package org.gdbtesting.janusgraph;

import org.gdbtesting.gremlin.GraphGlobalState;
import org.gdbtesting.janusgraph.gen.JanusGraphAddVertexGeneration;

public class Main {

    public static void main(String[] args) {
        GraphGlobalState state = new GraphGlobalState(2);
        state.setRemoteFile("conf/janusgraph.yaml");
        JanusGraphProvider provider = new JanusGraphProvider(state);
        try{
            provider.generateGraph();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
