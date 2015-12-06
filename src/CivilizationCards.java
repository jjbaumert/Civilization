import java.util.Map;

public class CivilizationCards {
    protected Map<String, CivilizationCard> cards;

    CivilizationCards() {
    }

    public void loadCardDescriptions() {
    }

    public int getCost(String cardName, Map<String, CivilizationCard> cards) {
        return 0;
    }

    public boolean checkPrerequisites(String cardName, Map<String, CivilizationCard> cards) {
        return false;
    }

    public CivilizationCard getCard(String cardName) {
        if(cards.containsKey(cardName)) {
            return cards.get(cardName);
        }
        return null;
    }
}
