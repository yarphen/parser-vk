package com.sheremet.mainsteps;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sheremet.threads.PicturesDownloaderThread;
import com.sheremet.utils.JTextAreaOutputStream;

@SuppressWarnings("deprecation")
public class Step2PicturesDownloader extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7731680534017232431L;
	private JTextArea input = new JTextArea();
	private JTextArea log = new JTextArea();
	private JButton sBtn = new JButton("Start");
	private JButton rBtn = new JButton("Refresh");
	private JButton ofBtn = new JButton("Open folder..");
	private JButton nsBtn = new JButton("Next step ->");
	private PicturesDownloaderThread picturesDownloaderThread;
	private boolean started = false;
	private JPanel panel = new JPanel();
	private JScrollPane inputScroll = new JScrollPane(input);
	private JScrollPane logScroll = new JScrollPane(log);
	private PrintStream txtOut = new PrintStream(new JTextAreaOutputStream(log, null));
	private void settingSizes() {
		panel.setPreferredSize(new Dimension(830, 500));
		sBtn.setSize(225, 45);
		rBtn.setSize(225, 45);
		ofBtn.setSize(225, 65);
		inputScroll.setSize(770, 180);
		nsBtn.setSize(225, 45);
		logScroll.setSize(535, 245);

		sBtn.setLocation(580, 225);
		rBtn.setLocation(580, 285);
		ofBtn.setLocation(580, 345);
		nsBtn.setLocation(580, 425);
		logScroll.setLocation(30, 225);
		inputScroll.setLocation(30, 30);
	}
	public Step2PicturesDownloader() {
		generalSettings();
		additionalSettings();
		settingSizes();
		addingComponents();
		addingListeners();
		pack();//for resizing
		setVisible(true);

	}
	private void addingListeners() {
		input.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (input.getText().isEmpty())
					refresh();
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				if (input.getText().equals("No files found.."))
					input.setText("");
			}
		});
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
				Step3Merger.main(null);
				dispose();
			}
		});
		rBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		sBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (started==false){
					if (JOptionPane.showOptionDialog(null, 
							"Do you really want to start process?\nIt will OVERWRITE all the images you have already downloaded.\nContinue?", 
							"Starting?", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, null, null)==JOptionPane.YES_OPTION){
						log.setText("");
						picturesDownloaderThread = new PicturesDownloaderThread(input.getText().split("\n"),new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								picturesDownloaderThread = null;
								input.setEnabled(true);
								rBtn.setEnabled(true);
								nsBtn.setEnabled(true);
								started = false;
								sBtn.setText("Start");
							}

						});
						picturesDownloaderThread.start();
						input.setEnabled(false);
						rBtn.setEnabled(false);
						nsBtn.setEnabled(false);
						started = true;
						sBtn.setText("Stop");
					}
				}else{
					if (JOptionPane.showOptionDialog(null, 
							"Do you really want to stop process?", 
							"Quit?", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, null, null)==JOptionPane.YES_OPTION){
						BufferedWriter br = picturesDownloaderThread.getWriter();
						if (br!=null)
							synchronized (br) {
								try {
									br.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						Scanner sc = picturesDownloaderThread.getScanner();
						if (sc!=null)
							synchronized (sc) {
								sc.close();
							}
						FileOutputStream fos = PicturesDownloaderThread.getOs();
						if (fos!=null)
							synchronized (fos) {
								try {
									fos.close();
								} catch (IOException e) {}
							}
						picturesDownloaderThread.stop();
						picturesDownloaderThread = null;
						input.setEnabled(true);
						rBtn.setEnabled(true);
						nsBtn.setEnabled(true);
						started = false;
						sBtn.setText("Start");
					}
				}
			}
		});
	}
	private void addingComponents() {
		panel.add(ofBtn);
		panel.add(nsBtn);
		panel.add(logScroll);
		panel.add(sBtn);
		panel.add(rBtn);
		panel.add(inputScroll);
		add(panel);

	}
	private void additionalSettings() {
		refresh();
		log.setText("This is log console.");
		log.setEditable(false);
		nsBtn.setEnabled(false);
		rBtn.setEnabled(true);
	}
	private void generalSettings() {
		setTitle("Step2");
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
				else{
					Step0Menu.main(null);
					dispose();
				}
			}
			@Override public void windowClosed(WindowEvent arg0) {}
			@Override public void windowActivated(WindowEvent arg0) {}
		});
		System.setOut(txtOut);
		System.setErr(txtOut);
		panel.setLayout(null);
	}
	private void refresh() {
		File csvdir = new File("albums");
		File [] files = csvdir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().endsWith(".csv")&&!arg0.isDirectory();
			}
		});
		input.setText("");
		if (files!=null)
			for (File f: files)
				input.append(f.getAbsolutePath()+"\n");
		if (input.getText().isEmpty())
			input.setText("No files found..");
	}
	public static void main(String[] args) {
		new Step2PicturesDownloader();
	}
}
