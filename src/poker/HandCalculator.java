package poker;

import java.util.*;

//poker order:
//  high card
//  pair
//  two pair
//  three of a kind
//  straight
//  flush
//  full house
//  4 of a kind
//  straight flush

//  no suit order yet...
// remember that Card::getFaceValue return Ace high --> [2,14]

public class HandCalculator{
   private static final int BASE     = 16;
   private static final int HANDSIZE = 5;

   public static final long NO_PAIR         = 0 * (long)Math.pow(BASE, 10);
   public static final long ONE_PAIR        = 1 * (long)Math.pow(BASE, 10);
   public static final long TWO_PAIR        = 2 * (long)Math.pow(BASE, 10);
   public static final long THREE_OF_A_KIND = 3 * (long)Math.pow(BASE, 10);
   public static final long STRAIGHT        = 4 * (long)Math.pow(BASE, 10);
   public static final long FLUSH           = 5 * (long)Math.pow(BASE, 10);
   public static final long FULL_HOUSE      = 6 * (long)Math.pow(BASE, 10);
   public static final long FOUR_OF_A_KIND  = 7 * (long)Math.pow(BASE, 10);
   public static final long STRAIGHT_FLUSH  = 8 * (long)Math.pow(BASE, 10);

   public static String pokerValueToString(long value){
      if(value >= STRAIGHT_FLUSH)
         return "Straight Flush";
      if(value >= FOUR_OF_A_KIND)
         return "Four of a Kind";
      if(value >= FULL_HOUSE)
         return "Full House";
      if(value >= FLUSH)
         return "Flush";
      if(value >= STRAIGHT)
         return "Straight";
      if(value >= THREE_OF_A_KIND)
         return "Three of a Kind";
      if(value >= TWO_PAIR)
         return "Two Pair";
      if(value >= ONE_PAIR)
         return "One Pair";
      else
         return "High Card";
   }

   public static long getHandValue(List cards){
      if(cards.size() == 0)
         return 0;

      Collections.sort(cards, new Card());

      if(cards.size() < 5)
         return getSmallHandValue(cards);

      long value = getStraightFlushValue(cards);
      if(value >= STRAIGHT_FLUSH)
         return value;

      value = getFourOfAKindValue(cards);
      if(value >= FOUR_OF_A_KIND)
         return value;

      value = getFullHouseValue(cards);
      if(value >= FULL_HOUSE)
         return value;

      value = getFlushValue(cards);
      if(value >= FLUSH)
         return value;

      value = getStraightValue(cards);
      if(value >= STRAIGHT)
         return value;

      value = getThreeOfAKindValue(cards);
      if(value >= THREE_OF_A_KIND)
         return value;

      value = getTwoPairValue(cards);
      if(value >= TWO_PAIR)
         return value;

      value = getOnePairValue(cards);
      if(value >= ONE_PAIR)
         return value;
      else
         return getNoPairValue(cards);
   }

   private static long getNoPairValue(List cards){
      long result = 0;
      int numCards = cards.size();
      for(int i = 0; i < numCards; i++){
         Card card = (Card)cards.get(i);
         result += card.getFaceValue() * Math.pow(BASE, i);
      }

      result += NO_PAIR;
      return result;
   }

   private static long getOnePairValue(List cards){
      long result = 0;
      int numCards = cards.size();
      Card prevCard = (Card)cards.get(0);
      int prevFaceValue = ((Card)cards.get(0)).getFaceValue();
      for(int i = 1; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         if(faceValue == prevFaceValue)
            result += ONE_PAIR;
         result += faceValue;
         prevFaceValue = faceValue;
      }

      return result;
   }

   private static long getTwoPairValue(List cards){
      long result = 0;
      int pairs = 0;
      int numCards = cards.size();
      int prevFaceValue = ((Card)cards.get(0)).getFaceValue();
      int highCardOne = prevFaceValue;
      int highCardTwo = 0;
      for(int i = 1; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();

         if(faceValue != prevFaceValue)
            continue;

         pairs++;

         if(faceValue > highCardOne){
            highCardTwo = highCardOne;
            highCardOne = faceValue;
         }

         prevFaceValue = faceValue;
      }

      if(pairs >= 2){
         result += TWO_PAIR;
         result += highCardOne;
         result += highCardTwo;
      }

      return result;
   }

   private static long getThreeOfAKindValue(List cards){
      long result = 0;
      int numCards = cards.size();
      int prevPrevFaceValue = ((Card)cards.get(0)).getFaceValue();
      int prevFaceValue = ((Card)cards.get(1)).getFaceValue();
      for(int i = 2; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         if(faceValue == prevFaceValue && faceValue == prevPrevFaceValue)
            result = faceValue;
         prevPrevFaceValue = prevFaceValue;
         prevFaceValue = faceValue;
      }

      if(result > 0)
         result += THREE_OF_A_KIND;

      return result;
   }

