package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cs3500.reversi.player.Player;

/**
 * Represent a 2-player Reversi game with a hexagonal grid,
 * where discs can be flipped along 6 directions.
 */
public class HexReversi extends AbstractReversi {
  /**
   * Creates and initializes this reversi game with the given board size
   * and the given 2 players.
   * @param boardSize the size of the game board
   * @param player1 one of the player in this game
   * @param player2 the other player in this game
   * @throws IllegalArgumentException if the given board size is illegal
   *                                  (less than 3)
   */
  public HexReversi(int boardSize, Player player1, Player player2) {
    super(boardSize, player1, player2);
  }

  @Override
  protected void validateBoardSize(int boardSize) {
    if (boardSize < 3) {
      throw new IllegalArgumentException("Board size must be at least 3!");
    }
  }

  /**
   * a cloning constructor that will create a new hex reversi model exactly the same
   * as the given hex reversi model.
   * @param reversi the hex reversi model to make a copy of
   */
  private HexReversi(HexReversi reversi) {
    super(reversi);
  }

  @Override
  // return an initialized game board.
  protected List<List<Cell>> initializeBoard() {
    int maxCoordinateValue = this.boardSize - 1;
    List<List<Cell>> gameBoard = new ArrayList<>();
    for (int r = -maxCoordinateValue; r <= maxCoordinateValue; r++) {
      List<Cell> row = new ArrayList<>();
      for (int q = -maxCoordinateValue; q <= maxCoordinateValue; q++) {
        int s = -q - r;
        if (s >= -maxCoordinateValue && s <= maxCoordinateValue) {
          HexagonalCoordinates hexagonalCoordinates = new CubeCoordinates(q, r);
          ReversiCell reversiCell = new ReversiCell(hexagonalCoordinates);
          row.add(reversiCell);
        }
      }
      gameBoard.add(Collections.unmodifiableList(row));
    }
    return Collections.unmodifiableList(gameBoard);
  }

  @Override
  // place the initial 6 discs on the board
  protected void placeInitialDiscs() {
    for (List<Cell> row : this.board) {
      for (Cell cell : row) {
        if (cell.getCoordinates().equals(new CubeCoordinates(1, -1))
            || cell.getCoordinates().equals(new CubeCoordinates(-1, 0))
            || cell.getCoordinates().equals(new CubeCoordinates(0, 1))) {
          cell.placeDisc(Disc.WHITE);
        }
        else if (cell.getCoordinates().equals(new CubeCoordinates(0, -1))
                 || cell.getCoordinates().equals(new CubeCoordinates(1, 0))
                 || cell.getCoordinates().equals(new CubeCoordinates(-1, 1))) {
          cell.placeDisc(Disc.BLACK);
        }
      }
    }
  }

  @Override
  protected ReversiModelReadWrite copyModel() {
    return new HexReversi(this);
  }

  @Override
  public List<Coordinates> getCorners() {
    // get the top, middle, and bottom rows because the 6 corners on the game board
    // are from those rows
    List<Cell> topRow = board.get(0);
    List<Cell> middleRow = board.get((board.size() - 1) / 2);
    List<Cell> bottomRow = board.get(board.size() - 1);
    // get the first and last cell in the top, middle, and bottom rows because
    // they are the corners of the game board, and put their coordinates into the list
    List<Coordinates> allCorners = new ArrayList<>(Arrays.asList(
            topRow.get(0).getCoordinates(),
            topRow.get(topRow.size() - 1).getCoordinates(),
            middleRow.get(0).getCoordinates(),
            middleRow.get(middleRow.size() - 1).getCoordinates(),
            bottomRow.get(0).getCoordinates(),
            bottomRow.get(bottomRow.size() - 1).getCoordinates()));
    return Collections.unmodifiableList(allCorners);
  }
}
