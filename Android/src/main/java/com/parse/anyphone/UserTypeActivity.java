package com.parse.anyphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class UserTypeActivity extends AppCompatActivity {

    private Button customerButton;
    private Button photographerButton;
    private Button floristButton;
    private Button artistButton;
    private Button logInButton;
    UserTypeButtonHandler userTypeButtonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select User Type");

        logInButton = (Button) findViewById(R.id.button5);
        customerButton = (Button) findViewById(R.id.button);
        photographerButton = (Button) findViewById(R.id.button2);
        floristButton = (Button) findViewById(R.id.button3);
        artistButton = (Button) findViewById(R.id.button4);
        userTypeButtonHandler = new UserTypeButtonHandler();

        logInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UserTypeActivity.this, LoginActivity.class);
                Button b = (Button) v;
                String buttonText = b.getText().toString();
                i.putExtra("USER_TYPE", buttonText);
                startActivity(i);
            }
        });
        customerButton.setOnClickListener(userTypeButtonHandler);
        photographerButton.setOnClickListener(userTypeButtonHandler);
        floristButton.setOnClickListener(userTypeButtonHandler);
        artistButton.setOnClickListener(userTypeButtonHandler);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    class UserTypeButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(UserTypeActivity.this, SignUpActivity.class);
            Button b = (Button) v;
            String buttonText = b.getText().toString();
            i.putExtra("USER_TYPE", buttonText);
            startActivity(i);
        }
    }

}
