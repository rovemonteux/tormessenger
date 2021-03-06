/**
 * Outgoing.java
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

package com.rovemonteux.tormessenger.gui.chat;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rovemonteux.tormessenger.network.NetworkClient;
import com.rovemonteux.tormessenger.network.TorLayer;

public class Outgoing implements Chat {

    private static final Logger LOG = LoggerFactory.getLogger(Outgoing.class);
    public String remoteUser = "";
    public NetworkClient nc = null;
    public ChatWindow chatFrame = null;
    public TorLayer torLayer = null;

    public Outgoing(String remoteUser_, TorLayer torLayer_) throws IOException {
        this.remoteUser = remoteUser_;
        this.nc = new NetworkClient(this.remoteUser+".onion", 443, torLayer);
        this.nc.connect();
        this.torLayer = torLayer_;
        this.chatFrame = new ChatWindow(this);
        this.chatFrame.setVisible(true);
    }

    public void display() throws IOException {
        this.chatFrame.setTitle("Chat with "+this.remoteUser);
        this.write(torLayer.username);
    }


    @Override
    public String read() throws IOException {
        return this.nc.read();
    }


    @Override
    public void write(String message) throws IOException {
        this.nc.write(message);
    }


}
