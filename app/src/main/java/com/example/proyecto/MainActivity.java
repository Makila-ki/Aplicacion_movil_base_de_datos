package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.model.Usua;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    String deviceId;
    private FirebaseFirestore mfi;

    int Encontrado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mfi= FirebaseFirestore.getInstance();
        readDateCo(deviceId);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                verifica(Encontrado);
            }
        },2000);




    }

    private void verifica(int deviceId){
        Handler handler1= new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (deviceId==1) {
                    String tip= Usua.getInstance().getTipoini();
                    String T_ad="Administrador", T_cl="Cliente";
                    if(T_ad.equals(tip)){
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
                        finish();
                    } else if (T_cl.equals(tip)) {
                        Intent intent = new Intent(MainActivity.this, Home_cli.class);
                        startActivity(intent);
                        finish();
                    }

                }else{
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);

    }

    private void readDateCo(String corrito2) {
        // Obtener la referencia a la colección

        Query queryy = mfi.collection("UsuariosDto").whereEqualTo("Id_sesi",corrito2);

        queryy.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot DocumentSnapshot:queryDocumentSnapshots){
                    String Usitoto, Tipipi;
                    Object pa=DocumentSnapshot.get("Contrasena");
                    Usitoto=DocumentSnapshot.getString("Usuario");
                    Tipipi=DocumentSnapshot.getString("Tipo_us");
                    String pi=DocumentSnapshot.getString("Foto");
                    String po=DocumentSnapshot.getString("Correo");
                    String ppa=DocumentSnapshot.getString("Nombre");
                    String ppe=DocumentSnapshot.getString("Telefono");
                    String ppi=DocumentSnapshot.getString("Saldo");
                    String ppu=DocumentSnapshot.getString("Tipo_cliente");
                    String pa1=pa.toString();

                    Usua.getInstance().setTipoini(Tipipi);
                    Usua.getInstance().setUsuarioini(Usitoto);
                    Usua.getInstance().setNombreini(ppa);
                    Usua.getInstance().setContrasenaini(pa1);
                    Usua.getInstance().setCorreoini(po);
                    Usua.getInstance().setFotoini(pi);
                    Usua.getInstance().setTelefonoini(ppe);
                    Usua.getInstance().setSaldoini(ppi);
                    Usua.getInstance().setTipos_cli(ppu);
                    Encontrado=1;
                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Encontrado=0;
            }
        });
    }


}