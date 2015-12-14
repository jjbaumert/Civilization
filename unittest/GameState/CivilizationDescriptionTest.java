package GameState;

import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class CivilizationDescriptionTest {
    Civilizations civilizations;
    CivilizationDescription civilizationDescription;

    @Before
    public void setUp() throws Exception {
        civilizations = new Civilizations();
        civilizations.loadCivilizations();
        civilizationDescription = civilizations.getCivilization("Crete");
    }

    @Test
    public void testGetColor() throws Exception {
        assertTrue(civilizationDescription.getColor().equals(Color.web("#009900")));
    }

    @Test
    public void testGetName() throws Exception {
        assertTrue(civilizationDescription.getName().equals("Crete"));
    }

    @Test
    public void testGetStartingTerritories() throws Exception {
        Set<String> startingTerritories = civilizationDescription.getStartingTerritories();
        assertTrue(startingTerritories.size()==2);
        assertTrue(startingTerritories.contains("Phaesios"));
        assertTrue(startingTerritories.contains("Knossus"));
    }

    @Test
    public void testGetValueToExit() throws Exception {
        for(int x=1; x<16; x++) {
            if(x<14) {
                assertTrue(civilizationDescription.getValueToExit(x)==0);
            } else if(x==14) {
                assertTrue(civilizationDescription.getValueToExit(x)==1300);
            } else if(x==15) {
                assertTrue(civilizationDescription.getValueToExit(x)==1600);
            }
        }
    }
}