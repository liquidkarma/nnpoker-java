package players;

import poker.PokerEvent;
import poker.PokerGame;

public class HumanPlayer extends Player{
   BetDialog betDialog = new BetDialog();

   public String getType(){
      return "HumanPlayer";
   }

   public PokerEvent handleDecision(PokerGame pokerGame){
      int potAmount = pokerGame.getPotAmount();

      betDialog.setPokerGame(pokerGame);
      betDialog.show();

      return betDialog.createPokerEvent();
   }
}

// vim:ts=3:et
