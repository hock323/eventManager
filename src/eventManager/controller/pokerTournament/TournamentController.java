package eventManager.controller.pokerTournament;

import eventManager.controller.VisorLocation;
import eventManager.fx.ModalDialog;
import eventManager.fx.OverrideDialog;
import eventManager.model.pokerTournament.Action;
import eventManager.model.pokerTournament.Summary;
import eventManager.model.pokerTournament.Tournament;
import eventManager.net.TournamentDataView;
import eventManager.per.ObjectDeserializer;
import eventManager.per.ObjectSerializer;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.HttpHostConnectException;

public class TournamentController implements Initializable {

    private SimpleBooleanProperty createMode = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty checkStep1 = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty checkStep2 = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty checkStep3 = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty checkStep4 = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty checkStep5 = new SimpleBooleanProperty(false); 
    private SimpleBooleanProperty tournamentChanged = new SimpleBooleanProperty(false);
    private String imageSource = "/eventManager/fx/icons/";
    private FormatController formatController;
    private BlindsController blindsController;
    private AnnouncementsController announcementsController;
    private PrizesController prizesController;
    private NumberFormat numberFormat = new NumberFormat() {
        private static final long serialVersionUID = 1L;
        @Override
        public StringBuffer format(double d, StringBuffer sb, FieldPosition fp) {
            StringBuffer s = new StringBuffer();
            s.append(Double.toString(d));
            return s;
        }
        @Override
        public StringBuffer format(long l, StringBuffer sb, FieldPosition fp) {
            StringBuffer s = new StringBuffer();
            s.append(Long.toString(l));
            return s;
        }
        @Override
        public Number parse(String string, ParsePosition pp) {
            try {
                int x = Integer.parseInt(string);
                if (x >= 0) {
                    return x;
                } else {
                    return 0;
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    };
    @FXML StackPane primePanel;
    @FXML Pane configPane;
    @FXML StackPane controlPane;
    @FXML HBox createHelpPane;
    @FXML Pane step1;
    @FXML Pane step2;
    @FXML Pane step3;
    @FXML Pane step4;
    @FXML Pane step5;
    @FXML VBox registeredPane;
    @FXML VBox reentriesPane;
    @FXML VBox rebuysPane;
    @FXML VBox addonsPane;
    @FXML VBox playersPane;
    @FXML VBox levelPane;
    @FXML HBox registeredControlsPane;
    @FXML HBox rebuysControlsPane;
    @FXML HBox addonsControlsPane;
    @FXML StackPane totalsPane;
    @FXML ImageView step1Image;
    @FXML ImageView step2Image;
    @FXML ImageView step3Image;
    @FXML ImageView step5Image;
    @FXML Label registeredPlayersLabel;
    @FXML Label livePlayersLabel;
    @FXML Label numRebuysLabel;
    @FXML Label numReentradasLabel;
    @FXML Label numAddOnLabel;
    @FXML Label numLevelLabel;
    @FXML Label receiptsLabel;
    @FXML Label handOutLabel;
    @FXML Label bountiesLabel;
    @FXML Label prizesLabel;
    @FXML Label feeLabel;
    @FXML Label orgExtFeeLabel;
    @FXML Label leagueJackPotLabel;
    @FXML Label timeLabel;   
    @FXML Label breakLabel;
    @FXML TextField nameField;
    @FXML TextField subNameField;
    @FXML TextField addAddonField;
    @FXML TextField addRebuyField;    
    @FXML TextField playerAddField;
    @FXML ComboBox<String> saveTemplateBox;
    @FXML ListView<Action> actionsListView;
    @FXML Button addPlayerButton;
    @FXML Button addAddOnButton;
    @FXML Button addReentryButton;
    @FXML Button addRebuyButton;
    @FXML Button upLevelButton;
    @FXML Button playButton;
    @FXML Button pauseButton;
    @FXML Button eliminatePlayerButton;
    @FXML Button undoButton;
    @FXML Button initTournamentButton;
    @FXML Button finalizeTournamentButton;
    @FXML TabPane tabPane;
    @FXML TableView<VisorLocation> visorTableView;
    @FXML TableColumn<VisorLocation, String> visorID;
    @FXML TableColumn<VisorLocation, String> visorOwner;    
    private MenuItem refreshVisorList;
    private ResourceBundle resourceBundle;
    private Tournament tournament = null;
    private ObservableList<VisorLocation> visorList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        loadTabs();
        Image image = new Image(getClass().getResourceAsStream(imageSource + "play.png"));
        playButton.setGraphic(new ImageView(image));
        image = new Image(getClass().getResourceAsStream(imageSource + "undo.png"));
        undoButton.setGraphic(new ImageView(image));
        image = new Image(getClass().getResourceAsStream(imageSource + "forward.png"));
        upLevelButton.setGraphic(new ImageView(image));
        checkStep1.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    step1Image.setOpacity(1);
                } else {
                    step1Image.setOpacity(0.3);
                }
            }
        });
        checkStep2.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    step2Image.setOpacity(1);
                } else {
                    step2Image.setOpacity(0.3);
                }
            }
        });
        checkStep3.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    step3Image.setOpacity(1);
                } else {
                    step3Image.setOpacity(0.3);
                }
            }
        });
        checkStep4.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1 && checkStep5.get()) {
                    step5Image.setOpacity(1);
                } else {
                    step5Image.setOpacity(0.3);
                }
            }
        });
        checkStep5.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1 && checkStep4.get()) {
                    step5Image.setOpacity(1);
                } else {
                    step5Image.setOpacity(0.3);
                }
            }
        });
        actionsListView.cellFactoryProperty();
        actionsListView.setCellFactory(new Callback<ListView<Action>, ListCell<Action>>() {
            @Override
            public ListCell<Action> call(ListView<Action> list) {
                return new ComposeActionHistoryCell();
            }
        });
        actionsListView.setOnScrollStarted(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent t) {
                actionsListView.scrollTo(0);
            }
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                step1.setStyle("-fx-background-color: #2D86EE;");
                step2.setStyle("-fx-background-color: #2D86EE;");
                step3.setStyle("-fx-background-color: #2D86EE;");
                step4.setStyle("-fx-background-color: #2D86EE;");
                step5.setStyle("-fx-background-color: #2D86EE;");
                switch (t1.getText()) {
                    case "Formato":
                        step1.setStyle("-fx-background-color: #008D17;");
                        break;
                    case "Ciegas":
                        step2.setStyle("-fx-background-color: #008D17;");
                        break;
                    case "Premios":
                        step3.setStyle("-fx-background-color: #008D17;");
                        break;
                    case "Anuncios":
                        step4.setStyle("-fx-background-color: #008D17;");
                        break;
                    case "Estado":
                        step5.setStyle("-fx-background-color: #008D17;");
                        break;
                }
            }
        });
        playerAddField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    addPlayerCommand(null);
                }
            }
        });
        addRebuyField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    addRebuyCommand(null);
                }
            }
        });
        addAddonField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    addAddOnCommand(null);
                }
            }
        });
        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!"".equals(t1)) {
                    ObjectSerializer.renameTournament(t, t1);
                    checkStep4.set(true);
                } else {
                    checkStep4.set(false);
                }
            }
        });
        chargeSaveTemplateBox();
    }

    
    private void loadTabs(){
        try {
            URL location = getClass().getResource("/eventManager/fx/FormatTab.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                StackPane pane = (StackPane) fxmlLoader.load();
                formatController = fxmlLoader.getController();
                tabPane.getTabs().get(0).setContent(pane);
                
                location = getClass().getResource("/eventManager/fx/BlindsTab.fxml");
                fxmlLoader = new FXMLLoader(location, resourceBundle);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                pane = (StackPane) fxmlLoader.load();
                blindsController = fxmlLoader.getController();
                tabPane.getTabs().get(1).setContent(pane);
                
                location = getClass().getResource("/eventManager/fx/PrizeTab.fxml");
                fxmlLoader = new FXMLLoader(location, resourceBundle);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                pane = (StackPane) fxmlLoader.load();
                prizesController = fxmlLoader.getController();
                tabPane.getTabs().get(2).setContent(pane);
                
                location = getClass().getResource("/eventManager/fx/AnnouncementTab.fxml");
                fxmlLoader = new FXMLLoader(location, resourceBundle);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                pane = (StackPane) fxmlLoader.load();
                announcementsController = fxmlLoader.getController();
                tabPane.getTabs().get(3).setContent(pane);
        } catch (IOException ex) {
            Logger.getLogger(TournamentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setLoadedTournament(){
        tabPane.getSelectionModel().select(4);
    }
    
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
        bind();
        formatController.setTournament(tournament);
        prizesController.setTournament(tournament);
        blindsController.setTournament(tournament);
        announcementsController.setTournament(tournament);
        formatController.loadingView();
        formatController.setTournamentChanged(tournamentChanged);
        prizesController.setTournamentChanged(tournamentChanged);
        blindsController.setTournamentChanged(tournamentChanged);
        announcementsController.setTournamentChanged(tournamentChanged);
        formatController.setCreateMode(createMode);
        blindsController.setCreateMode(createMode);
        prizesController.setCreateMode(createMode);
        formatController.bind();
        blindsController.bind();
        prizesController.bind();
        announcementsController.bind();
        checkToInit();
    }
    
    public Tournament getTournament() {
        return tournament;
    }

    public ObservableList<VisorLocation> getVisorList() {
        return visorList;
    }

    public void setVisorList(ObservableList<VisorLocation> visorList) {
        this.visorList = visorList;
        announcementsController.setVisorList(visorList);
        prizesController.setVisorList(visorList);
        blindsController.setVisorList(visorList);
    }

    public boolean isTournamentChanged() {
        return tournamentChanged.get();
    }
    
    public TextField getNameField() {
        return nameField;
    }
    
    public SimpleBooleanProperty tournamentChangedProperty() {
        return tournamentChanged;
    }
    
    public void bind(){
        numLevelLabel.textProperty().bind(tournament.getLevelInProgress().levelStringProperty());
        livePlayersLabel.setText(tournament.getPlayerList().size() + "");
        numRebuysLabel.textProperty().bindBidirectional(tournament.getSummary().rebuysProperty(), numberFormat);
        numAddOnLabel.textProperty().bindBidirectional(tournament.getSummary().addonsProperty(), numberFormat);
        actionsListView.setItems(tournament.getActionsHistory());
        nameField.textProperty().bindBidirectional(tournament.tournamentNameProperty());
        subNameField.textProperty().bindBidirectional(tournament.tournamentSubNameProperty());
        registeredPlayersLabel.textProperty().bindBidirectional(tournament.getSummary().registeredPlayersProperty(), numberFormat);
        timeLabel.textProperty().bind(tournament.getLevelTimeService().messageProperty());
        setChangeLevelListener();
        breakLabel.visibleProperty().bind(tournament.breakTimeProperty());
        breakLabel.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1 == true && "ED_BREAK".equals(tournament.getTournamentStateString())) {
                    pauseButton.setVisible(false);
                    playButton.setVisible(true);
                }
            }
        });
        nameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1 == false && checkStep4.get()) {
                    for (VisorLocation v : getVisorList()) {
                        if (v.getOwnerID() == tournament.getID()) {
                            try {
                                v.setOwner(nameField.getText());
                                TournamentVisorController.lockVisor(v, tournament.getID(), tournament.getTournamentName());
                            } catch (IOException ex) {
                                if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                                    TournamentVisorController.poll(getVisorList());
                                }
                                //nameField.setText(t);
                            }
                        }
                    }
                }
            }
        });
        formatController.formatCheckProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                checkStep1.set(t1);
            }
        });
        blindsController.blindSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                checkStep2.set(t1);
            }
        });
        prizesController.prizeSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                checkStep3.set(t1);
            }
        });
    }
    
    private void setChangeLevelListener(){
        tournament.getLevelInProgress().levelProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                if (tournament.isLastLevel()) {
                    upLevelButton.setDisable(true);
                }
                if (tournament.getTournamentConfiguration().getRebuy() != null
                        && tournament.getLevelInProgress().getLevel()
                        > tournament.getTournamentConfiguration().getRebuy().getMaxLevel()) {
                    addRebuyButton.setDisable(true);
                }
                if (tournament.getTournamentConfiguration().getBuyin().getMaxLevelBuyin() != 0
                        && tournament.getLevelInProgress().getLevel()
                        > tournament.getTournamentConfiguration().getBuyin().getMaxLevelBuyin()) {
                    addPlayerButton.setDisable(true);
                }
                if (tournament.getTournamentConfiguration().getReentrada() != null
                        && tournament.getLevelInProgress().getLevel()
                        > tournament.getTournamentConfiguration().getReentrada().getMaxLevel()) {
                    addReentryButton.setDisable(true);
                }
                blindsController.getBlindStructTableView().getSelectionModel().select(tournament.getLevelInProgress().getLevel()-1);
                blindsController.getBlindStructTableView().scrollTo(tournament.getLevelInProgress().getLevel());
            }
        });
    }
    
    @FXML public void addPlayerCommand(ActionEvent e) {
        if (tournament != null) {
            if (!"".equals(playerAddField.getText())) {
                try {
                    int n = Integer.parseInt(playerAddField.getText());
                    if (n > 0) 
                        tournament.addPlayer(false, n);
                }catch (NumberFormatException ex){
                    new ModalDialog(primePanel.getScene().getWindow(), "Incorrect Amount","Please, revise the parameter").show();
                    playerAddField.setText("");            
                }
            }
            else tournament.addPlayer(false, 1); 
            livePlayersLabel.setText(tournament.getPlayerList().size() + "");
            playerAddField.setText("");
            refreshSummary();
            eliminatePlayerButton.setDisable(false);
            tournamentChanged.set(true);
        }
    }

    @FXML public void eliminatePlayerCommand(ActionEvent e) throws IOException {
        if (tournament != null) {
            tournament.substractPlayer(false);
            livePlayersLabel.setText(tournament.getPlayerList().size() + "");
            refreshSummary();
            Summary s = tournament.getSummary();
            if (tournament.getTournamentConfiguration().getReentrada() != null
                    && tournament.getPlayerList().size() < s.getRegisteredPlayers()) {
                addReentryButton.setDisable(false);
            } else {
                addReentryButton.setDisable(true);
            }
            if (tournament.getPlayerList().size() == 1) {
                eliminatePlayerButton.setDisable(true);
                eliminatePlayerButton.setVisible(false);
            }
            tournamentChanged.set(true);
        }
    }
    
    @FXML public void addRebuyCommand(ActionEvent e) {
        if (tournament != null) {
            if (!"".equals(addRebuyField.getText())) {
                try {
                    int n = Integer.parseInt(addRebuyField.getText());
                    if (n > 0 && n <= tournament.getPlayerList().size()) 
                        tournament.addRebuy(0, n);    
                    else throw new NumberFormatException();
                    addRebuyField.setText("");   
                }catch (NumberFormatException ex){
                    new ModalDialog(primePanel.getScene().getWindow(), "Incorrect Amount","Please, revise the parameter").show();      
                    addRebuyField.setText("");
                }
            }
            else tournament.addRebuy(0, 1);
            numRebuysLabel.setText(tournament.getSummary().getRebuys() + "");
            refreshSummary();
            tournamentChanged.set(true);
        }
    }

    @FXML public void addReentradaCommand(ActionEvent e) {
        if (tournament != null) {
            tournament.addReentrada(0);
            tournament.getPlayerList().addPlayer();
            tournament.getSummary().setEliminatedPlayers(tournament.getSummary().getEliminatedPlayers()-1);
            livePlayersLabel.setText(tournament.getPlayerList().size() + "");
            int maxLevel = tournament.getTournamentConfiguration().getReentrada().getMaxLevel();
            if (tournament.getLevelInProgress().getLevel() > maxLevel
                    || tournament.getPlayerList().size() >= tournament.getSummary().getRegisteredPlayers()) {
                addReentryButton.setDisable(true);
                addReentryButton.setVisible(false);
            }
            refreshSummary();
            numReentradasLabel.setText(tournament.getSummary().getReentradas() + ""); 
            tournamentChanged.set(true);
        }
    }

    @FXML public void addAddOnCommand(ActionEvent e) {
        try {
            if (tournament != null) {
                if (!"".equals(addAddonField.getText())) {
                    int n = Integer.parseInt(addAddonField.getText());
                    if (n <= tournament.getPlayerList().size())
                        tournament.addAddon(0, n);
                    else throw new NumberFormatException();
                }
                else
                    tournament.addAddon(0, 1);
                refreshSummary();
                numAddOnLabel.setText(tournament.getSummary().getAddons() + "");
                tournamentChanged.set(true);
            }
        } catch (NumberFormatException ex) {
            new ModalDialog(primePanel.getScene().getWindow(), "Incorrect Amount","Please, revise the parameter").show();
        }
    }
    
    @FXML public void playPauseCommand(ActionEvent e) {
        if (tournament != null) {
            switch (tournament.getTournamentStateString()) {
                case "PLAYING":
                    try {
                        TournamentVisorController.pauseAction(getVisorList(), tournament.getID(), tournament.getLevelTimeService().getLevelTime());
                        playButton.setVisible(true);
                        playButton.toFront();
                        pauseButton.setVisible(false);
                        tournament.pauseLevel();
                        break;
                    } catch (IOException ex) {
                        if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                            TournamentVisorController.poll(getVisorList());
                            playButton.setVisible(true);
                            playButton.toFront();
                            pauseButton.setVisible(false);
                            tournament.pauseLevel();
                            System.out.println("Pause time: " + ex);
                        }
                        break;
                    }
                case "PAUSED":
                case "ED_BREAK":
                    try {
                        TournamentVisorController.playAction(getVisorList(), tournament.getID(), tournament.getLevelTimeService().getLevelTime());
                        pauseButton.setVisible(true);
                        playButton.setVisible(false);
                        tournament.resumeLevel();
                        break;
                    } catch (IOException ex) {
                        System.out.println("Play time: " +ex);
                        break;
                    }
                case "CR_BREAK":
                case "DB_BREAK":
                case "BREAK":
                    try {
                        TournamentVisorController.pauseAction(getVisorList(), tournament.getID(), tournament.getLevelTimeService().getLevelTime());
                        playButton.setVisible(true);
                        pauseButton.setVisible(false);
                        tournament.pauseBreak();
                        break;
                    } catch (IOException ex) {
                        if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                            TournamentVisorController.poll(getVisorList());
                            playButton.setVisible(true);
                            playButton.toFront();
                            pauseButton.setVisible(false);
                            tournament.pauseLevel();
                        }
                        System.out.println("Pause ERROR: " + ex);
                        break;
                    }
                case "BREAK_PAUSED":
                    try {
                        TournamentVisorController.playAction(getVisorList(), tournament.getID(), tournament.getLevelTimeService().getLevelTime());
                        pauseButton.setVisible(true);
                        playButton.setVisible(false);
                        tournament.startBreakTime();
                        break;
                    } catch (IOException ex) {
                        System.out.println("Play ERROR: " +ex);
                        break;
                    }
                case "STOPPED":
                    try {
                        TournamentVisorController.playAction(getVisorList(), tournament.getID(), tournament.getLevelTimeService().getLevelTime());
                        pauseButton.setVisible(true);
                        playButton.setVisible(false);
                        pauseButton.toFront();
                        tournament.startLevel();
                        break;
                    } catch (IOException ex) {
                        System.out.println("Play ERROR: " +ex);
                        break;
                    }
                    
            }
        }
    }

    @FXML public void stopCommand(MouseEvent e) {
        if (tournament != null) {
            tournament.stopLevel();
        }
    }

    @FXML public void upLevelCommand(ActionEvent e) {
        if (tournament != null) {
            String aux = "";
            try {
                if (tournament.getTournamentStateString().contains("BREAK")) {
                    if (tournament.getTournamentStateString().contains("PAUSED")) {
                        aux = "PAUSED";
                        tournament.setTournamentState("PAUSED");
                    } else {
                        aux = "PLAYING";
                        tournament.setTournamentState("PLAYING");
                    }
                }
                TournamentVisorController.setLevel(getVisorList(), tournament.getID(), tournament.getLevelInProgress().getLevel() + 1);
                tournament.levelUp();
                tournamentChanged.set(true);
            } catch (IOException ex) {
                if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                    TournamentVisorController.poll(getVisorList());
                }
                Logger.getLogger(TournamentController.class.getName()).log(Level.SEVERE, null, ex);
                if ("PLAYING".equals(aux)) {
                    tournament.setTournamentState("BREAK");
                } else {
                    if ("PAUSED".equals(aux)) {
                        tournament.setTournamentState("BREAK_PAUSED");
                    }
                }
            }
        }
    }
    
    @FXML public void downLevelCommand() {
        try {
            TournamentVisorController.setLevel(getVisorList(), tournament.getID(), tournament.getLevelInProgress().getLevel() - 1);
            tournament.levelDown();
            if (tournament.getTournamentConfiguration().getRebuy() != null
                    && tournament.getLevelInProgress().getLevel()
                    <= tournament.getTournamentConfiguration().getRebuy().getMaxLevel()) {
                addRebuyButton.setDisable(false);
            }
            if (tournament.getTournamentConfiguration().getBuyin().getMaxLevelBuyin() != null
                    && tournament.getLevelInProgress().getLevel()
                    <= tournament.getTournamentConfiguration().getBuyin().getMaxLevelBuyin()) {
                addPlayerButton.setDisable(false);
            }
            if (tournament.getTournamentConfiguration().getReentrada() != null
                    && tournament.getLevelInProgress().getLevel()
                    <= tournament.getTournamentConfiguration().getReentrada().getMaxLevel()) {
                addReentryButton.setDisable(false);
            }
            tournamentChanged.set(true);
        } catch (IOException ex) {
            if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                TournamentVisorController.poll(getVisorList());
            }
            Logger.getLogger(TournamentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML public void undoCommand(ActionEvent e) {
     String action = actionsListView.getItems().get(0).getAction();
     if (action.contains("ADDON")){
         undoAddOnAdded();
     }
     if (action.contains("REBUY")){
         undoRebuyAdded();
     }
     if (action.contains("RE-ENTRY")){
         undoReentryAdded();
     }
     if (action.contains("PLAYER") && action.contains("ADDED")){
         undoPlayerAdded();
     }
     if (action.contains("PLAYER") && action.contains("ELIMINATED")){
         undoEliminatePlayer();
     }
     if (action.contains("LEVEL")){
         downLevelCommand();
         return;
     }
     refreshSummary();
    }
    
    @FXML public void fieldClicked(Event e){
        TextField field = (TextField) e.getSource();
        if (!field.isDisable())
            field.setText(""); 
    }
    
    @FXML public void stepSelected(MouseEvent e) {
        Pane pane = (Pane) e.getSource();
        switch (pane.getId()) {
            case "step1":
                tabPane.getSelectionModel().select(0);
                break;
            case "step2":
                tabPane.getSelectionModel().select(1);
                break;
            case "step3":
                tabPane.getSelectionModel().select(2);
                break;
            case "step4":
                tabPane.getSelectionModel().select(3);
                break;
            case "step5":
                tabPane.getSelectionModel().select(4);
                break;
        }
        step1.setStyle("-fx-background-color: #2D86EE;");
        step2.setStyle("-fx-background-color: #2D86EE;");
        step3.setStyle("-fx-background-color: #2D86EE;");
        step4.setStyle("-fx-background-color: #2D86EE;");
        step5.setStyle("-fx-background-color: #2D86EE;");
        pane.setStyle("-fx-background-color: #008D17;");
    }
    
    public void showHideCommand(ActionEvent e) {
        Button button = (Button) e.getSource();
        if (button.getText().contains("1")) {
            TournamentVisorController.showHideVisor(getVisorList(), tournament.getID(), new String[1]);
        }
    }
    
    private void chargeSaveTemplateBox() {
        File dir = new File(ObjectDeserializer.DIR + ObjectDeserializer.TOURNAMENT_CONFIGURATION_DIR);
        String[] ficheros = dir.getAbsoluteFile().list();
        ObservableList<String> list = FXCollections.observableList(new ArrayList<String>());
        for (int x = 0; x < ficheros.length; x++) {
            if (!ficheros[x].startsWith(".") && ficheros[x].endsWith(".xml")) {
                list.add(ficheros[x].substring(0, ficheros[x].lastIndexOf(".")));
            }
        }
        saveTemplateBox.setItems(list);
    }
    
    public void saveTournamentConfiguration(ActionEvent e) {
        final String name = saveTemplateBox.getValue();
        if (!"".equals(name)) {
            if (saveTemplateBox.getItems().indexOf(name) >= 0) {
                SimpleBooleanProperty answer = new SimpleBooleanProperty(false);
                OverrideDialog o = new OverrideDialog(saveTemplateBox.getScene().getWindow(), name + 
                        resourceBundle.getString("saveQuestion1.text"), resourceBundle.getString("saveQuestion2.text"), answer, resourceBundle);
                answer.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                        if (t1) {
                            ObjectSerializer.saveObject(tournament.getTournamentConfiguration(), name, false);
                            chargeSaveTemplateBox();
                        }
                    }
                });
            } else {
                ObjectSerializer.saveObject(tournament.getTournamentConfiguration(), name, false);
                chargeSaveTemplateBox();
            }
        }
    }
    
    public void saveTournamentCommand(ActionEvent e) {
        if (tournament != null) {
            new Thread() {
                @Override
                public void run() {
                    ObjectSerializer.saveObject(tournament, tournament.getTournamentName(), false);
                    tournamentChanged.set(false);
                }
            };
        }
    }
    
    public void chargeVisorTableView() {
        visorTableView.setEditable(false);
        visorID.setCellValueFactory(new PropertyValueFactory<VisorLocation, String>("ID"));
        visorOwner.setCellValueFactory(new PropertyValueFactory<VisorLocation, String>("owner"));
        visorTableView.setItems(getVisorList());
        visorTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() > 1 && visorTableView.getSelectionModel().getSelectedItem() != null) {
                    lockUnLockVisor(visorTableView.getSelectionModel().getSelectedItem());
                }
            }
        });
        final ContextMenu visorTableMenu = new ContextMenu();
        refreshVisorList = new MenuItem("Refresh");
        visorTableMenu.getItems().add(refreshVisorList);
        refreshVisorList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                TournamentVisorController.poll(getVisorList());
                visorTableView.setItems(getVisorList());                
            }
        });
        visorTableView.setContextMenu(visorTableMenu);
    }

    private boolean lockUnLockVisor(VisorLocation visor) {
        if (!visor.isBusy()) {
            try {
                TournamentVisorController.lockVisor(visor, tournament.getID(), tournament.getTournamentName());
                visor.setOwner(tournament.getTournamentName());
                visor.setOwnerID(tournament.getID());
                visor.setBusy(true);
                checkStep5.set(true);
                if (!createMode.get()) {
                    TournamentVisorController.refreshTournamentDataView(getVisorList(),
                tournament.getID(), new TournamentDataView(tournament));
                }
            } catch (IOException ex) {
                if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                    TournamentVisorController.poll(getVisorList());
                }
                   System.out.println("LOCK VISOR ERROR: " + ex.getCause().toString());
                return false;
            }
        } else {
            unlockVisor(visor);
        }
        return true;
    }

    private void unlockVisor(VisorLocation visor) {
        if (visor.getOwnerID() == tournament.getID()) {
                try {
                    TournamentVisorController.unlockVisor(visor);
                    visor.setOwner("Free");
                    visor.setBusy(false);
                    visor.setOwnerID(0);
                    boolean check = false;
                    for (VisorLocation v : getVisorList()) {
                        if (v.getID().equals(tournament.getID() + "")) {
                            check = true;
                        }
                    }
                    if (!check) {
                        if (tournament.getLevelTimeService().isRunning()) {
                            playPauseCommand(null);
                        }
                    }
                    checkStep5.set(check);
                } catch (IOException ex) {
                    if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                        TournamentVisorController.poll(getVisorList());
                    }
                    System.out.println("UNLOCK VISOR ERROR: " + ex.getCause().toString());
                }
            }
    }
    
    public void undoEliminatePlayer() {
        tournament.addPlayer(true, 1);
        livePlayersLabel.setText(tournament.getPlayerList().size() + "");
        eliminatePlayerButton.setDisable(false);
    }

    public void undoAddOnAdded() {
        tournament.subtractAddon(0);
        addAddOnButton.setDisable(false);
    }

    public void undoReentryAdded() {
        tournament.subtractReentrada(0);
        tournament.getPlayerList().substractPlayer();
        numReentradasLabel.setText(tournament.getSummary().getReentradas() + "");
        livePlayersLabel.setText(tournament.getPlayerList().size() + "");
        addReentryButton.setDisable(false);
    }

    public void undoRebuyAdded() {
        tournament.subtractRebuy(0);
        numRebuysLabel.setText(tournament.getSummary().getRebuys() + "");
        addRebuyButton.setDisable(false);
    }

    public void undoPlayerAdded() {
        tournament.substractPlayer(true);
        livePlayersLabel.setText(tournament.getPlayerList().size() + "");
    }

    private void refreshSummary() {
        ArrayList<Integer> array = tournament.refreshSummary();
        TournamentVisorController.refreshTournamentDataView(getVisorList(),
                tournament.getID(), new TournamentDataView(tournament));
        receiptsLabel.setText(array.get(4) + "");
        handOutLabel.setText(array.get(5) + "");
        feeLabel.setText(array.get(6) + "");
        orgExtFeeLabel.setText(array.get(7) + "");
        leagueJackPotLabel.setText(array.get(8) + "");
        prizesLabel.setText(array.get(9) + "");
        bountiesLabel.setText(array.get(10) + "");
        prizesController.refreshPrizes(array);
    }   
    
    private void checkToInit(){
        if ("".equals(tournament.getTournamentName())) {
            checkStep4.set(false);
        }
        checkStep4.set(true);
        if (tournament.getTournamentConfiguration().getBuyin().getChips() <= 0
                || tournament.getTournamentConfiguration().getBuyin().getCost() <= 0
                || tournament.getTournamentConfiguration().getFees().getOrganizationFee() <= 0) {
            checkStep1.set(false);
        } else {
            checkStep1.set(true);
        }
        if (tournament.getTournamentConfiguration().getBlindStruct() == null) {
            checkStep2.set(false);
        } else {
            checkStep2.set(true);
        }
        if (tournament.getTournamentConfiguration().getPrizeStruct() == null
                || tournament.getTournamentConfiguration().getPrizeStruct().getPercentagesList().isEmpty()) {
            checkStep3.set(false);
        } else {
            checkStep3.set(true);
        }
        if ((checkStep1.get() && checkStep2.get() && checkStep3.get() && checkStep4.get() && checkStep5.get())) {
            setInitedState();
            createMode.set(false);
        }
    }
    
    public void startTournament(ActionEvent e) {
        if (checkStep1.get() && checkStep2.get() && checkStep3.get() && checkStep4.get() && checkStep5.get()) {
            try {
                TournamentVisorController.refreshTime(getVisorList(), tournament.getID(), tournament.getLevelTimeService().getLevelTime());
                refreshSummary();
                setInitedState();
            } catch (IOException ex) {
                if (ex instanceof SocketTimeoutException) {
                    try {
                        TournamentVisorController.refreshTime(getVisorList(), tournament.getID(), tournament.getLevelTimeService().getLevelTime());
                    } catch (IOException ex1) {
                        Logger.getLogger(TournamentController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                        TournamentVisorController.poll(getVisorList());
                    }
                } else {
                    Logger.getLogger(TournamentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if (!checkStep1.get()) {
                tabPane.getSelectionModel().select(0);
            } else {
                if (!checkStep2.get()) {
                    tabPane.getSelectionModel().select(1);
                } else {
                    if (!checkStep3.get()){
                        tabPane.getSelectionModel().select(2);
                    }
                        
                }
            }
            //System.out.println(checkStep1.get() +" "+ checkStep2.get() +" "+ checkStep3.get() +" "+ checkStep4.get() +" "+ checkStep5.get());
        }
    }
        
    private static class ComposeActionHistoryCell extends ListCell<Action> {
        @Override
        public void updateItem(Action item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(getItem().getAction());
            }
            setGraphic(null);
        }
    }

    public void setInitedState() {
        showControlMode();
        refreshControlView();
        formatController.setInitedState();
        blindsController.setInitedState();
        prizesController.setInitedState();
        initTournamentButton.setVisible(false);
        finalizeTournamentButton.setVisible(true);
        createMode.set(false);
        totalsPane.setVisible(true);
    }
    
    public void refreshControlView() {
        if (tournament.getTournamentConfiguration().getReentrada() == null) {
            reentriesPane.setVisible(false);
        }
        if (tournament.getTournamentConfiguration().getRebuy() == null) {
            rebuysPane.setVisible(false);
        }
        if (tournament.getTournamentConfiguration().getAddon() == null) {
            addonsPane.setVisible(false);
        }    
    }

    public void setFinishedState() {
        for (VisorLocation visor : visorList) {
            if (visor.getOwnerID() == tournament.getID()) {
                try {
                    TournamentVisorController.unlockVisor(visor);
                } catch (IOException ex) {
                    if (ex instanceof NoHttpResponseException | ex instanceof HttpHostConnectException) {
                        TournamentVisorController.poll(getVisorList());
                    }
                    System.out.println("UNLOCK VISOR ERROR: " + ex.getCause().toString());
                }
            }
        }
        controlPane.setVisible(false);
        createMode.set(false);
        finalizeTournamentButton.setVisible(false);
    }

    // Visual efects
    public void showControlMode() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), createHelpPane);
        fadeTransition.setFromValue(1.0f);
        fadeTransition.setToValue(0.0f);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(700), controlPane);
        fadeTransition2.setFromValue(0.0f);
        fadeTransition2.setToValue(1.0f);
        fadeTransition2.setCycleCount(1);
        fadeTransition2.setAutoReverse(false);
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                fadeTransition2);
        controlPane.setVisible(true);
        createHelpPane.toBack();
        parallelTransition.play();
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

    public void controlPaneFadeOn(MouseEvent t) {
        Pane node = (Pane) t.getSource();
        Node fadedNode;
        if (registeredPane.equals(node)) {
            fadedNode = registeredControlsPane;
        } else {
            if (reentriesPane.equals(node)) {
                fadedNode = addReentryButton;
            } else {
                if (rebuysPane.equals(node)) {
                    fadedNode = rebuysControlsPane;
                } else {
                    if (addonsPane.equals(node)) {
                        fadedNode = addonsControlsPane;
                    } else {
                        if (playersPane.equals(node)) {
                            fadedNode = eliminatePlayerButton;
                        } else {
                            fadedNode = upLevelButton;
                        }
                    }
                }
            }
        }
        if (!fadedNode.isDisable()) {
            fadedNode.setVisible(true);
            FadeTransition fade = new FadeTransition(Duration.millis(300), fadedNode);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        }
    }

    public void controlPaneFadeOff(MouseEvent t) {
        Pane node = (Pane) t.getSource();
        Node fadedNode;
        if (node == registeredPane) {
            fadedNode = registeredControlsPane;
        } else {
            if (node == reentriesPane) {
                fadedNode = addReentryButton;
            } else {
                if (node == rebuysPane) {
                    fadedNode = rebuysControlsPane;
                } else {
                    if (node == addonsPane) {
                        fadedNode = addonsControlsPane;
                    } else {
                        if (node == playersPane) {
                            fadedNode = eliminatePlayerButton;
                        } else {
                            fadedNode = upLevelButton;
                        }
                    }
                }
            }
        }
        if (!fadedNode.isDisable()) {
            FadeTransition fade = new FadeTransition(Duration.millis(300), fadedNode);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        }
    }
}