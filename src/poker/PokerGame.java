package poker;

import java.util.Vector;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import players.Player;

public class PokerGame implements Runnable{
   private static final long MAX_HANDS       = 10;
   private static final long handRange       = 52 * MAX_HANDS;

   private static final long PAIR            = 0 * handRange;
   private static final long TWO_PAIR        = 1 * handRange;
   private static final long THREE_OF_A_KIND = 2 * handRange;
   private static final long FOUR_OF_A_KIND  = 3 * handRange;
   private static final long FULL_HOUSE      = 4 * handRange;
   private static final long STRAIGHT        = 5 * handRange;
   private static final long STRAIGHT_FLUSH  = 6 * handRange;
   private static final long FLUSH           = 7 * handRange;

   private Vector pokerListeners = new Vector();

   private int deckSize = Card.MAX_SUIT * Card.MAX_VALUE;
   private Card [] cards = null;
   private int cardIndex = 0;

   private Vector players = new Vector();

   private Vector upCards = new Vector();

   private int pot = 0;
   private int callAmount = 0;

   PokerComm pokerComm = new PokerComm(this);

   public PokerGame(Class playerType){
      try{
         Player player = (Player)playerType.newInstance();
         players.addElement(player);
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }

   public void addPokerListener(PokerListener listener){
      pokerListeners.addElement(listener);

      if(players.size() > 0)
         broadcastPokerEvent(new PokerEvent(PokerEvent.ADD_PLAYERS, players));

      if(upCards.size() > 0)
         broadcastPokerEvent(new PokerEvent(PokerEvent.TURN_UP, upCards));
   }

   public void removePokerListener(PokerListener listener){
      pokerListeners.removeElement(listener);
   }

   private void broadcastPokerEvent(PokerEvent event){
      int numListeners = pokerListeners.size();
      for(int i = 0; i < numListeners; i++)
         ((PokerListener)pokerListeners.elementAt(i)).pokerEvent(event);
   }

   private void init(){
      pot = 0;

      shuffleCards();
      cardIndex = 0;
      upCards = new Vector();

      int numPlayers = players.size();
      for(int i = 0; i < numPlayers; i++){
         Player player = (Player)players.elementAt(i);
         player.reset();
      }
   }

   public void start(){
      (new Thread(this)).start();
   }

   public void run(){
      init();

      // Texas Hold 'Em
      deal(2);

      int validPlayers = getPlayerDecision();
      if(validPlayers > 1){
         turnUpCard();
         turnUpCard();
         turnUpCard();

         validPlayers = getPlayerDecision();
         while(validPlayers > 1 && upCards.size() < 5){
            turnUpCard();
            validPlayers = getPlayerDecision();
         }
      }

      Vector winners = findWinners();

      //one last refresh before winners are announced
      broadcastPokerEvent(new PokerEvent(PokerEvent.REFRESH));

      int numBestPlayers = winners.size();
      if(numBestPlayers > 0){
         int amount = pot / numBestPlayers;
         String message = "Player: ";
         for(int i = 0; i < numBestPlayers; i++){
            int index = ((Integer)winners.elementAt(i)).intValue();
            if(i > 0)
               message += ", ";
            if(index == 0)
               message += "Localuser";
            else
               message += index;
            Player player = (Player)players.elementAt(index);
            player.setPileSize(player.getPileSize() + amount);
         }
         message += "\nPayout: $" + amount;
         JOptionPane.showMessageDialog(null, message, "Winner", JOptionPane.INFORMATION_MESSAGE);
      }

      pot = 0;

      broadcastPokerEvent(new PokerEvent(PokerEvent.REFRESH));
   }

   private Vector findWinners(){
      Vector bestPlayers = new Vector();
      long maxHand = -1;

      int numPlayers = players.size();
      for(int i = 0; i < numPlayers; i++){
         Player player = (Player)players.elementAt(i);
         if(player.isIn()){
            player.turnUpCards();
            Vector checkCards = new Vector();
            checkCards.addAll(upCards);
            checkCards.addAll(player.getCards());
            long value = HandCalculator.getHandValue(checkCards);
            if(value > maxHand){
               bestPlayers = new Vector();
               bestPlayers.addElement(new Integer(i));
               maxHand = value;
            }
            else if(value == maxHand)
               bestPlayers.addElement(new Integer(i));
         }
      }

      return bestPlayers;
   }

   public int getCallAmount(){
      return callAmount;
   }

   private int getPlayerDecision(){
      int numPlayers = players.size();
      int numIn = 0;

      for(int i = 0; i < numPlayers; i++){
         if(((Player)players.elementAt(i)).isIn())
            numIn++;
      }

      if(numIn > 1){
         boolean betsEqual = false;
         int [] bets = new int[numPlayers];
         while(!betsEqual){
            callAmount = 0;
            for(int i = 0; i < numPlayers; i++){
               Player player = (Player)players.elementAt(i);
               if(player.isIn()){
                  PokerEvent pokerEvent = player.makeDecision(this);
                  bets[i] = 0;
                  switch(pokerEvent.getType()){
                     case PokerEvent.BET:
                        bets[i] = pokerEvent.getValue();
                        pot += bets[i];
                        if(bets[i] > callAmount)
                           callAmount = bets[i];
                        break;
                     case PokerEvent.FOLD:
                     default:
                        break;
                  }
               }
            }

            int currentBet = -1;
            betsEqual = true;
            for(int i = 0; i < numPlayers && betsEqual; i++){
               Player player = (Player)players.elementAt(i);
               if(player.isIn()){
                  if(currentBet >= 0 && bets[i] != currentBet)
                     betsEqual = false;
                  else if(currentBet < 0)
                     currentBet = bets[i];
               }
            }

            broadcastPokerEvent(new PokerEvent(PokerEvent.REFRESH));
         }
      }

      return numIn;
   }

   private void shuffleCards(){
      Random random = new Random(System.currentTimeMillis());

      List cardList = new ArrayList();

      for(int i = 0; i < Card.MAX_SUIT; i++){
         int suitIndex = i * Card.MAX_VALUE;
         for(int j = 0; j < Card.MAX_VALUE; j++)
            cardList.add(new Card(i, j + 1));
      }

      Collections.shuffle(cardList, random);

      cards = new Card[cardList.size()];
      cards = (Card[])cardList.toArray(cards);

//      System.err.println("-------------------");
//      for(int i = 0; i < deckSize; i++)
//         System.err.println(i + ": " + cards[i].toString());
   }

   public void deal(int numCards){
      int numPlayers = players.size();
      for(int i = 0; i < numPlayers; i++){
         Player player = (Player)players.elementAt(i);
         player.clearCards();

         Vector playerCards = new Vector();
         for(int j = 0; j < numCards; j++)
            playerCards.addElement(cards[cardIndex++]);

         player.addCards(playerCards);
      }

      broadcastPokerEvent(new PokerEvent(PokerEvent.DEAL));
   }

   public void turnUpCard(){
      cards[cardIndex].turnUp();
      upCards.addElement(cards[cardIndex++]);
      broadcastPokerEvent(new PokerEvent(PokerEvent.TURN_UP, upCards));
   }

   public void startServer(int port){
      pokerComm.startServer(port);
   }

   public void shutdownServer(){
      pokerComm.shutdownServer();
   }

   public void connect(String host, int port){
      pokerComm.connect(host, port);
   }

   public void disconnect(){
      pokerComm.disconnect();
   }

   public boolean isConnected(){
      return pokerComm.isConnected();
   }

   public boolean isServer(){
      return pokerComm.isServer();
   }

   public void addPlayer(Player player){
      if(cards != null){
         Vector playerCards = new Vector();
         playerCards.addElement(cards[cardIndex++]);
         playerCards.addElement(cards[cardIndex++]);
         player.addCards(playerCards);
      }

      players.addElement(player);
      broadcastPokerEvent(new PokerEvent(PokerEvent.ADD_PLAYERS, players));
   }

   public void removePlayer(Player player){
      players.removeElement(player);
//      removePokerListener((PokerListener)player);
      broadcastPokerEvent(new PokerEvent(PokerEvent.DROP_PLAYERS, players));
   }

   public Vector getPlayers(){
      return players;
   }

   public Vector getUpCards(){
      return upCards;
   }

   public int getPotAmount(){
      return pot;
   }
}

// vim:ts=3:et
