package com.cmatri;

import com.cmatri.gui.CubeRenderer;
import com.cmatri.gui.SolverUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {

    CubeRenderer renderer;
    CubeState state;

    public Main() {
        renderer = new CubeRenderer();
        state = new CubeState(renderer::updateState);

        JFrame frame = new JFrame("SolverUI");
        frame.setPreferredSize(new Dimension(1000, 550));
        frame.setContentPane(new SolverUI().background);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(true);
        frame.setContentPane(renderer);
        frame.pack();
        frame.setVisible(true);

        state.setSolved();
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        state.move(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //state.move('r');
        //state.move('u');
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
