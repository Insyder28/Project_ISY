package gui;

import threading.Buffer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectWindow extends JFrame implements ActionListener {
    JButton connect = new JButton();
    JButton exit = new JButton();
    JLabel label = new JLabel();
    JButton back = new JButton();

    JTextField serverIP = new JTextField();

    Buffer<String> ipBuffer = new Buffer<>();

    public ConnectWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(720, 720);
        setVisible(true);
        setTitle("Game Launcher");
        setLocationRelativeTo(null);


        connect.setText("Connect");
        connect.addActionListener(this);
        connect.setBounds(200, 300, 100, 50);

        label.setBounds(250,200,200,100);
        label.setText("Enter IP address");

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);

        serverIP.setLayout(null);
        serverIP.setBounds(250, 500, 200, 50);
        serverIP.setVisible(true);
        serverIP.replaceSelection("Enter IP here");
        serverIP.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        add(back);
        add(connect);
        add(label);
        add(exit);
        add(serverIP);

        back.setBounds(20, 20, 75, 75);
        back.setText("Return");
        back.addActionListener(this);

        exit.setBounds(600, 20, 75, 75);
        exit.setText("Exit");
        exit.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect){
            String ip = serverIP.getText();
            if (invalidIp(ip)) JOptionPane.showMessageDialog(this, "Please enter a valid IP address.", "Invalid IP address", JOptionPane.ERROR_MESSAGE);
            else {
                ipBuffer.set(ip);
                setVisible(false);
            }
        }

        else if(e.getSource() == back){
            setVisible(false);
        }

        else if (e.getSource() == exit){
            setVisible(false);
        }
    }

    public String getIp() {
        setLocationRelativeTo(null);
        setVisible(true);
        return ipBuffer.await();
    }


    private boolean invalidIp(String ip) {
        try {
            String address;

            if (ip.contains(":")) {
                String[] addressAndPort = ip.split(":");
                address = addressAndPort[0];

                int port = Integer.parseInt(addressAndPort[1]);
                if (port < 0 || port > 65535) return true;
            }
            else {
                address = ip;
            }

            if (address.equalsIgnoreCase("localhost")) return false;

            String[] numbers = address.split("\\.");
            if (numbers.length != 4) return true;

            for (String number : numbers) {
                int num = Integer.parseInt(number);
                if (num < 0 || num > 255) return true;
            }
        }
        catch (NumberFormatException e) {
            return true;
        }

        return false;
    }
}
