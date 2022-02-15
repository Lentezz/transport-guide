package com.univer.transport_guide;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.univer.transport_guide.model.User;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    RelativeLayout registerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        //registerLayout = findViewById(R.id.register_layout);
        btnRegister.setOnClickListener(view -> showRegisterWindow());
    }

    public void showRegisterWindow(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Register now");
        dialog.setMessage("Write all now");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window, null);

        dialog.setView(registerWindow);

        final MaterialEditText name = registerWindow.findViewById(R.id.nameField);
        final MaterialEditText password = registerWindow.findViewById(R.id.passField);
        final MaterialEditText email = registerWindow.findViewById(R.id.emailField);

        dialog.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        dialog.setPositiveButton("Add", (dialogInterface, i) -> {
            if(TextUtils.isEmpty(email.getText().toString())){
                Snackbar.make(registerLayout, "ERROR email", Snackbar.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(name.getText().toString())){
                Snackbar.make(registerLayout, "ERROR name", Snackbar.LENGTH_LONG).show();
                return;
            }
            if(password.getText().toString().length() < 6){
                Snackbar.make(registerLayout, "ERROR password", Snackbar.LENGTH_LONG).show();
                return;
            }
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setEmail(email.getText().toString());
                        user.setName(name.getText().toString());
                        user.setPassword(password.getText().toString());

                        users.child(user.getEmail()).setValue(user).addOnSuccessListener(unused ->
                                Snackbar.make(registerLayout, "MMM Nice", Snackbar.LENGTH_LONG).show());

                    });
        });
        dialog.show();

    }

}