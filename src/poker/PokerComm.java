package poker;

public class PokerComm{
   public static final String DEFAULT_HOST = "localhost";
   public static final int DEFAULT_PORT = 6666;

   private String host = DEFAULT_HOST;
   private int port = DEFAULT_PORT;

   private PokerServer pokerServer = null;
   private PokerClient pokerClient = null;

   private PokerGame pokerGame = null;

   public PokerComm(PokerGame pokerGame){
      this.pokerGame = pokerGame;
   }

   public void startServer(int port){
      this.port = port;
      shutdownServer();
      pokerServer = new PokerServer(port, pokerGame);
      pokerServer.start();
   }

   public void shutdownServer(){
      if(pokerServer != null){
         pokerServer.kill();
         pokerServer = null;
      }
   }

   public void connect(String host, int port){
      disconnect();
      this.host = host;
      this.port = port;

      try{
         pokerClient = new PokerClient(host, port, pokerGame);
         pokerClient.start();
      }
      catch(Exception e){
         e.printStackTrace();
         pokerClient = null;
      }
   }

   public void disconnect(){
      if(pokerClient != null){
         pokerClient.kill();
         pokerClient = null;
      }
   }

   public boolean isConnected(){
      return (pokerClient != null);
   }

   public boolean isServer(){
      return (pokerServer != null);
   }
}

// vim:ts=3:et
