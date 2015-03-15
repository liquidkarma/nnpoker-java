package players;

import poker.PokerEvent;
import poker.PokerGame;
import poker.Card;

import java.util.Vector;

public class NNPlayer extends Player{
   private int numBits = (int)Math.round((double)Math.log(DEFAULT_PILE) / Math.log(2.0));

   private int numInput = 52 + numBits + numBits;
   private int numHidden = 100;
   private int numOutput = 3;
   private NeuralNetwork nn = new NeuralNetwork(numInput, numHidden, numOutput);

   public String getType(){
      return "NNPlayer";
   }

   private void fillBits(int num, double [] inputs, int start){
      int end = start + numBits;
      int shiftBit = 1;
      for(int i = start; i < end; i++){
         inputs[i] = (num & shiftBit) > 0 ? 1 : 0;
         shiftBit <<= 1;
      }
   }

   private int getBits(double [] outputs, int start){
      int num = 0;
      int shiftBit = 1;
      int end = start + numBits;
      for(int i = start; i < end; i++){
         if(outputs[i] > 0.7) //cutoff for value of 1
            num |= shiftBit;
         shiftBit <<= 1;
      }
      return num;
   }

   public PokerEvent handleDecision(PokerGame pokerGame){
//      System.err.println("numBits: " + numBits);

      PokerEvent pokerEvent = null;

      double [] inputs = new double[numInput];
      double [] outputs = new double[numOutput];

      Vector upCards = new Vector();
      upCards.addAll(pokerGame.getUpCards());
      upCards.addAll(cards);
      int numCards = upCards.size();
      for(int i = 0; i < numCards; i++){
         Card card = (Card)upCards.elementAt(i);
         inputs[card.getFaceValue() * 4 + card.getSuitValue()] = 1.0;
      }

      fillBits(pile, inputs, 52);
      fillBits(pokerGame.getPotAmount(), inputs, 52 + numBits);

      nn.activate(inputs, outputs);

      int decision = 0;

      if(outputs[1] > outputs[0] && outputs[1] > outputs[2])
         decision = 1;
      else if(outputs[2] > outputs[0] && outputs[2] > outputs[1])
         decision = 2;

      switch(decision){
         case 0:
//            pokerEvent = new PokerEvent(PokerEvent.BET, 10);
//            break;
         case 1:
            pokerEvent = new PokerEvent(PokerEvent.CALL);
            break;
         case 2:
            pokerEvent = new PokerEvent(PokerEvent.FOLD);
            break;
         default:
            break;
      }

      return pokerEvent;
   }
}

// vim:ts=3:et
