/*
 * Copyright 2013 Himanshu Bhardwaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.himanshu.poc.sockets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {

	public static void main(String[] args) {
		// declaration section:
		// smtpClient: our client socket
		// os: output stream
		// is: input stream
		Socket smtpSocket = null;
		DataOutputStream os = null;
		DataInputStream is = null;
		// Initialization section:
		// Try to open a socket on port 8018
		// Try to open input and output streams
		try {
			smtpSocket = new Socket("localhost", 8018);
			os = new DataOutputStream(smtpSocket.getOutputStream());
			is = new DataInputStream(smtpSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to: hostname");
		}
		// If everything has been initialized then we want to write some data
		// to the socket we have opened a connection to on port 25
		if (smtpSocket != null && os != null && is != null) {
			try {
				// The capital string before each colon has a special meaning to
				// SMTP
				// you may want to read the SMTP specification, RFC1822/3
				os.writeBytes("HELO\n");
				os.writeBytes("MAIL From: k3is@fundy.csd.unbsj.ca\n");
				os.writeBytes("RCPT To: k3is@fundy.csd.unbsj.ca\n");
				os.writeBytes("DATA\n");
				os.writeBytes("From: k3is@fundy.csd.unbsj.ca\n");
				os.writeBytes("Subject: testing\n");
				os.writeBytes("Hi there\n"); // message body
				os.writeBytes("\n.\n");
				os.writeBytes("QUIT");
				// keep on reading from/to the socket till we receive the "Ok"
				// from SMTP,
				// once we received that then we want to break.
				String responseLine;
				while ((responseLine = is.readLine()) != null) {
					System.out.println("Server: " + responseLine);
					if (responseLine.indexOf("Ok") != -1) {
						break;
					}
				}
				// clean up:
				// close the output stream
				// close the input stream
				// close the socket
				os.close();
				is.close();
				smtpSocket.close();
			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}

}
