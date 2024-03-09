package cs3500.reversi.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cs3500.reversi.model.ModelCreator;
import cs3500.reversi.player.PlayerActionFeatures;
import cs3500.reversi.model.ReversiModelReadOnly;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Represents an individual window that contains and shows a Reversi game.
 */
public class ReversiGUI extends JFrame implements ReversiFrame {
  private final AbstractGameScreen panel;
  private JLabel gameStatus;

  /**
   * Constructs a ReversiGUI with the specified game model.
   * Initializes the main window properties, sets up the game panel,
   * and makes it focusable for keyboard events.
   * @param model The readonly model of the Reversi game used to initialize the game screen.
   */
  public ReversiGUI(ReversiModelReadOnly model, ModelCreator.GameType gameType) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    panel = createGamePanel(model, gameType);
    //System.out.println(panel.getSize());
    this.gameStatus = new JLabel();
    this.gameStatus.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    //System.out.println(gameStatus.getSize());
    this.add(panel);
    this.add(gameStatus, BorderLayout.NORTH);
    this.pack();
    panel.setFocusable(true);
    panel.requestFocus();
    this.setVisible(true);
    this.gameStatus.setSize(new Dimension(this.panel.getSize().width, 20));
    System.out.println(panel.getSize());
    System.out.println(gameStatus.getSize());
  }

  // creates game panel corresponding to the given game type
  private AbstractGameScreen createGamePanel(ReversiModelReadOnly model,
                                             ModelCreator.GameType gameType) {
    AbstractGameScreen panel;
    if (gameType == ModelCreator.GameType.HEX) {
      panel = new ReversiScreenWithHint(model);
    } else {
      panel = new SquareReversiGameScreen(model);
    }
    return panel;
  }

  @Override
  public void addFeatures(PlayerActionFeatures listener) {
    this.panel.addFeatures(listener);
  }

  @Override
  public void refresh() {
    this.panel.repaint();
  }

  @Override
  public void showMessage(String message) {
    showMessageDialog(this, message);
  }

  @Override
  public void toggleMustPassState(boolean playerMustPass) {
    this.panel.toggleMustPassState(playerMustPass);
  }

  @Override
  public void setGameStateDescription(String description) {
    gameStatus.setText(description);
  }
}
