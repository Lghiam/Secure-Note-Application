package com.example.notepad.user_sign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.notepad.HomeActivity;
import com.example.notepad.MainActivity;
import com.example.notepad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button regBtn;
    private TextInputLayout inEmail, inPass;
    private FirebaseAuth fAuth;
    private DatabaseReference fUserDatabase;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regBtn = (Button) findViewById(R.id.regBtn);
        inEmail = (TextInputLayout) findViewById(R.id.input_reg_email);
        inPass = (TextInputLayout) findViewById(R.id.input_reg_pass);

        fAuth = FirebaseAuth.getInstance();
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");




        //REGISTER BUTTON FUNCTION
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uemail = inEmail.getEditText().getText().toString().trim();
                String upass = inPass.getEditText().getText().toString().trim();

                //Registry form validation
                if (!uemail.isEmpty() && !upass.isEmpty()) {
                    if (upass.length()>7){
                        registerUser(uemail, upass);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Password requires 8 or more characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }




    private void registerUser(final String email, String pass){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request");
        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    fUserDatabase.child(fAuth.getCurrentUser().getUid())
                            .child("details").child("email").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();

                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                //Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
