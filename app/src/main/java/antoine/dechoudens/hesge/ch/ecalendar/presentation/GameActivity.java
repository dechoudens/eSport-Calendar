package antoine.dechoudens.hesge.ch.ecalendar.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.TreeSet;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeCompetitions;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeGames;

import static antoine.dechoudens.hesge.ch.ecalendar.R.id.lvGames;

public class GameActivity extends AppCompatActivity implements GetFromUrl.Listener{
    private ListeCompetitions listeCompetitions;
    private ListView lvComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        definirVariables();
        initialise();
    }

    private void definirVariables() {
        lvComp = (ListView) findViewById(R.id.lvComp);
    }

    private void initialise() {
        //TO DO: INTENT - et passer le nom du jeu en URL_OBS + nom
        new GetFromUrl(this).execute(NomsWebService.URL_OBS);
    }

    @Override
    public void onGetFromUrlResult(JSONObject json) {
        //A MODIFIER pour obs
        TreeSet<Competition> comps = new TreeSet<>();
        try {
            JSONObject JSONtags = json.getJSONObject("tag_list");
            JSONObject JSONList = JSONtags.getJSONObject("list");
            Iterator itr = JSONList.keys();
            String res = "";
            while(itr.hasNext()){
                comps.add(new Competition(itr.next().toString()));
            }
            listeCompetitions = new ListeCompetitions(this, comps);
            lvComp.setAdapter(listeCompetitions.getAdapter());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetFromUrlError(Exception e) {

    }
}
