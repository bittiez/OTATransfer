package transfer.thread;

import transfer.ui.MainUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by tad on 6/23/2015.
 */
public class Client implements Runnable  {
    private ServerSocket serverSocket;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private ArrayList<File> fileList;

    public Client(ServerSocket serverSocket, Socket client, ArrayList<File> fileList){
        this.serverSocket = serverSocket;
        this.client = client;
        this.fileList = fileList;
    }

    @Override
    public void run() {
        if(client.isConnected()){
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String rec = in.readLine();
                if(rec == null) {
                    client.close(); //break;
                    }
                if(!rec.isEmpty()){
                    String cmd = rec.substring(0, 3);
                    String msg = rec.substring(4);
                    switch (cmd){
                        case "001":
                            //Send file list
                            break;
                        case "002":
                            //Send file
                            if(!msg.isEmpty()){
                                for(File f : fileList){
                                    if(f.getName().equals(msg)){
                                        sendFile(f);
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFile(File file){
        try {
            System.out.println("Sending file to " + client.getInetAddress().getHostAddress());
            File myFile = file;
            byte[] myByteArray = new byte[(int) myFile.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
            int br;
            OutputStream os = client.getOutputStream();
            while((br = bis.read(myByteArray)) != -1){
                os.write(myByteArray, 0, br);
            }
            os.close();
            client.close();
            System.out.println("Sent file to " + client.getInetAddress().getHostAddress());
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
