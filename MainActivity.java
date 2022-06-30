package com.example.fingerprint_auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //acum ca avem layoutul facut, vom construii aplicatia si partea de user permision
        //selectez buton nostru si mesajul text
        TextView msg_txt = findViewById(R.id.txt_msg);
        Button login_btn = findViewById(R.id.login_btn);

        //creez biometric manager si verific daca userul poate folosi senzorul de finger print sau nu
        BiometricManager biometricManager = BiometricManager.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    msg_txt.setText("You can use the fingerprint sensor to login");
                    String colorString;
                    msg_txt.setTextColor(Color.parseColor( colorString: "#fafafa"));
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    msg_txt.setText("the device don't have fingerprint sensor");
                    login_btn.setVisibility(View.GONE);
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    msg_txt.setText("the biometric sensor is currently unavailable");
                    login_btn.setVisibility(View.GONE);
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    msg_txt.setText("your device don't have any fingerprint saved, please check your security settings");
                    login_btn.setVisibility(View.GONE);
                    break;
            }

            Context context;
            Executor executor = ContextCompat.getCodeCacheDir( context: this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this,executor, new BiometricPrompt.AuthenticationCallback()) {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(getApplicationContext(), text: "Login Success !", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            }};

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Login")
                    .setDescription("Use your fingerprint to login to your app")
                    .setNegativeButtonText("Text")
                    .build();

            login_btn.setOnClickListener(new View.OnClickListener() {
                private BiometricPrompt biometricPrompt;

                @Override
                public void onClick(View view) {
                    biometricPrompt.authenticate(promptInfo);
                }
            });


    }
}