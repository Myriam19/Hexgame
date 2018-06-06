package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class HexModel extends Observable {

    /** ******************
     *
     *  Les attributs inGame et currentGame permettent de connaitre l'état du jeu.
     *  L'attribut inGame permet de différencier le panel du menu et du jeu et l'attribut currentGame permet de savoir si une partie est en cours.
     *  Grâce à l'attribut currentGame on peut proposer différente action dans le menu, comme une action de reset si une partie est en cours.
     *
     *  ******************
     */
    private boolean inGame; // Si on n'est sur le panel jeu
    private boolean currentGame; // Si une partie est en cours
    private boolean finished;
    private Color player; //  Bleu ou Rouge selon le joueur
    private Color winner; // Blanche pour auncun gagnant, Bleu ou Rouge selon le gagnant
    private boolean victory; // 0 si un player gagne et 1 sinon
    public Cell lastBlueCell;
	public Cell lastRedCell;

    public Grid grid;// = new Grid(9,9); // La grille du jeu

    /** ************
     *
     *  Constructeur
     *
     *  ************
     */
    public HexModel(int size){
        this.inGame = false; // On affecte false pour dire qu'on commence sur le panel du menu
        this.currentGame = false; // On afecte false car aucune partie n'est lancé
        this.finished = false;
        this.player = Color.BLUE; // On affecte false au player pour dire que le player bleu commence
        this.winner = Color.WHITE;
        this.victory = false; // On affecte false à la victory car auncun player n'a gagné
        this.grid = new Grid(size,size);
        
        grid.buildGrid(); // On construit la grille
    }
    
    public HexModel() {
	}

    /** ************
     *
     *  Accesseurs
     *
     *  ************
     */
    public boolean getInGame(){
        return this.inGame;
    }

    public void setInGame(boolean inGame){
        this.inGame = inGame;
        setChanged();
        notifyObservers();
    }
    public void  setFinished(boolean finished){
    	this.finished = finished;
	
}
    public boolean getFinished(){
    	return this.finished;
	
}

    public boolean getCurrentGame(){
        return this.currentGame;
    }

    public void setCurrentGame(boolean currentGame) {
        this.currentGame = currentGame;
        setChanged();
        notifyObservers();
    }

    public Color getPlayer() { return this.player; }

    public void setPlayer(Color player) {
        this.player = player;
        setChanged();
        notifyObservers();
    }

    public Color getWinner() {
        return winner;
    }

    // Lorsqu'un player gagne on affecte true a victory et l'attribut gagnant prend le player qui gagne
    public void setWinner(Color winner) {
        this.winner = winner;
        this.victory = true;
        setChanged();
        notifyObservers();
    }

    // Il n'y a pas d'accesseur d'écriture pour l'attribut victory car la modification de la victoire se fait avec l'accesseur setWinner.
    // Si un player gagne alors on met automatiquement l'attribut victory a true.
    public boolean getVictory(){
        return victory;
    }
    public void setVictory(boolean victory) {
		this.victory = victory;
	}

    /** ************
     *
     *  Méthode appelé au restart, elle reconstruit la grid et affecte les attributs comme dans le constructeur
     *
     *  ************
     */
    public void rebuild() {
        grid.clear();
        grid.buildGrid();
        this.inGame = false;
        this.currentGame = false;
        this.player = Color.BLUE;
        this.victory = false;
        setChanged();
        notifyObservers();
    }

    /** ************
     *
     *  Méthode pour détecter la victory d'un player
     *
     *  La détection de la victory est décomposée en trois méthodes.
     *  La méthode researchVictory est appellé après un clique du player. On va tester la victory pour le player qui a cliqué.
     *  De cette facon on ne fait pas de test inutile en testant à chaque clique la victory pour les deux joueurs, onne test que pour le player qui a cliqué.
     *
     *  La méthode researchVictory fait appel aux méthodes researchNeighborCells et researchWinner.
     *  La méthode researchNeighborCells retourne une arrayList de toutes les cellules voisines de même couleur que la cellule passé en paramètre.
     *  A partir des cellules voisines on fait appel à la méthode rechercheGagnant qui va parcourir l'arrayList des cellules voisines.
     *  Pour chaque cellule présente dans l'arayList on va faire un appel récursif à la méthode rechercheGagnant en les passant en paramètre
     *  ce qui va nous donner au fur et à mesure les cellules voisines de toutes les cellules de la couelur du player.
     *  Si pour une des cellules voisines la coordonnée X (respectivement Y) est supérieur ou égale au nombre de lignes -2 ( respectivement
     *  le nombre de colones -2), le player bleu (respectivement rouge) gagne.
     *
     *  ************
     */
    public void researchVictory(int i, int j){

        boolean victory;
        Cell startingCell; // Cellule de depart de l'algorithme de victoire
        ArrayList<Cell> neighborCells; // ArrayList qui va stocker les celluls voisines

        startingCell = grid.getCell(i,j);
        grid.setPast(startingCell); // On dit qu'on est deja passe par cette cellule pour ne pas y retourner
        neighborCells = researchNeighborCells(startingCell); // On récupère les cellules voisines de la cellule de départ
        victory = researchWinner(neighborCells); // On lance l'algo récursif

        // Si il y a victory on affecte le gagnant au bon player
        if (victory) {
            if (startingCell.getColor() == Color.BLUE)
                setWinner(Color.BLUE);

            else
                setWinner(Color.RED);
        }

        grid.resetPast(); // On remet l'attribut passe de toutes les cellules a false
        setChanged();
        notifyObservers();
    }
    
    public List<Cell> researchOneChainVictory(List<Cell> existingCells, Cell cellResearched){
    	List<Cell> neig;
    	if(cellResearched.getColor() == Color.BLUE){
    		neig = researchSecondNeighboorsDown(cellResearched, cellResearched.getColor());
    	}else{
    		neig = researchSecondNeighboorsRight(cellResearched, cellResearched.getColor());
    	}
    	if(!cellResearched.past2){
	    	existingCells.add(cellResearched);
	    	cellResearched.past2 = true;
	    	int maxI = -1;
	    	Cell choosen = null;
	    	for (Cell cell : neig) {
	    		int xy;
	    		if(cellResearched.getColor() == Color.BLUE){
	    			xy = cell.getX();
	    		}else{
	    			xy = cell.getY();
	    		}
	    		if(maxI < xy){
	    			maxI = xy;
	    			choosen = cell;
	    		}
			}
	    	if(choosen != null){
		    	if(!choosen.past2){
		    		researchOneChainVictory(existingCells, choosen);
		    	}
		    	for (Cell cell : neig) {
		    		if(cell != choosen){
		    			cell.past2 = false;
		    		}
				}
	    	}
    	}
    	return existingCells;
    }
    
    public List<List<Cell>> researchChainVictory(Color color){
    	List<Cell> cells;
    	List<List<Cell>> chains = new ArrayList<List<Cell>>();
    	if(color ==Color.BLUE){
    		cells = grid.blueCells;
    	}else{
    		cells = grid.redCells;
    	}
    	
    	for (Cell cell : cells) {
    		if(!cell.past2){
	    		List<Cell> neig = researchSecondNeighboors(cell, cell.getColor());
	    		List<Cell> chaintmp = new ArrayList<Cell>();
	    		
	    		chaintmp.add(cell);
	    		cell.past2 = true;
	    		for (Cell cell2 : neig) {
	    			chaintmp = researchOneChainVictory(chaintmp,cell2);
				}
	    		chains.add(chaintmp);
    		}
    	}
    	
    	for (Cell cell : cells) {
    		cell.past2 = false;
    	}
    	
    	return chains;
    	

//        boolean victory;
//        Cell startingCell; // Cellule de depart de l'algorithme de victoire
//        List<Cell> neighborCells; // ArrayList qui va stocker les celluls voisines
//
//        startingCell = grid.getCell(i,j);
//        grid.setPast(startingCell); // On dit qu'on est deja passe par cette cellule pour ne pas y retourner
//        neighborCells = researchSecondNeighboors(startingCell, player); // On récupère les cellules voisines de la cellule de départ
//        victory = researchChainWinner(neighborCells); // On lance l'algo récursif
//
//        // Si il y a victory on affecte le gagnant au bon player
//        if (victory) {
//            if (startingCell.getColor() == Color.BLUE)
//                setWinner(Color.BLUE);
//
//            else
//                setWinner(Color.RED);
//        }else{
//        	for (Cell cell : researchNeighborCells(startingCell)) {
//				researchChainVictory(cell.getX(), cell.getY());
//			}
//        }
//
//        grid.resetPast(); // On remet l'attribut passe de toutes les cellules a false
//        setChanged();
//        notifyObservers();
    }
//
    public boolean researchWinner(ArrayList<Cell> neighborCells) {

        boolean victory = false;
        ArrayList<Cell> nextNeighborCells; // Arraylist qui va stocker les cellules voisines des cellules de l'arrayList passée en paramètre

        // On teste si il y a des cellules voisines
        if (!neighborCells.isEmpty()) {
            // Pour toutes chaque cellule de l'arrayList passe en parametre on va recuperer les cellules voisines de ces  cellules
            for (Cell cell : neighborCells) {
                nextNeighborCells = researchNeighborCells(cell);

                // Test de victory
                for (Cell cells : nextNeighborCells) {
                    if (cells.getColor() == Color.BLUE && cells.getX() >= grid.getNbLines()-2)
                        victory = true;
                    if (cells.getColor() == Color.RED && cells.getY() >= grid.getNbColumns()-2)
                        victory = true;
                }

                // Si le player ne gagne pas on relance l'algorithme de recherche de gagnant
                if (!victory)
                    victory = researchWinner(nextNeighborCells);
            }
        }

        return victory;
    }
    
    
    public boolean researchChainWinner(List<Cell> neighborCells) {

        boolean victory = false;
        List<Cell> nextNeighborCells; // Arraylist qui va stocker les cellules voisines des cellules de l'arrayList passée en paramètre

        // On teste si il y a des cellules voisines
        if (!neighborCells.isEmpty()) {
            // Pour toutes chaque cellule de l'arrayList passe en parametre on va recuperer les cellules voisines de ces  cellules
            for (Cell cell : neighborCells) {
                nextNeighborCells = researchSecondNeighboors(cell, cell.getColor());

                // Test de victory
                for (Cell cells : nextNeighborCells) {
                    if (cells.getColor() == Color.BLUE && cells.getX() >= grid.getNbLines()-2)
                        victory = true;
                    if (cells.getColor() == Color.RED && cells.getY() >= grid.getNbColumns()-2)
                        victory = true;
                }

                // Si le player ne gagne pas on relance l'algorithme de recherche de gagnant
                if (!victory)
                    victory = researchChainWinner(nextNeighborCells);
            }
        }

        return victory;
    }

    public ArrayList<Cell> researchNeighborCells(Cell cellForResearch) {

        ArrayList<Cell> neighborCells = new ArrayList<>(); // Arraylist qui va stocker les cellules voisines si elles existes
        int i = cellForResearch.getX();
        int j = cellForResearch.getY();
        Color color = cellForResearch.getColor();

        // On créer 6 celules qui correspondent aux 6 cellules voisines d'une cellule
        Cell c1 = grid.getCell(i-1,j);
        Cell c2 = grid.getCell(i-1,j+1);
        Cell c3 = grid.getCell(i,j+1);
        Cell c4 = grid.getCell(i+1,j);
        Cell c5 = grid.getCell(i+1,j-1);
        Cell c6 = grid.getCell(i,j-1);

        // En revanche pour les cellules des coins et du bord il n'y a pas 6 cellules voisines mais moins, c'est pourquoi on test si elles sont null.
        // Si elles le sont c'est qu'il n'existe pas de cellule voisines avec les coordonées reseignéé.

        if (c1 != null && !grid.getPast(c1) && c1.getColor() == color) {
            neighborCells.add(c1);
            grid.setPast(c1);
        }

        if (c2 != null && !grid.getPast(c2) && c2.getColor() == color) {
            neighborCells.add(c2);
            grid.setPast(c2);
        }

        if (c3 != null && !grid.getPast(c3) && c3.getColor() == color) {
            neighborCells.add(c3);
            grid.setPast(c3);
        }

        if (c4 != null && !grid.getPast(c4) && c4.getColor() == color) {
            neighborCells.add(c4);
            grid.setPast(c4);
        }

        if (c5 != null && !grid.getPast(c5) && c5.getColor() == color) {
            neighborCells.add(c5);
            grid.setPast(c5);
        }

        if (c6 != null && !grid.getPast(c6) && c6.getColor() == color) {
            neighborCells.add(c6);
            grid.setPast(c6);
        }

        return neighborCells;
    }
    
    public List<Cell> researchSecondNeighboors(Cell cell, Color c){
		List<Cell> winningNeighboors = new ArrayList<Cell>();
		Cell celltmp = grid.getCell(cell.getX()+2, cell.getY()-1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()+1, cell.getY()+1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()+1, cell.getY()-2);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-1, cell.getY()-1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-2, cell.getY()+1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-1, cell.getY()+2);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		return winningNeighboors;
	}
    
    
    public List<Cell> researchSecondNeighboorsDown(Cell cell, Color c){
		List<Cell> winningNeighboors = new ArrayList<Cell>();
		Cell celltmp = grid.getCell(cell.getX()+2, cell.getY()-1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()+1, cell.getY()+1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()+1, cell.getY()-2);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		return winningNeighboors;
	}
    
    public List<Cell> researchSecondNeighboorsRight(Cell cell, Color c){
		List<Cell> winningNeighboors = new ArrayList<Cell>();
		Cell celltmp = grid.getCell(cell.getX()-1, cell.getY()-1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-2, cell.getY()+1);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-1, cell.getY()+2);
		if(celltmp!=null && celltmp.getColor().equals(c)){
			winningNeighboors.add(celltmp);
		}
		
		return winningNeighboors;
	}
    
    public boolean isFreeToTake(Cell c1, Cell c2){
    	List<Cell> c1Neig = researchFreeNeighborCells(c1);
    	List<Cell> c2Neig = researchFreeNeighborCells(c2);
    	List<Cell> commonCells = new ArrayList<Cell>();
    	
    	for (Cell cell : c1Neig) {
			if(c2Neig.contains(cell))
				commonCells.add(cell);
		}
    	
    	if(commonCells.size() == 2)
    		return true;
    	return false;
    }
    
