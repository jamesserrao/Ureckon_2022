package com.sreesubh.ureckon2022;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    SignInButton g_signin;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //g_signin = findViewById(R.id.g_signin);
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_2)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,signInOptions);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(user!=null)
        {
            startActivity(new Intent(LoginActivity.this,MainDashedBoard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    private void signIn()
    {
        Intent signinIntent = googleSignInClient.getSignInIntent();
        signinactivity.launch(signinIntent);
    }

    ActivityResultLauncher<Intent> signinactivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                        if (task.isSuccessful())
                        {
                            try {
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                try {
                                    AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                                    Log.d("AuthID",account.getIdToken());
                                    firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful())
                                            {
                                                startActivity(new Intent(LoginActivity.this,MainDashedBoard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else{
                                                Log.d("API 1","Error-> "+task.getException());
                                                Toast.makeText(LoginActivity.this, "Auth Fail!!-> "+task.getException(), Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                                }catch (Exception e)
                                {
                                }
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
}