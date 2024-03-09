package cs3500.reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.Disc;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Mock the view of a Reversi Game for testing if logical coordinates of cell selected
 * are correct when a click happen.
 */
public class MockGameScreen extends JPanel {
  private final ReversiModelReadOnly model;
  private final Map<Disc, Color> discColorMap;
  private final int hexagonCellCenterToTopSize;
  private final int discRadius;
  // Class Invariant: selected and selectedCoordinates must be either both empty
  // or both have a value
  private Optional<Shape> selected;
  private Optional<Coordinates> selectedCoordinates;
  private Optional<Map<Shape, Coordinates>> cells;
  private Map<Shape, Color> discs;
  private double transformScale;
  private double translateX;
  private double translateY;
  private Appendable out;

  /**
   * Constructs a ReversiGameScreen with the specified game model.
   * Initializes display properties and sets up a mouse listener for user interactions.
   * @param model The readonly model of the Reversi game.
   */
  public MockGameScreen(ReversiModelReadOnly model, Appendable out) {
    this.model = model;
    Map<Disc, Color> discMappingColor = new HashMap<>();
    discMappingColor.put(Disc.BLACK, Color.BLACK);
    discMappingColor.put(Disc.WHITE, Color.WHITE);
    this.discColorMap = Collections.unmodifiableMap(discMappingColor);
    this.hexagonCellCenterToTopSize = 40;
    this.discRadius = 20;
    // class invariant that selected and selectedCoordinates must be consistent
    // is kept by this constructor
    this.selected = Optional.empty();
    this.selectedCoordinates = Optional.empty();
    this.cells = Optional.empty();
    this.discs = new HashMap<>();
    this.transformScale = 1;
    this.translateX = 0;
    this.translateY = 0;
    this.out = out;
    this.updateCurrentBoard();
  }

  @Override
  public Dimension getPreferredSize() {
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
        double currentRowXOffset = hexagonCellWidth / 2 * Math.abs(rowNum - (boardSideLength - 1));
        double currentCellXLocation = currentRowXOffset + (locInRow + 0.5) * hexagonCellWidth;
        // only when the cells in board haven't been initialized yet will we need to create them
        if (this.cells.isEmpty()) {
          Shape hexagonCell = this.createPointyTopHexagon(currentCellXLocation, currentRowYLocation,
                  this.hexagonCellCenterToTopSize);
          hexagonalBoard.put(hexagonCell, row.get(locInRow).getCoordinates());
        }

        Cell cell = row.get(locInRow);

        if (!cell.isEmpty() && this.discColorMap.containsKey(cell.getDisc())) {
          Color discColor = this.discColorMap.get(cell.getDisc());
          Ellipse2D.Double disc = new Ellipse2D.Double(currentCellXLocation - this.discRadius,
                  currentRowYLocation - this.discRadius, this.discRadius * 2, this.discRadius * 2);
          discsOnBoard.put(disc, discColor);
        }
      }
    }
    // only when the cells in board haven't been initialized yet will we need to change this.cells
    if (this.cells.isEmpty()) {
      this.cells = Optional.of(hexagonalBoard);
    }
    this.discs = discsOnBoard;
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

  /**
   * a mouse event listener class to receive and react to mouse event.
   */
  public class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      boolean cellSelected = false;
      if (cells.isPresent()) {
        Map<Shape, Coordinates> shapeCoordinatesMap = cells.get();
        for (Shape cell : shapeCoordinatesMap.keySet()) {
          if (cell.contains((e.getX() - translateX) / transformScale,
                  (e.getY() - translateY) / transformScale)) {
            // if there is no cell currently selected or the cell currently selected
            // is not this cell that this click is on, then record this cell as selected
            if (selected.isEmpty() || !selected.get().equals(cell)) {
              Coordinates selectedCoords = shapeCoordinatesMap.get(cell);
              // class invariant that selected and selectedCoordinates must be consistent
              // is kept by this method
              selected = Optional.of(cell);
              selectedCoordinates = Optional.of(selectedCoords);
              cellSelected = true;
              // append the logical coordinates of this selected cell to the given output source
              try {
                out.append(selectedCoords.toString() + "\n");
              } catch (IOException exception) {
                throw new IllegalArgumentException("Output source has a problem!");
              }
            }
          }
        }
      }
      // this can be true when the user click on the same cell that is already selected
      // to deselect it, or when the user click out side of the board
      if (! cellSelected) {
        // class invariant that selected and selectedCoordinates must be consistent
        // is kept by this method
        selected = Optional.empty();
        selectedCoordinates = Optional.empty();
      }
      repaint();
    }
  }
}
