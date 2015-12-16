package GameState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CitiesTest {
    Cities cities;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        cities = new Cities();
    }

    @Test
    public void testCitiesBuilt() throws Exception {
    }

    @Test
    public void testCityTokenAvailable() throws Exception {

    }

    @Test
    public void testMarkForReduction() throws Exception {

    }

    @Test
    public void testReduceMarkedCities() throws Exception {

    }

    @Test
    public void testMarkForBuilding_Simple() throws Exception {
        Set<String> cityList = new HashSet<>();

        assertTrue("Mark city for building failed.",cities.countMarkedOrPlaced()==0);
        cityList.clear();
        cityList.add("City");
        cities.markForBuilding(cityList);
        assertTrue("Mark city for building failed.",cities.countMarkedOrPlaced()==1);
    }

    @Test
    public void testMarkForBuilding_Duplicate() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cities.markForBuilding(cityList);
        assertTrue(cities.countMarkedOrPlaced()==1);
        exception.expect(Cities.CityRegionDuplicate.class);
        cities.markForBuilding(cityList);
    }

    @Test
    public void testMarkForBuilding_Twice() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cityList.add("1");
        cities.markForBuilding(cityList);
        exception.expect(Cities.CityRegionDuplicate.class);
        cities.markForBuilding(cityList);
    }

    @Test
    public void testMarkForBuilding_Empty() throws Exception {
        Set<String> cityList = new HashSet<>();

        assertTrue(cities.getCityTokensAvailable()==9);
        cities.markForBuilding(cityList);
        assertTrue(cities.getCityTokensAvailable()==9);
    }

    @Test
    public void testMarkForBuilding_AlreadyBuilt() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cities.markForBuilding(cityList);
        cities.buildMarkedCities();
        exception.expect(Cities.CityAlreadyBuilt.class);
        cities.markForBuilding(cityList);
    }

    @Test
    public void testMarkForBuilding_MoreThanPossible() throws Exception {
        Set<String> cityList = new HashSet<>();

        for(int x=1; x<=9; x++) {
            cityList.clear();
            cityList.add(Integer.toString(x));
            cities.markForBuilding(cityList);
        }

        assertTrue(cities.countMarkedOrPlaced() == 9);

        cityList.clear();
        cityList.add("10");
        exception.expect(Cities.CityTokensNotAvailable.class);
        cities.markForBuilding(cityList);
        assertTrue(cities.countMarkedOrPlaced() == 9);
    }

    @Test
    public void testBuildMarkedCities() throws Exception {

    }

    @Test
    public void testMarkedForReduction() throws Exception {

    }
}