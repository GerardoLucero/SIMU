package com.example.topitzin.simu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.topitzin.simu.objetos.FirebaseReferences;
import com.example.topitzin.simu.objetos.Radios;
import com.example.topitzin.simu.objetos.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

public class radioIndividual extends AppCompatActivity {

    String[] radio;
    TextView nombre, ip, rx, tx, señal;
    AlertDialog.Builder alert;
    AlertDialog.Builder alertE;
    String key;
    View alertLayout;
    Bundle bundle;
    FloatingActionButton atras, modificar, eliminar;
    public FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDB;
    Radios estacion;
    private TextView estado;
    private TextView tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_individual);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        mDB = database.getReference().getRoot();
        Intent i = getIntent();

        buscarObjetos();

         radio = i.getStringArrayExtra("radioselected");

        if (radio.length > 0) {
            llenarespacios(radio);
        }


        eventos();
        crearAlerta();
        crearElimnar();
    }

    private void crearElimnar() {
        alertE = new AlertDialog.Builder(this);
        alertE.setTitle("¿Está seguro de eliminar el registro?");
        alertE.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do something awesome!
                mDB.child(FirebaseReferences.Radios_REFERENCE).child(key).removeValue();
                atras.callOnClick();
            }
        });

        alertE.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
    }

    private void crearAlerta() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertLayout = inflater.inflate(R.layout.ip_layout, null);
        alert = new AlertDialog.Builder(this);
        final EditText ed_ip = (EditText) alertLayout.findViewById(R.id.et_username);

        if (radio != null){
            ed_ip.setText(""+radio[0]);
            alert.setTitle(radio[1] + " / " + key);
        }



        alert.setView(alertLayout);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newip = ed_ip.getText().toString();
                // Do something awesome!
                //mDB.child(FirebaseReferences.Radios_REFERENCE).child(key).child("ip").se
                if (!newip.equals("")) {
                    estacion = new Radios(radio[1], newip, Integer.parseInt(radio[2]), Integer.parseInt(radio[3]), Double.parseDouble(radio[4]), true, Integer.parseInt(radio[6]), "",0);


                    Map<String, Object> postValues = estacion.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/"+FirebaseReferences.Radios_REFERENCE+"/" + key, postValues);

                    mDB.updateChildren(childUpdates);
                    atras.callOnClick();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
    }

    private void eventos() {
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertLayout.getParent() != null){
                    ((ViewGroup)alertLayout.getParent()).removeView(alertLayout);
                }
                alert.show();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertLayout.getParent() != null){
                    ((ViewGroup)alertLayout.getParent()).removeView(alertLayout);
                }
                alertE.show();
            }
        });
    }

    private void llenarespacios(String[] radio) {
        nombre.setText(radio[1]);
        ip.setText("Ip: "+radio[0]);
        rx.setText("Tasa Rx: "+radio[2]);
        tx.setText("Tasa Tx: "+radio[3]);
        señal.setText("Señal: "+radio[4]);
        key = radio[5];
        if (radio[7].equals("true"))
            estado.setText("Estado: Conectado");
        else
            estado.setText("Estado: Desconectado");

        tiempo.setText(calculartiempo(radio[8]));
    }

    private String calculartiempo(String s) {
        int time = Integer.parseInt(s);

        int horas = ((time/1000)/60)/60;
        int min = (horas * 60) - ((time/1000)/60);
        min = Math.abs(min);

        return "Horas: " + String.valueOf(horas) + ", Minutos: " + String.valueOf(min) + " conectado";
    }

    private void buscarObjetos() {
        nombre = (TextView) findViewById(R.id.nombre);
        ip = (TextView) findViewById(R.id.ip);
        rx = (TextView) findViewById(R.id.Rx);
        tx = (TextView) findViewById(R.id.Tx);
        señal = (TextView) findViewById(R.id.señal);
        estado = (TextView)findViewById(R.id.estado);
        tiempo = (TextView)findViewById(R.id.tiempo);
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
}
