package cs3500.reversi.player;

/**
 * A creator class for creating a specific type of player that will be playing
 * a Reversi game. Type of players can be human or machine players, with machine players
 * distinguished by the type of strategy they use.
 */
public class PlayerCreator {
  /**
   * Represents a type of player, either a human player or machine player
   * specified by the strategy it uses.
   */
  public enum PlayerType {
    HUMAN, SIMPLISTIC, SMARTER, MINIMAX;
  }

  /**
   * Creates a player corresponding to the given specified type.
   * Default to return a human player if the given type isn't one of the
   * three machine players.
   * @return a player object corresponding to the given specified type
   */
  public static Player create(PlayerType type) {
    switch (type) {
      case SIMPLISTIC:
        return new SimplisticPlayer();
      case SMARTER:
        return new SmarterPlayer();
      case MINIMAX:
        return new MinimaxPlayer();
      default:
        return new HumanPlayer();
    }
  }
}
