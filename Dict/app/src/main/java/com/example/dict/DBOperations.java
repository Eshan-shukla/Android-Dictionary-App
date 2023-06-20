package com.example.dict;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class DBOperations {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    //add data to  database
    public static void addToDB(Word word) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Words");
        databaseReference.child(word.getName()).setValue(word);
    }

    public static void addToUserDB(Account account) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");
        databaseReference.child(account.getUsername()).setValue(account);

    }
}

