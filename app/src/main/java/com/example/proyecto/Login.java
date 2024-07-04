package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.model.Usua;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button ini, reg;
    EditText passlog, usualog;
    boolean passlogvis;
    int Error;
    ProgressDialog progressDialog;
    String deviceId, Usss;

    private FirebaseFirestore mfiStore,mfiStoree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ini= findViewById(R.id.Btn_ini);
        reg= findViewById(R.id.Btn_reg);
        passlog= findViewById(R.id.Contrasenalog);
        usualog=findViewById(R.id.Usuariolog);

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Error=0;
        mfiStore= FirebaseFirestore.getInstance();
        mfiStoree= FirebaseFirestore.getInstance();

        ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inini= usualog.getText().toString().trim();
                String paspa= passlog.getText().toString().trim();
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Iniciando Sesion...");
                progressDialog.show();
                if(inini.isEmpty()&&paspa.isEmpty()){
                    Toast.makeText(Login.this, "Hay un dato que es encuentra vacio", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else{
                    readDate(inini,paspa);
                    Handler handler1= new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Error==0) {
                                AgregarId(deviceId, Usss);
                                String T_ad="Administrador", T_cl="Cliente";
                                String Tipo=Usua.getInstance().getTipoini();
                                Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (T_ad.equals(Tipo)){
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(Login.this, Home.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (T_cl.equals(Tipo)) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(Login.this, Home_cli.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                },2000);

                            }else{
                                progressDialog.dismiss();
                            }
                        }
                    },3000);
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Regis Rege= new Fragment_Regis();
                Rege.show(getSupportFragmentManager(), "Fragment_Regis");
            }
        });

        passlog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right= 2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=passlog.getRight()-passlog.getCompoundDrawables()[Right].getBounds().width()){
                        int selet=passlog.getSelectionEnd();
                        if(passlogvis){
                            passlog.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            passlog.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passlogvis=false;
                        }else {
                            passlog.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            passlog.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passlogvis=true;
                        }
                        passlog.setSelection(selet);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void AgregarId(String deviceId, String Usito) {
        Map<String, Object> user = new HashMap<>();
        user.put("Id_sesi", deviceId);
        mfiStoree.collection("UsuariosDto").document(Usito).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "No se puedo agregar Id", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void readDate(String Usu1, String Con1) {
        DocumentReference docRef = mfiStore.collection("UsuariosDto").document(Usu1);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if (documentSnapshot.contains("Contrasena")){
                        Object pa=documentSnapshot.get("Contrasena");
                        String pe=documentSnapshot.getString("Tipo_us");
                        String pi=documentSnapshot.getString("Foto");
                        String po=documentSnapshot.getString("Correo");
                        String ppa=documentSnapshot.getString("Nombre");
                        String ppe=documentSnapshot.getString("Telefono");
                        String ppi=documentSnapshot.getString("Saldo");
                        String ppu=documentSnapshot.getString("Tipo_cliente");

                        String pa1=pa.toString();
                        if (Con1.equals(pa1)){
                            Usua.getInstance().setTipoini(pe);
                            Usua.getInstance().setUsuarioini(Usu1);
                            Usua.getInstance().setNombreini(ppa);
                            Usua.getInstance().setContrasenaini(pa1);
                            Usua.getInstance().setCorreoini(po);
                            Usua.getInstance().setFotoini(pi);
                            Usua.getInstance().setTelefonoini(ppe);
                            Usua.getInstance().setSaldoini(ppi);
                            Usua.getInstance().setTipos_cli(ppu);
                            Usss=Usu1;
                            Error=0;
                        }else{
                            Toast.makeText(Login.this, "Contraseña erronea", Toast.LENGTH_SHORT).show();
                            Error=1;
                            progressDialog.dismiss();
                        }
                    }else{
                        Toast.makeText(Login.this, "El documento no tiene el campo Contraseña", Toast.LENGTH_SHORT).show();
                        Error=1;
                    }
                }else{
                    Toast.makeText(Login.this, "Usuario no existente", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Error=1;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error al encontrar el documento", Toast.LENGTH_SHORT).show();
                Error=1;
            }
        });


    }




}