package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;
import cs3500.reversi.player.Player;

/**
 * Represents a strategy that a machine player in a Reversi game can use
 * to choose the best move based on the Minimax strategy.
 */
public class MinimaxStrategy implements Strategy {
  @Override
  public Optional<Coordinates> chooseMove(ReversiModelReadOnly model, Player player) {
    List<Coordinates> pickedList = new ArrayList<>(
            new Minimax().chooseMove(model, player));
    return Coordinates.upperLeftMost(pickedList, model.getBoardSideLength());
  }
}
