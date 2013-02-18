package eventManager.fx;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ModalDialog extends Stage {

    public ModalDialog(Window owner, String str1, String str2) {
        initOwner(owner);
        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);
        Group root = new Group();
        Image img = new Image(getClass().getResourceAsStream("error_icon.png"));
        ImageView iv = new ImageView(img);
        double width = iv.layoutBoundsProperty().getValue().getWidth();
        double height = iv.layoutBoundsProperty().getValue().getHeight();
        iv.setX(80.0);
        iv.setY(10.0);
        root.getChildren().add(iv);
        Text msg1 = new Text(str1);
        msg1.setFill(Color.BLACK);
        msg1.setFont(new Font("Arial", 20.0));
        msg1.setX(7);
        msg1.setY(80);
        root.getChildren().add(msg1);
        Text msg2 = new Text(str2);
        msg2.setFill(Color.BLACK);
        msg2.setFont(new Font("Arial", 12.0));
        msg2.setX(20);
        msg2.setY(msg1.getY() + 20.0);
        root.getChildren().add(msg2);
        Scene scene = new Scene(root, 200, 120.0, Color.WHITE);
        EventHandler<MouseEvent> ehme;
        ehme = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                close();
            }
        };
        scene.setOnMousePressed(ehme);
        setScene(scene);
        setX(owner.getX() + Math.abs(owner.getWidth() - scene.getWidth()) / 2.0);
        setY(owner.getY() + Math.abs(owner.getHeight() - scene.getHeight()) / 2.0);

    }

    @FXML
    public void chargeData() {
    }

    @FXML
    public void cancel() {
        this.close();
    }
}
