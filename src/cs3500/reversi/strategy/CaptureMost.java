package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a strategy in playing Reversi to find a coordinates on board in order to
 * capture as many pieces on this turn as possible.
 */
public class CaptureMost extends AbstractChainableStrategy {
  @Override
  public List<Coordinates> chooseMove(ReversiModelReadOnly model, Player player,
                                      List<Coordinates> coordsAvailableToMoveTo) {
    // the minimum score a coordinates must make the player earn if the player place a disc
    // in this coordinates in order to be saved into picked cell list
    int scoreBaseline = 1;
    // a picked coords list that contains one (or more if there is a tie) coordinates with the
    // highest possible score that a player can earn in this round
    List<Coordinates> pickedCoords = new ArrayList<>();
    for (Coordinates coordinates : coordsAvailableToMoveTo) {
      int scoreOfCurrentCell = model.scoreEarnedByPlaceIn(player, coordinates);
      if (scoreOfCurrentCell > scoreBaseline) {
        scoreBaseline = scoreOfCurrentCell;
        // remove all the coords picked before because the current coords excels them all,
        // and update the picked coords list as only containing the current cell
        pickedCoords = new ArrayList<>(Arrays.asList(coordinates));
      } else if (scoreOfCurrentCell == scoreBaseline) {
        // because this coords is equally good as the current baseline meaning there is a tie,
        // just add it into the picked list
        pickedCoords.add(coordinates);
      }
    }
    return pickedCoords;
  }
}
