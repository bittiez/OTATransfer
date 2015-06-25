package transfer.thread;

import transfer.customUI.StatusBar;
import transfer.other.URLReader;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by tad on 6/23/2015.
 */
public class Server implements Runnable {
    public Boolean running;

    private ServerSocket serverSocket;
    private StatusBar footerBar;
    private String ip, port;
    private ArrayList<File> fileList;

    public Server(StatusBar footerBar, ArrayList<File> fileList) throws Exception {
        this.footerBar = footerBar;
        this.fileList = fileList;
        ip = URLReader.getText("http://ip-api.com/csv");
        String[] ipp = ip.split(",");
        ip = ipp[ipp.length -1];
    }
    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(0);
            running = true;
            port = serverSocket.getLocalPort() +"";
            footerBar.setText("Listening on "+ ip + " : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(running){
            try {
                Client c = new Client(serverSocket, serverSocket.accept(), fileList);
                new Thread(c).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getLink(String filename){
        return ip + ":" + port + ":" + filename;
    }
}
