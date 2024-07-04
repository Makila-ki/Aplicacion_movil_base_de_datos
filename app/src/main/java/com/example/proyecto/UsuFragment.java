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

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.Home;
import com.example.proyecto.model.Usua;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class UsuFragment extends Fragment {
    FragmentManager tata;
    ImageView ima_usu;
    String Us,Fo;
    StorageReference storageReference;
    String storage_path = "Produ/", dowload_uri;
    Button btn_Cerrar;
    Button btn_foto_ag, btn_foto_borrar, btn_editar_usu;
    TextView el_us, el_SA;
    private Uri imagen_uri;
    String photoo="photo", idd;
    private FirebaseFirestore mfiStoree;
    ProgressDialog progressDialog;
    private static final int COD_SEL_IMAGE=300;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mali = inflater.inflate(R.layout.fragment_usu, container, false);
        btn_Cerrar = mali.findViewById(R.id.Btn_Cerrar);
        el_us=mali.findViewById(R.id.Us_Para_ver);
        ima_usu= mali.findViewById(R.id.Ima_foto_AD);
        btn_foto_ag=mali.findViewById(R.id.Agre_ima_us);
        btn_foto_borrar=mali.findViewById(R.id.Elimi_ima_us);
        btn_editar_usu=mali.findViewById(R.id.Editar);
        storageReference= FirebaseStorage.getInstance().getReference();
        mfiStoree= FirebaseFirestore.getInstance();
        tata=getActivity().getSupportFragmentManager();
        Fo=Usua.getInstance().getFotoini();
        Us= Usua.getInstance().getUsuarioini();
        try {
            if(!Fo.equals("")){
                Picasso.with(getContext()).load(Fo).resize(400,400).into(ima_usu);
            }
        }catch (Exception e){

        }


        el_us.setText(Us);

        btn_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cerrar(Us);
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
        btn_foto_ag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        btn_foto_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agregar_dato("");
            }
        });
        btn_editar_usu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editar_Cliente editarCliente= new Editar_Cliente();
                Bundle bundle = new Bundle();
                bundle.putString("id_us", Us);
                editarCliente.setArguments(bundle);
                editarCliente.show(tata, "open frag");
            }
        });

        return mali;
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
                subirPhoto(imagen_uri, Us);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri imagenUri, String us) {
        Calendar calendar = Calendar.getInstance();
        int año =calendar.get(Calendar.YEAR);
        int mes =calendar.get(Calendar.MONDAY)+1;
        int dia =calendar.get(Calendar.DAY_OF_MONTH);
        int hora=calendar.get(Calendar.HOUR_OF_DAY);
        int minuto=calendar.get(Calendar.MINUTE);
        int segundo=calendar.get(Calendar.SECOND);
        String ruta_storage_pho= storage_path+""+photoo+""+ us+"_"+segundo+"_"+minuto+"_"+hora+"_"+dia+"_"+mes+"_"+año;
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

        mfiStoree.collection("UsuariosDto").document(Us)
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Usua.getInstance().setFotoini(dowloadUri);
                        String s=Usua.getInstance().getFotoini();
                        if(s.equals("")){
                            imagen_uri= Uri.parse("");
                            Picasso.with(getContext()).load(imagen_uri).resize(400,400).into(ima_usu);
                        }else{
                            Picasso.with(getContext()).load(dowloadUri).resize(400,400).into(ima_usu);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No se puedo agregar Id", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Cerrar(String Usito) {
        Map<String, Object> user = new HashMap<>();
        user.put("Id_sesi", "");
        mfiStoree.collection("UsuariosDto").document(Usito).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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