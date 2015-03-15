package players;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import poker.PokerEvent;
import poker.PokerGame;

public class BetDialog extends JDialog implements ActionListener{
   PokerGame pokerGame = null;

   JButton okButton = new JButton("Ok");

   JRadioButton betButton = new JRadioButton("Bet", true);
   JTextField betAmount = new JTextField("0");
   JRadioButton callButton = new JRadioButton("Call", false);
   JRadioButton foldButton = new JRadioButton("Fold", false);

   ButtonGroup buttonGroup = new ButtonGroup();

   public BetDialog(){
      super();
      setModal(true);
      setTitle("Bet");
      setSize(new Dimension(300, 200));
      setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

      buttonGroup.add(betButton);
      buttonGroup.add(callButton);
      buttonGroup.add(foldButton);

      betAmount.setPreferredSize(new Dimension(50, 21));

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(4, 1));

      JPanel subPanel = new JPanel();
      subPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      subPanel.add(betButton);
      subPanel.add(new JLabel("Amount: $"));
      subPanel.add(betAmount);
      panel.add(subPanel);

      subPanel = new JPanel();
      subPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      subPanel.add(callButton);
      panel.add(subPanel);

      subPanel = new JPanel();
      subPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      subPanel.add(foldButton);
      panel.add(subPanel);

      subPanel = new JPanel();
      okButton.addActionListener(this);
      subPanel.add(okButton);
      panel.add(subPanel);

      getContentPane().add(panel, BorderLayout.CENTER);
   }

   public void actionPerformed(ActionEvent event){
      Object source = event.getSource();
      if(source == okButton)
         hide();
   }

   public void setPokerGame(PokerGame pokerGame){
      this.pokerGame = pokerGame;
   }

   public PokerEvent createPokerEvent(){
      if(betButton.isSelected())
         return (new PokerEvent(PokerEvent.BET, Integer.parseInt(betAmount.getText())));
      else if(callButton.isSelected())
         return (new PokerEvent(PokerEvent.CALL));
      else if(foldButton.isSelected())
         return (new PokerEvent(PokerEvent.FOLD));
      else
         return (new PokerEvent());
   }
}

// vim:ts=3:et
