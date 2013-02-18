package eventManager.model.pokerTournament;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({PrizeStruct.class})
public class PrizeStructList extends ArrayList<PrizeStruct> {

    private static final long serialVersionUID = 1L;

    public PrizeStructList() {
        super();
        add(new CPTPrizeStruct());
        add(new OACPrizeStruct());
        add(new TenerifePrizeStruct());
        add(new LPAPrizeStruct());
        add(new CustomPrizeStruct());
        add(new SatelitePrizeStruct());
    }

    @XmlElement(name = "definedPrizeStruct")
    public List<PrizeStruct> getDefindedPrizeStructList() {
        return this;
    }
}
