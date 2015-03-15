package players;

import poker.PokerEvent;
import poker.PokerGame;

import java.util.Random;

public class RandomPlayer extends Player{
   private Random random = new Random();

   public String getType(){
      return "RandomPlayer";
   }

   public PokerEvent handleDecision(PokerGame pokerGame){
      double decision = random.nextDouble();

      PokerEvent pokerEvent = null;

      if(decision > 0.7 && pile > 0)
         pokerEvent = new PokerEvent(PokerEvent.BET, random.nextInt(pile));
      else if(decision > 0.3 && pile > 0)
         pokerEvent = new PokerEvent(PokerEvent.CALL);
      else
         pokerEvent = new PokerEvent(PokerEvent.FOLD);

      return pokerEvent;
   }
}

// vim:ts=3:et
