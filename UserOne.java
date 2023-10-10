import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class UserOne extends JFrame implements ActionListener, Runnable {
    private JTextField text;
    private JPanel textingArea;
    private Box vertical;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name = "Kaleen Bhaiya";

    public UserOne() {
        // Set up the frame
        setTitle("Mirzapur Chat");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel header = createHeader();
        textingArea = createTextingArea();
        text = createTextField();
        JButton sendButton = createSendButton();

        // Initialize vertical box for message display
        vertical = Box.createVerticalBox();

        // Add components to the frame
        add(header, BorderLayout.NORTH);
        add(textingArea, BorderLayout.CENTER);
        add(text, BorderLayout.SOUTH);
        add(sendButton, BorderLayout.EAST);

        // Initialize the client socket
        try {
            Socket socket = new Socket("localhost", 2003);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(7, 94, 84));
        header.setLayout(new BorderLayout());

        // ... Add header components (back button, profile picture, etc.) here ...

        return header;
    }

    private JPanel createTextingArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // ... Add components for displaying messages here ...

        return panel;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("SANS SERIF", Font.PLAIN, 16));
        return textField;
    }

    private JButton createSendButton() {
        JButton sendButton = new JButton("SEND");
        sendButton.setBackground(new Color(7, 94, 84));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(this);
        sendButton.setFont(new Font("SANS SERIF", Font.PLAIN, 14));
        return sendButton;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String messageText = text.getText().trim();
        if (!messageText.isEmpty()) {
            sendMessage(name, messageText);
            text.setText(""); // Clear the text field after sending the message
        }
    }

    private void sendMessage(String senderName, String messageText) {
        // Format and display the message
        String formattedMessage = formatMessage(senderName, messageText);
        displayMessage(formattedMessage);

        // Send the message to the server
        try {
            writer.write(formattedMessage);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatMessage(String senderName, String messageText) {
        // Format the message with sender's name and current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(cal.getTime());

        return "<html><p>" + senderName + " (" + currentTime + ")</p><p>" + messageText + "</p></html>";
    }

    private void displayMessage(String message) {
        JPanel messagePanel = createMessagePanel(message);
        vertical.add(messagePanel);
        vertical.add(Box.createVerticalStrut(15)); // Add spacing between messages
        textingArea.add(vertical, BorderLayout.PAGE_START);
        validate();
    }

    private JPanel createMessagePanel(String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(message);
        label.setFont(new Font("Tahoma", Font.PLAIN, 16));
        label.setBackground(new Color(37, 211, 102));
        label.setOpaque(true);
        label.setBorder(new EmptyBorder(0, 15, 0, 50));
        panel.add(label);
        return panel;
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (!msg.contains(name)) {
                    displayMessage(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserOne userOne = new UserOne();
            Thread t1 = new Thread(userOne);
            t1.start();
            userOne.setVisible(true);
        });
    }
}
