import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.player.HumanPlayer;
import cs3500.reversi.player.Player;
import cs3500.reversi.model.CubeCoordinates;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.view.ReversiTextualView;

/**
 * Test class for Reversi Textual View.
 */
public class TextualViewTests {
  Player player1;
  Player player2;
  ReversiModelReadWrite model;
  Appendable out;
  ReversiTextualView view;

  @Before
  public void setUp() {
    this.player1 = new HumanPlayer();
    this.player2 = new HumanPlayer();
    this.model = new HexReversi(3, this.player1, this.player2);
    this.out = new StringBuilder();
    this.view = new ReversiTextualView(this.model, this.out);
  }

  @Test
  public void testInitialBoard() {
    this.view.render();
    Assert.assertEquals("  _ _ _\n _ X O _\n_ O _ X _\n _ X O _\n  _ _ _",
            this.out.toString());
  }

  @Test
  public void testBoardAfterPlacingAValidDisc() {
    this.model.placeIn(this.player1, new CubeCoordinates(1, -2));
    this.view.render();
    Assert.assertEquals("  _ O _\n _ O O _\n_ O _ X _\n _ X O _\n  _ _ _",
            this.out.toString());
  }

  @Test
  public void testBoardAfterPlacingInvalidDisc() {
    // the rendering of the initial board
    this.view.render();
    Assert.assertEquals("  _ _ _\n _ X O _\n_ O _ X _\n _ X O _\n  _ _ _",
            this.out.toString());

    // try an invalid move
    try {
      this.model.placeIn(this.player1, new CubeCoordinates(0, -2));
    } catch (IllegalArgumentException e) {
      // purposefully ignore because this is not the point of this test
    }

    // there should be no change in the view
    this.view.render();
    Assert.assertEquals("  _ _ _\n _ X O _\n_ O _ X _\n _ X O _\n  _ _ _"
                    + "  _ _ _\n _ X O _\n_ O _ X _\n _ X O _\n  _ _ _",
            this.out.toString());
  }


}
