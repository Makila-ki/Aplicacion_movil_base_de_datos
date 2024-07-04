package com.example.proyecto.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.Agregar_al_car;
import com.example.proyecto.R;
import com.example.proyecto.model.Pro_cli;
import com.example.proyecto.model.Usua;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Produ_cliAdapter extends FirestoreRecyclerAdapter<Pro_cli, Produ_cliAdapter.ViewHolder> {

    View fm;
    FragmentManager tata;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Produ_cliAdapter(@NonNull FirestoreRecyclerOptions<Pro_cli> options, View fm, FragmentManager tata) {
        super(options);
        this.fm=fm;
        this.tata=tata;
    }

    @Override
    protected void onBindViewHolder(@NonNull Produ_cliAdapter.ViewHolder holder, int position, @NonNull Pro_cli model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id= documentSnapshot.getId();
        int prueva= Integer.parseInt(model.getCantidad_pro());
        String prue="true";
        if(prue.equals(model.getEstado())){
                holder.nom_c.setText(model.getNombre_pro());
                holder.can_c.setText(model.getCantidad_pro());
                holder.pre_c.setText("$"+model.getPrecio_pro());
                holder.tip_c.setText(model.getTipo_pro());
                String photoPro= model.getPhoto();
                try{
                    if (!photoPro.equals("")){
                        Picasso.with(fm.getContext().getApplicationContext()).load(photoPro).resize(400,400).into(holder.image_cli);
                    }

                }catch (Exception e){

                }
                holder.btn_pro_c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Agregar_al_car agregar_al_car = new Agregar_al_car();
                        Bundle bundle = new Bundle();
                        bundle.putString("id_pro", id);
                        agregar_al_car.setArguments(bundle);
                        agregar_al_car.show(tata, "open frag");
                    }
                });

        }else{

        }

    }


    @NonNull
    @Override
    public Produ_cliAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.productoscli,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nom_c, tip_c, can_c, pre_c;
        ImageButton btn_pro_c;
        ImageView image_cli;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom_c=itemView.findViewById(R.id.Titulo_cli);
            tip_c=itemView.findViewById(R.id.Tipo_cli);
            can_c=itemView.findViewById(R.id.Cantidad_cli);
            pre_c=itemView.findViewById(R.id.Precio_cli);
            image_cli=itemView.findViewById(R.id.Ima_cli_pro);
            btn_pro_c=itemView.findViewById(R.id.Agre_pro_car);

        }
    }
}
