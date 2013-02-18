package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayerAction extends Action {

    private Player player;

    public PlayerAction() {
    }
    
    public PlayerAction(String action, Player player) {
        super(action);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
