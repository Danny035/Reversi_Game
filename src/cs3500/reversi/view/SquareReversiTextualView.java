package cs3500.reversi.view;

import java.io.IOException;
import java.util.List;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.Disc;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a textual view of a square Reversi where the board is in a square shape.
 * _ represents an empty space, X is a black disc, and O is a white disc.
 */
public class SquareReversiTextualView implements ReversiView {
  private final ReversiModelReadOnly model;
  private final Appendable out;

  /**
   * Initializes a new instance of the textual View of a square Reversi game.
   * @param model The model to be rendered.
   * @param out the output source to append the textual view
   */
  public SquareReversiTextualView(ReversiModelReadOnly model, Appendable out) {
    this.model = model;
    this.out = out;
  }

  @Override
  public void render() {
    try {
      this.out.append(this.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Error in showing the game board!");
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    List<List<Cell>> board = this.model.getBoard();
    this.boardToString(board, builder);
    return builder.toString();
  }

  // append a textual representation of the given board to the given StringBuilder
  private void boardToString(List<List<Cell>> board, StringBuilder builder) {
    for (int rowNum = 0; rowNum < board.size(); rowNum++) {
      List<Cell> row = board.get(rowNum);
      // if this is not the first row of cells in the board,
      // then append a new line to it to separate it with the row in front of it
      if (rowNum != 0) {
        builder.append("\n");
      }
      this.rowToString(row, builder);
    }
  }

  // append a textual representation of the given row to the given StringBuilder
  private void rowToString(List<Cell> row, StringBuilder builder) {
    int numCellsInRow = row.size();
    for (int cellNum = 0; cellNum < numCellsInRow; cellNum++) {
      Cell cell = row.get(cellNum);
      // if this is not the first cell in the current row,
      // then append a space before it to separate it with the cell in front of it
      if (cellNum != 0) {
        builder.append(" ");
      }
      this.cellToString(cell, builder);
    }
  }

  // append the textual representation for the given cell to the given StringBuilder,
  // with _ represents an empty cell,
  // X represents a cell with a Black disc,
  // O represents a cell with a White disc,
  private void cellToString(Cell cell, StringBuilder builder) {
    if (cell.isEmpty()) {
      builder.append("_");
    } else if (cell.getDisc() == Disc.BLACK) {
      builder.append("X");
    } else if (cell.getDisc() == Disc.WHITE) {
      builder.append("O");
    }
  }
}
