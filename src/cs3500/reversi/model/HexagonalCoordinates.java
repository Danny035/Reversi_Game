package cs3500.reversi.model;

/**
 * Represents a coordinats system for a hexagonal game board, with the base axis of q, r, s.
 */
public interface HexagonalCoordinates extends Coordinates {
  /**
   * Returns the location in the Q axis.
   * @return the Q in cube coordinates
   */
  int getQ();

  /**
   * Returns the location in the R axis.
   * @return the R in cube coordinates
   */
  int getR();

  /**
   * Returns the location in the S axis.
   * @return the S in cube coordinates
   */
  int getS();
}