   private static long getFourOfAKindValue(List cards){
      int numCards = cards.size();
      int card0 = ((Card)cards.get(0)).getFaceValue();
      int card1 = ((Card)cards.get(1)).getFaceValue();
      int card2 = ((Card)cards.get(2)).getFaceValue();
      for(int i = 3; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         if(faceValue == card0 && faceValue == card1 && faceValue == card2)
            return FOUR_OF_A_KIND + faceValue;
         card0 = card1;
         card1 = card2;
         card2 = faceValue;
      }

      return 0;
   }

   private static long getFullHouseValue(List cards){
      long result = 0;
      int numCards = cards.size();
      int prevPrevFaceValue = ((Card)cards.get(0)).getFaceValue();
      int prevFaceValue = ((Card)cards.get(1)).getFaceValue();
      for(int i = 2; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         if(faceValue == prevFaceValue && faceValue == prevPrevFaceValue)
            result = faceValue;
         prevPrevFaceValue = prevFaceValue;
         prevFaceValue = faceValue;
      }

      if(result > 0){
         long resultTwo = 0;
         prevFaceValue = ((Card)cards.get(0)).getFaceValue();
         for(int i = 1; i < numCards; i++){
            int faceValue = ((Card)cards.get(i)).getFaceValue();
            if(faceValue == prevFaceValue && faceValue != result)
               resultTwo = faceValue;
            prevFaceValue = faceValue;
         }

         if(resultTwo > 0)
            result += FULL_HOUSE;
         else
            return resultTwo;
      }

      return result;
   }

   private static long getStraightValue(List cards){
      long result = 0;
      cards = new ArrayList(cards);
      int numCards = cards.size();
      List removeCards = new ArrayList();
      int card0 = ((Card)cards.get(0)).getFaceValue();
      for(int i = 1; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         if(faceValue == card0)
            removeCards.add(cards.get(i - 1));
         card0 = faceValue;
      }

      cards.removeAll(removeCards);
      numCards = cards.size();

      if(numCards < 5)
         return getSmallHandValue(cards);

      card0 = ((Card)cards.get(0)).getFaceValue();
      int card1 = ((Card)cards.get(1)).getFaceValue();
      int card2 = ((Card)cards.get(2)).getFaceValue();
      int card3 = ((Card)cards.get(3)).getFaceValue();

      for(int i = 4; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         if(faceValue == (card3 + 1) && card3 == (card2 + 1) && card2 == (card1 + 1) && card1 == (card0 + 1))
            result = faceValue;

         card0 = card1;
         card1 = card2;
         card2 = card3;
         card3 = faceValue;
      }

      if(result > 0)
         result += STRAIGHT;

      return result;
   }

   private static long getFlushValue(List cards){
      int [] suits = new int[4];
      int [] results = new int[4];

      int numCards = cards.size();

      for(int i = 0; i < numCards; i++){
         Card card = (Card)cards.get(i);
         int suit = card.getSuitValue();
         int faceValue = card.getFaceValue();

         suits[suit]++;
         results[suit] += faceValue;

         if(suits[suit] >= 4)
            return (results[suit] + FLUSH);
      }

      return 0;
   }

   private static long getStraightFlushValue(List cards){
      long result = 0;
      cards = new ArrayList(cards);
      int numCards = cards.size();
      List removeCards = new ArrayList();
      int card0 = ((Card)cards.get(0)).getFaceValue();
      for(int i = 1; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         if(faceValue == card0)
            removeCards.add(cards.get(i - 1));
         card0 = faceValue;
      }

      cards.removeAll(removeCards);
      numCards = cards.size();

      if(numCards < 5)
         return getSmallHandValue(cards);

      card0 = ((Card)cards.get(0)).getFaceValue();
      int card1 = ((Card)cards.get(1)).getFaceValue();
      int card2 = ((Card)cards.get(2)).getFaceValue();
      int card3 = ((Card)cards.get(3)).getFaceValue();

      int suit0 = ((Card)cards.get(0)).getSuitValue();
      int suit1 = ((Card)cards.get(1)).getSuitValue();
      int suit2 = ((Card)cards.get(2)).getSuitValue();
      int suit3 = ((Card)cards.get(3)).getSuitValue();

      for(int i = 4; i < numCards; i++){
         int faceValue = ((Card)cards.get(i)).getFaceValue();
         int suit = ((Card)cards.get(i)).getSuitValue();
         if(faceValue == (card3 + 1) && card3 == (card2 + 1) && card2 == (card1 + 1) && card1 == (card0 + 1)
            && suit == suit0 && suit == suit1 && suit == suit2 && suit == suit3)
            result = faceValue;

         card0 = card1;
         card1 = card2;
         card2 = card3;
         card3 = faceValue;

         suit0 = suit1;
         suit1 = suit2;
         suit2 = suit3;
         suit3 = suit;
      }

      if(result > 0)
         result += STRAIGHT_FLUSH;

      return result;
   }

