package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import cs3500.reversi.utils.Utils;


/**
 * Represents a cube coordinates for representing cell location in a hexagonal grid,
 * with origin (0, 0, 0) in the middle of the hexagonal grid, and each coordinate have
 * 6 neighbors: above, below, upper and lower right, upper and lower left.
 * An example cube coordinates (q, r, s) of cells in a hexagonal grid with side length of 3:
 * <pre>
 *             (0, -2, 2)   (1, -2, 1)   (2, -2, 0)
 *
 *      (-1, -1, 2)   (0, -1, 1)   (1, -1, 0)   (2, -1, -1)
 *
 * (-2, 0, 2)   (-1, 0, 1)   (0, 0, 0)   (1, 0, -1)   (2, 0, -21)
 *
 *      (-2, 1, 1)   (-1, 1, 0)   (0, 1, -0)   (1, 1, -2)
 *
 *             (-2, 2, 0)   (-1, 2, -1)   (0, 2, -2)
 * </pre>
 * Note that (0, -1, 1), (1, -1, 0), (-1, 0, 1), (1, 0, -1), (-1, 1, 0), (0, 1, -0)
 * are neighbors to the origin (0, 0, 0).
 * More information can be found on:
 * https://www.redblobgames.com/grids/hexagons
 */
public class CubeCoordinates implements HexagonalCoordinates {
  private final int q;
  private final int r;
  private final int s;

  /**
   * Creates a cube coordinates using just the location in
   * Q axis and R axis, because location in S axis can be calculated
   * from Q and R.
   * @param q the location of the coordinate in Q axis
   * @param r the location of the coordinate in R axis
   */
  public CubeCoordinates(int q, int r) {
    this.q = q;
    this.r = r;
    //Ensure q+r+s always == 0
    this.s = - q - r;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof CubeCoordinates) {
      CubeCoordinates that = (CubeCoordinates) obj;
      return this.q == that.q && this.r == that.r && this.s == that.s;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.q, this.r, this.s);
  }

  @Override
  public String toString() {
    return String.format("Cube Coordinates: %d, %d, %d", this.q, this.r, this.s);
  }

  @Override
  public int getQ() {
    return this.q;
  }

  @Override
  public int getR() {
    return this.r;
  }

  @Override
  public int getS() {
    return this.s;
  }

  @Override
  public boolean validCoordsOnBoard(int boardSideLength) {
    // Q, R, S values should all be >= -(boardSideLength - 1) and <= boardSideLength - 1,
    // to be a valid coordinates on a board with given side length
    int maxValue = boardSideLength - 1;
    return Utils.checkValuesInBetween(this.q, -maxValue, maxValue)
        && Utils.checkValuesInBetween(this.r, -maxValue, maxValue)
        && Utils.checkValuesInBetween(this.s, -maxValue, maxValue);
  }

  private Coordinates getUpperLeftNeighbor() {
    return new CubeCoordinates(q, r - 1);
  }

  private Coordinates getUpperRightNeighbor() {
    return new CubeCoordinates(q + 1, r - 1);
  }

  private Coordinates getLeftNeighbor() {
    return new CubeCoordinates(q - 1, r);
  }

  private Coordinates getRightNeighbor() {
    return new CubeCoordinates(q + 1, r);
  }

  private Coordinates getLowerLeftNeighbor() {
    return new CubeCoordinates(q - 1, r + 1);
  }

  private Coordinates getLowerRightNeighbor() {
    return new CubeCoordinates(q, r + 1);
  }

  @Override
  public List<Coordinates> getAllNeighbors() {
    return new ArrayList<>(Arrays.asList(this.getUpperLeftNeighbor(),
                                         this.getUpperRightNeighbor(),
                                         this.getLeftNeighbor(),
                                         this.getRightNeighbor(),
                                         this.getLowerLeftNeighbor(),
                                         this.getLowerRightNeighbor()));
  }

  @Override
  public List<Function<Coordinates, Coordinates>> getNeighborFunctions() {
    List<Function<Coordinates, Coordinates>> functions = new ArrayList<>();
    // a hexagonal coordinates have 6 neighbors
    for (int i = 0; i < 6; i++) {
      final int idx = i;
      functions.add((coords -> coords.getAllNeighbors().get(idx)));
    }
    return functions;
  }

  @Override
  public int getRow(int boardSideLength) {
    return this.r + boardSideLength - 1;
  }

  @Override
  public int getLocationInRow(int boardSideLength) {
    if (this.r < 0) {
      return this.q + boardSideLength - 1 + this.r;
    } else {
      return this.q + boardSideLength - 1;
    }
  }
}
