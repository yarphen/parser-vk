package com.sheremet.mainsteps;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Step0Menu extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8949915548753963927L;
	private JPanel panel = new JPanel();
	private JLabel  label = new JLabel("Click your module!");
	private JButton step1 = new JButton("Step1");
	private JButton step2 = new JButton("Step2");
	private JButton step3 = new JButton("Step3");
	private JButton step4 = new JButton("Step4");
	private JButton step5 = new JButton("Step5");

	public Step0Menu() {

		generalSettings();
		settingSizes();
		addingComponents();
		addingListeners();
		pack();
		setVisible(true);
	}

	private void settingSizes() {
		panel.setPreferredSize(new Dimension(830, 500));
		label.setSize(225, 50);
		step1.setSize(225, 50);
		step2.setSize(225, 50);
		step3.setSize(225, 50);
		step4.setSize(225, 50);
		step5.setSize(225, 50);

		label.setLocation(303, 50);
		step1.setLocation(303, 150);
		step2.setLocation(303, 210);
		step3.setLocation(303, 270);
		step4.setLocation(303, 330);
		step5.setLocation(303, 390);
	}

	private void addingComponents() {
		panel.add(step1);
		panel.add(step2);
		panel.add(step3);
		panel.add(step4);
		panel.add(step5);
		panel.add(label);
		add(panel);
	}

	private void generalSettings() {
		setTitle("Menu");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override public void windowOpened(WindowEvent arg0) {}
			@Override public void windowIconified(WindowEvent arg0) {}
			@Override public void windowDeiconified(WindowEvent arg0) {}
			@Override public void windowDeactivated(WindowEvent arg0) {}
			@Override public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
			@Override public void windowClosed(WindowEvent arg0) {}
			@Override public void windowActivated(WindowEvent arg0) {}
		});
		panel.setLayout(null);
	}


	private void addingListeners() {
		step1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Step1Preparser.main(null);
				dispose();
			}
		});
		step2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Step2PicturesDownloader.main(null);
				dispose();
			}
		});
		step3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Step3Merger.main(null);
				dispose();
			}
		});
		step4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Step4SizeMatcher.main(null);
				dispose();
			}
		});
		step5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Step5Expander.main(null);
				dispose();
			}
		});
	}
	public static void main(String[] args) {
		new Step0Menu();
	}
}
