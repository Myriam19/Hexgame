package TU;

import Model.Cell;
import Model.HexModel;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.util.ArrayList;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class HexModelTest {

    //On définit le model pour les tests
    HexModel modelTest = new HexModel(9);

    @Test
    public void testChangePlayer(){
        modelTest.setPlayer(Color.BLUE);
        Color playerTest = modelTest.getPlayer();

        Assert.assertEquals(playerTest,Color.BLUE);
    }

    @Test
    public void testSetWinnerBlue(){
        modelTest.setWinner(Color.BLUE);
        Color winnerTest = modelTest.getWinner();

        Assert.assertEquals(winnerTest,Color.BLUE);
    }

    @Test
    public void testSetWinnerRed(){
        modelTest.setWinner(Color.RED);
        Color winnerTest = modelTest.getWinner();

        Assert.assertEquals(winnerTest,Color.RED);
    }

    @Test
    public void testVicrotyEmptyGrid(){
        modelTest.researchVictory(0,1);
        boolean vicrotyTest = modelTest.getVictory();

        Assert.assertEquals(vicrotyTest,false);
    }

    @Test
    public void testVicrotyFullGrid(){
        // On rempli la grille avec des cellules bleus pour faire un chemin et tester la victoire
        for (Cell c: modelTest.grid) {
            if (c.getY() == 5)
                c.setColor(Color.BLUE);
        }

        modelTest.researchVictory(0,1);
        boolean vicrotyTest = modelTest.getVictory();
        // On a rempli la grille de cellule bleu, on test également la victoire du bleu
        Color winnerTest = modelTest.getWinner();

        Assert.assertEquals(vicrotyTest,true);
        Assert.assertEquals(winnerTest,Color.BLUE);
    }

    @Test
    public void testVictoryBlue(){
        // On rempli la grid avec des cellules bleus pour faire un chemin et tester la victoire bleu
        for (Cell c: modelTest.grid) {
            if (c.getY() == 5)
                c.setColor(Color.BLUE);
        }

        modelTest.researchVictory(0,1);
        Color winnerTest = modelTest.getWinner();

        Assert.assertEquals(winnerTest,Color.BLUE);
    }

    @Test
    public void testVictoryRed(){
        // On rempli la grille avec des cellules rouges pour faire un chemin et tester la victoire rouge
        for (Cell c: modelTest.grid) {
            if (c.getX() == 6)
                c.setColor(Color.RED);
        }

        modelTest.researchVictory(1,0);
        Color winnerTest = modelTest.getWinner();

        Assert.assertEquals(winnerTest,Color.RED);
    }

    @Test
    public void testResearchNeighborCells(){

        // On définit la cellule avec laquelle on lancera la recherche
        int i = 4;
        int j = 4;
        Cell startingCellTest = modelTest.grid.getCell(i,j);
        startingCellTest.setColor(Color.BLUE);

        // On définit les deux ArrayList qui vont stocker les cellules voisines bleu et rouge
        ArrayList<Cell> neighborCellBlueTest;

        // On rempli la grille avec des cellules de couleur pour tester la recherche des cellules voisines
        Cell c1 = modelTest.grid.getCell(i-1,j);
        Cell c2 = modelTest.grid.getCell(i-1,j+1);
        Cell c3 = modelTest.grid.getCell(i,j+1);
        Cell c4 = modelTest.grid.getCell(i+1,j);
        Cell c5 = modelTest.grid.getCell(i+1,j-1);
        Cell c6 = modelTest.grid.getCell(i,j-1);

        c1.setColor(Color.BLUE);
        c2.setColor(Color.RED);
        c3.setColor(Color.BLUE);
        c4.setColor(Color.RED);
        c5.setColor(Color.BLUE);
        c6.setColor(Color.BLUE);

        neighborCellBlueTest = modelTest.researchNeighborCells(startingCellTest);

        Assert.assertEquals(neighborCellBlueTest.size(),4);
    }
}