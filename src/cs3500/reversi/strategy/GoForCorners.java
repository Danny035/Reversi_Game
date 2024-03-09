package cs3500.reversi.strategy;

import java.util.List;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a strategy in playing Reversi that try to get the corners of the game board
 * because after taking the corners, there is no way opponent can flip back those the discs.
 */
public class GoForCorners extends AbstractChainableStrategy {
  @Override
  public List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player,
                                      List<Coordinates> coordsAvailableToMoveTo) {
    List<Coordinates> allCorners = model.getCorners();
    // if coords available for the current player contains at least one corner,
    // then only keep all the corners that this player can place a disc into
    // in the list of available coords list as this strategy's choice
    if (coordsAvailableToMoveTo.stream().anyMatch(allCorners::contains)) {
      coordsAvailableToMoveTo.retainAll(allCorners);
    }
    // if there is no corners in the given list of available coords, then just
    // don't make any choice
    return coordsAvailableToMoveTo;
  }
}
