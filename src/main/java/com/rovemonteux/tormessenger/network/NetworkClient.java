/**
 * NetworkClient.java
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

package com.rovemonteux.tormessenger.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rovemonteux.silvertunnel.netlib.api.NetSocket;
import com.rovemonteux.silvertunnel.netlib.api.util.TcpipNetAddress;

public class NetworkClient {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkClient.class);
    private TcpipNetAddress networkAddress = null;
    private TorLayer torLayer = null;
    private NetSocket netSocket = null;

    public NetworkClient(String address, int port, TorLayer torLayer_) {
        this.networkAddress = new TcpipNetAddress(address, port);
        this.torLayer = torLayer_;
    }

    public void connect() throws IOException {
        this.netSocket = torLayer.getLayer().createNetSocket(null, null, this.networkAddress);
    }

    public void write(String message) throws IOException {
        PrintWriter out = new PrintWriter(netSocket.getOutputStream(), true);
        out.println(message);
        out.flush();
    }

    public String read() throws IOException {
        Reader r = new InputStreamReader(netSocket.getInputStream(), Charset.forName("UTF-8"));
        int intch;
        StringBuilder input = new StringBuilder();
        while ((intch = r.read()) != 10) {
            char ch = (char) intch;
            input.append(ch);
        }
        r.close();
        /*if (!(this.remoteUser == null) && !(this.remoteUser.length() < 1)) {
        	chatFrame.chatArea.append(input.toString().trim()+"\n");
        }*/
        return input.toString().trim();
    }

    public void disconnect() throws IOException {
        if (this.netSocket != null) {
            this.netSocket.close();
        }
    }

}
