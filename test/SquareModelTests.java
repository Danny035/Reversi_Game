import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.Disc;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.model.RowColumnCoordinates;
import cs3500.reversi.model.SquareReversi;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;

/**
 * A test class for square model class and row column coordinates.
 */
public class SquareModelTests {
  private RowColumnCoordinates coords;
  private RowColumnCoordinates coords1;
  private RowColumnCoordinates coords2;
  private RowColumnCoordinates coords3;
  private RowColumnCoordinates coords4;
  private RowColumnCoordinates coords5;
  private RowColumnCoordinates coords6;
  private RowColumnCoordinates coords7;
  private RowColumnCoordinates coords8;
  private SquareReversi squareReversi1;

  @Before
  public void setup() {
    Player player1 = new HumanPlayer();
    Player player2 = new HumanPlayer();
    coords = new RowColumnCoordinates(1,1);
    coords1 =  new RowColumnCoordinates(0, 1);
    coords2 =  new RowColumnCoordinates(1, 0);
    coords3 =  new RowColumnCoordinates(1, 2);
    coords4 =  new RowColumnCoordinates(2, 1);
    coords5 =  new RowColumnCoordinates(0, 0);
    coords6 =  new RowColumnCoordinates(0, 2);
    coords7 =  new RowColumnCoordinates(2, 0);
    coords8 =  new RowColumnCoordinates(2, 2);
    SquareReversi squareReversi = new SquareReversi(6, player1, player2);
  }

  @Test
  public void testGetAllNeighbors() {
    List<Coordinates> actualNeighbors = coords.getAllNeighbors();
    List<Coordinates> expectedNeighbors = Arrays.asList(coords1,coords2,coords3,coords4,coords5,
            coords6,coords7,coords8);
    Assert.assertEquals(expectedNeighbors.size(), actualNeighbors.size());
    for (Coordinates expectedNeighbor : expectedNeighbors) {
      Assert.assertTrue(actualNeighbors.contains(expectedNeighbor));
    }
  }

  @Test
  public void testGetNeighborFunctions() {
    List<Function<Coordinates, Coordinates>> neighborFunctions = coords.getNeighborFunctions();
    Assert.assertEquals(8, neighborFunctions.size());
    Assert.assertEquals(coords1, neighborFunctions.get(0).apply(coords));
    Assert.assertEquals(coords4, neighborFunctions.get(1).apply(coords));
    Assert.assertEquals(coords2, neighborFunctions.get(2).apply(coords));
    Assert.assertEquals(coords3, neighborFunctions.get(3).apply(coords));
    Assert.assertEquals(coords5, neighborFunctions.get(4).apply(coords));
    Assert.assertEquals(coords7, neighborFunctions.get(5).apply(coords));
    Assert.assertEquals(coords6, neighborFunctions.get(6).apply(coords));
    Assert.assertEquals(coords8, neighborFunctions.get(7).apply(coords));
  }

  @Test
  public void testGetRow() {
    Assert.assertEquals(coords.getRow(1),1);
    Assert.assertEquals(coords.getRow(10),1);
    Assert.assertEquals(coords1.getRow(1),0);
    Assert.assertEquals(coords1.getRow(10),0);
  }

  @Test
  public void testGetLocationInRow() {
    Assert.assertEquals(coords.getLocationInRow(1),1);
    Assert.assertEquals(coords.getLocationInRow(10),1);
    Assert.assertEquals(coords1.getLocationInRow(1),1);
    Assert.assertEquals(coords1.getLocationInRow(10),1);
  }

  @Test
  public void testValidCoordsOnBoard() {
    Assert.assertFalse(coords.validCoordsOnBoard(1));
    Assert.assertTrue(coords4.validCoordsOnBoard(3));
    Assert.assertFalse(coords.validCoordsOnBoard(-1));
    Assert.assertFalse(coords4.validCoordsOnBoard(1));
  }

  @Test
  public void testEqual() {
    Assert.assertEquals(coords, coords);
    RowColumnCoordinates coords1 = new RowColumnCoordinates(1, 1);
    Assert.assertEquals(coords,coords1);
    Assert.assertNotEquals(coords, coords2);
  }

  @Test
  public void testHashCode() {
    Assert.assertEquals(coords.hashCode(), coords.hashCode());
    RowColumnCoordinates coords1 = new RowColumnCoordinates(1, 1);
    Assert.assertEquals(coords.hashCode(),coords1.hashCode());
    Assert.assertNotEquals(coords.hashCode(), coords2.hashCode());
  }

  // Subclass to expose the protected method
  private static class TestableSquareReversi extends SquareReversi {
    public TestableSquareReversi(int boardSize) {
      super(boardSize, null, null);
    }

    public void testValidateBoardSize(int boardSize) {
      super.validateBoardSize(boardSize);
    }

    public List<List<Cell>> testInitializeBoard() {
      return super.initializeBoard();
    }

    public void testPlaceInitialDiscs() {
      super.placeInitialDiscs();
    }

    public ReversiModelReadWrite testCopyModel() {
      return super.copyModel();
    }
  }


  @Test
  public void testValidateBoardSizeWithInvalidSizes() {
    TestableSquareReversi game = new TestableSquareReversi(8);
    Assert.assertThrows(IllegalArgumentException.class, () -> game.testValidateBoardSize(0));
    Assert.assertThrows(IllegalArgumentException.class, () -> game.testValidateBoardSize(-4));
    Assert.assertThrows(IllegalArgumentException.class, () -> game.testValidateBoardSize(5));
  }

  @Test
  public void testInitializeBoard() {
    TestableSquareReversi game = new TestableSquareReversi(8);
    List<List<Cell>> board = game.testInitializeBoard();
    Assert.assertEquals(8, board.size());
    for (List<Cell> row : board) {
      Assert.assertEquals(8, row.size());
      for (Cell cell : row) {
        Assert.assertNotNull(cell);
      }
    }
  }

  @Test
  public void testPlaceInitialDiscs() {
    TestableSquareReversi game = new TestableSquareReversi(8);
    Assert.assertEquals(Disc.WHITE, game.getCellInBoard(new RowColumnCoordinates(3,4)).getDisc());
    Assert.assertEquals(Disc.WHITE, game.getCellInBoard(new RowColumnCoordinates(4,3)).getDisc());
    Assert.assertEquals(Disc.BLACK, game.getCellInBoard(new RowColumnCoordinates(3, 3)).getDisc());
    Assert.assertEquals(Disc.BLACK, game.getCellInBoard(new RowColumnCoordinates(4, 4)).getDisc());
  }
}
