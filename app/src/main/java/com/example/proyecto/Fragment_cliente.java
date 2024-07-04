package com.example.proyecto;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.model.Usua;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Fragment_cliente extends Fragment {
    private final int INTERVAL = 1000;
    FragmentManager tata;
    ImageView image_cli;


    String Clii,Fot;
    StorageReference storageReference;
    String storage_path = "Produ/", dowload_uri;
    Button btn_Cerrarr;
    Button btn_Agre_fot, btn_bora_fot, btn_edi_cli, btn_edi_app;

    TextView el_cli, SALDOI;
    private FirebaseFirestore mfiStoree;
    ProgressDialog progressDialog;
    private Uri imagen_uri;
    String photoo="photo", idd;
    private static final int COD_SEL_IMAGE=300;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Cerrarmaki = inflater.inflate(R.layout.fragment_cliente, container, false);
        btn_Cerrarr=Cerrarmaki.findViewById(R.id.Btn_Cerrar_cli);
        el_cli=Cerrarmaki.findViewById(R.id.Cl_para_ver);
        image_cli=Cerrarmaki.findViewById(R.id.Ima_foto_CL);
        btn_Agre_fot=Cerrarmaki.findViewById(R.id.Agre_ima_cli);
        btn_bora_fot=Cerrarmaki.findViewById(R.id.Elimi_ima_cli);
        btn_edi_cli=Cerrarmaki.findViewById(R.id.Editar_cli);
        btn_edi_app=Cerrarmaki.findViewById(R.id.Editar_app);
        SALDOI=Cerrarmaki.findViewById(R.id.Saldo_cli);
        storageReference= FirebaseStorage.getInstance().getReference();
        mfiStoree=FirebaseFirestore.getInstance();
        tata=getActivity().getSupportFragmentManager();
        Clii= Usua.getInstance().getUsuarioini();
        Fot=Usua.getInstance().getFotoini();

        SALDOI.setText("$"+Usua.getInstance().getSaldoini());
        el_cli.setText(Clii);
        try {
            if(!Fot.equals("")){
                Picasso.with(getContext()).load(Fot).resize(400,400).into(image_cli);
            }
        }catch (Exception e){

        }

        btn_Cerrarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cerrar_cli(Clii);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Cerrando Sesion");
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                },1000);
            }
        });
        btn_Agre_fot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        btn_bora_fot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agregar_dato("");
            }
        });
        btn_edi_cli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editar_Cliente editarCliente= new Editar_Cliente();
                Bundle bundle = new Bundle();
                bundle.putString("id_us", Clii);
                editarCliente.setArguments(bundle);
                editarCliente.show(tata, "open frag");
            }
        });
        btn_edi_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Cerrarmaki.getContext(), Ajustes_tema.class);
                startActivity(intent);
            }
        });
        startTimer();

        return Cerrarmaki;
    }
    private void startTimer() {
        new CountDownTimer(Long.MAX_VALUE, INTERVAL) {
            public void onTick(long millisUntilFinished) {
                // Actualiza el contenido del TextView cada vez que se complete un intervalo
                updateTextView();
            }

            public void onFinish() {
                // Aquí puedes realizar acciones al terminar el temporizador, si es necesario
            }
        }.start();
    }
    private void updateTextView() {
        // Actualiza el contenido del TextView según sea necesario
        // Por ejemplo:
        SALDOI.setText("$"+Usua.getInstance().getSaldoini());
    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== RESULT_OK){
            if(requestCode==COD_SEL_IMAGE){
                imagen_uri=data.getData();
                subirPhoto_cli(imagen_uri, Clii);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto_cli(Uri imagenUri, String clii) {
        Calendar calendar = Calendar.getInstance();
        int año =calendar.get(Calendar.YEAR);
        int mes =calendar.get(Calendar.MONDAY)+1;
        int dia =calendar.get(Calendar.DAY_OF_MONTH);
        int hora=calendar.get(Calendar.HOUR_OF_DAY);
        int minuto=calendar.get(Calendar.MINUTE);
        int segundo=calendar.get(Calendar.SECOND);
        String ruta_storage_pho= storage_path+""+photoo+""+ clii+"_"+segundo+"_"+minuto+"_"+hora+"_"+dia+"_"+mes+"_"+año;
        StorageReference reference= storageReference.child(ruta_storage_pho);
        reference.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()){
                    if (uriTask.isSuccessful()){
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dowload_uri = uri.toString();
                                Agregar_dato(dowload_uri);
                                Toast.makeText(getContext(), "Foto agregada", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "aqui hay un error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Agregar_dato(String dowloadUri) {
        Map<String, Object> user = new HashMap<>();
        user.put("Foto", dowloadUri);

        mfiStoree.collection("UsuariosDto").document(Clii)
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Usua.getInstance().setFotoini(dowloadUri);
                        String s=Usua.getInstance().getFotoini();
                        if(s.equals("")){
                            Toast.makeText(getContext(), "Dato Borrado", Toast.LENGTH_SHORT).show();
                            imagen_uri= Uri.parse("");
                            Picasso.with(getContext()).load(imagen_uri).resize(400,400).into(image_cli);
                        }else{
                            Picasso.with(getContext()).load(dowloadUri).resize(400,400).into(image_cli);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No se puedo agregar Id", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void Cerrar_cli(String clii) {
        Map<String, Object> user = new HashMap<>();
        user.put("Id_sesi", "");
        mfiStoree.collection("UsuariosDto").document(clii).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No se puedo agregar Id", Toast.LENGTH_SHORT).show();
            }
        });
    }
}