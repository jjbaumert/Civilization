package GameState;

public class GameState {
    //
    // Game Sequence
    //
    //      Setup
    //
    //          Layout Archaeological Succession Chart (A.S.T.)
    //          Layout Player Map
    //          Shuffle Trading Cards
    //              Set aside cards for the number of players and shuffle
    //              Shuffle remaining cards with tradeable calamity
    //              Place non-tradeable calamity at bottom of stack
    //          Layout Civilization Cards
    //              For smaller games optionally eliminate some cards
    //          Select Number of Players
    //              Determines available map
    //              Determines number of tokens
    //          Select Civilization
    //          Give out Tokens
    //
    //
    //      Game Sequence
    //
    //          Collect Taxation
    //          Population Expansion
    //          Census
    //          Construct Ships (Remove Surplus Ships)
    //          Movement
    //          Conflict
    //          Build Cities
    //          Remove Surplus Population (Reduce Unsupported Cities)
    //          Acquire Trade Cards (Buy Gold)
    //          Trade
    //          Acquire Civilization Cards (Return excess trade cards)
    //          Resolve Calamities (Reduce Unsupported Cities)
    //          Alter A.S.T.
    //          Check Victory Conditions
    //

    int numberOfPlayers;

    GameState(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
