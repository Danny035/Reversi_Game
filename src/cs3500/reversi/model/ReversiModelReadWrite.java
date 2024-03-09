package cs3500.reversi.model;

import cs3500.reversi.player.Player;

/**
 * Represents a read and write model interface for a Reversi game that
 * provides both observation and modification methods in the game.
 */
public interface ReversiModelReadWrite extends ReversiModelReadOnly {

  /**
   * The given player choose to pass the given round.
   * @param player the player who choose to pass the given round
   * @throws IllegalStateException if the game is over or
   *                              if this is not the given player's turn to play.
   */
  void pass(Player player);

  /**
   * The given player try to place a disc in the cell of the given coordinates.
   * @param player the player who
   * @param coordinates the coordinates of the cell that
   *                    the player want to place a disc in
   * @throws IllegalArgumentException if the space is occupied or the move can't earn any points
   * @throws IllegalStateException if the game is over or
   *                               if this is not the given player's turn to play
   */
  void placeIn(Player player, Coordinates coordinates);

  /**
   * Starts this Reversi game, so that the players can start playing.
   */
  void startGame();

  /**
   * Add the given Features object as a listener that will get notified
   * of the turn (active player who should be making the next move) of the game
   * when game starts and every time the turn changed, and also whether a player is stuck
   * with no possible move.
   * @param listener the Features listener to receive notification
   */
  void addFeatures(ModelFeatures listener);
}
