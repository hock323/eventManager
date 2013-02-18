package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Addon.class, Fees.class, ReBuy.class, Reentry.class, Knockout.class, BuyIn.class, PrizeStruct.class, BlindStruct.class})
public class TournamentConfiguration {

    private Addon addon = null;
    private Fees fees = new Fees(0, 0, 0);
    private ReBuy rebuy = null;
    private Reentry reentrada = null;
    private Knockout knockout = null;
    private BuyIn buyin;
    private PrizeStruct prizeStruct;
    private ChopPrizes chopPrizes;
    private BlindStruct blindStruct;
    private int soundAlert1 = 0;
    private int soundAlert2 = 0;
    private boolean levelAlert = false;
    private String name = "";

    public TournamentConfiguration(BuyIn buyin, PrizeStruct prizeStruct, BlindStruct blindStruct) {
        this.buyin = buyin;
        this.prizeStruct = prizeStruct;
        this.blindStruct = blindStruct;
        chopPrizes = new ChopPrizes(prizeStruct);
    }

    public TournamentConfiguration() {
    }

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }

    public Fees getFees() {
        return fees;
    }

    public void setFees(Fees fees) {
        this.fees = fees;
    }

    public Reentry getReentrada() {
        return reentrada;
    }

    public void setReentrada(Reentry reentrada) {
        this.reentrada = reentrada;
    }

    public ReBuy getRebuy() {
        return rebuy;
    }

    public void setRebuy(ReBuy rebuy) {
        this.rebuy = rebuy;
    }

    public BuyIn getBuyin() {
        return buyin;
    }

    public void setBuyin(BuyIn buyin) {
        this.buyin = buyin;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlElement
    public PrizeStruct getPrizeStruct() {
        return prizeStruct;
    }

    public void setPrizeStruct(PrizeStruct prizeStruct) {
        this.prizeStruct = prizeStruct;
        chopPrizes = new ChopPrizes(prizeStruct);
    }

    @XmlElement
    public ChopPrizes getChopPrizes() {
        return chopPrizes;
    }

    public void setChopPrizes(ChopPrizes chopPrizes) {
        this.chopPrizes = chopPrizes;
    }

    public BlindStruct getBlindStruct() {
        return blindStruct;
    }

    public void setBlindStruct(BlindStruct blindStruct) {
        this.blindStruct = blindStruct;
    }

    @XmlElement
    public Knockout getKnockout() {
        return knockout;
    }

    public void setKnockout(Knockout knockout) {
        this.knockout = knockout;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getSoundAlert1() {
        return soundAlert1;
    }

    public void setSoundAlert1(int soundAlert1) {
        this.soundAlert1 = soundAlert1;
    }

    @XmlElement
    public int getSoundAlert2() {
        return soundAlert2;
    }

    public void setSoundAlert2(int soundAlert2) {
        this.soundAlert2 = soundAlert2;
    }

    @XmlElement
    public boolean isLevelAlert() {
        return levelAlert;
    }

    public void setLevelAlert(boolean levelAlert) {
        this.levelAlert = levelAlert;
    }
}