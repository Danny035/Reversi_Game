package cs3500.reversi.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.player.Player;

/**
 * A mock implementation of the ReversiReadWrite Model interface used for testing whether
 * certain methods are called or what parameters was passed in during the determination of
 * strategies. This model append such interested tracking information into an Appendable object
 * as a transcript.
 */
public class MockModelToTranscript implements ReversiModelReadWrite {
  Appendable out;

  /**
   * Construct a new MockModelToTranscript with given Appendable objects that
   * will later be used to append interested method calling / parameters passing in information
   * to facilitate testing.
   * @param out an output source that can append transcript of interested method calling
   *            or parameters passing in actions.
   */
  public MockModelToTranscript(Appendable out) {
    this.out = out;
  }

  @Override
  public int getBoardSideLength() {
    return 0;
  }

  @Override
  public List<List<Cell>> getBoard() {
    return new ArrayList<>();
  }

  @Override
  public int scoreEarnedByPlaceIn(Player player, Coordinates coordinates) {
    this.appendTranscript(coordinates.toString());
    return 0;
  }

  @Override
  public boolean movable(Player player) {
    return false;
  }

  @Override
  public int getScore(Player player) {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Player getTurn() {
    return null;
  }

  @Override
  public Cell getCellInBoard(Coordinates coordinates) {
    return null;
  }

  @Override
  public ReversiModelReadOnly tryMove(Player player, Coordinates coordinates)
          throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Disc getDiscOf(Player player) throws IllegalArgumentException {
    return null;
  }

  @Override
  public List<Coordinates> getCorners() {
    return null;
  }

  @Override
  public void pass(Player player) {
    // intentionally left blank because this is a mock model
  }

  @Override
  public void placeIn(Player player, Coordinates coordinates) {
    // intentionally left blank because this is a mock model
  }

  @Override
  public void startGame() {
    // intentionally left blank because this is a mock model
  }

  @Override
  public void addFeatures(ModelFeatures listener) {
    // intentionally left blank because this is a mock model
  }

  private void appendTranscript(String transcript) {
    try {
      this.out.append(transcript + "\n");
    } catch (IOException e) {
      throw new IllegalArgumentException("Output source has a problem.");
    }
  }
}
