import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.controller.ReversiControllerImpl;
import cs3500.reversi.model.CubeCoordinates;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.model.RowColumnCoordinates;
import cs3500.reversi.model.SquareReversi;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.controller.MockHexReversiController;
import cs3500.reversi.view.MockViewFrame;
import cs3500.reversi.view.MockViewPanel;


/**
 * Tests for the HexReversiController class.
 * This class contains unit tests to verify the correct behavior of game controller actions.
 * It utilizes mock implementations of the game model and view to simulate different game scenarios.
 */
public class ControllerTests {
  private Player player1;
  private MockViewFrame mockFrame;
  private MockViewPanel mockPanel;
  private MockHexReversiController mockController;
  private ReversiControllerImpl squareController;

  @Before
  public void setup() {
    player1 = new HumanPlayer();
    Player player2 = new HumanPlayer();
    ReversiModelReadWrite model1 = new HexReversi(6, player1, player2);
    ReversiModelReadWrite model2 = new SquareReversi(4,player1,player2);
    mockFrame = new MockViewFrame();
    mockPanel = new MockViewPanel();
    mockController = new MockHexReversiController(model1,mockFrame,player1, new StringBuilder());
    squareController = new ReversiControllerImpl(model2, mockFrame, player1);
    model1.startGame();
  }

  @Test
  public void testGameEndsAction() {
    mockController.gameEnds();
    String expectedMessage = "Game ends!"; // Replace with the actual expected message
    Assert.assertEquals(expectedMessage, mockFrame.getLastMessage());
  }

  @Test
  public void testGameEndsActioninSquare() {
    squareController.gameEnds();
    String expectedMessage = "Game ends!"; // Replace with the actual expected message
    Assert.assertEquals(expectedMessage, mockFrame.getLastMessage());
  }

  @Test
  public void testMustPassAction() {
    mockController.mustPass(player1);
    Assert.assertFalse("Panel must pass state should be false in initialize model",
            mockPanel.getMustPassState());
  }

  @Test
  public void testMustPassActionInSquare() {
    squareController.mustPass(player1);
    Assert.assertFalse("Panel must pass state should be false in initialize model",
            mockPanel.getMustPassState());
  }

  @Test
  public void testNewTurnAction() {
    mockController.newTurn(player1);
    // Verify that the appropriate actions were taken.
    Assert.assertTrue("View should be refreshed on new turn", mockFrame.getIsRefresh());
  }

  @Test
  public void testNewTurnActionInSquare() {
    squareController.newTurn(player1);
    // Verify that the appropriate actions were taken.
    Assert.assertTrue("View should be refreshed on new turn", mockFrame.getIsRefresh());
  }

  @Test
  public void testPassAction() {
    mockPanel.addFeatures(mockController.getPlayerActionFeatures());
    mockPanel.triggerPassAction();
    // Verify that the pass action was triggered
    Assert.assertTrue("Must pass state should be true after pressing space",
            mockPanel.getMustPassState());
  }

  @Test
  public void testPassActionInSquare() {
    mockPanel.addFeatures(mockController.getPlayerActionFeatures());
    mockPanel.triggerPassAction();
    // Verify that the pass action was triggered
    Assert.assertTrue("Must pass state should be true after pressing space",
            mockPanel.getMustPassState());
  }

  @Test
  public void testPlaceInAction() {
    mockPanel.addFeatures(mockController.getPlayerActionFeatures());
    Coordinates coords = new CubeCoordinates(1, 2);
    mockPanel.setSelectedCoordinates(coords);
    mockPanel.triggerPlaceInAction(coords);
    // Verify that the placeIn action was triggered with correct coordinates
    Assert.assertEquals("Coordinates should match the selected ones", coords,
            mockPanel.lastPlacedInCoordinates);
  }

  @Test
  public void testPlaceInActionInSquare() {
    mockPanel.addFeatures(mockController.getPlayerActionFeatures());
    Coordinates coords = new RowColumnCoordinates(0, 1);
    mockPanel.setSelectedCoordinates(coords);
    mockPanel.triggerPlaceInAction(coords);
    // Verify that the placeIn action was triggered with correct coordinates
    Assert.assertEquals("Coordinates should match the selected ones", coords,
            mockPanel.lastPlacedInCoordinates);
  }

  @Test
  public void testSelectAction() {
    Coordinates coords = new CubeCoordinates(-1, 2);
    int expectedScore = 1;
    int actualScore = mockController.select(coords);
    // Assert that the returned score matches the expected score
    Assert.assertEquals("Score should match the expected score for given coordinates",
            expectedScore, actualScore);
  }

  @Test
  public void testSelectActionInSquare() {
    Coordinates coords = new RowColumnCoordinates(0,1);
    int expectedScore = 1;
    int actualScore = squareController.select(coords);
    // Assert that the returned score matches the expected score
    Assert.assertEquals("Score should match the expected score for given coordinates",
            expectedScore, actualScore);
  }

  @Test
  public void testInvalidPassAction() {
    mockController.pass();
    Coordinates illegalCoords = new CubeCoordinates(4, 5); // Example illegal coordinates
    mockController.placeIn(illegalCoords);
    // Assert that the appropriate message/state is set to reflect pass was not allowed
    String expectedMessage = "You can't move now!"; // Replace with the actual expected message
    Assert.assertEquals("Message should indicate that passing is not allowed",
            expectedMessage, mockFrame.getLastMessage());
  }

  @Test
  public void testInvalidPassActionInSquare() {
    squareController.pass();
    Coordinates illegalCoords = new RowColumnCoordinates(4, 5); // Example illegal coordinates
    squareController.placeIn(illegalCoords);
    // Assert that the appropriate message/state is set to reflect pass was not allowed
    String expectedMessage = "You can't move now!"; // Replace with the actual expected message
    Assert.assertEquals("Message should indicate that passing is not allowed",
            expectedMessage, mockFrame.getLastMessage());
  }

  @Test
  public void testInvalidPlaceInAction() {
    Coordinates illegalCoords = new CubeCoordinates(4, 5); // Example illegal coordinates
    mockController.placeIn(illegalCoords);
    // Assert that the model does not update and the view shows an error message
    String expectedMessage = "Illegal move!"; // Replace with the actual expected message
    Assert.assertEquals("Message should indicate that the move is illegal",
            expectedMessage, mockFrame.getLastMessage());
  }

  @Test
  public void testInvalidPlaceInActionInSquare() {
    Coordinates illegalCoords = new RowColumnCoordinates(4, 5); // Example illegal coordinates
    squareController.placeIn(illegalCoords);
    // Assert that the model does not update and the view shows an error message
    String expectedMessage = "Illegal move!"; // Replace with the actual expected message
    Assert.assertEquals("Message should indicate that the move is illegal",
            expectedMessage, mockFrame.getLastMessage());
  }

}
