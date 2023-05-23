package com.example.simpleblog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ArticleDetailActivity extends AppCompatActivity {
    private TextView articleTitleTextView;
    private TextView articleDescriptionTextView;
    private TextView articleDateTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        articleTitleTextView = findViewById(R.id.articleTitleTextView);
        articleDescriptionTextView = findViewById(R.id.articleDescriptionTextView);
        articleDateTextView = findViewById(R.id.articleDateTextView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        articleTitleTextView.setText(title);
        articleDescriptionTextView.setText(description);

        String articleDate = sharedPreferences.getString("articleDate", "");
        articleDateTextView.setText(articleDate);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ArticleDetailActivity.this, MainActivity.class));
            }
        });
    }
}
