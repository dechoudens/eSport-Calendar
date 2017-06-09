package antoine.dechoudens.hesge.ch.ecalendar.base;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;

/**
 * Created by Meckanik on 31.05.2017.
 */

public class PostEntries extends AsyncTask<Competition, Void, String> {

    public interface Listener {
        void onGetFromUrlResult (String result); /* Appelée avec l'objet JSON récupéré comme paramètre */
        void onGetFromUrlError (Exception e);      /* Appelée en cas d'erreur avec l'Exception survenue en paramètre */
    }

    private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */
    private Listener listener = null;

    public PostEntries (Listener listener) {this.listener = listener;}

    @Override
    protected String doInBackground(Competition... params) {
        try {
            Competition comp = params[0];
            Log.d("POST COWABOO", comp.toString());
            String address = "http://groups.cowaboo.net/group3/public/api/entry";
            JSONObject json = new JSONObject();
            json.put("secretKey", comp.getSecretKey());
            json.put("observatoryId", comp.getGame().getNom());
            json.put("tags", comp.getNom());
            json.put("value", comp.getValues());
            String requestBody = json.toString();
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();

            InputStream inputStream;
            // get stream
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            // parse stream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
            // put into JSONObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Content", response);
            jsonObject.put("Message", urlConnection.getResponseMessage());
            jsonObject.put("Length", urlConnection.getContentLength());
            jsonObject.put("Type", urlConnection.getContentType());
            return jsonObject.toString();
        } catch (IOException | JSONException e) {
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener == null) {return;}
        if (result != null) {listener.onGetFromUrlResult(result);} else {listener.onGetFromUrlError(exception);}
    }
}
