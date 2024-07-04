package com.example.proyecto.adapter;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.Clien_car;
import com.example.proyecto.model.Usua;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Clien_carAdapter extends FirestoreRecyclerAdapter<Clien_car,Clien_carAdapter.ViewHolder> {
    int Dato,accion, accion2;
    Double sobra;
    String id_pro, sal;
    ProgressDialog progressDialog;
    private FirebaseFirestore mferis= FirebaseFirestore.getInstance();
    View fm;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Clien_carAdapter(@NonNull FirestoreRecyclerOptions<Clien_car> options, View fm) {
        super(options);
        this.fm=fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull Clien_carAdapter.ViewHolder holder, int position, @NonNull Clien_car model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id= documentSnapshot.getId();
        String esta= model.getEstado(), VERI="true";
        if(VERI.equals(esta)){
            holder.no_car.setText(model.getNombre());
            holder.es_car.setText("Pagado");
            holder.ca_car.setText(model.getCantidad());
            holder.pr_car.setText("$"+model.getPrecio());
            String prepree= model.getPrecio();
            String fotito=model.getFoto();
            try{
                if (!fotito.equals("")){
                    Picasso.with(fm.getContext().getApplicationContext()).load(fotito).resize(400,400).into(holder.foto_car);
                }

            }catch (Exception e){

            }
            holder.bt_sacar_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(fm.getContext());
                    progressDialog.setMessage("Cancelando pedido");
                    progressDialog.show();
                    sobra= (double) 0;
                    sal=Usua.getInstance().getSaldoini();
                    Double tempopo= Double.valueOf(sal);
                    Double temprep= Double.valueOf(prepree);
                        sobra=tempopo+temprep;
                        String SSS= String.valueOf(sobra);
                    Pago_Actu(SSS);
                    Handler handler2= new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deletePro(id);
                            progressDialog.dismiss();
                        }
                    },2000);


                }
            });



        } else {
            holder.no_car.setText(model.getNombre());
            holder.es_car.setText("No Pagado");
            holder.ca_car.setText(model.getCantidad());
            holder.pr_car.setText("$"+model.getPrecio());
            String prepre= model.getPrecio();
            String cancan= (String) holder.ca_car.getText();
            String nomnom=(String) holder.no_car.getText();
            String fotito=model.getFoto();
            try{
                if (!fotito.equals("")){
                    Picasso.with(fm.getContext().getApplicationContext()).load(fotito).resize(400,400).into(holder.foto_car);
                }

            }catch (Exception e){

            }
            holder.bt_sacar_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(fm.getContext());
                    progressDialog.setMessage("Sacando del carrito");
                    progressDialog.show();
                    readDateCo(nomnom,cancan);
                    Handler handler1= new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String dato_s= String.valueOf(Dato);
                            Agregar_dato(id_pro, dato_s);
                            Handler handler= new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    deletePro(id);
                                    progressDialog.dismiss();
                                }
                            },2000);
                        }
                    },2000);
                }
            });
            holder.bt_pagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sobra= (double) 0;
                    sal=Usua.getInstance().getSaldoini();
                    Double tempopo= Double.valueOf(sal);
                    Double temprep= Double.valueOf(prepre);
                    if (temprep<tempopo){
                        accion=0;
                        sobra=tempopo-temprep;
                    }else{
                        accion=1;
                    }
                    progressDialog = new ProgressDialog(fm.getContext());
                    progressDialog.setMessage("Pagando");
                    progressDialog.show();

                    Handler handler1= new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(accion==1){
                                progressDialog.dismiss();
                                Toast.makeText(fm.getContext(), "Saldo Insuficiente", Toast.LENGTH_SHORT).show();
                            }else{
                                String sobra_s= String.valueOf(sobra);
                                Pago_Actu(sobra_s);
                                Handler handler= new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (accion2==1){
                                            progressDialog.dismiss();
                                            Toast.makeText(fm.getContext(), "Error al pagar", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Cambiar_es("true",id);
                                            progressDialog.dismiss();
                                        }

                                    }
                                },4000);
                            }

                        }
                    },4000);

                }
            });

        }
    }
    private void deletePro(String id) {
        mferis.collection("Car_"+Usua.getInstance().getUsuarioini()).document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(fm.getContext(), "Dato no eliminado", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Agregar_dato(String docu, String canti) {
        Map<String, Object> user = new HashMap<>();
        user.put("Cantidad_pro", canti);

        mferis.collection("ProductosDto").document(docu)
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(fm.getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void readDateCo(String corrito2, String canti) {
        // Obtener la referencia a la colección

        Query queryy = mferis.collection("ProductosDto").whereEqualTo("Nombre_pro",corrito2);

        queryy.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot DocumentSnapshot:queryDocumentSnapshots){

                    String pi=DocumentSnapshot.getString("Tipo_pro");
                    id_pro=corrito2+"_"+pi;

                    int pe= Integer.parseInt(DocumentSnapshot.getString("Cantidad_pro"));
                    int pu= Integer.parseInt(canti);
                    Dato=pe+pu;

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    private void Pago_Actu(String canti) {
        Map<String, Object> user = new HashMap<>();
        user.put("Saldo", canti);

        mferis.collection("UsuariosDto").document(""+Usua.getInstance().getUsuarioini())
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Usua.getInstance().setSaldoini(""+canti);
                        accion2=0;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(fm.getContext(), "Error al pagar actu", Toast.LENGTH_SHORT).show();
                        accion2=1;
                    }
                });
    }
    private void Cambiar_es(String esta, String idd) {
        Map<String, Object> user = new HashMap<>();
        user.put("Estado", esta);

        mferis.collection("Car_"+Usua.getInstance().getUsuarioini()).document(idd)
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(fm.getContext(), "Error al cambiar estado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public Clien_carAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.productocar_cli,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView no_car, ca_car, es_car, pr_car;
        ImageButton bt_pagar, bt_sacar_car;
        ImageView foto_car;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            no_car = itemView.findViewById(R.id.Titulo_cart);
            ca_car = itemView.findViewById(R.id.canti_fi);
            es_car = itemView.findViewById(R.id.Estado_car);
            pr_car = itemView.findViewById(R.id.Precio_fifi);
            bt_pagar = itemView.findViewById(R.id.Pagar);
            bt_sacar_car=itemView.findViewById(R.id.Eliminar_car);
            foto_car=itemView.findViewById(R.id.ima_car);
        }
    }
}
