package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import cs3500.reversi.player.Player;
import cs3500.reversi.utils.Utils;

/**
 * Represents a Reversi game which abstract out the common code among different
 * implementations of Reversi model.
 */
public abstract class AbstractReversi implements ReversiModelReadWrite {
  // represents the board game, each inner List<Cell> is a row in the board
  // with 0th List<Cell> being the top row in board,
  // and 0th Cell is the leftmost cell in the row.
  protected final List<List<Cell>> board;
  protected final int boardSize;
  // Class Invariant: the length of the list of players and list of discs
  // must always be the same size.
  protected final List<Player> players;
  protected final List<Disc> discs;

  // a record of how many rounds this game have continued
  protected int round;
  // number of consecutive passes that players have taken
  protected int numPass;
  // a list of Features listeners that are registered to receive notification
  // about the current turn of the game when game starts and every time the turn changes
  protected final List<ModelFeatures> listeners;
  // true if this is a real playing game that players take turn to play and after every move,
  // the next player would need to be notified to move, or false if this is a mock try-out game
  // where it doesn't and shouldn't continue the gaming process of calling the next player
  protected final boolean isRealGame;

  /**
   * A cloning constructor that sets the attributes of this Reversi model to
   * be the same as the given Reversi model.
   * @param reversi a Reversi model to be cloned
   */
  public AbstractReversi(AbstractReversi reversi) {
    this.boardSize = reversi.boardSize;
    this.board = reversi.getBoard();
    this.players = reversi.players;
    this.discs = reversi.discs;
    this.numPass = reversi.numPass;
    this.round = reversi.round;
    this.listeners = reversi.listeners;
    this.isRealGame = false;
  }

  /**
   * Constructs a Reversi model that has the game board of given board size,
   * and the given 2 players playing it.
   * @param boardSize the size of the game board
   * @param player1 one of the players of this game
   * @param player2 the other player of this game
   */
  public AbstractReversi(int boardSize, Player player1, Player player2) {
    this.validateBoardSize(boardSize);
    this.boardSize = boardSize;
    this.board = this.initializeBoard();
    this.placeInitialDiscs();
    // class invariants that this.players and this.discs have the same size
    // are kept
    List<Player> playerList = new ArrayList<>(Arrays.asList(player1, player2));
    List<Disc> discList = new ArrayList<>(Arrays.asList(Disc.WHITE, Disc.BLACK));
    this.players = Collections.unmodifiableList(playerList);
    this.discs = Collections.unmodifiableList(discList);
    this.round = 0;
    this.numPass = 0;
    this.listeners = new ArrayList<>();
    this.isRealGame = true;
  }

  protected abstract void validateBoardSize(int boardSize);

  protected abstract List<List<Cell>> initializeBoard();

  protected abstract void placeInitialDiscs();

  @Override
  public int getBoardSideLength() {
    return this.boardSize;
  }

  @Override
  public List<List<Cell>> getBoard() {
    List<List<Cell>> copy = new ArrayList<>();
    for (List<Cell> row : this.board) {
      List<Cell> copyRow = new ArrayList<>();
      for (Cell cell : row) {
        copyRow.add(cell.copy());
      }
      copy.add(copyRow);
    }
    return copy;
  }

  @Override
  public int scoreEarnedByPlaceIn(Player player, Coordinates coordinates) {
    this.checkGameIsContinuing();
    int score = 0;
    // if this is the given player's turn to play and the given coordinates is valid
    // and the cell is empty so that the player can place a disc
    if (player.equals(this.getTurn()) && coordinates.validCoordsOnBoard(this.boardSize)
            && this.getCellInBoard(coordinates).isEmpty()) {
      for (Function<Coordinates, Coordinates> func : coordinates.getNeighborFunctions()) {
        score += this.searchThrough(func, coordinates, this.getDiscOf(player), 0);
      }
    }
    return score;
  }

  // Check if this game haven't ended, throw IllegalStateException if the game already ended
  protected void checkGameIsContinuing() throws IllegalStateException {
    if (this.isGameOver()) {
      throw new IllegalStateException("Game already ended!");
    }
  }

