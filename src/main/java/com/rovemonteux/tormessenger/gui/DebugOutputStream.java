/**
 * DebugOutputStream.java
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

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class DebugOutputStream extends OutputStream {
    private Desktop desktop = null;
    private String current_message = "";

    public DebugOutputStream(Desktop desktop_) {
        this.desktop = desktop_;
    }

    @Override
    public void write(int b) throws IOException {
        String character_value = String.valueOf((char)b);
        desktop.debugTextArea.append(character_value);
        desktop.debugTextArea.setCaretPosition(desktop.debugTextArea.getDocument().getLength());
        if (character_value.equals("\n")) {
            try {
                desktop.statusLabel.setText(current_message);
                current_message = "";
            } catch (Exception e) {

            }
        } else {
            current_message += character_value;
        }
    }
}
