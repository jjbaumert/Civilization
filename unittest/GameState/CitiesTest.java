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
    public void testMarkForReduction_Empty() throws Exception {
        Set<String> cityList = new HashSet<>();

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        cities.markForReduction(cityList);
        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
    }

    @Test
    public void testMarkForReduction_Simple() throws Exception {
        Set<String> cityList = new HashSet<>();
        cityList.add("1");

        cities.markForBuilding(cityList);
        cities.buildMarkedCities();

        assertTrue(cities.getCitiesMarkedForReduction().size() == 0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);

        cities.markForReduction(cityList);

        assertTrue(cities.getCitiesMarkedForReduction().size()==1);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
    }

    @Test
    public void testMarkForReduction_NotPlaced() throws Exception {
        Set<String> cityList = new HashSet<>();
        cityList.add("1");
        cities.markForBuilding(cityList);
        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        assertTrue(cities.numberOfCitiesBuilt()==1);

        cityList.clear();
        cityList.add("2");
        exception.expect(Cities.CityNotPlaced.class);
        cities.markForReduction(cityList);

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        assertTrue(cities.numberOfCitiesBuilt()==1);
    }

    @Test
    public void testMarkForReduction_AlreadyMarked() throws Exception {
        Set<String> cityList = new HashSet<>();
        cityList.add("1");
        cities.markForBuilding(cityList);
        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        assertTrue(cities.numberOfCitiesBuilt()==1);

        cities.markForReduction(cityList);

        assertTrue(cities.getCitiesMarkedForReduction().size()==1);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        assertTrue(cities.numberOfCitiesBuilt()==1);

        exception.expect(Cities.CityAlreadyMarked.class);
        cities.markForReduction(cityList);

        assertTrue(cities.getCitiesMarkedForReduction().size()==1);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        assertTrue(cities.numberOfCitiesBuilt()==1);
    }

    @Test
    public void testReduceMarkedCities_EmptyReduced_EmptyCities() throws Exception {
        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==0);
        assertTrue(cities.numberOfCitiesBuilt()==0);

        cities.reduceMarkedCities();

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==0);
        assertTrue(cities.numberOfCitiesBuilt()==0);
    }
    @Test

    public void testReduceMarkedCities_EmptyReduced_WithCities() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cityList.add("2");

        cities.markForBuilding(cityList);
        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }

        cityList.clear();
        cityList.add("3");
        cities.markForBuilding(cityList);

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==3);
        assertTrue(cities.numberOfCitiesBuilt()==2);

        cities.reduceMarkedCities();

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==3);
        assertTrue(cities.numberOfCitiesBuilt()==2);
    }

    @Test
    public void testReduceMarkedCities_Simple() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cityList.add("2");

        cities.markForBuilding(cityList);
        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }

        cityList.clear();
        cityList.add("1");

        cities.markForReduction(cityList);

        assertTrue(cities.getCitiesMarkedForReduction().size()==1);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==2);
        assertTrue(cities.numberOfCitiesBuilt()==2);

        cities.reduceMarkedCities();

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        assertTrue(cities.numberOfCitiesBuilt()==1);

        assertTrue(cities.getBuiltCities().iterator().next().equals("2"));
    }

    @Test
    public void testReduceMarkedCities_All() throws Exception {
        Set<String> cityList = new HashSet<>();

        for(int x=1; x<=9; x++) {
            cityList.add(Integer.toString(x));
        }

        cities.markForBuilding(cityList);
        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }
        cities.markForReduction(cityList);

        assertTrue(cities.getCitiesMarkedForReduction().size()==9);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==9);
        assertTrue(cities.numberOfCitiesBuilt()==9);

        cities.reduceMarkedCities();

        assertTrue(cities.getCitiesMarkedForReduction().size()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==0);
        assertTrue(cities.numberOfCitiesBuilt()==0);
    }

    @Test
    public void testMarkForBuilding_Simple() throws Exception {
        Set<String> cityList = new HashSet<>();

        assertTrue("Mark city for building failed.",cities.numberOfCitiesMarkedOrPlaced()==0);
        cityList.clear();
        cityList.add("City");
        cities.markForBuilding(cityList);
        assertTrue("Mark city for building failed.",cities.numberOfCitiesMarkedOrPlaced()==1);
    }

    @Test
    public void testMarkForBuilding_Duplicate() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cities.markForBuilding(cityList);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        exception.expect(Cities.CityRegionDuplicate.class);
        cities.markForBuilding(cityList);
    }

    @Test
    public void testMarkForBuilding_Twice() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cities.markForBuilding(cityList);
        exception.expect(Cities.CityRegionDuplicate.class);
        cities.markForBuilding(cityList);
    }

    @Test
    public void testMarkForBuilding_Empty() throws Exception {
        Set<String> cityList = new HashSet<>();

        assertTrue(cities.numberOfCityTokensAvailable()==9);
        cities.markForBuilding(cityList);
        assertTrue(cities.numberOfCityTokensAvailable()==9);
    }

    @Test
    public void testMarkForBuilding_AlreadyBuilt() throws Exception {
        Set<String> cityList = new HashSet<>();

        cityList.add("1");
        cities.markForBuilding(cityList);
        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }
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

        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==9);

        cityList.clear();
        cityList.add("10");
        exception.expect(Cities.CityTokensNotAvailable.class);
        cities.markForBuilding(cityList);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==9);
    }

    @Test
    public void testBuildMarkedCities_Empty() throws Exception {
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==0);
        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==0);
    }

    @Test
    public void testBuildMarkedCities_Simple() throws Exception {
        Set<String> cityList = new HashSet<>();
        cityList.add("1");

        assertTrue(cities.numberOfCitiesBuilt()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==0);
        assertTrue(cities.numberOfCityTokensAvailable()==9);

        cities.markForBuilding(cityList);

        assertTrue(cities.numberOfCitiesBuilt()==0);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);
        assertTrue(cities.numberOfCityTokensAvailable()==8);

        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }

        assertTrue(cities.numberOfCitiesBuilt()==1);
        assertTrue(cities.numberOfCityTokensAvailable()==8);
        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==1);

    }

    @Test
    public void testBuildMarkedCities_Full() throws Exception {
        Set<String> cityList = new HashSet<>();

        for(int x=1; x<=9; x++) {
            cityList.clear();
            cityList.add(Integer.toString(x));
            cities.markForBuilding(cityList);
        }

        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==9);
        assertTrue(cities.numberOfCitiesBuilt()==0);

        try {
            cities.buildMarkedCities();
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }

        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==9);
        assertTrue(cities.numberOfCitiesBuilt()==9);

        try {
            cities.buildMarkedCities(); // empty build
        } catch (Cities.CityTokensNotAvailable cityTokensNotAvailable) {
            cityTokensNotAvailable.printStackTrace();
        }

        assertTrue(cities.numberOfCitiesMarkedOrPlaced()==9);
        assertTrue(cities.numberOfCitiesBuilt()==9);
    }
}