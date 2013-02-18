package eventManager.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class VisorLocation {

    private String IP;
    private final int PORT = 8080;
    private SimpleBooleanProperty busy = new SimpleBooleanProperty(false);
    private SimpleStringProperty ID = new SimpleStringProperty("");
    private SimpleStringProperty owner = new SimpleStringProperty("Free");
    private SimpleLongProperty ownerID = new SimpleLongProperty();

    public VisorLocation(String IP, Boolean busy, String ID) {
        this.IP = IP;
        this.busy.set(busy);
        this.ID.set(ID);
    }
    
    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPORT() {
        return PORT;
    }

    public void setBusy(Boolean busy) {
        this.busy.set(busy);
    }

    public Boolean isBusy() {
        return busy.get();
    }

    public String getID() {
        return ID.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public SimpleBooleanProperty busyProperty() {
        return busy;
    }

    public SimpleStringProperty IDProperty() {
        return ID;
    }

    public String getOwner() {
        return owner.get();
    }
    
    public SimpleStringProperty ownerProperty() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }
    
    public Long getOwnerID() {
        return ownerID.get();
    }
    
    public void setOwnerID(long owner) {
        this.ownerID.set(owner);
    }
         
    
    
}
