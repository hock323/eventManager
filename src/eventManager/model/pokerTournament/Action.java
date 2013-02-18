package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({PlayerAction.class, LevelAction.class})
public abstract class Action {
    
    private String action;

    public Action() {
    }

    public Action(String action) {
        this.action = action;
    }
    
    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Action{" + "action=" + action + '}';
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    
    
}