  // search through the given direction to see possible score that can be earned.
  protected int searchThrough(Function<Coordinates, Coordinates> getNeighbor,
                            Coordinates coordinates, Disc disc, int scoreAccumulator) {
    Coordinates neighbor = getNeighbor.apply(coordinates);
    // if there is no neighbor to the given coordinates in this direction anymore,
    // or there is no disc on the cell of the given coordinates, meaning the search stream
    // is broken, and haven't found the given kind of disc to end, then return 0
    if (!neighbor.validCoordsOnBoard(this.boardSize) || this.getCellInBoard(neighbor).isEmpty()) {
      return 0;
    }
    Cell cell = this.getCellInBoard(neighbor);
    Disc discInCurrentCell = cell.getDisc();
    if (discInCurrentCell != disc) {
      return this.searchThrough(getNeighbor, neighbor, disc, scoreAccumulator + 1);
    } else {
      return scoreAccumulator;
    }
  }

  @Override
  public boolean movable(Player player) {
    this.checkGameIsContinuing();
    // if this is not this player's turn to play, then just return false
    if (!player.equals(this.getTurn())) {
      return false;
    }
    List<Disc> opponentDiscs = this.getAllOpponentDiscsOnBoard(player);
    List<Cell> opponentCells = this.getAllCellsWithDiscs(opponentDiscs);
    Set<Cell> possibleCellToMove = this.getAllCellsAround(opponentCells);
    return this.canPlayerPlaceDiscInAny(possibleCellToMove, player);
  }

  // returns all discs on board that is from an opponent of the given player.
  private List<Disc> getAllOpponentDiscsOnBoard(Player player) {
    List<Disc> allOpponentDiscs = new ArrayList<>();
    int playerIndex = this.players.indexOf(player);
    for (int idx = 0; idx < this.discs.size(); idx++) {
      if (idx != playerIndex) {
        allOpponentDiscs.add(this.discs.get(idx));
      }
    }
    return allOpponentDiscs;
  }

  // returns a list of all cells that has any of the given discs put on them.
  private List<Cell> getAllCellsWithDiscs(List<Disc> discs) {
    List<Cell> cells = new ArrayList<>();
    for (List<Cell> row : this.board) {
      for (Cell cell : row) {
        if (!cell.isEmpty() && discs.contains(cell.getDisc())) {
          cells.add(cell);
        }
      }
    }
    return cells;
  }

  // returns a list of all cells that are neighbors of any cells
  // in the given list of cells.
  private Set<Cell> getAllCellsAround(List<Cell> cells) {
    Set<Cell> allCellsAroundGivenCells = new HashSet<>();
    for (Cell cell : cells) {
      allCellsAroundGivenCells.addAll(this.getAllNeighbors(cell));
    }
    return allCellsAroundGivenCells;
  }

  // return a list of all neighbor cells of the given cell
  private List<Cell> getAllNeighbors(Cell cell) {
    List<Cell> allNeighborCells = new ArrayList<>();
    Coordinates cellCoordinates = cell.getCoordinates();
    for (Coordinates neighbor : cellCoordinates.getAllNeighbors()) {
      if (neighbor.validCoordsOnBoard(this.boardSize)) {
        allNeighborCells.add(this.getCellInBoard(neighbor));
      }
    }
    return allNeighborCells;
  }

