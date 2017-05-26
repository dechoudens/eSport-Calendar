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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeGames;

public class MainActivity extends AppCompatActivity implements GetFromUrl.Listener{
    private ListeGames listeGames;
    private ListView lvGames;

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
        lvGames = (ListView) findViewById(R.id.lvGames);
    }

    private void initialise() {
        new GetFromUrl(this).execute(NomsWebService.URL_TAGS);
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
        TreeSet<Game> games = new TreeSet<>();
        try {
            JSONObject JSONtags = json.getJSONObject("tag_list");
            JSONObject JSONList = JSONtags.getJSONObject("list");
            Iterator itr = JSONList.keys();
            String res = "";
            while(itr.hasNext()){
                games.add(new Game(itr.next().toString()));
            }
            listeGames = new ListeGames(this, games);
            lvGames.setAdapter(listeGames.getAdapter());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetFromUrlError(Exception e) {
        Log.d("game", e.getMessage());
    }
}
