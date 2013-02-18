package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class AnonymousPlayer extends Player {

    public AnonymousPlayer() {
    }
    
    public AnonymousPlayer(int ID) {
        super(ID);
    }

    
    
}
