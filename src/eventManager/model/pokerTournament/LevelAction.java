package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LevelAction extends Action {

    public LevelAction() {
    }
    
    public LevelAction(String action) {
        super(action);
    }
}