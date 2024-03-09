package cs3500.reversi.strategy;

import java.util.Optional;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a strategy that has the ability to choose the best move for a player
 * based on this strategy.
 */
public interface Strategy {
  /**
   * Choose the best move (or the uppermost-leftmost coordinate if there are a tie,
   * upper-ness comes before the left-ness) for the given player for the given game.
   * @param model the model of the game that the player is currently playing
   * @param player the player to choose the move for
   * @return an Optional of HexagonalCoordinates which is present if there is a best move
   *         exist based on this strategy or empty if there is not any possible move
   */
  Optional<Coordinates> chooseMove(ReversiModelReadOnly model, Player player);
}
