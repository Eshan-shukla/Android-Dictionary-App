package com.example.dict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class Signup extends AppCompatActivity {
    private Button register;
    private EditText name, pass, rePass;
    private Account account;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        register = (Button)findViewById(R.id.register);

        name = (EditText)findViewById(R.id.name);
        pass = (EditText)findViewById(R.id.password);
        rePass = (EditText) findViewById(R.id.RePassword);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //store all the edit text values in the variable
                String n = name.getText().toString();
                String p = pass.getText().toString();
                String pr = rePass.getText().toString();
                //String encryptedPass = "";

                if ((n.equals("")) || (p.equals("")) || (pr.equals(""))) {
                    Toast.makeText(Signup.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {

                    //check whether username exists in the database
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Accounts");

                    //traverse along the database node to check whether username exists
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = false;
                            Iterable<DataSnapshot> x = snapshot.getChildren();
                            for (Iterator<DataSnapshot> ds = x.iterator(); ds.hasNext(); ) {
                                String name = ds.next().child("username").getValue().toString();
                                //if name in the database is same as n then make flag true
                                if (name.equals(n)) {
                                    flag = true;
                                }
                            }
                            //no username of String n exists in the database
                            if (!flag) {
                                //if username is unique check password matching
                                if (checkPasswordMatch(p, pr)) {
                                    //check length of the password
                                    if (checkLength(p)) {
                                        String encryptedPass = "";
                                        try {
                                            encryptedPass = PassEncrypt.encryptPassword(p);
                                            Log.v("password",encryptedPass);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //add to the database
                                        account = new Account(n, encryptedPass);
                                        DBOperations.addToUserDB(account);
                                        Toast.makeText(Signup.this, "Registered successfully!", Toast.LENGTH_SHORT).show();

                                        //change the screen to logIn screen
                                        Intent intent = new Intent(Signup.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Signup.this, R.string.error_length, Toast.LENGTH_SHORT).show();
                                        //erLength.setText(R.string.error_length);
                                        pass.setText("");
                                        rePass.setText("");
                                    }
                                } else {
                                    Toast.makeText(Signup.this, R.string.error_pass, Toast.LENGTH_SHORT).show();
                                    //erPass.setText(R.string.error_pass);
                                    pass.setText("");
                                    rePass.setText("");
                                }

                            } else {
                                //Toast.makeText(Signup.this, R.string.error_name, Toast.LENGTH_SHORT).show();
                                //erName.setText(R.string.error_name);
                                name.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });

    }

    private boolean checkPasswordMatch(String pass, String rePass){
        if(pass.equals(rePass)){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkLength(String pass){
        if(pass.length() > 9){
            return true;
        }
        else{
            return false;
        }
    }
}
