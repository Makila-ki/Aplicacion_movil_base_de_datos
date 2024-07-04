package com.example.proyecto;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.model.Usua;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class CarFragment extends Fragment {

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

    ProgressDialog progressDialog;
    private static final int COD_SEL_IMAGE=300;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cartmaki=inflater.inflate(R.layout.fragment_car, container, false);
        btn_insepro=cartmaki.findViewById(R.id.IngresarPro);
        nompro=cartmaki.findViewById(R.id.NombrePro);
        tipopro=cartmaki.findViewById(R.id.TipoPro);
        marpro=cartmaki.findViewById(R.id.MarcaPro);
        canpro=cartmaki.findViewById(R.id.Cantidad);
        prepro=cartmaki.findViewById(R.id.PrecioPro);
        mospro= cartmaki.findViewById(R.id.checkBox);
        photo=cartmaki.findViewById(R.id.Para_ima);
        ro=cartmaki.findViewById(R.id.Urlima);
        btn_foto_agre=cartmaki.findViewById(R.id.Agre_ima);
        btn_foto_eli=cartmaki.findViewById(R.id.Elimi_ima);

        storageReference= FirebaseStorage.getInstance().getReference();

        mfi= FirebaseFirestore.getInstance();
        mficua = FirebaseFirestore.getInstance();

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
                                    Agregarpro(Nompro, Tippro, Marpro, Canpro, Prepro, Estpro,dowload_uri);

                                    Handler handler1= new Handler();
                                    handler1.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Error!=1){
                                                progressDialog.dismiss();
                                                nompro.setText("");
                                                tipopro.setText("");
                                                marpro.setText("");
                                                canpro.setText("");
                                                prepro.setText("");
                                                imagen_uri= Uri.parse("");
                                                Picasso.with(getContext()).load(imagen_uri).resize(400,400).into(photo);
                                                mospro.setChecked(false);
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


        return cartmaki;
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

    private void Agregarpro(String nompro, String tippro, String marpro, String canpro, String prepro,String estpro, String dire) {
        Map<String, Object> user = new HashMap<>();
        user.put("Nombre_pro", nompro);
        user.put("Tipo_pro", tippro);
        user.put("Marca_pro", marpro);
        user.put("Cantidad_pro", canpro);
        user.put("Precio_pro", prepro);
        user.put("Estado", estpro);
        user.put("Photo", dire);

        mfi.collection("ProductosDto").document(nompro+"_"+tippro)
                .set(user)
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Error=Error+1;

                    }
                });
    }

}