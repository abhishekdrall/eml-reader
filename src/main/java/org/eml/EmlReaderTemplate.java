package org.eml;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EmlReaderTemplate extends JFrame {


    JLabel l1, l2, l3;
    JTextField tf1;
    JButton btn2, btn1;

    /* Doc: Swing Application to Read Eml files*/
    EmlReaderTemplate() {
        JFrame frame = new JFrame("Eml Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l1 = new JLabel("Eml Reader");
        l1.setForeground(Color.gray);
        l1.setFont(new Font("Sherif", Font.BOLD, 26));

        l2 = new JLabel("Folder path:");
        l2.setFont(new Font("Sherif", Font.BOLD, 18));

        l3 = new JLabel("Note: The application reads the .eml files and extracts senders name and subject to a new file inside 'C:\\Eml Output'.");
        l3.setFont(new Font("Sheriff", Font.ITALIC, 12));

        tf1 = new JTextField();

        // defining the browse button
        btn1 = new JButton("Browse");
        btn1.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                tf1.setText(file.getAbsolutePath());
                tf1.setFont(new Font("Sherif", Font.BOLD, 14));
            }
        });

        // defining the Read Eml button
        btn2 = new JButton("Read Eml");
        btn2.addActionListener(e -> {
            String result = null;
            try {
                // calls the initial method with path as selected by user using browse button
                //Start of the main logic.
                result = EmlReader.initial(tf1.getText());
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Unsuccessful Eml file
            if(!result.equals("Eml reading successful")) {
                JOptionPane.showMessageDialog(this, "Please select a valid file or folder with valid files",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Successful Eml file
            else {
                SuccessDialogueBox successBox = new SuccessDialogueBox();
                successBox.setVisible(true);
                JLabel label = new JLabel("<html><div style='text-align: center;'>Status:<br/>" +result+"</div></html>", SwingConstants.CENTER);
                label.setFont(new Font("Sheriff", Font.BOLD, 16));
                label.setHorizontalAlignment(0);
                successBox.setLocationRelativeTo(null);
                successBox.getContentPane().add(label);
            }
        });

        l1.setBounds(230, 50, 400, 30);
        l2.setBounds(80, 110, 280, 30);
        l3.setBounds(80, 130, 400, 30);
        tf1.setBounds(250, 110, 230, 30);
        btn1.setBounds(480,110,80,30);
        btn2.setBounds(260, 180, 100, 30);

        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(tf1);
        frame.add(btn1);
        frame.add(btn2);

        frame.setSize(670, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    //Main function to start the application.
    public static void main(String[] args) {
        new EmlReaderTemplate();
    }

}