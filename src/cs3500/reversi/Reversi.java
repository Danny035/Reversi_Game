package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.controller.ReversiControllerImpl;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.ModelCreator;
import cs3500.reversi.model.ReversiModelReadWrite;
import cs3500.reversi.player.Player;
import cs3500.reversi.player.PlayerCreator;
import cs3500.reversi.view.ReversiGUI;

/**
 * The entry point for the Reversi game application.
 * Initializes the game model, creates the graphical user interface, and starts the game.
 * The main method sets up a Reversi game with two players and a specified board size.
 * It also allows for the possibility to simulate an intermediate state of the game
 * by executing a series of predetermined moves. This can be used for testing or
 * demonstrating specific scenarios within the game.
 */
public final class Reversi {
  /**
   * The main entry point for the Reversi game application. Initializes the game with two players,
   * sets up the game model and views, and starts the game.
   * @param args arguments passed in for designating the type of players that will involve
   *             in this game (can be human or machine players, specified by
   *             "human", "simplistic", "smarter", "minimax")
   */
  public static void main(String[] args) {
    // a list of players involved in this game
    List<Player> players = new ArrayList<>(2);
    int boardSize = 6;
    ModelCreator.GameType gameType = ModelCreator.GameType.HEX;
    // for the first two strings in the given args if there are enough strings in the args,
    // or for however many the string that the args have if it has under two strings
    for (int i = 0; i < Math.min(args.length, 4); i++) {
      if (i == 0) {
        gameType = getGameType(args[i]);
      } else if (i == 1) {
        boardSize = validateGameBoardSize(getGameBoardSize(args[i]), gameType);
      } else {
        players.add(createPlayer(args[i]));
      }
    }

    // if there is not enough string in the args to create two players,
    // then default the missing player(s) to be human
    for (int numPlayer = players.size(); numPlayer < 2; numPlayer++) {
      players.add(PlayerCreator.create(PlayerCreator.PlayerType.HUMAN));
    }

    Player player1 = players.get(0);
    Player player2 = players.get(1);
    ReversiModelReadWrite model = ModelCreator.create(gameType, boardSize, player1, player2);
    ReversiGUI view1 = new ReversiGUI(model, gameType);
    ReversiGUI view2 = new ReversiGUI(model, gameType);
    ReversiController controller1 = new ReversiControllerImpl(model, view1, player1);
    ReversiController controller2 = new ReversiControllerImpl(model, view2, player2);
    model.startGame();
  }

  // validate the given board size for the given game type
  private static int validateGameBoardSize(int boardSize, ModelCreator.GameType gameType) {
    // hexagonal Reversi requires of a board of size being at least 3
    if (gameType == ModelCreator.GameType.HEX) {
      boardSize = boardSize >= 3 ? boardSize : 6;
    }
    // square Reversi requires of a board of size being greater than 2 and a even integer
    else if (gameType == ModelCreator.GameType.SQUARE) {
      boardSize = boardSize > 2 && boardSize % 2 == 0 ? boardSize : 6;
    }
    return boardSize;
  }

  // creates a player using our strategy corresponds to the given arguments,
  // defaults to be a human player
  private static Player createPlayer(String arg) {
    try {
      PlayerCreator.PlayerType playerType = PlayerCreator.PlayerType
              .valueOf(arg.toUpperCase());
      return PlayerCreator.create(playerType);
    } catch (IllegalArgumentException e) {
      // if an IllegalArgumentException occur, meaning there isn't a PlayerType
      // corresponding to the string, then default this player to be human player
      return PlayerCreator.create(PlayerCreator.PlayerType.HUMAN);
    }
  }

  // gets the type of game that the player would like to player,
  // defaults a hexagonal board
  private static ModelCreator.GameType getGameType(String arg) {
    try {
      return ModelCreator.GameType.valueOf(arg.toUpperCase());
    } catch (IllegalArgumentException e) {
      return ModelCreator.GameType.HEX;
    }
  }

  // gets the size of game board that the player would like to player, defaults 6
  private static int getGameBoardSize(String arg) {
    try {
      return Integer.valueOf(arg);
    } catch (NumberFormatException e) {
      return 6;
    }
  }
}