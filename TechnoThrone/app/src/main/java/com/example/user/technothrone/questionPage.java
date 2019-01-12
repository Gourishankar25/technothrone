package com.example.user.technothrone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class questionPage extends AppCompatActivity {
    String teamNumber;
    DatabaseReference displayAns;
    DatabaseReference displayQuestion;
    DatabaseReference displayA;
    DatabaseReference displayB;
    DatabaseReference displayC;
    DatabaseReference displayD;
    TextView quetion;
    TextView optionA;
    TextView optionB;
    TextView optionC;
    TextView optionD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);
        teamNumber=getIntent().getExtras().getString("teamNo");
        quetion=findViewById(R.id.question);
        optionA=findViewById(R.id.optiona);
        optionB=findViewById(R.id.optionb);
        optionC=findViewById(R.id.optionc);
        optionD=findViewById(R.id.optiond);
        displayAns= FirebaseDatabase.getInstance().getReference().child("display");
        displayAns.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String qNo = dataSnapshot.getValue().toString();
                if (qNo.equals("null"))
                {
                    quetion.setText("battle ground is preparing");
                }
                else
                {
                   displayQuestion=FirebaseDatabase.getInstance().getReference().child("questions").child(qNo).child("q");
                   displayQuestion.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           String q = dataSnapshot.getValue().toString();
                           quetion.setText(q);
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
                   displayA=FirebaseDatabase.getInstance().getReference().child("questions").child(qNo).child("a");
                   displayA.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           String oA = dataSnapshot.getValue().toString();
                           optionA.setText(oA);

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
                    displayB=FirebaseDatabase.getInstance().getReference().child("questions").child(qNo).child("b");
                    displayB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String oB = dataSnapshot.getValue().toString();
                            //Toast.makeText(questionPage.this,oB,Toast.LENGTH_SHORT).show();
                            optionB.setText(oB);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    displayC=FirebaseDatabase.getInstance().getReference().child("questions").child(qNo).child("c");
                    displayC.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String oC = dataSnapshot.getValue().toString();
                            optionC.setText(oC);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    displayD=FirebaseDatabase.getInstance().getReference().child("questions").child(qNo).child("d");
                    displayD.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String oD = dataSnapshot.getValue().toString();
                            optionD.setText(oD);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