  // returns true if the given player can place a disc in any of
  // the given collection of cells, false if none of the cells can be a legal move.
  private boolean canPlayerPlaceDiscInAny(Collection<Cell> possibleCellToMove, Player player) {
    for (Cell cell : possibleCellToMove) {
      if (this.scoreEarnedByPlaceIn(player, cell.getCoordinates()) > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int getScore(Player player) {
    int score = 0;
    Disc discOfPlayer = this.getDiscOf(player);
    for (List<Cell> row : this.board) {
      for (Cell cell : row) {
        if (!cell.isEmpty() && cell.getDisc() == discOfPlayer) {
          score += 1;
        }
      }
    }
    return score;
  }

  @Override
  public boolean isGameOver() {
    return this.numPass == this.players.size();
  }

  @Override
  public Player getTurn() {
    this.checkGameIsContinuing();
    int currentSequence = this.round % this.players.size();
    return this.players.get(currentSequence);
  }

  @Override
  public Cell getCellInBoard(Coordinates coordinates) throws IllegalArgumentException {
    int row = coordinates.getRow(this.boardSize);
    int locationInRow = coordinates.getLocationInRow(this.boardSize);
    if (Utils.checkValuesInBetween(row, 0, this.board.size() - 1)
            && Utils.checkValuesInBetween(locationInRow, 0, this.board.get(row).size() - 1)) {
      return this.board.get(row).get(locationInRow);
    }
    throw new IllegalArgumentException("Invalid coordinates!");
  }

  @Override
  public ReversiModelReadOnly tryMove(Player player, Coordinates coordinates)
          throws IllegalArgumentException, IllegalStateException {
    ReversiModelReadWrite newModel = this.copyModel();
    newModel.placeIn(player, coordinates);
    return newModel;
  }

  protected abstract ReversiModelReadWrite copyModel();

  @Override
  public void pass(Player player) {
    this.checkValidTurn(player);
    this.checkGameIsContinuing();
    this.numPass += 1;
    this.round += 1;
    if (!isGameOver()) {
      this.endAMove();
    } else {
      this.notifyGameEnds();
    }
  }

  // Check if this is the given player's turn, if not, throw IllegalStateException
  private void checkValidTurn(Player player) throws IllegalStateException {
    if (!this.getTurn().equals(player)) {
      throw new IllegalStateException("It's not your turn yet!");
    }
  }

  // a helper method that deals with the end of a move by notifying the next player to make a move
  // if this is in a real game
  private void endAMove() {
    if (isRealGame) {
      this.notifyNewTurn();
    }
  }

  // notify all the listeners which subscribed to get the turn of the game
  // about the player that should be playing for the current new round
  private void notifyNewTurn() {
    Player playerOfTurn = this.getTurn();
    boolean currentPlayerCannotMove = !movable(playerOfTurn);
    int currentRound = this.round;
    for (ModelFeatures listener : this.listeners) {
      if (currentPlayerCannotMove && !isGameOver()) {
        listener.mustPass(playerOfTurn);
      }
      // if a player already take move on this round, then there is no need to continue
      // broadcasting the new turn notification for this round
      if (currentRound == this.round) {
        listener.newTurn(playerOfTurn);
      }
    }
  }

  // notify all the listeners which subscribed to listen to the changes of this model
  // that this game have ended
  private void notifyGameEnds() {
    for (ModelFeatures listener : this.listeners) {
      listener.gameEnds();
    }
  }

  @Override
  public void placeIn(Player player, Coordinates coordinates) {
    this.checkValidTurn(player);
    this.checkGameIsContinuing();
    // earning a score of 0 meaning that this is an illegal move
    if (this.scoreEarnedByPlaceIn(player, coordinates) == 0) {
      throw new IllegalArgumentException("this coordinates is not a valid place to put disc in!");
    }
    Disc discOfThisPlayer = this.getDiscOf(player);
    this.getCellInBoard(coordinates).placeDisc(discOfThisPlayer);

    for (Function<Coordinates, Coordinates> func : coordinates.getNeighborFunctions()) {
      if (this.searchThrough(func, coordinates, this.getDiscOf(player), 0) > 0) {
        this.flipAlong(func, coordinates, discOfThisPlayer);
      }
    }
    this.numPass = 0;
    this.round += 1;
    this.endAMove();
  }

  // flip the discs along the given direction to the given disc
  private void flipAlong(Function<Coordinates, Coordinates> getNeighbor,
                         Coordinates coordinates, Disc disc) {
    Coordinates neighbor = getNeighbor.apply(coordinates);
    if (!coordinates.validCoordsOnBoard(this.boardSize)
            || this.getCellInBoard(neighbor).isEmpty()) {
      throw new IllegalArgumentException("Wrong direction to flip!");
    }
    // if this cell is already occupied by the given disc that we are trying to flip discs into,
    // this means that this flip should stop.
    else if (this.getCellInBoard(neighbor).getDisc().equals(disc)) {
      return;
    }
    Cell cell = this.getCellInBoard(neighbor);
    cell.flipDiscTo(disc);
    this.flipAlong(getNeighbor,neighbor, disc);
  }

  @Override
  public void startGame() {
    this.notifyNewTurn();
  }

  @Override
  public void addFeatures(ModelFeatures listener) {
    this.listeners.add(listener);
  }

  @Override
  public Disc getDiscOf(Player player) {
    if (!this.players.contains(player)) {
      throw new IllegalArgumentException("Player not involved in this game!");
    }
    int playerIndex = this.players.indexOf(player);
    return this.discs.get(playerIndex);
  }
}
