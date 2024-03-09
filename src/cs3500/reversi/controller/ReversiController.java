package cs3500.reversi.controller;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;

/**
 * Represents a controller for a Reversi game that is responsible for listening to the model
 * and notify the view and also listen to the view and make change to the model.
 */
public interface ReversiController {
  /**
   * Player that this controller is acting on behalf of choose to pass the current round.
   */
  void pass();

  /**
   * Player that this controller is acting on behalf of choose to place a disc in the
   * given coordinates.
   * @param coordinates the coordinates that the player would like to place a disc on
   */
  void placeIn(Coordinates coordinates);

  /**
   * Player that this controller is acting on behalf of select the cell on the given coordinates.
   * @param coordinates the coordinates of the cell that the player selected
   * @return the possible score that can be earned of this player choose to place a disc
   *         on that coordinates
   */
  int select(Coordinates coordinates);

  /**
   * Receive notification that there is a new turn in the game,
   * and the active player who should be playing a move this round is the given player.
   * If the given player is the player that this controller is representing,
   * then notify the player to make a move.
   * @param player the active player who should be playing a move this round
   */
  void newTurn(Player player);

  /**
   * Receive notification that the given player is stuck with
   * no possible move so the player must pass this round of the game.
   * If the given player is the player that this controller is representing,
   * then notify the view to alert the player that the player is no movable.
   * @param player the player that does not have any possible move
   *               so must pass this round of the game
   */
  void mustPass(Player player);

  /**
   * Receive notification that the game have ended.
   */
  void gameEnds();
}
