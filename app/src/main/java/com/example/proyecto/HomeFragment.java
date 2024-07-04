package com.example.proyecto;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.proyecto.adapter.ProductosAdapter;
import com.example.proyecto.model.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class HomeFragment extends Fragment {
    View estra;
    SearchView searchView;
    RecyclerView mRecy;
    ProductosAdapter proda;
    FirebaseFirestore mfi;
    Query query;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View homemaki = inflater.inflate(R.layout.fragment_home, container, false);
        mfi= FirebaseFirestore.getInstance();
        mRecy=homemaki.findViewById(R.id.RecyCli1);
        searchView=homemaki.findViewById(R.id.Buscador);
        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(com.android.car.ui.R.string.car_ui_toolbar_default_search_hint));
        txtSearch.setHintTextColor(Color.BLACK);
        txtSearch.setTextColor(Color.BLACK);
        mRecy.setLayoutManager(new LinearLayoutManager(getContext()));
        query=mfi.collection("ProductosDto");
        search_view();

        FirestoreRecyclerOptions<Producto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Producto>().setQuery(query, Producto.class).build();

        FragmentManager HomeFragment = null;
        proda= new ProductosAdapter(firestoreRecyclerOptions,homemaki,getActivity().getSupportFragmentManager());
        proda.notifyDataSetChanged();
        mRecy.setAdapter(proda);

        estra=homemaki;
        return homemaki;
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
        FirestoreRecyclerOptions<Producto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Producto>().setQuery(query.orderBy("Nombre_pro").startAt(s)
                        .endAt(s+"~"), Producto.class).build();
        proda = new ProductosAdapter(firestoreRecyclerOptions,estra,getActivity().getSupportFragmentManager());
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