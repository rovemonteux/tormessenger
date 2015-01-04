/**
 * ChatWindow.java
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatWindow extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(ChatWindow.class);
    public JTextArea chatArea = null;
    private JLabel titleLabel = null;
    private JScrollPane scrollPane = null;
    private JButton submitButton = null;
    public JTextField messageField = null;
    public Chat chat = null;

    public ChatWindow(Chat chat_) {
        this.chat = chat_;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        titleLabel = new JLabel();
        scrollPane = new JScrollPane();
        chatArea = new JTextArea();
        submitButton = new JButton();
        submitButton.setText("Send");
        messageField = new JTextField();
        submitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    chat.write(messageField.getText().trim());
                } catch (IOException e1) {
                    LOG.error(e1.toString());
                    e1.printStackTrace();
                }
            }
        });
        setTitle("Chat");
        titleLabel.setText("Messages");

        chatArea.setColumns(20);
        chatArea.setRows(5);
        scrollPane.setViewportView(chatArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                      .addContainerGap()
                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                .addComponent(titleLabel).addComponent(messageField).addComponent(submitButton))
                      .addContainerGap()
                     )
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                      .addContainerGap()
                      .addComponent(titleLabel)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                      .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                      .addComponent(messageField)
                      .addComponent(submitButton)
                      .addContainerGap()
                     )

        );

        pack();
    }
}
