package cs3500.reversi.model;

import java.util.List;
import java.util.ArrayList;

import cs3500.reversi.player.Player;

/**
 * A mock Reversi model where it lies about the points that could be earned for a particular
 * coordinates on board, which is for testing whether strategies relating to determining
 * values that each coordinates worth actually returns the most valuable coordinates.
 */
public class MockReversiModelForValuableLocation implements ReversiModelReadOnly {
  private List<Coordinates> inspectedCoordinates;
  private List<List<Cell>> mockBoard;
  private List<Coordinates> corner;
  private Coordinates valuableLocation;
  private int highScore;

  /**
   * Constructs a new MockReversiModelForValuableLocation with specified coordinates and score.
   * This mock model is designed to simulate a scenario where a particular set of coordinates on
   * the game board is assigned an artificially high score. It is used to test the effectiveness of
   * strategies that aim to identify the most valuable coordinates on the game board.
   * @param valuableLocation a specific coordinates that the user of this class wants
   *                         to lie about
   * @param highScore a specific score for the coordinates that the user of this class wants
   *                  to lie about
   */
  public MockReversiModelForValuableLocation(Coordinates valuableLocation, int highScore) {
    this.valuableLocation = valuableLocation;
    this.highScore = highScore;
    this.inspectedCoordinates = new ArrayList<>();
    this.mockBoard = new ArrayList<>();
  }

  @Override
  public int scoreEarnedByPlaceIn(Player player, Coordinates coordinates) {
    if (coordinates.equals(valuableLocation)) {
      return highScore;
    }
    return 0; // Return a low score for other locations
  }

  // Setter method to customize the board for different test scenarios
  public void setMockBoard(List<List<Cell>> mockBoard) {
    this.mockBoard = mockBoard;
  }

  public List<Coordinates> getInspectedCoordinates() {
    return inspectedCoordinates;
  }


  @Override
  public int getBoardSideLength() {
    return 0;
  }

  @Override
  public List<List<Cell>> getBoard() {
    return null;
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

}
