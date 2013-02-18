package eventManager.net;

import eventManager.model.pokerTournament.Announcement;
import eventManager.model.pokerTournament.BlindStruct;
import eventManager.model.pokerTournament.Level;
import eventManager.model.pokerTournament.PrizeStruct;
import eventManager.model.pokerTournament.Tournament;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Announcement.class, Level.class, PrizeStruct.class, BlindStruct.class})
public final class TournamentDataView {

    private int chipsAverage = 0;
    private int buyins = 0;
    private int livePlayers = 0;
    private int reentries = 0;
    private int addOns = 0;
    private int rebuys = 0;
    private int time = 0;
    private int soundAlert1 = 0;
    private int soundAlert2 = 0;
    private String tournamentName = "";
    private String tournamentSubName = "";
    private boolean rebuyTournament = false;
    private boolean addonTournament = false;
    private boolean soundAllow = true;
    private int transcurredTime = 0;
    private BlindStruct blindStruct;
    private PrizeStruct prizeStruct;
    private int level;
    private ObservableList<Announcement> announcementList = FXCollections.observableList(new ArrayList<Announcement>());
    private enum stateType { PLAYING, PAUSED, BREAK, BREAK_PAUSED, INFINITE_BREAK, STOPPED, ED_BREAK, CR_BREAK, DB_BREAK };
    private stateType tournamentState = stateType.STOPPED;
    
    public TournamentDataView(Tournament t) {
        chipsAverage=t.getSummary().getChipsAverage();
        buyins=t.getSummary().getRegisteredPlayers();
        reentries=t.getSummary().getReentradas();
        rebuys=t.getSummary().getRebuys();
        addOns=t.getSummary().getAddons();
        livePlayers=t.getPlayerList().size();
        buyins=t.getSummary().getRegisteredPlayers();
        soundAlert1 = t.getTournamentConfiguration().getSoundAlert1();
        soundAlert2 = t.getTournamentConfiguration().getSoundAlert2();
        tournamentName = t.getTournamentName();
        tournamentSubName = t.getTournamentSubName();
        rebuyTournament = (t.getTournamentConfiguration().getRebuy() == null ? false : true);
        addonTournament = (t.getTournamentConfiguration().getAddon() == null ? false : true);
        soundAllow = t.isSoundAllowed();
        blindStruct = t.getTournamentConfiguration().getBlindStruct();
        prizeStruct = t.getTournamentConfiguration().getPrizeStruct();
        level = t.getLevelInProgress().getLevel();
        announcementList = t.getAnnouncementList();
        tournamentState = stateType.valueOf(t.getTournamentStateString());
        transcurredTime = t.getTranscurredTime();
    }

    public TournamentDataView() {
    }
    
    public void setAll(Tournament t){
        chipsAverage=t.getSummary().getChipsAverage();
        buyins=t.getSummary().getRegisteredPlayers();
        reentries=t.getSummary().getReentradas();
        rebuys=t.getSummary().getRebuys();
        addOns=t.getSummary().getAddons();
        livePlayers=t.getPlayerList().size();
        buyins=t.getSummary().getRegisteredPlayers();
        soundAlert1 = t.getTournamentConfiguration().getSoundAlert1();
        soundAlert2 = t.getTournamentConfiguration().getSoundAlert2();
        tournamentName = t.getTournamentName();
        tournamentSubName = t.getTournamentSubName();
        rebuyTournament = (t.getTournamentConfiguration().getRebuy() == null ? false : true);
        addonTournament = (t.getTournamentConfiguration().getAddon() == null ? false : true);
        soundAllow = t.isSoundAllowed();
        blindStruct = t.getTournamentConfiguration().getBlindStruct();
        prizeStruct = t.getTournamentConfiguration().getPrizeStruct();
        level = t.getLevelInProgress().getLevel();
        announcementList = t.getAnnouncementList();
        tournamentState = stateType.valueOf(t.getTournamentStateString());
    }
    
    @XmlElement
    public BlindStruct getBlindStruct() {
        return blindStruct;
    }

    @XmlElement
    public int getChipsAverage() {
        return chipsAverage;
    }

    @XmlElement
    public int getBuyins() {
        return buyins;
    }

    @XmlElement
    public int getReentries() {
        return reentries;
    }

    @XmlElement
    public int getAddOns() {
        return addOns;
    }

    @XmlElement
    public int getRebuys() {
        return rebuys;
    }

    @XmlElement
    public String getTournamentName() {
        return tournamentName;
    }

    @XmlElement
    public String getTournamentSubName() {
        return tournamentSubName;
    }

    @XmlElement
    public int getLivePlayers() {
        return livePlayers;
    }

    @XmlElement
    public boolean isRebuyTournament() {
        return rebuyTournament;
    }

    @XmlElement
    public boolean isAddonTournament() {
        return addonTournament;
    }

    @XmlElement
    public int getLevel() {
        return level;
    }

    @XmlElement
    public synchronized ObservableList<Announcement> getAnnouncementList() {
        return announcementList;
    }

    @XmlElement
    public PrizeStruct getPrizeStruct() {
        return prizeStruct;
    }

    @XmlElement
    public boolean isSoundAllow() {
        return soundAllow;
    }

    @XmlElement
    public int getSoundAlert1() {
        return soundAlert1;
    }

    @XmlElement
    public int getSoundAlert2() {
        return soundAlert2;
    }

    @XmlElement
    public String getTournamentState() {
        return tournamentState.name();
    }

    public void setTournamentState(stateType tournamentState) {
        this.tournamentState = tournamentState;
    }

    @XmlElement
    public int getTranscurredTime() {
        return transcurredTime;
    }
    
    @XmlElement
    public int getTime() {
        return time;
    }
    
    
}
