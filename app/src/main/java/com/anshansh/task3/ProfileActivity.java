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
    private Button sendMessageRequest, cancelRequest;

    private DatabaseReference databaseReference, requestReference, contactsReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        requestReference = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        contactsReference = FirebaseDatabase.getInstance().getReference().child("Contacts");

        recieverUserId = getIntent().getExtras().get("visit_user_id").toString();
        currentUserId = mAuth.getCurrentUser().getUid();

        Toast.makeText(this, "UserID" + recieverUserId, Toast.LENGTH_SHORT).show();

        userProfileName = (TextView) findViewById(R.id.visit_username);

        sendMessageRequest = (Button) findViewById(R.id.send_request_button);
        cancelRequest = (Button) findViewById(R.id.cancel_request_button);


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
                            else if (request_type.equals("recieved"))
                            {
                                currentStat = "request_recieved";
                                sendMessageRequest.setText("Accept Chat Request");

                                cancelRequest.setVisibility(View.VISIBLE);

                                cancelRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelChatRequest();
                                    }
                                });


                            }

                        }
                        else
                        {
                            contactsReference.child(currentUserId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(recieverUserId))
                                            {
                                                currentStat = "friends";
                                                sendMessageRequest.setText("Remove Contact");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
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

                    if (currentStat.equals("request_sent"))
                    {
                        CancelChatRequest();
                    }

                    if (currentStat.equals("request_recieved"))
                    {
                        AcceptChatRequest();
                    }
                    if (currentStat.equals("friends"))
                    {
                        RemoveSpecificContact();
                    }

                }
            });
        }
        else
        {
            sendMessageRequest.setVisibility(View.INVISIBLE);
        }

    }

    private void RemoveSpecificContact() {


        contactsReference.child(currentUserId).child(recieverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            contactsReference.child(recieverUserId).child(currentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                sendMessageRequest.setEnabled(true);
                                                currentStat = "new";
                                                sendMessageRequest.setText("Send Message");

                                                cancelRequest.setVisibility(View.INVISIBLE);
                                                cancelRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }

                    }
                });

    }

    private void AcceptChatRequest() {

        contactsReference.child(currentUserId).child(recieverUserId)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            contactsReference.child(recieverUserId).child(currentUserId)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task){
                                            if (task.isSuccessful())
                                            {
                                                requestReference.child(currentUserId).child(recieverUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                 if (task.isSuccessful())
                                                                 {
                                                                     requestReference.child(recieverUserId).child(currentUserId)
                                                                             .removeValue()
                                                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                 @Override
                                                                                 public void onComplete(@NonNull Task<Void> task){

                                                                                     if (task.isSuccessful())
                                                                                     {

                                                                                         sendMessageRequest.setEnabled(true);
                                                                                         currentStat = "friends";
                                                                                         sendMessageRequest.setText("Remove");

                                                                                         cancelRequest.setVisibility(View.INVISIBLE);
                                                                                         cancelRequest.setEnabled(false);

                                                                                     }
                                                                                 }
                                                                             });
                                                                 }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void CancelChatRequest() {

        requestReference.child(currentUserId).child(recieverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            requestReference.child(recieverUserId).child(currentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                sendMessageRequest.setEnabled(true);
                                                currentStat = "new";
                                                sendMessageRequest.setText("Send Message");

                                                cancelRequest.setVisibility(View.INVISIBLE);
                                                cancelRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }

                    }
                });
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
