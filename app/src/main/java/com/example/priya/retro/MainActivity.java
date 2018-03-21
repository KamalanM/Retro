package com.example.priya.retro;

import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    Button get;
    String query;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textview);
        editText=(EditText) findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.listview);
        get=(Button)findViewById(R.id.button);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                query=editText.getText().toString();

                String API_BASE_URL = "https://api.github.com/";

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(API_BASE_URL)
                                .addConverterFactory(
                                        GsonConverterFactory.create()
                                );

                Retrofit retrofit =
                        builder
                                .client(
                                        httpClient.build()
                                )
                                .build();

                // Create a very simple REST adapter which points the GitHub API endpoint.
                GitHubClient client =  retrofit.create(GitHubClient.class);

                // Fetch a list of the Github repositories.
                Call<List<GitHubRepo>> call =
                        client.reposForUser(query);

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<List<GitHubRepo>>() {
                    @Override
                    public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                        // The network call was a success and we got a response
                        Log.d("TAg","REs");
                        textView.setText(response.toString());
                        List<GitHubRepo> repos = response.body();
                        listView.setAdapter(new GitHubRepoAdapter(MainActivity.this,repos));
                        // TODO: use the repository list and display it
                    }

                    @Override
                    public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {


                        // TODO: handle error
                    }
                });


            }
        });

    }
}
