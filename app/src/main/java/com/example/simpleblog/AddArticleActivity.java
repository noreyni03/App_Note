package com.example.simpleblog;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddArticleActivity extends AppCompatActivity {
    private TextView buttonAjouter;
    private SQLiteDatabase database;
    private EditText inputDescription;
    private EditText inputTitle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_article);

        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        buttonAjouter = findViewById(R.id.buttonAjouter);
        database = openOrCreateDatabase("articles", 0, null);
        createTableIfNotExists();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(inputTitle.getText().toString().trim(), inputDescription.getText().toString().trim());
                inputTitle.setText("");
                inputDescription.setText("");
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddArticleActivity.this, ArticleDetailActivity.class));
                finish();
            }
        });
    }

    private void createTableIfNotExists() {
        database.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)");
    }

    private void insertData(String title, String description) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        long rowId = database.insert("articles", null, values);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("articleDate", currentDate);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
