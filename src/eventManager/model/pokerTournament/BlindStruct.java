package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({BreakTypeList.class})
public class BlindStruct {
    
    private ObservableList<Level> levelList = FXCollections.observableArrayList();
    private BreakTypeList breakTypeList = new BreakTypeList();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleIntegerProperty duration = new SimpleIntegerProperty(0);

    public Level nextLevel(Level level) {
        for (Level l : levelList) {
            if (l.getLevel() == level.getLevel()) {
                return get((levelList.indexOf(l)) + 1);
            }
        }
        return null;
    }

    public Level get(int index){
        return levelList.get(index);
    }
    
    public Level previousLevel(Level level) {
        try {
            for (Level l : levelList) {
                if (l.getLevel() == level.getLevel()) {
                    return get((levelList.indexOf(l)) - 1);
                }
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
        }
        return level;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    
    @XmlAttribute
    public int getDuration() {
        return duration.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty durationProperty() {
        int h = duration.get() / 60;
        String m = duration.get() % 60 + "";
        if (m.length() == 1)
            m = "0" + m;
        return new SimpleStringProperty(h+":"+m+ " H");
    }    
    
    public void setDuration(int duration) {
        this.duration.set(duration);
    }
    
    public boolean isInBreakTypeList(String code) {
        for (String c : breakTypeList) {
            if (code.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public void setLevelList(ObservableList<Level> levelList) {
        this.levelList = levelList;
    }

    public int calculateDuration(){
        int x;
        duration.set(0);
        for (Level l : levelList) {
            try {
                x = Integer.parseInt(l.getBreather());
            } catch (NumberFormatException ex) {
                x = 0;
            }
            duration.set(duration.get() + l.getTime() + x);
        }
        return duration.get();
    }
    
    @XmlElement
    public ObservableList<Level> getLevelList() {
        return levelList;
    }
    
    @XmlElement
    public BreakTypeList getBreakTypeList() {
        return breakTypeList;
    }
}
