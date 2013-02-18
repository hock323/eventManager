package eventManager.model.pokerTournament;

import java.io.Serializable;
import javafx.beans.property.SimpleIntegerProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Fees implements Serializable {

    private static final long serialVersionUID = 1L;
    private SimpleIntegerProperty organizationFee = new SimpleIntegerProperty();
    private SimpleIntegerProperty externalOrganization = new SimpleIntegerProperty();
    private SimpleIntegerProperty leaguePool = new SimpleIntegerProperty();

    public Fees() {
    }

    public Fees(int organizationFee, int externalOrganization, int leaguePool) {
        this.organizationFee.set(organizationFee);
        this.externalOrganization.set(externalOrganization);
        this.leaguePool.set(leaguePool);
    }

    public int getOrganizationFee() {
        return organizationFee.get();
    }

    public void setOrganizationFee(int organizationFee) {
        this.organizationFee.set(organizationFee);
    }

    public int getExternalOrganization() {
        return externalOrganization.get();
    }

    public void setExternalOrganization(int externalOrganization) {
        this.externalOrganization.set(externalOrganization);
    }

    public int getLeaguePool() {
        return leaguePool.get();
    }

    public void setLeaguePool(int leaguePool) {
        this.leaguePool.set(leaguePool);
    }

    public SimpleIntegerProperty organizationFeeProperty() {
        return organizationFee;
    }

    public SimpleIntegerProperty externalOrganizationProperty() {
        return externalOrganization;
    }

    public SimpleIntegerProperty leaguePoolProperty() {
        return leaguePool;
    }

    @Override
    public String toString() {
        return "Fees{" + "organizationFee=" + organizationFee + ", externalOrganization=" + externalOrganization + ", leagueJackpot=" + leaguePool + '}';
    }
}
