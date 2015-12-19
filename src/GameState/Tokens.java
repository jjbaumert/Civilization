package GameState;

import java.util.HashSet;
import java.util.Set;

public class Tokens {
    public class NotEnoughTokens extends Exception {}
    public class InvalidPopulationLocation extends Exception {}

    protected int unallocatedTokens;
    protected int goldTokens;
    protected int population;

    protected Set<PopulationToken> populationTokens;
    protected Set<PopulationToken> unallocatedPopulationTokens;
    protected Set<PopulationToken> markedPopulationTokens;

    public Set<PopulationToken> getPopulationTokens() {
        return new HashSet<>();
    }

    public int getUnallocatedTokens() {
        return unallocatedTokens;
    }

    public int getGoldTokens() {
        return goldTokens;
    }

    public int getPopulation() {
        return population;
    }

    public int getNumberOfUnallocatedPopulation() {
        return unallocatedPopulationTokens.size();
    }

    public void markPopulationTokenForRegion(String regionName) throws NotEnoughTokens {
        if(unallocatedPopulationTokens.size()==0) {
            throw new NotEnoughTokens();
        }

        PopulationToken token = unallocatedPopulationTokens.iterator().next();
        unallocatedPopulationTokens.remove(token);
        token.setRegionName(regionName);
        markedPopulationTokens.add(token);
    }

    public void unmarkPopulationTokenForRegion(String regionName) throws InvalidPopulationLocation {
        for(PopulationToken token: markedPopulationTokens) {
            if (token.getRegion().equals(regionName)) {
                markedPopulationTokens.remove(token);
                token.setRegionName("");
                unallocatedPopulationTokens.add(token);
                return;
            }
        }

        throw new InvalidPopulationLocation();
    }

    public void finalizedMarkedPopulation() {
        populationTokens.addAll(markedPopulationTokens);
        markedPopulationTokens.clear();
    }

    public void buyGold(int amount) throws NotEnoughTokens {
        if(unallocatedTokens<amount) {
            throw new NotEnoughTokens();
        }

        unallocatedTokens -= amount;
        goldTokens += amount;
    }

    public void useGold(int amount) throws NotEnoughTokens {
        if(goldTokens<amount) {
            throw new NotEnoughTokens();
        }

        goldTokens -= amount;
        unallocatedTokens += amount;
    }
}
