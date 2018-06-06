package Controller;

import Model.Cell;
import Model.Grid;
import Model.HexModel;
import View.HexView;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class HexController implements ActionListener,MouseListener{

	private HexModel model;
	private HexView view;
	private int playerblue = 0;
	private int playerred = 0;
	public int nbBlueMove = 0;
	public int nbRedMove = 0;
	
	

	/** ************
	 *
	 *  Constructeur
	 *
	 *  ************
	 */

	public HexController(HexModel model, HexView view,int playerblue,int playerred){
		this.model = model;
		this.view = view;
		this.playerred = playerred;
		this.playerblue = playerblue;
		view.pMenu.bPlay.addActionListener(this);
		view.pMenu.bReset.addActionListener(this);
		view.pMenu.bQuit.addActionListener(this);

		view.pGame.addMouseListener(this);
		view.pGame.bReturn.addActionListener(this);

		view.pVictory.panel.bReturn.addActionListener(this);
	}

	public HexController(HexModel model, HexView view){
		this.model = model;
		this.view = view;

		view.pMenu.bPlay.addActionListener(this);
		view.pMenu.bReset.addActionListener(this);
		view.pMenu.bQuit.addActionListener(this);

		view.pGame.addMouseListener(this);
		view.pGame.bReturn.addActionListener(this);

		view.pVictory.panel.bReturn.addActionListener(this);
	}

	/** ******************
	 *
	 *  Action des boutons
	 *
	 *  Les attributs du model enJeu et enCours permettent de connaitre l'état du jeu.
	 *  L'attribut enJeu permet de différencier le panel du menu et du jeu et l'attribut enCours permet de savoir si une partie est en cours.
	 *  Grâce à l'attribut enCours on peut proposer différente action dans le menu, comme une action de reset si une partie est en cours.
	 *
	 *  ******************
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * Action dans la fenêtre du menu sans que le jeu sois lancé
		 */
		if (!model.getInGame() && !model.getCurrentGame()) {
			// Si on clique sur le bouton play le jeu se lance
			if (e.getSource() == view.pMenu.bPlay) {
				model.setInGame(true);
				model.setCurrentGame(true);
			}
			else if (e.getSource() == view.pMenu.bQuit) {
				// Si on clique sur le bouton quitter on quitte le jeu
				view.dispose();
			}
		}

		/**
		 * Action dans la fen�tre du menu avec le jeu lanc�
		 */
		else if (!model.getInGame() && model.getCurrentGame()){
			// Si on clique sur le bouton play on retourne au jeu et on continue la partie en cours
			if (e.getSource() == view.pMenu.bPlay) {
				model.setInGame(true);
				model.setCurrentGame(true);
			}
			else if (e.getSource() == view.pMenu.bReset){
				// Si on clique sur le bouton reset on relance une nouvelle partie
				model.rebuild();
				model.setInGame(true);
				model.setCurrentGame(true);
			}
			else if (e.getSource() == view.pMenu.bQuit) {
				// Si on clique sur le bouton quitter on quitte le jeu
				model.setFinished(true);
				view.dispose();
			}
		}

		/**
		 * Action dans la fen�tre de jeu
		 */
		else if (model.getInGame() && model.getCurrentGame()) {
			// Si on clique sur le bouton retour on retourne au menu et la partie est toujours en cours, c'est à dire qu'on pourra y revenir
			// en appuyant sur play dans le menu
			if (e.getSource() == view.pGame.bReturn) {
				model.setInGame(false);
				model.setCurrentGame(true);
				view.pVictory.setVisible(false);
			}
			// Si on clique sur le bouton retour de la fenêtre de victoire on retourne au menu, ce qui arrête la partie en cours puisqu'on a eu un gagant
			if(e.getSource() == view.pVictory.panel.bReturn) {
				view.pVictory.setVisible(false);
				model.rebuild();
			}
		}
	}
	public Cell randomPlay(){
		System.out.println("on est bon");
		int nbMove=0;
		if(model.getPlayer() == Color.BLUE){
			nbMove = nbBlueMove;
		}else{
			nbMove = nbRedMove;
		}
		if(nbMove == 0){
			return model.grid.getCell(4, 4);
		}
		
		Cell priorityCell = getPriorityCell();
		if(priorityCell != null)
			return priorityCell;
		
//		boolean winningChain = isWinningChain(model, model.getPlayer());
//		System.out.println(winningChain);
//		if(winningChain){
//			System.out.println("winningchain");
//			completeChain();
//		}
		Cell c = combinedCells();	
         
		return c;
	}
	
