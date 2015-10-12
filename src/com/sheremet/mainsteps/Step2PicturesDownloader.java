package com.sheremet.mainsteps;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sheremet.threads.PicturesDownloaderThread;
import com.sheremet.utils.JTextAreaOutputStream;

public class Step2PicturesDownloader extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7731680534017232431L;
	private JTextArea header = new JTextArea();
	private JTextArea log = new JTextArea();
	private JButton sBtn = new JButton("Start");
	private JButton prBtn = new JButton("Pause");
	private JButton ofBtn = new JButton("Open folder..");
	private JButton nsBtn = new JButton("Next step ->");
	private PicturesDownloaderThread picturesDownloaderThread;
	private boolean paused = false;
	private boolean started = false;
	private JPanel panel = new JPanel();
	private JScrollPane headerScroll = new JScrollPane(header);
	private JScrollPane logScroll = new JScrollPane(log);
	private PrintStream txtOut = new PrintStream(new JTextAreaOutputStream(log, null));
	private void settingSizes() {
		panel.setPreferredSize(new Dimension(830, 500));
		sBtn.setSize(225, 45);
		prBtn.setSize(225, 45);
		ofBtn.setSize(225, 65);
		headerScroll.setSize(770, 180);
		nsBtn.setSize(225, 45);
		logScroll.setSize(535, 245);

		sBtn.setLocation(580, 225);
		prBtn.setLocation(580, 285);
		ofBtn.setLocation(580, 345);
		nsBtn.setLocation(580, 425);
		logScroll.setLocation(30, 225);
		headerScroll.setLocation(30, 30);
	}
	public Step2PicturesDownloader() {



		generalSettings();
		additionalSettings();
		settingSizes();
		addingComponents();
		addingListeners();
		pack();//for resizing

	}
	private void addingListeners() {
		ofBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					File imgdir = new File("images");
					if (!imgdir.exists())
						imgdir.mkdirs();
					Desktop.getDesktop().open(imgdir);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		});
		nsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (new File("Step3.jar").exists()){
					try {
						Runtime.getRuntime().exec("java -jar Step3.jar");
						System.exit(0);
					} catch (IOException e) {}
				}else{
					JOptionPane.showMessageDialog(null, "Next step module not found!");
				}
			}
		});
		prBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (started)
					if (paused){
						picturesDownloaderThread.resume();
						paused=false;
						prBtn.setText("Pause");
					}
					else{
						picturesDownloaderThread.suspend();
						prBtn.setText("Resume");
						paused=true;
					}

			}
		});
//		sBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				if (started==false){
//					if (JOptionPane.showOptionDialog(null, 
//							"Do you really want to start process?\nIt will OVERWRITE all the images you have already downloaded.\nPlease, check list of albums carefully!\nContinue?", 
//							"Starting?", 
//							JOptionPane.YES_NO_OPTION, 
//							JOptionPane.QUESTION_MESSAGE, 
//							null, null, null)==JOptionPane.YES_OPTION){
//						log.setText("");
//						picturesDownloaderThread = new PicturesDownloaderThread(header.getText().split("\n"), new ActionListener() {
//							@Override
//							public void actionPerformed(ActionEvent arg0) {
//								picturesDownloaderThread = null;
//								header.setEnabled(true);
//								prBtn.setEnabled(false);
//								nsBtn.setEnabled(true);
//								started = false;
//								sBtn.setText("Start");
//							}
//
//						},custom);
//						picturesDownloaderThread.start();
//						header.setEnabled(false);
//						prBtn.setEnabled(true);
//						nsBtn.setEnabled(false);
//						started = true;
//						sBtn.setText("Stop");
//						prBtn.setText("Pause");
//					}
//				}else{
//					if (JOptionPane.showOptionDialog(null, 
//							"Do you really want to stop process?", 
//							"Quit?", 
//							JOptionPane.YES_NO_OPTION, 
//							JOptionPane.QUESTION_MESSAGE, 
//							null, null, null)==JOptionPane.YES_OPTION){
//						PrintWriter pw = picturesDownloaderThread.getWriter();
//						if (pw!=null)
//							synchronized (pw) {
//								pw.close();
//							}
//						pw = picturesDownloaderThread.getLogWriter();
//						if (pw!=null)
//							synchronized (pw) {
//								pw.close();
//							}
//						picturesDownloaderThread.stop();
//						picturesDownloaderThread = null;
//						header.setEnabled(true);
//						prBtn.setEnabled(false);
//						nsBtn.setEnabled(true);
//						started = false;
//						sBtn.setText("Start");
//					}
//				}
//			}
//		});
	}
	private void addingComponents() {
		panel.add(ofBtn);
		panel.add(nsBtn);
		panel.add(logScroll);
		panel.add(sBtn);
		panel.add(prBtn);
		panel.add(headerScroll);
		add(panel);

	}
	private void additionalSettings() {
		log.setText("This is log console.");
		log.setEditable(false);
		nsBtn.setEnabled(false);
		prBtn.setEnabled(false);
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
				if (picturesDownloaderThread!=null&&picturesDownloaderThread.isAlive())
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
	public static void main(String[] args) {
		new Step2PicturesDownloader();
	}
}
