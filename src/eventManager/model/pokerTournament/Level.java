package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Level implements Cloneable {

    private SimpleIntegerProperty level = new SimpleIntegerProperty();
    private SimpleStringProperty levelString = new SimpleStringProperty("1");
    private SimpleIntegerProperty time = new SimpleIntegerProperty();
    private SimpleIntegerProperty ante = new SimpleIntegerProperty();
    private SimpleIntegerProperty bigBlind = new SimpleIntegerProperty();
    private SimpleIntegerProperty smallBlind = new SimpleIntegerProperty();
    private SimpleStringProperty breather = new SimpleStringProperty();
    
    public Level(int level, int time, int smallBlind, int ante, int bigBlind, String breather) {
        this.time.set(time);
        this.ante.set(ante);
        this.bigBlind.set(bigBlind);
        this.smallBlind.set(smallBlind);
        this.level.set(level);
        this.breather.set(breather);
        levelString.set(level+"");
    }

    public Level() {
    }

    public int getTime() {
        return time.get();
    }

    public void setTime(int time) {
        this.time.set(time);

    }

    public int getAnte() {
        return ante.get();
    }

    public void setAnte(int ante) {
        this.ante.set(ante);
    }

    public int getBigBlind() {
        return bigBlind.get();
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind.set(bigBlind);
    }

    public int getSmallBlind() {
        return smallBlind.get();
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind.set(smallBlind);
    }

    public int getLevel() {
        return level.get();
    }

    public void setLevel(int level) {
        this.level.set(level);
        levelString.set(level+"");
    }

    public String getBreather() {
        return breather.get();
    }

    public void setBreather(String breather) {
        this.breather.set(breather);
    }

    @Override
    protected Level clone() throws CloneNotSupportedException {
        return(Level) super.clone();
    }

    public SimpleIntegerProperty levelProperty() {
        return level;
    }

    public SimpleIntegerProperty timeProperty() {
        return time;
    }

    public SimpleIntegerProperty anteProperty() {
        return ante;
    }

    public SimpleIntegerProperty bigBlindProperty() {
        return bigBlind;
    }

    public SimpleIntegerProperty smallBlindProperty() {
        return smallBlind;
    }

    public SimpleStringProperty breatherProperty() {
        return breather;
    }
    
    public SimpleStringProperty levelStringProperty() {
        return levelString;
    }
    
    public void setAll(int level, int time, int smallBlind, int ante, int bigBlind, String breather){
        this.time.set(time);
        this.ante.set(ante);
        this.bigBlind.set(bigBlind);
        this.smallBlind.set(smallBlind);
        this.level.set(level);
        this.breather.set(breather);
        levelString.set(level+"");
    }
    
    @Override
    public String toString() {
        return "Level{" + "level=" + level + ", time=" + time + ", ante=" + ante + ", bigBlind=" + bigBlind + ", smallBlind=" + smallBlind + '}';
    }
        
}