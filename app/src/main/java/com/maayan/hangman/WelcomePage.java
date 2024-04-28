package com.maayan.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//https://sites.google.com/view/hangmanprivacy-policy/home?authuser=1
//privacy policy

//https://sites.google.com/view/hangman-termsofuse/home?authuser=1
//terms of use



public class WelcomePage extends AppCompatActivity {

    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        play = findViewById(R.id.BTN_PLAY);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                // Optionally, you can pass data to the next activity using extras
               // intent.putExtra("key", "value");
                // Start the SecondActivity
                startActivity(intent);
                finish();
            }
        });



    }
}
