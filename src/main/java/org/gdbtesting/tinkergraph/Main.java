package org.gdbtesting.tinkergraph;

import org.gdbtesting.gremlin.GraphGlobalState;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        GraphGlobalState state = new GraphGlobalState(2);
        state.setRemoteFile("conf/remote.yaml");
        TinkerGraphProvider provider = new TinkerGraphProvider(state);
        try {
            provider.generateAndTestDatabase(state);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
