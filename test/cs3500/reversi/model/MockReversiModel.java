package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.player.Player;

/**
 * A mock implementation of the ReversiModelReadOnly interface for testing purpose.
 * This mock model primarily serves to track the coordinates inspected during game operations
 * and to simulate a game board without implement the actual Reversi rules.
 */
public class MockReversiModel implements ReversiModelReadOnly {
  private List<Coordinates> inspectedCoordinates;
  private List<List<Cell>> mockBoard;
  private List<Coordinates> corner;

  /**
   * Construct a new MockReversiModel with the empty lists of valid move coords adn the mock board.
   * This mock board can be used to set up specific board configurations for testing.
   */
  public MockReversiModel(List<Coordinates> corner) {
    this.inspectedCoordinates = new ArrayList<>();
    this.mockBoard = new ArrayList<>();
    this.corner = corner;

  }

  // Setter method to customize the board for different test scenarios
  public void setMockBoard(List<List<Cell>> mockBoard) {
    this.mockBoard = mockBoard;
  }

  public List<Coordinates> getInspectedCoordinates() {
    return inspectedCoordinates;
  }

  @Override
  public List<List<Cell>> getBoard() {
    return this.mockBoard;
  }

  @Override
  public int getBoardSideLength() {
    return mockBoard.size();
  }


  @Override
  public int scoreEarnedByPlaceIn(Player player, Coordinates coordinates) {
    inspectedCoordinates.add(coordinates);
    // Return a default score or a mocked score
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
    return this.corner;
  }
}
