package eventManager.per;

import eventManager.model.pokerTournament.Announcement;
import eventManager.model.pokerTournament.BlindStruct;
import eventManager.model.pokerTournament.PrizeStruct;
import eventManager.model.pokerTournament.Tournament;
import eventManager.model.pokerTournament.TournamentConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class ObjectSerializer {

    public static final String TOURNAMENT_DIR = "tournaments";
    public static final String TOURNAMENT_CONFIGURATION_DIR = "tournamentConfigurations";
    public static final String BLINDS_DIR = "blindStructs";
    public static final String PRIZES_DIR = "prizeStructs";
    public static final String ANNOUNCEMENT_DIR = "announcements";
    public static final String TEMP_DIR = "tmp";
    public static final String SEPARATOR = System.getProperties().getProperty("file.separator");
    public static final String DIR = System.getProperties().getProperty("user.dir") + SEPARATOR + "eventManager" + SEPARATOR + "res" + SEPARATOR;

    public static boolean saveObject(Object object, String name, boolean temp) {
        FileOutputStream outputFile;
        JAXBContext context;
        Marshaller marshaller;
        File file;
        String s = null;
        if (Tournament.class.isInstance(object)) {
            if (!temp) {
                file = new File(DIR + TOURNAMENT_DIR + SEPARATOR + name + ".xml");
                File tempFile = new File(DIR + TEMP_DIR + SEPARATOR + "tmp" + name + ".xml");
                if (tempFile.exists())
                    tempFile.delete();
            } else {
                file = new File(DIR + TEMP_DIR + SEPARATOR + "tmp" + name + ".xml");
            }
            Tournament objectSerialized = (Tournament) object;
            s = objectSerialized.getTournamentStateString();
            objectSerialized.setTournamentState("STOPPED");

        } else {
            if (TournamentConfiguration.class.isInstance(object)) {
                file = new File(DIR + TOURNAMENT_CONFIGURATION_DIR + SEPARATOR + name + ".xml");
                TournamentConfiguration objectSerialized = (TournamentConfiguration) object;
                objectSerialized.setName(name);
            } else {
                if (BlindStruct.class.isInstance(object)) {
                    file = new File(DIR + BLINDS_DIR + SEPARATOR + name + ".xml");
                    BlindStruct objectSerialized = (BlindStruct) object;
                    objectSerialized.setName(name);
                } else {
                    if (Announcement.class.isInstance(object)) {
                        file = new File(DIR + ANNOUNCEMENT_DIR + SEPARATOR + name + ".xml");                        
                    } else {
                        file = new File(DIR + PRIZES_DIR + SEPARATOR + name + ".xml");
                        PrizeStruct objectSerialized = (PrizeStruct) object;
                        objectSerialized.setName(name);
                    }
                }
            }
        }
        try {
            outputFile = new FileOutputStream(file.getAbsolutePath());
            context = JAXBContext.newInstance(object.getClass());
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, outputFile);
        } catch (FileNotFoundException | NullPointerException | JAXBException ex) {
            System.out.println("error Serializando: "+ ex.getCause().toString());
        }
        if (Tournament.class.isInstance(object)) {
            Tournament objectSerialized = (Tournament) object;
            objectSerialized.setTournamentState(s);
        }
        return true;
    }
    
    public static void renameTournament(String t0,String t1) {
        File file = new File(DIR + TEMP_DIR + SEPARATOR + "tmp" + t0 + ".xml");
        file.renameTo(new File(DIR + TEMP_DIR + SEPARATOR + "tmp" + t1 + ".xml"));
        file = new File(DIR + TOURNAMENT_DIR + SEPARATOR + t0 + ".xml");
        file.renameTo(new File(DIR + TOURNAMENT_DIR + SEPARATOR + t1 + ".xml"));
    }
}
