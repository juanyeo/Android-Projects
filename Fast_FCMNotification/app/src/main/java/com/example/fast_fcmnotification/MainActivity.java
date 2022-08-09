package com.example.fast_fcmnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView firebaseTokenTextView;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        firebaseTokenTextView = (TextView) findViewById(R.id.firebaseTokenTextView);

        initFirebase();
    }

    private void initFirebase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    firebaseTokenTextView.setText(token);
                }
            }
        });
    }
}