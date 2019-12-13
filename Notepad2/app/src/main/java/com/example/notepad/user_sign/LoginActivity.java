package com.example.notepad.user_sign;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.example.notepad.MainActivity;
        import com.example.notepad.R;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPass;
    private Button btnLogin;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        inputEmail = (TextInputLayout) findViewById(R.id.input_log_email);
        inputPass = (TextInputLayout) findViewById(R.id.input_log_pass);
        btnLogin = (Button) findViewById(R.id.btn_log);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logEmail = inputEmail.getEditText().getText().toString().trim();
                String logPass = inputPass.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(logEmail) && !TextUtils.isEmpty(logPass)){
                    login(logEmail, logPass);
                }
            }
        });
    }

    private void login(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                    Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
