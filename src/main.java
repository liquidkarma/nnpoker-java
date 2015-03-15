import javax.swing.UIManager;

import poker.PokerFrame;

public class main{
   public static void main(String [] args){
      PokerFrame pokerFrame = new PokerFrame();
      pokerFrame.show();
   }

   static{
      try{
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
}

// vim:ts=3:et