//    public List<Cell> getCommonList(Cell c1, Cell c2){
//    	List<Cell> c1Neig = researchFreeNeighborCells(c1);
//    	List<Cell> c2Neig = researchFreeNeighborCells(c2);
//    	List<Cell> commonCells = new ArrayList<Cell>();
//    	
//    	for (Cell cell : c1Neig) {
//			if(c2Neig.contains(cell))
//				commonCells.add(cell);
//		}
//    }
    
    
    
    public ArrayList<Cell> researchFreeNeighborCells(Cell cellForResearch) {

        ArrayList<Cell> neighborCells = new ArrayList<>(); // Arraylist qui va stocker les cellules voisines si elles existes
        int i = cellForResearch.getX();
        int j = cellForResearch.getY();
        Color color = cellForResearch.getColor();

        // On créer 6 celules qui correspondent aux 6 cellules voisines d'une cellule
        Cell c1 = grid.getCell(i-1,j);
        Cell c2 = grid.getCell(i-1,j+1);
        Cell c3 = grid.getCell(i,j+1);
        Cell c4 = grid.getCell(i+1,j);
        Cell c5 = grid.getCell(i+1,j-1);
        Cell c6 = grid.getCell(i,j-1);

        // En revanche pour les cellules des coins et du bord il n'y a pas 6 cellules voisines mais moins, c'est pourquoi on test si elles sont null.
        // Si elles le sont c'est qu'il n'existe pas de cellule voisines avec les coordonées reseignéé.

        if (c1 != null && c1.getColor() == Color.WHITE) {
            neighborCells.add(c1);
            grid.setPast(c1);
        }

        if (c2 != null && c2.getColor() == Color.WHITE) {
            neighborCells.add(c2);
            grid.setPast(c2);
        }

        if (c3 != null && c3.getColor() == Color.WHITE) {
            neighborCells.add(c3);
            grid.setPast(c3);
        }

        if (c4 != null && c4.getColor() == Color.WHITE) {
            neighborCells.add(c4);
            grid.setPast(c4);
        }

        if (c5 != null && c5.getColor() == Color.WHITE) {
            neighborCells.add(c5);
            grid.setPast(c5);
        }

        if (c6 != null && c6.getColor() == Color.WHITE) {
            neighborCells.add(c6);
            grid.setPast(c6);
        }

        return neighborCells;
    }
    
    public List<Cell> researchFreeSecondNeighboors(Cell cell){
		List<Cell> winningNeighboors = new ArrayList<Cell>();
		Cell celltmp = grid.getCell(cell.getX()+2, cell.getY()-1);
		if(celltmp!=null && celltmp.getColor().equals(Color.WHITE) && isFreeToTake(cell, celltmp)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()+1, cell.getY()+1);
		if(celltmp!=null && celltmp.getColor().equals(Color.WHITE) && isFreeToTake(cell, celltmp)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()+1, cell.getY()-2);
		if(celltmp!=null  && celltmp.getColor().equals(Color.WHITE) && isFreeToTake(cell, celltmp)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-1, cell.getY()-1);
		if(celltmp!=null && celltmp.getColor().equals(Color.WHITE) && isFreeToTake(cell, celltmp)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-2, cell.getY()+1);
		if(celltmp!=null && celltmp.getColor().equals(Color.WHITE) && isFreeToTake(cell, celltmp)){
			winningNeighboors.add(celltmp);
		}
		
		celltmp = grid.getCell(cell.getX()-1, cell.getY()+2);
		if(celltmp!=null && celltmp.getColor().equals(Color.WHITE) && isFreeToTake(cell, celltmp)){
			winningNeighboors.add(celltmp);
		}
		
		return winningNeighboors;
	}
    
    public HexModel clone(){
    	HexModel newModel = new HexModel();
    	
    	newModel.inGame = this.inGame;
    	newModel.currentGame = this.currentGame;
    	newModel.finished = this.finished;
    	newModel.player = this.player;
    	newModel.winner = this.winner;
    	newModel.victory = this.victory;
    	newModel.grid = this.grid.clone();
    	newModel.lastBlueCell = this.lastBlueCell.clone();
    	newModel.lastRedCell = this.lastRedCell.clone();
    	
    	return newModel;    	
    }
}
