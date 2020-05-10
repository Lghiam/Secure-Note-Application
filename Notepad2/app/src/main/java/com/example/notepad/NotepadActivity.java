package com.example.notepad;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.notepad.user_sign.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class NotepadActivity extends AppCompatActivity {

    EditText addTitle, addContent;
    FirebaseAuth fAuth;
    DatabaseReference fNotesDatabase;
    String noteId = "anything";
    boolean noteExists;
    Button btnBackArrow;
    Button btnDelete;

    //Storing current date in string
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("E, d MMMM yyyy");
    String strDate = formatter.format(date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        btnDelete = (Button) findViewById(R.id.binBtn);

        //Check if note exists - set visibility for delete btn
        try {
            noteId = getIntent().getStringExtra("noteId");
            if (noteId.equals("anything")) {
                noteExists = false;
            } else {
                noteExists = true;
                btnDelete.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        //Delete button function
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteId.equals("anything")){
                }
                else{
                    fNotesDatabase.child(noteId).removeValue();
                }
            }
        });



        //Back arrow save note
        btnBackArrow = (Button) findViewById(R.id.backarrowBtn);

        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nTitle = addTitle.getText().toString();
                String nContent = addContent.getText().toString();

                //Note save validation
                if (nTitle.isEmpty() || nContent.isEmpty()) {
                    startActivity(new Intent(NotepadActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(NotepadActivity.this, MainActivity.class));
                    createNote(nTitle, nContent);
                }

            }
        });



        addTitle = findViewById(R.id.etTitle);
        addContent = findViewById(R.id.etContent);
        FloatingActionButton fab = findViewById(R.id.fab);

        fAuth = FirebaseAuth.getInstance();
        fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());



        //Save note button function
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nTitle = addTitle.getText().toString();
                String nContent = addContent.getText().toString();


                //Note save validation
                if (nTitle.isEmpty() || nContent.isEmpty()) {
                }
                else {
                    createNote(nTitle, nContent);
                    startActivity(new Intent(NotepadActivity.this, MainActivity.class));
                }
            }
        });
        putData();
    }



    //Entering note information to notepad
    private void putData() {
        if (noteExists) {
            fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("content")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String content = dataSnapshot.child("content").getValue().toString();

                        addTitle.setText(title);
                        addContent.setText(content);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



    //Create note function
    private void createNote(String nTitle, String nContent) {
        if (fAuth.getCurrentUser() != null) {
            if (noteExists) {
                //Updating existing notes
                Map updateMap = new HashMap();
                updateMap.put("title", nTitle);
                updateMap.put("content", nContent);
                updateMap.put("timestamp", strDate);

                fNotesDatabase.child(noteId).updateChildren(updateMap);
                startActivity(new Intent(NotepadActivity.this, MainActivity.class));
            }
            else {
                //Creating new notes
                final DatabaseReference newNotesRef = fNotesDatabase.push();
                final Map<String, Object> noteMap = new HashMap<>();

                noteMap.put("title", nTitle);
                noteMap.put("content", nContent);
                noteMap.put("timestamp", strDate);

                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNotesRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(NotepadActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NotepadActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                mainThread.start();
            }
        }
    }


    //Save note if back button pressed while in notepad activity
    public void onBackPressed() {
        String nTitle = addTitle.getText().toString();
        String nContent = addContent.getText().toString();
        //Note save validation
        if (nTitle.isEmpty() || nContent.isEmpty()) {
            startActivity(new Intent(NotepadActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(NotepadActivity.this, MainActivity.class));
            createNote(nTitle, nContent);
        }
    }
}