//	public Map<Integer,List<Cell>> blueWinningChain(List<Cell> blueCells){
//		List<Cell> blueWinningChain = new ArrayList<Cell>();
//		List<Cell> blueCellsTmp = new ArrayList<Cell>();
//		blueCellsTmp.addAll(blueCells);
//		
//		for (Cell cell : blueCells) {
//			
//		}
//	}
	
	public void completeChain(){
		List<Cell> chain = getMaxChain(model, model.getPlayer());
		for(int i =0; i<chain.size()-1; i++){
			List<Cell> commonCells = getCommonNeighbors(chain.get(i), chain.get(i+1));
			boolean completed = false;
			for (Cell cell : commonCells) {
				if(cell.getColor() == model.getPlayer())
					completed = true;
			}
			if(!completed)
				commonCells.get(0).setColor(model.getPlayer());
		}
	}
	
	public Cell combinedCells(){
		HexModel testModel = model;
		List<Cell> freeCells = strategy(testModel, testModel.getPlayer());
		int[] scores = new int[freeCells.size()];
//		for (Cell c : model.grid.blueCells) {
////			if(c.getColor() == Color.BLUE)
//				System.out.println("bcellinit : "+c.getX()+","+c.getY());
//		}
//		for (Cell c : testModel.grid.blueCells) {
////			if(c.getColor() == Color.BLUE)
//				System.out.println("bcell : "+c.getX()+","+c.getY());
//		}
		
		for (int i = 0; i< freeCells.size(); i++) {
			freeCells.get(i).setColor(testModel.getPlayer());
			if(testModel.getPlayer() == Color.BLUE)
				testModel.grid.blueCells.add(freeCells.get(i));
			else
				testModel.grid.redCells.add(freeCells.get(i));
			scores[i] = tree(testModel, testModel.getPlayer(), 3, 1, freeCells.get(i));
			freeCells.get(i).setColor(Color.WHITE);
			if(testModel.getPlayer() == Color.BLUE)
				testModel.grid.blueCells.remove(freeCells.get(i));
			else
				testModel.grid.redCells.remove(freeCells.get(i));
		}
		

		int maxScore = -10000000;
		int maxIndice = 0;
		
		for(int i =0; i<scores.length; i++){
			if(maxScore < scores[i]){
				
				maxScore = scores[i];
				maxIndice = i;
			}
		}
		Cell res = freeCells.get(maxIndice);
		return model.grid.getCell(res.getX(), res.getY());
		
	}
	
	public int tree(HexModel model, Color color, int high, int turn, Cell originCell){
		boolean victory = false;
		victory = isWinningChain(model, color);
//		victory = model.getVictory();
		
		if(victory){
			model.setVictory(false);
			return turn * 10000 * high;
		}else{
			if(high > 0){
				if(color.equals(Color.BLUE)){
					color=Color.RED;
				}else{
					color=Color.BLUE;
				}
				
				int score = 0;
				for (Cell cell : strategy(model,color)) {
					cell.setColor(color);
					if(color == Color.BLUE)
						model.grid.blueCells.add(cell);
					else
						model.grid.redCells.add(cell);
					
					score += tree(model, color, high-1, -turn, originCell);
					cell.setColor(Color.WHITE);
					if(color == Color.BLUE)
						model.grid.blueCells.remove(cell);
					else
						model.grid.redCells.remove(cell);
				}
				return score;
			}else{
				int score = turn * advantageEval(model, color, originCell);
				if(color == Color.blue){
					
				}else{
					
				}
				return score;
			}
			
		}
	}
	
	public List<Cell> strategy(HexModel model, Color color){
		List<Cell>  cells = new ArrayList<Cell>();
		List<Cell> myCells;
		Cell adverseCell;
		int nbMove = 0;
		if(color.equals(Color.BLUE)){
			myCells = model.grid.blueCells;
//			System.out.println("blueCells : "+myCells);
			adverseCell = model.lastRedCell;
			nbMove = nbBlueMove;
		}else{
			myCells = model.grid.redCells;
			adverseCell = model.lastBlueCell;
			nbMove = nbRedMove;
		}
		
//		cells.addAll(model.researchFreeNeighborCells(myCell));
		for (Cell cell : myCells) {
			if(nbMove < 4){
				int xy = 0;
				if(color == Color.blue)
					xy = cell.getX();
				else
					xy = cell.getY();
				if(!(xy == 0 || xy == 8)){
					List<Cell> n = model.researchFreeSecondNeighboors(cell);
					for (Cell cell2 : n) {
						if(!cells.contains(cell2) && !isDeadCell(cell2))
							cells.add(cell2);
					}
				}
			}else{
				List<Cell> n = model.researchFreeSecondNeighboors(cell);
				for (Cell cell2 : n) {
					if(!cells.contains(cell2) && !isDeadCell(cell2))
						cells.add(cell2);
				}
			}
			
		}
		for (Cell cell : model.researchFreeSecondNeighboors(adverseCell)) {
			if(!cells.contains(cell) && !isDeadCell(cell))
				cells.add(cell);
		}
//		System.out.println("size : "+cells.size());
		return cells;
	}
	
	
	
	public int getMaxChainSize(HexModel model, Color color){
		int max = 0;
		List<List<Cell>> chains = model.researchChainVictory(color);
		for (List<Cell> list : chains) {
			max = Math.max(max, list.size());
		}
		return max;
	}
	
	public List<Cell> getMaxChain(HexModel model, Color color){
		int max = 0;
		List<List<Cell>> chains = model.researchChainVictory(color);
		List<Cell> choosenList = null;
		for (List<Cell> list : chains) {
			if(max<list.size()){
				max = list.size();
				choosenList = list;
			}
		}
		return choosenList;
	}
	
	public boolean isConnectedChain(List<Cell> chain, Color color){
		int min =10;
		int max = -1;
		for (Cell cell : chain) {
			if(color == Color.blue){
				min = Math.min(min, cell.getX());
				max = Math.max(max, cell.getX());
			}else{
				min = Math.min(min, cell.getY());
				max = Math.max(max, cell.getY());
			}
			
			if((min == 0 || min == 1) || (max == 7 || max == 8)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isWinningChain(HexModel model, Color color){
		List<Cell> chains = getMaxChain(model, color);
		int min =10;
		int max = -1;
		for (Cell cell : chains) {
			System.out.println("Cell ("+cell.getX()+","+cell.getY()+")");
			if(color == Color.blue){
				min = Math.min(min, cell.getX());
				max = Math.max(max, cell.getX());
			}else{
				min = Math.min(min, cell.getY());
				max = Math.max(max, cell.getY());
			}
			if((min == 0 || min == 1) && (max == 7 || max == 8)){
				return true;
			}
		}
		System.out.println("ok");
		return false;
	}
	
	public int advantageEval(HexModel model, Color color, Cell originCell){
		List<Cell> redCells = getMaxChain(model, Color.RED);
		List<Cell> blueCells = getMaxChain(model, Color.BLUE);
		int redScore = 10*redCells.size();
		int blueScore = 10*blueCells.size();
		if(isConnectedChain(redCells, Color.RED))
			redScore += 1;
		if(isConnectedChain(blueCells, Color.BLUE))
			blueScore += 1;
		
		int score;
		
		if(color.equals(Color.BLUE)){
			score = blueScore - redScore;
		}else{
			score = redScore - blueScore;
		}
		
		return score;
	}
	
	
	
	public int countConnected(HexModel model, Color color){
		List<Cell> allCells;
		if(color.equals(Color.BLUE)){
			allCells = model.grid.blueCells;
		}else{
			allCells = model.grid.redCells;
		}
		
		List<Cell> chain = new ArrayList<Cell>();
		for (Cell cell : allCells) {
			List<Cell> neighbors = model.researchNeighborCells(cell);
			if(neighbors != null && !neighbors.isEmpty())
				chain.add(cell);
		}
		
		return chain.size();
	}
	
	public int secondCountConnected(HexModel model, Color color){
		List<Cell> allCells;
		if(color.equals(Color.BLUE)){
			allCells = model.grid.blueCells;
		}else{
			allCells = model.grid.redCells;
		}
		
		List<Cell> chain = new ArrayList<Cell>();
		for (Cell cell : allCells) {
			List<Cell> neighbors = model.researchSecondNeighboors(cell, color);
			if(neighbors != null && !neighbors.isEmpty())
				chain.add(cell);
		}
		
		return chain.size();
	}
	
	//PRIORITY CELLS
	public Cell getPriorityCell(){
		List<Cell> chain = getMaxChain(model, model.getPlayer());
		
		HexModel testModel = model.clone();
		
		for (Cell cell : model.grid.getFreeCells()) {
			if(!isDeadCell(cell)){
				if(model.getPlayer() == Color.blue){
					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.BLUE);
					testModel.researchVictory(0, 1);
					if(testModel.getVictory())
						return cell;
					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.RED);
					testModel.researchVictory(1, 0);
					if(testModel.getVictory())
						return cell;
				}else{
					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.RED);
					testModel.researchVictory(1, 0);
					if(testModel.getVictory())
						return cell;
					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.BLUE);
					testModel.researchVictory(0, 1);
					if(testModel.getVictory())
						return cell;
				}
				testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.WHITE);
			}
		}
		
		for (int i = 0 ; i<chain.size()-1; i++) {
			
			if(!isFreeBetweenCells(chain.get(i), chain.get(i+1))){
				List<Cell> cells = getCommonNeighbors(chain.get(i), chain.get(i+1));
				for (Cell cell : cells) {
					if(cell.getColor() == Color.white)
						return cell;
				}
			}
		}
		
