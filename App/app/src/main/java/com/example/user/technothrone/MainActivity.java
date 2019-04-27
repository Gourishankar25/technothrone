package com.example.user.technothrone;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText teamNumber;
    EditText teamPassword;
    Button enterTeam;
    DatabaseReference teamPasswords;
    DatabaseReference check;
    String teamNo;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar appBar = getSupportActionBar();
        appBar.setTitle("TechnoThrone");

        teamNumber = findViewById(R.id.teamnumber);
        teamPassword=findViewById(R.id.teamPassword);
        enterTeam = findViewById(R.id.enterbutton);

        enterTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamNo = teamNumber.getText().toString();
                String password = teamPassword.getText().toString();

                if ( teamNo.equals("25") && password.equals("agha"))
                {
                        Intent i = new Intent(MainActivity.this,AdminPage.class);
                        i.putExtra("teamNo", teamNo);
                        startActivity(i);
                } else {

                    check = FirebaseDatabase.getInstance().getReference().child("user").child(teamNo).child("status");

                    check.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String s = "";
                            try {
                                s = dataSnapshot.getValue().toString();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "my lord check your key", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (s.equals("false")) {
                                teamPasswords = FirebaseDatabase.getInstance().getReference().child("user").child(teamNo).child("password");
                                teamPasswords.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String passWord = dataSnapshot.getValue().toString();
                                        pass = teamPassword.getText().toString().trim();
                                        // Toast.makeText(MainActivity.this,pass,Toast.LENGTH_SHORT).show();
                                        if (passWord.equals(pass)) {

                                            Toast.makeText(MainActivity.this, "my lord tech throne kingdom invites you", Toast.LENGTH_SHORT).show();
                                            check.setValue("true");
                                            Intent i = new Intent(MainActivity.this, questionPage.class);
                                            i.putExtra("teamNo", teamNo);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(MainActivity.this, "my lord insert correct key", Toast.LENGTH_SHORT).show();
                                             return;
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            } else {
                                teamNumber.setError("allready some person from your kingdom entered");
                                teamNumber.requestFocus();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
            }
            }
        });
    }
}
