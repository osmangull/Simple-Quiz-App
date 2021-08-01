package com.example.sanalkasifapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanalkasifapp.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView already_accont,sign_in;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db ;
    private Button loginBtn, registerBtn,forgetButton;
    private EditText registerMail,registerName,registerPhone,registerPassword,loginMail,loginPassword,forget_mail;
    private String registerMailString,registerNameString,registerPhoneString,registerPswString,loginMailString,loginPswString;
    private ProgressDialog progressDialog;
    private TextView forgot_password_btn;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);
        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        String mail = prefs.getString("mail", "");//"No name defined" is the default value.
        String psw = prefs.getString("psw", ""); //0 is the default value.
        already_accont = findViewById(R.id.already_accont);
        sign_in = findViewById(R.id.sign_in);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.registerButton);
        forgetButton = findViewById(R.id.sifirlaBtn);
        forgot_password_btn = findViewById(R.id.forgot_password_btn);
        registerMail = findViewById(R.id.mail);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        registerName = findViewById(R.id.name);
        registerPassword = findViewById(R.id.password);
        loginMail = findViewById(R.id.editTextEmail);
        loginPassword = findViewById(R.id.editTextPassword);
        forget_mail = findViewById(R.id.forgot_mail);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.GONE);
                forget_mail.setText("");
                findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                findViewById(R.id.registerScreen).setVisibility(View.GONE);
                findViewById(R.id.forgetPassScreen).setVisibility(View.GONE);
            }
        });

        forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.VISIBLE);
                findViewById(R.id.loginScreen).setVisibility(View.GONE);
                findViewById(R.id.registerScreen).setVisibility(View.GONE);
                findViewById(R.id.forgetPassScreen).setVisibility(View.VISIBLE);
            }
        });
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword(forget_mail.getText().toString().trim());
            }
        });

        currentUser = mAuth.getCurrentUser();

        already_accont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loginScreen).setVisibility(View.GONE);
                findViewById(R.id.registerScreen).setVisibility(View.VISIBLE);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                findViewById(R.id.registerScreen).setVisibility(View.GONE);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                registerUser();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginMailString = loginMail.getText().toString().trim();
                loginPswString = loginPassword.getText().toString().trim();
                userLogin(loginMailString,loginPswString);
            }
        });

        if (!mail.isEmpty() && !psw.isEmpty()){
            userLogin(mail,psw);
        }


    }


    private void userLogin(String mail,String psw){
        try {
            if (mAuth!= null && !mAuth.getCurrentUser().equals(null)){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }
        catch (Exception e ){}
        if (mail.isEmpty()){
            Toast.makeText(LoginActivity.this, "Mail adresi boş bırakılamaz", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(psw.isEmpty()){
            Toast.makeText(LoginActivity.this, "Şifre boş bırakılamaz", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(LoginActivity.this, "Mail adresi hatalı", Toast.LENGTH_SHORT).show();
        }

        else {
            mAuth.signInWithEmailAndPassword(mail, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.setMessage("Giriş başarılı yönlendiriliyorsunuz");
                        if (!progressDialog.isShowing())
                        progressDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor editor = getSharedPreferences("LoginPref", MODE_PRIVATE).edit();
                                editor.putString("mail", mail);
                                editor.putString("psw", psw);
                                editor.apply();
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 1500);
                    }
                }
            });
        }
    }

    private void forgetPassword(String mail){
        if (mail.isEmpty()){
            Toast.makeText(LoginActivity.this, "Mail adresi boş bırakılamaz", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(LoginActivity.this, "Mail adresi hatalı", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(LoginActivity.this, "Şifre sıfırlama maili gönderildi", Toast.LENGTH_SHORT).show();
            forget_mail.setText("");
            findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
            findViewById(R.id.registerScreen).setVisibility(View.GONE);
            findViewById(R.id.forgetPassScreen).setVisibility(View.GONE);
            mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                    }
                }

            });
        }
    }
    private void registerUser(){
        progressDialog.setMessage("Kullanıcı oluşturuluyor");
        progressDialog.show();
        registerMailString = registerMail.getText().toString();
        registerNameString = registerName.getText().toString();
        registerPswString = registerPassword.getText().toString();

        if (registerMailString.equals("")) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Mail adresi boş bırakılamaz", Toast.LENGTH_SHORT).show();
            return;
        } else if (registerNameString.equals("")) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Kullanıcı adı boş bırakılamaz", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(registerMailString).matches()) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Mail adresi hatalı", Toast.LENGTH_SHORT).show();
        } else if (registerPswString.replace(" ", "").equals("") || registerPswString.length() < 6) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Şifre alanı boş veya hatalı bir şifre girişi yaptınız", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(registerMailString, registerPswString).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Yetkilendirme Hatası",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", registerNameString);
                        user.put("mail", registerMailString);


                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Kullanıcı oluşturuldu", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();

                                    }
                                });

                        registerMail.setText("");
                        registerName.setText("");
                        registerPassword.setText("");
                        findViewById(R.id.loginScreen).setVisibility(View.VISIBLE);
                        findViewById(R.id.registerScreen).setVisibility(View.GONE);

                    }
                }
            });
        }
    }

}