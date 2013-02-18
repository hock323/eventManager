package eventManager.model.pokerTournament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@SuppressWarnings("serial")
public class BreakTypeList extends ArrayList<String> {

    public BreakTypeList() {
        add("None");
        add("Normal Break");
        add("Dinner Break");
        add("Chip Race");
        add("End Of Day");
    }

    @XmlElement(name = "breakCode")
    public List<String> getBreakTypeList() {
        return this;
    }
    
    public boolean isInBreakTypeList(String code) {
        for (Iterator<String> it = this.iterator(); it.hasNext();) {
            String c = it.next();
            if (code.equals(c)) {
                return true;
            }
        }
        return false;
    }
}
