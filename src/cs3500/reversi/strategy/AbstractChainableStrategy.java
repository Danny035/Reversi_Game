package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Cell;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * An abstract base class for all specific strategy implementations in the Reversi game.
 * This abstract class abstract out a common implementation of a version of chooseMove method
 * where the caller of this method doesn't pass in a ready list of available coordinates
 * for this player to place a disc in.
 */
public abstract class AbstractChainableStrategy implements ChainableStrategy {
  @Override
  public List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player) {
    return this.chooseMove(model, player, this.getValidMove(model, player));
  }

  @Override
  public abstract List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player,
                                               List<Coordinates> coordsAvailableToMoveTo);

  /**
   * returns a list of coordinates that the given player is able to place a disc into
   * for this round of the game.
   * @param player the player
   * @return The List of the HexagonalCoordinates that the player can move
   */
  private List<Coordinates> getValidMove(ReversiModelReadOnly model, Player player) {
    List<Coordinates> validMove = new ArrayList<>();
    for (List<Cell> row : model.getBoard()) {
      for (Cell cell : row) {
        Coordinates coordinates = cell.getCoordinates();
        if (model.scoreEarnedByPlaceIn(player, coordinates) > 0) {
          validMove.add(coordinates);
        }
      }
    }
    return validMove;
  }
}
