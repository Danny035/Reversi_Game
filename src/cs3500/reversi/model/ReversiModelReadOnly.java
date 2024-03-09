package cs3500.reversi.model;

import java.util.List;


import cs3500.reversi.player.Player;

/**
 * Represents a read only model interface for a Reversi game that
 * only provides the observation methods in the game.
 */
public interface ReversiModelReadOnly {
  /**
   * Returns the side length of the game board.
   * A reversi game board is a regular grid of cells,
   * meaning that all sides have the same length.
   * @return the side length of the game board
   */
  int getBoardSideLength();

  /**
   * Returns the current board state of the game.
   * @return the list of cells on board
   */
  List<List<Cell>> getBoard();

  /**
   * Calculates and returns the score that the current player can earn
   * by placing a disc in the given coordinates in this round of the game.
   * A score of 0 means that this is an illegal move.
   * @param player the player
   * @param coordinates coordinates of a cell on the game board
   * @return score that the current player can earn by placing a disc
   *         in the given coordinates in this round of the game
   * @throws IllegalStateException if the game is over
   */
  int scoreEarnedByPlaceIn(Player player, Coordinates coordinates);

  /**
   * Determines if the given player have any legal move that can be made
   * for this round of the game, if not, meaning the player must pass.
   * @param player the player to be determined whether is movable
   * @return true if the given player can make a legal move, and
   *         false if this is not the given player's turn to place a disc, or
   *         there is no legal move that the player can make
   * @throws IllegalStateException if the game is over
   */
  boolean movable(Player player);

  /**
   * Returns the current score of the given player.
   * @param player the player to check score
   * @return the current score of the given player
   */
  int getScore(Player player);

  /**
   * Determines if the game is over.
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Returns the player who get the current turn.
   * @return the player that should be placing a disc or passing for the current turn
   * @throws IllegalStateException if the game is over
   */
  Player getTurn();

  /**
   * Gets the cell at the given location on board.
   * @param coordinates the coordinates indicating the location of the cell
   * @return the cell at the given location on board if there indeed is one
   * @throws IllegalArgumentException if the given coordinates is not a valid location on board
   *                                  (i.e. there is no cell at that location)
   */
  Cell getCellInBoard(Coordinates coordinates);

  /**
   * Returns a newly created Reversi model representing the game state after trying to
   * place a disc of the given player's at the given coordinates.
   * Notice that this method doesn't change this game.
   * @param player the player who tries placing a disc
   * @param coordinates the coordinates that the player is trying ti place a disc on
   * @return a newly created Reversi model representing the game state after trying the move
   * @throws IllegalArgumentException if the space is occupied or the move can't earn any points
   * @throws IllegalStateException if the game is over or
   *                               if this is not the given player's turn to play
   */
  ReversiModelReadOnly tryMove(Player player, Coordinates coordinates)
                                            throws IllegalArgumentException, IllegalStateException;

  /**
   * Returns the disc of the given player if the player involves in this game.
   * @param player the player to get the disc of
   * @return the disc of the given player if the player involves in this game.
   * @throws IllegalArgumentException if the player does not involve in this game.
   */
  Disc getDiscOf(Player player) throws IllegalArgumentException;

  /**
   * Returns a list of all the corners in the game board.
   * @return a list of all the corners in the game board.
   */
  List<Coordinates> getCorners();
}
