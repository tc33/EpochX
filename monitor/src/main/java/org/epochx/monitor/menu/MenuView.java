/* 
 * Copyright 2007-2012
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.monitor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.epochx.monitor.Monitor;


/**
 * The view menu.
 */
public class MenuView extends Menu {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 1430021550373815161L;
	
	private final JMenuItem centre;
	private final JMenuItem autoResize;
	private final JMenuItem maximize;

	MenuView(Monitor m) {
		super(m);
		setName("View Menu");
		setText("View");
		setMnemonic('V');
		
//		ButtonGroup lnfGroup = new ButtonGroup();
//		LookAndFeelInfo[] lnfis =  UIManager.getInstalledLookAndFeels();
//		for(LookAndFeelInfo lnfi : lnfis) {
//			LnFButton item = new LnFButton(lnfi);
//			lnfGroup.add(item);
//			add(item);
//		}

		// centre Item.
		centre = new JMenuItem("Centre");
		centre.setMnemonic('C');
		centre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		centre.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				monitor.setLocationRelativeTo(null);
			}
		});
		add(centre);

		// autoResize Item.
		autoResize = new JMenuItem("Auto-Resize");
		autoResize.setMnemonic('R');
		autoResize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		autoResize.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				monitor.setVisible(false);
				monitor.pack();
				monitor.setVisible(true);
			}
		});
		add(autoResize);

		// maximize Item.
		maximize = new JMenuItem("Maximize");
		maximize.setMnemonic('M');
		maximize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
		maximize.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				monitor.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		});
		add(maximize);
	}
	
	
	@SuppressWarnings("unused")
	private class LnFButton extends JRadioButtonMenuItem implements ActionListener {
		
		/**
		 * The serialVersionUID.
		 */
		private static final long serialVersionUID = -8701018985049174064L;
		private LookAndFeelInfo lnfi;
		
		LnFButton (LookAndFeelInfo lnfi) {
			super(lnfi.getName());
			this.lnfi = lnfi;
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e) {
			try {
				UIManager.setLookAndFeel(lnfi.getClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			SwingUtilities.updateComponentTreeUI(monitor);
			monitor.pack();
			
		}
	}
	
}