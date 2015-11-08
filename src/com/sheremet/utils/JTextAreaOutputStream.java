package com.sheremet.utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class JTextAreaOutputStream extends OutputStream {
	private JTextArea textArea;
	private JTextAreaOutputStream another;
	private boolean open = false;
	public JTextAreaOutputStream(JTextArea textArea, JTextAreaOutputStream albmOut) {
		this.textArea = textArea;
		another = albmOut;
	}
	@Override
	public void write(byte[] arg0) throws IOException {
		if (open&&another!=null)another.write(arg0);
		if (textArea!=null){
			textArea.append(new String(arg0));
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
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