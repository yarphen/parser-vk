package com.sheremet.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

public class JTextAreaOutputStream extends OutputStream {
	private JTextArea textArea;
	private PrintStream another;
	private boolean open = false;
	public JTextAreaOutputStream(JTextArea textArea, PrintStream albmOut) {
		this.textArea = textArea;
		another = albmOut;
	}

	@Override
	public void write(int b) throws IOException {
		if (open&&another!=null)another.write(b);
		if (textArea!=null){
			textArea.append(String.valueOf((char)b));
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean w) {
		this.open = w;
	}
}