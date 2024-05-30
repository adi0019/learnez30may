package com.example.final5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;

import android.speech.RecognizerIntent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.final5.recomandation.Video;
import com.example.final5.recomandation.VideoAdapter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.google.android.gms.common.internal.ImagesContract;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class practice_with_aitest extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_CODE_AUDIO_PERMISSION = 2;
    private VideoAdapter adapter;
    public Button checkreponse, clear_btn;
    private TextView count;

    private  TextView getTextViewGrammarErrors;
    private EditText editText;

    private ImageView micButton;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue, requestQueue1;

    private TextView topic_generator;

    private List<Video> videoList;
    private YouTubePlayerView youtubePlayerView;
    public String urlcount = "https://ml90.azurewebsites.net/word_count";
    private String[] item = {"Tell me your daily routine", "Your most unforgetteable incident", "My Favorite Hobby", "A Memorable Vacation", "The Importance of Family","My Best Friend", "A Day in My Life", "A Sport I Enjoy Watching or Playing","My Favorite Season of the Year","The Benefits of Learning a Second Language", "The Importance of Mental Health Awareness", "Your fitness goals","The Pros and Cons of Remote Work", "DREAMS", "ASPIRATIONS", "A story of your choice"};
    private int accuracy1;
    private TextView textViewErrors;

    private TextView textViewAccuracy;
    private TextView textViewSpellingErrors;
    private TextView textViewGrammarErrors;
    private TextView textViewAdditionalSuggestions;

    /* JADX INFO: Access modifiers changed from: protected */
    @SuppressLint("MissingInflatedId")
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_with_ai);
        getSupportActionBar().hide();

        textViewAccuracy = findViewById(R.id.textViewAccuracy);
        textViewSpellingErrors = findViewById(R.id.textViewSpellingErrors);
        textViewGrammarErrors = findViewById(R.id.textViewGrammarErrors);
        textViewAdditionalSuggestions = findViewById(R.id.textViewAdditionalSuggestions);
        textViewErrors = findViewById(R.id.textViewErrors);
        getTextViewGrammarErrors=findViewById(R.id.textView_grammer);
        micButton =findViewById(R.id.idIVMic);
        clear_btn=findViewById(R.id.clear_btn);

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.getText().clear();
            }
        });


        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(practice_with_aitest.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(practice_with_aitest.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
                }
                else{
                    startVoiceInput();
                }
                
            }
        });

        this.topic_generator = (TextView) findViewById(R.id.topic_genertator_text);
        this.checkreponse = (Button) findViewById(R.id.response_check_btn);

        generateTopic();

        this.youtubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.suggestion_recylcer_view);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.videoList = new ArrayList();
        VideoAdapter videoAdapter = new VideoAdapter(this, this.videoList);
        this.adapter = videoAdapter;
        this.recyclerView.setAdapter(videoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        this.requestQueue = Volley.newRequestQueue(this);
     //   fetchSuggestedVideos(accuracy);

        this.editText = (EditText) findViewById(R.id.textInput);


        //New Method
        requestQueue1 = Volley.newRequestQueue(this);
        checkreponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editText.getText().toString();
                if (!inputText.isEmpty()) {
                    makeApiRequest(inputText);
                    checkGrammar(practice_with_aitest.this,inputText);

                } else {
                    Toast.makeText(practice_with_aitest.this, "Please enter text", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    private void makeApiRequest(String inputText) {
        String url = "https://spelling-and-grammar-checker.vercel.app/check_spelling_and_grammar";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("text", inputText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        float accuracy = (float) response.getDouble("accuracy");
                        int spellingErrors = response.getInt("spelling_errors");
                        int grammarErrors = response.getInt("grammar_errors");


                        JSONArray errorsArray1 = response.getJSONArray("errors");
                        List<String> errorsList = new ArrayList<>();
                        for (int i = 0; i < errorsArray1.length(); i++) {
                            String errorString = errorsArray1.getString(i);
                            errorsList.add(errorString);
                        }
                        displayErrorsAndSuggestions(errorsList);



                        updateUI(accuracy, spellingErrors, grammarErrors);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(practice_with_aitest.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(practice_with_aitest.this, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue1.add(jsonObjectRequest);
    }

    private void updateUI(float accuracy, int spellingErrors, int grammarErrors) {
        textViewAccuracy.setText(""+ accuracy);
        textViewSpellingErrors.setText("" + spellingErrors);
        textViewGrammarErrors.setText("" + grammarErrors);
         accuracy1 =(int)accuracy;
        fetchSuggestedVideos(accuracy1);
    }

    private void displayErrorsAndSuggestions(List<String> errorsList) {
        StringBuilder errorsStringBuilder = new StringBuilder();
        for (String error : errorsList) {
            errorsStringBuilder.append(error).append("\n\n");
        }
        textViewErrors.setText(errorsStringBuilder.toString());
    }




    private void fetchSuggestedVideos(int accuracy) {
        String url = "https://rec9075.azurewebsites.net/suggest_videos?accuracy=" + this.accuracy1;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, url, null, new Response.Listener<JSONObject>() { // from class: com.example.final5.practice_with_aitest.3
            @Override // com.android.volley.Response.Listener
            public void onResponse(JSONObject response) {
                try {
                    JSONArray suggestions = response.getJSONArray("suggestions");
                    practice_with_aitest.this.videoList.clear();
                    for (int i = 0; i < suggestions.length(); i++) {
                        JSONObject suggestion = suggestions.getJSONObject(i);
                        String title = suggestion.getString("title");
                        String url2 = suggestion.getString(ImagesContract.URL);
                        Video video = new Video(title, url2);
                        practice_with_aitest.this.videoList.add(video);
                    }
                    practice_with_aitest.this.adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("ERROR", "JSON Parsing error", e);
                }
            }
        }, new Response.ErrorListener() { // from class: com.example.final5.practice_with_aitest.4
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Volley error", error);
            }
        });
       this.requestQueue.add(jsonObjectRequest);
    }








    private void generateTopic() {
        this.topic_generator.setOnClickListener(new View.OnClickListener() { // from class: com.example.final5.practice_with_aitest.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Random random = new Random();
                int i = random.nextInt(practice_with_aitest.this.item.length);
                practice_with_aitest.this.topic_generator.setText(practice_with_aitest.this.item[i]);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */

    private void checkGrammar(final Context context, String text) {
        String url = "https://13spell.azurewebsites.net/check-grammar";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            StringBuilder errorText = new StringBuilder();
                            for (int i = 0; i < response.getJSONArray("errors").length(); i++) {
                                String error = response.getJSONArray("errors").getString(i);
                                errorText.append(" ").append(error).append("\n");
                            }
                            getTextViewGrammarErrors.setText(errorText.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                            Log.e("JSON Parsing Error", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Volley Error", error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Your device does not support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            if (result != null && !result.isEmpty()) {
//                editText.setText(result.get(0));
//            }
            if (result != null && !result.isEmpty()) {
                String newText = result.get(0);
                String existingText = editText.getText().toString();
                String combinedText = existingText + "\n" + newText;
                editText.setText(combinedText);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceInput();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}