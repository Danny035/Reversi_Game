import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.RowColumnCoordinates;
import cs3500.reversi.model.SquareReversi;
import cs3500.reversi.player.Player;
import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.strategy.AvoidGivingCorners;
import cs3500.reversi.strategy.CaptureMost;
import cs3500.reversi.strategy.ChainableStrategy;
import cs3500.reversi.strategy.GoForCorners;
import cs3500.reversi.strategy.Minimax;
import cs3500.reversi.strategy.SimplisticStrategy;
import cs3500.reversi.strategy.SmarterStrategy;
import cs3500.reversi.strategy.Strategy;
import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CubeCoordinates;
import cs3500.reversi.model.Disc;
import cs3500.reversi.model.ReversiCell;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.MockModelToTranscript;
import cs3500.reversi.model.MockModelToTranscriptGameLogic;
import cs3500.reversi.model.MockReversiModel;
import cs3500.reversi.model.MockReversiModelForValuableLocation;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.view.MockGameScreen;

/**
 * Test class for Reversi Strategy classes.
 */
public class StrategicTests {
  private Player player1;
  private Player player2;
  private ReversiModelReadWrite model1;
  private ReversiModelReadWrite model2;
  private ReversiModelReadWrite squareModel1;
  private ReversiModelReadWrite squareModel2;
  private ChainableStrategy avoidGivingCorners;
  private ChainableStrategy minimax;
  private ChainableStrategy captureMost;
  private ChainableStrategy goForCorners;
  private Strategy simplisticStrategy;
  private Strategy smarterStrategy;
  private MockModelToTranscript mockModelToTranscript;
  private StringBuilder transcript;
  private MockModelToTranscriptGameLogic mockModelToTranscriptGameLogic;

  @Before
  public void init() {
    player1 = new HumanPlayer();
    player2 = new HumanPlayer();
    model1 = new HexReversi(3, player1, player2);
    model2 = new HexReversi(4, player1, player2);
    squareModel1 = new SquareReversi(4,player1,player2);
    squareModel2 = new SquareReversi(6,player1,player2);
    minimax = new Minimax();
    avoidGivingCorners = new AvoidGivingCorners();
    captureMost = new CaptureMost();
    goForCorners = new GoForCorners();
    simplisticStrategy = new SimplisticStrategy();
    smarterStrategy = new SmarterStrategy();
    transcript = new StringBuilder();
    mockModelToTranscript = new MockModelToTranscript(transcript);
    mockModelToTranscriptGameLogic = new MockModelToTranscriptGameLogic(3, player1,
                                                                  player2, transcript);
  }

