package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a strategy in playing Reversi that avoid all cells next to corners to avoid
 * giving the opponent the ability to get a corner on their next turn.
 */
public class AvoidGivingCorners extends AbstractChainableStrategy {
  @Override
  public  List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player,
                                             List<Coordinates> coordsAvailableToMoveTo) {
    List<Coordinates> allCorners = model.getCorners();
    List<Coordinates> cornerNeighbors = new ArrayList<>();
    for (Coordinates corner : allCorners) {
      cornerNeighbors.addAll(corner.getAllNeighbors());
    }
    coordsAvailableToMoveTo.removeAll(cornerNeighbors);
    return coordsAvailableToMoveTo;
  }
}
