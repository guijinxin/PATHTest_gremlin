package org.gdbtesting;

import org.gdbtesting.gremlin.GraphGlobalState;
import org.gdbtesting.gremlin.GremlinGraphProvider;
import org.gdbtesting.janusgraph.JanusGraphProvider;

import java.util.Scanner;

public class Starter {

    public static void main(String[] args) {
        if (args.length != 6)
            System.out.println("Missing Parameters!, 1.QueryDepth, 2.VerMaxNum, 3.EdgeMaxNum, 4.EdgeLabelNum, 5.VerLabelNum, 6.QueryNum");
        GraphGlobalState state = new GraphGlobalState(Integer.parseInt(args[0]));

        state.setVerticesMaxNum(Integer.parseInt(args[1]));
        state.setEdgesMaxNum(Integer.parseInt(args[2]));
        state.setEdgeLabelNum(Integer.parseInt(args[3]));
        state.setVertexLabelNum(Integer.parseInt(args[4]));
        state.setQueryNum(Integer.parseInt(args[5]));
        state.setRepeatTimes(1);
        /*state.setGenerateDepth(5);
        state.setVerticesMaxNum(100);
        state.setEdgesMaxNum(200);
        state.setEdgeLabelNum(20);
        state.setVertexLabelNum(20);
        state.setQueryNum(10);*/
        GremlinGraphProvider provider = new GremlinGraphProvider(state);

        //Scanner input = new Scanner(System.in);
        //int i = input.nextInt();
        try {
            provider.generateAndTestDatabase(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //int j = input.nextInt();
        System.exit(0);
    }
}
