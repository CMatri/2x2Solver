package com.cmatri.gui;

import com.cmatri.CubeState;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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

public class CubeRenderer extends JPanel {
    private int cubieSize;
    private char[] state;

    private Map<Character, Color> colorMap = new HashMap<Character, Color>() {{
        put('w', Color.white);
        put('y', Color.yellow);
        put('r', Color.red);
        put('b', Color.blue);
        put('g', Color.green);
        put('o', Color.orange);
    }};

    public CubeRenderer() {
        setPreferredSize(new Dimension(500, 550));
        cubieSize = 50;
    }

    public void updateState(char[] state) {
        this.state = state;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state == null) return;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                g.setColor(colorMap.get(state[i * 2 + j]));
                g.fillRect(j * cubieSize + cubieSize * 3, cubieSize * i + cubieSize, cubieSize, cubieSize);
                g.setColor(Color.black);
                g.drawRect(j * cubieSize + cubieSize * 3, cubieSize * i + cubieSize, cubieSize, cubieSize);
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                g.setColor(colorMap.get(state[i * 2 + j + 16]));
                g.fillRect(j * cubieSize + cubieSize + cubieSize * (i < 2 ? 0 : 4), cubieSize * 3 + (i % 2 == 0 ? cubieSize : 0), cubieSize, cubieSize);
                g.setColor(Color.black);
                g.drawRect(j * cubieSize + cubieSize + cubieSize * (i < 2 ? 0 : 4), cubieSize * 3 + (i % 2 == 0 ? cubieSize : 0), cubieSize, cubieSize);
            }
        }
    }
}
