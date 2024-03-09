package cs3500.reversi.controller;

import java.io.IOException;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.view.ReversiFrame;
import cs3500.reversi.player.PlayerActionFeatures;


/**
 * A mock controller implementation for the Reversi game.
 * This class is designed for testing purposes and simulates the functionalities
 * of a game controller interacting with the model and view components of the game.
 */
public class MockHexReversiController implements ReversiController {
  private final PlayerActionFeatures playerActionFeatures;
  private final ReversiModelReadWrite model;
  private final ReversiFrame view;
  private final Player player;
  private Appendable out;

  /**
   * Constructs a MockHexReversiController with the given model, view, and player.
   * Initializes listeners and features for model-view interaction.
   */
  public MockHexReversiController(ReversiModelReadWrite model, ReversiFrame view, Player player,
                                  Appendable out) {
    this.model = model;
    this.view = view;
    ModelFeatures modelFeatures = new ReversiFeatures(this);
    this.playerActionFeatures = new ReversiFeatures(this);
    this.player = player;
    // add listeners to the model and the view
    this.model.addFeatures(modelFeatures);
    this.view.addFeatures(this.playerActionFeatures);
    this.player.addFeatures(this.playerActionFeatures);
    this.out = out;
  }


  @Override
  public void pass() {
    try {
      this.view.toggleMustPassState(false);
      this.model.pass(this.player);
    } catch (IllegalStateException e) {
      this.view.showMessage("You can't move now!");
    }
  }

  @Override
  public void placeIn(Coordinates coordinates) {
    try {
      this.model.placeIn(this.player, coordinates);
    } catch (IllegalArgumentException e) {
      this.view.showMessage("Illegal move!");
    } catch (IllegalStateException e) {
      this.view.showMessage("You can't move now!");
    }
  }

  @Override
  public int select(Coordinates coordinates) {
    return this.model.scoreEarnedByPlaceIn(this.player, coordinates);
  }

  @Override
  public void newTurn(Player player) {
    this.write("newTurn");
    // if the given active player is the player that this controller is acting on behalf of,
    // then call the player to make a move
    if (player == this.player) {
      this.write(" player should move");
      this.player.turnToMove(this.model);
    }
    // tell the view to refresh itself because a new turn means an action have happened
    this.view.refresh();
  }

  @Override
  public void mustPass(Player player) {
    if (player == this.player) {
      this.view.toggleMustPassState(true);
    }
  }

  @Override
  public void gameEnds() {
    this.view.showMessage("Game ends!");
  }

  public PlayerActionFeatures getPlayerActionFeatures() {
    return this.playerActionFeatures;
  }

  // write the given string the output source
  private void write(String str) {
    try {
      this.out.append(str);
    } catch (IOException e) {
      throw new IllegalStateException("Error in showing the view!");
    }
  }
}
