package Controller;
import Model.Cell;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import Model.HexModel;

public class RandomPlayer {
	private HexModel model;
	RandomPlayer(HexModel model){
		
		this.model = model;
	}
	Cell randomplay(){
		ArrayList<Cell>  free = new ArrayList<Cell>();
		Random rand = new Random();
		for (Cell c : model.grid) {
			if(c.getColor() == Color.WHITE){
				free.add(c);
			}
		}
		
		Cell c = free.get(rand.nextInt(free.size()));	
        System.out.println(c.getX()+" "+c.getY());
		
		return c;
	}
	

}
