package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Vector;

import players.Player;

public class PokerPanel extends JPanel implements PokerListener, ComponentListener, MouseListener, ActionListener{
   private int width = 0;
   private int height = 0;

   private Image doubleImage = null;

   private boolean needUpdate = true;

   private PokerGame pokerGame = null;
   private Vector players = null;
   private Vector upCards = new Vector();

   private ImageIcon spade = null;
   private ImageIcon club = null;
   private ImageIcon heart = null;
   private ImageIcon diamond = null;

   private FontMetrics metrics = null;
   private int fontHeight = 0;
   private int barHeight = 0;
   private int popupX = 0;

   private JPopupMenu removePlayerPopup = new JPopupMenu();
   private JMenuItem removePlayerItem = new JMenuItem("Remove Player");

   public PokerPanel(PokerGame pokerGame){
      super();

      addComponentListener(this);
      addMouseListener(this);

      width = getWidth();
      height = getHeight();

      ClassLoader cl = getClass().getClassLoader();
      spade   = new ImageIcon(cl.getResource("images/spade.png"));
      club    = new ImageIcon(cl.getResource("images/club.png"));
      heart   = new ImageIcon(cl.getResource("images/heart.png"));
      diamond = new ImageIcon(cl.getResource("images/diamond.png"));

      this.pokerGame = pokerGame;
      pokerGame.addPokerListener(this);

      removePlayerItem.addActionListener(this);
      removePlayerPopup.add(removePlayerItem);
   }

   public void pokerEvent(PokerEvent event){
      switch(event.getType()){
         case PokerEvent.DEAL:
            upCards.removeAllElements();
            break;
         case PokerEvent.TURN_UP:
            upCards = event.getEntities();
            break;
         case PokerEvent.ADD_PLAYERS:
         case PokerEvent.DROP_PLAYERS:
            players = event.getEntities();
            break;
         case PokerEvent.BET:
         case PokerEvent.CALL:
         case PokerEvent.FOLD:
         case PokerEvent.GAME_OVER:
         case PokerEvent.REFRESH:
            break;
         default:
            System.err.println("Unknown Poker Event: " + event.getType());
            break;
      }

      needUpdate = true;
      repaint();
   }

   private int drawCard(Graphics g, int x, int y, Card card, int cardWidth, boolean forceUp){
      if(cardWidth > (width / 10))
         cardWidth = width / 10;

      int cardHeight = 4 * cardWidth / 3;
      int arcSize = cardWidth / 5;

      g.setColor(Color.black);

      if(forceUp || card.isUp()){
         g.drawRoundRect(x, y, cardWidth, cardHeight, arcSize, arcSize);

         Image image = spade.getImage();
         String suit = card.getSuit();
         if(suit == "Hearts"){
            g.setColor(Color.red);
            image = heart.getImage();
         }
         else if(suit == "Diamonds"){
            g.setColor(Color.red);
            image = diamond.getImage();
         }
         else if(suit == "Clubs")
            image = club.getImage();

         g.drawString(card.getValue(), x + 2, y + fontHeight);

         int margin = 7;
         int imagex = x + cardWidth / margin;
         int imagey = y + cardHeight / margin;
         int scaledWidth = (margin - 2) * cardWidth / margin;
         int scaledHeight = (margin - 2) * cardHeight / margin;

         int imageWidth = image.getWidth(this);
         int imageHeight = image.getHeight(this);

         g.drawImage(image, imagex, imagey, imagex + scaledWidth, imagey + scaledHeight, 0, 0, imageWidth, imageHeight, this);
      }
      else
         g.fillRoundRect(x, y, cardWidth, cardHeight, arcSize, arcSize);

      return (x + cardWidth);
   }

