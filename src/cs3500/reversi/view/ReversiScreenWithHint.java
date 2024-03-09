package cs3500.reversi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JButton;

import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a Reversi game screen that have the ability to show hint
 * when clicking on a cell in the board, and the hint can be turned on or off.
 */
public class ReversiScreenWithHint extends HexReversiGameScreen {
  private boolean enableHint;

  /**
   * Constructs a ReversiGameScreen with the specified game model.
   * Initializes display properties and sets up a mouse listener for user interactions.
   *
   * @param model The readonly model of the Reversi game.
   */
  public ReversiScreenWithHint(ReversiModelReadOnly model) {
    super(model);
    this.enableHint = false;
    setLayout(new FlowLayout(FlowLayout.RIGHT));
    JButton hintButton = new JButton("Enable Hints");
    hintButton.addActionListener(e -> {
      //Toggle the button
      this.setHintsEnabled(!this.enableHint);
      hintButton.setText(this.enableHint ? "Disable Hints" : "Enable Hints");
      this.requestFocus();
      this.repaint();
    });
    this.add(hintButton, BorderLayout.EAST);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    if (enableHint && selected.isPresent()) {
      Shape cell = selected.get();
      Rectangle bounds = cell.getBounds();
      String scoreText = String.valueOf(potentialScore.get());
      FontMetrics fm = g2d.getFontMetrics();
      int textWidth = fm.stringWidth(scoreText);
      int textHeight = fm.getHeight();
      // Calculate the position of the text to be centered within the cell.
      int x = (int)(translateX + transformScale * ( bounds.x + (bounds.width - textWidth) / 2));
      int y = (int)(translateY + transformScale *
                      (bounds.y + (bounds.height - textHeight) / 2 + fm.getAscent()));
      // Choose a color for the hint number
      g2d.setColor(Color.BLUE);
      g2d.drawString(scoreText, x, y);
    }
  }

  // EFFECT:
  // set the state of whether the hint is enabled
  private void setHintsEnabled(boolean b) {
    this.enableHint = b;
  }
}
