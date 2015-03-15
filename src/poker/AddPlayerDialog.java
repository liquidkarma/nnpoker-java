package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;

import players.Player;

public class AddPlayerDialog extends JDialog implements ActionListener{
   private static final String pkg = "players";
   private static final String [] predefinedPlayers = {
      "HumanPlayer",
      "NNPlayer",
      "RandomPlayer"
   };

   private JButton okButton = new JButton("Ok");
   private JButton cancelButton = new JButton("Cancel");

   private JComboBox playerCombo = new JComboBox();

   private PokerGame pokerGame = null;

   public AddPlayerDialog(PokerGame pokerGame){
      super();

      this.pokerGame = pokerGame;

      setTitle("Add Player");
      setSize(new Dimension(200, 130));
      setModal(true);

      JPanel panel = new JPanel();
      panel.add(playerCombo);
      getContentPane().add(panel, BorderLayout.CENTER);

      panel = new JPanel();
      okButton.addActionListener(this);
      panel.add(okButton);
      cancelButton.addActionListener(this);
      panel.add(cancelButton);
      getContentPane().add(panel, BorderLayout.SOUTH);

      okButton.requestFocus();

      findPlayers();
   }

   public void show(){
      super.show();
   }

   public void actionPerformed(ActionEvent e){
      Object source = e.getSource();
      if(source == okButton){
         try{
            String className = pkg + "." + playerCombo.getSelectedItem().toString();
            pokerGame.addPlayer((Player)Class.forName(className).newInstance());
         }
         catch(Exception ex){
            ex.printStackTrace();
         }
         setVisible(false);
      }
      else if(source == cancelButton)
         setVisible(false);
   }

   private void findPlayers(){
      playerCombo.removeAllItems();

      File currentDir = new File("./" + pkg);
      if(currentDir.exists()){
         String [] classFiles = currentDir.list();
         int numFiles = classFiles.length;
         for(int i = 0; i < numFiles; i++){
            String file = classFiles[i];
            if(file.endsWith(".class")){
               file = file.substring(0, file.indexOf('.'));
               if(!file.equals("Player")){
                  String className = pkg + "." + file;
                  try{
                     Class c = Class.forName(className);
                     if(c == Player.class || c.getSuperclass() == Player.class)
                        playerCombo.addItem(file);
                  }
                  catch(Exception e){
                     e.printStackTrace();
                  }
               }
            }
         }
      }
      else{
/*
         ClassLoader cl = getClass().getClassLoader();

         String jarFile = cl.getResource("main.class").toString();
         jarFile = jarFile.substring(0, jarFile.indexOf('!'));
         System.err.println("jar file = " + jarFile);

         int numPredefinedPlayers = predefinedPlayers.length;
         for(int i = 0; i < numPredefinedPlayers; i++){
            String className = pkg + "." + predefinedPlayers[i];
            try{
               Class c = cl.loadClass(className);
               if(c == Player.class || c.getSuperclass() == Player.class)
                  playerCombo.addItem(predefinedPlayers[i]);
            }
            catch(Exception e){
               e.printStackTrace();
            }
         }
*/

         int numPredefinedPlayers = predefinedPlayers.length;
         for(int i = 0; i < numPredefinedPlayers; i++)
            playerCombo.addItem(predefinedPlayers[i]);
      }
   }
}

// vim:ts=3:et
