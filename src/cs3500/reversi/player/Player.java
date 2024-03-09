package cs3500.reversi.player;

import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a player in a reversi game, that can click on a cell to get the potential score
 * that can be earned by placing a disc into the cell, and actually place a disc into a cell in
 * the game board, and choose to pass this round of the game.
 */
public interface Player {
  /**
   * Indicates that the turn for this player have now come so this player now have the ability
   * to make an action, either place a disc or pass this round.
   * @param model the model that is sending out the notification that this player
   *              should make a move
   */
  void turnToMove(ReversiModelReadOnly model);

  /**
   * Try to place this player's disc onto the cell in the game board
   * represented by the given row number and the location of the cell
   * in the row.
   * @param coordinates the coordinates of the cell that this player
   *                    would like to place a disc on
   */
  void placeIn(Coordinates coordinates);

  /**
   * The player choose to pass this round of the game.
   */
  void pass();

  /**
   * Adds the given features as a listener so that this player make a move (either clickOn to
   * place a disc onto a cell or pass to pass this round), the listeners will get notified of
   * such an action have happened.
   * @param playerActionFeatures a features listener that listens to high-level player actions
   *                 happening in this window
   */
  void addFeatures(PlayerActionFeatures playerActionFeatures);
}
