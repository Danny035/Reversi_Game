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

/**
 * Represents the game screen of a hexagonal Reversi game which shows the board, discs, and
 * highlight the cell when player click on a particular cell (click again or click outside the
 * game board to de-highlight). The player can press return button to place a disc on a selected
 * cell, or press space to pass this round of the game.
 */
public class HexReversiGameScreen extends AbstractGameScreen {
  private final int hexagonCellCenterToTopSize;
  private final Color normalBackgroundColor;
  private final Color mustPassBackgroundColor;

  /**
   * Constructs a ReversiGameScreen with the specified game model.
   * Initializes display properties and sets up a mouse listener for user interactions.
   * @param model The readonly model of the Reversi game.
   */
  public HexReversiGameScreen(ReversiModelReadOnly model) {
    super(model);
    this.hexagonCellCenterToTopSize = 40;
    this.normalBackgroundColor = Color.WHITE;
    this.mustPassBackgroundColor = Color.ORANGE;
  }

  @Override
  public void toggleMustPassState(boolean playerMustPass) {
    this.noMoreMove = playerMustPass;
    this.repaint();
  }

  @Override
  protected Dimension getGameBoardSize() {
    // the total width of the screen should be equal to the number of cells in the widest row
    // multiplied by the width of a single cell
    int width = (int) Math.ceil((this.model.getBoardSideLength() * 2 - 1)
            * this.getHexagonWidth(this.hexagonCellCenterToTopSize));

    // the total height of the screen should be equal to the number of rows in board minus
    // the middle row (which has its full height) multiplied by 3/4 of the cell's height
    // or 3/2 of the distance from the center of a cell to its top
    int height = (int) (3.0 / 2.0 * this.hexagonCellCenterToTopSize
            * (this.model.getBoard().size() - 1) + 2 * this.hexagonCellCenterToTopSize);
    return new Dimension(width, height);
  }

  @Override
  public Dimension getPreferredSize() {
    return this.getGameBoardSize();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    drawBackground(g2d);
    transformScreen(g2d);
    updateCurrentBoard();
    drawCells(g2d);
    drawSelected(g2d);
    drawDiscs(g2d);
  }

  // draws the background of the game screen
  private void drawBackground(Graphics2D g2d) {
    if (noMoreMove) {
      g2d.setColor(this.mustPassBackgroundColor);
    } else {
      g2d.setColor(this.normalBackgroundColor);
    }
    g2d.fillRect(0, 0 , getBounds().width, getBounds().height);
  }

  // updates the current board state
  private void updateCurrentBoard() {
    List<List<Cell>> board = this.model.getBoard();
    Map<Shape, Coordinates> hexagonalBoard = new HashMap<>();
    Map<Shape, Color> discsOnBoard = new HashMap<>();
    int boardSideLength = this.model.getBoardSideLength();
    double hexagonCellWidth = this.getHexagonWidth(this.hexagonCellCenterToTopSize);

    for (int rowNum = 0; rowNum < board.size(); rowNum++) {
      double currentRowYLocation = (rowNum * 3.0 / 2.0 + 1) * this.hexagonCellCenterToTopSize;
      List<Cell> row = board.get(rowNum);
      for (int locInRow = 0; locInRow < row.size(); locInRow++) {
        Cell currentCell = row.get(locInRow);
        double currentRowXOffset = hexagonCellWidth / 2 * Math.abs(rowNum - (boardSideLength - 1));
        double currentCellXLocation = currentRowXOffset + (locInRow + 0.5) * hexagonCellWidth;

        recordCellShapes(hexagonalBoard, currentCell, currentCellXLocation, currentRowYLocation);
        recordDiscOnCell(discsOnBoard, currentCell, currentCellXLocation, currentRowYLocation);
      }
    }
    // only when the cells in board haven't been initialized yet will we need to change this.cells
    if (this.cells.isEmpty()) {
      this.cells = Optional.of(hexagonalBoard);
    }
    this.discs = discsOnBoard;
  }

  // record the shape of the given cell
  private void recordCellShapes(Map<Shape, Coordinates> hexagonalBoard, Cell currentCell,
                                double centerX, double centerY) {
    // only when the cells in board haven't been initialized yet will we need to create them
    if (this.cells.isEmpty()) {
      Shape hexagonCell = this.createPointyTopHexagon(centerX, centerY,
              this.hexagonCellCenterToTopSize);
      hexagonalBoard.put(hexagonCell, currentCell.getCoordinates());
    }
  }

  // creates a pointy top hexagon by the given center coordinates and the distance between
  // the center point and the top point
  private Shape createPointyTopHexagon(double centerX, double centerY, double centerToTop) {
    double halfWidth = this.getHexagonWidth(centerToTop) / 2;

    Path2D.Double hexagon = new Path2D.Double();
    // move to the top point of this hexagon
    hexagon.moveTo(centerX, centerY + centerToTop);
    // draw a line to the upper right point
    hexagon.lineTo(centerX + halfWidth, centerY + centerToTop / 2);
    // draw a line to the lower right point
    hexagon.lineTo(centerX + halfWidth, centerY - centerToTop / 2);
    // draw a line to the bottom point
    hexagon.lineTo(centerX, centerY - centerToTop);
    // draw a line to the lower left point
    hexagon.lineTo(centerX - halfWidth, centerY - centerToTop / 2);
    // draw a line to the upper left point
    hexagon.lineTo(centerX - halfWidth, centerY + centerToTop / 2);
    // draw a line back to the top point of this hexagon
    hexagon.lineTo(centerX, centerY + centerToTop);

    return hexagon;
  }

  // calculates and returns the width of the hexagon with the given center to top size.
  private double getHexagonWidth(double centerToTop) {
    return Math.sqrt(3) * centerToTop;
  }
}
