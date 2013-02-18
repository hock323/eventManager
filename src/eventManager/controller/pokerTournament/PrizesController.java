package eventManager.controller.pokerTournament;

import eventManager.controller.VisorLocation;
import eventManager.fx.EditingIntegerChopCell;
import eventManager.fx.EditingIntegerPrizeCell;
import eventManager.fx.EditingPrizeCell;
import eventManager.fx.OverrideDialog;
import eventManager.model.pokerTournament.ChopPosition;
import eventManager.model.pokerTournament.ChopPrizes;
import eventManager.model.pokerTournament.CustomPrizeStruct;
import eventManager.model.pokerTournament.PrizePosition;
import eventManager.model.pokerTournament.PrizeStruct;
import eventManager.model.pokerTournament.PrizeStructList;
import eventManager.model.pokerTournament.SatelitePrizeStruct;
import eventManager.model.pokerTournament.Tournament;
import eventManager.net.TournamentDataView;
import eventManager.per.ObjectDeserializer;
import eventManager.per.ObjectSerializer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
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
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;

public class PrizesController implements Initializable {

    private SimpleBooleanProperty prizeSelected = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty tournamentChanged;
    private SimpleBooleanProperty createMode;
    private ObservableList<VisorLocation> visorList;
    @FXML Label totalPrizes;
    @FXML Label totalPercentages;
    @FXML Label totalPrizesLabel;
    @FXML Label totalPercentagesLabel;
    @FXML Label countedChipsLabel;
    @FXML Label prizeNameLabel;
    @FXML ComboBox<String> savePrizeBox;
    @FXML ChoiceBox<String> customChoice;
    @FXML Button savePrizes;
    @FXML Button chopsButton;
    @FXML Button selectPrizeButton;
    @FXML RadioButton onePackage;
    @FXML RadioButton varPackages;
    @FXML TextField addPrizePos;
    @FXML TextField addPrizeRel;
    @FXML TextField chopChipsField;
    @FXML TextField j;
    @FXML TextField m;
    @FXML TextField c;
    @FXML TextField customPercentage;
    @FXML TextField packagesField;
    @FXML TextField subpackagesField;
    @FXML StackPane editPrizesPane;
    @FXML StackPane prizesStackPane;
    @FXML StackPane prizeTablePane;
    @FXML Pane chopsPane;
    @FXML Pane satelitePane;
    @FXML Pane customPrizesPane;
    @FXML AnchorPane totalPrizesPane;
    @FXML HBox editTableViewPane;
    @FXML ListView<String> prizeStructListView;
    @FXML TableView<ChopPosition> chopsTableView;
    @FXML TableView<PrizePosition> prizeStructTableView;
    @FXML TableColumn<PrizePosition, Integer> positionTableColumn;
    @FXML TableColumn<PrizePosition, Double> relPercentageTableColumn;
    @FXML TableColumn<PrizePosition, Double> absPercentageTableColumn;
    @FXML TableColumn<PrizePosition, Integer> prizeTableColumn;
    @FXML TableColumn<ChopPosition, Integer> chopPositionTableClumn;
    @FXML TableColumn<ChopPosition, Integer> chopChipsTableColumn;
    @FXML TableColumn<ChopPosition, Integer> icmTableColumn;
    @FXML TableColumn<ChopPosition, Integer> simpleTableColumn;
    String imageSource;;
    Tournament tournament;
    ResourceBundle resourceBundle;
    PrizeStructList defaultPrizeStructs = new PrizeStructList();
    ObservableList<String> onFilePrizes = FXCollections.observableList(new ArrayList<String>());
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        imageSource = resourceBundle.getString("imageSource.text");
        chargePrizesTableView();
        chargeChopsStruct();
        chargePrizeStructListView();
        chargeSavePrizesBox();
    }

    public Tournament getTournament() {
        return tournament;
    }

    public SimpleBooleanProperty prizeSelectedProperty() {
        return prizeSelected;
    }
    
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void setTournamentChanged(SimpleBooleanProperty tournamentChanged) {
        this.tournamentChanged = tournamentChanged;
    }

    public void setCreateMode(SimpleBooleanProperty createMode) {
        this.createMode = createMode;
    }

    public ObservableList<VisorLocation> getVisorList() {
        return visorList;
    }

    public void setVisorList(ObservableList<VisorLocation> visorList) {
        this.visorList = visorList;
    }
    
    public TableView<PrizePosition> getPrizeStructTableView() {
        return prizeStructTableView;
    }
    
    public void chargePrizesTableView(){
        prizeStructTableView.setEditable(true);
        positionTableColumn.setCellValueFactory(new PropertyValueFactory<PrizePosition, Integer>("Position"));
        relPercentageTableColumn.setCellValueFactory(new PropertyValueFactory<PrizePosition, Double>("relPercentage"));
        absPercentageTableColumn.setCellValueFactory(new PropertyValueFactory<PrizePosition, Double>("absPercentage"));
        prizeTableColumn.setCellValueFactory(new PropertyValueFactory<PrizePosition, Integer>("money"));         
        Callback<TableColumn<PrizePosition, Double>, TableCell<PrizePosition, Double>> cellDoubleFactory =
                new Callback<TableColumn<PrizePosition, Double>, TableCell<PrizePosition, Double>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingPrizeCell();
                    }
                };
        Callback<TableColumn<PrizePosition, Integer>, TableCell<PrizePosition, Integer>> cellFactory =
                new Callback<TableColumn<PrizePosition, Integer>, TableCell<PrizePosition, Integer>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingIntegerPrizeCell();
                    }
                };
        absPercentageTableColumn.setCellFactory(cellDoubleFactory);
        relPercentageTableColumn.setCellFactory(cellDoubleFactory);
        prizeTableColumn.setCellFactory(cellFactory);
        absPercentageTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PrizePosition, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PrizePosition, Double> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setAbsPercentage(t.getNewValue());
                if (tournament != null) {
                    int s = tournament.getTranscurredTime();
                    int totalPercentages = 0;
                    for (int i = 0; i < prizeStructTableView.getItems().size(); i++) {
                        totalPercentages += prizeStructTableView.getItems().get(i).getAbsPercentage();
                    }
                    totalPercentagesLabel.setText(totalPercentages + "%");
                }
                TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                tournamentChanged.set(true);
            }
        });
        prizeTableColumn.setOnEditCommit( new EventHandler<TableColumn.CellEditEvent<PrizePosition, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PrizePosition, Integer> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setMoney(t.getNewValue());
                if (tournament != null) {
                    int s = tournament.getTranscurredTime();
                    tournament.getTournamentConfiguration().getChopPrizes().calculate();
                    int totalPrizes = 0;
                    for (int i = 0; i < prizeStructTableView.getItems().size(); i++) {
                        totalPrizes += prizeStructTableView.getItems().get(i).getMoney();
                    }
                    totalPrizesLabel.setText(totalPrizes + "€");
                }
                TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                tournamentChanged.set(true);
            }
        });
    }
    
    public void chargeChopsStruct() {
        chopsTableView.setEditable(true);
        chopPositionTableClumn.setCellValueFactory(new PropertyValueFactory<ChopPosition, Integer>("position"));
        chopChipsTableColumn.setCellValueFactory(new PropertyValueFactory<ChopPosition, Integer>("chips"));
        icmTableColumn.setCellValueFactory(new PropertyValueFactory<ChopPosition, Integer>("icm"));
        simpleTableColumn.setCellValueFactory(new PropertyValueFactory<ChopPosition, Integer>("simple"));
        Callback<TableColumn<ChopPosition, Integer>, TableCell<ChopPosition, Integer>> cellFactory =
                new Callback<TableColumn<ChopPosition, Integer>, TableCell<ChopPosition, Integer>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingIntegerChopCell();
                    }
                };
        chopChipsTableColumn.setCellFactory(cellFactory);
        chopChipsTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ChopPosition, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ChopPosition, Integer> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setChips(t.getNewValue());
                if (tournament != null) {
                    int countedChop = 0;
                    for (int i = 0; i < chopsTableView.getItems().size(); i++) {
                        countedChop += chopsTableView.getItems().get(i).getChips();
                    }
                    countedChipsLabel.setText(countedChop + "");
                    tournament.getTournamentConfiguration().getChopPrizes().calculate();
                }
            }
        });
    }

    private void chargePrizeStructListView() {
        ObservableList<String> prizeStructObservableList = FXCollections.observableList(new ArrayList<String>());
        for (PrizeStruct s : defaultPrizeStructs) {
            prizeStructObservableList.add(s.getName());
        }
        String separator = System.getProperties().getProperty("file.separator");
        File dir = new File(System.getProperties().getProperty("user.dir") + separator + "eventManager" + separator + "res" + separator + "prizeStructs");
        String[] ficheros = dir.getAbsoluteFile().list();
        for (int x = 0; x < ficheros.length; x++) {
            if (!ficheros[x].startsWith(".") && ficheros[x].endsWith(".xml")) {
                String name = ficheros[x].substring(0, ficheros[x].indexOf("."));
                onFilePrizes.add(name);
                prizeStructObservableList.add(name);
            }
        }
        prizeStructListView.cellFactoryProperty();
        prizeStructListView.setItems(prizeStructObservableList);
        prizeStructListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ComposeDefinedPrizeStructCell();
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ContextMenu prizesListMenu = new ContextMenu();
                MenuItem removeMenuItem = new MenuItem(resourceBundle.getString("deleteButton.text"));
                MenuItem setDefaultItem = new MenuItem(resourceBundle.getString("setDefault.text"));
                prizesListMenu.getItems().add(setDefaultItem);
                prizesListMenu.getItems().add(removeMenuItem);
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        deletePrizesCommand(null);
                    }
                });
                setDefaultItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                    }
                });
                prizeStructListView.setContextMenu(prizesListMenu);
            }
        });
        prizeStructListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1 != null) {
                    if (t == null || !"Custom Prizes".equals(t) && !"Satelite Tournament".equals(t)) {
                        if ("Custom Prizes".equals(t1)) {
                            customPrizesPane.setVisible(false);
                            satelitePane.setVisible(false);
                            selectPrizeButton.setVisible(true);
                            prizeListTranslation(customPrizesPane);
                            selectPrizeButton.toFront();
                        } else {
                            if ("Satelite Tournament".equals(t1)) {
                                customPrizesPane.setVisible(false);
                                satelitePane.setVisible(false);
                                selectPrizeButton.setVisible(true);
                                prizeListTranslation(satelitePane);
                                selectPrizeButton.toFront();
                            }
                        }
                    } else {
                        if ("Custom Prizes".equals(t1)) {
                            customPrizesPane.setTranslateX(0);
                            customPrizesPane.setVisible(true);
                            satelitePane.setVisible(false);
                            selectPrizeButton.setVisible(true);
                            customPrizesPane.toFront();
                            selectPrizeButton.toFront();
                            customPrizesPane.setOpacity(1);
                        } else {
                            if ("Satelite Tournament".equals(t1)) {
                                customPrizesPane.setVisible(false);
                                satelitePane.setTranslateX(0);
                                satelitePane.setVisible(true);
                                selectPrizeButton.setVisible(true);
                                satelitePane.toFront();
                                selectPrizeButton.toFront();
                                satelitePane.setOpacity(1);
                            } else {
                                satelitePane.setTranslateX(0);
                                satelitePane.setVisible(false);
                                customPrizesPane.setTranslateX(0);
                                customPrizesPane.setVisible(false);
                                selectPrizeButton.setVisible(false);
                            }
                        }
                    }
                }
            }
        });
        prizeStructListView.getSelectionModel().select(0);
        prizeStructListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                    selectPrizesCommand(null);
            }
        });
        customChoice.getItems().clear();
        customChoice.getItems().add("Inscritos");
        customChoice.getItems().add("Inscritos + Reentradas");
        customChoice.getSelectionModel().selectFirst();
        
    }

    private void chargeSavePrizesBox() {
        ArrayList<String> prizesList = new ArrayList<>();
        for (String p : prizeStructListView.getItems()) {
            prizesList.add(p);
        }
        prizesList.addAll(onFilePrizes);
        savePrizeBox.setItems(FXCollections.observableList(prizesList));
    }
    
    private void prizeListTranslation(Pane paneFaded) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), paneFaded);
        fadeTransition.setFromValue(0.0f);
        fadeTransition.setToValue(1.0f);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(700), paneFaded);
        translateTransition.setFromX(50);
        translateTransition.setToX(0);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                translateTransition);
        paneFaded.setVisible(true);
        parallelTransition.play();
    }

    public void savePrizeStruct(ActionEvent e) throws IOException {
        final String name = savePrizeBox.getValue();
        if (!"".equals(name)) {
            if (savePrizeBox.getItems().indexOf(name) >= 0) {
                SimpleBooleanProperty answer = new SimpleBooleanProperty(false);
                OverrideDialog o = new OverrideDialog(savePrizeBox.getScene().getWindow(), name + 
                        resourceBundle.getString("saveQuestion1.text"), resourceBundle.getString("saveQuestion2.text"), answer, resourceBundle);
                answer.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                        if (t1) {
                            ObjectSerializer.saveObject(tournament.getTournamentConfiguration(), name, false);
                            chargeSavePrizesBox();
                        }
                    }
                });
            } else {
                ObjectSerializer.saveObject(tournament.getTournamentConfiguration().getPrizeStruct(), savePrizeBox.getValue(), false);
            }
        }
    }

    @FXML public void addPrizePosition(ActionEvent e) {
        try {
            ObservableList<PrizePosition> data = prizeStructTableView.getItems();
            data.add(new PrizePosition(Integer.parseInt(addPrizePos.getText()), 0.0,
                    Double.parseDouble(addPrizeRel.getText()), 0));
            prizeStructTableView.scrollTo(data.size());
            prizeStructTableView.getSelectionModel().selectLast();
            addPrizePos.setText("");
            addPrizeRel.setText("");
        } catch (NumberFormatException ex) {
            //new ModalDialog(primePanel.getScene().getWindow(), "Incorrect Information", "Please revise the parameters").show();
        }
    }
   
    @FXML public void addChopPrize(ActionEvent e) {
        try {
            ChopPrizes chops = tournament.getTournamentConfiguration().getChopPrizes();
            chops.add(new ChopPosition(chops.getChopsList().size() + 1, Integer.parseInt(chopChipsField.getText()), 0, 0));
            chopsTableView.scrollTo(chopsTableView.getItems().size());
            chopsTableView.getSelectionModel().selectLast();
            chopChipsField.setText("");
            chopsTableView.getItems().setAll(chops.getChopsList());
            int countedChop = 0;
            for (int i = 0; i < chopsTableView.getItems().size(); i++) {
                countedChop += chopsTableView.getItems().get(i).getChips();
            }
            countedChipsLabel.setText(countedChop + "");
        } catch (NumberFormatException ex) {
            //new ModalDialog(primePanel.getScene().getWindow(), "Incorrect Information", "Please revise the parameters").show();
        }
    }
    
    public void showHideChops(ActionEvent e){
        Button b = (Button) e.getSource();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), chopsPane);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        if (b.getText().equals(resourceBundle.getString("showChops.text"))) {
            fadeTransition.setFromValue(0.0f);
            fadeTransition.setToValue(1.0f);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().addAll(
                    fadeTransition);
            chopsPane.setVisible(true);
            parallelTransition.play();
            b.setText(resourceBundle.getString("hideChops.text"));
        } else {
            fadeTransition.setFromValue(1.0f);
            fadeTransition.setToValue(0.0f);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().addAll(
                    fadeTransition);
            parallelTransition.play();
            b.setText(resourceBundle.getString("showChops.text"));
        }
    } 
   
    @FXML public void selectPrizesCommand(ActionEvent e) {
        PrizeStruct prizes = null;
        if (prizeStructListView.getSelectionModel().getSelectedItem() != null) {
            String prizesName = prizeStructListView.getSelectionModel().getSelectedItem();
            if (onFilePrizes.indexOf(prizesName) >= 0) {
                prizes = (PrizeStruct) ObjectDeserializer.loadObject(ObjectDeserializer.PRIZES_OBJECT, prizesName);
            } else {
                for (PrizeStruct p : defaultPrizeStructs) {
                    if (p.getName().equals(prizesName)) {
                        prizes = p;
                        break;
                    }
                }
            }
            try {
                if (prizes instanceof SatelitePrizeStruct) {
                    if (Integer.parseInt(packagesField.getText()) <= 0) {
                        throw new NumberFormatException();
                    }
                    SatelitePrizeStruct s = (SatelitePrizeStruct) prizes;
                    s.setPackageAmount(Integer.parseInt(packagesField.getText()));
                    if (!subpackagesField.isDisable()) {
                        if (Integer.parseInt(subpackagesField.getText()) <= 0) {
                            throw new NumberFormatException();
                        }
                        s.setSubpackageAmount(Integer.parseInt(subpackagesField.getText()));
                    } else {
                        s.setSubpackageAmount(0);
                    }
                    tournament.getTournamentConfiguration().setPrizeStruct(s);
                } else if (prizes instanceof CustomPrizeStruct) {
                    if (Integer.parseInt(this.c.getText()) <= 0 || Integer.parseInt(this.j.getText()) <= 0
                            || Integer.parseInt(this.j.getText()) <= 0 || Integer.parseInt(this.customPercentage.getText()) <= 0) {
                        throw new NumberFormatException();
                    }
                    CustomPrizeStruct c = (CustomPrizeStruct) prizes;
                    c.setC(Integer.parseInt(this.c.getText()));
                    c.setJ(Integer.parseInt(this.j.getText()));
                    c.setM(Integer.parseInt(this.m.getText()));
                    c.setPercentage(Integer.parseInt(this.customPercentage.getText()));

                    if (customChoice.getSelectionModel().getSelectedIndex() == 0) {
                        c.setMode(false);
                    } else {
                        c.setMode(true);
                    }
                    tournament.getTournamentConfiguration().setPrizeStruct(c);
                } else {
                    tournament.getTournamentConfiguration().setPrizeStruct(prizes);
                }
                tournament.getSummary().setPrizeStruct(tournament.getTournamentConfiguration().getPrizeStruct());
                prizeStructTableView.getItems().setAll(tournament.getTournamentConfiguration().getPrizeStruct().getPercentagesList());
                prizeSelected.set(true);
            } catch (NumberFormatException ex) {
                tournament.getTournamentConfiguration().setPrizeStruct(null);
                prizeSelected.set(false);
            }
        }
    }

    @FXML public void deletePrizesCommand(ActionEvent e) {
        if (prizeStructListView.getSelectionModel().getSelectedIndex() >= 0) {
            File dir = new File(ObjectDeserializer.DIR + ObjectDeserializer.PRIZES_DIR);
            String[] ficheros = dir.getAbsoluteFile().list();
            String prize = prizeStructListView.getSelectionModel().getSelectedItem() + ".xml";
            for (int x = 0; x < ficheros.length; x++) {
                if (ficheros[x].equals(prize)) {
                    File fichero = new File(ObjectDeserializer.DIR + ObjectDeserializer.PRIZES_DIR + ObjectDeserializer.SEPARATOR + ficheros[x]);
                    fichero.delete();
                    prizeStructListView.getItems().remove(prizeStructListView.getSelectionModel().getSelectedItem());
                    if (prizeStructListView.getItems().size() > 0) {
                        prizeStructListView.getSelectionModel().selectFirst();
                    }
                    break;
                }
            }
        }
    }

    public void customPackageOption(ActionEvent e) {
        RadioButton r = (RadioButton) e.getSource();
        if ("onePackage".equals(r.getText())) {
            subpackagesField.setDisable(true);
        } else {
            subpackagesField.setDisable(false);
        }
    }

    public void bind() {
        chopsTableView.getItems().setAll(tournament.getTournamentConfiguration().getChopPrizes().getChopsList());
        if (tournament.getTournamentConfiguration().getPrizeStruct() != null) {
            prizeStructTableView.getItems().setAll(tournament.getTournamentConfiguration().getPrizeStruct().getPercentagesList());
            prizeSelected.set(true);
            prizeStructListView.getSelectionModel().clearSelection();
        }
        else {
            selectPrizesCommand(null);
        }
        if (!"New Configuration".equals(tournament.getTournamentConfiguration().getName())) {
            //prizeNameLabel.setText(tournament.getTournamentConfiguration().getPrizeStruct().getName());
            prizeTablePane.setVisible(true);
        }
    }

    public void setInitedState() {
        editPrizesPane.setVisible(false);
        //relPercentageTableColumn.setVisible(false);
        relPercentageTableColumn.setPrefWidth(0);
        prizeTableColumn.setPrefWidth(180);
        StackPane.setMargin(prizeTablePane, new Insets(74, 0, 30, 30));
        StackPane.setMargin(prizeStructTableView, new Insets(0, 30, 40, 0));
        totalPrizesPane.setVisible(true);
        editTableViewPane.setVisible(false);
        chopsButton.setVisible(true);
    }

    private static class ComposeDefinedPrizeStructCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(getItem());
                setGraphic(null);
            }
        }
    }
    
    public void refreshPrizes(ArrayList<Integer> array){
        //totalChipsLabel.setText(array.get(11) + "");
        if (tournament.getTournamentConfiguration().getPrizeStruct() != null) {
            ObservableList<PrizePosition> data = FXCollections.observableArrayList(
                    tournament.getSummary().getPrizeStruct().getPercentagesList());
            prizeStructTableView.getItems().clear();
            int players = tournament.getSummary().getPrizeStruct().getPlayersWhoRecieve();
            int totalPrizes = 0;
            int totalPercentages = 0;
            for (int i = 0; i < players; i++) {
                prizeStructTableView.getItems().add(i, data.get(i));
                totalPrizes += data.get(i).getMoney();
                totalPercentages += data.get(i).getAbsPercentage();
            }
            totalPercentagesLabel.setText(totalPercentages + "%");
            totalPrizesLabel.setText(totalPrizes + "€");  
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
}
