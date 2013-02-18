package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Announcement {

    SimpleStringProperty announcement = new SimpleStringProperty("");
    SimpleBooleanProperty active = new SimpleBooleanProperty();
    SimpleBooleanProperty highlight = new SimpleBooleanProperty();
    Long ID = System.currentTimeMillis();

    public Announcement(String announcement, Boolean active, Boolean highlight) {
        this.announcement.set(announcement);
        this.active.set(active);
        this.highlight.set(highlight);
    }

    public Announcement() {
    }
    
    @XmlAttribute
    public String getAnnouncement() {
        return announcement.get();
    }

    public void setAnnouncement(String announcement) {
        this.announcement.set(announcement);
    }

    @XmlAttribute
    public Boolean isActive() {
        return active.get();
    }

    public void setActive(Boolean active) {
        this.active.set(active);
    }

    @XmlAttribute
    public Boolean isHighlight() {
        return highlight.get();
    }

    public void setHighlight(Boolean highlight) {
        this.highlight.set(highlight);
    }

    public SimpleStringProperty announcementProperty() {
        return announcement;
    }

    public SimpleBooleanProperty activeProperty() {
        return active;
    }

    public SimpleBooleanProperty highlightProperty() {
        return highlight;
    }

    @XmlAttribute
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    
    
}
