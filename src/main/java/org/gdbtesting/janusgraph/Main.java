package org.gdbtesting.janusgraph;

import org.gdbtesting.gremlin.GraphGlobalState;
import org.gdbtesting.janusgraph.gen.JanusGraphAddVertexGeneration;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
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
