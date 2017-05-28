package antoine.dechoudens.hesge.ch.ecalendar.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import antoine.dechoudens.hesge.ch.ecalendar.R;
import antoine.dechoudens.hesge.ch.ecalendar.base.GetFromUrl;
import antoine.dechoudens.hesge.ch.ecalendar.base.NomsWebService;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Competition;
import antoine.dechoudens.hesge.ch.ecalendar.domain.Game;

public class CompActivity extends AppCompatActivity {
    private TextView tvNomComp;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp);
        definirVariables();
        initialise();
    }

    private void definirVariables() {
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvNomComp = (TextView) findViewById(R.id.tvNomComp);
    }

    private void initialise() {
        Intent intent = getIntent();
        Competition comp = (Competition)intent.getSerializableExtra("comp");
        tvNomComp.setText(comp.getNom());
        tvDescription.setText(comp.getDescription());
    }

}
