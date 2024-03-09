package cs3500.reversi.model;

/**
 * Represents a hexagonal cell in the Reversi game board,
 * that a player can place a disc onto the cell.
 */
public class ReversiCell implements Cell {
  private final Coordinates coordinates;
  private Disc discOnCell;

  /**
   * Creates a hexagonal cell with the given coordinates,
   * default to not having any disc on cell.
   * @param coordinates the coordinates of this hexagonal cell
   */
  public ReversiCell(Coordinates coordinates) {
    this(coordinates, null);
  }

  /**
   * Creates a hexagonal cell with the given coordinates
   * and given disc if any.
   * @param coordinates the coordinates of this hexagonal cell
   * @param discOnCell the disc on this hexagonal cell or null
   */
  public ReversiCell(Coordinates coordinates, Disc discOnCell) {
    this.coordinates = coordinates;
    this.discOnCell = discOnCell;
  }

  @Override
  public boolean isEmpty() {
    return this.discOnCell == null;
  }

  @Override
  public Disc getDisc() {
    if (this.isEmpty()) {
      throw new IllegalStateException("No disc on cell!");
    }
    return this.discOnCell;
  }


  @Override
  public void placeDisc(Disc disc) {
    if (!this.isEmpty()) {
      throw new IllegalStateException("This cell is already occupied");
    }
    this.discOnCell = disc;
  }

  @Override
  public void flipDiscTo(Disc disc) {
    if (this.isEmpty()) {
      throw new IllegalStateException("No disc on cell!");
    }
    if (this.discOnCell == disc) {
      throw new IllegalStateException("Wrong cell to flip! Already occupied by this disc!");
    }
    this.discOnCell = disc;
  }

  @Override
  public Coordinates getCoordinates() {
    return this.coordinates;
  }

  @Override
  public Cell copy() {
    return new ReversiCell(this.coordinates, this.discOnCell);
  }
}