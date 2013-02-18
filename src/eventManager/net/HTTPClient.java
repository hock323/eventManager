package eventManager.net;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HTTPClient {

    public final int PORT = 8080;
    private final int timeoutConnection = 700;
    private final int timeoutSocket = 700;
    
    public String GET(String IP, String action, String[] params) throws ClientProtocolException, IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        String request = "http://" + IP + ":" + PORT + "/TournamentVisor?op=" + action;
        for (String s : params) {
            request += ";" + s;
        }
        request = request.replace(' ', '&');
        if (!request.contains("POLL")) {
            System.out.println("Request:" + request);
        }
        HttpGet getRequest = new HttpGet(request);
        getRequest.addHeader("accept", "application/plain");
        HttpResponse response = httpClient.execute(getRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        String stringResponse = EntityUtils.toString(response.getEntity()).toString();
        httpClient.getConnectionManager().shutdown();
        return stringResponse;
    }

    public void POST(String IP, int port, String action, String[] params, InputStream stream, long size) throws FileNotFoundException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String request = "http://" + IP + ":" + PORT + "/TournamentVisor?op=" + action;
        for (String s : params) {
            request += ";" + s;
        }
        request = request.replace(' ', '&');
        System.out.println("Request:" + request);
        HttpPost postRequest = new HttpPost(request);
        postRequest.addHeader("accept", "application/xml");
        postRequest.setEntity(new InputStreamEntity(stream, size));
        postRequest.setHeader("Content-type", "text/xml; charset=UTF-8");
        HttpResponse response = httpClient.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

    }
}
