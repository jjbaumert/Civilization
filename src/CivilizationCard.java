import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CivilizationCard {
    private String name;
    private int cost;
    private String description;

    private Map<String, Integer> credits;
    private Set<String> prerequisites;

    CivilizationCard() {
        credits = new HashMap<>();
        prerequisites = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public int getCost(Map<String, CivilizationCard> cards) {
        return 0;
    }

    public boolean checkPrerequisites(Map<String, CivilizationCard> cards) {
        return false;
    }

    Set<String> getPrerequisites() {
        return prerequisites;
    }
}
