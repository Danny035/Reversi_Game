package cs3500.reversi.player;

import java.util.Optional;

import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;
import cs3500.reversi.strategy.SimplisticStrategy;

/**
 * Represents a machine player in a reversi game which uses Simplistic Strategy.
 */
public class SimplisticPlayer extends AbstractMachinePlayer {
  @Override
  protected Optional<Coordinates> chooseMove(ReversiModelReadOnly model) {
    return new SimplisticStrategy().chooseMove(model, this);
  }
}
