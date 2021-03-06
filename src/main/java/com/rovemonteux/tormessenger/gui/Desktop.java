/**
 * Desktop.java
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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintStream;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rovemonteux.tormessenger.network.NetworkClient;
import com.rovemonteux.tormessenger.network.NetworkServer;
import com.rovemonteux.tormessenger.network.TorLayer;

public class Desktop implements ItemListener {

    private static final Logger LOG = LoggerFactory.getLogger(Desktop.class);

    private static final long serialVersionUID = 1439323576513832536L;

    JPanel cards;
    final static String FRIENDSPANEL = "Friends";
    final static String DEBUGPANEL = "Network Info";
    public TorLayer torLayer = null;
    public JTextArea debugTextArea = null;
    public JLabel statusLabel = null;
    public JButton button = null;
    public JList<String> list;
    public DefaultListModel<String> listModel;
    public JFrame jframe;
    public boolean connected = false;
    public Menu menu = null;

    public void addComponentToPane(Container pane, JFrame jframe) {

        this.jframe = jframe;
        this.menu = new Menu(this);
        jframe.setJMenuBar(menu.createMenuBar());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel friendsPanel = new JPanel();
        friendsPanel.setLayout(new BorderLayout());

        String[] selections = { "Rove Linux", "Rove Mac", "Rove Android"};
        JList<String> list = new JList<String>(selections);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSize(friendsPanel.getSize());
        list.setLayout(new BorderLayout());
        //list.setSelectedIndex(1);
        JScrollPane listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setSize(friendsPanel.getSize());
        //listScrollPane.setLayout(new BorderLayout());
        friendsPanel.add(listScrollPane);
        //friendsPanel.add(list);
        JPanel debugPanel = new JPanel();
        debugPanel.setLayout(new BorderLayout());
        debugTextArea.setLayout(new BorderLayout());
        debugPanel.add(new JScrollPane(debugTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        tabbedPane.addTab(FRIENDSPANEL, friendsPanel);
        tabbedPane.addTab(DEBUGPANEL, debugPanel);
        pane.add(tabbedPane, BorderLayout.CENTER);
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        pane.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(pane.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Connecting, please wait...");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }

    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }

    public static void create() {
        JFrame frame = new JFrame("Tor Messenger - Offline");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(ClassLoader.getSystemResource("src/main/resources/tormessenger64.png"));
        frame.setIconImage(img);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Desktop desktop = new Desktop();
        desktop.debugTextArea = new JTextArea();
        desktop.debugTextArea.setEditable(false);

        DebugOutputStream debugStream = new DebugOutputStream(desktop);
        desktop.addComponentToPane(frame.getContentPane(), frame);

        System.setOut(new PrintStream(debugStream));
        System.setErr(new PrintStream(debugStream));

        frame.pack();
        frame.setBounds(100, 100, 450, 300);
        frame.setVisible(true);

        desktop.torLayer = new TorLayer(desktop);
        desktop.torLayer.connect();

    }

}
