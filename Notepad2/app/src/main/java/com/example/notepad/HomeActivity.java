package com.example.notepad;

        import androidx.appcompat.app.AppCompatActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import com.example.notepad.user_sign.LoginActivity;
        import com.example.notepad.user_sign.RegisterActivity;
        import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    //button variables
    private Button login1;
    private Button create1;
    private Button notepad;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fAuth = FirebaseAuth.getInstance();

        //declaring variables
        login1 = (Button) findViewById(R.id.login1);
        create1 = (Button) findViewById(R.id.create1);

        updateUI();

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        create1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }


    private void register(){
        Intent regIntent = new Intent(HomeActivity.this, RegisterActivity.class);
        startActivity(regIntent);
    }


    private void login(){
        Intent logIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(logIntent);
    }



    private void updateUI(){
        //if user is signed into app
        if(fAuth.getCurrentUser() != null){
            Log.i("HomeActivity", "fAuth != null");
            Intent startIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();
        }
        else {
            Log.i("HomeActivity", "fAuth == null");
        }
    }


}