package com.example.yggdralisk.wroclawtouristhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yggdralisk on 16.05.16.
 */
public class EventsActivity extends AppCompatActivity {
    Spinner spinner;

    static final String[] UsableLanguages = {"English", "Polish", "German", "Russian", "Czech", "Any"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);

        spinner = ButterKnife.findById(this, R.id.languages_events_spinner);
        setSpinner();

    }

    private void setSpinner() {
        ArrayList<String> tempLangs = new ArrayList<>();
        String choosenLang = getChoice();

        for (int i = 0; i < UsableLanguages.length; i++)
            if (!UsableLanguages[i].equalsIgnoreCase(choosenLang))
                tempLangs.add(UsableLanguages[i]);

        tempLangs.add(0, choosenLang);
        spinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tempLangs));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveChoice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveChoice();
    }

    private void saveChoice() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.shared_pref_choosen_lang),spinner.getSelectedItem().toString()).apply();
    }

    private String getChoice() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getString(getString(R.string.shared_pref_choosen_lang),"Any");
    }

}
