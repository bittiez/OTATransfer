package transfer;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Created by tad on 6/21/2015.
 */
public class AnchorTools {
    public static Node topleft(Node child, double top, double left){
        AnchorPane.setTopAnchor(child, top);
        AnchorPane.setLeftAnchor(child, left);
        return child;
    }
    public static Node bottomleft(Node child, double bottom, double left){
        AnchorPane.setBottomAnchor(child, bottom);
        AnchorPane.setLeftAnchor(child, left);
        return child;
    }
    public static Node bottomleftright(Node child, double bottom, double left, double right){
        AnchorPane.setBottomAnchor(child, bottom);
        AnchorPane.setLeftAnchor(child, left);
        AnchorPane.setRightAnchor(child, right);
        return child;
    }
    public static Node right(Node child, double right){
        AnchorPane.setRightAnchor(child, right);
        return child;
    }
    public static Node bottomright(Node child, double bottom, double right){
        AnchorPane.setBottomAnchor(child, bottom);
        AnchorPane.setRightAnchor(child, right);
        return child;
    }
}
