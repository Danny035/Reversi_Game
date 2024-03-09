package cs3500.reversi.player;

import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a human player that will interact with the game through game window.
 */
public class HumanPlayer extends AbstractPlayer {
  @Override
  public void turnToMove(ReversiModelReadOnly model) {
    // intentionally left blank because human player should interact with the view
    // to react to the notification that it's their turn to move
  }
}
