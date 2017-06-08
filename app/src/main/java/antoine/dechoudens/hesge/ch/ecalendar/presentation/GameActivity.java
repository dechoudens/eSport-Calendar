package antoine.dechoudens.hesge.ch.ecalendar.presentation;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetKeysFromMail;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;
import antoine.dechoudens.hesge.ch.ecalendar.metier.Data;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeCompetitions;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeGames;

import static antoine.dechoudens.hesge.ch.ecalendar.R.id.btnLike;
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
    private Game game;
    private Button btnAjouterComp;
    private static Context context;
    private Data data = Data.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        definirVariables();
        initialise();
        definirListener();
        context = this;
    }

    private void definirVariables() {
        lvComp = (ListView) findViewById(R.id.lvComp);
        tvNomGame = (TextView) findViewById(R.id.tvNomGame);
        btnAjouterComp = (Button) findViewById(R.id.btnAjouterComp);
    }

    private void initialise() {
        Intent intent = getIntent();
        game = (Game)intent.getSerializableExtra("game");
        tvNomGame.setText(game.getNom());
        btnAjouterComp.setText("Ajouter une compétition");
        new GetFromUrl(this).execute(NomsWebService.URL_OBS + game.getNom());
        GameActivity.this.setTitle("Sélectionnez une compétition");
    }

    private void definirListener() {
        lvComp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> hm = (HashMap<String, Object>) parent.getItemAtPosition(position);
                Competition comp = (Competition) hm.get(ListeCompetitions.REF_COMP);
                Intent intent = new Intent(getApplicationContext(), CompActivity.class);
                data.setCompetition(comp);
                startActivityForResult(intent, COMP);
            }
        });

        btnAjouterComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AjouterActivity.class);
                intent.putExtra("game", game);
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    @Override
    public void onGetFromUrlResult(JSONObject json) {
        TreeSet<Competition> comps = new TreeSet<>();
        try {
            JSONObject jsonDictionary = json.getJSONObject("dictionary");
            String author = (String)jsonDictionary.get("author");

            JSONObject jsonEntries = jsonDictionary.getJSONObject("entries");
            Iterator itr = jsonEntries.keys();
            String res = "";
            while(itr.hasNext()){
                String key = (String) itr.next();
                JSONObject jsonEntry = jsonEntries.getJSONObject(key);
                String values = jsonEntry.getString("value");
                Competition comp = createCompetition(values, key, author);
                new GetKeysFromMail().execute(comp);
                comps.add(comp);
            }
            listeCompetitions = new ListeCompetitions(this, comps);
            lvComp.setAdapter(listeCompetitions.getAdapter());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Competition createCompetition(String values, String key, String author) {
        List<String> dates = new ArrayList<>();
        StringTokenizer strT = new StringTokenizer(values, ";");
        String nom = strT.nextToken();
        String description = strT.nextToken();
        while(strT.hasMoreTokens()) {
            dates.add(strT.nextToken());
        }
        return new Competition(nom, description, key, dates, game, author);
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
