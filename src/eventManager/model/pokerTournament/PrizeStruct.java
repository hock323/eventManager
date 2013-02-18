package eventManager.model.pokerTournament;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType
@XmlSeeAlso({PrizePosition.class, CPTPrizeStruct.class, OACPrizeStruct.class, 
    LPAPrizeStruct.class, TenerifePrizeStruct.class, SatelitePrizeStruct.class, CustomPrizeStruct.class})
public abstract class PrizeStruct {

    protected ObservableList<PrizePosition> percentagesList = FXCollections.observableArrayList();
    protected int registeredPlayers = 0;
    protected int reentries = 0;
    protected int handout = 0;
    protected int playersWhoRecieve = 0;
    protected String name;

    public PrizeStruct() {
    }

    @XmlAttribute
    public int getPlayersWhoRecieve() {
        return playersWhoRecieve;
    }

    public void setPlayersWhoRecieve(int playersWhoRecieve) {
        this.playersWhoRecieve = playersWhoRecieve;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public int getRegistredPlayers() {
        return registeredPlayers;
    }

    public void setRegistredPlayers(int registredPlayers) {
        this.registeredPlayers = registredPlayers;
        calculatePrizes();
    }

    @XmlAttribute
    public int getReentries() {
        return reentries;
    }

    public void setReentries(int reentries) {
        this.reentries = reentries;
        calculatePrizes();
    }
    
    public int getHandout() {
        return handout;
    }

    public void setHandout(int handout) {
        this.handout = handout;
        calculatePrizes();
    }

    @XmlElement
    public ObservableList<PrizePosition> getPercentagesList() {
        return percentagesList;
    }

    public void setPercentagesList(ObservableList<PrizePosition> percentagesList) {
        this.percentagesList = percentagesList;
    }

    public void calculatePlayersWhoReceive(int registredPlayers, int reentries) {
        int numPlayers = 0;
        if ((registredPlayers + reentries) > 17) {
            if ((registredPlayers + reentries) <= 107) {
                numPlayers = ((registredPlayers + reentries) / 10) + 2;
            } else if ((registredPlayers + reentries) <= 250) {
                numPlayers = ((registredPlayers + reentries) / 9) + 1;
            }
            if ((numPlayers % 3) == 1) {
                numPlayers--;
            }
            if ((numPlayers % 3) == 2) {
                numPlayers++;
            }
        }
        playersWhoRecieve = numPlayers;
    }

    public void calculateAbsPercentages() {
        double denominador = 0;
        double x;
        for (int i = 0; i < playersWhoRecieve; i++) {
            denominador += percentagesList.get(i).getRelPercentage();
        }
        for (int i = 0; i < playersWhoRecieve; i++) {
            x = ((int) ((percentagesList.get(i).getRelPercentage() / denominador) * 10000)) / 100.0;
            percentagesList.get(i).setAbsPercentage(x);
        }
        
    }

    public void calculatePrizes() {
        calculatePlayersWhoReceive(registeredPlayers, reentries);
        calculateAbsPercentages();
        double prize, prizeCheck = 0;
        int players = getPlayersWhoRecieve();
        for (int i = 0; i < players; i++) {
            prize = (handout * getPercentagesList().get(i).getAbsPercentage()) / 100.0;
            if (prize != 0) {
                if ("Tenerife".equals(getName())) {
                    prize = (int) (((prize / 10) + 5) * 10);
                } else {
                    prize = (int) (((prize / 10) + 0.5) * 10);
                }
            }
            getPercentagesList().get(i).setMoney((int) prize);
            prizeCheck += prize;
        }
        for (int i = players; i< percentagesList.size();i++) {
            getPercentagesList().get(i).setMoney(0);
        }
        if (getPercentagesList().size() > 0) {
            int x = getPercentagesList().get(0).getMoney();
            getPercentagesList().get(0).setMoney((int) (x - (prizeCheck - handout)));
        }
    }
}
