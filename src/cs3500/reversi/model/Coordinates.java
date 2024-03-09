package cs3500.reversi.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Coordinates which represents a location on a hexagonal grid.
 */
public interface Coordinates {
  /**
   * Returns a list of coordinates that represents the locations on a hexagonal grid
   * that are neighbors to this coordinates (sharing a side).
   * @return a list of neighbor coordinates of this coordinates
   */
  List<Coordinates> getAllNeighbors();

  /**
   * Returns a list of functions that takes in a Coordinates and returns a specific neighbor
   * Coordinates of the given Coordinates. The returned list contains functions to get to all
   * possible neighbors under a specific coordinate system.
   * @return a list of functions that takes in a Coordinates and returns a specific neighbor
   *         Coordinates of the given Coordinates for all moving directions based on the
   *         coordinate system.
   */
  List<Function<Coordinates, Coordinates>> getNeighborFunctions();

  /**
   * Returns row number in a regular game board specified by given side length
   * that this coordinates locates in.
   * @param boardSideLength the side length of the game board to be checked the row number
   *                        of this coordinates
   * @return the row number that this coordinates locates in
   *         (0 indicates the topmost row, 1 indicates the row below that)
   */
  int getRow(int boardSideLength);

  /**
   * Returns the location of this coordinates if it is a coordinates on a board with
   * the given board side length.
   * @param boardSideLength the side length of the game board to be checked the location
   *                        of this coordinates
   * @return the location in a row that this coordinates locates in
   *         (0 indicates the leftmost coordinates in its row, 1 indicates the coordinates
   *         right next to that).
   */
  int getLocationInRow(int boardSideLength);


  /**
   * Determines whether this coordinates is a valid coordinates on a board with the given
   * side length (assume same side length on all sides).
   * @param boardSideLength the side length of a regular board
   *                        (meaning all sides have same length)
   * @return true if this coordinates should present on a board with given side length,
   *         false if this coordinates is out of bound of the specified board.
   */
  boolean validCoordsOnBoard(int boardSideLength);

  /**
   * Choose the uppermost-leftmost coordinates in the given list of coordinates that are in
   * a board with the given side length (upper-ness comes before the left-ness).
   * @param coordinatesList the list of coordinates to choose the
   *                        uppermost-leftmost coordinates from
   * @param boardSideLength the side length of the board
   * @return an Optional HexagonalCoordinates which will be present if the list is not empty,
   *         else be empty if the given list of coordinates is empty
   */
  static Optional<Coordinates> upperLeftMost(List<Coordinates> coordinatesList,
                                             int boardSideLength) {
    if (coordinatesList.size() == 0) {
      // there is no coordinates so just return an empty optional of HexagonalCoordinates
      return Optional.empty();
    } else if (coordinatesList.size() == 1) {
      // there is only 1 coordinates in the list, there is no other coordinates to be
      // compared with, so just return it
      return Optional.of(coordinatesList.get(0));
    } else {
      Coordinates upperLeftMost = coordinatesList.get(0);
      for (Coordinates coordinates : coordinatesList.subList(1, coordinatesList.size())) {
        // if this coordinates is above the current "upperLeftMost" coordinates in record,
        // or if this coordinates is in the same top-ness level as the current
        // "upperLeftMost" coordinates in record, but is more to the left,
        // then make this coordinates to be the upperLeftMost coordinates
        if (coordinates.getRow(boardSideLength) < upperLeftMost.getRow(boardSideLength)
                || (coordinates.getRow(boardSideLength) == upperLeftMost.getRow(boardSideLength)
                && coordinates.getLocationInRow(boardSideLength)
                   < upperLeftMost.getLocationInRow(boardSideLength))) {
          upperLeftMost = coordinates;
        }
      }
      return Optional.of(upperLeftMost);
    }
  }
}
