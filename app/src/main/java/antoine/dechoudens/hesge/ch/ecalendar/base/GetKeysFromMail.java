package antoine.dechoudens.hesge.ch.ecalendar.base;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;

/**
 * Created by Meckanik on 08.06.2017.
 */

public class GetKeysFromMail extends AsyncTask<Competition, Void, JSONObject> {
    private Competition comp;

    @Override
  /* Récupération de l'object JSON à partir de l'URL donné en paramètre sous forme d'un String */
    protected JSONObject doInBackground (Competition... params) {
        comp = params[0];
        try {
            URL address = new URL("http://groups.cowaboo.net/group3/public/api/users");
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
            JSONObject user_list = result.getJSONObject("user_list");
            JSONObject list = user_list.getJSONObject("list");
            String publicKey = (String)list.get(comp.getAuthor());
            comp.setPublicKey(publicKey);
        } catch (JSONException e) {
            Log.d("error",e.getMessage());
        }
    } // onPostExecute
}
