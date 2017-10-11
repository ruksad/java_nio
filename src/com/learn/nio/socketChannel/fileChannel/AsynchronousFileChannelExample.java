package com.learn.nio.socketChannel.fileChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynchronousFileChannelExample {



  public void readFileIncoolestWay() {

    Path path = Paths.get("largeFile.txt");

    try (AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
      int fileSize = (int) afc.size();
      ByteBuffer dataBuffer = ByteBuffer.allocate(fileSize);

      Future<Integer> result = afc.read(dataBuffer, 0);
      while (!result.isDone()) {
        System.out.println("Sleeping for 2  seconds...");
        Thread.sleep(2000);
      }
      int readBytes = result.get();

      System.out.format("%s bytes read   from  %s%n", readBytes, path);
      System.out.format("Read data is:%n");

      byte[] byteData = dataBuffer.array();
      Charset cs = Charset.forName("UTF-8");
      String data = new String(byteData, cs);

      System.out.println(data);
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

}


