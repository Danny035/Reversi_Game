package cs3500.reversi.strategy;

import java.util.List;
import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a strategy that can pick a list of best moves for a player
 * based on this strategy, which can also be chained with other strategies
 * to create combined strategies with each strategy determining which coordinates
 * from all the coordinates that are available to move into that it favors.
 */
public interface ChainableStrategy {
  /**
   * Choose the best move (or the uppermost-leftmost coordinate if there are a tie,
   * upper-ness comes before the left-ness) for the given player for the given game.
   * @param model the model of the game that the player is currently playing
   * @param player the player to choose the move for
   * @return an Optional of HexagonalCoordinates which is present if there is a best move
   *         exist based on this strategy or empty if there is not any possible move
   */
  List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player);

  /**
   * Choose the best move (or the uppermost-leftmost coordinate if there are a tie,
   * upper-ness comes before the left-ness) for the given player for the given game.
   * @param model the model of the game that the player is currently playing
   * @param player the player to choose the move for
   * @param coordsAvailableToMoveTo a list of coordinates that is known that this player
   *                                is able to move to
   * @return an Optional of HexagonalCoordinates which is present if there is a best move
   *         exist based on this strategy or empty if there is not any possible move
   */
  List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player,
                               List<Coordinates> coordsAvailableToMoveTo);
}
