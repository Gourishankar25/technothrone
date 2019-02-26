package com.example.user.technothrone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPage extends AppCompatActivity {

    DatabaseReference displayQuestion;
    EditText qno;

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

    public void evaluatePot(View v)
    {

    }
}