//		for (Cell cell : model.grid.getFreeCells()) {
//			if(!isDeadCell(cell)){
//				if(model.getPlayer() == Color.blue){
//					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.BLUE);
//					if(isWinningChain(testModel, Color.BLUE))
//						return cell;
//					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.RED);
//					if(isWinningChain(testModel, Color.RED))
//						return cell;
//				}else{
//					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.RED);
//					if(isWinningChain(testModel, Color.RED))
//						return cell;
//					testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.BLUE);
//					if(isWinningChain(testModel, Color.BLUE))
//						return cell;
//				}
//				testModel.grid.getCell(cell.getX(), cell.getY()).setColor(Color.WHITE);
//			}
//		}
		return null;
	}
	
	public boolean isFreeBetweenCells(Cell c1, Cell c2){
    	List<Cell> commonCells = getCommonNeighbors(c1, c2);
    	
    	for (Cell cell : commonCells) {
			if(!(cell.getColor() == Color.white))
				return false;
		}
    	return true;
    }
	
	//TREE
	
	
//	public int combinedTree(HexModel model, List<Cell> freeCells, int turn, Color player){
//		boolean victory = false;
//		if(player.equals(Color.BLUE)){
//			model.researchVictory(0,1);
//			
//		}else{
//			model.researchVictory(1,0);
//		}
//		
//		victory = model.getVictory();
//		if(victory){
//			model.setVictory(false);
//			return turn * factorial(model.grid.getFreeCells().size());
//		}else if(freeCells.size() == 0){
//			return 0;
//		}else {
//			int score = 0;
//			
//			if(player.equals(Color.BLUE)){
//				player = Color.RED;				
//			}else{
//				player = Color.BLUE;
//			}
//			
//			List<Cell> freeCellsTmp = new ArrayList<Cell>();
//			
//			freeCellsTmp.addAll(freeCells);
//			
//			for (Cell cell : freeCells) {
//				System.out.println("Tree cell : "+cell.getX()+","+cell.getY());
//				cell.setColor(player);
//				freeCells.remove(cell);
//				score += combinedTree(model, freeCells, -turn, player);
//				cell.setColor(Color.WHITE);
//				freeCells.add(cell);
//			}
//			return score;
//		}
//		
//	}
	//END OF TREE
	
	public static int factorial(int n) {
	    if (n>1)
	        return n*factorial(n-1);
	    else
	        return 1;
	}
	
	public Color getOppositeColor(){
		if(model.getPlayer() == Color.RED)
			return Color.BLUE;
		return Color.RED;
	}
	
	public boolean isDeadCell(Cell c){
		if(isDeadCell1(c) || isDeadCell2(c) || isDeadCell3(c)){
			return true;
		}
		return false;
	}
	
	//DEAD CELLS ADDED BY MYRIAM
	public boolean isDeadCell1(Cell c){
		List<Cell> cells = getNeighbors(c);
		cells.addAll(cells);
		
		for (int i=0; i<6; i++) {
			Cell cell = cells.get(i);
			if(!cell.getColor().equals(Color.WHITE)){
				if(cells.get(i+1).getColor().equals(cell.getColor())){
					if(cells.get(i+2).getColor().equals(cell.getColor())){
						if(cells.get(i+3).getColor().equals(cell.getColor())){
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isDeadCell2(Cell c){
		List<Cell> cells = getNeighbors(c);
		cells.addAll(cells);
		
		for (int i=0; i<6; i++) {
			Cell cell = cells.get(i);
			if(!cell.getColor().equals(Color.WHITE)){
				if(cells.get(i+1).getColor().equals(cell.getColor())){
					if(cells.get(i+2).getColor().equals(cell.getColor())){
						if(isOppositeColor(cell, cells.get(i+4))){
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isDeadCell3(Cell c){
		List<Cell> cells = getNeighbors(c);
		cells.addAll(cells);
		
		for (int i=0; i<6; i++) {
			Cell cell = cells.get(i);
			if(!cell.getColor().equals(Color.WHITE)){
				if(cells.get(i+1).getColor().equals(cell.getColor())){
					if(isOppositeColor(cell, cells.get(i+3))){
						if(isOppositeColor(cell, cells.get(i+4))){
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	//END OF DEAD CELLS
	
	public boolean isOppositeColor(Cell c1, Cell c2){
		if(c1.getColor().equals(Color.BLUE) && c2.getColor().equals(Color.RED)){
			return true;
		}
		else if(c2.getColor().equals(Color.BLUE) && c1.getColor().equals(Color.RED)){
			return true;
		}
			
		return false;
	}
	
	public List<Cell> getCommonNeighbors(Cell c1, Cell c2){
		List<Cell> c1Neig = getNeighbors(c1);
    	List<Cell> c2Neig = getNeighbors(c2);
    	List<Cell> commonCells = new ArrayList<Cell>();
    	
    	for (Cell cell : c1Neig) {
			if(c2Neig.contains(cell))
				commonCells.add(cell);
		}
    	return commonCells;
	}
	
	public List<Cell> getNeighbors(Cell c){
		ArrayList<Cell> neighborCells = new ArrayList<>(); // Arraylist qui va stocker les cellules voisines si elles existes
        int i = c.getX();
        int j = c.getY();
        Color color = c.getColor();

        // On créer 6 celules qui correspondent aux 6 cellules voisines d'une cellule
        Cell c1 = model.grid.getCell(i-1,j);
        Cell c2 = model.grid.getCell(i-1,j+1);
        Cell c3 = model.grid.getCell(i,j+1);
        Cell c4 = model.grid.getCell(i+1,j);
        Cell c5 = model.grid.getCell(i+1,j-1);
        Cell c6 = model.grid.getCell(i,j-1);
        
        neighborCells.add(c1);
        neighborCells.add(c2);
        neighborCells.add(c3);
        neighborCells.add(c4);
        neighborCells.add(c5);
        neighborCells.add(c6);
        
        return neighborCells;
	}
	
	public boolean areNeighbors(Cell c1, Cell c2){
		List<Cell> cells = model.researchNeighborCells(c1);
		
		for (Cell cell : cells) {
			if(cell.getX() == c2.getX() && cell.getY() == c2.getY())
				return true;
		}
		return false;
	}
	
	public boolean isOpponent(Cell c){
		return !c.getColor().equals(model.getPlayer());
	}
	/** **************************
	 *
	 *  Action de la souris en jeu
	 *
	 *  **************************
	 */

	@Override
	public void mouseClicked(MouseEvent e) {
		if (model.getInGame())
		{
			// On recupere les coordonnees du clique de la souris
			float x = e.getX();
			float y = e.getY();
			/**
			 * Pour toutes les cellules de la grid si les coordonnées de la souris sont comprises dans une cellule de la grid alors on change la couleur
			 * de cette cellule avec la couleur du joueur en courant.
			 * On appel aussi la méthode victoire du model après chaque clique du joueur. On test la victoire par rapport a la couleur du joueur.
			 */
			for (Cell c : model.grid) {
				if (c.contains(x,y) && c.getColor() == Color.WHITE){
					if(model.getPlayer() == Color.BLUE){
						
						if (playerblue ==0){
							System.out.println("Bleu humain : Cell("+c.getX()+" "+c.getY()+")");
							c.setColor(Color.BLUE);
							nbBlueMove++;
							model.grid.blueCells.add(c);
							model.lastBlueCell = c;
							// On test la victoire pour le joueur bleu, c'est à dire en partant de la cellule bleu en 0,1
						
							model.setPlayer(Color.RED);
							model.researchVictory(0,1);   
						}
					}
					else{ 
						
						if(playerred == 0){
							System.out.println("Rouge humain : Cell("+c.getX()+" "+c.getY()+")");
							c.setColor(Color.RED);
							nbRedMove++;
							model.grid.redCells.add(c);
							model.lastRedCell = c;
							if(isWinningChain(model, Color.red))
							model.setPlayer(Color.BLUE);
							// On test la victoire pour le joueur rouge, c'est à dire en partant de la cellule rouge en 1,0
							model.researchVictory(1,0);
						}
					}
				}

			}
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
