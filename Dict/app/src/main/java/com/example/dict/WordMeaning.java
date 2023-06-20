package com.example.dict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class WordMeaning extends AppCompatActivity {
    private TextView word,category,meaning;
    private ImageButton audio;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        word = (TextView)findViewById(R.id.word);
        category = (TextView)findViewById(R.id.category);
        meaning = (TextView)findViewById(R.id.meaning);
        String wordA = getIntent().getStringExtra("Word");
        String cat = "(" + getIntent().getStringExtra("Category") + ")";
        String mean = getIntent().getStringExtra("Meaning");
        word.setText(wordA);
        category.setText(cat);
        meaning.setText(mean);

        audio = (ImageButton) findViewById(R.id.audio);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(wordA, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }
}