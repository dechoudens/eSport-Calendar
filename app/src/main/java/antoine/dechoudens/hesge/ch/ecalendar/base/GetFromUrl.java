package antoine.dechoudens.hesge.ch.ecalendar.base;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Meckanik on 25.05.2017.
 */
public class GetFromUrl extends AsyncTask<String, Void, JSONObject> {

    /* Listener chargé de traiter les résultats */
    public interface Listener {
        void onGetFromUrlResult (JSONObject json); /* Appelée avec l'objet JSON récupéré comme paramètre */
        void onGetFromUrlError (Exception e);      /* Appelée en cas d'erreur avec l'Exception survenue en paramètre */
    } //  Listener

    private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */
    private Listener listener = null;   /* Listener (éventuel) chargé de traiter les résultats */

    /* Constructeur */
    public GetFromUrl (Listener listener) {this.listener = listener;}

    @Override
  /* Récupération de l'object JSON à partir de l'URL donné en paramètre sous forme d'un String */
    protected JSONObject doInBackground (String... params) {
        try {
            URL address = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)address.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true); connection.setDoOutput(false); /* Valeurs par défaut: donc inutile ici! */

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                result.append(line).append("\n");
                line = reader.readLine();
            }
            in.close();
            connection.disconnect();
            return new JSONObject(result.toString());
        } catch (Exception e) {
            exception = e;
            return null;
        }
    } // doInBackground

    @Override
  /* Le résultat est transmis au listener */
    protected void onPostExecute (JSONObject result) {
        if (listener == null) {return;}
        if (result != null) {listener.onGetFromUrlResult(result);} else {listener.onGetFromUrlError(exception);}
    } // onPostExecute

} // GetFromURL

