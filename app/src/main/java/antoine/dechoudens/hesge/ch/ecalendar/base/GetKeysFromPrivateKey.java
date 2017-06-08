package antoine.dechoudens.hesge.ch.ecalendar.base;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeCompetitions;

/**
 * Created by Meckanik on 31.05.2017.
 */

public class GetKeysFromPrivateKey extends AsyncTask<String, Void, JSONObject> {
    private String privateAddress;
    @Override
  /* Récupération de l'object JSON à partir de l'URL donné en paramètre sous forme d'un String */
    protected JSONObject doInBackground (String... params) {
        try {
            privateAddress = params[0];
            URL address = new URL("http://groups.cowaboo.net/group3/public/api/user?secretKey="+privateAddress);
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
            return null;
        }
    } // doInBackground

    @Override
  /* Le résultat est transmis au listener */
    protected void onPostExecute (JSONObject result) {
        try {
            String publicAddress = (String)result.get("publicAddress");
            new PostStellar().execute(publicAddress, privateAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // onPostExecute
}
