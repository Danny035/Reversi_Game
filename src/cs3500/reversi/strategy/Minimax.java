package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a strategy in playing Reversi to choose the move that leaves their opponent
 * in a situation with no good moves (minimizes the maximum move the opponent can make).
 * NOTE: This strategy assumes the opponent is using the CaptureMost strategy,
 * and this strategy only considers one step further in the future
 * (meaning it only minimizes the maximum move the opponent can make for the next move).
 * Also, this strategy didn't consider passing when there is still available move as a choice.
 */
public class Minimax extends AbstractChainableStrategy {
  @Override
  public List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player,
                                      List<Coordinates> coordsAvailableToMoveTo) {
    // first initialize the highest possible earn for the opponent to be a number that is
    // higher than any possible earn the opponent can get
    int opponentHighestPossibleEarn = model.getBoard().size() * model.getBoard().size();

    // a picked coords list that contains one (or more if there is a tie) coordinates that
    // by placing a disc of this player's into it can make the opponent to earn the
    // lowest possible score in the next round
    List<Coordinates> pickedCoords = new ArrayList<>();

    for (Coordinates coords : coordsAvailableToMoveTo) {
      ReversiModelReadOnly newModel = model.tryMove(player, coords);
      Player opponent = newModel.getTurn();
      List<Coordinates> opponentMove = new CaptureMost()
              .chooseMove(newModel, opponent);
      int newOpponentHighestEarn;
      // if moving into this coordinates can make the opponent have no possible move
      // then the highest score the opponent can earn is 0
      if (opponentMove.size() == 0) {
        newOpponentHighestEarn = 0;
      }
      // if the opponent does have some possible moves, then the opponentMove should contain
      // one (or more if there is a tie) best move that can earn the most
      else {
        newOpponentHighestEarn = newModel.scoreEarnedByPlaceIn(opponent, opponentMove.get(0));
      }

      // if moving into this coordinates can make the opponent have a lower than ever
      // possible highest earn, then replace the record with the new lowest value
      // and replace the list of picked coords to contain only the current coords.
      if (opponentHighestPossibleEarn > newOpponentHighestEarn) {
        opponentHighestPossibleEarn = newOpponentHighestEarn;
        pickedCoords = new ArrayList<>(Arrays.asList(coords));
      }
      // if placing a disc into this coordinates is as good as some coordinates we found before
      // meaning there is a tie, then just add this coordinates into the list of picked coords.
      else if (opponentHighestPossibleEarn == newOpponentHighestEarn) {
        pickedCoords.add(coords);
      }
    }
    return pickedCoords;
  }
}
