/**
 * NetworkServer.java
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rovemonteux.silvertunnel.netlib.api.NetSocket;
import com.rovemonteux.silvertunnel.netlib.layer.tor.TorHiddenServicePortPrivateNetAddress;
import com.rovemonteux.silvertunnel.netlib.layer.tor.TorHiddenServicePrivateNetAddress;
import com.rovemonteux.silvertunnel.netlib.layer.tor.TorNetLayerUtil;
import com.rovemonteux.silvertunnel.netlib.layer.tor.TorNetServerSocket;
import com.rovemonteux.tormessenger.gui.Desktop;
import com.rovemonteux.tormessenger.gui.chat.Incoming;

public class NetworkServer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkServer.class);
    private TorNetLayerUtil torNetLayerUtil = null;
    private TorLayer torLayer = null;
    private Desktop desktop = null;
    public TorNetServerSocket netServerSocket = null;

    public NetworkServer(Desktop desktop_) {
        this.desktop = desktop_;
        this.torLayer = desktop_.torLayer;
    }

    public void run() {
        try {
            this.torNetLayerUtil = TorNetLayerUtil.getInstance();
            File directory = new File("service");
            if (!(directory.exists())) {
                directory.mkdir();
                TorHiddenServicePrivateNetAddress newNetAddress = torNetLayerUtil.createNewTorHiddenServicePrivateNetAddress();
                torNetLayerUtil.writeTorHiddenServicePrivateNetAddressToFiles(directory, newNetAddress);
                newNetAddress = null;
            }
            TorHiddenServicePrivateNetAddress netAddress = torNetLayerUtil.readTorHiddenServicePrivateNetAddressFromFiles(directory, true);
            int port = 443;
            TorHiddenServicePortPrivateNetAddress netAddressWithPort = new TorHiddenServicePortPrivateNetAddress(netAddress, port);
            LOG.info("Creating server socket on Tor layer");
            netServerSocket = (TorNetServerSocket)torLayer.getLayer().createNetServerSocket(null, netAddressWithPort);
            desktop.statusLabel.setText("Connected as '"+netServerSocket.getShortHostname()+"'.");
            desktop.jframe.setTitle("Tor Messenger - "+netServerSocket.getShortHostname()+" - Online");
            LOG.info("Created server socket for hidden service "+netServerSocket.getHostname()+" - user is online");
            while (true) {
                final NetSocket netSocket = netServerSocket.accept();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Incoming incoming = new Incoming(netSocket);
                            if (incoming.handshake()) {
                                while (true) {
                                    incoming.read();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                netSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } .start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
