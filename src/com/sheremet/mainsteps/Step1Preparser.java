package com.sheremet.mainsteps;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sheremet.threads.PreparserThread;
import com.sheremet.utils.JTextAreaOutputStream;

public class Step1Preparser extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7383456702568975064L;
	private JLabel label = new JLabel("Input here list of albums (hyperlinks - each in new line)");
	private JTextArea input = new JTextArea();
	private JTextArea log = new JTextArea();
	private JButton sBtn = new JButton("Start");
	private JButton prBtn = new JButton("Pause");
	private JButton ofBtn = new JButton("Open folder..");
	private JButton nsBtn = new JButton("Next step ->");
	private PreparserThread preparserThread;
	private boolean paused = false;
	private boolean started = false;
	private JPanel panel = new JPanel();
	private JScrollPane inputScroll = new JScrollPane(input);
	private JTextArea albumLog = new JTextArea();
	private JScrollPane albumLogScroll = new JScrollPane(albumLog );
	private PrintStream albmOut = new PrintStream(new JTextAreaOutputStream(albumLog, null));
	private JScrollPane logScroll = new JScrollPane(log);
	private JTextAreaOutputStream custom = new JTextAreaOutputStream(log, albmOut);
	private PrintStream txtOut = new PrintStream(custom);

	public Step1Preparser() {



		generalSettings();
		additionalSettings();
		settingSizes();
		addingComponents();
		addingListeners();

		pack();//for resizing

	}
	private void settingSizes() {
		panel.setPreferredSize(new Dimension(830, 500));
		label.setSize(775, 40);
		sBtn.setSize(225, 45);
		prBtn.setSize(225, 45);
		ofBtn.setSize(225, 65);
		inputScroll.setSize(377, 140);
		albumLogScroll.setSize(378, 140);
		nsBtn.setSize(225, 45);
		logScroll.setSize(535, 245);

		albumLogScroll.setLocation(422, 70);
		sBtn.setLocation(580, 225);
		prBtn.setLocation(580, 285);
		ofBtn.setLocation(580, 345);
		nsBtn.setLocation(580, 425);
		logScroll.setLocation(30, 225);
		inputScroll.setLocation(30, 70);
		label.setLocation(30, 30);
	}
	private void addingComponents() {
		panel.add(label);
		panel.add(ofBtn);
		panel.add(nsBtn);
		panel.add(logScroll);
		panel.add(sBtn);
		panel.add(prBtn);
		panel.add(inputScroll);
		panel.add(albumLogScroll);
		add(panel);

	}
	private void generalSettings() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override public void windowOpened(WindowEvent arg0) {}
			@Override public void windowIconified(WindowEvent arg0) {}
			@Override public void windowDeiconified(WindowEvent arg0) {}
			@Override public void windowDeactivated(WindowEvent arg0) {}
			@Override public void windowClosing(WindowEvent arg0) {
				if (preparserThread!=null&&preparserThread.isAlive())
					javax.swing.JOptionPane.showMessageDialog(null, "You can't until you stop the process!!");
				else
					System.exit(0);
			}
			@Override public void windowClosed(WindowEvent arg0) {}
			@Override public void windowActivated(WindowEvent arg0) {}
		});
		System.setOut(txtOut);
		System.setErr(txtOut);
		setVisible(true);
		panel.setLayout(null);
	}
	private void additionalSettings() {
		input.setText("Input albums..");
		log.setText("This is log console.");
		albumLog.setText("This is album log console.");
		log.setEditable(false);
		albumLog.setEditable(false);
		nsBtn.setEnabled(false);
		prBtn.setEnabled(false);
	}
	private void addingListeners() {
		ofBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File("albums"));
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		});
		nsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (new File("Step2.jar").exists()){
					try {
						Runtime.getRuntime().exec("java -jar Step2.jar");
						System.exit(0);
					} catch (IOException e) {}
				}else{
					JOptionPane.showMessageDialog(null, "Next step module not found!");
				}
			}
		});
		input.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (input.getText().isEmpty())
					input.setText("Input albums..");
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				if (input.getText().equals("Input albums.."))
					input.setText("");
			}
		});
		prBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (started)
					if (paused){
						preparserThread.resume();
						paused=false;
						prBtn.setText("Pause");
					}
					else{
						preparserThread.suspend();
						prBtn.setText("Resume");
						paused=true;
					}

			}
		});
		sBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (started==false){
					if (JOptionPane.showOptionDialog(null, 
							"Do you really want to start process?\nIt will OVERWRITE all the albums you have already done.\nPlease, check list of albums carefully!\nContinue?", 
							"Starting?", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, null, null)==JOptionPane.YES_OPTION){
						log.setText("");
						albumLog.setText("");
						preparserThread = new PreparserThread(input.getText().split("\n"), new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								preparserThread = null;
								input.setEnabled(true);
								prBtn.setEnabled(false);
								nsBtn.setEnabled(true);
								started = false;
								sBtn.setText("Start");
							}

						},custom);
						preparserThread.start();
						input.setEnabled(false);
						prBtn.setEnabled(true);
						nsBtn.setEnabled(false);
						started = true;
						sBtn.setText("Stop");
						prBtn.setText("Pause");
					}
				}else{
					if (JOptionPane.showOptionDialog(null, 
							"Do you really want to stop process?", 
							"Quit?", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, null, null)==JOptionPane.YES_OPTION){
						PrintWriter pw = preparserThread.getWriter();
						if (pw!=null)
							synchronized (pw) {
								pw.close();
							}
						pw = preparserThread.getLogWriter();
						if (pw!=null)
							synchronized (pw) {
								pw.close();
							}
						preparserThread.stop();
						preparserThread = null;
						input.setEnabled(true);
						prBtn.setEnabled(false);
						nsBtn.setEnabled(true);
						started = false;
						sBtn.setText("Start");
					}
				}
			}
		});
	}
	public static void main(String[] args)  {

		new Step1Preparser();
	}
}
