package com.cmatri;

import java.util.*;
import java.util.stream.Collectors;

public class CubeSolver {
    private static final char[] permutations = {'R', 'r', 'U', 'u', 'F', 'f'};//, 'D', 'd', 'L', 'l', 'B', 'b'};

    private static String findPath(HashMap<String, Integer> moves, CubeState state) {
        StringBuilder path = new StringBuilder();
        String s = String.valueOf(state.getState());
        while (moves.get(s) != null) {
            int m = moves.get(s);
            state.move(permutations[m % 2 == 0 ? m + 1 : m - 1]);
            s = String.valueOf(state.getState());
            path.append(permutations[m % 2 == 0 ? m + 1 : m - 1]);
        }
        return path.toString();
    }

    private static String invert(String alg) {
        return new StringBuilder().append(alg).reverse().chars().map(c -> (Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c))).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public static String solveCube(CubeState state) {
        String end = String.valueOf(CubeState.solvedState);
        String start = String.valueOf(state.getState());
        HashMap<String, Integer> movesForward = new HashMap<>();
        HashMap<String, Integer> movesBackward = new HashMap<>();
        Deque<String> forward = new ArrayDeque<>();
        Deque<String> backward = new ArrayDeque<>();

        movesForward.put(end, null);
        movesBackward.put(start, null);
        forward.add(end);
        backward.add(start);
        while (true) {
            end = forward.remove();
            for (int i = 0; i < permutations.length; i++) {
                state.setState(end.toCharArray());
                state.move(permutations[i]);
                String s = String.valueOf(state.getState());
                if (!movesForward.containsKey(s)) {
                    movesForward.put(s, i);
                    forward.add(s);
                }
            }

            start = backward.remove();
            for (int i = 0; i < permutations.length; i++) {
                state.setState(start.toCharArray());
                state.move(permutations[i]);
                String s = String.valueOf(state.getState());
                if (!movesBackward.containsKey(s)) {
                    movesBackward.put(s, i);
                    backward.add(s);
                }
                if (movesForward.containsKey(s)) { // found path
                    String f = findPath(movesForward, state);
                    state.setState(s.toCharArray());
                    String b = findPath(movesBackward, state);
                    state.setState(s.toCharArray());
                    return invert(b) + f;
                }
            }
        }
    }
}
