/**
 * Menu.java
 *
 * Copyright (C) 2015 Rove Monteux.  All Rights Reserved.
 *
 * This is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.rovemonteux.tormessenger.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rovemonteux.tormessenger.network.NetworkServer;
import com.rovemonteux.tormessenger.network.TorLayer;
import com.rovemonteux.tormessenger.gui.chat.Outgoing;


public class Menu implements ActionListener, ItemListener {

    private static final Logger LOG = LoggerFactory.getLogger(Menu.class);

    public JMenuItem menuAdd, menuAbout;
    private Desktop desktop = null;

    public Menu(Desktop desktop_) {
        this.desktop = desktop_;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, menuHelp;
        menuBar = new JMenuBar();
        menu = new JMenu("User");
        menu.setMnemonic(KeyEvent.VK_U);
        menu.getAccessibleContext().setAccessibleDescription("User menu");
        menuBar.add(menu);
        menuAdd = new JMenuItem("Add Friend",KeyEvent.VK_A);
        menuAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuAdd.getAccessibleContext().setAccessibleDescription("Add a friend to the friends rooster");
        menuAdd.setName("add");
        menuAdd.addActionListener(this);
        menu.add(menuAdd);
        menuHelp = new JMenu("Help");
        menuHelp.setMnemonic(KeyEvent.VK_H);
        menuHelp.getAccessibleContext().setAccessibleDescription("Tor Messenger's Help");
        menuHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	try {
                    String remoteUser = JOptionPane.showInputDialog("Please enter the friend's username");
                    Outgoing chat = new Outgoing(remoteUser.trim().toLowerCase(), desktop.torLayer);
                    chat.display();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        menuBar.add(menuHelp);
        menuAbout = new JMenuItem("About",KeyEvent.VK_B);
        menuAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuAbout.getAccessibleContext().setAccessibleDescription("About Tor Messenger");
        menuAbout.setName("about");
        menuAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                About ad = new About();
                ad.setVisible(true);
            }
        });
        menuHelp.add(menuAbout);
        return menuBar;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       
    }
}
