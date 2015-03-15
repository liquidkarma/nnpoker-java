package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConnectDialog extends JDialog implements ActionListener{
   JButton okButton = new JButton("Ok");
   JButton cancelButton = new JButton("Cancel");

   JTextField hostName = new JTextField();
   JTextField port = new JTextField();

   PokerGame pokerGame = null;

   public ConnectDialog(PokerGame pokerGame){
      super();

      this.pokerGame = pokerGame;

      setTitle("Connect");
      setSize(new Dimension(200, 130));
      setModal(true);

      hostName.setPreferredSize(new Dimension(100, 21));
      hostName.setText(PokerComm.DEFAULT_HOST);
      port.setPreferredSize(new Dimension(100, 21));
      port.setText(Integer.toString(PokerComm.DEFAULT_PORT));

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(2, 1));
      JPanel subPanel = new JPanel();
      subPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      subPanel.add(new JLabel("Host:"));
      subPanel.add(hostName);
      panel.add(subPanel);
      subPanel = new JPanel();
      subPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      subPanel.add(new JLabel("Port:"));
      subPanel.add(port);
      panel.add(subPanel);
      getContentPane().add(panel, BorderLayout.CENTER);

      panel = new JPanel();
      okButton.addActionListener(this);
      panel.add(okButton);
      cancelButton.addActionListener(this);
      panel.add(cancelButton);
      getContentPane().add(panel, BorderLayout.SOUTH);
   }

   public void actionPerformed(ActionEvent e){
      Object source = e.getSource();
      if(source == okButton){
         pokerGame.connect(hostName.getText(), Integer.parseInt(port.getText()));
         setVisible(false);
      }
      else if(source == cancelButton)
         setVisible(false);
   }
}

// vim:ts=3:et
