package com.anshansh.task3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button sendMessageButton;
    private EditText userMessageInput;
    private ScrollView scrollView;
    private TextView displayMessage;

    private String currentGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupChatActivity.this, currentGroupName, Toast.LENGTH_SHORT).show();
        
        InitializeFields();
    }

    private void InitializeFields() {

        toolbar = (Toolbar) findViewById(R.id.groupchat_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Group Name");

        sendMessageButton = (Button) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);

        displayMessage = (TextView) findViewById(R.id.groupchat_display);

        scrollView = (ScrollView) findViewById(R.id.scrollview);



    }
}
