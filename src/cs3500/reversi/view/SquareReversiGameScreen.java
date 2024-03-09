package cs3500.reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Represents the game screen of a Square Reversi game which shows the board, discs, and
 * highlight the cell when player click on a particular cell (click again or click outside the
 * game board to de-highlight). The player can press return button to place a disc on a selected
 * cell, or press space to pass this round of the game.
 */
public class SquareReversiGameScreen extends AbstractGameScreen {
  private final int cellSize;

  /**
   * Constructs a square reversi game screen that show the current game state
   * of the given model.
   *
   * @param model the model that have the information about current game state
   */
  public SquareReversiGameScreen(ReversiModelReadOnly model) {
    super(model);
    this.cellSize = 60;
  }

  @Override
  public void toggleMustPassState(boolean mustPass) {
    if (mustPass) {
      showMessageDialog(this, "You must pass!");
    }
  }

  @Override
  protected Dimension getGameBoardSize() {
    // the total width and height of the screen should be equal to
    // the number of cells in the row and column
    // (regular board have same number of row and column)
    // multiplied by the size of a single cell
    int size = this.model.getBoardSideLength() * this.cellSize;
    return new Dimension(size, size);
  }

  @Override
  public Dimension getPreferredSize() {
    int preferredWidth = this.model.getBoardSideLength() * this.cellSize;
    int preferredHeight = this.model.getBoardSideLength() * this.cellSize;
    return new Dimension(preferredWidth, preferredHeight);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    transformScreen(g2d);
    updateCurrentBoard();
    drawCells(g2d);
    drawSelected(g2d);
    drawDiscs(g2d);
  }

  // updates the current board state
  private void updateCurrentBoard() {
    List<List<Cell>> board = this.model.getBoard();
    Map<Shape, Coordinates> squareBoard = new HashMap<>();
    Map<Shape, Color> discsOnBoard = new HashMap<>();
    int boardSideLength = this.model.getBoardSideLength();

    for (int rowNum = 0; rowNum < boardSideLength; rowNum++) {
      List<Cell> row = board.get(rowNum);
      double currentRowYLocation = (rowNum + 0.5) * this.cellSize;

      for (int colNum = 0; colNum < boardSideLength; colNum++) {
        Cell currentCell = row.get(colNum);
        double currentCellXLocation = (colNum + 0.5) * this.cellSize;
        recordCellShapes(squareBoard, currentCell, currentCellXLocation, currentRowYLocation);
        recordDiscOnCell(discsOnBoard, currentCell, currentCellXLocation, currentRowYLocation);
      }
    }
    // only when the cells in board haven't been initialized yet will we need to change this.cells
    if (this.cells.isEmpty()) {
      this.cells = Optional.of(squareBoard);
    }
    this.discs = discsOnBoard;
  }

  // record the shape of the given cell
  private void recordCellShapes(Map<Shape, Coordinates> hexagonalBoard, Cell currentCell,
                                double centerX, double centerY) {
    // only when the cells in board haven't been initialized yet will we need to create them
    if (this.cells.isEmpty()) {
      Shape squareCell = this.createSquareCell(centerX, centerY, this.cellSize);
      hexagonalBoard.put(squareCell, currentCell.getCoordinates());
    }
  }

  // create the square cell shape center in the given x and y location
  // with the given size
  private Shape createSquareCell(double centerX, double centerY, double size) {
    double halfWidth = size / 2.0;

    Path2D.Double square = new Path2D.Double();
    // move to the top left point of this square
    square.moveTo(centerX - halfWidth, centerY - halfWidth);
    // draw a line to the top right point
    square.lineTo(centerX + halfWidth, centerY - halfWidth);
    // draw a line to the bottom right point
    square.lineTo(centerX + halfWidth, centerY + halfWidth);
    // draw a line to the bottom left point
    square.lineTo(centerX - halfWidth, centerY + halfWidth);
    // draw a line back to the top left point of this square
    square.lineTo(centerX - halfWidth, centerY - halfWidth);

    return square;
  }
}