package Proveedores;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestioninventario.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Productos.ErrorStringsFragment;

public class RegistroProveedorActivity extends AppCompatActivity {

    private ArrayList<Proveedor> lista_proveedor=new ArrayList<Proveedor>();
    private Proveedor proveedor;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    EditText editTextNombre,editTextProducto,editTextTelefono,editTextDireccion;

    private final int Gallery_REQUEST_CODE = 1;
    private Uri image_url = null;
    private ImageView imagenProveedor;
    StorageReference storageReference;
    String imagen2;




    Button boton_aceptar,boton_salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_proveedor);
        storageReference= FirebaseStorage.getInstance().getReference();

        //cargarInformacion(lista_proveedor);

        imagenProveedor=findViewById(R.id.imagenProveedor);
        imagenProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        boton_salir=findViewById(R.id.boton_salir_registro);
        boton_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProveedor();
            }
        });

        boton_aceptar = findViewById(R.id.boton_aceptar_registro);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!validarInformación())){
                    irError();
                }else{
                    if((validarProveedor(editTextNombre.getText().toString()))){
                        irConfirmar();
                    }else{ErrorStringsFragment exampleDialog= new ErrorStringsFragment();
                        exampleDialog.show(getSupportFragmentManager(),"Error");
                    }
                }
            }
        });

        editTextNombre= findViewById(R.id.editTextNombre);
        editTextProducto= findViewById(R.id.editTextProducto);
        editTextTelefono= findViewById(R.id.editTextTelefono);
        editTextDireccion= findViewById(R.id.editTextDireccion);
    }

    private void irError() {
        ErrorCamposObligatoriosProveedorFragment exampleDialog= new ErrorCamposObligatoriosProveedorFragment();
        exampleDialog.show(getSupportFragmentManager(),"Error");
    }

    private void irConfirmar() {
        String telefono,direccion, ima;


        if(image_url==null){
            ima="noFoto";
        }else{
            ima= image_url.getLastPathSegment().toString();
            cargarFoto();
        }

        if(editTextTelefono.getText().toString().isEmpty()){
            telefono="-";
        }else{
            telefono=editTextTelefono.getText().toString();
        }
        if(editTextDireccion.getText().toString().isEmpty()){
            direccion="-";
        }else{
            direccion=editTextDireccion.getText().toString();
        }
        ConfirmarProveedorFragment exampleDialog= new ConfirmarProveedorFragment(editTextNombre.getText().toString(),editTextProducto.getText().toString(),telefono,direccion, ima);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }

    private void irProveedor() {
        Intent intent = new Intent(RegistroProveedorActivity.this, ProveedoresActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isInteger(String text) {
        int v;
        try {
            v=Integer.parseInt(text);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    public boolean validarInformación(){
        Boolean val=true;
        if (editTextNombre.getText().toString().isEmpty()){
            editTextNombre.setError("Campo Obligatorio");
            val=false;
        }
        if (editTextProducto.getText().toString().isEmpty()){
            editTextProducto.setError("Campo Obligatorio");
            val=false;
        }

        return val;
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
    public void registrarProveedor(String name, String producto, String telefono, String direccion, String imagen){
        Map<String, Object> proveedor = new HashMap<>();
        proveedor.put("nombre", name);
        proveedor.put("producto", producto);
        proveedor.put("telefono",telefono);
        proveedor.put("direccion",direccion);
        proveedor.put("imagen", imagen);

        final DatabaseReference myRef = database.getReference().child(user.getUid()).child("Proveedores");
        myRef.push().setValue(proveedor);
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
            imagenProveedor.setImageURI(image_url);
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
