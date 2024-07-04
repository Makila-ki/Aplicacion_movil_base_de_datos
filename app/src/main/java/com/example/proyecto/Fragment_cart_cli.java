package com.example.proyecto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto.adapter.Clien_carAdapter;
import com.example.proyecto.model.Clien_car;
import com.example.proyecto.model.Usua;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Fragment_cart_cli extends Fragment {
    FirebaseFirestore mfi;
    RecyclerView mRecy;

    Clien_carAdapter carAdapter;
    Query query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View carcli= inflater.inflate(R.layout.fragment_cart_cli, container, false);
        String nommm= Usua.getInstance().getUsuarioini();
        mfi=FirebaseFirestore.getInstance();
        mRecy=carcli.findViewById(R.id.RecyP_car);

        mRecy.setLayoutManager(new LinearLayoutManager(getContext()));
        query=mfi.collection("Car_"+nommm);
        FirestoreRecyclerOptions<Clien_car> firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Clien_car>().setQuery(query, Clien_car.class).build();

        carAdapter= new Clien_carAdapter(firestoreRecyclerOptions, carcli);
        carAdapter.notifyDataSetChanged();
        mRecy.setAdapter(carAdapter);

        return carcli;
    }
    public void onStart() {
        super.onStart();
        carAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        carAdapter.stopListening();
    }
}