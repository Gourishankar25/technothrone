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

import static java.lang.Math.abs;

public class AdminPage extends AppCompatActivity {

    DatabaseReference displayQuestion;
    EditText qno;
    DatabaseReference pot;
    DatabaseReference winner;
    long c;
   // ArrayList<String> amnt = new ArrayList<>();
    long tot;
    String maxi = "";
     long max = -9999;
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
                {
                    long t = Integer.valueOf(dataSnapshot.getValue().toString());
                    if(max<t)
                    {
                        max = t;
                        maxi = dataSnapshot.getKey();
                    }
                    tot+=abs(Integer.valueOf(dataSnapshot.getValue().toString()));
                    pot.child("Total").setValue(tot);
                }
                //amnt.add(dataSnapshot.getValue().toString());
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
        tot=0;



       /* long total =0;
        long max = 0;

        for(int i=0;i<amnt.size();i++)
        {
            long a= Integer.getInteger(amnt.get(i));
            if(max<a)
            {
                max = i;
            }
            total+= a;
        } */


    }
    public void addamount(View view)
    {
        if(!maxi.equals(""))
        {
            winner=FirebaseDatabase.getInstance().getReference().child("user").child(maxi).child("balance");
            winner.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String a=dataSnapshot.getValue().toString();
                    c=Integer.valueOf(a);
                    c+=tot;
                    winner.setValue(c);

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
        }
        maxi="";
    }


}
