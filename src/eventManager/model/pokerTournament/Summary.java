package eventManager.model.pokerTournament;

import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Octavio Roncal Andres
 */
@XmlRootElement
public class Summary {

    private int receipts = 0; // Recaudacion
    private int feeAmount = 0;
    private int handOut = 0; //Bote que se reparte en premios
    private int orgExtFeeAmount = 0;
    private int leaguePoolAmount = 0;
    private SimpleIntegerProperty rebuys = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty reentradas = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty chipsAverage = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty eliminatedPlayers = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty registeredPlayers = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty livePlayers = new SimpleIntegerProperty(1);
    private SimpleIntegerProperty addons  = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty totalChips = new SimpleIntegerProperty(0);
    private int knockoutAmount = 0;
    private PrizeStruct prizeStruct;
    private TournamentConfiguration configuration;
    
    public Summary() {
    }

    public Summary(TournamentConfiguration configuration) {
        this.configuration = configuration;
        prizeStruct = configuration.getPrizeStruct();
    }

    
    public int getReceipts() {
        return receipts;
    }

    public int getHandOut() {
        return handOut;
    }

    public int getOrgExtFee() {
        return orgExtFeeAmount;
    }

    public int getLeaguePool() {
        return leaguePoolAmount;
    }

    public int getRebuys() {
        return rebuys.get();
    }

    public void setRebuys(int rebuys) {
        this.rebuys.set(rebuys);
    }

    public SimpleIntegerProperty rebuysProperty() {
        return rebuys;
    }
    
    public int getFeeAmount() {
        return feeAmount;
    }

    public int getKnockoutAmount() {
        return knockoutAmount;
    }

    public int getReentradas() {
        return reentradas.get();
    }

    public SimpleIntegerProperty reentradasProperty() {
        return reentradas;
    }

    public SimpleIntegerProperty chipsAverageProperty() {
        return chipsAverage;
    }

    public void setChipsAverage(int chipsAverage) {
        this.chipsAverage.set(chipsAverage);
    }

    public int getChipsAverage() {
        return chipsAverage.get();
    }

    public SimpleIntegerProperty totalChipsProperty() {
        return totalChips;
    }

    public int getTotalChips() {
        return totalChips.get();
    }
    
    @XmlElement
    public TournamentConfiguration getConfiguration() {
        return configuration;
    }
    
    public void updateReentries(int reentradas) {
        this.reentradas.set(reentradas);
        prizeStruct.setReentries(reentradas);
    }

    public int getAddons() {
        return addons.get();
    }

    public void setAddons(int addOns) {
        this.addons.set(addOns);
    }

    public void setConfiguration(TournamentConfiguration configuration) {
        this.configuration = configuration;
    }

    public SimpleIntegerProperty addonsProperty() {
        return addons;
    }
    
    public int getEliminatedPlayers() {
        return eliminatedPlayers.get();
    }

    public void setEliminatedPlayers(int eliminatedPlayers) {
        this.eliminatedPlayers.set(eliminatedPlayers);
    }

    public SimpleIntegerProperty eliminatedPlayersProperty() {
        return eliminatedPlayers;
    }

    public SimpleIntegerProperty registeredPlayersProperty() {
        return registeredPlayers;
    }
    
    public int getRegisteredPlayers() {
        return registeredPlayers.get();
    }
    
    public void setRegisteredPlayers(int registeredPlayers) {
        this.registeredPlayers.set(registeredPlayers);
    }

    public void setReceipts(int receipts) {
        this.receipts = receipts;
    }

    public void setHandOut(int handOut) {
        this.handOut = handOut;
    }

    public void setReentradas(int reentradas) {
        this.reentradas.set(reentradas);
    }

    public void setPrizeStruct(PrizeStruct prizeStruct) {
        this.prizeStruct = prizeStruct;
    }

    public void updateRegisteredPlayers(int registeredPlayers) {
        this.registeredPlayers.set(registeredPlayers);
        if (prizeStruct != null)
            prizeStruct.setRegistredPlayers(registeredPlayers);
    }

    public SimpleIntegerProperty livePlayersProperty() {
        return livePlayers;
    }
    
    @XmlElement
    public PrizeStruct getPrizeStruct() {
        return prizeStruct;
    }
    
    public ArrayList<Integer> calculate() {
        int rebuyCost = 0;
        int addonCost = 0;
        int knockoutCost = 0;
        if (configuration.getRebuy() != null) {
            rebuyCost = configuration.getRebuy().getCost();
        }
        if (configuration.getAddon() != null) {
            addonCost = configuration.getAddon().getCost();
        }
        if (configuration.getKnockout() != null) {
            knockoutCost = configuration.getKnockout().getCost();
        }
        livePlayers.set(getRegisteredPlayers() - getEliminatedPlayers());
        int points = configuration.getBuyin().getChips() * getRegisteredPlayers();
        if (configuration.getReentrada() != null) {
            points += configuration.getBuyin().getChips() * getReentradas();
        }
        if (configuration.getRebuy() != null) {
            points += configuration.getRebuy().getChips() * getRebuys();
        }
        if (configuration.getAddon() != null) {
            points += configuration.getAddon().getChips() * getAddons();
        }
        totalChips.set(points);
        if (livePlayers.get() > 0)
            chipsAverage.set(points / livePlayers.get());
        else
            chipsAverage.set(0);
        ArrayList<Integer> summary = new ArrayList<>();
        summary.add(registeredPlayers.get());
        summary.add(rebuys.get());
        summary.add(reentradas.get());
        summary.add(addons.get());
        knockoutAmount = (registeredPlayers.get() + reentradas.get()) * knockoutCost;
        receipts = (registeredPlayers.get() + reentradas.get()) * configuration.getBuyin().getCost() + rebuys.get() * rebuyCost + addons.get() * addonCost;
        int prizes;
        if (configuration.getFees() != null) {
            feeAmount = (registeredPlayers.get() + reentradas.get()) * configuration.getFees().getOrganizationFee();
            prizes = receipts - feeAmount;
            orgExtFeeAmount = prizes * configuration.getFees().getExternalOrganization() / 100;
            leaguePoolAmount = prizes * configuration.getFees().getLeaguePool() / 100;
        } else {
            prizes = receipts;
            feeAmount = 0;
            orgExtFeeAmount = 0;
            leaguePoolAmount = 0;
        }
        handOut = prizes - orgExtFeeAmount - leaguePoolAmount - knockoutAmount;
        summary.add(receipts);
        summary.add(handOut);
        summary.add(feeAmount);
        summary.add(orgExtFeeAmount);
        summary.add(leaguePoolAmount);
        summary.add(prizes);
        summary.add(knockoutAmount);
        summary.add(totalChips.get());
        if (prizeStruct != null)
            prizeStruct.setHandout(handOut);
        configuration.getChopPrizes().setTotalChips(points);
        return summary;
    }

    @Override
    public String toString() {
        return "Summary{" + "receipts=" + receipts + ", fee=" + feeAmount + ", handout=" + handOut + ", orgExt="
                + orgExtFeeAmount + ", leagueJackPot=" + leaguePoolAmount + ", rebuys=" + rebuys + ", reentradas="
                + reentradas + ", addons=" + addons + ", eliminatedPlayers=" + eliminatedPlayers + ", registeredPlayers="
                + registeredPlayers + '}';
    }
}
