package com.example.proyecto;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class Fragment_Regis extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    Button bt_agregar;
    ImageButton bt_cerrarReg;

    String Valous, Usito, Valoco, Corrito, tipossss;
    EditText pass, nomm, edaa,usuu,corr,tell;

    boolean passvista;

    int Error=0, Error2=0;

    private FirebaseFirestore mfi, mfiStore,mfiStoree;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View agre = inflater.inflate(R.layout.fragment__regis, container, false);
        // Inflate the layout for this fragment
        bt_agregar=agre.findViewById(R.id.Ingresar);
        bt_cerrarReg=agre.findViewById(R.id.CerrarReg);
        pass=agre.findViewById(R.id.Contrasena);
        nomm=agre.findViewById(R.id.Nombre);
        edaa=agre.findViewById(R.id.Edad);
        usuu=agre.findViewById(R.id.Usuario);
        corr=agre.findViewById(R.id.Correo);
        tell=agre.findViewById(R.id.Telefono);
        spinner=agre.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getContext(), R.array.location,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setDrawingCacheBackgroundColor(getContext().getResources().getColor(R.color.black));

        spinner.setOnItemSelectedListener(this);

        tipossss="";

        mfi= FirebaseFirestore.getInstance();
        mfiStore= FirebaseFirestore.getInstance();
        mfiStoree= FirebaseFirestore.getInstance();


        bt_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Error=0;
                String Nombre1= nomm.getText().toString().trim();
                String Edad1= edaa.getText().toString().trim();
                String Usuario1= usuu.getText().toString().trim();
                String Contrasena1= pass.getText().toString().trim();
                String Correo1= corr.getText().toString().trim();
                String Telefono1= tell.getText().toString().trim();

                if (Nombre1.isEmpty()&&Usuario1.isEmpty()&&Contrasena1.isEmpty()&&Correo1.isEmpty()&&tipossss.isEmpty()){
                    Toast.makeText(getContext(), "Algun Dato Se encuentra Vacio", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        if (Edad1!=null&&Telefono1!=null){
                            readDateCo(Correo1);
                            readDate(Usuario1);

                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Agregando Informacion");
                            progressDialog.show();
                            Handler handler1= new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Analisis(Nombre1, Edad1,Telefono1);
                                    if(Error==0){
                                        Agregar(Nombre1, Edad1, Usuario1, Contrasena1,Correo1, Telefono1, tipossss);
                                        if (Error2==1){
                                            Toast.makeText(getContext(), "Error al agregar datos.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            progressDialog.dismiss();
                                            nomm.setText("");
                                            edaa.setText("");
                                            usuu.setText("");
                                            pass.setText("");
                                            corr.setText("");
                                            tell.setText("");
                                            Toast.makeText(getContext(), "Datos Agregados", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        progressDialog.dismiss();
                                    }
                                }
                            },5000);

                        }else{

                        }
                    }catch (NumberFormatException nft){
                        Toast.makeText(getContext(), "Algun Dato Se encuentra Vacio", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        bt_cerrarReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right= 2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=pass.getRight()-pass.getCompoundDrawables()[Right].getBounds().width()){
                        int selet=pass.getSelectionEnd();
                        if(passvista){
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passvista=false;
                        }else {
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passvista=true;
                        }
                        pass.setSelection(selet);
                        return true;
                    }
                }
                return false;
            }
        });

        return agre;
    }

    private void Agregar(String nombre1, String edad1, String usuario1, String contrasena1, String correo1, String telefono1, String tipooos) {
        Map<String, Object> user = new HashMap<>();
        user.put("Nombre", nombre1);
        user.put("Edad", edad1);
        user.put("Usuario", usuario1);
        user.put("Contrasena", contrasena1);
        user.put("Correo", correo1);
        user.put("Telefono", telefono1);
        user.put("Id_sesi", "");
        user.put("Tipo_us", "Cliente");
        user.put("Saldo","");
        user.put("Foto","");
        user.put("Tipo_cliente", tipooos);
        mfi.collection("UsuariosDto").document(usuario1)
                .set(user)
                .addOnFailureListener(new OnFailureListener(){
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


        if(Valous.equals(Usito)){
            Toast.makeText(getContext(), "Error en el Usuario:Este Usuario ya existe.", Toast.LENGTH_SHORT).show();
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

    private void readDate(String curp2) {
        // Obtener la referencia a la colección

        Query query = mfiStore.collection("UsuariosDto").whereEqualTo("Usuario",curp2);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Usito="true";
                }else{
                    Usito="false";
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error buscar igual", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();
        tipossss=choice;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}