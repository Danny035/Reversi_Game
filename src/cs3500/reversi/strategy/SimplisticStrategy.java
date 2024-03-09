package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a simplistic strategy in playing Reversi that will just go after
 * the coordinates which can earn the player the most points in score.
 */
public class SimplisticStrategy implements Strategy {
  @Override
  public Optional<Coordinates> chooseMove(ReversiModelReadOnly model, Player player) {
    List<Coordinates> pickedList = new ArrayList<>(
                                                    new CaptureMost().chooseMove(model, player));
    return Coordinates.upperLeftMost(pickedList, model.getBoardSideLength());
  }
}