   private static long getSmallHandValue(List cards){
      int numCards = cards.size();

      int [] faces = new int[4];
      int [] suits = new int[4];

      for(int i = 0; i < numCards; i++){
         Card card = (Card)cards.get(i);
         faces[i] = card.getFaceValue();
         suits[i] = card.getSuitValue();
      }

      switch(numCards){
         case 1:
            return getNoPairValue(cards);
         case 2:
            if(faces[1] == faces[0] + 1){
               if(suits[0] == suits[1])
                  return STRAIGHT_FLUSH + faces[1];
               else
                  return STRAIGHT + faces[1];
            }
            else if(suits[0] == suits[1])
               return FLUSH + faces[0] + faces[1];
            else if(faces[1] == faces[0])
               return ONE_PAIR + faces[0] * 2;
            else
               return getNoPairValue(cards);
         case 3:
            if(faces[2] == faces[1] + 1 && faces[1] == faces[0] + 1){
               if(suits[2] == suits[1] && suits[2] == suits[0])
                  return STRAIGHT_FLUSH + faces[2];
               else
                  return STRAIGHT + faces[2];
            }
            else if(suits[2] == suits[1] && suits[2] == suits[0])
               return FLUSH + faces[2] + faces[1] + faces[0];
            else if(faces[2] == faces[1] && faces[2] == faces[0])
               return THREE_OF_A_KIND + faces[2];
            else if(faces[2] == faces[1])
               return ONE_PAIR + faces[2] * 2;
            else if(faces[1] == faces[0])
               return ONE_PAIR + faces[1] * 2;
            else
               return getNoPairValue(cards);
         case 4:
            if(faces[3] == faces[2] + 1 && faces[2] == faces[1] + 1 && faces[1] == faces[0] + 1){
               if(suits[3] == suits[2] && suits[3] == suits[1] && suits[3] == suits[0])
                  return STRAIGHT_FLUSH + faces[3];
               else
                  return STRAIGHT + faces[3];
            }
            else if(faces[3] == faces[2] && faces[3] == faces[1] && faces[3] == faces[0])
               return FOUR_OF_A_KIND + faces[3];
            else if(suits[3] == suits[2] && suits[3] == suits[1] && suits[3] == suits[0])
               return FLUSH + faces[3] + faces[2] + faces[1] + faces[0];
            else if(faces[3] == faces[2] && faces[3] == faces[1])
               return THREE_OF_A_KIND + faces[3];
            else if(faces[2] == faces[1] && faces[2] == faces[0])
               return THREE_OF_A_KIND + faces[2];
            else{
               long value = getOnePairValue(cards);
               if(value >= ONE_PAIR)
                  return value;
               else
                  return getNoPairValue(cards);
            }
         default:
            System.err.println("Invalid number of cards for small card lookup: " + numCards);
            break;
      }

      return 0;
   }

/*
   public static long getHandValue(Vector cards){
      int numValues = Card.MAX_VALUE;
      int numSuits = Card.MAX_SUIT;

      int [] values = new int[numValues];
      int [] suits = new int[numSuits];

      int numCards = cards.size();
      for(int i = 0; i < numCards; i++){
         Card card = (Card)cards.elementAt(i);
         System.out.print(":" + card.toString() + ": ");
         values[card.getFaceValue() - 2]++;
         suits[card.getSuitValue()]++;
      }

      long value = 0;
      int flushIndex = -1;

      // flush
      for(int i = 0; i < numSuits; i++){
         if(suits[i] >= 5){
            flushIndex = i;
            break;
         }
      }

      int highCard = -1;

      for(int i = 0; i < numValues; i++){
         int cardValue = i + 2;

         switch(values[i]){
            case 0:
            case 1:
               break;
            case 2:
               if(value == THREE_OF_A_KIND)
                  value = FULL_HOUSE;
               else
                  value = PAIR;
               break;
            case 3:
               if(value == PAIR)
                  value = FULL_HOUSE;
               else
                  value = THREE_OF_A_KIND;
               break;
            case 4:
               value = FOUR_OF_A_KIND;
            default:
               break;
         }

         if(values[i] > 0 && cardValue > highCard)
            highCard = cardValue;
      }

      numValues -= 5;
      for(int i = 0; i < numValues; i++){
         if(values[i] > 0 && values[i + 1] > 0 && values[i + 2] > 0 && values[i + 3] > 0 && values[i + 4] > 0){
            if(flushIndex > 0)
               value = STRAIGHT_FLUSH;
            else
               value = STRAIGHT;
         }
      }

      value += highCard;

      System.out.println(" => " + value);

      return value;
   }
*/
}

// vim:ts=3:et