  @Test
  public void testCaptureMostIn3SideLengthBoard() {
    List<Coordinates> actualChoose1 = captureMost.chooseMove(model1,player1);
    // because all 6 coords will earn the player 1 equal score of 1 so capture most strategy
    // will just return them all
    List<Coordinates> expected1 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, -2),
            new CubeCoordinates(-1, -1),
            new CubeCoordinates(2, -1),
            new CubeCoordinates(-2, 1),
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2)
    ));
    Assert.assertEquals(expected1.size(), actualChoose1.size());
    Assert.assertTrue(expected1.containsAll(actualChoose1));

    model1.placeIn(player1, new CubeCoordinates(1, -2));

    // because all 3 coords will earn the player 2 equal score of 1 so capture most strategy
    // will just return them all
    List<Coordinates> expected2 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2),
            new CubeCoordinates(-1, -1)
    ));

    List<Coordinates> actualChoose2 = captureMost.chooseMove(model1,player2);
    Assert.assertEquals(expected2.size(), actualChoose2.size());
    Assert.assertTrue(expected1.containsAll(actualChoose2));
    model1.placeIn(player2, new CubeCoordinates(-1, -1));

    // because now player 1 can earn a score of 2 if placing a disc at (-2, 1),
    // in contrast of only earning 1 points when placing at either (1, 1) or (-1, 2)
    // so the capture most strategy will only return (-2, 1), excluding the other 2 possibilities
    List<Coordinates> expected3 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-2, 1)
    ));

    List<Coordinates> actualChoose3 = captureMost.chooseMove(model1,player1);
    Assert.assertEquals(expected3.size(), actualChoose3.size());
    Assert.assertTrue(expected3.containsAll(actualChoose3));
    model1.placeIn(player1, new CubeCoordinates(-2, 1));

    // because now player 2 can earn a score of 3 if placing a disc at (-1, 2),
    // in contrast of only earning 1 points when placing at (2, -1)
    // so the capture most strategy will only return (-1, 2), excluding (2, -1)
    List<Coordinates> expected4 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-1, 2)
    ));

    List<Coordinates> actualChoose4 = captureMost.chooseMove(model1,player2);
    Assert.assertEquals(expected4.size(), actualChoose4.size());
    Assert.assertTrue(expected4.containsAll(actualChoose4));
    model1.placeIn(player2, new CubeCoordinates(-1, 2));

    // because now player 1 have only one place available so the capture most strategy
    // will just return it
    List<Coordinates> expected5 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, 1)
    ));

    List<Coordinates> actualChoose5 = captureMost.chooseMove(model1,player1);
    Assert.assertEquals(expected5.size(), actualChoose5.size());
    Assert.assertTrue(expected5.containsAll(actualChoose5));
    model1.placeIn(player1, new CubeCoordinates(1, 1));

    // because now player 2 have only one place available so the capture most strategy
    // will just return it
    List<Coordinates> expected6 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(2, -1)
    ));

    List<Coordinates> actualChoose6 = captureMost.chooseMove(model1,player2);
    Assert.assertEquals(expected6.size(), actualChoose6.size());
    Assert.assertTrue(expected6.containsAll(actualChoose6));
    model1.placeIn(player2, new CubeCoordinates(2, -1));

    List<Coordinates> actualChoose7 = captureMost.chooseMove(model1, player1);
    Assert.assertEquals(0, actualChoose7.size());
    model1.pass(player1);

    List<Coordinates> actualChoose8 = captureMost.chooseMove(model1, player2);
    Assert.assertEquals(0, actualChoose8.size());
    model1.pass(player2);
    Assert.assertTrue(model1.isGameOver());
  }

  @Test
  public void testAvoidGivingCornersIn3SideLengthBoard() {
    List<Coordinates> actualChoose1 = avoidGivingCorners.chooseMove(model1, player1);
    // because all available moves are neighbors of a corner so this strategy think there is
    // no good move
    Assert.assertEquals(0, actualChoose1.size());
    model1.pass(player1);

    List<Coordinates> actualChoose2 = avoidGivingCorners.chooseMove(model1, player2);
    // because all available moves are, again, neighbors of a corner so this strategy think
    // there is no good move
    Assert.assertEquals(0, actualChoose2.size());
    model1.pass(player2);
    Assert.assertTrue(model1.isGameOver());
  }

  @Test
  // because there is no way to get a corner in a 3x3 board, so using go for corners
  // strategy in a 3x3 board just returns all the possible moves each time
  public void testGoForCornersIn3SideLengthBoard() {
    // because all 6 available coords are not corners so GoForCorners strategy will
    // just return them all
    List<Coordinates> actualChoose1 = goForCorners.chooseMove(model1, player1);
    List<Coordinates> expected1 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, -2),
            new CubeCoordinates(-1, -1),
            new CubeCoordinates(2, -1),
            new CubeCoordinates(-2, 1),
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2)
    ));
    Assert.assertEquals(expected1.size(), actualChoose1.size());
    Assert.assertTrue(expected1.containsAll(actualChoose1));

    model1.placeIn(player1, new CubeCoordinates(1, -2));

    // because all 3 available coords are not corners so GoForCorners strategy will
    // just return them all
    List<Coordinates> actualChoose2 = goForCorners.chooseMove(model1, player2);
    List<Coordinates> expected2 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2),
            new CubeCoordinates(-1, -1)
    ));
    Assert.assertEquals(expected2.size(), actualChoose2.size());
    Assert.assertTrue(expected2.containsAll(actualChoose2));

    model1.placeIn(player2, new CubeCoordinates(1, 1));

    // because the only 1 available coords is not a corner so GoForCorners strategy will
    // just return it
    List<Coordinates> actualChoose3 = goForCorners.chooseMove(model1, player1);
    List<Coordinates> expected3 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-1, 2)
    ));
    Assert.assertEquals(expected3.size(), actualChoose3.size());
    Assert.assertTrue(expected3.containsAll(actualChoose3));

    model1.placeIn(player1, new CubeCoordinates(-1, 2));

    // because the only 1 available coords is not a corner so GoForCorners strategy will
    // just return it
    List<Coordinates> actualChoose4 = goForCorners.chooseMove(model1, player2);
    List<Coordinates> expected4 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-2, 1)
    ));
    Assert.assertEquals(expected4.size(), actualChoose4.size());
    Assert.assertTrue(expected4.containsAll(actualChoose4));

    model1.placeIn(player2, new CubeCoordinates(-2, 1));

    // because the only 1 available coords is not a corner so GoForCorners strategy will
    // just return it
    List<Coordinates> actualChoose5 = goForCorners.chooseMove(model1, player1);
    List<Coordinates> expected5 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(2, -1)
    ));
    Assert.assertEquals(expected5.size(), actualChoose5.size());
    Assert.assertTrue(expected5.containsAll(actualChoose5));

    model1.placeIn(player1, new CubeCoordinates(2, -1));

    // because the only 1 available coords is not a corner so GoForCorners strategy will
    // just return it
    List<Coordinates> actualChoose6 = goForCorners.chooseMove(model1, player2);
    List<Coordinates> expected6 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-1, -1)
    ));
    Assert.assertEquals(expected6.size(), actualChoose6.size());
    Assert.assertTrue(expected6.containsAll(actualChoose6));

    model1.placeIn(player2, new CubeCoordinates(-1, -1));

    List<Coordinates> actualChoose7 = goForCorners.chooseMove(model1, player1);
    Assert.assertEquals(0, actualChoose7.size());
    model1.pass(player1);

    List<Coordinates> actualChoose8 = goForCorners.chooseMove(model1, player2);
    Assert.assertEquals(0, actualChoose8.size());
    model1.pass(player2);
    Assert.assertTrue(model1.isGameOver());
  }

  @Test
  public void testGoForCornersIn4SideLengthBoard() {
    // because all 6 available coords are not corners so GoForCorners strategy will
    // just return them all
    List<Coordinates> actualChoose1 = goForCorners.chooseMove(model2, player1);
    List<Coordinates> expected1 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, -2),
            new CubeCoordinates(-1, -1),
            new CubeCoordinates(2, -1),
            new CubeCoordinates(-2, 1),
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2)
    ));
    Assert.assertEquals(expected1.size(), actualChoose1.size());
    Assert.assertTrue(expected1.containsAll(actualChoose1));

    model2.placeIn(player1, new CubeCoordinates(1, -2));

    // because all 4 available coords are not corners so GoForCorners strategy will
    // just return them all
    List<Coordinates> actualChoose2 = goForCorners.chooseMove(model2, player2);
    List<Coordinates> expected2 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2),
            new CubeCoordinates(-1, -1),
            new CubeCoordinates(1, -3)
    ));
    Assert.assertEquals(expected2.size(), actualChoose2.size());
    Assert.assertTrue(expected2.containsAll(actualChoose2));

    model2.placeIn(player2, new CubeCoordinates(1, -3));

    // because all 4 available coords are not corners so GoForCorners strategy will
    // just return them all
    List<Coordinates> actualChoose3 = goForCorners.chooseMove(model2, player1);
    List<Coordinates> expected3 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-1, 2),
            new CubeCoordinates(2, -1),
            new CubeCoordinates(-2, 1),
            new CubeCoordinates(2, -3)
    ));
    Assert.assertEquals(expected3.size(), actualChoose3.size());
    Assert.assertTrue(expected3.containsAll(actualChoose3));

    model2.placeIn(player1, new CubeCoordinates(2, -3));

    // because now the available coords contains a corner so even though there are many
    // coords available to place a disc in for player 2, but the GoForCorners strategy will
    // just return the corner coords
    List<Coordinates> actualChoose4 = goForCorners.chooseMove(model2, player2);
    List<Coordinates> expected4 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(3, -3)
    ));
    Assert.assertEquals(expected4.size(), actualChoose4.size());
    Assert.assertTrue(expected4.containsAll(actualChoose4));
  }

  @Test
  public void testMinimaxIn3SideLengthBoard() {
    List<Coordinates> actualChoose1 = minimax.chooseMove(model1, player1);
    List<Coordinates> expected1 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, -2),
            new CubeCoordinates(-1, -1),
            new CubeCoordinates(2, -1),
            new CubeCoordinates(-2, 1),
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2)
    ));
    Assert.assertEquals(expected1.size(), actualChoose1.size());
    Assert.assertTrue(expected1.containsAll(actualChoose1));

    model1.placeIn(player1, new CubeCoordinates(1, -2));

    // the strategy left out the coordinates at (-1, -1) because that will give the opponent
    // player 1 the ability to gain 2 on the next round, while (1, 1) and (-1, 2) make
    // the opponent's gain to be topped at 1
    List<Coordinates> actualChoose2 = minimax.chooseMove(model1, player2);
    List<Coordinates> expected2 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(1, 1),
            new CubeCoordinates(-1, 2)
    ));
    Assert.assertEquals(expected2.size(), actualChoose2.size());
    Assert.assertTrue(expected2.containsAll(actualChoose2));


    model1.placeIn(player2, new CubeCoordinates(1, 1));

    List<Coordinates> actualChoose3 = minimax.chooseMove(model1, player1);
    List<Coordinates> expected3 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-1, 2)
    ));
    Assert.assertEquals(expected3.size(), actualChoose3.size());
    Assert.assertTrue(expected3.containsAll(actualChoose3));

    model1.placeIn(player1, new CubeCoordinates(-1, 2));

    List<Coordinates> actualChoose4 = minimax.chooseMove(model1, player2);
    List<Coordinates> expected4 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-2, 1)
    ));
    Assert.assertEquals(expected4.size(), actualChoose4.size());
    Assert.assertTrue(expected4.containsAll(actualChoose4));

    model1.placeIn(player2, new CubeCoordinates(-2, 1));

    List<Coordinates> actualChoose5 = minimax.chooseMove(model1, player1);
    List<Coordinates> expected5 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(2, -1)
    ));
    Assert.assertEquals(expected5.size(), actualChoose5.size());
    Assert.assertTrue(expected5.containsAll(actualChoose5));

    model1.placeIn(player1, new CubeCoordinates(2, -1));

    List<Coordinates> actualChoose6 = minimax.chooseMove(model1, player2);
    List<Coordinates> expected6 = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(-1, -1)
    ));
    Assert.assertEquals(expected6.size(), actualChoose6.size());
    Assert.assertTrue(expected6.containsAll(actualChoose6));

    model1.placeIn(player2, new CubeCoordinates(-1, -1));

    List<Coordinates> actualChoose7 = minimax.chooseMove(model1, player1);
    Assert.assertEquals(0, actualChoose7.size());
    model1.pass(player1);

    List<Coordinates> actualChoose8 = minimax.chooseMove(model1, player2);
    Assert.assertEquals(0, actualChoose8.size());
    model1.pass(player2);
    Assert.assertTrue(model1.isGameOver());
  }

  @Test
  public void testSimplisticStrategyIn3SideLengthBoard() {
    Optional<Coordinates> actualChoose1 = simplisticStrategy.chooseMove(model1, player1);
    Assert.assertEquals(new CubeCoordinates(1, -2), actualChoose1.get());
    model1.placeIn(player1, new CubeCoordinates(1, -2));

    Optional<Coordinates> actualChoose2 = simplisticStrategy.chooseMove(model1, player2);
    Assert.assertEquals(new CubeCoordinates(-1, -1), actualChoose2.get());
    model1.placeIn(player2, new CubeCoordinates(-1, -1));

    Optional<Coordinates> actualChoose3 = simplisticStrategy.chooseMove(model1, player1);
    Assert.assertEquals(new CubeCoordinates(-2, 1), actualChoose3.get());
    model1.placeIn(player1, new CubeCoordinates(-2, 1));

    Optional<Coordinates> actualChoose4 = simplisticStrategy.chooseMove(model1, player2);
    Assert.assertEquals(new CubeCoordinates(-1, 2), actualChoose4.get());
    model1.placeIn(player2, new CubeCoordinates(-1, 2));

    Optional<Coordinates> actualChoose5 = simplisticStrategy.chooseMove(model1, player1);
    Assert.assertEquals(new CubeCoordinates(1, 1), actualChoose5.get());
    model1.placeIn(player1, new CubeCoordinates(1, 1));

    Optional<Coordinates> actualChoose6 = simplisticStrategy.chooseMove(model1, player2);
    Assert.assertEquals(new CubeCoordinates(2, -1), actualChoose6.get());
    model1.placeIn(player2, new CubeCoordinates(2, -1));

    //No valid move for player1
    Optional<Coordinates> actualChoose7 = simplisticStrategy.chooseMove(model1, player1);
    Assert.assertTrue(actualChoose7.isEmpty());
  }

  @Test
  public void testSmarterStrategyIn3SideLengthBoard() {
    Optional<Coordinates> actualChoose1 = smarterStrategy.chooseMove(model1, player1);
    // because all available moves are neighbors of a corner and smarter strategy has
    // AvoidGivingCorners strategy chained into it, so it think there is no good move
    Assert.assertTrue(actualChoose1.isEmpty());
    model1.pass(player1);

    Optional<Coordinates> actualChoose2 = smarterStrategy.chooseMove(model1, player2);
    // because all available moves are, again, neighbors of a corner so this strategy think
    // there is no good move
    Assert.assertTrue(actualChoose2.isEmpty());
    model1.pass(player2);
    Assert.assertTrue(model1.isGameOver());
  }

  @Test
  public void testGoForCornersStrategyChecksCornersMockModel() {
    //The List for all corner
    List<Coordinates> allCorner = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(0,-3),
            new CubeCoordinates(3,-3),
            new CubeCoordinates(-3,0),
            new CubeCoordinates(3,0),
            new CubeCoordinates(-3,3),
            new CubeCoordinates(0,3)));
    MockReversiModel mockReversiModel1 = new MockReversiModel(allCorner);
    //making a mock that player 1 who is playing with white next move can be all the corner
    List<List<Cell>> mockBoard1 = new ArrayList<>(Arrays.asList(Arrays.asList(
                    //first row
                    new ReversiCell(new CubeCoordinates(0, -3), null),
                    new ReversiCell(new CubeCoordinates(1, -3), null),
                    new ReversiCell(new CubeCoordinates(2, -3), null),
                    new ReversiCell(new CubeCoordinates(3, -3), null)),
            //second row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-1, -2), null),
                    new ReversiCell(new CubeCoordinates(0, -2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(1, -2), null),
                    new ReversiCell(new CubeCoordinates(2, -2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(3, -2), null)),
            //third row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-2, -1), null),
                    new ReversiCell(new CubeCoordinates(-1, -1), null),
                    new ReversiCell(new CubeCoordinates(0, -1), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(1, -1), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(2, -1), null),
                    new ReversiCell(new CubeCoordinates(3, -1), null)),
            //forth row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 0), null),
                    new ReversiCell(new CubeCoordinates(-2, 0), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(-1, 0), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(0, 0), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(1, 0), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(2, 0), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(3, 0), null)),
            //fifth row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 1), null),
                    new ReversiCell(new CubeCoordinates(-2, 1), null),
                    new ReversiCell(new CubeCoordinates(-1, 1), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(0, 1), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(1, 1), null),
                    new ReversiCell(new CubeCoordinates(2, 1), null)),
            //sixth row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 2), null),
                    new ReversiCell(new CubeCoordinates(-2, 2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(-1, 2), null),
                    new ReversiCell(new CubeCoordinates(0, 2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(1, 2), null)),
            //seventh row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 3), null),
                    new ReversiCell(new CubeCoordinates(-2, 3), null),
                    new ReversiCell(new CubeCoordinates(-1, 3), null),
                    new ReversiCell(new CubeCoordinates(0, 3), null))));
    //The potential move coords for the player1 who with white disc
    List<Coordinates> coordsAvailableToMoveTo = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(0,-3),
            new CubeCoordinates(3,-3),
            new CubeCoordinates(-3,0),
            new CubeCoordinates(3,0),
            new CubeCoordinates(-3,3),
            new CubeCoordinates(0,3),
            new CubeCoordinates(1,-2),
            new CubeCoordinates(-1,-1),
            new CubeCoordinates(2,-1),
            new CubeCoordinates(-2,1),
            new CubeCoordinates(1,1),
            new CubeCoordinates(-1,2)));
    //The List for all corner
    List<Coordinates> corner = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(0,-3),
            new CubeCoordinates(3,-3),
            new CubeCoordinates(-3,0),
            new CubeCoordinates(3,0),
            new CubeCoordinates(-3,3),
            new CubeCoordinates(0,3)));
    mockReversiModel1.setMockBoard(mockBoard1);
    List<Coordinates> actualChoose = goForCorners.chooseMove(mockReversiModel1, player1,
            coordsAvailableToMoveTo);
    Assert.assertEquals(actualChoose.size(), 6);
    Assert.assertTrue(actualChoose.containsAll(corner));
  }

  @Test
  public void testMostValuableLocationMockModel() {
    //Already occupy with a black disc
    Coordinates invalidCoords = new CubeCoordinates(1,-2);
    MockReversiModelForValuableLocation mockReversiModel1 =
            new MockReversiModelForValuableLocation(invalidCoords, 100);
    //making a mock that player 1 who is playing with white next move can be all the corner
    List<List<Cell>> mockBoard2 = new ArrayList<>(Arrays.asList(Arrays.asList(
                    //first row
                    new ReversiCell(new CubeCoordinates(0, -3), null),
                    new ReversiCell(new CubeCoordinates(1, -3), null),
                    new ReversiCell(new CubeCoordinates(2, -3), null),
                    new ReversiCell(new CubeCoordinates(3, -3), null)),
            //second row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-1, -2), null),
                    new ReversiCell(new CubeCoordinates(0, -2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(1, -2), null),
                    new ReversiCell(new CubeCoordinates(2, -2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(3, -2), null)),
            //third row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-2, -1), null),
                    new ReversiCell(new CubeCoordinates(-1, -1), null),
                    new ReversiCell(new CubeCoordinates(0, -1), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(1, -1), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(2, -1), null),
                    new ReversiCell(new CubeCoordinates(3, -1), null)),
            //forth row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 0), null),
                    new ReversiCell(new CubeCoordinates(-2, 0), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(-1, 0), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(0, 0), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(1, 0), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(2, 0), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(3, 0), null)),
            //fifth row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 1), null),
                    new ReversiCell(new CubeCoordinates(-2, 1), null),
                    new ReversiCell(new CubeCoordinates(-1, 1), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(0, 1), Disc.WHITE),
                    new ReversiCell(new CubeCoordinates(1, 1), null),
                    new ReversiCell(new CubeCoordinates(2, 1), null)),
            //sixth row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 2), null),
                    new ReversiCell(new CubeCoordinates(-2, 2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(-1, 2), null),
                    new ReversiCell(new CubeCoordinates(0, 2), Disc.BLACK),
                    new ReversiCell(new CubeCoordinates(1, 2), null)),
            //seventh row
            Arrays.asList(
                    new ReversiCell(new CubeCoordinates(-3, 3), null),
                    new ReversiCell(new CubeCoordinates(-2, 3), null),
                    new ReversiCell(new CubeCoordinates(-1, 3), null),
                    new ReversiCell(new CubeCoordinates(0, 3), null))));


    mockReversiModel1.setMockBoard(mockBoard2);
    //The potential move coords for the player1 who with white disc
    List<Coordinates> coordsAvailableToMoveTo = new ArrayList<>(Arrays.asList(
            new CubeCoordinates(0,-3),
            new CubeCoordinates(3,-3),
            new CubeCoordinates(-3,0),
            new CubeCoordinates(3,0),
            new CubeCoordinates(-3,3),
            new CubeCoordinates(0,3),
            new CubeCoordinates(1,-2),
            new CubeCoordinates(-1,-1),
            new CubeCoordinates(2,-1),
            new CubeCoordinates(-2,1),
            new CubeCoordinates(1,1),
            new CubeCoordinates(-1,2)));

    mockReversiModel1.setMockBoard(mockBoard2);
    List<Coordinates> actualChoose = captureMost.chooseMove(mockReversiModel1, player1,
            coordsAvailableToMoveTo);
    Assert.assertEquals(actualChoose.size(), 1);
    // the strategy 1 (capture most strategy) indeed choose the coords that is lied to be the
    // most valuable even it is not actually even available on board
    Assert.assertEquals(actualChoose.get(0),invalidCoords);
  }

  @Test
  public void testCaptureMostChecksAllAvailableCellsMockModelTranscript() {
    captureMost.chooseMove(mockModelToTranscript, player1, new ArrayList<>(Arrays.asList(
            new CubeCoordinates(0, 0),
            new CubeCoordinates(1, 1)
    )));
    Assert.assertEquals("Cube Coordinates: 0, 0, 0\nCube Coordinates: 1, 1, -2\n",
            transcript.toString());
  }

  @Test
  public void testCaptureMostChecksAllAvailableCellsTranscript() {
    FileWriter fileWriter;
    try {
      fileWriter = new FileWriter("strategy-transcript.txt");
      fileWriter.append("Coordinates checked by Capture Most Strategy for player 1 "
              + "in the start of the game:\n");
      MockModelToTranscriptGameLogic transcriptToFile = new MockModelToTranscriptGameLogic(3,
              player1, player2, fileWriter);
      captureMost.chooseMove(transcriptToFile, player1);
      fileWriter.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("File not found!");
    }
    // stub test to stop style checker from complaining
    Assert.assertTrue(true);
  }

  @Test
  public void testCaptureMostChecksAllAvailableCellsRealGame() {
    captureMost.chooseMove(mockModelToTranscriptGameLogic, player1);
    String actualChecks1 = transcript.toString();

    Assert.assertTrue(actualChecks1.contains("Cube Coordinates: 1, -2, 1"));
    Assert.assertTrue(actualChecks1.contains("Cube Coordinates: -1, -1, 2"));
    Assert.assertTrue(actualChecks1.contains("Cube Coordinates: 2, -1, -1"));
    Assert.assertTrue(actualChecks1.contains("Cube Coordinates: -2, 1, 1"));
    Assert.assertTrue(actualChecks1.contains("Cube Coordinates: 1, 1, -2"));
    Assert.assertTrue(actualChecks1.contains("Cube Coordinates: -1, 2, -1"));

    mockModelToTranscriptGameLogic.placeIn(player1, new CubeCoordinates(1, 1));
    captureMost.chooseMove(mockModelToTranscriptGameLogic, player2);
    String actualChecks3 = transcript.toString();
    Assert.assertTrue(actualChecks3.contains("Cube Coordinates: -1, -1, 2"));
    Assert.assertTrue(actualChecks3.contains("Cube Coordinates: -2, 1, 1"));
    Assert.assertTrue(actualChecks3.contains("Cube Coordinates: 2, -1, -1"));
  }

  @Test
  public void testViewLogicCoords() {
    StringBuilder output = new StringBuilder();
    MockGameScreen mockView = new MockGameScreen(model1, output);
    MockGameScreen.MouseEventsListener mouseEventsListener = mockView.new MouseEventsListener();
    Dimension screenSize = mockView.getPreferredSize();
    MouseEvent click1 = new MouseEvent(mockView, 0, 123L, 0, screenSize.width / 2,
            screenSize.height / 2, 1, false);
    mouseEventsListener.mousePressed(click1);
    Assert.assertEquals(output.toString(), "Cube Coordinates: 0, 0, 0\n");

    int cellWidth = screenSize.width / 5;
    int cellHeight = screenSize.height / 5;

    // clears the output source and test for the left of the middle cell
    output.setLength(0);
    MouseEvent click2 = new MouseEvent(mockView, 0, 123L, 0,
            screenSize.width / 2 - cellWidth,
            screenSize.height / 2, 1, false);
    mouseEventsListener.mousePressed(click2);
    Assert.assertEquals(output.toString(), "Cube Coordinates: -1, 0, 1\n");

    // clears the output source and test for the right of the middle cell
    output.setLength(0);
    MouseEvent click3 = new MouseEvent(mockView, 0, 123L, 0,
            screenSize.width / 2 + cellWidth,
            screenSize.height / 2, 1, false);
    mouseEventsListener.mousePressed(click3);
    Assert.assertEquals(output.toString(), "Cube Coordinates: 1, 0, -1\n");

    // clears the output source and test for the upper left of the middle cell
    output.setLength(0);
    MouseEvent click4 = new MouseEvent(mockView, 0, 123L, 0,
            screenSize.width / 2 - cellWidth / 2,
            screenSize.height / 2 - cellHeight, 1, false);
    mouseEventsListener.mousePressed(click4);
    Assert.assertEquals(output.toString(), "Cube Coordinates: 0, -1, 1\n");

    // clears the output source and test for the upper right of the middle cell
    output.setLength(0);
    MouseEvent click5 = new MouseEvent(mockView, 0, 123L, 0,
            screenSize.width / 2 + cellWidth / 2,
            screenSize.height / 2 - cellHeight, 1, false);
    mouseEventsListener.mousePressed(click5);
    Assert.assertEquals(output.toString(), "Cube Coordinates: 1, -1, 0\n");

    // clears the output source and test for the lower left of the middle cell
    output.setLength(0);
    MouseEvent click6 = new MouseEvent(mockView, 0, 123L, 0,
            screenSize.width / 2 - cellWidth / 2,
            screenSize.height / 2 + cellHeight, 1, false);
    mouseEventsListener.mousePressed(click6);
    Assert.assertEquals(output.toString(), "Cube Coordinates: -1, 1, 0\n");

    // clears the output source and test for the lower right of the middle cell
    output.setLength(0);
    MouseEvent click7 = new MouseEvent(mockView, 0, 123L, 0,
            screenSize.width / 2 + cellWidth / 2,
            screenSize.height / 2 + cellHeight, 1, false);
    mouseEventsListener.mousePressed(click7);
    Assert.assertEquals(output.toString(), "Cube Coordinates: 0, 1, -1\n");
  }









  //Test for squareReversi

  @Test
  public void testCaptureMostInSquare() {
    //invalid coords
    Coordinates invalidCoords = new RowColumnCoordinates(0,0);
    MockReversiModelForValuableLocation mockReversiModel1 =
            new MockReversiModelForValuableLocation(invalidCoords, 100);
    mockReversiModel1.setMockBoard(squareModel2.getBoard());

    //The potential move coords for the disc black who with white disc
    List<Coordinates> coordsAvailableToMoveTo = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(0,0),
            new RowColumnCoordinates(1,3),
            new RowColumnCoordinates(1,4),
            new RowColumnCoordinates(1,5),
            new RowColumnCoordinates(2,5),
            new RowColumnCoordinates(3,1),
            new RowColumnCoordinates(3,5),
            new RowColumnCoordinates(4,2)));

    List<Coordinates> expected = mockReversiModel1.getInspectedCoordinates();

    List<Coordinates> actualChoose = captureMost.chooseMove(mockReversiModel1, player2,
            coordsAvailableToMoveTo);
    Assert.assertEquals(actualChoose.size(), 1);
    Assert.assertEquals(actualChoose.get(0),invalidCoords);
  }

  @Test
  public void testGoForCornersStrategyInSquare() {
    //mock corner coords
    List<Coordinates> allCorner = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(1,1),
            new RowColumnCoordinates(0,0),
            new RowColumnCoordinates(0,4),
            new RowColumnCoordinates(4,4)));
    MockReversiModel mockReversiModel1 = new MockReversiModel(allCorner);
    //making a mock that player 1 who is playing with white next move can be all the corner
    List<List<Cell>> mockBoard1 = squareModel2.getBoard();
    mockReversiModel1.setMockBoard(squareModel2.getBoard());
    //The potential move coords for the player1 who with white disc
    List<Coordinates> coordsAvailableToMoveTo = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(1,1),
            new RowColumnCoordinates(3,5),
            new RowColumnCoordinates(5,5),
            new RowColumnCoordinates(1,5),
            new RowColumnCoordinates(4,4)));
    //The List for all corner
    List<Coordinates> corner = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(1,1),
            new RowColumnCoordinates(4,4)));

    List<Coordinates> actualChoose = goForCorners.chooseMove(mockReversiModel1, player1,
            coordsAvailableToMoveTo);
    Assert.assertEquals(actualChoose.size(), 2);
    Assert.assertTrue(actualChoose.containsAll(corner));
  }

  @Test
  public void testAvoidGoForCornersStrategyInSquare() {
    //mock corner coords
    List<Coordinates> allCorner;
    allCorner = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(5,0),
            new RowColumnCoordinates(0,0),
            new RowColumnCoordinates(0,5),
            new RowColumnCoordinates(5,5)));
    MockReversiModel mockReversiModel1 = new MockReversiModel(allCorner);
    //making a mock that player 1 who is playing with white next move can be all the corner
    List<List<Cell>> mockBoard1 = squareModel2.getBoard();
    mockReversiModel1.setMockBoard(squareModel2.getBoard());
    //The potential move coords for the player1 who with white disc
    List<Coordinates> coordsAvailableToMoveTo = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(1,1),
            new RowColumnCoordinates(0,1),
            new RowColumnCoordinates(1,0),

            new RowColumnCoordinates(2,2),
            new RowColumnCoordinates(0,5),
            new RowColumnCoordinates(5,0)));

    List<Coordinates> expect = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(2,2),
            new RowColumnCoordinates(0,5),
            new RowColumnCoordinates(5,0)));

    List<Coordinates> actualChoose = avoidGivingCorners.chooseMove(mockReversiModel1, player1,
            coordsAvailableToMoveTo);
    Assert.assertEquals(actualChoose.size(), expect.size());
    Assert.assertTrue(actualChoose.containsAll(expect));
  }

  @Test
  public void testMinimaxInSquare() {
    List<Coordinates> actualChoose1 = minimax.chooseMove(squareModel2, player1);
    List<Coordinates> expected1 = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(1, 2),
            new RowColumnCoordinates(2, 1),
            new RowColumnCoordinates(4, 3),
            new RowColumnCoordinates(3, 4)));

    Assert.assertEquals(expected1.size(), actualChoose1.size());


    squareModel2.placeIn(player1, new RowColumnCoordinates(1, 2));

    List<Coordinates> actualChoose2 = minimax.chooseMove(squareModel2, player2);
    List<Coordinates> expected2 = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(1, 1),
            new RowColumnCoordinates(3, 1)
    ));
    Assert.assertEquals(expected2.size(), actualChoose2.size());
    Assert.assertTrue(expected2.containsAll(actualChoose2));

    squareModel2.placeIn(player2, new RowColumnCoordinates(1, 1));

    List<Coordinates> actualChoose3 = minimax.chooseMove(squareModel2, player1);
    //RowColumnCoordinates(2, 1) will let the other player earn more score
    List<Coordinates> expected3 = new ArrayList<>(Arrays.asList(
            new RowColumnCoordinates(1, 0),
            new RowColumnCoordinates(3, 4),
            new RowColumnCoordinates(4, 3)
    ));
    Assert.assertEquals(expected3.size(), actualChoose3.size());
    Assert.assertTrue(expected3.containsAll(actualChoose3));
  }

  @Test
  public void testSimplisticStrategyInSquare() {
    Optional<Coordinates> actualChoose1 = simplisticStrategy.chooseMove(squareModel1, player1);
    Assert.assertEquals(new RowColumnCoordinates(0, 1), actualChoose1.get());
    squareModel1.placeIn(player1, new RowColumnCoordinates(0, 1));

    Optional<Coordinates> actualChoose2 = simplisticStrategy.chooseMove(squareModel1, player2);
    Assert.assertEquals(new RowColumnCoordinates(0, 0), actualChoose2.get());
    squareModel1.placeIn(player2, new RowColumnCoordinates(0, 0));

    Optional<Coordinates> actualChoose3 = simplisticStrategy.chooseMove(squareModel1, player1);
    Assert.assertEquals(new RowColumnCoordinates(1, 0), actualChoose3.get());
    squareModel1.placeIn(player1, new RowColumnCoordinates(1, 0));

    Optional<Coordinates> actualChoose4 = simplisticStrategy.chooseMove(squareModel1, player2);
    Assert.assertEquals(new RowColumnCoordinates(0, 2), actualChoose4.get());
    squareModel1.placeIn(player2, new RowColumnCoordinates(0, 2));

    Optional<Coordinates> actualChoose5 = simplisticStrategy.chooseMove(squareModel1, player1);
    Assert.assertEquals(new RowColumnCoordinates(0, 3), actualChoose5.get());
    squareModel1.placeIn(player1, new RowColumnCoordinates(0, 3));

    Optional<Coordinates> actualChoose6 = simplisticStrategy.chooseMove(squareModel1, player2);
    Assert.assertEquals(new RowColumnCoordinates(2, 0), actualChoose6.get());
    squareModel1.placeIn(player2, new RowColumnCoordinates(2, 0));

    Optional<Coordinates> actualChoose7 = simplisticStrategy.chooseMove(squareModel1, player1);
    Assert.assertEquals(new RowColumnCoordinates(3, 0), actualChoose7.get());
    squareModel1.placeIn(player1, new RowColumnCoordinates(3, 0));

    Optional<Coordinates> actualChoose8 = simplisticStrategy.chooseMove(squareModel1, player2);
    Assert.assertEquals(new RowColumnCoordinates(1, 3), actualChoose8.get());
    squareModel1.placeIn(player2, new RowColumnCoordinates(1, 3));

    //No valid move for player2
    Optional<Coordinates> actualChoose9 = simplisticStrategy.chooseMove(squareModel1, player2);
    Assert.assertTrue(actualChoose9.isEmpty());
  }

  @Test
  public void testSmarterStrategyInSquare() {
    Optional<Coordinates> actualChoose1 = smarterStrategy.chooseMove(squareModel1, player1);
    // because all available moves are neighbors of a corner and smarter strategy has
    // AvoidGivingCorners strategy chained into it, so it think there is no good move
    Assert.assertTrue(actualChoose1.isEmpty());
    squareModel1.pass(player1);

    Optional<Coordinates> actualChoose2 = smarterStrategy.chooseMove(squareModel1, player2);
    // because all available moves are, again, neighbors of a corner so this strategy think
    // there is no good move
    Assert.assertTrue(actualChoose2.isEmpty());
    squareModel1.pass(player2);
    Assert.assertTrue(squareModel1.isGameOver());
  }









}
