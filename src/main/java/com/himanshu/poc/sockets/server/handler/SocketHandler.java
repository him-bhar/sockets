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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
			InputStream is = client_socket.getInputStream();
			byte[] byteArr = new byte[1024];
			
			int read;
			int BUFFER_SIZE = 1024;
			
			/*Using bytearrayoutputstream here gives me the flexibility to have an auto-increasing byte buffer. 
			Thus removes the headache of me managing the array, also see how I am writing the bytearray into the output stream.
			This is done so bcoz the array might contain data upto few bytes but am always reading it upto BUFFER_SIZE, 
			so can have garbage data. So in order to fix this, while writing into bytearrayoutputstream. I simply use the size
			returned by is.read, gives me the number of bytes received, so write only that much ignoring the garbage values.*/
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			//Here I am reading from socketinputstream, and filling data in a bytearray of 1024 length.
			while ((read = is.read(byteArr, 0, BUFFER_SIZE)) != -1) {
				//Here I am writing upto the length received from inputstream.read method. So garbage values are lost. What a safe :)
				baos.write(byteArr, 0, read);	//This is an optimized design, instead of having so many strings
			}
			System.out.println("Output is :"+new String(baos.toByteArray()));
			baos.close();
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
