package com.example.user.technothrone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Path;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    DatabaseReference answer;
    DatabaseReference pot;
    DatabaseReference balanceofteam;
    DatabaseReference positionofteam;
    TextView quetion;
    TextView optionA;
    TextView optionB;
    TextView optionC;
    TextView optionD;
    TextView balance;
    TextView currentPosition;
    EditText Bid;
    private RadioGroup mFirstGroup;
    private RadioGroup mSecondGroup;
    Boolean submitedans;
    String qNo;
    int flag=0;
    TextView mTimer;
    int Balance;
    int Position;

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

        teamNumber=getIntent().getExtras().getString("teamNo");
        quetion=findViewById(R.id.question);

        optionA=findViewById(R.id.optiona);
        optionB=findViewById(R.id.optionb);
        optionC=findViewById(R.id.optionc);
        optionD=findViewById(R.id.optiond);
        Bid = findViewById(R.id.bidamt);
        submitedans=false;
        balance = findViewById(R.id.balance);
        currentPosition = findViewById(R.id.position);

        mFirstGroup = (RadioGroup) findViewById(R.id.opt1);
        mSecondGroup = (RadioGroup) findViewById(R.id.opt2);
        balanceofteam=FirebaseDatabase.getInstance().getReference().child("user").child(teamNumber).child("balance");
        balanceofteam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Balance=Integer.valueOf(dataSnapshot.getValue().toString());

                loadBalancePosition(Balance,Position);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        positionofteam=FirebaseDatabase.getInstance().getReference().child("user").child(teamNumber).child("position");
        positionofteam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Position=Integer.valueOf(dataSnapshot.getValue().toString());
                loadBalancePosition(Balance,Position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // loading balance and position in app
       // loadBalancePosition(Balance,Position);

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

        mTimer = (TextView) findViewById(R.id.time);
        CountDownTimer Timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimer.setText((millisUntilFinished / 1000 )+" s");
            }

            public void onFinish() {
                mTimer.setText("Time Up!!");
                submitedans = true;
            }
        }.start();

        Button submit = (Button) findViewById(R.id.submit);
        // when submit button is clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RadioButton btn = findViewById(selected);

                if(btn==null)
                {
                    // btn is null if no option is selected and gives toast
                    Toast.makeText(questionPage.this,"Please select an option",Toast.LENGTH_SHORT).show();
                }
                else {
                    String bidamt = Bid.getText().toString();
                    if (bidamt.equals("")) {
                        // bid is null if bid amount is null
                        Toast.makeText(questionPage.this, "Please select the bid amount", Toast.LENGTH_SHORT).show();
                    } else {
                        if (submitedans == false) {
                            //String bidamt = Bid.getText().toString();
                           // Toast.makeText(questionPage.this, "value of bidamt"+bidamt, Toast.LENGTH_SHORT).show();
                            Option = btn.getText().toString();
                           // String bidamt = Bid.getText().toString();
                            bidAmnt = Integer.valueOf(bidamt);
                            submitedans = true;
                            bid();
                        } else {
                            Toast.makeText(questionPage.this, "you already have choosen option", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    displayAns= FirebaseDatabase.getInstance().getReference().child("display");
    displayAns.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qNo = dataSnapshot.getValue().toString();
                if (qNo.equals("null"))
                {
                    // when database display value is null
                    quetion.setText("battle ground is preparing");
                }
                else
                {
                    // database value is not equal to null and equal to some question number
                    Bid.setText("");
                    submitedans=false;
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
                    // displaying option a
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
                        // displaying option b
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
                        //displaying option c
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
                    //displaying option d
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
        balanceofteam=FirebaseDatabase.getInstance().getReference().child("user").child(teamNumber).child("balance");
        balanceofteam.setValue(Balance);
        //loadBalancePosition(Balance,Position);
        answer=FirebaseDatabase.getInstance().getReference().child("questions").child(qNo).child("ans");
        answer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String cans= dataSnapshot.getValue().toString();
                if(Option.equals(cans))
                {
                    Toast.makeText(questionPage.this,"Option Selected is right:"+Option+" The bid Amount is:"+bidAmnt,Toast.LENGTH_SHORT).show();
                    pot=FirebaseDatabase.getInstance().getReference().child("pot").child(teamNumber);
                    pot.setValue(bidAmnt);
                }
                else
                {
                    Toast.makeText(questionPage.this,"Option Selected is wrong:"+Option+" The bid Amount is:"+bidAmnt,Toast.LENGTH_SHORT).show();
                    pot=FirebaseDatabase.getInstance().getReference().child("pot").child(teamNumber);
                    pot.setValue(-1*bidAmnt);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // Toast.makeText(questionPage.this,"Option Selected is:"+Option+" The bid Amount is:"+bidAmnt,Toast.LENGTH_SHORT).show();
    }

    void loadBalancePosition(int bal,int pos)
    {
        balance.setText(String.valueOf(bal));
        currentPosition.setText(String.valueOf(pos));
    }

}
