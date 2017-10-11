package com.learn.nio.socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiPortEcho {
  private int ports[];
  private ByteBuffer echoBuffer = ByteBuffer.allocate( 1024 );

  public MultiPortEcho( int ports[] ) throws IOException {
    this.ports = ports;

    configure_selector();
  }

  private void configure_selector() throws IOException {
    Selector selector= Selector.open();

    for(int i=0;i<ports.length;i++){
      ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
      serverSocketChannel.configureBlocking(false);
      ServerSocket serverSocket=serverSocketChannel.socket();
      InetSocketAddress address=new InetSocketAddress(ports[i]);
      serverSocket.bind(address);

      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      System.out.println("Going to listen on " + ports[i]);
    }

    while(true){
      int num=selector.select();

      Set selectedKeys=selector.selectedKeys();
      Iterator it = selectedKeys.iterator();
      while (it.hasNext()){

        SelectionKey selectionKey=(SelectionKey) it.next();
        if ((selectionKey.readyOps() & SelectionKey.OP_ACCEPT)== SelectionKey.OP_ACCEPT){
          // Accept the new connection
         ServerSocketChannel serverSocketChannel=(ServerSocketChannel)selectionKey.channel();
          SocketChannel socketChannel = serverSocketChannel.accept();
          socketChannel.configureBlocking(false);

          // Add the new connection to the selector
          SelectionKey newKey = socketChannel.register(selector, SelectionKey.OP_READ);
          it.remove();

          System.out.println( "Got connection from "+socketChannel );
        }else if((selectionKey.readyOps() & SelectionKey.OP_READ)== SelectionKey.OP_READ){
          // Read the data
          SocketChannel sc = (SocketChannel)selectionKey.channel();

          // Echo data
          int bytesEchoed = 0;
          while (true) {
            echoBuffer.clear();

            int number_of_bytes = sc.read(echoBuffer);

            if (number_of_bytes <= 0) {
              break;
            }

            echoBuffer.flip();

            sc.write(echoBuffer);
            bytesEchoed += number_of_bytes;
          }

          System.out.println("Echoed " + bytesEchoed + " from " + sc);

          it.remove();
        }
      }
    }
  }
}
