package cs3500.reversi.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.Disc;
import cs3500.reversi.model.ReversiModelReadOnly;
import cs3500.reversi.player.PlayerActionFeatures;

/**
 * This class implements the {@code ReversiView} interface and is responsible for rendering the
 * current state of the game board in a textual format. The board is represented as a list of rows,
 * where each row is a list of cells.
 */
public class ReversiTextualView implements ReversiView, ReversiFrame {
  private final ReversiModelReadOnly model;
  private final Appendable out;
  private final List<PlayerActionFeatures> listeners;

  /**
   * Initializes a new instance of the View.
   * @param model The model to be rendered.
   * @param out the output source to append the textual view
   */
  public ReversiTextualView(ReversiModelReadOnly model, Appendable out) {
    this.model = model;
    this.out = out;
    this.listeners = new ArrayList<>();
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
    int boardSize = this.model.getBoardSideLength();
    for (int rowNum = 0; rowNum < board.size(); rowNum++) {
      List<Cell> row = board.get(rowNum);

      // if this is not the first row of cells in the board,
      // then append a new line to it to separate it with the row in front of it
      if (rowNum != 0) {
        builder.append("\n");
      }

      // append empty spaces in front of the row when the row is not the widest row
      builder.append(" ".repeat(Math.abs(rowNum - (boardSize - 1))));

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

  @Override
  public void addFeatures(PlayerActionFeatures playerActionFeatures) {
    this.listeners.add(playerActionFeatures);
  }

  @Override
  public void refresh() {
    // add a delimiter
    this.write("--------------");
    this.endOutput();
    this.render();
    this.endOutput();
  }

  @Override
  public void showMessage(String message) {
    this.write(message);
  }

  @Override
  public void toggleMustPassState(boolean mustPass) {
    if (mustPass) {
      this.write("You must pass!");
    }
  }

  @Override
  public void setGameStateDescription(String description) {
    this.write(description);
    this.endOutput();
  }

  // write the given string to be show in the textual view
  private void write(String str) {
    try {
      this.out.append(str);
    } catch (IOException e) {
      throw new IllegalStateException("Error in showing the view!");
    }
  }

  // ends an output by adding a new line character
  private void endOutput() {
    this.write("\n");
  }
}
