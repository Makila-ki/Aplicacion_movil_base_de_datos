package com.example.proyecto;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class Editar_Cliente extends DialogFragment {
    private FirebaseFirestore mfi,mfiStore,mfiStoree;
    boolean passvista;

    EditText pass_ed, nomm_ed, edaa_ed,usuu_ed,corr_ed,tell_ed;
    Button btn_editar;
    ImageButton cerrar_edit;
    int Error=0, Error2;
    String id;
    String Valous, Usito, Valoco, Corrito;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id=getArguments().getString("id_us");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Editar_cli= inflater.inflate(R.layout.fragment_editar__cliente, container, false);
        mfi= FirebaseFirestore.getInstance();
        mfiStore= FirebaseFirestore.getInstance();
        mfiStoree= FirebaseFirestore.getInstance();
        pass_ed=Editar_cli.findViewById(R.id.Edi_Contrasena);
        nomm_ed=Editar_cli.findViewById(R.id.Edi_Nombre);
        edaa_ed=Editar_cli.findViewById(R.id.Edi_Edad);
        corr_ed=Editar_cli.findViewById(R.id.Edi_Correo);
        tell_ed=Editar_cli.findViewById(R.id.Edi_Telefono);
        btn_editar=Editar_cli.findViewById(R.id.Actualizar);
        cerrar_edit=Editar_cli.findViewById(R.id.CerrarEditor);

        if(id==null||id==""){
        }else{
            readDateCo2(id);
            btn_editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Error=0;
                    Error2=0;
                    String Nombre1= nomm_ed.getText().toString().trim();
                    String Edad1= edaa_ed.getText().toString().trim();
                    String Contrasena1= pass_ed.getText().toString().trim();
                    String Correo1= corr_ed.getText().toString().trim();
                    String Telefono1= tell_ed.getText().toString().trim();
                    if (Nombre1.isEmpty()&&Contrasena1.isEmpty()&&Correo1.isEmpty()){
                        Toast.makeText(getContext(), "Algun Dato Se encuentra Vacio", Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            if (Edad1!=null&&Telefono1!=null){
                                readDateCo(Correo1);

                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Actualizando Informacion");
                                progressDialog.show();
                                Handler handler1= new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Analisis(Nombre1, Edad1,Telefono1);
                                        if(Error==0){
                                            Agregar_actua(Nombre1, Edad1, Contrasena1,Correo1, Telefono1);
                                            Handler handler= new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (Error2==1){
                                                        Toast.makeText(getContext(), "Error al Actualizar datos.", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        progressDialog.dismiss();
                                                        dismiss();
                                                        Toast.makeText(getContext(), "Datos Agregados", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            },2000);

                                        }else{
                                            progressDialog.dismiss();
                                        }
                                    }
                                },3000);

                            }else{

                            }
                        }catch (NumberFormatException nft){
                            Toast.makeText(getContext(), "Algun Dato Se encuentra Vacio", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
            cerrar_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        pass_ed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right= 2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=pass_ed.getRight()-pass_ed.getCompoundDrawables()[Right].getBounds().width()){
                        int selet=pass_ed.getSelectionEnd();
                        if(passvista){
                            pass_ed.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            pass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passvista=false;
                        }else {
                            pass_ed.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passvista=true;
                        }
                        pass_ed.setSelection(selet);
                        return true;
                    }
                }
                return false;
            }
        });

        return Editar_cli;
    }

    private void Agregar_actua(String nombre1, String edad1, String contrasena1, String correo1, String telefono1) {
        Map<String, Object> user = new HashMap<>();
        user.put("Nombre", nombre1);
        user.put("Edad", edad1);
        user.put("Contrasena", contrasena1);
        user.put("Correo", correo1);
        user.put("Telefono", telefono1);
        mfiStoree.collection("UsuariosDto").document(id).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Error2=0;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Error2=1;
            }
        });
    }

    public void Analisis(String Nomb2,String Edad2,String Tele2){
        Valous="true";
        Valoco="true";

        String Nomes = Nomb2;
        String[] partes = Nomes.split(" ");
        if (partes.length != 4) {
            Toast.makeText(getContext(), "Error en el Nombre: No cumple con las Caracteristicas.", Toast.LENGTH_SHORT).show();
            Error=Error+1;
        }

        int Edaa= Integer.parseInt(Edad2);
        if(Edaa<130){
        }else {
            Toast.makeText(getContext(), "Error en la Edad: Edad No valida.", Toast.LENGTH_SHORT).show();
            Error=Error+1;
        }
        if (Edaa<18){
            Toast.makeText(getContext(), "Error en la Edad: Debes de ser mayor de edad.", Toast.LENGTH_SHORT).show();
            Error=Error+1;
        }
        int Tell= Tele2.length();
        if(Tell!=10){
            Toast.makeText(getContext(), "Error en la Telefono: Numero de telefono no valido.", Toast.LENGTH_SHORT).show();
            Error=Error+1;
        }
        if(Valoco.equals(Corrito)){
            Toast.makeText(getContext(), "Error en el Correo:Este correo ya tiene una cuenta", Toast.LENGTH_SHORT).show();
            Error=Error+1;
        }



    }

    private void readDateCo(String corrito2) {
        // Obtener la referencia a la colección

        Query queryy = mfiStoree.collection("UsuariosDto").whereEqualTo("Correo",corrito2);

        queryy.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Corrito="true";
                }else{
                    Corrito="false";
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error buscar igual", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void readDateCo2(String corrito2) {
        mfi.collection("UsuariosDto").document(corrito2).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombre = documentSnapshot.getString("Nombre");
                String pass = documentSnapshot.getString("Contrasena");
                String edad= documentSnapshot.getString("Edad");
                String correo= documentSnapshot.getString("Correo");
                String telefono= documentSnapshot.getString("Telefono");
                nomm_ed.setText(nombre);
                pass_ed.setText(pass);
                edaa_ed.setText(edad);
                corr_ed.setText(correo);
                tell_ed.setText(telefono);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}