package eventManager.controller.pokerTournament;

import eventManager.controller.VisorLocation;
import eventManager.fx.OverrideDialog;
import eventManager.model.pokerTournament.BlindStruct;
import eventManager.model.pokerTournament.BreakTypeList;
import eventManager.model.pokerTournament.Level;
import eventManager.model.pokerTournament.Tournament;
import eventManager.net.TournamentDataView;
import eventManager.per.ObjectDeserializer;
import eventManager.per.ObjectSerializer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class BlindsController implements Initializable{
    private ObservableList<BlindStruct> blindStructObservableList = FXCollections.observableList(new ArrayList<BlindStruct>());
    String imageSource = "/eventManager/fx/icons/";
    private SimpleBooleanProperty blindSelected = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty tournamentChanged;
    private SimpleBooleanProperty createMode;
    private ObservableList<VisorLocation> visorList;
    @FXML Label durationLabel;
    @FXML Label loadBlindLabel;
    @FXML TableView<BlindStruct> blindStructListView;
    @FXML TableColumn<BlindStruct, String> nameColumn;
    @FXML TableColumn<BlindStruct, String> durationColumn;
    @FXML TextField levelField;
    @FXML TextField timeField;
    @FXML TextField anteField;
    @FXML TextField bigBlindField;
    @FXML TextField smallBlindField;
    @FXML TextField breakDurationField;
    @FXML ComboBox<String> saveBlindBox;
    @FXML ChoiceBox<String> breakTypeBox;
    @FXML TableView<Level> blindStructTableView;
    @FXML TableColumn<Level, Integer> level;
    @FXML TableColumn<Level, Integer> time;
    @FXML TableColumn<Level, Integer> ante;
    @FXML TableColumn<Level, Integer> sb;
    @FXML TableColumn<Level, Integer> bb;
    @FXML TableColumn<Level, String> breather;
    Tournament tournament;
    ResourceBundle resourceBundle;
    ContextMenu blindTableMenu;
    MenuItem deleteItem;
    MenuItem insertAflerItem;
    MenuItem insertBeforeItem;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        chargeBlindStructView();
        chargeBlindStructListView();

    }

    public ObservableList<VisorLocation> getVisorList() {
        return visorList;
    }

    public void setVisorList(ObservableList<VisorLocation> visorList) {
        this.visorList = visorList;
    }

    public void bind() {
        if (tournament.getTournamentConfiguration().getBlindStruct() != null) {
            blindStructTableView.getItems().setAll(tournament.getTournamentConfiguration().getBlindStruct().getLevelList());
            blindStructListView.getSelectionModel().clearSelection();
        } else {
            selectBlindsCommand(null);
        }
        if (!"New Configuration".equals(tournament.getTournamentConfiguration().getName())) {
            blindSelected.set(true);
        }
        blindStructTableView.getSelectionModel().select(tournament.getLevelInProgress().getLevel() - 1);
        ArrayList<String> blinds = new ArrayList<>();
        for (BlindStruct b : blindStructListView.getItems()) {
            blinds.add(b.getName());
        }
        saveBlindBox.setItems(FXCollections.observableList(blinds));
    }

    public void setCreateMode(SimpleBooleanProperty createMode) {
        this.createMode = createMode;
    }
    
    public SimpleBooleanProperty blindSelectedProperty() {
        return blindSelected;
    }
   
    public void setTournamentChanged(SimpleBooleanProperty tournamentChanged) {
        this.tournamentChanged = tournamentChanged;
    }
    
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public TableView<Level> getBlindStructTableView() {
        return blindStructTableView;
    }
   
    @FXML public void selectBlindsCommand(ActionEvent e) {
        if (blindStructListView.getSelectionModel().getSelectedItem() != null) {
            tournament.getTournamentConfiguration().setBlindStruct(blindStructListView.getSelectionModel().getSelectedItem());
            blindStructTableView.getItems().setAll(blindStructListView.getSelectionModel().getSelectedItem().getLevelList());
            blindStructTableView.getSelectionModel().selectFirst();
            calculateTournamentDuration();
            tournament.setLevelInProgress(1);
            blindSelected.set(true);
        }
    }

    public void saveBlindStruct(ActionEvent e) throws IOException {
        final String name = saveBlindBox.getValue();
        if (!"".equals(name)) {
            if (saveBlindBox.getItems().indexOf(name) >= 0) {
                SimpleBooleanProperty answer = new SimpleBooleanProperty(false);
                OverrideDialog o = new OverrideDialog(saveBlindBox.getScene().getWindow(), name + 
                        resourceBundle.getString("saveQuestion1.text"), resourceBundle.getString("saveQuestion2.text"), answer, resourceBundle);
                answer.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                        if (t1) {
                            ObjectSerializer.saveObject(tournament.getTournamentConfiguration().getBlindStruct(), name, false);
                        }
                    }
                });
            } else {
                ObjectSerializer.saveObject(tournament.getTournamentConfiguration().getBlindStruct(), name, false);
            }
            chargeBlindStructListView();
        }
    }

    public void newBlindStruct(ActionEvent e) {
        BlindStruct blinds = new BlindStruct();
        blinds.getLevelList().add(new Level(1, 0, 0, 0, 0, "0"));
        blinds.setName("New Blinds");
        blindStructListView.getItems().add(blinds);
        blindStructListView.getSelectionModel().select(blinds);
    }

    public void deleteBlindLevelCommand(ActionEvent e) {
        Level l = blindStructTableView.getSelectionModel().getSelectedItem();
        int x = blindStructTableView.getSelectionModel().getSelectedIndex();
        blindStructTableView.getItems().remove(l);
        for (int i = x; i < blindStructTableView.getItems().size(); i++) {
            blindStructTableView.getItems().get(i).setLevel(i + 1);
        }
        blindStructTableView.getSelectionModel().select(x);
        ArrayList<String> blinds = new ArrayList<>();
        for (BlindStruct b : blindStructListView.getItems()) {
            blinds.add(b.getName());
        }
        saveBlindBox.setItems(FXCollections.observableList(blinds));
        if (!createMode.get()) {
            TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
            tournamentChanged.set(true);
        }
    }

    public void addBlindLevelCommand(ActionEvent e) {
        ObservableList<Level> list = tournament.getTournamentConfiguration().getBlindStruct().getLevelList();
        list.add(new Level(list.size()+1, 0, 0, 0, 0, "0"));
        if (tournament.getTournamentConfiguration().getBlindStruct().getName() != null){
            blindStructTableView.getItems().setAll(list);
        }
        blindStructTableView.getSelectionModel().select(list.size()-1);
        blindStructTableView.scrollTo(blindStructTableView.getItems().size()-1);
        timeField.requestFocus();
        if (!createMode.get()) {
            TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
            tournamentChanged.set(true);
        }
    }

    private void chargeBlindStructView() {
        blindStructTableView.setEditable(true);
        level.setCellValueFactory(new PropertyValueFactory<Level, Integer>("level"));
        time.setCellValueFactory(new PropertyValueFactory<Level, Integer>("time"));
        ante.setCellValueFactory(new PropertyValueFactory<Level, Integer>("ante"));
        sb.setCellValueFactory(new PropertyValueFactory<Level, Integer>("smallBlind"));
        bb.setCellValueFactory(new PropertyValueFactory<Level, Integer>("bigBlind"));
        breather.setCellValueFactory(new PropertyValueFactory<Level, String>("breather"));
        breakTypeBox.getItems().setAll(new BlindStruct().getBreakTypeList());
        breakTypeBox.getSelectionModel().selectFirst();
        blindStructTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Level>() {
            @Override
            public void changed(ObservableValue<? extends Level> ov, Level t, Level t1) {
                if (t1 != null) {
                    levelField.setText(t1.getLevel() + "");
                    timeField.setText(t1.getTime() + "");
                    anteField.setText(t1.getAnte() + "");
                    bigBlindField.setText(t1.getBigBlind() + "");
                    smallBlindField.setText(t1.getSmallBlind() + "");
                    try {
                        int time = Integer.parseInt(t1.getBreather());
                        breakDurationField.setText(time + "");
                        if (time == 0) {
                            breakTypeBox.getSelectionModel().select("None");
                        } else {
                            breakTypeBox.getSelectionModel().select("Normal Break");
                        }
                    } catch (NumberFormatException w) {
                        if (t1.getBreather().contains("CR")) {
                            breakTypeBox.getSelectionModel().select("Chip Race");
                        }
                        if (t1.getBreather().contains("ED")) {
                            breakTypeBox.getSelectionModel().select("End Of Day");
                        }
                        if (t1.getBreather().contains("DB")) {
                            breakTypeBox.getSelectionModel().select("Dinner Break");
                        }
                        try {
                            int time = Integer.parseInt(t1.getBreather().substring(2));
                            breakDurationField.setText(time + "");
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final ContextMenu blindTableMenu = new ContextMenu();
                insertAflerItem = new MenuItem(resourceBundle.getString("insertAfter.text"));
                insertBeforeItem = new MenuItem(resourceBundle.getString("insertBefore.text"));
                deleteItem = new MenuItem(resourceBundle.getString("delete.text"));
                blindTableMenu.getItems().add(insertBeforeItem);
                blindTableMenu.getItems().add(insertAflerItem);
                blindTableMenu.getItems().add(deleteItem);
                insertAflerItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        insert(e);
                    }
                });
                insertBeforeItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        insert(e);
                    }
                });
                deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        removeTableRow(e);
                    }
                });
                blindStructTableView.setContextMenu(blindTableMenu);
            }
        });
        breakDurationField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent t) {
                if (t.getCode() != KeyCode.ENTER) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String duration = breakDurationField.getText();
                                int x = Integer.parseInt(duration);
                                if (x != 0) {
                                    switch (breakTypeBox.getSelectionModel().getSelectedItem()) {
                                        case "Normal Break":
                                            blindStructTableView.getSelectionModel().getSelectedItem().setBreather(x + "");
                                            break;
                                        case "Dinner Break":
                                            blindStructTableView.getSelectionModel().getSelectedItem().setBreather("DB" + x);
                                            break;
                                        case "Chip Race":
                                            blindStructTableView.getSelectionModel().getSelectedItem().setBreather("CR" + x);
                                            break;
                                        case "End Of Day":
                                            blindStructTableView.getSelectionModel().getSelectedItem().setBreather("ED" + x);
                                            break;
                                    }
                                } else {
                                    breakTypeBox.getSelectionModel().select("None");
                                    x = Integer.parseInt(breakDurationField.getText() + t.getCharacter());
                                    blindStructTableView.getSelectionModel().getSelectedItem().setBreather(x + "");
                                }
                                calculateTournamentDuration();
                            } catch (NumberFormatException | StringIndexOutOfBoundsException w) {
                                String s = breakDurationField.getText();
                                breakDurationField.setText(s.replace(t.getCharacter(), ""));
                                blindStructTableView.getSelectionModel().getSelectedItem().setBreather("0");
                            }
                        }
                    });
                }
            }
        });
        breakDurationField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                 if (t.getCode() == KeyCode.ENTER) addBlindLevelCommand(null);
            }
        });
        breakTypeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                BreakTypeList b = new BreakTypeList();
                breakTypeBox.getSelectionModel().select(t1.intValue());
                Level l = blindStructTableView.getSelectionModel().getSelectedItem();
                int time;
                if (l.getBreather().length() > 2) {
                    time = Integer.parseInt(l.getBreather().substring(2));
                } else {
                    time = Integer.parseInt(l.getBreather());
                }
                switch (breakTypeBox.getSelectionModel().getSelectedItem()) {
                    case "None":
                        blindStructTableView.getSelectionModel().getSelectedItem().setBreather("0");
                        breakDurationField.setText("0");
                        breakDurationField.setDisable(true);
                        calculateTournamentDuration();
                        break;
                    case "Normal Break":
                        blindStructTableView.getSelectionModel().getSelectedItem().setBreather(time + "");
                        breakDurationField.setDisable(false);
                        break;
                    case "Dinner Break":
                        blindStructTableView.getSelectionModel().getSelectedItem().setBreather("DB" + time);
                        breakDurationField.setDisable(false);
                        break;
                    case "Chip Race":
                        blindStructTableView.getSelectionModel().getSelectedItem().setBreather("CR" + time);
                        breakDurationField.setDisable(false);
                        break;
                    case "End Of Day":
                        blindStructTableView.getSelectionModel().getSelectedItem().setBreather("ED" + time);
                        breakDurationField.setDisable(false);
                        break;
                }
                if (!createMode.get()) {
                    TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                    tournamentChanged.set(true);
                }
            }
        });
        anteField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent t) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int level = Integer.parseInt(levelField.getText());
                            int t = Integer.parseInt(anteField.getText());
                            blindStructTableView.getSelectionModel().getSelectedItem().setAnte(t);
                            if (level == tournament.getLevelInProgress().getLevel()) {   
                                tournament.getLevelInProgress().setAnte(t);
                            }
                            calculateTournamentDuration();
                        } catch (NumberFormatException e) {
                            String s = anteField.getText();
                            anteField.setText(s.replace(t.getCharacter(), ""));
                        }
                        if (!createMode.get()) {
                            TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                            tournamentChanged.set(true);
                        }
                    }
                });
                t.consume();
            }
        });
        timeField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent t) {
                t.consume();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int level = Integer.parseInt(levelField.getText());
                            int t = Integer.parseInt(timeField.getText());
                            if (t == 0) {
                                t++;
                            }
                            blindStructTableView.getSelectionModel().getSelectedItem().setTime(t);
                            if (level == tournament.getLevelInProgress().getLevel() && createMode.get()) {
                                tournament.getLevelInProgress().setTime(t);
                                tournament.getLevelTimeService().setLevelTime(tournament.getLevelInProgress().getTime()*60);
                            }
                            calculateTournamentDuration();
                        } catch (NumberFormatException e) {
                            String s = timeField.getText();
                            timeField.setText(s.replace(t.getCharacter(), ""));
                        }
                        if (!createMode.get()) {
                            TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                            tournamentChanged.set(true);
                        }
                    }
                });
            }
        });
        bigBlindField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent t) {
                t.consume();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int level = Integer.parseInt(levelField.getText());
                            int t = Integer.parseInt(bigBlindField.getText());
                            blindStructTableView.getSelectionModel().getSelectedItem().setBigBlind(t);
                            if (level == tournament.getLevelInProgress().getLevel() && createMode.get()) {
                                tournament.getLevelInProgress().setBigBlind(t);
                            }
                        } catch (NumberFormatException e) {
                            String s = bigBlindField.getText();
                            bigBlindField.setText(s.replace(t.getCharacter(), ""));
                        }
                        if (!createMode.get()) {
                            TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                            tournamentChanged.set(true);
                        }
                    }
                });

            }
        });
        smallBlindField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent t) {
                t.consume();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int level = Integer.parseInt(levelField.getText());
                            int t = Integer.parseInt(smallBlindField.getText());
                            blindStructTableView.getSelectionModel().getSelectedItem().setSmallBlind(t);
                            if (level == tournament.getLevelInProgress().getLevel() && createMode.get()) {
                                tournament.getLevelInProgress().setSmallBlind(t);
                            }
                        } catch (NumberFormatException e) {
                            String s = smallBlindField.getText();
                            smallBlindField.setText(s.replace(t.getCharacter(), ""));
                        }
                        if (!createMode.get()) {
                            TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                            tournamentChanged.set(true);
                        }
                    }
                });

            }
        });
        blindStructTableView.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                Pane header = (Pane) blindStructTableView.lookup("TableHeaderRow");
                if (header != null && header.isVisible()) {
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                    header.setManaged(false);
                }
            }
        });
        blindStructTableView.getItems().setAll(new BlindStruct().getLevelList());
    }

    public void calculateTournamentDuration() {
        int n = tournament.getTournamentConfiguration().getBlindStruct().calculateDuration();
        int y = n % 60;
        String s;
        if (y <= 9) {
            s = n / 60 + ":0" + n % 60;
        } else {
            s = n / 60 + ":" + n % 60;
        }
        durationLabel.setText(s);
    }

    private void chargeBlindStructListView() {
        blindStructObservableList = FXCollections.observableList(new ArrayList<BlindStruct>());
        File dir = new File(ObjectDeserializer.DIR + ObjectDeserializer.BLINDS_DIR);
        String[] ficheros = dir.getAbsoluteFile().list();
        BlindStruct blindStruct;
        for (int x = 0; x < ficheros.length; x++) {
            if (!ficheros[x].startsWith(".") && ficheros[x].endsWith(".xml")) {
                blindStruct = (BlindStruct) ObjectDeserializer.loadObject(ObjectDeserializer.BLINDS_OBJECT, ficheros[x]);
                blindStructObservableList.add(blindStruct);
            }
        }
        nameColumn.setCellValueFactory(new PropertyValueFactory<BlindStruct, String>("name"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<BlindStruct, String>("duration"));
        blindStructListView.setItems(blindStructObservableList);
        blindStructListView.getSelectionModel().selectFirst();
        blindStructListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                selectBlindsCommand(null);
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ContextMenu blindListMenu = new ContextMenu();
                MenuItem removeMenuItem = new MenuItem(resourceBundle.getString("delete.text"));
                MenuItem setDefaultItem = new MenuItem(resourceBundle.getString("setDefault.text"));
                blindListMenu.getItems().add(setDefaultItem);
                blindListMenu.getItems().add(removeMenuItem);
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        deleteBlindStructCommand();
                    }
                });
                setDefaultItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                    }
                });
                blindStructListView.setContextMenu(blindListMenu);
            }
        });
        ArrayList<String> blinds = new ArrayList<>();
        for (BlindStruct b : blindStructListView.getItems()) {
            blinds.add(b.getName());
        }
        saveBlindBox.setItems(FXCollections.observableList(blinds));
    }

    public void deleteBlindStructCommand() {
        if (blindStructListView.getSelectionModel().getSelectedIndex() >= 0) {
            File fichero = new File(ObjectDeserializer.DIR + ObjectDeserializer.BLINDS_DIR+ ObjectDeserializer.SEPARATOR
                    + blindStructListView.getSelectionModel().getSelectedItem().getName() + ".xml");
            fichero.delete();
            blindStructListView.getItems().remove(blindStructListView.getSelectionModel().getSelectedItem());
            if (blindStructListView.getItems().size() > 0) {
                blindStructListView.getSelectionModel().selectFirst();
            }
        }
        ArrayList<String> blinds = new ArrayList<>();
        for (BlindStruct b : blindStructListView.getItems()) {
            blinds.add(b.getName());
        }
        saveBlindBox.setItems(FXCollections.observableList(blinds));
    }

    public void clearBlindTable(ActionEvent e) {
        BlindStruct blinds = new BlindStruct();
        blinds.getLevelList().add(new Level(1, 1, 0, 0, 0, "0"));
        tournament.getTournamentConfiguration().setBlindStruct(blinds);
        blindStructTableView.setItems(tournament.getTournamentConfiguration().getBlindStruct().getLevelList());
        blindStructTableView.getSelectionModel().select(0);
        
    }

    public void insert(ActionEvent e) {
        MenuItem m = (MenuItem) e.getSource();
        if (m == insertAflerItem) {
            Level l = blindStructTableView.getSelectionModel().getSelectedItem();
            int x = blindStructTableView.getSelectionModel().getSelectedIndex() + 2;
            ObservableList<Level> list = tournament.getTournamentConfiguration().getBlindStruct().getLevelList();
            list.add(l.getLevel(), new Level(l.getLevel() + 1, 1, 0, 0, 0, "0"));
            for (int i = x; i < list.size(); i++) {
                list.get(i).setLevel(i + 1);
            }
            blindStructTableView.getItems().setAll(list);
            blindStructTableView.getSelectionModel().select(x - 1);
        } else {
            Level l = blindStructTableView.getSelectionModel().getSelectedItem();
            int x = blindStructTableView.getSelectionModel().getSelectedIndex();
            ObservableList<Level> list = tournament.getTournamentConfiguration().getBlindStruct().getLevelList();
            list.add(l.getLevel() - 1, new Level(l.getLevel() + 1, 1, 0, 0, 0, "0"));
            for (int i = x; i < list.size(); i++) {
                list.get(i).setLevel(i + 1);
            }
            blindStructTableView.getItems().setAll(list);
            blindStructTableView.getSelectionModel().select(x);
            if (!createMode.get()) {
                TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                tournamentChanged.set(true);
            }
        }
    }

    public void removeTableRow(ActionEvent e) {
        ObservableList<Level> list = tournament.getTournamentConfiguration().getBlindStruct().getLevelList();
        int x = blindStructTableView.getSelectionModel().getSelectedIndex();
        list.remove(x);
        for (int i = x; i < list.size(); i++) {
            list.get(i).setLevel(i + 1);
        }
        blindStructTableView.getItems().setAll(list);
        blindStructTableView.getSelectionModel().select(x);
        if (!createMode.get()) {
            TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
            tournamentChanged.set(true);
        }
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

    public void setInitedState() {
        blindStructListView.setDisable(true);
        loadBlindLabel.setDisable(true);
    }
}
