package com.example.user.technothrone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    TextView balance;
    TextView currentPosition;
    EditText Bid;
    Boolean Submitted;
    private RadioGroup mFirstGroup;
    private RadioGroup mSecondGroup;

    int Balance = 3000;
    int Position = 1;

    String Option ="";
    int selected = 0;
    int bidAmnt;
    private boolean isChecking = true;
    private int mCheckedId = R.id.optiona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);
        androidx.appcompat.app.ActionBar appBar = getSupportActionBar();
        appBar.hide();
        Submitted=false;
        teamNumber=getIntent().getExtras().getString("teamNo");
        quetion=findViewById(R.id.question);

        optionA=findViewById(R.id.optiona);
        optionB=findViewById(R.id.optionb);
        optionC=findViewById(R.id.optionc);
        optionD=findViewById(R.id.optiond);
        Bid = findViewById(R.id.bidamt);

        balance = findViewById(R.id.balance);
        currentPosition = findViewById(R.id.position);

        mFirstGroup = (RadioGroup) findViewById(R.id.opt1);
        mSecondGroup = (RadioGroup) findViewById(R.id.opt2);

        loadBalancePosition(Balance,Position);

        mFirstGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    mSecondGroup.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
                selected = mFirstGroup.getCheckedRadioButtonId();
            }
        });

        mSecondGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    mFirstGroup.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
                selected = mSecondGroup.getCheckedRadioButtonId();
            }
        });


        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RadioButton btn = findViewById(selected);

                if (Balance > 0 ) {

                    if (btn == null) {
                        Toast.makeText(questionPage.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    } else if (Bid.getText().toString().equals("")) {
                        Toast.makeText(questionPage.this, "Please select the bid amount", Toast.LENGTH_SHORT).show();
                    } else {
                        if(!Submitted) {
                            Option = btn.getText().toString();
                            String bidamt = Bid.getText().toString();
                            bidAmnt = Integer.valueOf(bidamt);
                            bid();
                        }
                        else{
                            Toast.makeText(questionPage.this, "Question can nly b submitted once", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(questionPage.this, "Your Balance is exhausted", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    void bid()
    {
        Balance  = Balance - bidAmnt;
        Submitted=true;
        loadBalancePosition(Balance,Position);
        Toast.makeText(questionPage.this,"Option Selected is:"+Option+" The bid Amount is:"+bidAmnt,Toast.LENGTH_SHORT).show();
    }

    void loadBalancePosition(int bal,int pos)
    {
        balance.setText(String.valueOf(bal));
        currentPosition.setText(String.valueOf(pos));
    }

}
