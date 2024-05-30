package com.example.final5;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ChatbotACtivity extends AppCompatActivity {
    private static final String API_URL = "https://chatbot9075.azurewebsites.net/chat";
    private List<ChatMessage> chatMessages;
    private ChatbotAdapter chatbotAdapter;
    private RecyclerView recyclerView;
    private ImageView sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_testing);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendButton = findViewById(R.id.sendButton1);

        chatMessages = new ArrayList<>();
        chatbotAdapter = new ChatbotAdapter(chatMessages);
        recyclerView.setAdapter(chatbotAdapter);

        final EditText userInputEditText = findViewById(R.id.userInputEditText);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = userInputEditText.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    chatMessages.add(new ChatMessage(userInput, true));
                    chatbotAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerView.scrollToPosition(chatMessages.size() - 1);
                    userInputEditText.setText("");
                    getBotResponse(userInput);  // Call the bot response function
                }
            }
        });
    }

    private void getBotResponse(String userInput) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("message", userInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, API_URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String botResponse = response.getString("response");
                    chatMessages.add(new ChatMessage(botResponse, false));
                    chatbotAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerView.scrollToPosition(chatMessages.size() - 1);
                } catch (JSONException e) {
                    Log.e("ContentValues", "Error parsing JSON response: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ContentValues", "Error sending request: " + error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
