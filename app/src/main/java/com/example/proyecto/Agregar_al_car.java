package com.example.proyecto;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.model.Usua;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Agregar_al_car extends DialogFragment {

    View PARA;
    private FirebaseFirestore mfi, mfiStoree;
    String id_pro;
    String foto;
    EditText numero_can;
    TextView canti, precio_fi, nom_agre;
    ImageButton agre_car, cance_car;
    ImageView agre_car_ima;
    double pre_int;
    int Error2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id_pro= getArguments().getString("id_pro");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Agrecar=inflater.inflate(R.layout.fragment_agregar_al_car, container, false);
        PARA=Agrecar;
        mfi= FirebaseFirestore.getInstance();
        mfiStoree= FirebaseFirestore.getInstance();
        numero_can=Agrecar.findViewById(R.id.Cantipedi);
        canti=Agrecar.findViewById(R.id.fr_cati);
        precio_fi=Agrecar.findViewById(R.id.Precio_fina);
        agre_car=Agrecar.findViewById(R.id.Agre_pro_car);
        cance_car=Agrecar.findViewById(R.id.Cance_pro);
        nom_agre=Agrecar.findViewById(R.id.Titulo_agre);
        agre_car_ima=Agrecar.findViewById(R.id.Ima_car_pro);

        numero_can.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tem1= String.valueOf(numero_can.getText());
                if(tem1.equals("")){
                    int tem3= 0;
                    double tem=tem3*pre_int;
                    String pa=""+tem;
                    precio_fi.setText(pa);
                }else{
                    int tem3= Integer.parseInt(tem1);
                    double tem=tem3*pre_int;
                    String pa=""+tem;
                    precio_fi.setText(pa);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(id_pro==null|| id_pro==""){

        }else{
            readDateCo(id_pro);
            agre_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Error2=0;
                    String nom1 = nom_agre.getText().toString().trim();
                    String can_f = numero_can.getText().toString().trim();
                    String pre_f = precio_fi.getText().toString().trim();
                    String can=canti.getText().toString().trim();
                    int can_f_int= Integer.parseInt(can_f), can_int= Integer.parseInt(can);
                    if (can_f_int>can_int){
                        Toast.makeText(getContext(), "La cantidad solicitada supera la existente", Toast.LENGTH_SHORT).show();
                    }else{
                        if(can_f.isEmpty()|| can_f_int==0){
                            Toast.makeText(getContext(), "No hay valor en la cantidad", Toast.LENGTH_SHORT).show();
                        }else{
                            int actu=can_int-can_f_int;
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Agregando Producto al carrito");
                            progressDialog.show();
                            Agregar_alcarro_f(nom1,can_f,pre_f, actu);
                            Handler handler1= new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Error2==1){
                                        Toast.makeText(getContext(), "Error al agregar al carrito.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }else {
                                        Toast.makeText(getContext(), "Datos Agregados", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        dismiss();
                                    }
                                }
                            },5000);

                        }
                    }


                }
            });
            cance_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }




        return Agrecar;
    }

    private void Agregar_alcarro_f(String nom1, String canF, String preF, int actu) {
        Map<String, Object> user = new HashMap<>();
        user.put("Nombre", nom1);
        user.put("Cantidad", canF);
        user.put("Precio", preF);
        user.put("Foto", foto);
        user.put("Estado","false");
        Calendar calendar = Calendar.getInstance();
        int año =calendar.get(Calendar.YEAR);
        int mes =calendar.get(Calendar.MONDAY)+1;
        int dia =calendar.get(Calendar.DAY_OF_MONTH);
        int hora=calendar.get(Calendar.HOUR_OF_DAY);
        int minuto=calendar.get(Calendar.MINUTE);
        int segundo=calendar.get(Calendar.SECOND);
        String id_pedi=""+nom1+"_"+canF+"_"+segundo+"_"+minuto+"_"+hora+"_"+dia+"_"+mes+"_"+año;
        String Nom= Usua.getInstance().getUsuarioini();
        mfi.collection("Car_"+Nom).document(id_pedi)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Error2=0;
                        Actualizar(actu);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Error2=1;
                    }
                });
    }

    private void Actualizar(int actu) {
        String can_actu=""+actu;
        Map<String, Object> user = new HashMap<>();
        user.put("Cantidad_pro", can_actu);
        mfiStoree.collection("ProductosDto").document(id_pro).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "No se puedo agregar Id", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readDateCo(String corrito2) {
        mfi.collection("ProductosDto").document(corrito2).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombre = documentSnapshot.getString("Nombre_pro");
                String cantidad = documentSnapshot.getString("Cantidad_pro");
                String precio= documentSnapshot.getString("Precio_pro");
                foto=documentSnapshot.getString("Photo");
                Picasso.with(PARA.getContext()).load(foto).resize(400,400).into(agre_car_ima);
                nom_agre.setText(nombre);
                canti.setText(cantidad);
                pre_int= Double.parseDouble(precio);
                precio_fi.setText(""+pre_int);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}