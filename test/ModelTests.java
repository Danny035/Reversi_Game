import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import cs3500.reversi.controller.MockHexReversiController;
import cs3500.reversi.model.HexagonalCoordinates;
import cs3500.reversi.model.MockModelFeatures;
import cs3500.reversi.model.ModelFeatures;
import cs3500.reversi.model.SquareReversi;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CubeCoordinates;
import cs3500.reversi.model.Disc;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.view.MockViewFrame;
import cs3500.reversi.view.SquareReversiTextualView;

/**
 * Test class for Reversi Model.
 */
public class ModelTests {
  private Cell hexCell1;
  private Cell hexCell2;
  private Cell hexCell3;
  private HexagonalCoordinates coords1;
  private HexagonalCoordinates coords2;
  private HexagonalCoordinates coords3;
  private HexagonalCoordinates coords4;
  private ReversiModelReadWrite model1;
  private ReversiModelReadWrite model2;
  private Player player1;
  private Player player2;


  @Before
  public void setup() {
    coords1 = new CubeCoordinates(0, 0);
    coords2 = new CubeCoordinates(1, 2);
    coords3 = new CubeCoordinates(-1, -2);
    coords4 = new CubeCoordinates(-1, 2);
    player1 = new HumanPlayer();
    player2 = new HumanPlayer();
    hexCell1 = new ReversiCell(coords1);
    hexCell2 = new ReversiCell(coords1, Disc.WHITE);
    hexCell3 = new ReversiCell(coords4, Disc.BLACK);
    model1 = new HexReversi(6,
            player1,
            player2);
    model2 = new HexReversi(3,
            player1,
            player2);
  }

  /*
  Following tests are for methods in CubeCoordinates
  */

  @Test
  public void testCubeCoordinatesConstructor() {
    Assert.assertEquals(0, coords1.getQ());
    Assert.assertEquals(0, coords1.getR());
    Assert.assertEquals(0, coords1.getS());

    Assert.assertEquals(1, coords2.getQ());
    Assert.assertEquals(2, coords2.getR());
    Assert.assertEquals(-3, coords2.getS());

    Assert.assertEquals(-1, coords3.getQ());
    Assert.assertEquals(-2, coords3.getR());
    Assert.assertEquals(3, coords3.getS());

    Assert.assertEquals(-1, coords4.getQ());
    Assert.assertEquals(2, coords4.getR());
    Assert.assertEquals(-1, coords4.getS());
  }

  @Test
  public void testCubeCoordinatesEquals() {
    CubeCoordinates sameCoords1 = new CubeCoordinates(0, 0);
    CubeCoordinates sameCoords2 = new CubeCoordinates(1, 2);
    CubeCoordinates sameCoords3 = new CubeCoordinates(-1, -2);
    CubeCoordinates sameCoords4 = new CubeCoordinates(-1, 2);
    CubeCoordinates differentCoords1 = new CubeCoordinates(1, -1);
    Assert.assertTrue(coords1.equals(sameCoords1));
    Assert.assertFalse(coords1.equals(differentCoords1));

    Assert.assertTrue(coords2.equals(sameCoords2));
    Assert.assertFalse(coords2.equals(differentCoords1));

    Assert.assertTrue(coords3.equals(sameCoords3));
    Assert.assertFalse(coords3.equals(differentCoords1));

    Assert.assertTrue(coords4.equals(sameCoords4));
    Assert.assertFalse(coords4.equals(differentCoords1));
  }

