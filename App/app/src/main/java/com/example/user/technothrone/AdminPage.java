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
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

class teamBalance
{
    String team;
    int balance;

    public teamBalance(String t,int b)
    {
        team = t;
        balance =b;
    }
}

class SortByBalance implements Comparator<teamBalance>
{
    public int compare(teamBalance a,teamBalance b)
    {
        return (a.balance - b.balance);
    }
}

public class AdminPage extends AppCompatActivity {

    DatabaseReference displayQuestion;
    EditText qno;
    DatabaseReference pot;
    DatabaseReference winner;
    DatabaseReference currentBalance;
    Boolean check;
    String maxi="";
    ArrayList<String> st;
    ArrayList<teamBalance> arr ;
    ArrayList<teamBalance> potbal;
    int c,tot;
    int n;
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
        tot=0;
        potbal = new ArrayList<>();

        pot = FirebaseDatabase.getInstance().getReference().child("pot");
        pot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getKey().equals("Total"))
                {
                    int t = Integer.valueOf(dataSnapshot.getValue().toString());
                    potbal.add(new teamBalance(dataSnapshot.getKey(),t));
                    tot+=abs(Integer.valueOf(dataSnapshot.getValue().toString()));
                    pot.child("Total").setValue(tot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public void addamount(View view)
    {
        st = new ArrayList<>();
        arr = new ArrayList<>();

        n = potbal.size();
        Collections.sort(potbal,new SortByBalance());

        maxi = potbal.get(n-1).team;

        for(int i=0;i<n;i++)
        {
            if(potbal.get(n-i-1).balance == potbal.get(n-1).balance)
            {
                maxi=potbal.get(n-i-1).team;
                st.add(maxi);
            }
            else{ break; }
        }

        int count=st.size();
        check=false;
        tot = tot/count;

        Log.i("abc", "value of tot is: "+tot+" and st is "+st);

        winner = FirebaseDatabase.getInstance().getReference().child("user");
        winner.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String t = dataSnapshot.child("balance").getValue().toString();
                c = Integer.valueOf(t);

                for (String str:st){
                    if(str.equals(dataSnapshot.getKey())) {
                        c += tot;
                        check = true;
                        winner.child(str).child("balance").setValue(c);
                    }
                }
                arr.add(new teamBalance(dataSnapshot.getKey(),c));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        maxi="";
    }

    public void updatePositions(View v)
    {
        Collections.sort(arr,new SortByBalance());

        for(int i=0;i<n;i++)
        {
            currentBalance = FirebaseDatabase.getInstance().getReference().child("user");
            currentBalance.child(arr.get(i).team).child("position").setValue(n-i);
        }

    }
}