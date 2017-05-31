package antoine.dechoudens.hesge.ch.ecalendar.presentation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.base.PostEntries;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;

public class AjouterActivity extends AppCompatActivity implements PostEntries.Listener{
    private Calendar myCalendar = Calendar.getInstance();
    private TextView tvTitreGameAjouter;
    private EditText etDate1;
    private EditText etDescription;
    private EditText etNom;
    private Button btnPost;
    private Button btnPlusDate;
    private static Context context;
    private Game game;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);
        definirVariable();
        definirListener();
        initialise();
        context = this;
    }


    private void definirVariable() {
        etDate1 = (EditText) findViewById(R.id.etDate1);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etNom = (EditText) findViewById(R.id.etNom);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnPlusDate = (Button) findViewById(R.id.btnPlusDate);
        tvTitreGameAjouter = (TextView) findViewById(R.id.tvTitreGameAjouter);

    }

    private void initialise() {
        Intent intent = getIntent();
        game = (Game)intent.getSerializableExtra("game");
        tvTitreGameAjouter.setText("Pour " + game.getNom());
        btnPost.setText("Ajouter");
        btnPost.setEnabled(false);
        AjouterActivity.this.setTitle("Nouvelle compétition");
    }


    private void definirListener() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        etDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AjouterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                valide();
            }
        };

        btnPlusDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Pas implémenté");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = etNom.getText().toString();
                String description = etDescription.getText().toString();
                List<String> dates = new ArrayList<String>();
                dates.add(etDate1.getText().toString());
                Competition competition = new Competition(nom, description, "", dates, game);
                new PostEntries(AjouterActivity.this).execute(competition);

                dialog = new ProgressDialog(AjouterActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        etNom.addTextChangedListener(tw);
        etDescription.addTextChangedListener(tw);
        etDate1.addTextChangedListener(tw);
    }

    private void valide(){
        if(etDate1.getText().toString().isEmpty()
                || etDescription.getText().toString().isEmpty()
                || etNom.getText().toString().isEmpty()){
            btnPost.setEnabled(false);
        }
        else{
            btnPost.setEnabled(true);
        }
    }

    private void updateLabel() {

        String myFormat = "dd.MM.yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        etDate1.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onGetFromUrlResult(String result) {
        dialog.cancel();

        CharSequence text = "Compétition ajoutée avec succès !";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        reinit();
    }

    @Override
    public void onGetFromUrlError(Exception e) {
        dialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("ERREUR - La compétition n'a pas été ajoutée");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void reinit() {
        etDescription.setText("");
        etDate1.setText("");
        etNom.setText("");
    }
}
