package org.gdbtesting.hugegraph;

import org.gdbtesting.gremlin.GraphGlobalState;
import org.gdbtesting.janusgraph.JanusGraphConnection;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        GraphGlobalState state = new GraphGlobalState(2);
        state.setRemoteFile("conf/remote-hugegraph.properties");
        HugeGraphProvider provider = new HugeGraphProvider(state);
        try{
            provider.generateGraph();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
