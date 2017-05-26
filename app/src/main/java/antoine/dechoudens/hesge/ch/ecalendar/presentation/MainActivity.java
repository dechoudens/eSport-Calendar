package antoine.dechoudens.hesge.ch.ecalendar.presentation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;

public class MainActivity extends AppCompatActivity implements GetFromUrl.Listener{
    private List<Game> games;
    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        definirVariables();
        initialise();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void definirVariables() {
        tvTest = (TextView)findViewById(R.id.tvTest);
    }

    private void initialise() {
        new GetFromUrl(this).execute(NomsWebService.URL_BASE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetFromUrlResult(JSONObject json) {
        games = new ArrayList<>();
        tvTest.setText("...Loading...");
        try {
            JSONArray JSONtags = json.getJSONArray("tag_list");
            JSONArray JSONobs = json.getJSONArray("list");
            tvTest.setText(JSONobs.get(0).toString());
        } catch (JSONException e) {
            tvTest.setText("Fuck");
        }

    }

    @Override
    public void onGetFromUrlError(Exception e) {
        tvTest.setText(e.getMessage());
    }
}
