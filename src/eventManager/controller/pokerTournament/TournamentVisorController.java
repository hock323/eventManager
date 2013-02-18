package eventManager.controller.pokerTournament;

import eventManager.controller.VisorLocation;
import eventManager.net.HTTPClient;
import eventManager.net.TournamentDataView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.http.client.ClientProtocolException;

public class TournamentVisorController {
    
    private static final String SET_LEVEL = "SET_LEVEL";
    private static final String POLL = "POLL";
    private static final String PLAY = "PLAY";
    private static final String LOCK = "LOCK";
    private static final String PAUSE = "PAUSE";
    private static final String SHOW_HIDE_VISOR = "SHOW_HIDE_VISOR";
    private static final String REFRESH_DATA_VIEW = "REFRESH_DATA_VIEW";
    private static final String REFRESH_TIME = "REFRESH_TIME";
    private static final String DELETE_TOURNAMENT = "DELETE_TOURNAMENT";
    private static final String CLOSE_VISOR = "CLOSE_VISOR"; 
    private static final String UNLOCK = "UNLOCK";
  
    public static void poll(final ObservableList<VisorLocation> list) {
        Thread t = new Thread() {
            @Override
            public void run() {
                int i;
                list.clear();
                for (i = 2; i < 255; i++) {
                    PollThread t = new PollThread(i, list);
                    t.setDaemon(false);
                    t.start();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TournamentVisorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        t.setDaemon(false);
        t.start();
    }
    
    private static class PollThread extends Thread {
        private int i;
        ObservableList<VisorLocation> list;
        final HTTPClient client = new HTTPClient();

        public PollThread(int i, ObservableList<VisorLocation> list) {
            this.i = i;
            this.list = list;
        }

        private VisorLocation checkPollAnswer(String stream, String IP) {
            boolean lock;
            String[] parameters = stream.split(";");
            if (Integer.parseInt(parameters[1]) == 0)
                lock = false;
            else lock = true;
            VisorLocation visor = new VisorLocation(IP, lock, parameters[0]);
            visor.setOwner(parameters[2]);
            return visor;
        }

        @Override
        public void run() {
            try {
                //network = "10.13.13";
                InetAddress netAddress = InetAddress.getLocalHost();
                String address = netAddress.getHostAddress();
                final String network = address.substring(0, address.lastIndexOf("."));
                boolean inList = false;
                String br;
                br = client.GET(network + "." + i, POLL, new String[1]);
                if (br != null) {
                    VisorLocation visor = checkPollAnswer(br, network + "." + i);
                    synchronized (list) {
                        for (VisorLocation v : list) {
                            if (v.getID().equals(visor.getID())) {
                                inList = true;
                                break;
                            }
                        }
                    }
                    if (!inList) {
                        synchronized (list) {
                            list.add(visor);
                        }
                    }
                }
            } catch (ClientProtocolException ex) {
            } catch (IOException ex) {
            }
        }
    }

    public static void lockVisor(VisorLocation visor, long ownerID, String owner) throws IOException {
        HTTPClient client = new HTTPClient();
        String[] stringLock = {"ID=" + ownerID, "NAME=" + owner};
        client.GET(visor.getIP(), LOCK, stringLock);
    }

    public static void refreshTournamentDataView(final ObservableList<VisorLocation> list, final long ownerID, final TournamentDataView dataView) {
        for (VisorLocation visor : list) {
            if (visor.getOwnerID() == ownerID) {
                RefreshDataThread t = new RefreshDataThread(visor, dataView);
                t.setDaemon(true);
                t.start();
            }
        }
    }
    
    private static class RefreshDataThread extends Thread {
        VisorLocation visor;
        TournamentDataView dataView;

        public RefreshDataThread(VisorLocation visor, TournamentDataView dataView) {
            this.visor = visor;
            this.dataView = dataView;
        }
        
        @Override
        public void run() {
            try {
                HTTPClient client = new HTTPClient();
                File output = new File("temp_"+ (long) (Math.random()*100000000)+".xml");
                JAXBContext context = JAXBContext.newInstance(TournamentDataView.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(dataView, output);
                InputStream input = new FileInputStream(output);
                client.POST(visor.getIP(), visor.getPORT(), REFRESH_DATA_VIEW, new String[1], input, output.length());
                output.delete();
            } catch (IOException | JAXBException ex) {
                System.out.println("Error refreshing");
                Logger.getLogger(TournamentVisorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void playAction(ObservableList<VisorLocation> list, long ownerID, int time) throws IOException {
        HTTPClient client;
        String[] stringTime = {"time=" + time};
        for (VisorLocation visor : list) {
            if (visor.getOwnerID() == ownerID) {
                client = new HTTPClient();
                client.GET(visor.getIP(), PLAY, stringTime);
            }
        }
    }

    public static void pauseAction(ObservableList<VisorLocation> list, long ownerID, int time) throws IOException {
        HTTPClient client;
        String[] stringTime = {"time=" + time};
        for (VisorLocation visor : list) {
            if (visor.getOwnerID() == ownerID) {
                client = new HTTPClient();
                client.GET(visor.getIP(), PAUSE, stringTime);
            }
        }
    }

    public static void refreshTime(ObservableList<VisorLocation> list, long ownerID, int time) throws IOException {
        HTTPClient client;
        String[] stringTime = {"time=" + time};
        if (list != null) {
            for (VisorLocation visor : list) {
                if (visor.getOwnerID() == ownerID) {
                    client = new HTTPClient();
                    client.GET(visor.getIP(), REFRESH_TIME, stringTime);
                }
            }
        }
    }

    public static void setLevel(ObservableList<VisorLocation> list, long ownerID, int level) throws IOException {
        HTTPClient client = new HTTPClient();
        String[] stringLevel = {"level=" + level};
        for (VisorLocation visor : list) {
            if (visor.getOwnerID() == ownerID) {
                client.GET(visor.getIP(), SET_LEVEL, stringLevel);
            }
        }
    }

    public static void showHideVisor(ObservableList<VisorLocation> list, long ownerID, String[] params) {
        HTTPClient client = new HTTPClient();
        for (VisorLocation visor : list) {
            if (visor.getOwnerID() == ownerID) {
                
            }
            try {
                client.GET(visor.getIP(), SHOW_HIDE_VISOR, params);
            } catch (IOException ex) {
                Logger.getLogger(TournamentVisorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void deleteTournament(ObservableList<VisorLocation> list, String[] params) {
        HTTPClient client = new HTTPClient();
        for (VisorLocation visor : list) {
            try {
                client.GET(visor.getIP(), DELETE_TOURNAMENT, params);
            } catch (IOException ex) {
                Logger.getLogger(TournamentVisorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void closeVisor(ObservableList<VisorLocation> list, String[] params) {
        HTTPClient client = new HTTPClient();
        for (VisorLocation visor : list) {
            try {
                client.GET(visor.getIP(), CLOSE_VISOR, params);
            } catch (IOException ex) {
                Logger.getLogger(TournamentVisorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static void unlockVisor(VisorLocation visor) throws IOException {
            HTTPClient client = new HTTPClient();
            String[] stringLock = {"ID=" + 0};
            client.GET(visor.getIP(), UNLOCK, stringLock);
    }
}
