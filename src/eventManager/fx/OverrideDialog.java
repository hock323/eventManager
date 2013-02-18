package eventManager.fx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class OverrideDialog implements Initializable {
    
    public SimpleBooleanProperty answer;
    String str1, str2;
    Stage dialogStage;
    @FXML Label str1Label;
    @FXML Label str2Label;
    
    public OverrideDialog(Window owner, String str1, String str2, SimpleBooleanProperty answer, ResourceBundle bundle) {
        try {
            dialogStage = new Stage();
            URL location = getClass().getResource("/eventManager/fx/saveDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location, bundle);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            AnchorPane dialogPane = (AnchorPane) fxmlLoader.load();
            OverrideDialog dialogController = fxmlLoader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setStr1Label(str1);
            dialogController.setStr2Label(str2);
            dialogController.setAnswer(answer);
            Scene dialogScene = new Scene(dialogPane);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setScene(dialogScene);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.toFront();
            dialogStage.show();

        } catch (IOException ex) {
            Logger.getLogger(OverrideDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public OverrideDialog() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setStr1Label(String str) {
        this.str1Label.setText(str);
    }

    public void setStr2Label(String str) {
        this.str2Label.setText(str);
    }

    public SimpleBooleanProperty getAnswer() {
        return answer;
    }

    public void setAnswer(SimpleBooleanProperty answer) {
        this.answer = answer;
    }
    
    @FXML public void cancel() {
        dialogStage.close();
    }
    
    @FXML public void save() {
        answer.set(true);
        dialogStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void mouseEntered(MouseEvent e) {
        Node node = (Node) e.getSource();
        node.getScene().setCursor(Cursor.HAND);
    }

    public void mouseExited(MouseEvent e) {
        Node node = (Node) e.getSource();
        try {
            node.getScene().setCursor(Cursor.DEFAULT);
        } catch (NullPointerException w) {
        }
    }
}
