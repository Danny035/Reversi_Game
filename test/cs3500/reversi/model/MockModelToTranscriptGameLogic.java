package cs3500.reversi.model;

import java.io.IOException;
import java.util.List;

import cs3500.reversi.player.Player;

/**
 * A mock Implement of the ReversiReadOnly interface used for testing some certain situation.
 * This class wraps around an actual HexReversi and provide additional functionality to log the
 * method calls and their results to Appendable.
 */
public class MockModelToTranscriptGameLogic implements ReversiModelReadWrite {
  ReversiModelReadWrite actualModel;
  Appendable out;

  /**
   * Construct a new MockModelToTranscriptGameLogic with the game board size,
   * 2 players that involved in playing this game, and those will be used to construct a real
   * Reversi model in order to test using the actual game logic. This constructor also takes in an
   * Appendable object as an output source to append transcript of method calling and/or
   * parameters passing in that are relevant to the test.
   * @param boardSize a game board size
   * @param player1 one of the 2 players involved in playing the game
   * @param player2 the other of the 2 players involved in playing the game
   * @param out output source to append transcript to
   */
  public MockModelToTranscriptGameLogic(int boardSize, Player player1, Player player2,
                                        Appendable out) {
    this.actualModel = new HexReversi(boardSize, player1, player2);
    this.out = out;
  }

  @Override
  public int getBoardSideLength() {
    return actualModel.getBoardSideLength();
  }

  @Override
  public List<List<Cell>> getBoard() {
    return actualModel.getBoard();
  }

  @Override
  public int scoreEarnedByPlaceIn(Player player, Coordinates coordinates) {
    this.appendTranscript(coordinates.toString());
    return actualModel.scoreEarnedByPlaceIn(player, coordinates);
  }

  @Override
  public boolean movable(Player player) {
    return actualModel.movable(player);
  }

  @Override
  public int getScore(Player player) {
    return actualModel.getScore(player);
  }

  @Override
  public boolean isGameOver() {
    return actualModel.isGameOver();
  }

  @Override
  public Player getTurn() {
    return actualModel.getTurn();
  }

  @Override
  public Cell getCellInBoard(Coordinates coordinates) {
    return actualModel.getCellInBoard(coordinates);
  }

  @Override
  public ReversiModelReadOnly tryMove(Player player, Coordinates coordinates)
          throws IllegalArgumentException, IllegalStateException {
    return actualModel.tryMove(player, coordinates);
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
    actualModel.pass(player);
  }

  @Override
  public void placeIn(Player player, Coordinates coordinates) {
    actualModel.placeIn(player, coordinates);
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
