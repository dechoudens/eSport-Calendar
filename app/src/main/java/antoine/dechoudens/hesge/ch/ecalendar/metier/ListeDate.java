package antoine.dechoudens.hesge.ch.ecalendar.metier;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;

/**
 * Created by Meckanik on 30.05.2017.
 */

public class ListeDate {
    public static final String REF_DATE = "Ref date";
    private static final String[] FROM = new String[] {"date"};
    private static final int[] TO = new int[] {R.id.tvUneDate};
    DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    private List<Date> dates;
    private SimpleAdapter adapter;
    private List<HashMap<String, Object>> data;

    public ListeDate(Context context, List<String> datesStr) {
        stringToDate(datesStr);
        data = new ArrayList<>();
        for (Date date : this.dates){
            HashMap<String, Object> map = new HashMap<>();
            map.put(FROM[0], formatter.format(date));
            map.put(REF_DATE, date);
            data.add(map);
        }
        adapter = new SimpleAdapter(context, data, R.layout.une_date, FROM, TO);
        adapter.notifyDataSetChanged();
    }

    private void stringToDate(List<String> datesStr) {
        dates = new ArrayList<>();
        for (String str : datesStr){
            try {
                Date date = (Date)formatter.parse(str);
                dates.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Date> getDates() {
        return dates;
    }

    public SimpleAdapter getAdapter() {
        return adapter;
    }

    public int getNbDates(){
        return dates.size();
    }
}
