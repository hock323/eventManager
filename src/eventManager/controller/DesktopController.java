package eventManager.controller;

import eventManager.controller.pokerTournament.TournamentController;
import eventManager.controller.pokerTournament.TournamentVisorController;
import eventManager.model.pokerTournament.BuyIn;
import eventManager.model.pokerTournament.Tournament;
import eventManager.model.pokerTournament.TournamentConfiguration;
import eventManager.per.ObjectDeserializer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DesktopController implements Initializable {

    private ObservableList<TournamentController> tournamentControllerList = FXCollections.observableList(new ArrayList<TournamentController>());
    private ObservableList<EventSelectionDialogController> tournamentDialogList = FXCollections.observableList(new ArrayList<EventSelectionDialogController>());
    private final ObservableList<VisorLocation> visorList = FXCollections.observableList(new ArrayList<VisorLocation>());
    private String FX_PATH;
    @FXML Stage stage;
    @FXML AnchorPane primePanel;
    @FXML Button addEventButton;
    @FXML TabPane eventTabPane;
    private ExecutorService executorService =
            new ThreadPoolExecutor(
            10, // core thread pool size
            10, // maximum thread pool size
            1, // time to wait before resizing pool
            TimeUnit.DAYS,
            new ArrayBlockingQueue<Runnable>(10, true),
            new ThreadPoolExecutor.CallerRunsPolicy());
    ResourceBundle resourceBundle;
    TournamentController tournamentController;
    AnchorPane tournamentPane;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        FX_PATH=rb.getString("fxPath.text");
        TournamentVisorController.poll(visorList);
    }

    public ObservableList<TournamentController> getTournamentControllerList() {
        return tournamentControllerList;
    }      

    public ExecutorService getExecutorService() {
        return executorService;
    }
   
    @FXML public void addEventCommand(ActionEvent e) {
        try {
            addEventButtonTraslate(true);
            URL location = getClass().getResource(FX_PATH + "EventDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            StackPane dialogPane = (StackPane) fxmlLoader.load();
            EventSelectionDialogController dialogController = fxmlLoader.getController();
            dialogController.getNewTournamentButton().setUserData(dialogController);
            tournamentDialogList.add(dialogController);
            final Tab tab = new Tab("Evento Nuevo");
            tab.setOnClosed(new EventHandler<Event>() {
                @Override
                public void handle(final Event t) {
                    Tab tab = (Tab) t.getSource();

                    addEventButtonTraslate(false);
                    for (TournamentController controller : tournamentControllerList) {
                        if (tab.getText().equals(controller.getTournament().getTournamentName())) {
                            controller.getTournament().getLevelTimeService().cancel();
                        }
                    }

                }
            });
            tab.setContent(dialogPane);
            eventTabPane.getTabs().add(tab);
            eventTabPane.getSelectionModel().selectLast();
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        URL location = getClass().getResource(FX_PATH + "Tournament.fxml");
                        FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
                        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                        tournamentPane = (AnchorPane) fxmlLoader.load();
                        tournamentController = fxmlLoader.getController();
                    } catch (IOException ex) {
                        Logger.getLogger(DesktopController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                }
            };
            new Thread(task).start();
            setTemplateSelectedListener(dialogController, tab);
            setNewTournamentSelectedListener(dialogController, tab);
            setSavedTournamentSelectedListener(dialogController, tab);
        } catch (IOException ex) {
            System.out.println("ADD EVENT COMMAND" + ex.getMessage());
        }
    }

    private void setSavedTournamentSelectedListener(EventSelectionDialogController dialogController, final Tab tab) {
        dialogController.getSavedTournamentSelected().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                Tournament tournament = (Tournament) ObjectDeserializer.loadObject(ObjectDeserializer.TOURNAMENT_OBJECT, t1);
                tournament.addTimeListener(executorService);
                tournament.getSummary().setConfiguration(tournament.getTournamentConfiguration());
                tournamentController.setTournament(tournament);
                tournamentControllerList.add(tournamentController);
                tournamentController.setVisorList(visorList);
                tournamentController.chargeVisorTableView();
                eventTabPane.getSelectionModel().getSelectedItem().setContent(tournamentPane);
                eventTabPane.getSelectionModel().getSelectedItem().textProperty().bind(tournamentController.getNameField().textProperty());
                addSaveButton(tab, tournamentController);
                primePanel.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }
    
    private void setTemplateSelectedListener(EventSelectionDialogController dialogController, final Tab tab) {
        dialogController.getTournamentTemplateSelected().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                TournamentConfiguration tc = (TournamentConfiguration) ObjectDeserializer.loadObject(ObjectDeserializer.TOURNAMENT_CONFIGURATION_OBJECT, t1);
                Tournament tournament = new Tournament(resourceBundle.getString("newTournament.text"), tc, executorService);
                tournament.setLevelInProgress(1);
                tournamentControllerList.add(tournamentController);
                tournamentController.setTournament(tournament);
                tournamentController.setVisorList(visorList);
                tournamentController.chargeVisorTableView();
                eventTabPane.getSelectionModel().getSelectedItem().setContent(tournamentPane);
                eventTabPane.getSelectionModel().getSelectedItem().textProperty().bind(tournamentController.getNameField().textProperty());
                addSaveButton(tab, tournamentController);
                primePanel.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void setNewTournamentSelectedListener(EventSelectionDialogController dialogController, final Tab tab) {
        dialogController.newTournamentSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                primePanel.getScene().setCursor(Cursor.WAIT);
                TournamentConfiguration tc = new TournamentConfiguration(new BuyIn(0, 0), null, null);
                tc.setName("New Configuration");
                Tournament tournament = new Tournament(resourceBundle.getString("newTournament.text"), tc, executorService);
                tournamentControllerList.add(tournamentController);
                tournamentController.setTournament(tournament);
                tournamentController.setVisorList(visorList);
                tournamentController.chargeVisorTableView();
                eventTabPane.getSelectionModel().getSelectedItem().setContent(tournamentPane);
                eventTabPane.getSelectionModel().getSelectedItem().textProperty().bind(tournamentController.getNameField().textProperty());
                addSaveButton(tab, tournamentController);
                primePanel.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    public void addEventButtonTraslate(boolean add) {
        double pos;
        double ini = addEventButton.getLayoutX();
        if (add) {
            pos = ini + 270;
        } else {
            pos = ini - 270;
        }
        addEventButton.setLayoutX(pos);
    }

    private void addSaveButton(Tab tab, final TournamentController tournamentController) {
        final Button saveButton = new Button();
        saveButton.setGraphicTextGap(2);
        saveButton.setStyle("-fx-background-color: transparent;");
        saveButton.setMinWidth(16);
        saveButton.setMaxWidth(16);
        saveButton.setPrefWidth(16);
        saveButton.setMaxHeight(16);
        Image image = new Image(getClass().getResourceAsStream(FX_PATH + "icons/bullet_red.png"));
        saveButton.setGraphic(new ImageView(image));
        tab.setGraphic(saveButton);
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                tournamentController.saveTournamentCommand(null);
                tournamentController.tournamentChangedProperty().set(false);
            }
        });
        saveButton.visibleProperty().bind(tournamentController.tournamentChangedProperty());
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                executorService.shutdownNow();
            }
        });
    }

    public void mouseEntered(MouseEvent e) {
        Node node = (Node) e.getSource();
        node.getScene().setCursor(Cursor.HAND);
    }

    public void mouseExited(MouseEvent e) {
        Node node = (Node) e.getSource();
        node.getScene().setCursor(Cursor.DEFAULT);
    }
}
