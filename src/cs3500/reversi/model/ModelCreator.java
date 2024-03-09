package cs3500.reversi.model;

import cs3500.reversi.player.Player;

/**
 * A model factory that have the ability to construct different type of Reversi game models,
 * such as Reversi game model with hexagonal game board, or square game board.
 */
public class ModelCreator {
  /**
   * Represents a type of Reversi game, specified by the kind of shape that the game board have.
   */
  public enum GameType {
    HEX, SQUARE
  }

  /**
   * Creates a Reversi game model that is of the given type, and have a game board
   * of given board size, with the given 2 players being the players involved in the game.
   * @param type type of Reversi game specified by the board shape
   * @param boardSize board size for the Reversi game board
   * @param player1 one of the players playing in the game
   * @param player2 the other player playing in the game
   * @return
   */
  public static ReversiModelReadWrite create(GameType type, int boardSize, Player player1,
                                             Player player2) {
    if (type == GameType.SQUARE) {
      return new SquareReversi(boardSize, player1, player2);
    } else {
      return new HexReversi(boardSize, player1, player2);
    }
  }
}
