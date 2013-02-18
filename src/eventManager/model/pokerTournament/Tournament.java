package eventManager.model.pokerTournament;

import eventManager.controller.VisorLocation;
import eventManager.controller.pokerTournament.TournamentVisorController;
import eventManager.model.Event;
import eventManager.net.TournamentDataView;
import eventManager.per.ObjectSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({TournamentConfiguration.class, Action.class, PlayerList.class, Level.class,
    Summary.class})
public final class Tournament extends Event {
    private final long ID = (long) (Math.random()*100000000);
    private SimpleStringProperty tournamentName = new SimpleStringProperty("");
    private SimpleStringProperty tournamentSubName = new SimpleStringProperty("");
    private SimpleBooleanProperty breakTime = new SimpleBooleanProperty(false);
    private enum stateType {PLAYING, PAUSED, BREAK, BREAK_PAUSED, INFINITE_BREAK, STOPPED, ED_BREAK, CR_BREAK, DB_BREAK};
    private stateType tournamentState = stateType.STOPPED;
    private TournamentConfiguration tournamentConfiguration;
    private final Level levelInProgress = new Level(0, 0, 0, 0, 0, "0");
    private LevelTimeService levelTimeService;
    private PlayerList playerList = new PlayerList();
    private Summary summary;
    private ObservableList<Action> actionsHistory = FXCollections.observableList(new ArrayList<Action>());
    private ObservableList<Announcement> announcementList = FXCollections.observableList(new ArrayList<Announcement>());
    private boolean anonymousTournament = true;
    private boolean soundAlerted = false;
    private SimpleBooleanProperty soundAllowed = new SimpleBooleanProperty(true);
    private SimpleIntegerProperty transcurredTime = new SimpleIntegerProperty(0);
    

