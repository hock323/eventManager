package eventManager.per;

import eventManager.model.pokerTournament.Announcement;
import eventManager.model.pokerTournament.BlindStruct;
import eventManager.model.pokerTournament.PrizeStruct;
import eventManager.model.pokerTournament.Tournament;
import eventManager.model.pokerTournament.TournamentConfiguration;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ObjectDeserializer {
    
    public static final String SEPARATOR = System.getProperties().getProperty("file.separator");
    public static final String DIR = System.getProperties().getProperty("user.dir") + SEPARATOR + "eventManager" + SEPARATOR + "res" + SEPARATOR;
    public static final String TOURNAMENT_DIR = "tournaments";
    public static final String TOURNAMENT_CONFIGURATION_DIR = "tournamentConfigurations";
    public static final String ANNOUNCEMENT_DIR = "announcements";
    public static final String BLINDS_DIR = "blindStructs";
    public static final String PRIZES_DIR = "prizeStructs";    
    public static final String TOURNAMENT_OBJECT = "T";
    public static final String BLINDS_OBJECT = "B";
    public static final String TOURNAMENT_CONFIGURATION_OBJECT = "TC";
    public static final String ANNOUNCEMENT_OBJECT = "A";
    public static final String PRIZES_OBJECT = "P";
    
    public static Object loadObject(String type, String name) {
        File file;
        try {
            JAXBContext context;
            if (TOURNAMENT_OBJECT.equals(type)) {
                file = new File(DIR + TOURNAMENT_DIR + SEPARATOR + name + ".xml");
                context = JAXBContext.newInstance(Tournament.class);
            } else {
                if (TOURNAMENT_CONFIGURATION_OBJECT.equals(type)) {
                    file = new File(DIR + TOURNAMENT_CONFIGURATION_DIR + SEPARATOR + name + ".xml");
                    context = JAXBContext.newInstance(TournamentConfiguration.class);
                } else {
                    if (BLINDS_OBJECT.equals(type)) {
                        file = new File(DIR + BLINDS_DIR + SEPARATOR + name);
                        context = JAXBContext.newInstance(BlindStruct.class);
                    } else {
                        if (ANNOUNCEMENT_OBJECT.equals(type)) {
                            file = new File(DIR + ANNOUNCEMENT_DIR + SEPARATOR + name);
                            context = JAXBContext.newInstance(Announcement.class);
                        } else {
                            file = new File(DIR + PRIZES_DIR + SEPARATOR + name + ".xml");
                            context = JAXBContext.newInstance(PrizeStruct.class);
                        }
                    }
                }
            }
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object object = unmarshaller.unmarshal(file);
            return object;
        } catch (NullPointerException | JAXBException ex) {
            System.out.println("error deserializando: " + ex.getCause().toString());
        }
        return null;
    }

}
