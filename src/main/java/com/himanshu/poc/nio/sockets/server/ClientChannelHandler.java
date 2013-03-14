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
package com.himanshu.poc.nio.sockets.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ClientChannelHandler {
	
	static Selector selector = null;
	static {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void handle (SocketChannel clientSocketChannel) {
		try {
			clientSocketChannel.configureBlocking(false);	//Configuring in non-blocking mode so can be used with Selector
			SelectionKey key = clientSocketChannel.register(selector, SelectionKey.OP_READ);
			
			int numberOfChannelsReady = selector.select();	//This is a blocking method, will move in another thread
			Set<SelectionKey> selectionKeysReady = selector.selectedKeys();
			Iterator<SelectionKey> selKeyIter = selectionKeysReady.iterator();
			while (selKeyIter.hasNext()) {
				//Retrieve socketchannel, read data from it, print here.
				SelectionKey keyToProcess = selKeyIter.next();
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				SocketChannel clientChannel = (SocketChannel)keyToProcess.channel();
				ByteBuffer buffer = ByteBuffer.allocate(10);
				int read;
				while ((read = clientChannel.read(buffer)) != -1) {
					baos.write(buffer.array());
					buffer.clear();
					System.out.println("Read is :"+read);
				}
				
				System.out.println("BAOS  "+new String(baos.toByteArray()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
