package com.cmatri;

import com.cmatri.gui.CubeRenderer;
import com.cmatri.gui.SolverUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import static com.cmatri.CubeState.decodeMoves;

public class Main implements KeyListener {

    CubeRenderer renderer;
    CubeState state;

    public Main() {
        renderer = new CubeRenderer();
        state = new CubeState(renderer::updateState);

        JFrame frame = new JFrame("SolverUI");
        frame.setPreferredSize(new Dimension(1000, 550));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(true);
        frame.setContentPane(renderer);

        JLabel scrambleLabel = new JLabel("Enter scramble: ");
        JTextField scrambleInput = new JTextField("F' U2 F U F' U2 F' U2 R'", 15);
        JButton scrambleButton = new JButton("Scramble");
        JButton solveButton = new JButton("Solve");

        renderer.add(scrambleLabel);
        renderer.add(scrambleInput);
        renderer.add(scrambleButton);
        renderer.add(solveButton);

        scrambleLabel.setBounds(400, 200, 100, 20);
        scrambleInput.setBounds(495, 200, 200, 20);
        scrambleButton.setBounds(420, 225, 110, 25);
        scrambleButton.addActionListener(e -> {
            String scramble = scrambleInput.getText().toUpperCase();
            if(scramble.contains("L") || scramble.contains("D") || scramble.contains("B")) {
                JOptionPane.showMessageDialog(null, "Not a WCA scramble.", "No", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Scramble: " + scramble);
            state.setSolved();
            state.algorithm(scramble);
        });

        solveButton.setBounds(540, 225, 110, 25);
        solveButton.addActionListener(e -> {
            state.setUpdateUI(false);
            long start = System.currentTimeMillis();
            String solution = CubeSolver.solveCube(state);
            System.out.println("Search took " + (System.currentTimeMillis() - start) + "ms.");
            solution = decodeMoves(solution);
            System.out.println("Solution: " + solution);
            state.setSolved();
            state.algorithm(scrambleInput.getText());
            state.setUpdateUI(true);
            state.algorithm(solution);
        });

        frame.pack();
        frame.setVisible(true);
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
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
