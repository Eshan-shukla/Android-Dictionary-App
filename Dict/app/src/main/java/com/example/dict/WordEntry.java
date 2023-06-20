package com.example.dict;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class WordEntry extends AppCompatActivity {
    Button insert,user;
    EditText name, cat, meaning;
    Word word;
    DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_entry);

        user = (Button) findViewById(R.id.user);
        insert = (Button) findViewById(R.id.insert);
        name = (EditText)findViewById(R.id.word);
        cat = (EditText)findViewById(R.id.category);
        meaning = (EditText)findViewById(R.id.meaning);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WordEntry.this, MainActivity.class);
                startActivity(intent);
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                String c = cat.getText().toString();
                String m = meaning.getText().toString();
                if((n.equals("")) || (c.equals("")) || (m.equals(""))){
                    Toast.makeText(WordEntry.this, "No Text field can be empty!", Toast.LENGTH_SHORT).show();
                }else{
                    dbr = FirebaseDatabase.getInstance().getReference("Words");
                    dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean flag = false;
                            Iterable<DataSnapshot> x = snapshot.getChildren();
                            for(Iterator<DataSnapshot> ds = x.iterator(); ds.hasNext();){
                                String wordname = ds.next().child("name").getValue().toString();
                                if(wordname.equals(n)){
                                    flag = true;
                                    Toast.makeText(WordEntry.this, "This word already exists in the dictionary.", Toast.LENGTH_SHORT).show();
                                    name.setText("");
                                    cat.setText("");
                                    meaning.setText("");
                                    break;
                                }
                            }
                            if(!flag){
                                word = new Word(n,c,m);
                                DBOperations.addToDB(word);
                                name.setText("");
                                cat.setText("");
                                meaning.setText("");
                                Toast.makeText(WordEntry.this, "Entered successfully into the database!", Toast.LENGTH_SHORT).show();
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
}