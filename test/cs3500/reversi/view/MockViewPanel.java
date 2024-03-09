package cs3500.reversi.view;

import java.awt.event.KeyListener;

import cs3500.reversi.player.PlayerActionFeatures;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.awt.event.KeyAdapter;
import cs3500.reversi.model.Coordinates;

/**
 * A mock implementation of the ReversiPanel interface for testing purposes.
 * This class simulates a graphical user interface panel for the Reversi game,
 * allowing for the simulation of player actions like passing a turn or placing a disc.
 */
public class MockViewPanel implements ReversiPanel {
  private PlayerActionFeatures playerActionFeatures;
  private KeyListener keyListener;
  public Coordinates lastPlacedInCoordinates;
  private boolean mustPassState;

  @Override
  public void addFeatures(PlayerActionFeatures features) {
    this.playerActionFeatures = features;
    this.keyListener = new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        // pressing space key meaning the player would like to pass this round of the game
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          features.pass();
          mustPassState = true;
          clearSelected();
        }
        // pressing enter key meaning the player would like to place the disc
        // in the selected cell if the player has selected any
        if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedCoordinates.isPresent()) {
          features.placeIn(selectedCoordinates.get());
          lastPlacedInCoordinates = selectedCoordinates.get();
          clearSelected();
        }
      }
    };
  }

  /**
   * Method simulate the player input.
   * @param keyEvent The KeyEvent object representing the key action to be simulated
   */
  public void simulateKeyPress(KeyEvent keyEvent) {
    if (keyListener != null) {
      keyListener.keyPressed(keyEvent);
    }
  }

  // Assuming selectedCoordinates is managed elsewhere in your panel
  private Optional<Coordinates> selectedCoordinates = Optional.empty();

  public void setSelectedCoordinates(Coordinates coordinates) {
    this.selectedCoordinates = Optional.of(coordinates);
  }

  private void clearSelected() {
    this.selectedCoordinates = Optional.empty();
  }


  @Override
  public void toggleMustPassState(boolean mustPass) {
    this.mustPassState = mustPass;
  }

  // Getters for test assertions
  public PlayerActionFeatures getFeatures() {
    return playerActionFeatures;
  }

  public boolean getMustPassState() {
    return mustPassState;
  }

  /**
   * Method to directly invoke the pass action.
   */
  public void triggerPassAction() {
    if (playerActionFeatures != null) {
      playerActionFeatures.pass();
      mustPassState = true;
      clearSelected();
    }
  }

  /**
   * Method to directly invoke the placeIn action.
   * @param coordinates the cubeCoordinates
   */
  public void triggerPlaceInAction(Coordinates coordinates) {
    if (playerActionFeatures != null && coordinates != null) {
      playerActionFeatures.placeIn(coordinates);
      lastPlacedInCoordinates = coordinates;
      clearSelected();
    }
  }
}
