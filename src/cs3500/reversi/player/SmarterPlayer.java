package cs3500.reversi.player;

import java.util.Optional;

import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;
import cs3500.reversi.strategy.SmarterStrategy;

/**
 * A player implementation that uses a smarter, possibly more complex algorithm for decision-making.
 * This class extends AbstractMachinePlayer and overrides the method to choose a move
 * using a smarter strategy. It represents an automated player that calculates the best move
 * based on the current state of the game model using a sophisticated approach.
 */
public class SmarterPlayer extends AbstractMachinePlayer {
  @Override
  protected Optional<Coordinates> chooseMove(ReversiModelReadOnly model) {
    return new SmarterStrategy().chooseMove(model, this);
  }
}
