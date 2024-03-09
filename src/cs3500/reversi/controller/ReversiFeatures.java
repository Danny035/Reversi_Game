package cs3500.reversi.controller;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.player.PlayerActionFeatures;

/**
 * Represents a Reversi Features that supports listening to high-level player action information
 * sent from a Reversi view, and notification send from a Reversi model.
 */
public class ReversiFeatures implements PlayerActionFeatures, ModelFeatures {
  // the controller that listens to the view / player and model through this features
  private ReversiController controller;

  public ReversiFeatures(ReversiController controller) {
    this.controller = controller;
  }

  @Override
  public void pass() {
    this.controller.pass();
  }

  @Override
  public void placeIn(Coordinates coordinates) {
    this.controller.placeIn(coordinates);
  }

  @Override
  public int select(Coordinates coordinates) {
    return this.controller.select(coordinates);
  }

  @Override
  public void newTurn(Player player) {
    this.controller.newTurn(player);
  }

  @Override
  public void mustPass(Player player) {
    this.controller.mustPass(player);
  }

  @Override
  public void gameEnds() {
    this.controller.gameEnds();
  }
}
