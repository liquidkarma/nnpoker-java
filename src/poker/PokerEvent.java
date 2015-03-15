package poker;

import java.util.Vector;

public class PokerEvent{
   public static final int NONE         = 0;
   public static final int DEAL         = 1;
   public static final int TURN_UP      = 2;
   public static final int ADD_PLAYERS  = 3;
   public static final int DROP_PLAYERS = 4;
   public static final int BET          = 5;
   public static final int CALL         = 6;
   public static final int FOLD         = 7;
   public static final int GAME_OVER    = 8;
   public static final int REFRESH      = 9;

   private int type = NONE;

   private int value = 0;

   private Vector entities = null;

   public PokerEvent(){
   }

   public PokerEvent(int type){
      this.type = type;
   }

   public PokerEvent(int type, Vector entities){
      this.type = type;
      this.entities = entities;
   }

   public PokerEvent(int type, int value){
      this.type = type;
      this.value = value;
   }

   public int getType(){
      return type;
   }

   public int getValue(){
      return value;
   }

   public Vector getEntities(){
      return entities;
   }
}

// vim:ts=3:et
