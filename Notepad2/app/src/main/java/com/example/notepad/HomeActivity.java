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

    private Button login1;
    private Button create1;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fAuth = FirebaseAuth.getInstance();
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

    //home to register button function
    private void register(){
        Intent regIntent = new Intent(HomeActivity.this, RegisterActivity.class);
        startActivity(regIntent);
    }

    //home to login button function
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


    //close application when back button is pressed
    public void onBackPressed() {
        //do nothing
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}