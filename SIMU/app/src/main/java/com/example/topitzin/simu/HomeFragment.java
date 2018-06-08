package com.example.topitzin.simu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.topitzin.simu.objetos.FirebaseReferences;
import com.example.topitzin.simu.objetos.Radios;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 */
public class HomeFragment extends Fragment {

    private String nombre, mail = "";
    private OnFragmentInteractionListener mListener;
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    FirebaseDatabase database;
    DatabaseReference mDB;
    public int dispositivos_conectados = 0;
    TextView dis_num, name;
    public ProgressDialog Progressdialog;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDB = database.getReference().getRoot();

        if (mAuth.getCurrentUser() != null) {
            nombre = mAuth.getCurrentUser().getDisplayName();
        }

        View view = buscarObjetos(inflater, container);
        View view1 = view.findViewById(R.id.content_main);
        Progressdialog.show();

        name.setText(nombre);


        return view;
    }

    private View buscarObjetos(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dis_num = (TextView) view.findViewById(R.id.disp_num);
        name = (TextView) view.findViewById(R.id.nombre);
        Progressdialog = new ProgressDialog(getContext());
        Progressdialog.setMessage("Sincronizando");
        Progressdialog.setIndeterminate(true);
        Progressdialog.setCancelable(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(getActivity(), LogIn.class));
                }
            }
        };

        mDB.child(FirebaseReferences.Radios_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //radios.removeAll(radios)
                dispositivos_conectados = 0;
                for (DataSnapshot DS :
                        dataSnapshot.getChildren()) {
                    Radios radio = DS.getValue(Radios.class);
                    //radio.setIp(DS.getKey());
                    //radios.add(radio);
                    if (radio.getEstado())
                        dispositivos_conectados++;
                }

                //adaptador.notifyDataSetChanged();

                dis_num.setText("\n" + String.valueOf(dispositivos_conectados));
                Progressdialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
