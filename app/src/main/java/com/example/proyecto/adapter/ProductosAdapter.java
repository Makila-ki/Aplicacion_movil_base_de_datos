package com.example.proyecto.adapter;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.Editar_Producto;
import com.example.proyecto.HomeFragment;
import com.example.proyecto.R;
import com.example.proyecto.model.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProductosAdapter extends FirestoreRecyclerAdapter<Producto, ProductosAdapter.ViewHolder> {

    private FirebaseFirestore mferis= FirebaseFirestore.getInstance();
    View fm;
    FragmentManager tata;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductosAdapter(@NonNull FirestoreRecyclerOptions<Producto> options, View fm,FragmentManager tata ) {
        super(options);
        this.fm=fm;
        this.tata=tata;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductosAdapter.ViewHolder holder, int position, @NonNull Producto model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id= documentSnapshot.getId();
        holder.noom.setText(model.getNombre_pro());
        holder.tiip.setText(model.getTipo_pro());
        holder.caan.setText(model.getCantidad_pro());
        holder.prre.setText("$"+model.getPrecio_pro());
        String photoPro= model.getPhoto();
        try{
            if (!photoPro.equals("")){
                Picasso.with(fm.getContext().getApplicationContext()).load(photoPro).resize(400,400).into(holder.ima_usu);
            }
        }catch (Exception e){

        }

        holder.btn_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePro(id);
            }
        });
        holder.bnt_edi_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editar_Producto editarProducto= new Editar_Producto();
                Bundle bundle = new Bundle();
                bundle.putString("id_usu", id);
                editarProducto.setArguments(bundle);
                editarProducto.show(tata, "open frag");
            }
        });
    }

    private void deletePro(String id) {
        mferis.collection("ProductosDto").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(fm.getContext(), "Dato eliminado", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(fm.getContext(), "Dato no eliminado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ProductosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.productosusu,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView noom, tiip, caan, prre;
        ImageButton btn_produto,bnt_edi_pro;
        ImageView ima_usu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noom=itemView.findViewById(R.id.Titulopro);
            tiip=itemView.findViewById(R.id.Tip_pro);
            caan=itemView.findViewById(R.id.Can_pro);
            prre=itemView.findViewById(R.id.Pre_pro);
            btn_produto=itemView.findViewById(R.id.EliPro);
            bnt_edi_pro=itemView.findViewById(R.id.EdiPro);
            ima_usu=itemView.findViewById(R.id.Ima_usu);
        }
    }
}
