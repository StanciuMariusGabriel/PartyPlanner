package com.example.dam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class RegisterActivity extends AppCompatActivity {
    private PartyVM viewModel;
    private EditText username;
    private EditText pass;
    private EditText confirmPass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewModel = new ViewModelProvider(this).get(PartyVM.class);

        username = findViewById(R.id.username_register);
        pass = findViewById(R.id.password_register);
        confirmPass = findViewById(R.id.confirm_password_register);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Username field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().equals("") || confirmPass.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Password fields can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().length() < 5 || username.getText().toString().length() < 5){
                    Toast.makeText(getApplicationContext(), "Username and password must be at least 5 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!username.getText().toString().matches("[a-zA-Z0-9]*")){
                    Toast.makeText(getApplicationContext(), "Username must contain only letters and numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.getText().toString().equals(confirmPass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords must match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(viewModel.addUser(username.getText().toString(), pass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Username taken", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
