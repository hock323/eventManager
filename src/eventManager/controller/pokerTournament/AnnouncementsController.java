package eventManager.controller.pokerTournament;

import eventManager.controller.VisorLocation;
import eventManager.fx.EditingBooleanCell;
import eventManager.fx.EditingStringAnnouncementCell;
import eventManager.model.pokerTournament.Announcement;
import eventManager.model.pokerTournament.Tournament;
import eventManager.net.TournamentDataView;
import eventManager.per.ObjectDeserializer;
import eventManager.per.ObjectSerializer;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class AnnouncementsController implements Initializable {

    private SimpleBooleanProperty tournamentChanged;
    private String imageSource = "/eventManager/fx/icons/";
    private MenuItem defaultAnnouncement;
    private MenuItem deleteAnnouncement;
    private Tournament tournament;
    private ResourceBundle resourceBundle;
    private ObservableList<Announcement> announcementObservableList = FXCollections.observableList(new ArrayList<Announcement>());;
    private ObservableList<VisorLocation> visorList;
    @FXML Button soundGeneralButton;
    @FXML ListView<Announcement> announcementList;
    @FXML TableView<Announcement> announcementTableView;
    @FXML TableColumn<Announcement, String> announcement;
    @FXML TableColumn<Announcement, Boolean> activeTableColumn;
    @FXML TableColumn<Announcement, Boolean> highlightTableColumn;
    @FXML TextField noticeTextField;
    @FXML TextField alert1Field;
    @FXML TextField alert2Field;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        Image image = new Image(getClass().getResourceAsStream(imageSource + "icon_sound.png"));
        soundGeneralButton.setGraphic(new ImageView(image));
        chargeAnnouncementStruct();
        setContextMenuInAnnouncementTableView();
        chargeSavedAnnouncementList();
        setContextMenuInListView();
        setAnnouncementListMouseEvent();
    }
    
    public void bind() {
        announcementTableView.getItems().setAll(tournament.getAnnouncementList());
    } 

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public SimpleBooleanProperty getTournamentChanged() {
        return tournamentChanged;
    }

    public void setTournamentChanged(SimpleBooleanProperty tournamentChanged) {
        this.tournamentChanged = tournamentChanged;
    }

    public void setVisorList(ObservableList<VisorLocation> visorList) {
        this.visorList = visorList;
    }

    public ObservableList<VisorLocation> getVisorList() {
        return visorList;
    }
    
    public void soundAllowCommand(ActionEvent e) {
        Button button = (Button) e.getSource();
        Image soundIcon = new Image(getClass().getResourceAsStream(imageSource + "icon_sound.png"));
        Image silenceIcon = new Image(getClass().getResourceAsStream(imageSource + "icon_silence.png"));
        if (button.getText().contains("true")) {
            tournament.setSoundAllowed(false);
            button.setText("b1false");
            button.setGraphic(new ImageView(silenceIcon));
        } else {
            button.setText("b1true");
            button.setGraphic(new ImageView(soundIcon));
            if ("true".equals(soundGeneralButton.getText())) {
                tournament.setSoundAllowed(true);
            } else {
                tournament.setSoundAllowed(false);
            }
        }
        tournamentChanged.set(true);
    }

    public void chargeSavedAnnouncementList() {
        File dir = new File(ObjectDeserializer.DIR + ObjectDeserializer.ANNOUNCEMENT_DIR);
        String[] ficheros = dir.getAbsoluteFile().list();
        Announcement announcement;
        for (int x = 0; x < ficheros.length; x++) {
            if (!ficheros[x].startsWith(".") && ficheros[x].endsWith(".xml")) {
                announcement = (Announcement) ObjectDeserializer.loadObject(ObjectDeserializer.ANNOUNCEMENT_OBJECT, ficheros[x]);
                announcementObservableList.add(announcement);
            }
        }
        announcementList.cellFactoryProperty();
        announcementList.setItems(announcementObservableList);
        announcementList.setCellFactory(new Callback<ListView<Announcement>, ListCell<Announcement>>() {
            @Override
            public ListCell<Announcement> call(ListView<Announcement> p) {
                return new ComposeDefinedAnnouncementCell();
            }
        });
    }

    private void setAnnouncementListMouseEvent() {
        announcementList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() > 1) {
                    if (tournament != null) {
                        tournament.getAnnouncementList().add(announcementList.getSelectionModel().getSelectedItem());
                        TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                        announcementTableView.getItems().setAll(tournament.getAnnouncementList());
                        tournamentChanged.set(true);
                    }
                }
            }
        });
    }
    
    private void setContextMenuInListView(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final ContextMenu announcementTableMenu = new ContextMenu();
                deleteAnnouncement = new MenuItem(resourceBundle.getString("deleteAnnouncement.text"));
                announcementTableMenu.getItems().add(deleteAnnouncement);
                deleteAnnouncement.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        removeAnnouncement(e);
                    }
                });
                announcementList.setContextMenu(announcementTableMenu);
            }
        });
    }
    
    public void chargeAnnouncementStruct() {
        announcementTableView.setEditable(true);
        announcement.setCellValueFactory(new PropertyValueFactory<Announcement, String>("announcement"));
        highlightTableColumn.setCellValueFactory(new PropertyValueFactory<Announcement, Boolean>("highlight"));
        activeTableColumn.setCellValueFactory(new PropertyValueFactory<Announcement, Boolean>("active"));
        Callback<TableColumn<Announcement, Boolean>, TableCell<Announcement, Boolean>> cellBooleanFactory =
                new Callback<TableColumn<Announcement, Boolean>, TableCell<Announcement, Boolean>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingBooleanCell();
                    }
                };
        Callback<TableColumn<Announcement, String>, TableCell<Announcement, String>> cellStringFactory =
                new Callback<TableColumn<Announcement, String>, TableCell<Announcement, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingStringAnnouncementCell();
                    }
                };
        highlightTableColumn.setCellFactory(cellBooleanFactory);
        activeTableColumn.setCellFactory(cellBooleanFactory);
        announcement.setCellFactory(cellStringFactory);
        announcement.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Announcement, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Announcement, String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setAnnouncement(t.getNewValue());
                TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                tournamentChanged.set(true);
            }
        });
        highlightTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Announcement, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Announcement, Boolean> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setHighlight(t.getNewValue());
                TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                tournamentChanged.set(true);
            }
        });
        activeTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Announcement, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Announcement, Boolean> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setActive(t.getNewValue());
                TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(), new TournamentDataView(tournament));
                tournamentChanged.set(true);
            }
        });
        
    }
    
    private void setContextMenuInAnnouncementTableView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final ContextMenu announcementTableMenu = new ContextMenu();
                defaultAnnouncement = new MenuItem(resourceBundle.getString("saveAnnouncement.text"));
                announcementTableMenu.getItems().add(defaultAnnouncement);
                defaultAnnouncement.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        setAnnouncementDefault(null);
                    }
                });
                announcementTableView.setContextMenu(announcementTableMenu);
            }
        });
    }
    
    public void publishNoticeCommand(ActionEvent e) {
        if (tournament != null) {
            if (!"".equals(noticeTextField.getText())) {
                tournament.addNotice(noticeTextField.getText());
                TournamentVisorController.refreshTournamentDataView(getVisorList(), tournament.getID(),new TournamentDataView(tournament));
                announcementTableView.getItems().setAll(tournament.getAnnouncementList());
                noticeTextField.setText("");
            }
            tournamentChanged.set(true);
        }
    }

    public void setAnnouncementDefault(ActionEvent e) {
        String s = announcementTableView.getSelectionModel().getSelectedItem().getAnnouncement().substring(0, 10);
        ObjectSerializer.saveObject(announcementTableView.getSelectionModel().getSelectedItem(), s, false);
        announcementObservableList.add(announcementTableView.getSelectionModel().getSelectedItem());
        announcementList.setItems(announcementObservableList);
    }

    public void removeAnnouncement(ActionEvent e) {
        if (announcementList.getSelectionModel().getSelectedIndex() >= 0) {
            File dir = new File(ObjectDeserializer.DIR + ObjectDeserializer.ANNOUNCEMENT_DIR);
            String[] ficheros = dir.getAbsoluteFile().list();
            String announcement = announcementList.getSelectionModel().getSelectedItem().getAnnouncement().substring(0, 10) + ".xml";
            for (int x = 0; x < ficheros.length; x++) {
                if (ficheros[x].equals(announcement)) {
                    File fichero = new File(ObjectDeserializer.DIR + ObjectDeserializer.ANNOUNCEMENT_DIR + ObjectDeserializer.SEPARATOR + ficheros[x]);
                    fichero.delete();
                    announcementList.getItems().remove(announcementList.getSelectionModel().getSelectedItem());
                    if (announcementList.getItems().size() > 0) {
                        announcementList.getSelectionModel().selectFirst();
                    }
                    break;
                }
            }
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

    private static class ComposeDefinedAnnouncementCell extends ListCell<Announcement> {
        @Override
        public void updateItem(Announcement item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(getItem().getAnnouncement());
                setGraphic(null);
            }
        }
    }
}
