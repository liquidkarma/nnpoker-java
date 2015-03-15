package poker;

import java.util.Comparator;

public class Card implements Comparator{
   public static final int SPADE   = 0;
   public static final int CLUB    = 1;
   public static final int HEART   = 2;
   public static final int DIAMOND = 3;

   public static final int MAX_SUIT = 4;
   public static final int MAX_VALUE = 13;

   private boolean validCard = false;
   private boolean isUp = false;

   private int suit = 0;
   private int value = 0;

   private String [] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
   private String [] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

   public Card(){
      suit = value = 0;
      validCard = false;
   }

   public Card(int suit, int value){
      this.suit = suit;
      this.value = value;

      if(value > 0 && value <= MAX_VALUE)
         validCard = true;
   }

   public boolean equals(Object obj){
      Card otherCard = (Card)obj;
      if(validCard && otherCard.isValid())
         return (suit == otherCard.suit && value == otherCard.value);
      else
         return false;
   }

   public int compare(Object obj1, Object obj2){
      Card c1 = (Card)obj1;
      Card c2 = (Card)obj2;
      if(c1.isValid() && c2.isValid())
         return (c2.getFaceValue() - c1.getFaceValue());
      else
         return 0;
   }

   public boolean isValid(){
      return validCard;
   }

   public boolean isUp(){
      return isUp;
   }

   public void turnUp(){
      isUp = true;
   }

   public String getSuit(){
      return suits[suit];
   }

   public String getValue(){
      return values[value - 1];
   }

   public int getSuitValue(){
      return suit;
   }

   public int getFaceValue(){
      if(value == 1)
         return 14;
      else
         return value;
   }

   public String toString(){
      if(validCard){
         String card = getValue() + " of " + getSuit();
         return card;
      }
      else
         return "ERROR";
   }
}

// vim:ts=3:et
