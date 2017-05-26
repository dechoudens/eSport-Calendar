package antoine.dechoudens.hesge.ch.ecalendar.metier;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;

/**
 * Created by Meckanik on 26.05.2017.
 */

public class ListeGames {
    public static final String REF_GAME = "Ref game";
    private static final String[] FROM = new String[] {"Game name"};
    private static final int[] TO = new int[] {R.id.tvNom};

    private TreeSet<Game> games;
    private SimpleAdapter adapter;
    private List<HashMap<String, Object>> data;

    public ListeGames(Context context, TreeSet<Game> games) {
        this.games = games;
        data = new ArrayList<>();
        for (Game g : this.games){
            HashMap<String, Object> map = new HashMap<>();
            map.put(FROM[0], g.getNom());
            map.put(REF_GAME, g);
            data.add(map);
        }
        adapter = new SimpleAdapter(context, data, R.layout.one_game, FROM, TO);
        adapter.notifyDataSetChanged();
    }

    public TreeSet<Game> getGames() {
        return games;
    }

    public SimpleAdapter getAdapter() {
        return adapter;
    }
}
