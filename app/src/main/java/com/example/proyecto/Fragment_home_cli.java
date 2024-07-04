package com.example.proyecto;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.proyecto.adapter.Produ_cliAdapter;
import com.example.proyecto.adapter.ProductosAdapter;
import com.example.proyecto.model.Pro_cli;
import com.example.proyecto.model.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Fragment_home_cli extends Fragment {
    View datito;
    SearchView searchView;
    RecyclerView mRecy;
    Produ_cliAdapter proda;
    FirebaseFirestore mfi;
    Query query;
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Homecli=inflater.inflate(R.layout.fragment_home_cli, container, false);
        mfi=FirebaseFirestore.getInstance();
        mRecy=Homecli.findViewById(R.id.RecyP_cli);
        searchView=Homecli.findViewById(R.id.Buscador_cli);
        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(com.android.car.ui.R.string.car_ui_toolbar_default_search_hint));
        txtSearch.setHintTextColor(Color.BLACK);
        txtSearch.setTextColor(Color.BLACK);


        mRecy.setLayoutManager(new LinearLayoutManager(getContext()));
        query=mfi.collection("ProductosDto").whereEqualTo("Estado", "true");
        search_view();

        FirestoreRecyclerOptions<Pro_cli> firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Pro_cli>().setQuery(query, Pro_cli.class).build();

        proda= new Produ_cliAdapter(firestoreRecyclerOptions, Homecli, getActivity().getSupportFragmentManager());
        proda.notifyDataSetChanged();
        mRecy.setAdapter(proda);

        datito=Homecli;
        return Homecli;
    }

    private void search_view() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                textSearch(query);
                return false;
            }
        });
    }

    private void textSearch(String s) {
        FirestoreRecyclerOptions<Pro_cli> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pro_cli>().setQuery(query.orderBy("Nombre_pro").startAt(s)
                        .endAt(s+"~"), Pro_cli.class).build();
        proda = new Produ_cliAdapter(firestoreRecyclerOptions,datito, getActivity().getSupportFragmentManager());
        proda.startListening();
        mRecy.setAdapter(proda);
    }

    @Override
    public void onStart() {
        super.onStart();
        proda.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        proda.stopListening();
    }
}