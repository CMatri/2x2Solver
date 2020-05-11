package com.cmatri.gui;

import com.cmatri.CubeState;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;

import static com.cmatri.CubeState.*;

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
    #20 #
    # B #
    # 22#
    #####

 */

public class CubeRenderer extends JPanel {
    private int faceSize;
    private char[] state;
    private Side[] cubies;
    private Deque<char[]> animQueue;
    private int updateDelay = 30;

    private Map<Character, Color> colorMap = new HashMap<Character, Color>() {{
        put('w', Color.white);
        put('y', Color.yellow);
        put('r', Color.red);
        put('b', Color.blue);
        put('g', Color.green);
        put('o', Color.orange);
    }};

    public CubeRenderer() {
        super(null);
        setPreferredSize(new Dimension(500, 550));
        faceSize = 80;
        animQueue = new ArrayDeque<>();
        cubies = new Side[]{
                new Side('w', faceSize * 2, faceSize),//u
                new Side('o', faceSize, faceSize * 2),//l
                new Side('g', faceSize * 2, 2 * faceSize),//f
                new Side('r', faceSize * 3, faceSize * 2),//r
                new Side('y', faceSize * 2, 3 * faceSize),//d
                new Side('b', faceSize * 2, 4 * faceSize),//b
        };

        Timer updateTimer = new Timer(updateDelay, e -> {
            if (!animQueue.isEmpty()) {
                this.state = animQueue.poll();
                repaint();
            }
        });
        updateTimer.setRepeats(true);
        updateTimer.start();
    }

    public void updateState(char[] state) {
        animQueue.add(state);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state == null) return;

        char[] vals = getSideValuesObj(state).inOrder();
        cubies[0].update(Arrays.copyOfRange(vals, CubeState.U_OFFSET, CubeState.U_OFFSET + 4));
        cubies[1].update(Arrays.copyOfRange(vals, CubeState.L_OFFSET, CubeState.L_OFFSET + 4));
        cubies[2].update(Arrays.copyOfRange(vals, CubeState.F_OFFSET, CubeState.F_OFFSET + 4));
        cubies[3].update(Arrays.copyOfRange(vals, CubeState.R_OFFSET, CubeState.R_OFFSET + 4));
        cubies[4].update(Arrays.copyOfRange(vals, CubeState.D_OFFSET, CubeState.D_OFFSET + 4));
        char[] bVals = Arrays.copyOfRange(vals, CubeState.B_OFFSET, CubeState.B_OFFSET + 4);
        char[] buf = bVals.clone();
        bVals[0] = buf[2];
        bVals[1] = buf[3];
        bVals[2] = buf[0];
        bVals[3] = buf[1];
        cubies[5].update(bVals);
        for (int i = 0; i < cubies.length; i++) cubies[i].draw(g, faceSize / 2);
    }

    private class Side {
        Color[] vals;
        int x, y;

        public Side(char[] vals, int x, int y) {
            this.x = x;
            this.y = y;
            update(vals);
        }

        public Side(char c, int x, int y) {
            this(new char[]{c, c, c, c}, x, y);
        }

        public Side() {
            this("wwww".toCharArray(), 0, 0);
        }

        public void update(char[] vals) {
            this.vals = new Color[]{
                    colorMap.get(vals[0]), colorMap.get(vals[1]), colorMap.get(vals[2]), colorMap.get(vals[3])
            };
        }

        public void draw(Graphics g, int cubieSize) {
            for (int i = 0; i < 4; i++) {
                int drawX = x + (i < 2 ? i : 4 - i - 1) * cubieSize;
                int drawY = y + (i < 2 ? 0 : 1) * cubieSize;
                Color oldColor = g.getColor();
                g.setColor(vals[i]);
                g.fillRect(drawX, drawY, cubieSize, cubieSize);
                g.setColor(Color.black);
                g.drawRect(drawX, drawY, cubieSize, cubieSize);
                g.setColor(oldColor);
            }
        }
    }
}
