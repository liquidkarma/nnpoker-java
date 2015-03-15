package poker;

import java.net.ServerSocket;
import java.net.SocketTimeoutException;

public class PokerServer extends Thread{
   private int port = 0;
   private PokerGame pokerGame = null;
   private boolean isRunning = false;

   public PokerServer(int port, PokerGame pokerGame){
      this.port = port;
      this.pokerGame = pokerGame;
   }

   public void run(){
      isRunning = true;

      try{
         ServerSocket ss = new ServerSocket(port);
         ss.setSoTimeout(1000); // set timeout to 1 second
         while(isRunning){
            try{
               PokerClient pokerClient = new PokerClient(ss.accept(), pokerGame);
               pokerClient.start();
            }
            catch(SocketTimeoutException timeoutException){
            }
            catch(Exception e){
               e.printStackTrace();
            }
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }

      isRunning = false;
   }

   public void kill(){
      isRunning = false;
   }
}

// vim:ts=3:et
