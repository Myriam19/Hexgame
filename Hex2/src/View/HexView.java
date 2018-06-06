package View;

import Model.HexModel;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class HexView extends JFrame implements Observer {

    HexModel model;

    public MenuPanel pMenu;
    public GamePanel pGame;
    public VictoryFrame pVictory;

    public HexView(HexModel model, String title){
        super(title);
        this.model=model;
        this.pMenu = new MenuPanel(model);
        this.pGame = new GamePanel(model);
        this.pVictory = new VictoryFrame(model);
        model.addObserver(this);

        setBounds(200,100,900,650);
     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().removeAll();
        getContentPane().add(pMenu);

        setVisible(true);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (model.getInGame()){
            getContentPane().removeAll();
            getContentPane().add(pGame);
        }
        else{
            getContentPane().removeAll();
            getContentPane().add(pMenu);
        }

       if(model.getVictory()){
           this.pVictory.setVisible(true);
       }
        else
           this.pVictory.setVisible(false);

        this.revalidate();
        this.repaint();
    }
}
