package eu.albertvila.udacity.githubtrending.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.data.Settings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupRadioGroup();
    }

    private void setupRadioGroup() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.settings_radioGroup);

        String[] languages = getResources().getStringArray(R.array.languages);

        for (String language : languages) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(language);
            radioButton.setContentDescription(language);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.list_item_language_text_size));
            radioButton.setId(View.generateViewId());
            // Check if it's the current selected language
            radioButton.setChecked(Settings.get(this).isSelectedLanguage(language));
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                String language = radioButton.getText().toString();
                Settings.get(SettingsActivity.this).setSelectedLanguage(language);
            }
        });
    }

}
