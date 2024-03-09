package cs3500.reversi.view;

import cs3500.reversi.player.PlayerActionFeatures;

/**
 * Mock the ReversiFrame to make sure the add feature work correctly in the controller.
 */
public class MockViewFrame implements ReversiFrame {
  private boolean mustPassState;
  private String lastMessage;
  private boolean isRefresh;
  private PlayerActionFeatures features;

  @Override
  public void addFeatures(PlayerActionFeatures listener) {
    this.features = listener;
  }

  @Override
  public void refresh() {
    this.isRefresh = true;
  }

  @Override
  public void showMessage(String message) {
    this.lastMessage = message;
  }

  @Override
  public void toggleMustPassState(boolean playerMustPass) {
    this.mustPassState = playerMustPass;
  }

  @Override
  public void setGameStateDescription(String description) {
    // intentionally left blank
  }

  public boolean getMustPassState() {
    return mustPassState;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public boolean getIsRefresh() {
    return isRefresh;
  }

  public PlayerActionFeatures getAddFeature() {
    return features;
  }
}
