package antoine.dechoudens.hesge.ch.ecalendar.metier;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;

/**
 * Created by Meckanik on 26.05.2017.
 */

public class ListeCompetitions {
    public static final String REF_COMP = "Ref comp";
    private static final String[] FROM = new String[] {"Comp name"};
    private static final int[] TO = new int[] {R.id.tvComp};

    private TreeSet<Competition> comps;
    private SimpleAdapter adapter;
    private List<HashMap<String, Object>> data;

    public ListeCompetitions(Context context, TreeSet<Competition> comps) {
        this.comps = comps;
        data = new ArrayList<>();
        for (Competition c : this.comps){
            HashMap<String, Object> map = new HashMap<>();
            map.put(FROM[0], c.getNom());
            map.put(REF_COMP, c);
            data.add(map);
        }
        adapter = new SimpleAdapter(context, data, R.layout.one_comp, FROM, TO);
        adapter.notifyDataSetChanged();
    }

    public TreeSet<Competition> getGames() {
        return comps;
    }

    public SimpleAdapter getAdapter() {
        return adapter;
    }
}
