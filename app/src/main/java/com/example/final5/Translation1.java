package com.example.final5;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class Translation1 extends AppCompatActivity {
    private Spinner spinnerSourceLanguage;
    private Spinner spinnerTargetLanguage;
    private EditText sourceText;
    private TextView outputtext;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translateTV;
    private ArrayList<String> languageList;
    private ArrayList<String> languageCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        getSupportActionBar().hide();

        spinnerSourceLanguage = findViewById(R.id.spinner_source_language);
        spinnerTargetLanguage = findViewById(R.id.spinner_target_language);
        sourceText = findViewById(R.id.idEditSource);
        outputtext = findViewById(R.id.textOutput1);
        micIV = findViewById(R.id.idIVMic);
        translateBtn = findViewById(R.id.idBtnTranslation);
        translateTV = findViewById(R.id.idTranslatedTV);

        languageList = new ArrayList<>();
        languageCodes = new ArrayList<>();
        setupLanguageList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSourceLanguage.setAdapter(adapter);
        spinnerTargetLanguage.setAdapter(adapter);

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
                intent.putExtra("android.speech.extra.PROMPT", "Say something to translate");
                try {
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Translation1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateTV.setVisibility(View.VISIBLE);
                translateTV.setText("");
                if (!sourceText.getText().toString().isEmpty()) {
                    translate();
                } else {
                    Toast.makeText(Translation1.this, "Please enter text to translate", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupLanguageList() {
        languageList.add("English");
        languageCodes.add("en");
        languageList.add("Spanish");
        languageCodes.add("es");
        languageList.add("French");
        languageCodes.add("fr");
        languageList.add("German");
        languageCodes.add("de");
        languageList.add("Italian");
        languageCodes.add("it");
        languageList.add("Portuguese");
        languageCodes.add("pt");
        languageList.add("Dutch");
        languageCodes.add("nl");
        languageList.add("Swedish");
        languageCodes.add("sv");
        languageList.add("Danish");
        languageCodes.add("da");
        languageList.add("Norwegian");
        languageCodes.add("no");
        languageList.add("Finnish");
        languageCodes.add("fi");
        languageList.add("Russian");
        languageCodes.add("ru");
        languageList.add("Polish");
        languageCodes.add("pl");
        languageList.add("Czech");
        languageCodes.add("cs");
        languageList.add("Turkish");
        languageCodes.add("tr");
        languageList.add("Arabic");
        languageCodes.add("ar");
        languageList.add("Chinese");
        languageCodes.add("zh");
        languageList.add("Japanese");
        languageCodes.add("ja");
        languageList.add("Korean");
        languageCodes.add("ko");
        languageList.add("Hindi");
        languageCodes.add("hi");
        languageList.add("Marathi");
        languageCodes.add("mr");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (result != null && !result.isEmpty()) {
                sourceText.setText(result.get(0));
            }
        }
    }

    private void translate() {
        String inputText = sourceText.getText().toString();
        String sourceLanguageCode = languageCodes.get(spinnerSourceLanguage.getSelectedItemPosition());
        String targetLanguageCode = languageCodes.get(spinnerTargetLanguage.getSelectedItemPosition());
        new TranslationTask().execute(inputText, sourceLanguageCode, targetLanguageCode);
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String inputText = params[0];
            String sourceLanguage = params[1];
            String targetLanguage = params[2];
            try {
                URL url = new URL("https://api.mymemory.translated.net/get?q="
                        + URLEncoder.encode(inputText, "UTF-8")
                        + "&langpair=" + sourceLanguage + "|" + targetLanguage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String translatedText = jsonObject.getJSONObject("responseData").getString("translatedText");
                    outputtext.setText(translatedText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Translation1.this, "Translation failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
