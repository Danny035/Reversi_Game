package cs3500.reversi.player;

import java.util.Optional;

import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;
import cs3500.reversi.strategy.MinimaxStrategy;

/**
 * A player implementation that uses the Minimax algorithm for decision-making.
 * This class extends AbstractMachinePlayer and overrides the method to choose a move
 * using the Minimax strategy. It represents an automated player that calculates the best move
 * based on the current state of the game model.
 */
public class MinimaxPlayer extends AbstractMachinePlayer {
  @Override
  protected Optional<Coordinates> chooseMove(ReversiModelReadOnly model) {
    return new MinimaxStrategy().chooseMove(model, this);
  }
}
