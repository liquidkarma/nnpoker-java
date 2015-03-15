package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerDialog extends JDialog implements ActionListener{
   JButton okButton = new JButton("Ok");
   JButton cancelButton = new JButton("Cancel");

   JTextField port = new JTextField();

   PokerGame pokerGame = null;

   public ServerDialog(PokerGame pokerGame){
      super();

      this.pokerGame = pokerGame;

      setTitle("Start Server");
      setSize(new Dimension(200, 130));
      setModal(true);

      port.setPreferredSize(new Dimension(100, 21));
      port.setText(Integer.toString(PokerComm.DEFAULT_PORT));

      JPanel panel = new JPanel();
      panel.setLayout(new FlowLayout(FlowLayout.LEFT));
      panel.add(new JLabel("Port:"));
      panel.add(port);
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
         pokerGame.startServer(Integer.parseInt(port.getText()));
         setVisible(false);
      }
      else if(source == cancelButton)
         setVisible(false);
   }
}

// vim:ts=3:et
