package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cs3500.reversi.player.Player;

/**
 * Represents a 2-player Reversi game with a square grid,
 * where discs can be flipped along 8 directions.
 */
public class SquareReversi extends AbstractReversi {
  /**
   * Creates and initializes this reversi game with the given board size
   * and the given 2 players.
   * @param boardSize the size of the game board
   * @param player1 one of the player in this game
   * @param player2 the other player in this game
   * @throws IllegalArgumentException if the given board size is illegal
   *                                  (less than 3)
   */
  public SquareReversi(int boardSize, Player player1, Player player2) {
    super(boardSize, player1, player2);
  }

  /**
   * A cloning constructor that will create a new square reversi model exactly the same
   * as the square hex reversi model.
   * @param that the hex reversi model to make a copy of
   */
  public SquareReversi(SquareReversi that) {
    super(that);
  }

  @Override
  protected void validateBoardSize(int boardSize) {
    if (boardSize <= 2 || boardSize % 2 != 0) {
      throw new IllegalArgumentException("Board size must be a positive even number "
              + "greater than 2!");
    }
  }

  @Override
  protected List<List<Cell>> initializeBoard() {
    List<List<Cell>> gameBoard = new ArrayList<>();
    for (int rowNum = 0; rowNum < this.boardSize; rowNum++) {
      List<Cell> row = new ArrayList<>();
      for (int colNum = 0; colNum < this.boardSize; colNum++) {
        Coordinates coordinates = new RowColumnCoordinates(rowNum, colNum);
        ReversiCell reversiCell = new ReversiCell(coordinates);
        row.add(reversiCell);
      }
      gameBoard.add(Collections.unmodifiableList(row));
    }
    return Collections.unmodifiableList(gameBoard);
  }

  @Override
  protected void placeInitialDiscs() {
    int mid = this.boardSize / 2;
    for (List<Cell> row : this.board) {
      for (Cell cell : row) {
        if (cell.getCoordinates().equals(new RowColumnCoordinates(mid - 1, mid))
            || cell.getCoordinates().equals(new RowColumnCoordinates(mid, mid - 1))) {
          cell.placeDisc(Disc.WHITE);
        }
        else if (cell.getCoordinates().equals(new RowColumnCoordinates(mid - 1, mid - 1))
                || cell.getCoordinates().equals(new RowColumnCoordinates(mid, mid))) {
          cell.placeDisc(Disc.BLACK);
        }
      }
    }
  }

  @Override
  protected ReversiModelReadWrite copyModel() {
    return new SquareReversi(this);
  }

  @Override
  public List<Coordinates> getCorners() {
    // get the top and bottom rows because the 4 corners on the game board
    // are from those 2 rows
    List<Cell> topRow = board.get(0);
    List<Cell> bottomRow = board.get(board.size() - 1);
    // get the first and last cell in the top and bottom rows because they are the corners of
    //  the game board, and put their coordinates into an unmodifiable list
    List<Coordinates> allCorners = new ArrayList<>(Arrays.asList(
            topRow.get(0).getCoordinates(),
            topRow.get(topRow.size() - 1).getCoordinates(),
            bottomRow.get(0).getCoordinates(),
            bottomRow.get(bottomRow.size() - 1).getCoordinates()));
    return Collections.unmodifiableList(allCorners);
  }
}
