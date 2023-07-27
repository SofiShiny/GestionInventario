package Productos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestioninventario.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import Proveedores.ConfirmarModificacionProveedorFragment;

public class ModificarProductoActivity extends AppCompatActivity {

    Button boton_modificar;
    ImageButton boton_atras;
    ImageView imagenP;
    String nom,pre,can,id,uid,max,min,imagen, imagen2, cA1,nA1,cA2,nA2,cA3,nA3,cA4,nA4,cA5,nA5;
    TextView tvNombre,tvCA1,tvCA2,tvCA3,tvCA4,tvCA5;
    EditText edNombre,edId,edPrecio,edCantidad,edMax,edMin,edCA1,edCA2,edCA3,edCA4,edCA5;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private final int Gallery_REQUEST_CODE = 1;
    private Uri image_url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_producto);
        tvNombre=findViewById(R.id.textViewNombre);
        edPrecio=findViewById(R.id.editTextPrecioConsultar);
        edCantidad=findViewById(R.id.editTextCantidadConsultar);
        edId=findViewById(R.id.editTextIDConsultar);
        edMax=findViewById(R.id.editTextStockMaxConsultar);
        edMin=findViewById(R.id.editTextStockMinConsultar);
        edCA1=findViewById(R.id.editTextCarac1Consultar);
        edCA2=findViewById(R.id.editTextCarac2Consultar);
        edCA3=findViewById(R.id.editTextCarac3Consultar);
        edCA4=findViewById(R.id.editTextCarac4Consultar);
        edCA5=findViewById(R.id.editTextCarac5Consultar);
        tvCA1=findViewById(R.id.tvCA1);
        tvCA2=findViewById(R.id.tvCA2);
        tvCA3=findViewById(R.id.tvCA3);
        tvCA4=findViewById(R.id.tvCA4);
        tvCA5=findViewById(R.id.tvCA5);
        imagenP=findViewById(R.id.imagen_producto);
        imagenP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {uploadPhoto();
                 }
        });
        Bundle rd =getIntent().getExtras();
        nom=rd.getString("nombre");
        id=rd.getString("id");
        pre=rd.getString("precio");
        can=rd.getString("cantidad");
        max=rd.getString("sMax");
        min=rd.getString("sMin");
        imagen=rd.getString("imagen");
        uid=rd.getString("uid");
        cA1=rd.getString("cCarA1");
        nA1=rd.getString("nCarA1");
        cA2=rd.getString("cCarA2");
        nA2=rd.getString("nCarA2");
        cA3=rd.getString("cCarA3");
        nA3=rd.getString("nCarA3");
        cA4=rd.getString("cCarA4");
        nA4=rd.getString("nCarA4");
        cA5=rd.getString("cCarA5");
        nA5=rd.getString("nCarA5");
        Log.d("hola",cA1);
        Log.d("hola","recibi inf");
        modificarCampos(nom,id,pre,can,max,min, imagen);
        modificarCamposAdicionales(cA1,nA1,cA2,nA2,cA3,nA3,cA4,nA4,cA5,nA5);
        Log.d("HolaBuga",tvNombre.getText().toString());
        boton_modificar=findViewById(R.id.boton_modificar_producto);
        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               irConfirmar();

            }
        });

        boton_atras=findViewById(R.id.boton_salir_modificar);
        boton_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irHome();
            }
        });
    }

    /*private void irConfirmar2() {
        ConfirmarModificacionProductoFragment exampleDialog= new ConfirmarModificacionProductoFragment();
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }*/

    public void modificarDatos(String nom, String id, String pre, String can, String max, String min, String cA1, String nA1, String cA2, String nA2, String cA3, String nA3, String cA4,String nA4, String cA5, String nA5, String uid, String imagen){
        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        Log.d("HolaBuga2","Holabug3");
                        Log.d("HolaBuga3","Holabug3");
                        String i= ds.getKey();
                        if (uid.equals(i)) {
                            Map<String, Object> producto = new HashMap<>();
                            producto.put("nombre", nom);
                            producto.put("precio", pre);
                            producto.put("cantidad", can);
                            producto.put("id", id);
                            producto.put("sMax", max);
                            producto.put("sMin", min);
                            producto.put("nCaracteristicaA1", nA1);
                            producto.put("cCaracteristicaA1", cA1);
                            producto.put("nCaracteristicaA2", nA2);
                            producto.put("cCaracteristicaA2", cA2);
                            producto.put("nCaracteristicaA3", nA3);
                            producto.put("cCaracteristicaA3", cA3);
                            producto.put("nCaracteristicaA4", nA4);
                            producto.put("cCaracteristicaA4", cA4);
                            producto.put("nCaracteristicaA5", nA5);
                            producto.put("cCaracteristicaA5", cA5);
                            producto.put("imagen",imagen);
                            database.getReference().child(user.getUid()).child("Productos").child(uid).updateChildren(producto);
                        }

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public boolean validarVacios(String texto){
        for (int i=0;i<texto.length();i++){
            if (texto.charAt(i)!= ' ')
                return false;
        }
        return true;
    }
    private void irHome() {
        Intent intent = new Intent(ModificarProductoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void modificarCampos(String nom, String id, String pre, String can, String max,String min, String imagen){
        tvNombre.setText(nom);
        edId.setText(id);
        edPrecio.setText(pre);
        edCantidad.setText(can);
        edCantidad.setEnabled(false);
        edMax.setText(max);
        edMin.setText(min);

        if(imagen!="noFoto"){

            StorageReference reference = storageReference.child("producto/"+imagen);
            long Maxbytes=1024*1024;

            reference.getBytes(Maxbytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imagenP.setImageBitmap(bitmap);
                }
            });
        }


    }
    private void irConfirmar() {
        String max,min,ima, carA1,carA2,carA3,carA4,carA5,nCA1,nCA2,nCA3,nCA4,nCA5;
       if(image_url==null){
            ima=imagen;
        }else{
            ima= image_url.getLastPathSegment();
            cargarFoto();
        }
        Log.d("Hola123",imagen);
        if (!edCA1.isShown()){
            carA1="0";
            nCA1="0";
        } else {
            carA1=edCA1.getText().toString();
            nCA1=tvCA1.getText().toString();
        }
        if(!edCA2.isShown()){
            carA2="0";
            nCA2="0";
        }else {
            carA2=edCA2.getText().toString();
            nCA2=tvCA2.getText().toString();
        }
        if (!edCA3.isShown()){
            carA3="0";
            nCA3="0";
        }else {
            carA3=edCA3.getText().toString();
            nCA3=tvCA3.getText().toString();
        }
        if (!edCA4.isShown()){
            carA4="0";
            nCA4="0";
        }else {
            carA4=edCA4.getText().toString();
            nCA4=tvCA4.getText().toString();
        }
        if (!edCA5.isShown()){
            carA5="0";
            nCA5="0";
        }else{
            carA5=edCA5.getText().toString();
            nCA5=tvCA5.getHint().toString();
        }
        if ((edMax.getText().toString().isEmpty()) || (validarVacios(edMax.getText().toString()))) {
            max="1000";
        }else{
            max=edMax.getText().toString();
        }
        if (edMin.getText().toString().isEmpty() || (validarVacios(edMin.getText().toString()))){
            min="1";
        }else {
            min=edMin.getText().toString();
        }

        ConfirmarModificacionProductoFragment exampleDialog= new ConfirmarModificacionProductoFragment(tvNombre.getText().toString(),edPrecio.getText().toString(),edCantidad.getText().toString(),edId.getText().toString(),max,min, carA1,nCA1,carA2,nCA2,carA3,nCA3,carA4,nCA4,carA5,nCA5,uid,ima);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }
    public void modificarCamposAdicionales( String cA1, String nA1,String cA2, String nA2,String cA3, String nA3,String cA4, String nA4,String cA5, String nA5){
        if (nA1.equals("0")){
            edCA1.setVisibility(View.INVISIBLE);
            tvCA1.setVisibility(View.INVISIBLE);
        }else{
            edCA1.setText(cA1);
            tvCA1.setText(nA1);
        }
        if (nA2.equals("0")){
            edCA2.setVisibility(View.INVISIBLE);
            tvCA2.setVisibility(View.INVISIBLE);
        }else{
            edCA2.setText(cA2);
            tvCA2.setText(nA2);
        }
        if (nA3.equals("0")){
            edCA3.setVisibility(View.INVISIBLE);
            tvCA3.setVisibility(View.INVISIBLE);
        }else{
            edCA3.setText(cA3);
            tvCA3.setText(nA3);
        }
        if (nA4.equals("0")){
            edCA4.setVisibility(View.INVISIBLE);
            tvCA4.setVisibility(View.INVISIBLE);
        }else{
            edCA4.setText(cA4);
            tvCA4.setText(nA4);
        }
        if (nA5.equals("0")){
            edCA5.setVisibility(View.INVISIBLE);
            tvCA5.setVisibility(View.INVISIBLE);
        }else{
            edCA5.setText(cA5);
            tvCA5.setText(nA5);
        }


    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, Gallery_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Gallery_REQUEST_CODE) && (resultCode == RESULT_OK)) {
            image_url = data.getData();
            imagenP.setImageURI(image_url);
        }
    }

    private void cargarFoto(){
        StorageReference reference = storageReference.child("producto/").child(image_url.getLastPathSegment());
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {imagen2= uri.toString();}
                });
            }
        });
    };
}