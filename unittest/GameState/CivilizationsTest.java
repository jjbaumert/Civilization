package GameState;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class CivilizationsTest {
    Civilizations civilizations;

    @Before
    public void setUp() throws Exception {
        civilizations = new Civilizations();
        civilizations.loadCivilizations();
    }

    @Test
    public void testGetNames() throws Exception {
        Set<String> names = civilizations.getNames();

        assertTrue(names.size()==9);
        assertTrue(names.contains("Africa"));
        assertTrue(names.contains("Italy"));
        assertTrue(names.contains("Illyria"));
        assertTrue(names.contains("Thrace"));
        assertTrue(names.contains("Crete"));
        assertTrue(names.contains("Asia"));
        assertTrue(names.contains("Assyria"));
        assertTrue(names.contains("Babylon"));
        assertTrue(names.contains("Egypt"));
    }
}