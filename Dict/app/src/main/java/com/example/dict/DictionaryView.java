package com.example.dict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class DictionaryView extends AppCompatActivity {
    private AutoCompleteTextView search;
    private ArrayList<String> words;
    private ListView listView;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_view);

        listView = (ListView)findViewById(R.id.list);
        search = (AutoCompleteTextView)findViewById(R.id.search);

        words = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Words");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String s = (String) snapshot.child("a").child("name").getValue();
                Iterable<DataSnapshot> it = snapshot.getChildren();
                for (Iterator<DataSnapshot> iter = it.iterator(); iter.hasNext(); ) {
                    String s = iter.next().child("name").getValue().toString();
                    words.add(s);
                }

                adapter = new ArrayAdapter<String>(DictionaryView.this, R.layout.activity_single_list_item,R.id.textView, words);
                search.setAdapter(adapter);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(MainActivity.this, words.get(i) +" ", Toast.LENGTH_SHORT).show();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Words");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snap) {
                                String word = adapter.getItem(i).toString();
                                String cat = "";
                                String mean = "";
                                Iterable<DataSnapshot> x = snap.getChildren();
                                for (Iterator<DataSnapshot> o = x.iterator(); o.hasNext(); ) {
                                    String s = o.next().child("name").getValue().toString();
                                    if(s.equals(word)){
                                        cat = snap.child(s).child("category").getValue().toString();
                                        mean = snap.child(s).child("meaning").getValue().toString();
                                        break;
                                    }

                                }
                                Intent intent = new Intent(DictionaryView.this, WordMeaning.class);
                                intent.putExtra("Word",word);
                                intent.putExtra("Category", cat);
                                intent.putExtra("Meaning",mean);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
}