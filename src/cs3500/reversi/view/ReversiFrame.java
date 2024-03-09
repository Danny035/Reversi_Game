package cs3500.reversi.view;

import cs3500.reversi.player.PlayerActionFeatures;

/**
 * Represents a Reversi game window, which includes the game screen
 * showing the current game state.
 */
public interface ReversiFrame {

  /**
   * Adds the given features as a listener so that after a keyboard event happens in panels of
   * this window, this panel will translate the meaning of the keyboard event to the features
   * listener by calling its method appropriately when needed.
   * @param playerActionFeatures a features listener that listens to high-level player actions
   *                 happening in this window
   */
  void addFeatures(PlayerActionFeatures playerActionFeatures);

  /**
   * Refresh the current view to make sure that the window is accurately displaying the
   * latest update of the board.
   */
  void refresh();

  /**
   * Pop up a message dialog to notify the player.
   * @param message the message of notification
   */
  void showMessage(String message);

  /**
   * Toggle whether the player is stuck in the state where the player have
   * no more available move thus the only choice the player can take
   * is to pass this round of the game.
   * The user interface will notify the player accordingly.
   * @param mustPass indicates whether the player is in the must pass state
   */
  void toggleMustPassState(boolean mustPass);

  /**
   * Sets the game state description that will show on the top of the game screen.
   * @param description the game state description text
   */
  void setGameStateDescription(String description);
}
