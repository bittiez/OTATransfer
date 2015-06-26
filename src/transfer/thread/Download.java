package transfer.thread;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static transfer.AnchorTools.*;

/**
 * Created by tad on 6/24/2015.
 */
public class Download implements Runnable {
    private String fileName, ip, port;
    private long downloaded = 0;
    private long bytePerSec = 0;
    private InetAddress inetAddress;
    private SocketAddress socketAddress;
    private Socket socket;
    private DataOutputStream dataOut;
    private InputStream is;
    private AnchorPane fileAnchor;
    private Label fileUIName, fileCompleted, fileSpeed;
    private static String fileComp = "Downloaded: ", fileSp = "Current DL Speed: ";

    private Stage fileUI;

    public Download(String fileName, String ip, String port){
        this.fileName = fileName;
        this.ip = ip;
        this.port = port;
        socket = new Socket();

        fileUI = new Stage();
        fileAnchor = new AnchorPane();
        fileUI.setScene(new Scene(fileAnchor, 250, 85));
        fileUI.setTitle("Downloading file " + fileName);

        fileUIName = new Label(fileName);
        fileCompleted = new Label();
        fileSpeed = new Label();

        topleft(fileUIName, 0, 0);
        bottomleft(fileCompleted, 0, 0);
        bottomright( fileSpeed, 0, 0);

        fileAnchor.getChildren().addAll(fileUIName, fileCompleted, fileSpeed);

        fileUI.show();
    }

    @Override
    public void run() {
        try {
            inetAddress = InetAddress.getByName(ip);
            socketAddress = new InetSocketAddress(inetAddress, Integer.parseInt(port));
            socket.connect(socketAddress, 2000); // 2 seconds time out
            dataOut = new DataOutputStream(socket.getOutputStream());
            is = socket.getInputStream();

            dataOut.writeBytes("002:"+fileName+System.lineSeparator());
            dataOut.flush();

            fetchFile();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchFile(){
        try {
            Path tpath = Paths.get(new File("").getAbsolutePath());
            byte[] myByteArray = new byte[1024];

            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int bytesRead;
            bytesRead = is.read(myByteArray);

            long start, end, timeBegin = System.currentTimeMillis();
            downloaded+=1024;
            while(bytesRead != -1){
                start = System.currentTimeMillis();

                bos.write(myByteArray, 0, bytesRead);
                myByteArray = new byte[1024];
                bytesRead = is.read(myByteArray);

                end = System.currentTimeMillis();
                downloaded+=1024;
                bytePerSec = (1024 / (end - start == 0 ? 1 : end - start)) * 1024;
                if(end - timeBegin > 1000){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            fileCompleted.setText(fileComp + humanReadableByteCount(downloaded, false));
                            fileSpeed.setText(fileSp + humanReadableByteCount(bytePerSec, false));
                        }
                    });
                }
                //System.out.println(humanReadableByteCount(downloaded, false) + " downloaded @ " + humanReadableByteCount(bytePerSec, false)+".");
            }

            bos.close();

            Platform.runLater(() -> fileSpeed.setText("File Completed."));

            System.out.println("Downloaded file.");
        }catch (Exception e){
            //System.err.println(e.toString());
            e.printStackTrace();
        }
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
