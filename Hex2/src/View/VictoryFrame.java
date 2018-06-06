package View;

import Model.HexModel;

import javax.swing.*;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class VictoryFrame{

    private JFrame frame;
    public VictoryPanel panel;
    private static final int Width = 600;
    private static final int Height = 150;

    VictoryFrame(HexModel model){
        panel = new VictoryPanel(model);
        frame = new JFrame("Victoire!");
        frame.setBounds(400,50,Width,Height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().add(panel);

        frame.setVisible(false);
    }

    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }
}
