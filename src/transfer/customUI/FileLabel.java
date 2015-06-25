package transfer.customUI;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.File;

import static transfer.AnchorTools.topleft;
import static transfer.AnchorTools.topright;

/**
 * Created by tad on 6/25/2015.
 */
public class FileLabel extends AnchorPane {
    private File file;
    private Label fileName;
    private Label fileSize;
    private TextField fileLink;

    public FileLabel(File file, String fileLink){
        super();
        this.file = file;
        fileName = new Label(file.getName());
        fileSize = new Label(humanReadableByteCount(file.length(), false));
        this.fileLink = new TextField(fileLink);

        topleft(this.fileLink, 0, 0);
        topleft(fileName, 0, 150);
        topright(fileSize, 0, 0);

        this.getChildren().add(fileName);
        this.getChildren().add(fileSize);
        this.getChildren().add(this.fileLink);

    }

    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
