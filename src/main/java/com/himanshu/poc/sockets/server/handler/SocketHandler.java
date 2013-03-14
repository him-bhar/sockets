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
package com.himanshu.poc.sockets.server.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocketHandler extends Thread {
	private final Socket client_socket;

	public Socket getClient_socket() {
		return client_socket;
	}

	public SocketHandler(Socket client_socket) {
		this.client_socket = client_socket;
	}

	public void handle() {
		SocketExecutor.submitJob(this);
	}

	/*Read an int, which is the size of the next segment of data being received. Create a buffer with that size, or use a roomy pre-existing buffer.
	Read into the buffer, making sure it is limited to the aforeread size. Rinse and repeat :)
	If you really don't know the size in advance as you said, read into an expanding ByteArrayOutputStream as
	the other answers have mentioned. However, the size method really is the most reliable.*/
	@Override
	public void run() {
		System.out.println(client_socket);
		try {
			System.out.println("reading socket");
			/*BufferedReader brIn = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			String inputLine = null;
			while ((inputLine = brIn.readLine()) != null) {
				System.out.println(inputLine);
			}*/
			InputStream is = client_socket.getInputStream();
			byte[] byteArr = new byte[1024];
			int inputsize = -1;
			int currentPos = 0;
			StringBuffer sb = new StringBuffer(11111);
			/*while((inputsize = is.read(byteArr)) != -1) {
				String processed = new String(byteArr);
				sb.append(processed);
			}*/
			int BUFFER_SIZE = 1024;
			int read;
			byte[] buffer = new byte[BUFFER_SIZE];
			String processed = "";
			while ((read = is.read(byteArr, 0, BUFFER_SIZE)) != -1) {
				String current = new String(byteArr);
				//os.write(buffer, 0, read);
				System.out.println("current Process   "+current);

				//processed +=current;
				sb.append(current.toString().trim());
			}
			System.out.println("Socket input is : "+sb.toString());
			System.out.println("Sending response to client  "+processed.toString());
			//client_socket.getOutputStream().write(sb.toString().getBytes());
			//client_socket.getOutputStream().close();

			FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\himanshu2100\\eee.txt"));
			fos.write(processed.getBytes());
			fos.close();
			is.close();
			client_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<10;i++) {
			sb.append(i);
		}
		System.out.println(sb.toString());
	}

}
