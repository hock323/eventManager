package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Octavio Roncal Andres
 */
@XmlRootElement
public class SingularPlayer extends Player {
    private String nacionality;
    private String nickname;

    public SingularPlayer() {
    }
    
    public SingularPlayer(int ID, String nacionality, String nickname) {
        super(ID);
        this.nacionality = nacionality;
        this.nickname = nickname;
    }

    @XmlAttribute
    public String getNacionality() {
        return nacionality;
    }

    public void setNacionality(String nacionality) {
        this.nacionality = nacionality;
    }

    @XmlAttribute
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "SingularPlayer{" + "nacionality=" + nacionality + ", nickname=" + nickname + '}';
    }
    
    
    
}
