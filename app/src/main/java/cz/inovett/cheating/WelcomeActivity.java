package cz.inovett.cheating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        auth = FirebaseAuth.getInstance();

    }

    public void startRegister(View view) {
        Intent i = new Intent(WelcomeActivity.this,RegisterActivity.class);
        startActivity(i);
    }

    public void startLogIn(View view) {
        Intent i = new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* checkUserExist();*/
    }

    /**private void checkUserExist() {

     if (auth.getCurrentUser()!=null) {

     final String name = auth.getCurrentUser().getUid();
     mDatabaseUsers.addValueEventListener(new ValueEventListener() {
    @Override public void onDataChange(DataSnapshot dataSnapshot) {
    if (!dataSnapshot.hasChild(name)) {
    Intent i = new Intent(WelcomeActivity.this, RegisterActivity.class); /**vyřešit rpoblémy
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(i);
    }else {
    Intent i = new Intent(WelcomeActivity.this, MainActivity.class); /**vyřešit rpoblémy
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(i);
    }
    }

    @Override public void onCancelled(DatabaseError databaseError) {

    }
    });
     }
     }*/
}
