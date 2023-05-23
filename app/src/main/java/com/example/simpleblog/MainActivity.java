package com.example.simpleblog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private LinearLayout articlesLayout;
    private SQLiteDatabase database;
    private int textViewMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        articlesLayout = findViewById(R.id.articlesLayout);
        database = openOrCreateDatabase("articles", 0, null);
        createTableIfNotExists();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddArticleActivity.class));
            }
        });

        textViewMargin = (int) getResources().getDisplayMetrics().density * 20;
        loadArticles();
    }

    private void createTableIfNotExists() {
        database.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)");
    }

    private void loadArticles() {
        articlesLayout.removeAllViews();
        Cursor cursor = database.rawQuery("SELECT * FROM articles", null);
        int idColumnIndex = cursor.getColumnIndex("id");
        int titleColumnIndex = cursor.getColumnIndex("title");
        int descriptionColumnIndex = cursor.getColumnIndex("description");

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);

            View articleView = getLayoutInflater().inflate(R.layout.article_item, null);
            TextView titleTextView = articleView.findViewById(R.id.articleTitle);
            TextView descriptionTextView = articleView.findViewById(R.id.articleDescription);

            if (title == null || title.isEmpty()) {
                titleTextView.setVisibility(View.INVISIBLE);
            } else {
                titleTextView.setText(title);
            }

            if (description == null || description.isEmpty()) {
                descriptionTextView.setVisibility(View.GONE);
            } else {
                descriptionTextView.setText(description);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(textViewMargin, textViewMargin, textViewMargin, textViewMargin);
            articleView.setLayoutParams(layoutParams);

            articleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    startActivity(intent);
                }
            });

            articlesLayout.addView(articleView);
        }

        if (cursor.getCount() == 0) {
            TextView emptyTextView = new TextView(this);
            emptyTextView.setText("No articles available.");
            emptyTextView.setGravity(Gravity.CENTER);
            emptyTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            articlesLayout.addView(emptyTextView);
        }

        cursor.close();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
