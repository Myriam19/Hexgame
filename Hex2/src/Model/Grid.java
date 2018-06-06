package Model;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class Grid extends ArrayList<Cell>{

    private int nbLines;
    private int nbColumns;
    public List<Cell> blueCells = new ArrayList<Cell>();
    public List<Cell> redCells = new ArrayList<Cell>();
    public Map<Cell, List<Cell>> bluesChain = new HashMap<Cell, List<Cell>>();

    public Grid(int nbLines, int nbColumns) {
        this.nbLines = nbLines;
        this.nbColumns = nbColumns;
    }
    public void buildGrid() {
        Cell c;
        for (int i = 0; i < nbLines; i++) {
            for (int j = 0; j < nbColumns; j++) {
                if((i != 0 && j != nbColumns -1) || (i != nbLines -1 && j != 0 )) {
                    if (i == 0){
                        c = new Cell(i,j, Color.BLUE,true);
                        blueCells.add(c);
                    }
                    else if (i == nbLines - 1){
                        c = new Cell(i,j,Color.BLUE,true);
                        blueCells.add(c);
                    }
                    else if (j == 0){
                        c = new Cell(i,j,Color.RED,true);
                        redCells.add(c);
                    }
                    else if (j == nbColumns - 1){
                        c = new Cell(i,j, Color.RED,true);
                        redCells.add(c);
                    }
                    else
                        c = new Cell(i,j, Color.WHITE,false);

                    add(c);
                }
            }
        }
    }

    public int getNbLines(){
        return nbLines;
    }

    public int getNbColumns(){
        return nbColumns;
    }

    public Cell getCell(int i, int j) {
        Cell cell = null;
        for (Cell c : this){
            if (c.getX() == i && c.getY() == j) {
                cell = c;
            }
        }
        return cell;
    }

    public void setPast(Cell cell) {
        cell.setPast(true);
    }

    public boolean getPast(Cell cell) {
        return cell.getPast();
    }

    public void resetPast() {
        for (Cell c : this)
            c.setPast(false);
    }
    
    public List<Cell> getFreeCells(){
    	List<Cell>  free = new ArrayList<Cell>();
		for (Cell c : this) {
			if((c.getColor() == Color.WHITE)){
				free.add(c);
			}
		}
		return free;
    }
    
    public Grid clone(){
    	Grid newGrid = new Grid(this.nbLines, this.nbColumns);
    	
    	for (Cell cell : this) {
			Cell newCell = cell.clone();
			newGrid.add(newCell);
		}
    	
    	for (Cell cell : this.blueCells) {
			newGrid.blueCells.add(newGrid.getCell(cell.getX(), cell.getY()));
		}
    	
    	for (Cell cell : this.redCells) {
			newGrid.redCells.add(newGrid.getCell(cell.getX(), cell.getY()));
		}
    	
    	return newGrid;
    	
    }
}
