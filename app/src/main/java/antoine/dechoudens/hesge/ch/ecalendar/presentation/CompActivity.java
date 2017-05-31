package antoine.dechoudens.hesge.ch.ecalendar.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeCompetitions;
import antoine.dechoudens.hesge.ch.ecalendar.metier.ListeDate;

import static android.media.MediaSyncEvent.createEvent;

public class CompActivity extends AppCompatActivity {
    private Competition competiton;
    private TextView tvNomComp;
    private TextView tvDescription;
    private Button btnToCalendrier;
    private Button btnAnnuler;
    private TextView tvDateTitre;
    private ListView lvDate;
    private ListeDate listeDate;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp);
        definirVariables();
        definirListeners();
        initialise();
        context = this;
    }

    private void definirVariables() {
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvNomComp = (TextView) findViewById(R.id.tvNomComp);
        btnToCalendrier = (Button) findViewById(R.id.btnToCalendrier);
        btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
        tvDateTitre = (TextView) findViewById(R.id.tvDateTitre);
        lvDate = (ListView) findViewById(R.id.lvDate);
    }

    private void initialise() {
        Intent intent = getIntent();
        Log.d("lol", "intent");
        competiton = (Competition)intent.getSerializableExtra("comp");
        Log.d("lol", "competiton");
        tvNomComp.setText(competiton.toString());
        Log.d("lol", "tostring");
        tvDescription.setText(competiton.getDescription());
        btnToCalendrier.setText("tout Ajouter");
        btnAnnuler.setText("Annuler");
        listeDate = new ListeDate(getApplicationContext() ,competiton.getDates());
        lvDate.setAdapter(listeDate.getAdapter());
        tvDateTitre.setText("Dates");
        CompActivity.this.setTitle("Ajouter au calendrier");
    }

    private void definirListeners() {
        btnToCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Cette action va vous demander de rajouter " + listeDate.getNbDates() + " évenements dans votre calendrier. Êtes-vous sûr de vouloir continuer ?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Oui",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<Date> dates = listeDate.getDates();
                                for(Date date : dates){
                                    createEventCalendar(date);
                                    dialog.cancel();
                                }
                            }
                        });

                builder.setNegativeButton(
                        "Non",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
        });

        lvDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> hm = (HashMap<String, Object>) parent.getItemAtPosition(position);
                Date date = (Date) hm.get(ListeDate.REF_DATE);
                createEventCalendar(date);
            }
        });
    }

    private void createEventCalendar(Date date){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setData(CalendarContract.Events.CONTENT_URI);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, competiton.getNom());
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, competiton.getDescription());
        calIntent.putExtra("allDay", false);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                date.getTime());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                date.getTime()+60*60*1000);
        startActivity(calIntent);
    }

}
