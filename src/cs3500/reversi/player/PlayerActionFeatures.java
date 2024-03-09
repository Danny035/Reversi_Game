package cs3500.reversi.player;

import cs3500.reversi.model.Coordinates;

/**
 * Represents a features listener that listens to high-order player action taken
 * in the view of a Reversi game.
 */
public interface PlayerActionFeatures {
  /**
   * The player whom this feature is focusing on choose to pass the current round.
   */
  void pass();

  /**
   * The player whom this feature is focusing on choose to place a disc on the given coordinates.
   * @param coordinates the coordinates that the player would like to place a disc on
   */
  void placeIn(Coordinates coordinates);

  /**
   * The player whom this feature is focusing on choose selected the cell located in
   * the given coordinates.
   * @param coordinates the coordinates that the player selected
   * @return the potential score that the player can get by placing a disc in this coordinates
   */
  int select(Coordinates coordinates);
}
