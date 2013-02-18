package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class LevelTimeService extends Service<Void> {
    
    private SimpleIntegerProperty timeInseconds = new SimpleIntegerProperty(1);
    
    
    public LevelTimeService() {
    }

    @XmlElement
    public synchronized SimpleIntegerProperty LevelTimeProperty() {
        return timeInseconds;
    }

    public synchronized void setLevelTime(int levelTime) {
        timeInseconds.set(levelTime);
    }

    public synchronized int getLevelTime() {
        return timeInseconds.get();
    }
    
    @Override
    protected synchronized Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String s, m;
                while (true) {
                    setLevelTime(getLevelTime() - 1);
                    s = (getLevelTime()) % 60 + "";
                    if (s.length() == 1) {
                        s = "0" + s;
                    }
                    m = (getLevelTime()) / 60 + "";
                    if (m.length() == 1) {
                        m = "0" + m;
                    }
                    
                    updateMessage(m + ":" + s);
                    Thread.sleep(1000);
                }
            }
        };
    }
    
}
