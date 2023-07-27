package Proveedores;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestioninventario.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConsultarProveedorActivity extends AppCompatActivity {

    Button boton_aceptar,boton_eliminar,boton_modificar;
    TextView tvNombre,tvProducto,tvTelefono,tvDireccion;
    ImageView tvImagen;
    String uid,nombre,producto,telefono,direccion,imagen;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_proveedores);

        tvNombre=findViewById(R.id.textViewNombreProveedor);
        tvProducto=findViewById(R.id.textViewProductoC);
        tvTelefono=findViewById(R.id.textViewTelefono);
        tvDireccion=findViewById(R.id.textViewDireccion);
        tvImagen = findViewById(R.id.imagenProveedor);

        Bundle rd =getIntent().getExtras();
        nombre=rd.getString("nombre");
        producto=rd.getString("producto");
        telefono=rd.getString("telefono");
        direccion=rd.getString("direccion");
        imagen = rd.getString("imagen");
        uid=rd.getString("uid");
        tvNombre.setText(nombre);
        tvProducto.setText(producto);
        tvTelefono.setText(telefono);
        tvDireccion.setText(direccion);
        modificarImagen(imagen);



        boton_aceptar=findViewById(R.id.boton_aceptar_consulta_proveedor);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                irProveedores();
            }
        });


        boton_eliminar=findViewById(R.id.boton_eliminar_proveedor);
        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irConfirmar();
            }
        });

        boton_modificar=findViewById(R.id.boton_modificar_proveedor);
        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irModificar();
            }
        });
    }
    public void modificarImagen(String img){
        if(img!="0"){

            StorageReference reference = storageReference.child("proveedores/"+img);
            long Maxbytes=16384*16384;

            reference.getBytes(Maxbytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    tvImagen.setImageBitmap(bitmap);
                }
            });
        }
    }
    public void irModificar(){
        Bundle ed= new Bundle();
        ed.putString("nombre",nombre);
        ed.putString("producto",producto);
        ed.putString("telefono",telefono);
        ed.putString("direccion",direccion);
        ed.putString("imagen",imagen);
        ed.putString("uid",uid);
        Intent intent= new Intent(ConsultarProveedorActivity.this, ModificarProveedorActivity.class);
        intent.putExtras(ed);
        startActivity(intent);
        finish();
        Log.d("hola","paso ventana");

    }
    private void irConfirmar() {
        ConfirmarEliminacionProveedorFragment exampleDialog= new ConfirmarEliminacionProveedorFragment(uid);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }

    private void irProveedores() {
        Intent intent = new Intent(ConsultarProveedorActivity.this, ProveedoresActivity.class);
        startActivity(intent);
        finish();
    }
    public void eliminarProveedor(String uid){
        database.getReference().child(user.getUid()).child("Proveedores").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            database.getReference().child(user.getUid()).child("Proveedores").child(i).removeValue();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
