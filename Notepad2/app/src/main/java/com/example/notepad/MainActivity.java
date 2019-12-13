package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        updateUI();


        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.openNoteBtn);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, NotepadActivity.class);
                startActivity(startIntent);
            }
        });
    }

    private void updateUI(){
        //if user is signed into app
        if(fAuth.getCurrentUser() != null){
            Log.i("MainActivity", "fAuth != null");
        }
        else {
            Intent startIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("MainActivity", "fAuth == null");
        }
    }
}
