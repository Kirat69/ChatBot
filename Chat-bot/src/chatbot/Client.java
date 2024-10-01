package chatbot;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class Client extends JFrame {

	private static final long serialVersionUID = 1L;

	BufferedReader br1;
	PrintWriter out;
	Socket socket;

	private JLabel heading = new JLabel("Client ChatBot");
	private JTextArea textarea = new JTextArea();
	private JTextField textfield = new JTextField();
	private Font font = new Font("Roboto", Font.PLAIN, 20);

	public Client() {

		try {
			System.out.println("Sending a Request");
			socket = new Socket("127.0.0.1", 6969);
			System.out.println("Request Accepted");

			br1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			createGui();
			handleEvent();

			startReading();

		} catch (Exception e) {
			System.out.println("Constructor Client : "+e);
		}
	}

	public void createGui() {
		
		this.setTitle("Client ChatBot Area");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		heading.setHorizontalAlignment(SwingConstants.CENTER);

		heading.setFont(font);
		textarea.setFont(font);
		textarea.setEditable(false);
		textfield.setFont(font);

		setLayout(new BorderLayout());

		this.add(heading, BorderLayout.NORTH);
		JScrollPane pane=new JScrollPane(textarea);
		this.add(pane, BorderLayout.CENTER);
		this.add(textfield, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	private void handleEvent() {

		textfield.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == 10) {
					
					String content = textfield.getText();
					textarea.append("Me : " + content + "\n");
					out.println(content);
					out.flush();
					textfield.setText("");
					textfield.requestFocus();
				}

			}
		});

	}

	public void startReading() {

		Runnable r1 = () -> {
			System.out.println("Reader is Started");
			try {
				while (true) {
					
					String msg = br1.readLine();
					if (msg.equals("exit")) {
						textarea.append("Server Stopped Interaction !.." + "\n");
						textfield.setEnabled(false);
						socket.close();
						break;
					}

					textarea.append("Server : " + msg + "\n");
 
				}

			} catch (Exception e) {
				System.out.println("Startreading : "+e);
			}
		};
		new Thread(r1).start();

	}

	public static void main(String args[]) {
		System.out.println("this is Client !");
		new Client();
	}
}
