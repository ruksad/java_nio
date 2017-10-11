package com.learn.nio;

import com.learn.nio.socketChannel.fileChannel.AsynchronousFileChannelExample;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class HelloWorld {

  public static void main(String [] args) throws IOException {
    HelloWorld helloWorld=new HelloWorld();
    //helloWorld.readFileOldSchoolWay();
     //helloWorld.readFileInCool();
    //helloWorld.Watcher();
    helloWorld.createSymblicLinks();
    //new MultiPortEcho(new int[]{8082,8085,8087});
    new AsynchronousFileChannelExample().readFileIncoolestWay();
  }

  public void readFileOldSchoolWay(){

        BufferedReader br = null;
        String sCurrentLine = null;

        try
        {
          br = new BufferedReader(
              new FileReader("ExperimentOnThis.txt"));
          while ((sCurrentLine = br.readLine()) != null)
          {
            System.out.println(sCurrentLine);
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        finally
        {
          try
          {
            if (br != null)
              br.close();
          } catch (IOException ex)
          {
            ex.printStackTrace();
          }
        }

  }

  public void readFileInCool() throws IOException {

    RandomAccessFile randomAccessFile=new RandomAccessFile("ExperimentOnThis.txt","r");
    FileChannel fileChannel=randomAccessFile.getChannel();
    ByteBuffer buffer=ByteBuffer.allocate(1024);
    while(fileChannel.read(buffer)>0){
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++)
        {
          System.out.print((char) buffer.get());
        }
        buffer.clear();
    }
    fileChannel.close();
    randomAccessFile.close();

  }

  public void Watcher(){
    Path currDir=Paths.get(".");
    try
    {
      WatchService watchService = currDir.getFileSystem().newWatchService();
      currDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE);
      WatchKey watchKey=watchService.take();
      List<WatchEvent<?>> events=watchKey.pollEvents();
      for (WatchEvent event : events) {
        System.out.println("Someone just did this event = "+event.kind().name()+" for the file = '" + event.context().toString() + "'.");

      }
    }catch (Exception e){
      System.out.println("Error: " + e.toString());
    }
  }

  public void createSymblicLinks(){
    Path existingFilePath = Paths.get("ExperimentOnThis.txt");
    Path symLinkPath = Paths.get("/home/mohammad/Documents/java/testLink1");
    try {
      Files.createSymbolicLink(symLinkPath, existingFilePath);
     // Files.createLink(symLinkPath, existingFilePath);

      System.out.format("Target of link" +
              " '%s' is '%s'%n", symLinkPath,
          Files.readSymbolicLink(symLinkPath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


