package com.bograntex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import com.bograntex.view.ProgressBarRelatorio;

public class Main {
	
	private ImageIcon image;
	
	public Main() {
		this.image = new ImageIcon("resources/img/icon.png");
	}
	
	
	private void createAndShowGUI() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("SIGI - SISTEMA DE INF. GERAIS INDUSTRIAL");
        JComponent newContentPane = new ProgressBarRelatorio();
        newContentPane.setBackground(Color.GRAY);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.setSize(300, 350);
        frame.setLocation(((dim.width/2)-(frame.getSize().width/2)), ((dim.height/2)-(frame.getSize().height/2)));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JInternalFrame.EXIT_ON_CLOSE);
        frame.setIconImage(this.image.getImage());
    }
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Main main =  new Main();
            	main.createAndShowGUI();
            }
        });
	}

}
