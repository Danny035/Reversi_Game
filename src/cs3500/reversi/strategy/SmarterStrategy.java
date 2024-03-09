package cs3500.reversi.strategy;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.player.Player;
import cs3500.reversi.model.Coordinates;
import cs3500.reversi.model.ReversiModelReadOnly;

/**
 * Represents a smarter strategy in playing Reversi that will first prioritize
 * choosing the corners, then avoid giving out the corners, then try to earn
 * the most points in score for this round of the game for the given player.
 */
public class SmarterStrategy implements Strategy {
  @Override
  public Optional<Coordinates> chooseMove(ReversiModelReadOnly model, Player player) {
    List<Coordinates> pickedList = new CaptureMost().chooseMove(model, player,
            new AvoidGivingCorners().chooseMove(model, player,
                    new GoForCorners().chooseMove(model, player)));
    return Coordinates.upperLeftMost(pickedList, model.getBoardSideLength());
  }
}
