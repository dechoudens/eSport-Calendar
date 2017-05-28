package antoine.dechoudens.hesge.ch.ecalendar.presentation;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeCompetitions;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeGames;

import static antoine.dechoudens.hesge.ch.ecalendar.R.id.lvGames;
import static antoine.dechoudens.hesge.ch.ecalendar.R.id.tvComp;
import static antoine.dechoudens.hesge.ch.ecalendar.presentation.MainActivity.COMP;
import static antoine.dechoudens.hesge.ch.ecalendar.presentation.MainActivity.GAME;
import static antoine.dechoudens.hesge.ch.ecalendar.presentation.MainActivity.RESULT_CANCEL;
import static antoine.dechoudens.hesge.ch.ecalendar.presentation.MainActivity.RESULT_OK;

public class GameActivity extends AppCompatActivity implements GetFromUrl.Listener{
    private ListeCompetitions listeCompetitions;
    private ListView lvComp;
    private TextView tvNomGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        definirVariables();
        initialise();
    }

    private void definirVariables() {
        lvComp = (ListView) findViewById(R.id.lvComp);
        tvNomGame = (TextView) findViewById(R.id.tvNomGame);
    }

    private void initialise() {
        Intent intent = getIntent();
        Game game = (Game)intent.getSerializableExtra("game");
        tvNomGame.setText(game.getNom());
        new GetFromUrl(this).execute(NomsWebService.URL_OBS + game.getNom());
    }
    private void definirListener() {
        lvComp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> hm = (HashMap<String, Object>) parent.getItemAtPosition(position);
                Competition comp = (Competition) hm.get(ListeGames.REF_GAME);
                Intent intent = new Intent(getApplicationContext(), CompActivity.class);
                intent.putExtra("comp", comp);
                startActivityForResult(intent, COMP);
            }
        });
    }

    @Override
    public void onGetFromUrlResult(JSONObject json) {
        TreeSet<Competition> comps = new TreeSet<>();
        try {
            JSONObject jsonEntries = json.getJSONObject("dictionary").getJSONObject("entries");
            Iterator itr = jsonEntries.keys();
            String res = "";
            while(itr.hasNext()){
                String key = (String) itr.next();
                JSONObject jsonEntry = jsonEntries.getJSONObject(key);
                String tag = jsonEntry.getString("tags");
                tag = formatTag(tag);
                comps.add(new Competition(tag, jsonEntry.getString("value"), jsonEntry.toString()));
            }
            listeCompetitions = new ListeCompetitions(this, comps);
            lvComp.setAdapter(listeCompetitions.getAdapter());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String formatTag(String tag) {
        tag = tag.substring(0,tag.length()-2);
        tag = tag.substring(2,tag.length());
        return tag;
    }

    @Override
    public void onGetFromUrlError(Exception e) {

    }
}
