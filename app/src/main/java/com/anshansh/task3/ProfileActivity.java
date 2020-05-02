package com.anshansh.task3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private String recieverUserId, currentStat, currentUserId;

    private TextView userProfileName;
    private Button sendMessageRequest;

    private DatabaseReference databaseReference, requestReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        requestReference = FirebaseDatabase.getInstance().getReference().child("Chat Requests");

        recieverUserId = getIntent().getExtras().get("visit_user_id").toString();
        currentUserId = mAuth.getCurrentUser().getUid();

        Toast.makeText(this, "UserID" + recieverUserId, Toast.LENGTH_SHORT).show();

        userProfileName = (TextView) findViewById(R.id.visit_username);

        sendMessageRequest = (Button) findViewById(R.id.send_request_button);

        currentStat = "new";

        RetriveUserInfo();
    }

    private void RetriveUserInfo() {
        databaseReference.child(recieverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String userName = dataSnapshot.child("name").getValue().toString();
                    userProfileName.setText(userName);

                    ManageChatRequest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void ManageChatRequest() {

        requestReference.child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(recieverUserId))
                        {

                            String request_type = dataSnapshot.child(recieverUserId).child("request_type").getValue().toString();

                            if (request_type.equals("sent"))
                            {
                                currentStat = "request_sent";
                                sendMessageRequest.setText("Cancel Request");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (!currentUserId.equals(recieverUserId))
        {
            sendMessageRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessageRequest.setEnabled(false);

                    if (currentStat.equals("new"))
                    {
                        SendChatRequest();
                    }

                }
            });
        }
        else
        {
            sendMessageRequest.setVisibility(View.INVISIBLE);
        }

    }

    private void SendChatRequest() {

        requestReference.child(currentUserId).child(recieverUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            requestReference.child(recieverUserId).child(currentUserId)
                                    .child("request_type").setValue("recieved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            sendMessageRequest.setEnabled(true);
                                            currentStat = "request_sent";
                                            sendMessageRequest.setText("Cancel Request");

                                        }
                                    });
                        }
                    }
                });
    }
}
