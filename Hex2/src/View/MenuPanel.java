package View;

import Model.HexModel;

import javax.swing.*;
import java.awt.*;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class MenuPanel extends JPanel{

    private static final int bWidth = 150;
    private static final int bHeight = 75;

    private HexModel model;

    JLabel title = new JLabel("HexGame");
    public JButton bPlay = new JButton("Jouer");
    public JButton bQuit = new JButton("Quitter");

    public JButton bReset = new JButton("Rejouer");

    MenuPanel(HexModel model){
        super();
        this.model = model;
        //Fond
        this.setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Box box1 = new Box(BoxLayout.X_AXIS);
        Box box2 = new Box(BoxLayout.X_AXIS);
        Box box3 = new Box(BoxLayout.X_AXIS);
        Box box4 = new Box(BoxLayout.X_AXIS);
 //       Box box5 = new Box(BoxLayout.X_AXIS);
        //Titre
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 100));
        box1.add(title, BorderLayout.NORTH);
        box2.add(new JSeparator());

        JPanel bPanel = new JPanel();
        bPanel.setBackground(Color.WHITE);
        //Button

        bPlay.setSize(bWidth,bHeight);
        bReset.setSize(bWidth,bHeight);
        bQuit.setSize(bWidth,bHeight);

        bPanel.add(bPlay, BorderLayout.NORTH);
        bPanel.add(bReset, BorderLayout.CENTER);
        bPanel.add(bQuit, BorderLayout.SOUTH);

        box3.add(bPanel);
        box4.add(new JSeparator());
    //    box5.add(new JLabel("2015 - 2016"));

        add(box1);
        add(box2);
        add(box3);
        add(box4);
   //     add(box5);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!this.model.getCurrentGame()){
            bPlay.setText("Jouer");
            bReset.setVisible(false);
        }
        else{
            bPlay.setText("Reprendre");
            bReset.setVisible(true);
        }
    }
}
