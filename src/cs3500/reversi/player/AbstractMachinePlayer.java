package cs3500.reversi.player;

import java.util.Optional;

import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * An abstract representation of an automated player  in the Reversi game.
 * This class extends AbstractPlayer and provides an implementation for automated move
 * selection based on the current state of the game model.
 */
public abstract class AbstractMachinePlayer extends AbstractPlayer {

  @Override
  public void turnToMove(ReversiModelReadOnly model) {
    Optional<Coordinates> choice = this.chooseMove(model);
    if (choice.isEmpty()) {
      this.pass();
    } else {
      this.placeIn(choice.get());
    }
  }

  /**
   * Abstract method to be implemented by subclasses to determine the next move
   * of the automated player. This method should analyze the current state of the game provided
   * by the model and return the chosen coordinates for the next move. If no valid move is
   * possible, it should return an empty.
   * @param model the read-only game model used to analyze the game state and determine
   *              the next move.
   * @return an {@link Optional} containing the chosen hexagonal coordinates for the next move,
   *         or an empty {@link Optional} if no valid move is available.
   */
  protected abstract Optional<Coordinates> chooseMove(ReversiModelReadOnly model);
}
