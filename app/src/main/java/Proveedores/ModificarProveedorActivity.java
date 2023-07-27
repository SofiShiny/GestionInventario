package Proveedores;

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

import com.example.gestioninventario.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import Productos.ErrorStringsFragment;

public class ModificarProveedorActivity extends AppCompatActivity {

    Button boton_modificar;
    ImageButton boton_atras;
    EditText edNombre,edProducto,edTelefono,edDireccion;
    String nombre,producto,telefono,direccion,uid,imagen, imagen2;
    ImageView imagenP;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private final int Gallery_REQUEST_CODE = 1;
    private Uri image_url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_proveedor);
        edNombre=findViewById(R.id.textViewNombreProveedor);
        edProducto=findViewById(R.id.textViewProductoC);
        edTelefono=findViewById(R.id.textViewTelefono);
        edDireccion=findViewById(R.id.textViewDireccion);
        imagenP=findViewById(R.id.imagenProveedor);
        imagenP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        Log.d("hola","empezo ventana");
       Bundle rd =getIntent().getExtras();
        nombre=rd.getString("nombre");
        producto=rd.getString("producto");
        telefono=rd.getString("telefono");
        direccion=rd.getString("direccion");
        imagen=rd.getString("imagen");
        uid=rd.getString("uid");
        Log.d("hola","recibio info");
        modificarCampos(nombre,producto,telefono,direccion,imagen);


        boton_modificar=findViewById(R.id.boton_modificar_proveedor);
        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!validarInformación())){
                    irError();
                }else{
                    if((validarProveedor(edNombre.getText().toString()))){
                        irConfirmar();
                    }else{
                        ErrorStringsFragment exampleDialog= new ErrorStringsFragment();
                        exampleDialog.show(getSupportFragmentManager(),"Error");
                    }
                }

            }
        });

        boton_atras=findViewById(R.id.boton_salir_modificar);
        boton_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProveedores();
            }
        });
    }
    public boolean validarProveedor(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
                return false;
            }
        }
        return true;
    }
    private void irError() {
        ErrorCamposObligatoriosProveedorFragment exampleDialog= new ErrorCamposObligatoriosProveedorFragment();
        exampleDialog.show(getSupportFragmentManager(),"Error");
    }

    private void irConfirmar() {
        String tel, dir, ima;

        if(image_url==null){
            ima=imagen;
        }else{
            ima= image_url.getLastPathSegment();
            cargarFoto();
        }
        if (edTelefono.getText().toString().isEmpty()){
            tel="-";
        }else{
            tel=edTelefono.getText().toString();
        }
        if (edDireccion.getText().toString().isEmpty()){
            dir="-";
        }else{
            dir=edDireccion.getText().toString();
        }
        ConfirmarModificacionProveedorFragment exampleDialog= new ConfirmarModificacionProveedorFragment(edNombre.getText().toString(),edProducto.getText().toString(),tel,dir,ima,uid);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }
    private void irProveedores() {
        Intent intent = new Intent(ModificarProveedorActivity.this, ProveedoresActivity.class);
        startActivity(intent);
        finish();
    }
    public void modificarDatos(String nombre, String producto, String telefono, String direccion, String imagen, String uid){
        database.getReference().child(user.getUid()).child("Proveedores").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String i= ds.getKey();
                        if (uid.equals(i)) {
                            Map<String, Object> proveedor = new HashMap<>();
                            proveedor.put("nombre", nombre);
                            proveedor.put("producto", producto);
                            proveedor.put("telefono", telefono);
                            proveedor.put("direccion", direccion);
                            proveedor.put("imagen", imagen);
                            database.getReference().child(user.getUid()).child("Proveedores").child(uid).updateChildren(proveedor);
                        }

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public boolean validarInformación(){
        Boolean val=true;
        if (edNombre.getText().toString().isEmpty()){
            edNombre.setError("Campo Obligatorio");
            val=false;
        }
        if (edProducto.getText().toString().isEmpty()){
            edProducto.setError("Campo Obligatorio");
            val=false;
        }

        return val;
    }

    public void modificarCampos(String nombre, String producto, String telefono, String direccion, String imagen){
        edNombre.setText(nombre);
        edProducto.setText(producto);
        edTelefono.setText(telefono);
        edDireccion.setText(direccion);

        if(imagen!="noFoto"){

            StorageReference reference = storageReference.child("proveedores/"+imagen);
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
        StorageReference reference = storageReference.child("proveedores/").child(image_url.getLastPathSegment());
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