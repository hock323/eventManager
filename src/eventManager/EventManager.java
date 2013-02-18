package eventManager;

import eventManager.controller.DesktopController;
import eventManager.controller.pokerTournament.TournamentController;
import eventManager.fx.ExitDialog;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EventManager extends Application {
    @Override
    public void start(final Stage primaryStage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/eventManager/res/fonts/Exo-Regular.ttf"), 18);
        Font.loadFont(getClass().getResourceAsStream("/eventManager/res/fonts/Exo-Bold.ttf"), 18);
        Font.loadFont(getClass().getResourceAsStream("/eventManager/res/fonts/CPTMono-Bold.ttf"), 18); 
        final ResourceBundle bundle;
        switch (System.getProperty("user.language")){
            case "es":
                bundle = ResourceBundle.getBundle("eventManager.bundles.Bundle_es_ES", new Locale("es", "ES"));
                break;
            case "en":
                bundle = ResourceBundle.getBundle("eventManager.bundles.Bundle_en_EN", new Locale("en", "EN"));
                break;
            default:
                bundle = ResourceBundle.getBundle("eventManager.bundles.Bundle_en_EN", new Locale("en", "EN"));
                break;
        }
        URL location = getClass().getResource("/eventManager/fx/EventManager.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setResources(bundle);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        AnchorPane root = (AnchorPane) fxmlLoader.load();
        Scene scene = new Scene(root, Color.TRANSPARENT);
        final DesktopController controller = (DesktopController) fxmlLoader.getController();
        controller.addEventCommand(null);
        controller.setStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle(bundle.getString("application.title"));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                boolean allSaved = true;
                for (TournamentController c : controller.getTournamentControllerList()) {
                    if (c.isTournamentChanged()) {
                        allSaved = false;
                    }
                }                
                if (!allSaved) {
                    SimpleBooleanProperty answer = new SimpleBooleanProperty(false);
                    ExitDialog o = new ExitDialog(primaryStage.getOwner(), "Are you sure to exit?", "You will lose the not saved changes", answer, bundle);
                    answer.addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                            if (t1) {
                                primaryStage.close();
                                controller.getExecutorService().shutdownNow();
                            }
                        }
                    });
                    t.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
