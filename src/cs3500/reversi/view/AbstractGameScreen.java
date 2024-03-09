package cs3500.reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.Disc;
import cs3500.reversi.model.ReversiModelReadOnly;
import cs3500.reversi.player.PlayerActionFeatures;

/**
 * Represents an abstract Reversi game screen that abstract over and reduce duplicate code
 * in each implementation of a specific Reversi game screen.
 */
public abstract class AbstractGameScreen extends JPanel implements ReversiPanel {
  protected final ReversiModelReadOnly model;
  protected final Map<Disc, Color> discColorMap;
  // Class Invariant: selected and selectedCoordinates must be either both empty
  // or both have a value
  protected Optional<Shape> selected;
  protected Optional<Coordinates> selectedCoordinates;
  protected Optional<Integer> potentialScore;
  protected final Color borderColor;
  protected final Color cellColor;
  protected final Color selectedCellColor;
  protected Optional<Map<Shape, Coordinates>> cells;
  protected Map<Shape, Color> discs;
  protected final int discRadius;
  protected double transformScale;
  protected double translateX;
  protected double translateY;
  protected boolean noMoreMove;
  protected String gameStateDescription;

  /**
   * Constructs a game screen and initialize the basic shared fields of the game screen.
   * @param model the model that this game screen is presenting information on
   */
  public AbstractGameScreen(ReversiModelReadOnly model) {
    this.model = model;
    Map<Disc, Color> discMappingColor = new HashMap<>();
    discMappingColor.put(Disc.BLACK, Color.BLACK);
    discMappingColor.put(Disc.WHITE, Color.WHITE);
    this.discColorMap = Collections.unmodifiableMap(discMappingColor);
    this.borderColor = Color.BLACK;
    this.cellColor = Color.lightGray;
    // class invariant that selected and selectedCoordinates must be consistent
    // is kept by this constructor
    this.selected = Optional.empty();
    this.selectedCoordinates = Optional.empty();
    this.potentialScore = Optional.empty();
    this.selectedCellColor = Color.pink;
    this.cells = Optional.empty();
    this.discs = new HashMap<>();
    this.discRadius = 20;
    this.transformScale = 1;
    this.translateX = 0;
    this.translateY = 0;
    this.noMoreMove = false;
    this.gameStateDescription = "";
    this.setVisible(true);
  }

  @Override
  public void addFeatures(PlayerActionFeatures playerActionFeatures) {
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        // pressing space key meaning the player would like to pass this round of the game
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          playerActionFeatures.pass();
          clearSelected();
        }
        // pressing enter key meaning the player would like to place the disc
        // in the selected cell if the player has selected any
        if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedCoordinates.isPresent()) {
          playerActionFeatures.placeIn(selectedCoordinates.get());
          clearSelected();
        }
      }
    });

    this.addMouseListener(new MouseInputAdapter() {
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
                potentialScore = Optional.of(playerActionFeatures.select(selectedCoords));
                cellSelected = true;
              }
            }
          }
        }
        // this can be true when the user click on the same cell that is already selected
        // to deselect it, or when the user click outside the board
        if (! cellSelected) {
          // class invariant that selected and selectedCoordinates must be consistent
          // is kept by this method
          selected = Optional.empty();
          selectedCoordinates = Optional.empty();
        }
        repaint();
      }
    });
  }


  // deselect the selected cell if there is one
  private void clearSelected() {
    // class invariant that selected and selectedCoordinates must be consistent
    // is kept by this method
    this.selected = Optional.empty();
    this.selectedCoordinates = Optional.empty();
  }

  @Override
  public abstract void toggleMustPassState(boolean mustPass);

  // transform the screen in case the user adjust the window size
  protected void transformScreen(Graphics2D g2d) {
    AffineTransform ret = new AffineTransform();
    Dimension gameBoardSize = getGameBoardSize();
    // make sure the board is always in a regular size
    this.transformScale = Math.min(getWidth() / gameBoardSize.getWidth(),
            getHeight() / gameBoardSize.getHeight());
    // make sure the board always stay in the middle of the window
    this.translateX = (getWidth() - this.transformScale * gameBoardSize.getWidth()) / 2;
    this.translateY = ((getHeight() - this.transformScale * gameBoardSize.getHeight()) / 2);
    ret.translate(this.translateX, this.translateY);
    ret.scale(this.transformScale, this.transformScale);
    g2d.transform(ret);
  }

  protected abstract Dimension getGameBoardSize();

  // draws the cells on the game board
  protected void drawCells(Graphics2D g2d) {
    if (this.cells.isPresent()) {
      for (Shape hexagonCell : this.cells.get().keySet()) {
        g2d.setColor(this.cellColor);
        g2d.fill(hexagonCell);
        g2d.setColor(this.borderColor);
        g2d.draw(hexagonCell);
      }
    }
  }

  // highlight the selected cell if the player have selected any
  protected void drawSelected(Graphics2D g2d) {
    if (this.selected.isPresent()) {
      Shape cell = this.selected.get();
      g2d.setColor(this.selectedCellColor);
      g2d.fill(cell);
      // re-draw the border again to ensure the entire border is shown
      // without covering up by the above filling
      g2d.setColor(this.borderColor);
      g2d.draw(cell);
    }
  }

  // draws the discs on the game board
  protected void drawDiscs(Graphics2D g2d) {
    for (Shape disc : this.discs.keySet()) {
      g2d.setColor(this.discs.get(disc));
      g2d.fill(disc);
    }
  }

  // draws the current game status of the player using this screen,
  // including what color of disc is this player using to player,
  // the score of the player, and whether this is the turn to play
  protected void printGameStatus(Graphics2D g2d) {
    g2d.setColor(Color.RED);
    g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    g2d.drawString(this.gameStateDescription, 10, 20);
  }

  // record the shape and location of the disc on the given cell if there is any
  protected void recordDiscOnCell(Map<Shape, Color> discsOnBoard, Cell cell,
                                  double centerX, double centerY) {
    if (!cell.isEmpty() && this.discColorMap.containsKey(cell.getDisc())) {
      Color discColor = this.discColorMap.get(cell.getDisc());
      Ellipse2D.Double disc = new Ellipse2D.Double(centerX - this.discRadius,
              centerY - this.discRadius, this.discRadius * 2, this.discRadius * 2);
      discsOnBoard.put(disc, discColor);
    }
  }
}
