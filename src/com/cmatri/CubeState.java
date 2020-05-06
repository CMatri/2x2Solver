package com.cmatri;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CubeState {
    private char[] state;
    private Consumer<char[]> uiUpdateHandle;

    public static class SideValues {
        public char[] l, r, u, d, f, b;

        private char[] merge(char[]... vals) {
            StringBuilder ret = new StringBuilder();
            ret.append(u);
            ret.append(l);
            ret.append(f);
            ret.append(r);
            ret.append(d);
            ret.append(b);
            return ret.toString().toCharArray();
        }

        public char[] inOrder() {
            return merge(u, l, f, r, d, b);
        }

        public SideValues(char[] l, char[] r, char[] u, char[] d, char[] f, char[] b) {
            this.l = l;
            this.r = r;
            this.u = u;
            this.d = d;
            this.f = f;
            this.b = b;
        }
    }

    public static final char W = 'w';
    public static final char Y = 'y';
    public static final char R = 'r';
    public static final char G = 'g';
    public static final char B = 'b';
    public static final char O = 'o';

/*
    #####
    #0 1#
    # W #
    #3 2#
#############
#4 5#8 9#12 #
# O # G # R #
#7 6# 10# 14#
#############
    #16 #
    # Y #
    # 18#
    #####
    # 23#
    # B #
    # 20#
    #####

 */

    public static final Integer[] Rp_TRANS = {0, 9, 10, 3, 4, 5, 6, 7, 8, 17, 18, 11, 15, 12, 13, 14, 16, 23, 20, 19, 2, 21, 22, 1}; // new
    public static final Integer[] R_TRANS = invertPerm(Rp_TRANS); // new
    public static final Integer[] L_TRANS = {8, 1, 2, 11, 5, 6, 7, 4, 16, 9, 10, 19, 12, 13, 14, 15, 22, 17, 18, 21, 20, 3, 0, 23}; // new
    public static final Integer[] Lp_TRANS = invertPerm(L_TRANS); // new
    public static final Integer[] Fp_TRANS = {0, 1, 5, 6, 4, 16, 17, 7, 11, 8, 9, 10, 3, 13, 14, 2, 15, 12, 18, 19, 20, 21, 22, 23}; // new
    public static final Integer[] F_TRANS = invertPerm(Fp_TRANS); // new
    public static final Integer[] B_TRANS = {13, 14, 2, 3, 1, 5, 6, 0, 8, 9, 10, 11, 12, 18, 19, 15, 16, 17, 7, 4, 21, 22, 23, 20}; // new
    public static final Integer[] Bp_TRANS = invertPerm(B_TRANS); // new
    public static final Integer[] Up_TRANS = {3, 0, 1, 2, 8, 9, 6, 7, 12, 13, 10, 11, 20, 21, 14, 15, 16, 17, 18, 19, 4, 5, 22, 23}; // new
    public static final Integer[] U_TRANS = invertPerm(Up_TRANS); // new
    public static final Integer[] D_TRANS = {0, 1, 2, 3, 4, 5, 22, 23, 8, 9, 6, 7, 12, 13, 10, 11, 19, 16, 17, 18, 20, 21, 15, 14}; // new
    public static final Integer[] Dp_TRANS = invertPerm(D_TRANS); // new

    private Map<Character, Integer[]> permMap = new HashMap<Character, Integer[]>() {{
        put('U', U_TRANS);
        put('D', D_TRANS);
        put('F', F_TRANS);
        put('B', B_TRANS);
        put('R', R_TRANS);
        put('L', L_TRANS);
        put('u', Up_TRANS);
        put('d', Dp_TRANS);
        put('f', Fp_TRANS);
        put('b', Bp_TRANS);
        put('r', Rp_TRANS);
        put('l', Lp_TRANS);
    }};

    public static final char[] solvedState = {W, W, W, W, O, O, O, O, G, G, G, G, R, R, R, R, Y, Y, Y, Y, B, B, B, B};
    public static final int L_OFFSET = 4;
    public static final int R_OFFSET = 12;
    public static final int U_OFFSET = 0;
    public static final int D_OFFSET = 16;
    public static final int F_OFFSET = 8;
    public static final int B_OFFSET = 20;

    public CubeState(Consumer<char[]> uiUpdateHandle) {
        this.state = new char[24];
        this.uiUpdateHandle = uiUpdateHandle;
        setSolved();
    }

    public char[] getState() {
        return this.state;
    }

    public void setState(char[] state) {
        this.state = state;
    }

    public static Integer[] invertPerm(Integer[] perm) {
        int n = perm.length;
        Integer[] g = new Integer[n];
        for (int i = 0; i < n; i++) {
            g[perm[i]] = i;
        }
        return g;
    }

    public void move(char face) {
        if (!permMap.containsKey(face)) return;
        Integer[] perm = permMap.get(face);
        char[] buf = state.clone();
        for (int i = 0; i < state.length; i++) {
            buf[perm[i]] = state[i];
        }
        changeState(buf);
        validateState();
    }

    public void moveInverse(char face) {
        if (!permMap.containsKey(face)) return;
        Integer[] perm = permMap.get(face);
        char[] buf = state.clone();
        for (int i = 0; i < state.length; i++) {
            buf[i] = state[perm[i]];
        }
        changeState(buf);
        validateState();
    }

    public void setSolved() {
        changeState(solvedState.clone());
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

    private void changeState(char[] newState) {
        this.state = newState;
        //uiUpdateHandle.accept(state);
    }

    private void validateState() {
        int r = 0, o = 0, w = 0, y = 0, g = 0, b = 0;
        for (int i = 0; i < state.length; i++) {
            switch (state[i]) {
                case 'w':
                    w++;
                    break;
                case 'y':
                    y++;
                    break;
                case 'g':
                    g++;
                    break;
                case 'b':
                    b++;
                    break;
                case 'r':
                    r++;
                    break;
                case 'o':
                    o++;
                    break;
            }
        }
        if (!(r == 4 && o == 4 && y == 4 && w == 4 && g == 4 && b == 4)) {
            System.err.println("Invalid state.");
            System.exit(0);
        }
        ;
    }

    public SideValues getSideValuesObj() {
        return new SideValues(getSideValues(L_OFFSET), getSideValues(R_OFFSET), getSideValues(U_OFFSET), getSideValues(D_OFFSET), getSideValues(F_OFFSET), getSideValues(B_OFFSET));
    }

    public static SideValues getSideValuesObj(char[] toSearch) {
        return new SideValues(getSideValues(L_OFFSET, toSearch), getSideValues(R_OFFSET, toSearch), getSideValues(U_OFFSET, toSearch), getSideValues(D_OFFSET, toSearch), getSideValues(F_OFFSET, toSearch), getSideValues(B_OFFSET, toSearch));
    }

    public static char[] getSideValues(int offset, char[] toSearch) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < 4; i++)
            ret.append(toSearch[offset + i]);
        return ret.toString().toCharArray();
    }

    private char[] getSideValues(int... offsets) {
        StringBuilder ret = new StringBuilder();
        for (int offset : offsets)
            ret.append(getSideValues(offset, state));
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

    private void algorithm(char[] alg) {
        for (char c : alg) move(c);
    }

    public void algorithm(String alg) {
        String[] split = alg.split(" ");
        ArrayList<Character> ops = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            switch (s) {
                case "R":
                    ops.add('R');
                    break;
                case "R'":
                    ops.add('r');
                    break;
                case "R2":
                    ops.add('R');
                    ops.add('R');
                    break;
                case "L":
                    ops.add('L');
                    break;
                case "L'":
                    ops.add('l');
                    break;
                case "L2":
                    ops.add('L');
                    ops.add('L');
                    break;
                case "F":
                    ops.add('F');
                    break;
                case "F'":
                    ops.add('f');
                    break;
                case "F2":
                    ops.add('F');
                    ops.add('F');
                    break;
                case "B":
                    ops.add('B');
                    break;
                case "B'":
                    ops.add('b');
                    break;
                case "B2":
                    ops.add('B');
                    ops.add('B');
                    break;
                case "U":
                    ops.add('U');
                    break;
                case "U'":
                    ops.add('u');
                    break;
                case "U2":
                    ops.add('U');
                    ops.add('U');
                    break;
                case "D":
                    ops.add('D');
                    break;
                case "D'":
                    ops.add('d');
                    break;
                case "D2":
                    ops.add('D');
                    ops.add('D');
                    break;
            }
        }
        char[] ret = new char[ops.size()];
        for (int i = 0; i < ret.length; i++) ret[i] = ops.get(i);
        algorithm(ret);
    }
}
