package poker;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class PokerClient extends Thread implements PokerListener{
   private Socket client = null;
   private PokerGame pokerGame = null;

   private boolean isRunning = true;

   public PokerClient(Socket client, PokerGame pokerGame){
      this.client = client;
      this.pokerGame = pokerGame;
   }

   public PokerClient(String host, int port, PokerGame pokerGame) throws UnknownHostException, IOException{
      client = new Socket(host, port);
      this.pokerGame = pokerGame;
   }

   public void run(){
      isRunning = true;
      pokerGame.addPokerListener(this);

      try{
         ObjectInputStream in = new ObjectInputStream(client.getInputStream());
         ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

         while(isRunning){
            PokerEvent event = (PokerEvent)in.readObject();
            switch(event.getType()){
               default:
                  break;
            }
         }

         in.close();
         out.close();

         client.shutdownOutput();
         client.shutdownInput();
         client.close();
      }
      catch(Exception e){
      }

      pokerGame.removePokerListener(this);
      isRunning = false;
   }

   public void kill(){
      isRunning = false;
   }

   public void pokerEvent(PokerEvent event){
   }
}

// vim:ts=3:et
