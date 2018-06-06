package TU;

import Model.Cell;
import Model.Grid;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Abomnes Gauthier
 * Bretheau Yann
 * S3C
 */

public class GridTest {

    // On définit la grid pour les tests
    Grid gridTest = new Grid(10,10);

    // Ce test permet également de tester la méthode getCell
    @Test
    public void testBuildGrid() {
        gridTest.buildGrid();

        // On récupère les cellules pour tester leur couleurs pour voir si la création de la grid fonctionne
        Cell c1 = gridTest.getCell(5,5);
        Color couleur1 = c1.getColor();

        Cell c2 = gridTest.getCell(0,3);
        Color couleur2 = c2.getColor();

        Cell c3 = gridTest.getCell(3,0);
        Color couleur3 = c3.getColor();

        assertEquals(couleur1, Color.WHITE);
        assertEquals(couleur2, Color.BLUE);
        assertEquals(couleur3, Color.RED);
    }

    @Test
    public void testResetPasse() {
        gridTest.buildGrid();

        // On met l'attribut passe a vrai pour les cellules de la ligne 5
        for (Cell c : gridTest) {
            if (c.getX() == 5)
                c.setPast(true);
        }

        // On test la méthode restetPast qui doit mettre l'atribut passe de toutes les cellules de la grid a false
        gridTest.resetPast();

        // Si l'attribut d'une des cellules est toujours a true, le boolean passeTest passera a true
        boolean passeTest = false;
        for (Cell c : gridTest) {
            if (c.getPast() == true)
                passeTest = true;
        }

        assertEquals(passeTest,false);
    }
}