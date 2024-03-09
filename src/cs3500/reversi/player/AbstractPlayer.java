package cs3500.reversi.player;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents an abstract implementation of a player in the Reversi game.
 * This class provides the basic functionality common to all players,
 * such as managing action listeners and executing common player actions.
 */
public abstract class AbstractPlayer implements Player {
  protected final List<PlayerActionFeatures> actionListeners;

  /**
   * Constructs an AbstractPlayer with an empty list of action listeners.
   */
  public AbstractPlayer() {
    this.actionListeners = new ArrayList<>();
  }

  @Override
  public abstract void turnToMove(ReversiModelReadOnly model);

  @Override
  public void placeIn(Coordinates coordinates) {
    for (PlayerActionFeatures listener : this.actionListeners) {
      listener.placeIn(coordinates);
    }
  }

  @Override
  public void pass() {
    for (PlayerActionFeatures listener : this.actionListeners) {
      listener.pass();
    }
  }

  @Override
  public void addFeatures(PlayerActionFeatures playerActionFeatures) {
    this.actionListeners.add(playerActionFeatures);
  }
}
