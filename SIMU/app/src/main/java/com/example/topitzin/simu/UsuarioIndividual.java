package com.example.topitzin.simu;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.topitzin.simu.objetos.FirebaseReferences;
import com.example.topitzin.simu.objetos.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UsuarioIndividual extends AppCompatActivity {

    String[] usuario;
    AlertDialog.Builder alert;
    String key;
    View alertLayout;
    Bundle bundle;
    FloatingActionButton atras, modificar, eliminar;
    public FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDB;
    TextView correo, nombre;
    SwitchCompat push, email, admin;
    Boolean pushValue, emailValue, adminValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_individual);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        mDB = database.getReference().getRoot();


        usuario = getIntent().getStringArrayExtra("userSelected");
        buscarObjetos();

        if (usuario.length > 0) {
            llenarespacios(usuario);
        }


        eventos();
    }

    private void buscarObjetos() {
        nombre = (TextView) findViewById(R.id.nombre);
        correo = (TextView) findViewById(R.id.correo);
        push = (SwitchCompat)findViewById(R.id.push);
        email = (SwitchCompat)findViewById(R.id.email);
        admin = (SwitchCompat)findViewById(R.id.admin);
        atras = (FloatingActionButton) findViewById(R.id.back);
        modificar = (FloatingActionButton)findViewById(R.id.modificar);
        eliminar = (FloatingActionButton) findViewById(R.id.eliminar);


        if (mAuth.getCurrentUser() != null)
            //buscar corro de adm en la base de datos y/o guardarlo en preferencias
            mDB.child(FirebaseReferences.Usuarios_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {
                        Usuarios user = snapshot.getValue(Usuarios.class);
                        String correoc = user.getEmail();
                        Boolean admn = user.getAdministrador();

                        if (mAuth.getCurrentUser().getEmail().equals(correoc) && !admn) {
                            modificar.hide();
                            eliminar.hide();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    private void eventos() {
//        atras.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               finish();
//            }
//        });
//
//        modificar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                crearAlerta("¿Desea modificar este usuario?", 1);
//                alert.show();
//            }
//        });
//
//        eliminar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                crearAlerta("¿Desea eliminar este usuario?", 2);
//                alert.show();
//            }
//        });

        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    adminValue = true;
                else
                    adminValue = false;
            }
        });

        email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    emailValue = true;
                else
                    emailValue = false;
            }
        });

        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    pushValue = true;
                else
                    pushValue = false;
            }
        });
    }

    private void crearAlerta(String titulo, final int id) {
        alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do something awesome!
                if (id == 2) {
                    mDB.child(FirebaseReferences.Usuarios_REFERENCE).child(key).removeValue();
                    atras.callOnClick();
                }
                else{
                    Usuarios user = new Usuarios(
                            usuario[1], /*Email*/
                            usuario[0], /*Nombre*/
                            usuario[6], /*Token*/
                            Boolean.valueOf(usuario[4]), /*Push*/
                            Boolean.valueOf(usuario[3]), /*Email*/
                            Boolean.valueOf(usuario[2]));/*Admin*/


                    user.setAdministrador(adminValue);
                    user.setEnableEmail(emailValue);
                    user.setEnablePush(pushValue);

                    Map<String, Object> postValues = user.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/"+FirebaseReferences.Usuarios_REFERENCE+"/" + key, postValues);

                    mDB.updateChildren(childUpdates);
                    atras.callOnClick();
                }
            }
        });
    }

    private void llenarespacios(String[] usuario) {
        correo.setText(usuario[1]);
        nombre.setText(usuario[0]);
        push.setChecked(Boolean.valueOf(usuario[4]));
        pushValue = Boolean.valueOf(usuario[4]);
        email.setChecked(Boolean.valueOf(usuario[3]));
        emailValue = Boolean.valueOf(usuario[3]);
        admin.setChecked(Boolean.valueOf(usuario[2]));
        adminValue = Boolean.valueOf(usuario[2]);
        key = usuario[5];
    }
}
