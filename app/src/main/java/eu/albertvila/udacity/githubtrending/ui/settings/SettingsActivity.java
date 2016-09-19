package eu.albertvila.udacity.githubtrending.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import eu.albertvila.udacity.githubtrending.R;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = (RadioGroup) findViewById(R.id.settings_radioGroup);

        String[] languages = getResources().getStringArray(R.array.languages);

        for (String language : languages) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(language);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.list_item_language_size));
            radioGroup.addView(radioButton);
        }
    }

}
