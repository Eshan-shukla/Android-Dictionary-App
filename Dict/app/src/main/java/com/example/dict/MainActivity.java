package com.example.dict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private Button login, signup, dict;
    private EditText n, p;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dict = (Button)findViewById(R.id.dict);
        signup = (Button)findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        n = (EditText)findViewById(R.id.name);
        p = (EditText)findViewById(R.id.password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = n.getText().toString();
                String password = p.getText().toString();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Accounts");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int k = 0;
                        boolean flag = false;
                        Iterable<DataSnapshot> x = snapshot.getChildren();
                        for(Iterator<DataSnapshot> ds = x.iterator(); ds.hasNext();  ){
                            String name = ds.next().child("username").getValue().toString();
                            if(username.equals(name)){
                                String pass = snapshot.child(name).child("password").getValue().toString();
                                try {
                                    String enPass = PassEncrypt.encryptPassword(password);
                                    if(enPass.equals(pass)){
                                        n.setText("");
                                        p.setText("");
                                        //if username and password both are correct allow the user to enter
                                        Intent intent = new Intent(MainActivity.this, WordEntry.class);
                                        startActivity(intent);

                                    }else{
                                        //if correct username but wrong password show error
                                        Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                        n.setText("");
                                        p.setText("");
                                    }

                                    flag = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                        //no username exists
                        if(!flag){
                            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            n.setText("");
                            p.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        dict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DictionaryView.class);
                startActivity(intent);

            }
        });
    }
}