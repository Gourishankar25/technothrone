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

    ArrayList<teamBalance> arr = new ArrayList<>();
    int i=0;
    int c;
    int tot;
    String maxi = "";
    int max = -9999;

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
        i=0;

        currentBalance = FirebaseDatabase.getInstance().getReference().child("user");
        currentBalance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String str = dataSnapshot.getKey();
                int bal = Integer.valueOf(dataSnapshot.child("balance").getValue().toString());
                Log.i("abc", "onChildAdded: "+str+" bal:"+bal);
                arr.add(new teamBalance(str,bal));
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

       pot = FirebaseDatabase.getInstance().getReference().child("pot");
        pot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getKey().equals("Total"))
                {
                    int t = Integer.valueOf(dataSnapshot.getValue().toString());
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

    }

    public void addamount(View view)
    {
        check=false;


        //Toast.makeText(AdminPage.this,"winner"+maxi,Toast.LENGTH_SHORT).show();

        winner=FirebaseDatabase.getInstance().getReference().child("user").child(maxi).child("balance");
        winner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!check)
                    {
                        Log.i("abc", "addamount ,,, winner is : "+maxi);

                        String s=dataSnapshot.getValue().toString();
                        Log.i("abc", "addamount ,,, winner is :"+dataSnapshot.getValue());
                        Log.i("abc", "onDataChange: "+s);
                        c=Integer.valueOf(s);
                        c= c + tot;
                        Log.i("abc", "onDataChange c is: "+c);
                        check=true;

                        winner.setValue(c);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Collections.sort(arr,new SortByBalance());

         for(int i=0;i<10;i++)
         {
             currentBalance = FirebaseDatabase.getInstance().getReference().child("user");
             currentBalance.child(arr.get(i).team).child("position").setValue(10-i);
         }

        maxi="";
    }
}