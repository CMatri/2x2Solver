package com.cmatri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CubeSolver {
    private static final char[] permutations = {'R', 'r', 'U', 'u', 'F', 'f'};

    public static char[] solveCube(CubeState state) {
        char[] end = CubeState.solvedState;
        char[] start = state.getState();
        char[] buf = {};
        boolean solved = false;
        Map<char[], Integer> moves = new HashMap<>();
        ArrayList<char[]> queue = new ArrayList<>();

        queue.add(start);
        while(queue.size() > 0) {
            buf = queue.remove(0);
            if(Arrays.equals(buf, end)) {
                System.out.println("Solved cube!");
                solved = true;
                break;
            }
            for(int i = 0; i < permutations.length; i++) {
                state.setState(buf);
                state.move(permutations[i]);
                if(!moves.containsKey(state.getState())){
                    queue.add(state.getState());
                    if(Arrays.equals(state.getState(), end))
                        moves.put(end, i % 2 == 0 ? i + 1 : i - 1);
                    else
                        moves.put(state.getState(), i % 2 == 0 ? i + 1 : i - 1);
                }
            }
        }

        if(solved) {
            buf = end;
            state.setState(buf);
            Integer move = moves.get(end);
            StringBuilder ans = new StringBuilder();
            while(move != null) {
                System.out.println(buf);
                state.move(permutations[move]);
                ans.append(permutations[move % 2 == 0 ? move + 1 : move - 1] + " ");
                buf = state.getState();
                for(char[] c : moves.keySet())
                    if(Arrays.equals(buf, c))
                        buf = c;
                move = moves.get(buf);
            }
            System.out.println("Solution: " + ans.toString());
        }

        return buf;
    }
}