  @Test
  public void testCubeCoordinatesHashCode() {
    CubeCoordinates sameCoords1 = new CubeCoordinates(0, 0);
    CubeCoordinates sameCoords2 = new CubeCoordinates(1, 2);
    CubeCoordinates sameCoords3 = new CubeCoordinates(-1, -2);
    CubeCoordinates sameCoords4 = new CubeCoordinates(-1, 2);
    Assert.assertEquals(this.coords1.hashCode(), sameCoords1.hashCode());
    Assert.assertEquals(this.coords2.hashCode(), sameCoords2.hashCode());
    Assert.assertEquals(this.coords3.hashCode(), sameCoords3.hashCode());
    Assert.assertEquals(this.coords4.hashCode(), sameCoords4.hashCode());

    Assert.assertNotEquals(this.coords1.hashCode(), sameCoords4.hashCode());
    Assert.assertNotEquals(this.coords2.hashCode(), sameCoords3.hashCode());
  }

  @Test
  public void testCubeCoordinatesToString() {
    Assert.assertEquals("Cube Coordinates: 0, 0, 0", coords1.toString());
    Assert.assertEquals("Cube Coordinates: 1, 2, -3", coords2.toString());
    Assert.assertEquals("Cube Coordinates: -1, -2, 3", coords3.toString());
    Assert.assertEquals("Cube Coordinates: -1, 2, -1", coords4.toString());
  }

  @Test
  public void testCubeCoordinatesGetQ() {
    Assert.assertEquals(this.coords1.getQ(), 0);
    Assert.assertEquals(this.coords2.getQ(), 1);
    Assert.assertEquals(this.coords3.getQ(), -1);
    Assert.assertEquals(this.coords4.getQ(), -1);
  }

  @Test
  public void testCubeCoordinatesGetR() {
    Assert.assertEquals(this.coords1.getR(), 0);
    Assert.assertEquals(this.coords2.getR(), 2);
    Assert.assertEquals(this.coords3.getR(), -2);
    Assert.assertEquals(this.coords4.getR(), 2);
  }

  @Test
  public void testCubeCoordinatesGetS() {
    Assert.assertEquals(this.coords1.getS(), 0);
    Assert.assertEquals(this.coords2.getS(), -3);
    Assert.assertEquals(this.coords3.getS(), 3);
    Assert.assertEquals(this.coords4.getS(), -1);
  }

  /*
  Following tests are for methods in HexCell class.
  */

  @Test
  public void testHexCellConstructor() {
    Assert.assertTrue(hexCell1.isEmpty());
    Assert.assertFalse(hexCell2.isEmpty());
  }

  @Test
  public void testConstructionWithDisc() {
    Assert.assertEquals(Disc.WHITE, hexCell2.getDisc());
  }

  @Test
  public void testPlaceDisc() {
    hexCell1.placeDisc(Disc.WHITE);
    Assert.assertFalse(hexCell1.isEmpty());
    Assert.assertEquals(Disc.WHITE, hexCell1.getDisc());
  }

  @Test
  public void testPlaceDiscOnOccupiedCell() {
    hexCell1.placeDisc(Disc.WHITE);
    Assert.assertThrows(IllegalStateException.class, () -> hexCell1.placeDisc(Disc.BLACK));
  }

  @Test
  public void testFlipDisc() {
    hexCell2.flipDiscTo(Disc.BLACK);
    Assert.assertEquals(Disc.BLACK, hexCell2.getDisc());
  }

  @Test
  public void testGetDiscFromEmptyCell() {
    Assert.assertThrows(IllegalStateException.class, hexCell1::getDisc);
  }

  @Test
  public void testGetCoordinates() {
    Assert.assertEquals(this.coords1, hexCell1.getCoordinates());
    Assert.assertEquals(this.coords1, hexCell2.getCoordinates());
    Assert.assertEquals(this.coords4, hexCell3.getCoordinates());
  }

  /*
  Following tests are for methods in HexReversi class.
  */

