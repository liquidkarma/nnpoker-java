package players;

import java.util.Vector;

import poker.PokerEvent;
import poker.PokerGame;
import poker.Card;

public abstract class Player{
   public static int DEFAULT_PILE = 1000;

   protected Vector cards = new Vector();

   protected int pile = DEFAULT_PILE;

   protected boolean stillIn = true;

   public Player(){
   }

   public Player(Vector cards){
      this.cards = cards;
   }

   public boolean isIn(){
      return stillIn;
   }

   public void reset(){
      stillIn = true;
      cards = new Vector();
   }

   public void addCards(Vector cards){
      this.cards.addAll(cards);
   }

   public Vector getCards(){
      return cards;
   }

   public void clearCards(){
      cards.removeAllElements();
   }

   public void turnUpCards(){
      int numCards = cards.size();
      for(int i = 0; i < numCards; i++)
         ((Card)cards.elementAt(i)).turnUp();
   }

   public void setPileSize(int pile){
      this.pile = pile;
   }

   public int getPileSize(){
      return pile;
   }

   public PokerEvent makeDecision(PokerGame pokerGame){
      PokerEvent pokerEvent = handleDecision(pokerGame);

      switch(pokerEvent.getType()){
         case PokerEvent.FOLD:
            stillIn = false;
            break;
         case PokerEvent.BET:
            pile -= pokerEvent.getValue();
            break;
         case PokerEvent.CALL:
            {
               int amount = pokerGame.getCallAmount();
               if(amount > pile)
                  amount = pile;
               pile -= amount;
               pokerEvent = new PokerEvent(PokerEvent.BET, amount);
            }
            break;
         default:
            break;
      }

      return pokerEvent;
   }

   protected abstract PokerEvent handleDecision(PokerGame pokerGame);

   public abstract String getType();
}

// vim:ts=3:et
