package cs3500.reversi.model;

/**
 * Represents a cell in a game of Reversi.
 */
public interface Cell {
  /**
   * Returns whether this cell have not been occupied by a disc yet.
   * @return true if this cell is empty, false otherwise
   */
  boolean isEmpty();

  /**
   * Returns the disc on this cell if there is any,
   * if this cell is empty, then throw IllegalStateException.
   * @return the disc on this cell if there is a disc
   * @throws IllegalStateException if this cell is empty
   */
  Disc getDisc();

  /**
   * Place the given disc onto this cell if this cell is empty.
   * @param disc the disc to be placed onto this cell
   * @throws IllegalStateException if this cell is already occupied
   */
  void placeDisc(Disc disc);

  /**
   * Flip the disc on this cell to the given disc.
   * @param disc the disc to be flipped into
   * @throws IllegalStateException if this cell is empty or
   *                               is already occupied by the given disc
   */
  void flipDiscTo(Disc disc);

  /**
   * Returns the hexagonal coordinates of this cell on the game board.
   * @return the hexagonal coordinates of this cell
   */
  Coordinates getCoordinates();

  /**
   * Creates a copy of this cell with exactly the same attributes.
   * @return a copy of this cell with exactly the same attributes
   */
  Cell copy();
}
