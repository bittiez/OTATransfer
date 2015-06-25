package transfer.thread;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public Download(String fileName, String ip, String port){
        this.fileName = fileName;
        this.ip = ip;
        this.port = port;
        socket = new Socket();
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

            long start, end;
            downloaded+=1024;
            while(bytesRead != -1){
                start = System.currentTimeMillis();

                bos.write(myByteArray, 0, bytesRead);
                myByteArray = new byte[1024];
                bytesRead = is.read(myByteArray);

                end = System.currentTimeMillis();
                downloaded+=1024;
                bytePerSec = (1024 / (end - start == 0 ? 1 : end - start)) * 1024;
                //System.out.println(humanReadableByteCount(downloaded, false) + " downloaded @ " + humanReadableByteCount(bytePerSec, false)+".");
            }

            bos.close();
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
