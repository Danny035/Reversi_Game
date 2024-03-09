package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import cs3500.reversi.utils.Utils;

/**
 * Represents a coordinates system in a rectangular board, specified by the row and column
 * that it locates in the board, with origin (0, 0) in the upper left corner of the board,
 * and each coordinate have 8 coordinates around it (above, below, left, right, and 4 diagonals)
 * as its neighbors.
 * An example row column coordinates (row, col) of cells in a square grid with side length of 3:
 * <pre>
 *   (0, 0)  (0, 1)  (0, 2)
 *   (1, 0)  (1, 1)  (1, 2)
 *   (2, 0)  (2, 1)  (2, 2)
 * </pre>
 * Note that all coordinates, except (1, 1) itself, in the above example is a neighbor to (1, 1).
 */
public class RowColumnCoordinates implements Coordinates {
  int row;
  int col;

  /**
   * Constructs a row-column coordinates with the given row and column number.
   * @param row the row that this coordinates locates in
   * @param col the column that this coordinates locates in
   */
  public RowColumnCoordinates(int row, int col) {
    this.row = row;
    this.col = col;
  }

  // returns a coordinates that represent the neighbor above this coordinates.
  private Coordinates getNeighborAbove() {
    return new RowColumnCoordinates(this.row - 1, this.col);
  }

  // returns a coordinates that represent the neighbor below this coordinates.
  private Coordinates getNeighborBelow() {
    return new RowColumnCoordinates(this.row + 1, this.col);
  }

  // returns a coordinates that represent the neighbor to the left of this coordinates.
  private Coordinates getLeftNeighbor() {
    return new RowColumnCoordinates(this.row, this.col - 1);
  }

  // returns a coordinates that represent the neighbor to the right of this coordinates.
  private Coordinates getRightNeighbor() {
    return new RowColumnCoordinates(this.row, this.col + 1);
  }

  // returns a coordinates that represent the upper left diagonal neighbor of this coordinates.
  private Coordinates getUpperLeftNeighbor() {
    return new RowColumnCoordinates(this.row - 1, this.col - 1);
  }

  // returns a coordinates that represent the lower left diagonal neighbor of this coordinates.
  private Coordinates getLowerLeftNeighbor() {
    return new RowColumnCoordinates(this.row + 1, this.col - 1);
  }

  // returns a coordinates that represent the upper left diagonal neighbor of this coordinates.
  private Coordinates getUpperRightNeighbor() {
    return new RowColumnCoordinates(this.row - 1, this.col + 1);
  }

  // returns a coordinates that represent the lower right diagonal neighbor of this coordinates.
  private Coordinates getLowerRightNeighbor() {
    return new RowColumnCoordinates(this.row + 1, this.col + 1);
  }

  @Override
  public List<Coordinates> getAllNeighbors() {
    return new ArrayList<>(Arrays.asList(
            this.getNeighborAbove(),
            this.getNeighborBelow(),
            this.getLeftNeighbor(),
            this.getRightNeighbor(),
            this.getUpperLeftNeighbor(),
            this.getLowerLeftNeighbor(),
            this.getUpperRightNeighbor(),
            this.getLowerRightNeighbor()
    ));
  }

  @Override
  public List<Function<Coordinates, Coordinates>> getNeighborFunctions() {
    List<Function<Coordinates, Coordinates>> functions = new ArrayList<>();
    // a row-column coordinates in a square game board have 8 neighbors
    for (int i = 0; i < 8; i++) {
      final int idx = i;
      functions.add((coords -> coords.getAllNeighbors().get(idx)));
    }
    return functions;
  }

  @Override
  public int getRow(int boardSideLength) {
    return this.row;
  }

  @Override
  public int getLocationInRow(int boardSideLength) {
    return this.col;
  }

  @Override
  public boolean validCoordsOnBoard(int boardSideLength) {
    return Utils.checkValuesInBetween(this.row, 0, boardSideLength - 1)
        && Utils.checkValuesInBetween(this.col, 0, boardSideLength - 1);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof RowColumnCoordinates) {
      RowColumnCoordinates that = (RowColumnCoordinates) obj;
      return this.row == that.row && this.col == that.col;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.row, this.col);
  }
}
