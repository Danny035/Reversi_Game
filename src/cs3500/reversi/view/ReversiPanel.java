package cs3500.reversi.view;

import cs3500.reversi.player.PlayerActionFeatures;

/**
 * Represents a panel in a Reversi game window, which is the graphics or
 * a part of the graphics shown to a player.
 */
public interface ReversiPanel {
  /**
   * Adds the given features as a listener so that after a keyboard event happens in this
   * panel, this panel will translate the meaning of the keyboard event to the features listener
   * by calling its method appropriately when needed.
   * @param playerActionFeatures a features listener that listens to high-level player actions
   *                 happening in this panel
   */
  void addFeatures(PlayerActionFeatures playerActionFeatures);

  /**
   * Toggle whether the player is stuck in the state where the player have
   * no more available move thus the only choice the player can take
   * is to pass this round of the game.
   * This game screen will notify the player accordingly.
   * @param mustPass indicates whether the player is in the must pass state
   */
  void toggleMustPassState(boolean mustPass);
}
