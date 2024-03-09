package cs3500.reversi.model;

import java.io.IOException;

import cs3500.reversi.player.Player;

/**
 * Mock a model features for testing.
 */
public class MockModelFeatures implements ModelFeatures {
  private Appendable out;

  public MockModelFeatures(Appendable out) {
    this.out = out;
  }

  @Override
  public void newTurn(Player player) {
    this.write("newTurn invoked");
    this.endOutput();
  }

  @Override
  public void mustPass(Player player) {
    this.write("mustPass invoked");
    this.endOutput();
  }

  @Override
  public void gameEnds() {
    this.write("gameEnds invoked");
    this.endOutput();
  }

  // write the given string the output source
  private void write(String str) {
    try {
      this.out.append(str);
    } catch (IOException e) {
      throw new IllegalStateException("Error in showing the view!");
    }
  }

  // ends an output by adding a new line character
  private void endOutput() {
    this.write("\n");
  }
}