    public Tournament(String name, TournamentConfiguration tc, ExecutorService service) {
        this.tournamentName.set(name);
        this.tournamentConfiguration = tc;
        summary = new Summary(tournamentConfiguration);
        summary.updateRegisteredPlayers(playerList.size());
//        this.visorList = visorLists;
        soundAllowed.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                //TournamentVisorController.refreshTournamentDataView(visorList, ID, new TournamentDataView());
            }
        });
        addTimeListener(service);
    }

    public Tournament() {
        soundAllowed.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                //TournamentVisorController.refreshTournamentDataView(visorList, ID, new TournamentDataView());
            }
        });
        // Al cargar, asignar el tiempo al levelTimeService tb
    }

    public void addTimeListener(ExecutorService executor){
        levelTimeService = new LevelTimeService();
        levelTimeService.setLevelTime(levelInProgress.getTime()*60);
        levelTimeService.setExecutor(executor);
        getLevelTimeService().LevelTimeProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                int n;
                if (t1.intValue() == 0) {
                    if (tournamentState == stateType.PLAYING) {
                        //new AlertSound("temple.wav").start();
                        try {
                            n = Integer.parseInt(getLevelInProgress().getBreather());
                            if (n != 0) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tournamentState = stateType.BREAK;
                                        getLevelTimeService().setLevelTime(Integer.parseInt(levelInProgress.getBreather()) * 60);
                                        setBreakTime(true);
                                    }
                                });
                            } else {
                                levelUp();
                            }
                        } catch (NumberFormatException ex) {
                            if (getLevelInProgress().getBreather().startsWith("CR")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tournamentState = stateType.CR_BREAK;
                                        setBreakTime(true);
                                        int time = Integer.parseInt(getLevelInProgress().getBreather().substring(2));
                                        getLevelTimeService().setLevelTime(time * 60);
                                    }
                                });
                            }
                            if (getLevelInProgress().getBreather().startsWith("ED")) {
                                tournamentState = stateType.ED_BREAK;
                                levelUp();
                                getLevelTimeService().cancel();
                            }
                            if (getLevelInProgress().getBreather().startsWith("DB")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tournamentState = stateType.DB_BREAK;
                                        int time = Integer.parseInt(getLevelInProgress().getBreather().substring(2));
                                        getLevelTimeService().setLevelTime(time * 60);
                                    }
                                });
                            }
                        }
                    } else {
                        tournamentState = stateType.PLAYING;
                        levelUp();
                    }
                } else {
                    if (t1.intValue() % 60 == 4) {
                        /*try {
                            TournamentVisorController.refreshTime(visorList, getID(), getLevelTimeService().getLevelTime());
                        } catch (IOException ex) {
                            if (ex instanceof org.apache.http.conn.ConnectTimeoutException)
                                Logger.getLogger(Tournament.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        }*/
                    }
                    if (t1.intValue() % 300 == 0) {
                        saveTempTournament();
                    }
                }
            }
        });
    }
    
    public void setAnonymousPlayersProperty(boolean anonymousPlayersProperty) {
        this.anonymousTournament = anonymousPlayersProperty;
    }
    
    @XmlAttribute
    public String getTournamentName() {
        return tournamentName.get();
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName.set(tournamentName);
    }

    public long getID() {
        return ID;
    }

    @XmlAttribute
    public String getTournamentSubName() {
        return tournamentSubName.get();
    }

    public void setTournamentSubName(String tournamentSubName) {
        this.tournamentSubName.set(tournamentSubName);
    }

    

    public SimpleStringProperty tournamentSubNameProperty() {
        return tournamentSubName;
    }

    public SimpleStringProperty tournamentNameProperty() {
        return tournamentName;
    }

    @XmlElement
    public String getTournamentStateString() {
        return tournamentState.toString();
    }

    public stateType getTournamentState() {
        return tournamentState;
    }
    
    public boolean isBreakTime() {
        return breakTime.get();
    }

    public void setBreakTime(boolean breakTime) {
        this.breakTime.set(breakTime);
    }

    public SimpleBooleanProperty breakTimeProperty() {
        return breakTime;
    }

    @XmlElement
    public synchronized ObservableList<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void addNotice(String notice) {
        synchronized (announcementList) {
            getAnnouncementList().add(new Announcement(notice, false, false));
        }
    }

    public synchronized LevelTimeService getLevelTimeService() {
        return levelTimeService;
    }

    @XmlAttribute
    public boolean isAnonymousTournament() {
        return anonymousTournament;
    }

    public void setTournamentState(String tournamentState) {
        this.tournamentState = stateType.valueOf(tournamentState);
    }

    @XmlElement
    public boolean isSoundAlerted() {
        return soundAlerted;
    }

    @XmlElement
    public synchronized TournamentConfiguration getTournamentConfiguration() {
        return tournamentConfiguration;
    }

    public void setTournamentConfiguration(TournamentConfiguration tournamentConfiguration) {
        this.tournamentConfiguration = tournamentConfiguration;
    }

    @XmlElement
    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    @XmlElement
    public synchronized int getTranscurredTime() {
        return transcurredTime.get();
    }

    public synchronized void setTranscurredTime(int t) {
        transcurredTime.set(t);
    }

    public synchronized SimpleIntegerProperty transcurredTimeProperty() {
        return transcurredTime;
    }

    public void updateTranscurredTime() {
        transcurredTime.set(0);
        for (Level l : tournamentConfiguration.getBlindStruct().getLevelList()) {
            if (l.getLevel() < getLevelInProgress().getLevel()) {
                transcurredTime.set(transcurredTime.get() + l.getTime());
                if (!"0".equals(l.getBreather())) {
                    try {
                        transcurredTime.set(transcurredTime.get() + Integer.parseInt(l.getBreather()));
                    } catch (NumberFormatException ex) {
                        if (l.getBreather().length() > 2) {
                            transcurredTime.set(transcurredTime.get() + Integer.parseInt(l.getBreather().substring(2)));
                        }
                    }
                }
            }
        }
        int aux;
        synchronized (getLevelTimeService()) {
            
            String s = new String(getLevelTimeService().getMessage());
            aux = getLevelInProgress().getTime() - getLevelTimeService().getLevelTime()/60;
        }
        synchronized (transcurredTime) {
            setTranscurredTime(getTranscurredTime() + aux);
        }
    }

    public void saveTempTournament() {
        if (this != null) {
            new Thread() {
                @Override
                public void run() {
                    ObjectSerializer.saveObject(this, getTournamentName(), true);
                }
            };
        }
    }

    @XmlElement
    public synchronized Level getLevelInProgress() {
        return levelInProgress;
    }

    public void setLevelInProgress(Level l){
        levelInProgress.setAll(l.getLevel(), l.getTime(),
                            l.getSmallBlind(), l.getAnte(), l.getBigBlind(), l.getBreather());
        //levelTimeService.setLevelTime(levelInProgress.getTime() * 60);
        
    }
    public void setLevelInProgress(final int level) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (tournamentConfiguration.getBlindStruct() != null) {
                    Level l = tournamentConfiguration.getBlindStruct().get(level - 1);
                    levelInProgress.setAll(l.getLevel(), l.getTime(),
                            l.getSmallBlind(), l.getAnte(), l.getBigBlind(), l.getBreather());
                    levelTimeService.setLevelTime(levelInProgress.getTime() * 60);
                }
            }
        });
    }
    
    @XmlElement
    public boolean isSoundAllowed() {
        return soundAllowed.get();
    }

    public void setSoundAllowed(boolean soundAllowed) {
        this.soundAllowed.set(soundAllowed);
    }

    public void setTournamentState(stateType tournamentState) {
        this.tournamentState = tournamentState;
    }

    public void setAnonymousTournament(boolean anonymousTournament) {
        this.anonymousTournament = anonymousTournament;
    }

    @XmlElement
    public PlayerList getPlayerList() {
        return playerList;
    }

    public void setPlayerList(PlayerList playerList) {
        this.playerList = playerList;
    }

    public synchronized void levelUp() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setLevelInProgress(levelInProgress.getLevel() + 1);
                setBreakTime(false);
                soundAlerted = false;
                actionsHistory.add(0, new LevelAction("STARTED LEVEL " + (getLevelInProgress().getLevel()+1)+""));
                updateTranscurredTime();
            }
        });

    }

    public synchronized void levelDown() {
        setLevelInProgress(levelInProgress.getLevel() - 1);
        soundAlerted = false;
        updateTranscurredTime();
        int i = 0;
        while (i < actionsHistory.size()) {
            if (actionsHistory.get(i).getAction().contains("LEVEL")) {
                actionsHistory.remove(i);
                break;
            }
            i++;
        }
    }

    public void startLevel() {
        tournamentState = stateType.PLAYING;
     //   actionsHistory.add(0, new LevelAction("START TOURNAMENT. "
       //         + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "h"));
        getLevelTimeService().start();
    }

    public void pauseLevel() {
        tournamentState = stateType.PAUSED;
        int s = getTranscurredTime();
        
        getLevelTimeService().cancel();
    }

    public void resumeLevel() {
        tournamentState = stateType.PLAYING;
        setBreakTime(false);
        getLevelTimeService().restart();
    }

    public void startBreakTime() {
        tournamentState = stateType.BREAK;
        getLevelTimeService().restart();
    }

    public void pauseBreak() {
        tournamentState = stateType.BREAK_PAUSED;
        int s = getTranscurredTime();
        getLevelTimeService().cancel();
    }

    public void stopLevel() {
        tournamentState = stateType.STOPPED;
        getLevelTimeService().cancel();
        getLevelTimeService().reset();
    }

    public void addPlayer(boolean undo, int n) {
        for (int i = 0; i < n; i++) {
            playerList.addPlayer();
        }
        if (undo) {
            summary.setEliminatedPlayers(summary.getEliminatedPlayers() - 1);
            int i = 0;
            while (i < actionsHistory.size()) {
                if (actionsHistory.get(i).getAction().contains("PLAYER ELIMINATED")) {
                    actionsHistory.remove(i);
                    break;
                }
                i++;
            }
        } else {
            summary.updateRegisteredPlayers(summary.getRegisteredPlayers() + n);
            if (n == 1) {
                actionsHistory.add(0, new PlayerAction("PLAYER ADDED. ", playerList.get(playerList.size() - 1)));
            } else {
                actionsHistory.add(0, new PlayerAction(n + " PLAYERS ADDED. ", playerList.get(playerList.size() - 1)));

            }
        }
    }

    public void substractPlayer(boolean undo) {
        if (undo) {
            summary.updateRegisteredPlayers(summary.getRegisteredPlayers() - 1);
            int i = 0;
            while (i < actionsHistory.size()) {
                if (actionsHistory.get(i).getAction().contains("PLAYER ADDED") || actionsHistory.get(i).getAction().contains("PLAYERS ADDED")) {
                    if (actionsHistory.get(i).getAction().contains("PLAYER ADDED")) {
                        actionsHistory.remove(i);
                    } else {
                        String s = actionsHistory.get(i).getAction();
                        int q = Integer.parseInt(s.substring(0, s.indexOf(" ")));
                        actionsHistory.remove(i);
                        if (q > 1)
                            actionsHistory.add(i, new PlayerAction((q - 1) + " PLAYERS ADDED. ", playerList.get(playerList.size() - 1)));
                    }
                    break;
                }
                i++;
            }
        } else {
            summary.setEliminatedPlayers(summary.getEliminatedPlayers() + 1);
            actionsHistory.add(0, new PlayerAction("PLAYER ELIMINATED. ", playerList.get(playerList.size() - 1)));
        }
        playerList.substractPlayer();
    }

    public void addRebuy(int ID, int n) {
        for (int i = 0; i < n; i++) {
            playerList.addRebuy(ID);
        }
        summary.setRebuys(summary.getRebuys() + n);
        if (n == 1) {
            actionsHistory.add(0, new PlayerAction("REBUY ADDED. ", playerList.get(ID)));
        } else {
            actionsHistory.add(0, new PlayerAction(n + " REBUYS ADDED. ", playerList.get(ID)));
        }
    }

    public void addReentrada(int ID) {
        playerList.addReentrada(ID);
        summary.updateReentries(summary.getReentradas() + 1);
        actionsHistory.add(0, new PlayerAction("RE-ENTRY ADDED. ", playerList.get(ID)));
    }

    public void addAddon(int ID, int n) {
        for (int i = 0; i < n; i++) {
            playerList.addAddOn(ID);
        }
        summary.setAddons(summary.getAddons() + n);
        if (n == 1) {
            actionsHistory.add(0, new PlayerAction("ADDON ADDED. ", playerList.get(ID)));
        } else {
            actionsHistory.add(0, new PlayerAction(n + " ADDONS ADDED. ", playerList.get(ID)));
        }
    }

    public void subtractRebuy(int ID) {
        playerList.subtractRebuy(ID);
        summary.setRebuys(summary.getRebuys() - 1);
        int i = 0;
        while (i < actionsHistory.size()) {
            if (actionsHistory.get(i).getAction().contains("REBUY ADDED") || actionsHistory.get(i).getAction().contains("REBUYS ADDED")) {
                if (actionsHistory.get(i).getAction().contains("REBUY ADDED")) {
                    actionsHistory.remove(i);
                } else {
                    String s = actionsHistory.get(i).getAction();
                    int q = Integer.parseInt(s.substring(0, s.indexOf(" ")));
                    actionsHistory.remove(i);
                    if (q > 1) {
                        actionsHistory.add(i, new PlayerAction((q - 1) + " REBUYS ADDED. ", playerList.get(ID)));
                    }
                }
                break;
            }
            i++;
        }
    }

    public void subtractReentrada(int ID) {
        playerList.subtractReentrada(ID);
        summary.updateReentries(summary.getReentradas() - 1);
        int i = 0;
        while (i < actionsHistory.size()) {
            if (actionsHistory.get(i).getAction().contains("RE-ENTRY ADDED")) {
                actionsHistory.remove(i);
                break;
            }
            i++;
        }
    }

    public void subtractAddon(int ID) {
        playerList.subtractAddOn(ID);
        summary.setAddons(summary.getAddons() - 1);
        int i = 0;
        while (i < actionsHistory.size()) {
            if (actionsHistory.get(i).getAction().contains("ADDON ADDED") || actionsHistory.get(i).getAction().contains("ADDONS ADDED")) {
                if (actionsHistory.get(i).getAction().contains("ADDON ADDED")) {
                    actionsHistory.remove(i);
                } else {
                    String s = actionsHistory.get(i).getAction();
                    int q = Integer.parseInt(s.substring(0, s.indexOf(" ")));
                    actionsHistory.remove(i);
                    if (q > 1) {
                        actionsHistory.add(i, new PlayerAction((q - 1) + " ADDONS ADDED. ", playerList.get(ID)));
                    }
                }
                break;
            }
            i++;
        }
    }

    public ArrayList<Integer> refreshSummary() {
        return summary.calculate();
    }

    @XmlElement
    public ObservableList<Action> getActionsHistory() {
        return actionsHistory;
    }

    public void setActionsHistory(ObservableList<Action> actionsHistory) {
        this.actionsHistory = actionsHistory;
    }

    public boolean isLastLevel() {
        Level l = tournamentConfiguration.getBlindStruct().get(tournamentConfiguration.getBlindStruct().getLevelList().size() - 1);
        if (l.getLevel() == getLevelInProgress().getLevel()) {
            return true;
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        levelTimeService.cancel();
        System.out.println(this.getTournamentName());
    }
    
}