   public void paint(Graphics g){
      if(!needUpdate && doubleImage != null)
         g.drawImage(doubleImage, 0, 0, this);
      else{
         needUpdate = false;
         doubleImage = createImage(width, height);
         Graphics g2 = doubleImage.getGraphics();

         metrics = g2.getFontMetrics();
         fontHeight = metrics.getHeight();

         g2.clearRect(0, 0, width, height);

         barHeight = height / 3;

         int y = barHeight;
         g2.setColor(Color.black);
         g2.drawLine(0, y, width, y);

         int potBar = 4 * width / 5;
         g2.drawLine(potBar, y, potBar, height);
         g2.drawString("Pot: $" + pokerGame.getPotAmount(), potBar + 5, y + fontHeight);

         int cardWidth = width / 10;

         if(upCards.size() > 0){
            int x = 5;
            int numCards = upCards.size();
            for(int i = 0; i < numCards; i++){
               x = drawCard(g2, x, y + 10, (Card)upCards.elementAt(i), cardWidth, false);
               x += 10;
            }
         }

         y += barHeight;
         g2.setColor(Color.black);
         g2.drawLine(0, y, width, y);

         int numPlayers = players.size();

         if(players != null && numPlayers > 0){
            Player player = (Player)players.elementAt(0);
            int x = potBar + 5;
            y += fontHeight;
            if(!player.isIn())
               g2.setColor(Color.lightGray);
            g2.drawString("Pile: $" + player.getPileSize(), x, y);
            g2.setColor(Color.black);
            Vector cards = player.getCards();
            int numCards = cards.size();
            x = 5;
            y = y - fontHeight + 10;
            for(int i = 0; i < numCards; i++){
               x = drawCard(g2, x, y, (Card)cards.elementAt(i), cardWidth, true);
               x += 10;
            }

            if(numPlayers > 1){
               x = 0;
               int playerSize = width / (numPlayers - 1);
               for(int i = 1; i < numPlayers; i++, x += playerSize){
                  g2.setColor(Color.black);
                  if(i > 1)
                     g2.drawLine(x, 0, x, barHeight);
                  player = (Player)players.elementAt(i);
                  if(!player.isIn())
                     g2.setColor(Color.lightGray);
                  y = fontHeight;
                  g2.drawString("Player: " + i, x + 5, y);
                  y += fontHeight;
                  g2.drawString("Type: " + player.getType(), x + 5, y);
                  y += fontHeight;
                  g2.drawString("Pile: $" + player.getPileSize(), x + 5, y);
                  y += fontHeight;

                  if(player.isIn()){
                     cards = player.getCards();
                     numCards = cards.size();
                     cardWidth = playerSize / 4;
                     int cardX = x + 5;
                     for(int j = 0; j < numCards; j++){
                        cardX = drawCard(g2, cardX, y, (Card)cards.elementAt(j), cardWidth, false);
                        cardX += 3;
                     }
                  }
               }
            }
         }

         g.drawImage(doubleImage, 0, 0, this);
      }
   }

   public void componentHidden(ComponentEvent e){
   }

   public void componentMoved(ComponentEvent e){
   }

   public void componentResized(ComponentEvent e){
      width = getWidth();
      height = getHeight();
      needUpdate = true;
      repaint();
   }

   public void componentShown(ComponentEvent e){
   }

   public void actionPerformed(ActionEvent e){
      Object source = e.getSource();
      if(source == removePlayerItem){
         int numPlayers = players.size();
         if(numPlayers > 1){
            int playerSize = width / (numPlayers - 1);
            int index = popupX / playerSize + 1;
            pokerGame.removePlayer((Player)players.elementAt(index));
         }
      }
   }

   private void checkPopup(MouseEvent e){
      if(e.isPopupTrigger()){
         popupX = e.getX();
         int y = e.getY();
         if(y < barHeight && players.size() > 1)
            removePlayerPopup.show(this, popupX, y);
      }
   }

   public void mousePressed(MouseEvent e){
      checkPopup(e);
   }

   public void mouseReleased(MouseEvent e){
      checkPopup(e);
   }

   public void mouseClicked(MouseEvent e){
      checkPopup(e);
   }

   public void mouseEntered(MouseEvent e){
   }

   public void mouseExited(MouseEvent e){
   }
}

// vim:ts=3:et
