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

public class UserTwo extends JFrame implements ActionListener, Runnable {
    JTextField text;
    JPanel textingArea;
    Box vertical = Box.createVerticalBox();
    BufferedReader reader;
    BufferedWriter writer;
    String name = "Munna Tripathi";

    UserTwo() {
        setLayout(null);

        JPanel Header = new JPanel();
        Header.setBackground(new Color(7, 94, 84));
        Header.setBounds(0, 0, 450, 70);
        add(Header);
        Header.setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);

        JLabel back = new JLabel(i3);
        back.setBounds(0, 20, 25, 25);
        Header.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/mirzapur.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);

        JLabel profilePicture = new JLabel(i6);
        profilePicture.setBounds(40, 10, 50, 50);
        Header.add(profilePicture);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);

        JLabel videoCall = new JLabel(i9);
        videoCall.setBounds(300, 20, 30, 30);
        Header.add(videoCall);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);

        JLabel phone = new JLabel(i12);
        phone.setBounds(360, 20, 35, 30);
        Header.add(phone);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);

        JLabel moreoption = new JLabel(i15);
        moreoption.setBounds(420, 20, 10, 25);
        Header.add(moreoption);

        JLabel groupName = new JLabel("Mirzapur");
        groupName.setForeground(Color.WHITE);
        groupName.setBounds(110, 15, 100, 18);
        groupName.setFont(new Font("SANS SERIF", Font.BOLD, 18));
        Header.add(groupName);

        JLabel groupMembers = new JLabel("Munna, Kaleen, Guddu, Bablu");
        groupMembers.setForeground(Color.WHITE);
        groupMembers.setBounds(110, 37, 160, 14);
        groupMembers.setFont(new Font("SANS SERIF", Font.BOLD, 11));
        Header.add(groupMembers);

        textingArea = new JPanel();
        textingArea.setBounds(5, 75, 440, 570);
        add(textingArea);
        textingArea.setLayout(null);
        setUndecorated(true);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SANS SERIF", Font.PLAIN, 16));
        add(text);

        JButton send = new JButton("SEND");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SANS SERIF", Font.PLAIN, 14));
        add(send);

        setSize(450, 700);
        setLocation(490, 50);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
        setFocusable(true);

        try {
            Socket socket = new Socket("localhost", 2003);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String out = "<html><p>" + name + "</p><p>" + text.getText() + "</p></html>";
        textingArea.setLayout(new BorderLayout());
        JPanel right = new JPanel(new BorderLayout());
        JPanel p2 = formatLabel(out);
        right.add(p2, BorderLayout.LINE_END);
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15));
        textingArea.add(vertical, BorderLayout.PAGE_START);

        try {
            writer.write(out);
            writer.write("\r\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        text.setText(" ");
        repaint();
        invalidate();
        validate();
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(0, 15, 0, 50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));

        panel.add(time);
        panel.add(output);
        return panel;
    }

    public void run() {
        try {
            String msg = "";
            while (true) {
                msg = reader.readLine();
                if (msg.contains(name)) {
                    continue;
                }

                JPanel panel = formatLabel(msg);
                JPanel left = new JPanel(new BorderLayout());
                left.setBackground(Color.WHITE);
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                textingArea.add(vertical, BorderLayout.PAGE_START);
                repaint();
                invalidate();
                validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        UserTwo two = new UserTwo();
        Thread t1 = new Thread(two);
        t1.start();
    }
}
