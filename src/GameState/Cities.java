package GameState;

import java.util.HashSet;
import java.util.Set;

public class Cities {
    class CityTokensNotAvailable extends Exception {}
    class CityRegionDuplicate extends Exception {}
    class CityAlreadyBuilt extends Exception {}
    class CityAlreadyMarked extends Exception {}

    private Set<City> placedCities;
    private Set<City> markedForReduction;
    private Set<String> markedForBuilding;

    Cities() {
        placedCities = new HashSet<>();
        markedForBuilding = new HashSet<>();
        markedForReduction = new HashSet<>();
    }

    int getCityTokensAvailable() {
        return 9-countMarkedOrPlaced();
    }

    int citiesBuilt() {
        return placedCities.size();
    }

    void markForReduction(Set<City> cities) throws CityAlreadyMarked {
        for(City markedCity: markedForReduction) {
            for(City city: cities) {
                if(markedCity.getRegionName().equals(city.getRegionName())) {
                    throw new CityAlreadyMarked();
                }
            }
        }
        markedForReduction.addAll(cities);
    }

    Set<String> reduceMarkedCities() {
        return new HashSet<>();
    }

    Set<City> markedForReduction() {
        return markedForReduction;
    }

    int countMarkedOrPlaced() {
        return placedCities.size() + markedForBuilding.size();
    }

    void markForBuilding(Set<String> regionNames) throws CityAlreadyBuilt, CityRegionDuplicate, CityTokensNotAvailable {
        if((9-countMarkedOrPlaced())<regionNames.size()) {
            throw new CityTokensNotAvailable();
        }

        for(String regionName: regionNames) {
            for(City city: placedCities) {
                if(city.getRegionName().equals(regionName)) {
                    throw new CityAlreadyBuilt();
                }
            }

            for(String cityName: markedForBuilding) {
                if(cityName.equals(regionName)) {
                    throw new CityRegionDuplicate();
                }
            }
        }

        markedForBuilding.addAll(regionNames);
    }

    Set<City> buildMarkedCities() throws CityTokensNotAvailable {
        Set<City> cities = new HashSet<>();

        for(String regionName: markedForBuilding) {
            City city = new City();
            city.setRegion(regionName);
            cities.add(city);
        }

        placedCities.addAll(cities);
        markedForBuilding.clear();

        return placedCities;
    }
}
