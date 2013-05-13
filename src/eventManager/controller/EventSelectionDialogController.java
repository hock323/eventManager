package eventManager.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class EventSelectionDialogController implements Initializable {
    private final String SEPARATOR = System.getProperties().getProperty("file.separator");
    private final String RESOURCE_PATH = System.getProperties().getProperty("user.dir")
            + SEPARATOR + "eventManager" + SEPARATOR + "res" + SEPARATOR;
    private final String iconSource = 
            ResourceBundle.getBundle("eventManager.bundles.Bundle_es_ES", new Locale("es", "ES")).getString("imageSource.text");
    @FXML Pane loadTemplateButton;
    @FXML Pane openEventButton;
    @FXML Pane newEventButton;
    @FXML VBox eventChooserPane;  
    @FXML Polygon pointer;
    @FXML Button selectButton;
    @FXML AnchorPane buttonsBox;
    @FXML Pane tournamentInfoPane;
    private final int MAX_EVENTS_PER_LINE = 4;
    private SimpleBooleanProperty newTournamentSelected = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty newEventsShowing = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty templatesShowing = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty savedEventsShowing = new SimpleBooleanProperty(false);
    private SimpleStringProperty tournamentTemplateSelected = new SimpleStringProperty("");
    private SimpleStringProperty savedTournamentSelected = new SimpleStringProperty("");
    private ResourceBundle resourceBundle;
    private enum EventType {NEW, TEMPLATE, SAVED};    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        setOpenEventButtonListener();
        setTemplateButtonListener();
        setNewEventButtonListener();
        chargeNewEventList();
    }

    public Pane getLoadTournamentButton() {
        return openEventButton;
    }

    public Pane getLoadConfigurationButton() {
        return loadTemplateButton;
    }

    public Pane getNewTournamentButton() {
        return newEventButton;
    }

    public SimpleStringProperty getTournamentTemplateSelected() {
        return tournamentTemplateSelected;
    }

    public SimpleStringProperty getSavedTournamentSelected() {
        return savedTournamentSelected;
    }
    
    public SimpleBooleanProperty newTournamentSelected() {
        return newTournamentSelected;
    }
   
    private void setOpenEventButtonListener() {
        openEventButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                movePointerTransition(openEventButton);
                templatesShowing.set(false);
                newEventsShowing.set(false);
                savedEventsShowing.set(true);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EventSelectionDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                chargeLastTournamentList();
            }
        });
    }

    private void setTemplateButtonListener() {
        loadTemplateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                movePointerTransition(loadTemplateButton);
                templatesShowing.set(true);
                savedEventsShowing.set(false);
                newEventsShowing.set(false);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EventSelectionDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                chargeTournamentConfigurationList();
            }
        });
    }
    
    private void setNewEventButtonListener() {
        newEventButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                movePointerTransition(newEventButton);
                templatesShowing.set(false);
                savedEventsShowing.set(false);
                newEventsShowing.set(true);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EventSelectionDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                chargeNewEventList();
            }
        });
    }

    private void chargeNewEventList() {
        ObservableList<String> list = FXCollections.observableList(new ArrayList<String>());
        list.add("Tournament");
        list.add("Advertisement");
        showNewEvents(list);
    }

    private void chargeLastTournamentList() {
        String dirPath = RESOURCE_PATH + "Tournaments" + SEPARATOR;
        ObservableList<String> list = FXCollections.observableList(new ArrayList<String>());
        File dir = new File(dirPath);
        String[] ficheros = dir.getAbsoluteFile().list();
        for (int x = 0; x < ficheros.length; x++) {
            if (!ficheros[x].startsWith(".") && ficheros[x].endsWith(".xml")) {
                list.add(ficheros[x].substring(0, (ficheros[x].lastIndexOf("."))));
            }
        }
        setInListView(list, EventType.SAVED);
    }

    private void chargeTournamentConfigurationList() {
        String dirPath = RESOURCE_PATH + "TournamentConfigurations" + SEPARATOR;
        ObservableList<String> list = FXCollections.observableList(new ArrayList<String>());
        File dir = new File(dirPath);
        String[] ficheros = dir.getAbsoluteFile().list();
        for (int x = 0; x < ficheros.length; x++) {
            if (!ficheros[x].startsWith(".") && ficheros[x].endsWith(".xml")) {
                list.add(ficheros[x].substring(0, (ficheros[x].lastIndexOf("."))));
            }
        }
        setInListView(list, EventType.TEMPLATE);
    }
    
    public void movePointerTransition(Node nodo) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(400), pointer);
        translateTransition.setFromY(pointer.getTranslateY());
        if ("newEventButton".equals(nodo.getId())) {
            if (pointer.getTranslateY() > 100) {
                if (pointer.getTranslateY() > 300) {
                    translateTransition.setToY(pointer.getTranslateY() - 360);
                } else {
                    translateTransition.setToY(pointer.getTranslateY() - 180);
                }
            }
        } else {
            if ("loadTemplateButton".equals(nodo.getId())) {
                if (pointer.getTranslateY() != 180) {
                    if (pointer.getTranslateY() > 180) {
                        translateTransition.setToY(pointer.getTranslateY() - 180);
                    } else {
                        translateTransition.setToY(pointer.getTranslateY() + 180);
                    }
                }
            } else {
                if (pointer.getTranslateY() != 360) {
                    if (pointer.getTranslateY() < 180) {
                        translateTransition.setToY(pointer.getTranslateY() + 360);
                    } else {
                        translateTransition.setToY(pointer.getTranslateY() + 180);
                    }
                }
            }
        }
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
    }

    private void showNewEvents(ObservableList<String> list) {
        HBox destiny = null;
        eventChooserPane.getChildren().clear();
        int i = 0;
        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            String name = it.next();
            if (i % MAX_EVENTS_PER_LINE == 0) {
                destiny = new HBox(12.0);
                eventChooserPane.getChildren().add(destiny);
            }
            Pane eventPane = createEventPane(name);
            newEventSelectedListener(eventPane);
            addIconToEventPane(eventPane, name, EventType.NEW);
            destiny.getChildren().add(eventPane);
            i++;
        }
    }
    
    private Pane createEventPane(String eventName) {
        Pane eventPane = new Pane();
        setMousePointerListener(eventPane);
        eventPane.setMinHeight(110);
        eventPane.setPrefHeight(110);
        eventPane.setPrefWidth(116);
        eventPane.setMinWidth(116);
        eventPane.setStyle("-fx-background-color: #2D86EE;");
        Label nameLabel = new Label(eventName);
        nameLabel.setId("name");
        nameLabel.setStyle("-fx-padding: 0 -5 0 0;");
        nameLabel.setMaxWidth(90);
        nameLabel.setWrapText(false);
        nameLabel.setLayoutX(14);
        nameLabel.setLayoutY(86);
        eventPane.getChildren().add(nameLabel);
        return eventPane;
    }
    
    private void newEventSelectedListener(Pane eventPane){
        eventPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Pane pane = (Pane) t.getSource();
                    Label lName = (Label) pane.lookup("#name");
                if ("Tournament".equals(lName.getText())) {
                    newTournamentSelected.set(true);
                }
            }
        });
    }
    
    private void addIconToEventPane(Pane eventPane, String eventName, EventType eventType) {
        Image image;
        ImageView imageView;
        if ("Advertisement".equals(eventName)) {
            image = new Image(getClass().getResourceAsStream(iconSource + "advertise.png"));
        } else {
            if (eventType.equals(EventType.SAVED)) {
                image = new Image(getClass().getResourceAsStream(iconSource + "open_poker.png"));
            } else {
                if (eventType.equals(EventType.NEW)) {
                    image = new Image(getClass().getResourceAsStream(iconSource + "spade.png"));
                } else {
                    image = new Image(getClass().getResourceAsStream(iconSource + "poker_template.png"));
                }
            }
        }
        imageView = new ImageView(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setLayoutX(28);
        imageView.setLayoutY(14);
        eventPane.getChildren().add(imageView);

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
    
    public void setMousePointerListener(Node node) {
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseEntered(t);
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseExited(t);
            }
        });
    }
    
    private void setInListView(ObservableList<String> list, EventType eventType) {
        HBox destiny = null;
        eventChooserPane.getChildren().clear();
        int i = 0;
        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            String name = it.next();
            if (i % MAX_EVENTS_PER_LINE == 0) {
                destiny = new HBox(12.0);
                eventChooserPane.getChildren().add(destiny);
            }
            Pane eventPane = createEventPane(name);
            addIconToEventPane(eventPane, "Tournament", eventType);
            eventPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    Pane pane = (Pane) t.getSource();
                    Label lName = (Label) pane.lookup("#name");
                    if (templatesShowing.get()) {
                        tournamentTemplateSelected.set(lName.getText());
                    } else {
                        savedTournamentSelected.set(lName.getText());
                    }
                }
            });
            addContextMenu(eventPane);
            destiny.getChildren().add(eventPane);
            i++;
        }
    }

    private void addContextMenu(final Pane eventPane) {
        ContextMenu prizesListMenu = new ContextMenu();
        MenuItem removeMenuItem = new MenuItem(resourceBundle.getString("deleteButton.text"));
        prizesListMenu.getItems().add(removeMenuItem);
        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                eventPane.setVisible(false);
                deleteEvent(eventPane);
            }
        });
    }

    private void deleteEvent(Pane eventPane) {
        Label eventName = (Label) eventPane.lookup("#name");
        String eventURL;
        if (templatesShowing.get()) {
            eventURL = RESOURCE_PATH + "TournamentConfigurations" + SEPARATOR + eventName.getText() + ".xml";
        } else {
            eventURL = RESOURCE_PATH + "Tournaments" + SEPARATOR + eventName.getText() + ".xml";
        }
        File file = new File(eventURL);
        file.delete();
    }
}
