package GameState;

import java.util.*;

public class Cities {
    final int MAXIMUM_CITIES=9;

    class CityTokensNotAvailable extends Exception {}
    class CityRegionDuplicate extends Exception {}
    class CityAlreadyBuilt extends Exception {}
    class CityAlreadyMarked extends Exception {}
    class CityNotPlaced extends Exception {}

    private Map<String, String> builtCities;
    private Set<String> markedForReduction;
    private Set<String> markedForBuilding;

    Cities() {
        builtCities = new HashMap<>();
        markedForBuilding = new HashSet<>();
        markedForReduction = new HashSet<>();
    }

    Collection<String> getBuiltCities() { return builtCities.values(); }

    int numberOfCityTokensAvailable() {
        return MAXIMUM_CITIES-numberOfCitiesMarkedOrPlaced();
    }

    int numberOfCitiesBuilt() {
        return builtCities.size();
    }

    int numberOfCitiesMarkedOrPlaced() {
        return builtCities.size() + markedForBuilding.size();
    }

    void markForReduction(Set<String> cityNames) throws CityAlreadyMarked, CityNotPlaced {
        for(String cityName: cityNames) {
            for(String markedCityName: markedForReduction) {
                if(markedCityName.equals(cityName)) {
                    throw new CityAlreadyMarked();
                }
            }

            if(!builtCities.containsKey(cityName)) {
                throw new CityNotPlaced();
            }
        }

        markedForReduction.addAll(cityNames);
    }

    Set<String> reduceMarkedCities() {
        Set<String> reducedCities = markedForReduction;
        markedForReduction = new HashSet<>();

        reducedCities.forEach(builtCities::remove);

        return reducedCities;
    }

    Set<String> getCitiesMarkedForReduction() {
        return markedForReduction;
    }

    void markForBuilding(Set<String> regionNames) throws CityAlreadyBuilt, CityRegionDuplicate, CityTokensNotAvailable {
        if((9-numberOfCitiesMarkedOrPlaced())<regionNames.size()) {
            throw new CityTokensNotAvailable();
        }

        for(String regionName: regionNames) {
            if (builtCities.containsKey(regionName)) {
                throw new CityAlreadyBuilt();
            }

            for(String cityName: markedForBuilding) {
                if(cityName.equals(regionName)) {
                    throw new CityRegionDuplicate();
                }
            }
        }

        markedForBuilding.addAll(regionNames);
    }

    Collection<String> buildMarkedCities() throws Cities.CityTokensNotAvailable {
        Collection<String> newCities = new HashSet<>();

        for(String regionName: markedForBuilding) {
            builtCities.put(regionName, regionName);
            newCities.add(regionName);
        }

        markedForBuilding.clear();

        return newCities;
    }

    Set<String> getCitiesMarkedForBuilding() {
        return markedForBuilding;
    }
}
