package View;

import Model.HexModel;

import javax.swing.*;
import java.awt.*;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class VictoryPanel extends JPanel {

    private HexModel model;
    public JButton bReturn = new JButton("Retour");
    public JLabel title = new JLabel("Un joueur a  gagne !");
    VictoryPanel(HexModel model){
        this.model = model;

        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Box box1 = new Box(BoxLayout.X_AXIS);
        Box box2 = new Box(BoxLayout.X_AXIS);
        //Box 1
        title.setForeground(Color.WHITE);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 50));
        box1.add(title);
        //Box 2
        bReturn.setSize(150,50);
        box2.add(bReturn);

        add(box1);
        add(box2);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (model.getWinner() == Color.RED){
            this.setBackground(Color.RED);
            title.setText("Le joueur Rouge a  gagné !");
        }
        else{
            this.setBackground(Color.BLUE);
            title.setText("Le joueur Bleu a  gagné !");
        }
    }
}
