package transfer.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import transfer.customUI.FileLabel;
import transfer.customUI.StatusBar;
import transfer.thread.Download;
import transfer.thread.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import static transfer.AnchorTools.*;

/**
 * Created by tad on 6/23/2015.
 */
public class MainUI extends Application {
    public ArrayList<File> availableFiles;
    private Stage mainStage;
    private Parent mainParent;
    private Scene mainScene;
    private AnchorPane mainAnchorPane;
    private StatusBar footerBar;
    private Server mainServer;
    private Button addFile;
    private Button downloadFile;
    private TextField downloadUrl;
    private VBox filesArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //--Initiating class-level variables--//
        mainAnchorPane = new AnchorPane();
        mainParent = mainAnchorPane;
        availableFiles = new ArrayList<File>();
        mainScene = new Scene(mainParent, 500, 200);
        mainStage = primaryStage;
        addFile = new Button("Share a file");
        downloadFile = new Button("Download file");
        downloadUrl = new TextField();
        filesArea = new VBox();
        //--End Initiating class-level variables--//
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        addFile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY){
                    addFile();
                }
            }
        });
        downloadFile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    downloadFile();
                }
            }
        });



        initCss();
        initBottomMenu();
        initShareButton();
        initDownloadButton();
        initFilesArea();
        startServer();

        mainStage.setTitle("OTA File Transfer");
        mainStage.setScene(mainScene);
        mainStage.show();


    }

    private void downloadFile(){
        if(!downloadUrl.getText().isEmpty()){
            String[] data = downloadUrl.getText().split(":");
            Download dl = new Download(data[2], data[0], data[1]);
            new Thread(dl).start();
        }
    }

    private void addFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a file to share");
        final File fileToShare = chooser.showOpenDialog(new Stage());
        if(fileToShare.exists()){
            availableFiles.add(fileToShare);
            //Do new file stuff
            FileLabel fl = new FileLabel(fileToShare, mainServer.getLink(fileToShare.getName()));
            fl.setPrefWidth(500);
            filesArea.getChildren().add(fl);

//            Stage fileUI = new Stage();
//            AnchorPane fileAnchor = new AnchorPane();
//            fileUI.setScene(new Scene(fileAnchor, 200, 40));
//            fileUI.setTitle("Sharing file " + fileToShare.getName());
//            TextField linkField = new TextField(mainServer.getLink(fileToShare.getName()));
//            linkField.setPrefWidth(200);
//            fileAnchor.getChildren().add(linkField);
//            fileAnchor.getChildren().add(bottomleft(new Label(fileToShare.getName()), 0, 0));
//            fileUI.show();
//            fileUI.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent e) {
//                    availableFiles.remove(fileToShare);
//                }
//            });
        }
    }

    private void initFilesArea(){
        topleft(filesArea, 5, 5);
        bottomright(filesArea, 55, 5);
        mainAnchorPane.getChildren().add(filesArea);
    }

    private void initDownloadButton(){
        mainAnchorPane.getChildren().add(bottomright(downloadFile, 25, 0));
        mainAnchorPane.getChildren().add(bottomright(downloadUrl, 25, 100));
    }

    private void initShareButton(){
        mainAnchorPane.getChildren().add(bottomleft(addFile, 25, 0));
    }

    private void startServer() throws Exception {
        mainServer = new Server(footerBar, availableFiles);
        new Thread(mainServer).start();
    }

    private void initCss(){
        mainScene.getStylesheets().add("/transfer/ui/style.css");
        mainAnchorPane.getStyleClass().add("mainBackground");
    }

    private void initBottomMenu(){
        footerBar = new StatusBar(500, 20);
        footerBar.setText("Initializing...");
        bottomleftright(footerBar, 0, 0, 0);
        footerBar.getStyleClass().add("footerBar");
        mainAnchorPane.getChildren().add(footerBar);
    }

}
