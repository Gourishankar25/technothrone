package com.example.user.technothrone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {

    DatabaseReference displayQuestion;
    EditText qno;
    DatabaseReference pot;
    int i;
    //long amnt[]=new long[10];
    ArrayList<String> amnt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);




    }

    public void pushQuestion(View v)
    {
        qno = findViewById(R.id.qno);
        displayQuestion = FirebaseDatabase.getInstance().getReference().child("display");

        String q =qno.getText().toString();
        displayQuestion.setValue(q);
    }

    public void evaluatePot(View v) {
      //  Toast.makeText(AdminPage.this,"fassak gouri",Toast.LENGTH_SHORT).show();

       pot = FirebaseDatabase.getInstance().getReference().child("pot");
        pot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getKey().equals("Total"))
                    amnt.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        long total =0;
        long max = 0;

        for(int i=0;i<amnt.size();i++)
        {
            long a= Integer.getInteger(amnt.get(i));
            if(max<a)
            {
                max = i;
            }
            total+= a;
        }
    }


}
