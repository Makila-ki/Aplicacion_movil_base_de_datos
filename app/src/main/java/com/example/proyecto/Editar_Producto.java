package com.example.proyecto;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.model.Usua;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Editar_Producto extends DialogFragment {
    View PARA;
    ImageButton sali;
    ImageView photo;
    TextView ro;
    EditText nompro, tipopro, marpro, canpro, prepro;
    Button btn_insepro,btn_foto_agre, btn_foto_eli;
    int Error=0, cantDo;
    private FirebaseFirestore mfi, mficua;
    CheckBox mospro;
    StorageReference storageReference;

    String storage_path = "Produ/", dowload_uri;

    private Uri imagen_uri;
    String photoo="photo", idd;
    String id_usu, foto;

    ProgressDialog progressDialog;
    private static final int COD_SEL_IMAGE=300;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_usu= getArguments().getString("id_usu");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View editor=inflater.inflate(R.layout.fragment_editar__producto, container, false);
        PARA=editor;
        btn_insepro=editor.findViewById(R.id.Editar_pro);
        nompro=editor.findViewById(R.id.NombrePro_ed);
        tipopro=editor.findViewById(R.id.TipoPro_ed);
        marpro=editor.findViewById(R.id.MarcaPro_ed);
        canpro=editor.findViewById(R.id.Cantidad_ed);
        prepro=editor.findViewById(R.id.PrecioPro_ed);
        mospro= editor.findViewById(R.id.checkBox_ed);
        photo=editor.findViewById(R.id.edi_ima);
        btn_foto_agre=editor.findViewById(R.id.agre_ima_edi);
        btn_foto_eli=editor.findViewById(R.id.elim_ima_edi);

        storageReference= FirebaseStorage.getInstance().getReference();

        mfi= FirebaseFirestore.getInstance();
        mficua = FirebaseFirestore.getInstance();
        if(id_usu==null|| id_usu==""){

        }else{
            readDateCo(id_usu);
            btn_foto_agre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPhoto();
                }
            });
            btn_foto_eli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagen_uri= Uri.parse("");
                    Picasso.with(getContext()).load(imagen_uri).resize(400,400).into(photo);
                }
            });
            btn_insepro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeTe();
                    Error=0;
                    String Nompro = nompro.getText().toString().trim();
                    String Tippro = tipopro.getText().toString().trim();
                    String Marpro = marpro.getText().toString().trim();
                    String Canpro = canpro.getText().toString().trim();
                    String Prepro = prepro.getText().toString().trim();
                    String Estpro = ""+mospro.isChecked();

                    if (Nompro.isEmpty()&&Tippro.isEmpty()&&Marpro.isEmpty()){
                        Toast.makeText(getContext(), "Existe un dato vacio", Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            if (Prepro != null&&Canpro!=null) {
                                subirPhoto(imagen_uri, Nompro, Tippro);
                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Agregando Producto");
                                progressDialog.show();
                                Handler handler2= new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Agregar_alcarro_f(Nompro, Tippro, Marpro, Canpro, Prepro, Estpro,dowload_uri);

                                        Handler handler1= new Handler();
                                        handler1.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(Error!=1){
                                                    progressDialog.dismiss();
                                                    dismiss();
                                                    Toast.makeText(getContext(), "Producto Agregado", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getContext(), "Producto No Agregado", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        },2000);
                                    }
                                },5000);

                            }else{

                            }
                        }catch (NumberFormatException nft){
                            Toast.makeText(getContext(), "Existe un dato vacio", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            });

        }


        return editor;
    }
    private void Agregar_alcarro_f(String nom1, String tip1, String mar1, String can1, String pre1, String est1, String fot1 ) {
        Map<String, Object> user = new HashMap<>();
        user.put("Nombre_pro", nom1);
        user.put("Tipo_pro", tip1);
        user.put("Marca_pro", mar1);
        user.put("Cantidad_pro", can1);
        user.put("Precio_pro", pre1);
        user.put("Estado", est1);
        user.put("Photo", fot1);
        mfi.collection("ProductosDto").document(nom1+"_"+tip1).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
                String marca= documentSnapshot.getString("Marca_pro");
                Boolean estado= Boolean.valueOf(documentSnapshot.getString("Estado_pro"));
                String tipo= documentSnapshot.getString("Tipo_pro");
                foto=documentSnapshot.getString("Photo");
                imagen_uri= Uri.parse(foto);
                Picasso.with(PARA.getContext()).load(foto).resize(400,400).into(photo);
                nompro.setText(nombre);
                tipopro.setText(tipo);
                marpro.setText(marca);
                canpro.setText(cantidad);
                prepro.setText(precio);
                mospro.setChecked(estado);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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
                Picasso.with(getContext()).load(imagen_uri).resize(400,400).into(photo);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void subirPhoto(Uri imagenUri, String NO, String tip) {
        String ruta_storage_pho= storage_path+""+photoo+""+ Usua.getInstance().getUsuarioini()+""+NO+""+tip;
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
    private void closeTe() {
        View view = getActivity().getCurrentFocus();
        if (view!=null){
            InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}