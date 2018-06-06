import java.awt.Color;

import Controller.HexController;
import Model.HexModel;
import View.HexView;
import Model.Cell;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class Main {

	public Main(){
		// Poue les valeur de playerblue et playerred :0 = Joueur humain au clic de souris et 1 = joueur aléatoire
		// Le joueur bleu commence
		int playerblue = 1;
		int playerred = 0;
		int size = 9;
		//Creation du model
		HexModel model = new HexModel(size);
		//Creation de la vue
		HexView view = new HexView(model,"Jeu de Hex");
		//Creation du controller
		HexController controller = new HexController(model,view,playerblue,playerred);
		while(!model.getFinished()){
			if(!model.getVictory()){
				System.out.print("ouais");
				if(playerblue==1 && model.getPlayer() == Color.BLUE && model.getCurrentGame()){
					Cell c = controller.randomPlay();
					System.out.println("Bleu alea : Cell("+c.getX()+" "+c.getY()+")");
					c.setColor(Color.BLUE);
					controller.nbBlueMove++;
					model.grid.blueCells.add(c);
					model.lastBlueCell = c;
					model.setPlayer(Color.RED);
					model.researchVictory(0,1);
				}

				else if(playerred==1 && model.getPlayer() == Color.RED && model.getCurrentGame()){

					Cell c = controller.randomPlay();
					System.out.println("Rouge alea : Cell("+c.getX()+" "+c.getY()+")");
					c.setColor(Color.RED);
					controller.nbRedMove++;
					model.grid.redCells.add(c);
					model.lastRedCell = c;
					model.setPlayer(Color.BLUE);
					model.researchVictory(1,0);
				}
			}
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