  @Test
  public void testGetBoardSideLength() {
    Assert.assertEquals(6, model1.getBoardSideLength());
    Assert.assertEquals(3, model2.getBoardSideLength());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidBoardSize() {
    new HexReversi(2, player1, player2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNegativeBoardSize() {
    new HexReversi(-1, player1, player2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidZeroBoardSize() {
    new HexReversi(0, player1, player2);
  }

  @Test
  public void testGetBoard() {
    List<List<Cell>> resultBoard = model1.getBoard();
    Assert.assertNotNull(resultBoard);
    int cellnum = 0;
    for (List<Cell> row:resultBoard) {
      cellnum += row.size();
    }
    Assert.assertEquals(91, cellnum);
  }

  @Test
  public void testGetBoardReturnsSeperateBoardThanOriginal() {
    List<List<Cell>> newBoard = model1.getBoard();
    Assert.assertEquals(3, model1.getScore(player1));
    Assert.assertEquals(3, model1.getScore(player2));
    newBoard.get(0).get(0).placeDisc(Disc.WHITE);
    // the game state of the original model1 didn't change
    Assert.assertEquals(3, model1.getScore(player1));
    Assert.assertEquals(3, model1.getScore(player2));
  }

  @Test
  public void testScoreEarnedByPlaceInLegalMove() {
    //Valid move for player1 with WHITE disc
    Coordinates validCoordinates1 = new CubeCoordinates(1,-2);
    int actualScore = model1.scoreEarnedByPlaceIn(player1, validCoordinates1);
    Assert.assertEquals(1, actualScore);
  }

  @Test
  public void testScoreEarnedByPlaceInIllegalMove() {
    //Invalid move for player1 WHITE disc
    Coordinates validCoordinates1 = new CubeCoordinates(0,0);
    int actualScore = model1.scoreEarnedByPlaceIn(player1, validCoordinates1);
    Assert.assertEquals(0, actualScore);
  }


  @Test
  public void testMovable() {
    Assert.assertTrue(model1.movable(player1));
    // false because this is not player2's turn to play
    Assert.assertFalse(model1.movable(player2));

    Assert.assertTrue(model2.movable(player1));
    Assert.assertFalse(model2.movable(player2));
    model2.placeIn(player1, new CubeCoordinates(1, -2));
    Assert.assertFalse(model2.movable(player1));
    Assert.assertTrue(model2.movable(player2));
    model2.placeIn(player2, new CubeCoordinates(1, 1));
    model2.placeIn(player1, new CubeCoordinates(-1, 2));
    model2.placeIn(player2, new CubeCoordinates(-2, 1));
    model2.placeIn(player1, new CubeCoordinates(2, -1));
    model2.placeIn(player2, new CubeCoordinates(-1, -1));
    Assert.assertFalse(model2.movable(player1));
    Assert.assertFalse(model2.movable(player2));
  }

  @Test
  public void testGetScore() {
    Assert.assertEquals(3, model1.getScore(player1));
    Assert.assertEquals(3, model1.getScore(player2));

    Assert.assertEquals(3, model2.getScore(player1));
    Assert.assertEquals(3, model2.getScore(player2));
    model2.placeIn(player1, new CubeCoordinates(1, -2));
    Assert.assertEquals(5, model2.getScore(player1));
    Assert.assertEquals(2, model2.getScore(player2));
    model2.placeIn(player2, new CubeCoordinates(1, 1));
    Assert.assertEquals(4, model2.getScore(player1));
    Assert.assertEquals(4, model2.getScore(player2));
  }

  @Test
  public void testGameOver() {
    Assert.assertFalse(model1.isGameOver());
    model1.pass(player1);
    model1.pass(player2);
    Assert.assertTrue(model1.isGameOver());

    Assert.assertFalse(model2.isGameOver());
    model2.placeIn(player1, new CubeCoordinates(1, -2));
    model2.placeIn(player2, new CubeCoordinates(1, 1));
    model2.placeIn(player1, new CubeCoordinates(-1, 2));
    model2.placeIn(player2, new CubeCoordinates(-2, 1));
    model2.placeIn(player1, new CubeCoordinates(2, -1));
    model2.placeIn(player2, new CubeCoordinates(-1, -1));
    model2.pass(player1);
    model2.pass(player2);
    Assert.assertTrue(model2.isGameOver());
  }

  @Test
  public void testGetTurn() {
    Assert.assertEquals(player1, model1.getTurn());

    Assert.assertEquals(player1, model2.getTurn());
    model2.placeIn(player1, new CubeCoordinates(1, -2));
    Assert.assertEquals(player2, model2.getTurn());
    model2.placeIn(player2, new CubeCoordinates(1, 1));
    Assert.assertEquals(player1, model2.getTurn());
  }

  @Test
  public void testPass() {
    model1.pass(player1);
    Assert.assertEquals(player2, model1.getTurn());
  }

  @Test(expected = IllegalStateException.class)
  public void testPassWrongPlayer() {
    model1.pass(player2);
  }

  @Test(expected = IllegalStateException.class)
  public void testPassGameEnded() {
    model1.pass(player1);
    model1.pass(player2);
    model1.pass(player1);
  }


  @Test(expected = IllegalStateException.class)
  public void testPlaceInGameOver() {
    model1.pass(player1);
    model1.pass(player2);
    model1.placeIn(player1, new CubeCoordinates(1, -2));
  }

  @Test(expected = IllegalStateException.class)
  public void testPlaceInWrongPlayer() {
    model1.placeIn(player2, new CubeCoordinates(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceInSpaceOccupied() {
    model2.placeIn(player1, new CubeCoordinates(1, -2));
    model2.placeIn(player2, new CubeCoordinates(1, 1));
    model2.placeIn(player1, new CubeCoordinates(-1, 2));
    model2.placeIn(player2, new CubeCoordinates(1, -2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceInNoScoreEarnedIllegalPlace() {
    model2.placeIn(player1, new CubeCoordinates(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceInNoScoreEarnedOutOfBound() {
    model2.placeIn(player1, new CubeCoordinates(4, 4));
  }

  @Test
  public void testTryMove() {
    Assert.assertEquals(3, model2.getScore(player1));
    Assert.assertEquals(3, model2.getScore(player2));
    ReversiModelReadOnly newModel = model2.tryMove(player1, new CubeCoordinates(1, -2));
    // the game state of the original model2 didn't change
    Assert.assertEquals(3, model2.getScore(player1));
    Assert.assertEquals(3, model2.getScore(player2));
    Assert.assertEquals(5, newModel.getScore(player1));
    Assert.assertEquals(2, newModel.getScore(player2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMoveOutOfBound() {
    model2.tryMove(player1, new CubeCoordinates(4, 4));
  }

  @Test
  public void testStartGame() {
    StringBuilder out1 = new StringBuilder();
    StringBuilder out2 = new StringBuilder();
    MockHexReversiController controller1 = new MockHexReversiController(model1,
            new MockViewFrame(), player1, out1);
    MockHexReversiController controller2 = new MockHexReversiController(model1,
            new MockViewFrame(), player2, out2);
    this.model1.startGame();
    Assert.assertEquals(out1.toString(), "newTurn player should move");
    Assert.assertEquals(out2.toString(), "newTurn");
  }

  @Test
  public void testAddFeatures() {
    StringBuilder out = new StringBuilder();
    ModelFeatures features = new MockModelFeatures(out);
    this.model1.addFeatures(features);
    this.model1.startGame();
    Assert.assertEquals(out.toString(), "newTurn invoked\n");
  }

  @Test
  public void testGetDiscOf() {
    Assert.assertEquals(Disc.WHITE, this.model1.getDiscOf(this.player1));
    Assert.assertEquals(Disc.BLACK, this.model1.getDiscOf(this.player2));
  }

  @Test
  public void renderTextualView() {
    Assert.assertTrue(true);
    ReversiModelReadOnly model = new SquareReversi(6, player1, player2);
    SquareReversiTextualView view = new SquareReversiTextualView(model, System.out);
    view.render();
  }
}
