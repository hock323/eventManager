package eventManager.model.pokerTournament;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Player.class})

public class PlayerList extends ArrayList<Player>{
    
    int id = 0;

    public void addRebuy(int id) {
        get(find(id)).addRebuy();
    }

    public void addReentrada(int id) {
        get(find(id)).addReentrada();
    }

    public void addAddOn(int id) {
        get(find(id)).addAddOn();
    }

    public int find(int id) {
        int playerPos = -1;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getID() == id) {
                playerPos = i;
                break;
            }
        }
        return playerPos;
    }

    public void addPlayer() {
        add(new AnonymousPlayer(id));
        id++;
    }

    public void addPLayer(String nick, String nationality) {
        add(new SingularPlayer(id, nick, nationality));
        id++;
    }

    public void substractPlayer() throws ArrayIndexOutOfBoundsException {
        remove(size() - 1);
        id--;
    }

    public void substractPlayer(int ID) throws ArrayIndexOutOfBoundsException {
        remove(find(ID));
        id--;
    }

    void subtractRebuy(int ID) {
        get(find(ID)).subtractRebuy();
    }

    void subtractReentrada(int ID) {
        get(find(ID)).subtractReentradas();
    }

    void subtractAddOn(int ID) {
        get(find(ID)).subtractAddOn();
    }
}
