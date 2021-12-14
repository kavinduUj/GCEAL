package com.appflavorsz.gceal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appflavorsz.gceal.R;
import com.appflavorsz.gceal.service.other.AlerdDialog;
import com.appflavorsz.gceal.service.other.SharePref;
import com.appflavorsz.gceal.service.other.Waiting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LoginRegister extends AppCompatActivity {

    private Button loginbtn;
    TextView register,registe002;
    private EditText fName,lName,email,phone,password,comPass;
    private RadioGroup maleFemale;
    String male,female,btnVal;
    int selectedId;
    RadioButton radioButton;
    AlerdDialog alerdDialog;

    Waiting waiting;
    boolean loading = false;
    Date currentTime;

    Dialog loginDialog,registerDialog,forgetPassword;

    private FirebaseAuth mAuth;
    private SharePref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        mainVariableCasting();
        mAuth = FirebaseAuth.getInstance();
        currentTime = Calendar.getInstance().getTime();
        Log.i("currentTime", String.valueOf(currentTime));
        pref = new SharePref(LoginRegister.this);




        loginbtn.setOnClickListener((View v)->{


            loginDialog.setContentView(R.layout.login_page);
            loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loginDialog.show();

            EditText email = (EditText)loginDialog.findViewById(R.id.loginMail);
            EditText pass = (EditText)loginDialog.findViewById(R.id.loginPass);
            Button loginButton = (Button)loginDialog.findViewById(R.id.loginButton);
            TextView forgetPass = (TextView)loginDialog.findViewById(R.id.forgetPassword);

            loginButton.setOnClickListener((View v1)->{

                email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                            email.setBackgroundResource(R.drawable.text_box);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        pass.setBackgroundResource(R.drawable.text_box);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                if (email.getText().toString().isEmpty()){
                    email.setBackgroundResource(R.drawable.err_text);
                }else if (pass.getText().toString().isEmpty()){
                    pass.setBackgroundResource(R.drawable.err_text);
                }else {

                String lMail = email.getText().toString();
                String lPass = pass.getText().toString();
                userLogin(lMail,lPass);
                }
            });

            forgetPass.setOnClickListener((View v2)->{

                forgetPassword.setContentView(R.layout.password_recover);
                forgetPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                forgetPassword.show();
                loginDialog.dismiss();

                EditText forget = (EditText)forgetPassword.findViewById(R.id.forgetEmail);
                Button sendEmail = (Button)forgetPassword.findViewById(R.id.sendEmail);
                Button cancel = (Button) forgetPassword.findViewById(R.id.forgetCancel);

                cancel.setOnClickListener((View v3)->{

                    forgetPassword.dismiss();
                });

                sendEmail.setOnClickListener((View v4)->{
                    forget.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            forget.setBackgroundResource(R.drawable.text_box);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    if (forget.getText().toString().isEmpty()){
                        forget.setBackgroundResource(R.drawable.err_text);
                    }else {
                    String getMail = forget.getText().toString();
                    passwordRecocer(getMail);
                    }

                });



            });

        });
        registe002.setOnClickListener((View v)->{
            registerDialog.setContentView(R.layout.regidter_page);
            registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            registerDialog.setCancelable(false);
            registerDialog.show();

            Button register = (Button)registerDialog.findViewById(R.id.registerButton);
            ImageView back = (ImageView)registerDialog.findViewById(R.id.registerBack);
            registeDialogVariable();


            back.setOnClickListener((View view)->{
                registerDialog.dismiss();
            });

            register.setOnClickListener((View view00)->{

                registerFieldCheck();
            });
        });
        register.setOnClickListener((View view)->{
            registerDialog.setContentView(R.layout.regidter_page);
            registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            registerDialog.setCancelable(false);
            registerDialog.show();

            Button register = (Button)registerDialog.findViewById(R.id.registerButton);
            ImageView back = (ImageView)registerDialog.findViewById(R.id.registerBack);
            registeDialogVariable();





            back.setOnClickListener((View view1)->{
                registerDialog.dismiss();
            });

            register.setOnClickListener((View view0)->{

                registerFieldCheck();


            });
        });

        if (pref.readStatus()){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    // Functions
    private void passwordRecocer(String getMail) {

        if (!isLoading()){
            showProcessDialog();
        }
        mAuth.sendPasswordResetEmail(getMail) .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                }else {

//                    alerdDialog.openD(LoginRegister.this,"Warning","Something is wrong.please try again later",R.drawable.ic_baseline_warning_24);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if (isLoading()){
                            goneProcessDialog();
                        }
                        String getErr = e.getMessage();
                        alerdDialog.openD(LoginRegister.this,"Warning",getErr,R.drawable.ic_baseline_warning_24);

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (isLoading()){
                    goneProcessDialog();
                }
                forgetPassword.dismiss();
                alerdDialog.openD(LoginRegister.this,"Email sent!","Please check your mail and change your password",R.drawable.ic_baseline_check_circle_24);

            }
        });
    }
    private void userLogin(String lMail, String lPass) {

        if(!isLoading()){
            showProcessDialog();
        }
        mAuth.signInWithEmailAndPassword(lMail, lPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isLoading()){
                                goneProcessDialog();
                            }
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginRegister.this, HomeActivity.class);
                            loginDialog.dismiss();
                            pref.wireLogStatus(true);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            LoginRegister.this.finish();
                            startActivity(i);


                        } else {

//                            alerdDialog.openD(LoginRegister.this,"Warning","Something is wrong.please try again later",R.drawable.ic_baseline_warning_24);


                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (isLoading()){
                    goneProcessDialog();
                }
                String getErr = e.getMessage();
                alerdDialog.openD(LoginRegister.this,"Warning",getErr,R.drawable.ic_baseline_warning_24);


            }
        });
    }
    private void userRegister(String email0, String password0) {

        if (!isLoading()){
            showProcessDialog();
        }
        selectedId =maleFemale.getCheckedRadioButtonId();
        radioButton = (RadioButton)registerDialog.findViewById(selectedId);
        btnVal = radioButton.getText().toString();

        mAuth.createUserWithEmailAndPassword(email0, password0).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();


                            String email = user.getEmail();
                            String uID = user.getUid();
                            String pass = password.getText().toString();
                            String mobi = phone.getText().toString();


                            HashMap<Object,String> map = new HashMap<>();
                            map.put("fName",fName.getText().toString());
                            map.put("lName",lName.getText().toString());
                            map.put("email",email);
                            map.put("password",pass);
                            map.put("phone",mobi);
                            map.put("proUrl","");
                            map.put("isActive","0");
                            map.put("bio","");
                            map.put("gender",btnVal);
                            map.put("dateAndTime", String.valueOf(currentTime));
                            map.put("uId",uID);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("users");
                            reference.child(uID).setValue(map);



                        } else {

//                            alerdDialog.openD(LoginRegister.this,"Warning","Something is wrong.please try again later",R.drawable.ic_baseline_warning_24);

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (isLoading()){
                    goneProcessDialog();
                }
                String getErr = e.getMessage();
                alerdDialog.openD(LoginRegister.this,"Warning",getErr,R.drawable.ic_baseline_warning_24);

            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                if (isLoading()){
                    goneProcessDialog();
                }
                registerDialog.dismiss();
                new AlertDialog.Builder(LoginRegister.this)
                        .setTitle("Congratulations! ")
                        .setMessage("Account registration successful")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_baseline_check_circle_24)
                        .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent ii = new Intent(LoginRegister.this,HomeActivity.class);
                                pref.wireLogStatus(true);
                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ii);
                                LoginRegister.this.finish();
                            }
                        })
                        .show();



            }
        });

    }
    private void registerFieldCheck(){
        fName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                fName.setBackgroundResource(R.drawable.text_box);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                lName.setBackgroundResource(R.drawable.text_box);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                email.setBackgroundResource(R.drawable.text_box);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                phone.setBackgroundResource(R.drawable.text_box);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                password.setBackgroundResource(R.drawable.text_box);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        comPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                comPass.setBackgroundResource(R.drawable.text_box);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (fName.getText().toString().isEmpty()){
            fName.setBackgroundResource(R.drawable.err_text);
        }
        else if (lName.getText().toString().isEmpty()){
            lName.setBackgroundResource(R.drawable.err_text);
        }
        else if (email.getText().toString().isEmpty()){
            email.setBackgroundResource(R.drawable.err_text);
        }
        else if (phone.getText().toString().isEmpty()){
            phone.setBackgroundResource(R.drawable.err_text);
        }
        else if (password.getText().toString().isEmpty()){
            password.setBackgroundResource(R.drawable.err_text);
        }
        else if (password.getText().toString().length()<6){
            Toast.makeText(LoginRegister.this,"Please enter more than 6 character",Toast.LENGTH_SHORT).show();
        }
        else if (comPass.getText().toString().isEmpty()){
            comPass.setBackgroundResource(R.drawable.err_text);
        }
        else if (!password.getText().toString().equals(comPass.getText().toString())){
            Toast.makeText(LoginRegister.this,"Password does'n match",Toast.LENGTH_SHORT).show();

        }
        else {
            String email0 = email.getText().toString();
            String password0 = password.getText().toString();
            userRegister(email0, password0);
        }
    }


    //Variable Casting
    private void mainVariableCasting() {

        loginbtn = (Button)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);
        registe002 = (TextView)findViewById(R.id.register01);

        loginDialog=new Dialog(this);
        registerDialog=new Dialog(this);
        forgetPassword = new Dialog(this);
        waiting = new Waiting(LoginRegister.this);
        alerdDialog = new AlerdDialog();


    }
    public void registeDialogVariable(){

        email = (EditText)registerDialog.findViewById(R.id.rMail);
        password = (EditText)registerDialog.findViewById(R.id.rPassword);
        fName =(EditText)registerDialog.findViewById(R.id.rFname);
        lName = (EditText)registerDialog.findViewById(R.id.rLname);
        phone = (EditText)registerDialog.findViewById(R.id.rPhone);
        comPass = (EditText)registerDialog.findViewById(R.id.ePasswordReType);
        fName =(EditText)registerDialog.findViewById(R.id.rFname);
        lName = (EditText)registerDialog.findViewById(R.id.rLname);
        phone = (EditText)registerDialog.findViewById(R.id.rPhone);
        comPass = (EditText)registerDialog.findViewById(R.id.ePasswordReType);
        maleFemale = (RadioGroup)registerDialog.findViewById(R.id.gender);


    }


    //Process Dialog(Loading)
    public void showProcessDialog(){
        loading = true;
        waiting.loadingStart();
    }
    public void goneProcessDialog(){
        loading = false;
        waiting.loadingGone();
    }
    public boolean isLoading(){
        return loading;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        registerDialog.dismiss();
    }
}