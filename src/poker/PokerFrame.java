package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import players.HumanPlayer;

public class PokerFrame extends JFrame implements ActionListener{
   private static int width  = 800;
   private static int height = 600;

   private ImageIcon windowIcon = null;

   private PokerGame pokerGame = new PokerGame(HumanPlayer.class);
   private PokerPanel pokerPanel = new PokerPanel(pokerGame);

   private ConnectDialog connectDialog = new ConnectDialog(pokerGame);
   private ServerDialog serverDialog = new ServerDialog(pokerGame);
   private AddPlayerDialog addPlayerDialog = new AddPlayerDialog(pokerGame);

   private JMenuBar menuBar = new JMenuBar();
   private JMenu fileMenu = new JMenu("File");
   private JMenuItem dealItem = new JMenuItem("Deal", KeyEvent.VK_D);
   private JMenuItem addItem = new JMenuItem("Add Player", KeyEvent.VK_A);
   private JMenuItem connectItem = new JMenuItem("Connect", KeyEvent.VK_C);
   private JMenuItem serverItem = new JMenuItem("Start Server", KeyEvent.VK_S);
   private JMenuItem quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);

   public PokerFrame(){
      setTitle("Poker");
      setSize(new Dimension(width, height));
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      ClassLoader cl = getClass().getClassLoader();
      windowIcon = new ImageIcon(cl.getResource("images/spade.png"));
      setIconImage(windowIcon.getImage());

      getContentPane().add(pokerPanel, BorderLayout.CENTER);

      dealItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
      dealItem.addActionListener(this);
      fileMenu.add(dealItem);

      addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
      addItem.addActionListener(this);
      fileMenu.add(addItem);

      connectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
      connectItem.addActionListener(this);
      fileMenu.add(connectItem);

      serverItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
      serverItem.addActionListener(this);
      fileMenu.add(serverItem);

      fileMenu.addSeparator();

      quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
      quitItem.addActionListener(this);
      fileMenu.add(quitItem);

      fileMenu.setMnemonic(KeyEvent.VK_F);
      menuBar.add(fileMenu);

      setJMenuBar(menuBar);
   }

   public void actionPerformed(ActionEvent e){
      Object source = e.getSource();
      if(source == dealItem)
         pokerGame.start();
      else if(source == addItem)
         addPlayerDialog.show();
      else if(source == connectItem){
         if(pokerGame.isConnected()){
            pokerGame.disconnect();
            connectItem.setText("Connect");
         }
         else{
            connectDialog.show();
            if(pokerGame.isConnected())
               connectItem.setText("Disconnect");
         }
      }
      else if(source == serverItem){
         if(pokerGame.isServer()){
            pokerGame.shutdownServer();
            serverItem.setText("Start Server");
         }
         else{
            serverDialog.show();
            if(pokerGame.isServer())
               serverItem.setText("Shutdown Server");
         }
      }
      else if(source == quitItem)
         System.exit(0);
   }
}

// vim:ts=3:et
