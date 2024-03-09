package cs3500.reversi.controller;

import cs3500.reversi.model.Disc;
import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.view.ReversiFrame;
import cs3500.reversi.player.PlayerActionFeatures;


/**
 * Represents a controller for a Reversi game which is interacting with the other part of
 * the game system (model and view) on behalf of a player.
 */
public class ReversiControllerImpl implements ReversiController {
  private final ReversiModelReadWrite model;
  private final ReversiFrame view;
  // the player that this controller is interacting with the rest of the system on behalf of
  private final Player player;

  /**
   * Constructs a new HexReversiController.
   * This controller acts as an intermediary between the model, view, and a player in the
   * Reversi game. It initializes and sets up listeners and features for both model and view
   * components to facilitate game actions and interactions.
   *
   * @param model The Reversi game model that this controller will interact with.
   *              It provides read and write access to the game's state and logic.
   * @param view The Reversi game view that this controller will update based on
   *             changes in the game stateand player actions. It represents the
   *             graphical user interface of the game.
   * @param player The player that this controller represents and manages within the game.
   *               The controller will relay the player's actions to the model and view.
   */
  public ReversiControllerImpl(ReversiModelReadWrite model, ReversiFrame view, Player player) {
    this.model = model;
    this.view = view;
    ModelFeatures modelFeatures = new ReversiFeatures(this);
    // features used to listen to a view
    PlayerActionFeatures playerActionFeatures = new ReversiFeatures(this);
    this.player = player;
    this.setGameStateDescriptionOnView(false);
    // add listeners to the model and the view
    this.model.addFeatures(modelFeatures);
    this.view.addFeatures(playerActionFeatures);
    this.player.addFeatures(playerActionFeatures);
  }

  // adds the game state description to the game window. Besides the description of the player's
  // disc color and current score, it will also notify the player if it's the player's turn to
  // play represented by the given boolean.
  private void setGameStateDescriptionOnView(boolean turn) {
    Disc discOfPlayer = this.model.getDiscOf(this.player);
    String discColor;
    if (discOfPlayer == Disc.WHITE) {
      discColor = "White";
    } else {
      discColor = "Black";
    }
    String score = "Score: " + this.model.getScore(this.player);
    String description = discColor + "\n" + score;
    if (turn) {
      description += "\nYour turn!";
    }
    this.view.setGameStateDescription(description);
  }


  @Override
  public void pass() {
    if (model.getTurn() == this.player) {
      this.view.toggleMustPassState(false);
      this.doneMove();
      this.model.pass(this.player);
    } else {
      this.view.showMessage("You can't move now!");
    }
  }

  @Override
  public void placeIn(Coordinates coordinates) {
    if (model.getTurn() == this.player) {
      if (this.model.scoreEarnedByPlaceIn(this.player, coordinates) > 0) {
        this.doneMove();
        this.model.placeIn(this.player, coordinates);
      } else {
        this.view.showMessage("Illegal move!");
      }
    } else {
      this.view.showMessage("You can't move now!");
    }
  }

  @Override
  public int select(Coordinates coordinates) {
    try {
      return this.model.scoreEarnedByPlaceIn(this.player, coordinates);
    } catch (IllegalStateException e) {
      // if there is an IllegalArgumentException thrown, then the game is over,
      // so just return 0
      return 0;
    }

  }

  // dealing with events after the player make a move for this round of the game
  private void doneMove() {
    this.setGameStateDescriptionOnView(false);
  }

  @Override
  public void newTurn(Player player) {
    // if the given active player is the player that this controller is acting on behalf of,
    // then call the player to make a move
    boolean playerHasTurn = player == this.player;
    this.setGameStateDescriptionOnView(playerHasTurn);
    if (playerHasTurn) {
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
}