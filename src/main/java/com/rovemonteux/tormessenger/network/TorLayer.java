/**
 * TorLayer.java
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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rovemonteux.silvertunnel.netlib.api.NetFactory;
import com.rovemonteux.silvertunnel.netlib.api.NetLayer;
import com.rovemonteux.silvertunnel.netlib.api.NetLayerIDs;
import com.rovemonteux.tormessenger.gui.Desktop;
import com.rovemonteux.tormessenger.gui.Menu;

public class TorLayer {

    private static final Logger LOG = LoggerFactory.getLogger(TorLayer.class);

    private NetLayer torLayer = null;
    public boolean online = false;
    public String username = "";
    public Desktop desktop;

    public TorLayer(Desktop desktop_) {
        this.desktop = desktop_;
        this.setLayer(NetFactory.getInstance().getNetLayerById(NetLayerIDs.TOR_OVER_TLS_OVER_TCPIP));
    }

    public void connect() {
        Thread launchTor = new Thread() {
            public void run() {
                try {
                    NetworkServer networkServer = new NetworkServer(desktop);
                    networkServer.run();
                    username = networkServer.netServerSocket.getShortHostname();
                    LOG.info("User is online");
                    online = true;
                } catch (Exception e) {
                    LOG.error("Error going online: "+e.getMessage());
                    online = false;
                }
            }
        };

        launchTor.start();
    }

    public void disconnect() {

    }

    public NetLayer getLayer() {
        return torLayer;
    }

    public void setLayer(NetLayer torLayer) {
        this.torLayer = torLayer;
    }

    public void shutdown() throws IOException {
        this.torLayer.clear();
    }
}
