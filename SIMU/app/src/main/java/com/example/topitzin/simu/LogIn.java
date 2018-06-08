package com.example.topitzin.simu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.topitzin.simu.objetos.FirebaseReferences;
import com.example.topitzin.simu.objetos.Usuarios;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class LogIn extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    public SignInButton mGoogleBtn;
    public GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences preferences;
    ProgressDialog dialog;
    SharedPreferences.Editor editor;
    private myAsyncTask ast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mGoogleBtn = (SignInButton)findViewById(R.id.signIn);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().getRoot();
        dialog = new ProgressDialog(this);
        
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){

                    //ast = new myAsyncTask();
                    //ast.execute();
                    registarUser();
                    //isAdm();
                    //mAuth.removeAuthStateListener(mAuthListener);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(LogIn.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }, 1500);
                    startActivity(new Intent(LogIn.this, MainActivity.class));
                    finish();
                }
                dialog.dismiss();
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LogIn.this, "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    //una vez que se autentico
    private void registarUser() {

        reference.child(FirebaseReferences.Usuarios_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean registrado = false;
                Usuarios user = new Usuarios();
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    user = snapshot.getValue(Usuarios.class);
                    if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())){
                        registrado = true;
                    }
                    if (registrado)
                        break;
                }
                if (!registrado){
                    user = new Usuarios(
                            mAuth.getCurrentUser().getEmail(),
                            mAuth.getCurrentUser().getDisplayName(),
                            preferences.getString("Token","1"),
                            true, true, false);
                    reference.child(FirebaseReferences.Usuarios_REFERENCE).push().setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        dialog.setCancelable(false);
        //dialog.setTitle("Iniciando sesión");

        dialog.setIndeterminate(true);
        dialog.setMessage("Authenticating...");
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                dialog.show();

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("hi", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("hi", "signInWithCredential", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void>
    {
        String mensaje = "";
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            startActivity(new Intent(LogIn.this, MainActivity.class));
            finish();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getApplicationContext() , getResources().getString(R.string.Syncn), getResources().getString(R.string.wait), true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // protected SoapObject doInBackground(Integer... branchNumber){

            reference.child(FirebaseReferences.Usuarios_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean registrado = false;
                    Boolean admin = false;
                    Usuarios user = new Usuarios();
                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {
                        user = snapshot.getValue(Usuarios.class);
                        if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())){
                            registrado = true;
                            admin = user.getAdministrador();
                        }
                        if (registrado)
                            break;
                    }
                    if (!registrado){
                        user = new Usuarios(
                                mAuth.getCurrentUser().getEmail(),
                                mAuth.getCurrentUser().getDisplayName(),
                                preferences.getString("Token","1"),
                                true, true, false);
                        admin = false;
                        reference.child(FirebaseReferences.Usuarios_REFERENCE).push().setValue(user);
                    }

                    editor.putBoolean("administrador", admin);
                    editor.apply();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }


        protected void onProgressUpdate(String values) {
            if (values != null) {
                // shows a toast for every value we get
                Toast.makeText(getApplicationContext(), values, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
