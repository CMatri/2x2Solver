package com.cmatri;

/*
    #####
    #0 1#
    # W #
    #2 3#
#############
#16 #4 5#20 #
# O # G # R #
# 19#6 7# 23#
#############
    #8 9#
    # Y #
    #   #
    #####
    #12 #
    # B #
    # 15#
    #####

 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CubeState {
    private char[] state;
    private Consumer<char[]> uiUpdateHandle;

    public static final char W = 'w';
    public static final char Y = 'y';
    public static final char R = 'r';
    public static final char G = 'g';
    public static final char B = 'b';
    public static final char O = 'o';

    public static final Integer[] R_TRANS = {0, 13, 2, 15, 4, 1, 6, 3, 8, 5, 10, 7, 12, 9, 14, 11, 16, 17, 18, 19, 21, 23, 22, 20}; // inter layer interaction not working.
    public static final Integer[] L_TRANS = {12, 1, 14, 3, 0, 5, 2, 7, 4, 9, 6, 11, 8, 13, 10, 15, 17, 19, 18, 16, 20, 21, 22, 23};
    public static final Integer[] F_TRANS = {0, 1, 20, 22, 5, 7, 6, 4, 17, 19, 10, 11, 12, 13, 14, 15, 16, 3, 18, 2, 9, 21, 8, 23};
    public static final Integer[] B_TRANS = {18, 16, 2, 3, 4, 5, 6, 7, 8, 9, 23, 21, 14, 12, 15, 13, 10, 17, 11, 19, 20, 0, 22, 1};
    public static final Integer[] U_TRANS = {1, 3, 0, 2, 16, 17, 6, 7, 8, 9, 10, 11, 12, 13, 21, 20, 15, 14, 18, 19, 4, 5, 22, 23};
    public static final Integer[] D_TRANS = {0, 1, 2, 3, 4, 5, 23, 21, 9, 11, 8, 10, 18, 16, 14, 15, 6, 17, 7, 19, 20, 12, 22, 13};

    private Map<Character, Integer[]> permMap = new HashMap<Character, Integer[]>() {{
        put('u', U_TRANS);
        put('d', D_TRANS);
        put('f', F_TRANS);
        put('b', B_TRANS);
        put('r', R_TRANS);
        put('l', L_TRANS);
    }};

    private static final char[] solvedState = {W, W, W, W, G, G, G, G, Y, Y, Y, Y, B, B, B, B, O, O, O, O, R, R, R, R};
    private static final int L_OFFSET = 16;
    private static final int R_OFFSET = 20;
    private static final int U_OFFSET = 0;
    private static final int D_OFFSET = 8;
    private static final int F_OFFSET = 4;
    private static final int B_OFFSET = 12;

    public CubeState(Consumer<char[]> uiUpdateHandle) {
        this.state = new char[24];
        this.uiUpdateHandle = uiUpdateHandle;
        setSolved();
    }

    public char[] getState() {
        return this.state;
    }

    public void algorithm(char[] alg) {
        for(char c : alg) move(c);
    }

    public void algorithm(String alg) {
        for(int i = 0; i < alg.length(); i++) move(alg.charAt(i));
    }

    public void move(char face) {
        if (!permMap.containsKey(face)) return;
        Integer[] perm = permMap.get(face);
        char[] buf = state.clone();
        for (int i = 0; i < state.length; i++) {
            buf[perm[i]] = state[i];
        }
        state = buf;
        uiUpdateHandle.accept(state);
    }

    public void moveInverse(char face) {
        if (!permMap.containsKey(face)) return;
        Integer[] perm = permMap.get(face);
        char[] buf = state.clone();
        for (int i = 0; i < state.length; i++) {
            buf[i] = state[perm[i]];
        }
        state = buf;
    }

    public void setSolved() {
        state = solvedState.clone();
        uiUpdateHandle.accept(state);
    }

    public void printState() {
        char[] top = getSideValues(U_OFFSET);
        char[] middle = getSideValues(L_OFFSET, F_OFFSET, R_OFFSET);
        char[] bottom = getSideValues(D_OFFSET);
        char[] back = getSideValues(B_OFFSET);
        printSide(top, true, false);
        printSide(middle);
        printSide(bottom, false, true);
        printSide(back, false, true);
    }

    private char[] getSideValues(int... offsets) {
        StringBuilder ret = new StringBuilder();
        for (int offset : offsets)
            for (int i = 0; i < 4; i++)
                ret.append(state[offset + i]);
        return ret.toString().toCharArray();
    }

    private void printSide(char[] data) {
        printSide(data, true, true);
    }

    private void printSide(char[] data, boolean top, boolean bottom) {
        if (data.length < 12) {
            System.out.println((top ? "\t#####\n" : "") +
                    "\t#" + data[0] + " " + data[1] + "#\n" +
                    "\t#   #\n" +
                    "\t#" + data[2] + " " + data[3] + "#" + (bottom ? "\n" : "") +
                    (bottom ? "\t#####\n" : ""));
        } else {
            System.out.println((top ? "#############\n" : "") +
                    "#" + data[0] + " " + data[1] + "#" + data[4] + " " + data[5] + "#" + data[8] + " " + data[9] + "#\n" +
                    "#   #   #   #\n" +
                    "#" + data[2] + " " + data[3] + "#" + data[6] + " " + data[7] + "#" + data[10] + " " + data[11] + "#\n" +
                    (bottom ? "#############" : ""));
        }
    }
}
