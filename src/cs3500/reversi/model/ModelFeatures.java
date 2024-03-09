package cs3500.reversi.model;

import cs3500.reversi.player.Player;

/**
 * Represents a features listener that listens to notifications sent from a Reversi model.
 */
public interface ModelFeatures {
  /**
   * Receive notification from a Reversi model that there is a new turn in the game,
   * and the active player who should be playing a move this round is the given player.
   * @param player the active player who should be playing a move this round
   */
  void newTurn(Player player);

  /**
   * Receive notification from a Reversi model that the given player is stuck with
   * no possible move so the player must pass this round of the game.
   * @param player the player that does not have any possible move
   *               so must pass this round of the game
   */
  void mustPass(Player player);

  /**
   * Receive notification from a Reversi model that the game have ended.
   */
  void gameEnds();
}
