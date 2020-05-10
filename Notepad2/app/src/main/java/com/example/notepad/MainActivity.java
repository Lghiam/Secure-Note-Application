package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.notepad.user_sign.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

/*References
2020. [online] Available at: <https://developer.android.com/studio/intro> [Accessed 10 May 2020].
Firebase. 2020. Firebase Guides. [online] Available at: <https://firebase.google.com/docs/guides> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=9qcJk9DA4Sg&list=PLgqXWQqMyp4-NdaZDXCz7tVpN2LeqhV2G&index=1> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=bZyEdyssUFA&list=PLgqXWQqMyp4-NdaZDXCz7tVpN2LeqhV2G&index=2> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=Z9pRsU4KPSk&list=PLgqXWQqMyp4-NdaZDXCz7tVpN2LeqhV2G&index=7&t=0s> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=Oz3_yleYMx0&list=PLlGT4GXi8_8eQPaxMYqh3c1oOLw3Iiph_&index=5&t=0s> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=qHpJQ_HMtIk&list=PLlGT4GXi8_8eQPaxMYqh3c1oOLw3Iiph_&index=5> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=SAvQszo-npg&list=PLGCjwl1RrtcTXrWuRTa59RyRmQ4OedWrt&index=38> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=tPV8xA7m-iw> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=uqQAKXD59eY&list=PLgqXWQqMyp4-NdaZDXCz7tVpN2LeqhV2G&index=5> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=7BjrJ5JEpb4&list=PLgqXWQqMyp4-NdaZDXCz7tVpN2LeqhV2G&index=3> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=Iy3TePS_yTk&list=PLgqXWQqMyp4-NdaZDXCz7tVpN2LeqhV2G&index=7> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=jXtof6OUtcE> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=-9dqVR5lnlk&list=PLgqXWQqMyp4-NdaZDXCz7tVpN2LeqhV2G&index=5&t=0s> [Accessed 10 May 2020].
2020. [online] Available at: <https://www.youtube.com/watch?v=oi-UAwiBigQ&list=PLGCjwl1RrtcTXrWuRTa59RyRmQ4OedWrt&index=10&t=0s> [Accessed 10 May 2020].
 */


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference fNotesDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ///Bottom Navigation Function
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_create:
                        startActivity(new Intent(MainActivity.this, NotepadActivity.class));
                        break;
                }
                return false;
            }
        });



        mNotesList = (RecyclerView) findViewById(R.id.main_notes_list);
        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);



        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }
        updateUI();
    }




    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(
                NoteModel.class,
                R.layout.single_note_layout,
                NoteViewHolder.class,
                fNotesDatabase
        ) {

            @Override
            protected void populateViewHolder(final NoteViewHolder viewHolder, NoteModel noteModel, int i) {
                final String noteId = getRef(i).getKey();

                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String timestamp = dataSnapshot.child("timestamp").getValue().toString();

                        viewHolder.setNoteTitle(title);
                        viewHolder.setNoteTime(timestamp);

                        //Function to view particular notes on main page
                        viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, NotepadActivity.class);
                                intent.putExtra("noteId", noteId);
                                startActivity(intent);
                            }
                        });


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        };
        mNotesList.setAdapter(firebaseRecyclerAdapter);
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



    //Close application when back button is pressed
    public void onBackPressed() {
        Intent x = new Intent(Intent.ACTION_MAIN);
        x.addCategory(Intent.CATEGORY_HOME);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(x);
    }
}
