package transfer.customUI;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by tad on 6/23/2015.
 */
public class StatusBar extends HBox {
    private Label message = new Label();
    public StatusBar(double width, double height){
        super();
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.getChildren().add(message);
        this.setVisible(true);
    }

    public void setText(String data){
        message.setText(data);
    }
}
