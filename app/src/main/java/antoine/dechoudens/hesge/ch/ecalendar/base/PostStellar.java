package antoine.dechoudens.hesge.ch.ecalendar.base;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import antoine.dechoudens.hesge.ch.ecalendar.metier.Data;
import antoine.dechoudens.hesge.ch.ecalendar.presentation.AjouterActivity;

/**
 * Created by Meckanik on 31.05.2017.
 */


public class PostStellar extends AsyncTask<String, Void, String> {
    private Data data = Data.getInstance();

    @Override
    protected String doInBackground(String... params) {
        try {
            String publicAddress = params[0];
            String privateAddress = params[1];
            String address = "http://groups.cowaboo.net/group3/public/api/user/transfer?public="+publicAddress
                    + "&secretKey=" + privateAddress
                    + "&destination=" + data.getCompetition().getPublicKey()
                    + "&amount=1"
                    ;
            JSONObject json = new JSONObject();
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
        Log.d("Stellar", result);
    }
}

