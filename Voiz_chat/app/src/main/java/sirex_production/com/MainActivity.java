package sirex_production.com;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    private static int SIGN_IN_CODE = 1;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter;
    private ImageButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activity_main = findViewById(R.id.activity_main);
        this.sendBtn = findViewById(R.id.imageView);
        this.sendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText textField = findViewById(R.id.text_input_area);
                if(textField.getText().toString().equals(""))
                {
                    return;
                }
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message(
                            FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(),
                            textField.getText().toString()
                        )
                );
                textField.setText("");
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        }
        else
        {
            Snackbar.make(activity_main, "Welcome", Snackbar.LENGTH_SHORT).show();
            displayMessages();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_CODE)
        {
            if(requestCode == RESULT_OK)
            {
                Snackbar.make(activity_main, "Welcome", Snackbar.LENGTH_SHORT).show();
                displayMessages();
            }
            else
            {
                Snackbar.make(activity_main, "You have to sing up first", Snackbar.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    private void displayMessages()
    {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        this.adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference())
        {
            @Override
            protected void populateView(View v, Message model, int position)
            {
                TextView mess_user, mess_time, mess_text;
                mess_user = v.findViewById(R.id.user_message);
                mess_time = v.findViewById(R.id.user_message_time);
                mess_text = v.findViewById(R.id.user_message_text);

                mess_user.setText(model.getUserName());
                mess_text.setText(model.getUserMessage());
                mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}