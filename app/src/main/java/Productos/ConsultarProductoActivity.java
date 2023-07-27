package Productos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ConsultarProductoActivity extends AppCompatActivity {

    Button boton_aceptar,boton_eliminar,boton_modificar;
    TextView tvNombre,tvCA1,tvCA2,tvCA3,tvCA4,tvCA5;
    EditText edNombre,edId,edPrecio,edCantidad,edMax,edMin,edCA1,edCA2,edCA3,edCA4,edCA5;
    ImageView edImagen;
    String nom,pre,can,id,uid,max,min,imagen, cA1,nA1,cA2,nA2,cA3,nA3,cA4,nA4,cA5,nA5;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_producto);
        tvNombre=findViewById(R.id.textViewNombre);
        edId=findViewById(R.id.editTextIDConsultar);
        edPrecio=findViewById(R.id.editTextPrecioConsultar);
        edCantidad=findViewById(R.id.editTextCantidadConsultar);
        edMax=findViewById(R.id.editTextStockMaxConsultar);
        edMin=findViewById(R.id.editTextStockMinConsultar);
        edCA1=findViewById(R.id.editTextCarac1Consultar);
        edCA2=findViewById(R.id.editTextCarac2Consultar);
        edCA3=findViewById(R.id.editTextCarac3Consultar);
        edCA4=findViewById(R.id.editTextCarac4Consultar);
        edCA5=findViewById(R.id.editTextCarac5Consultar);
        edImagen= findViewById(R.id.imagenProveedor);
        tvCA1=findViewById(R.id.tvCA1);
        tvCA2=findViewById(R.id.tvCA2);
        tvCA3=findViewById(R.id.tvCA3);
        tvCA4=findViewById(R.id.tvCA4);
        tvCA5=findViewById(R.id.tvCA5);
        Log.d("Hola123",tvNombre.getText().toString());
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
        Log.d("Nombre45",cA1);
        Log.d("Nombre45",nA1);
        Log.d("Nombre45",cA2);
        Log.d("Nombre45",nA2);
        modificarCampos(nom,id,pre,can,max,min, imagen);
        modificarCamposAdicionales(cA1,nA1,cA2,nA2,cA3,nA3,cA4,nA4,cA5,nA5);
        modificarImagen(imagen);
        validarCampos();

        boton_modificar=findViewById(R.id.boton_modificar_producto);
        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                irModificar();
            }
        });
        boton_aceptar=findViewById(R.id.boton_aceptar_consulta);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultarProductoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        boton_eliminar=findViewById(R.id.boton_eliminar_producto);
        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    irConfirmar();
            }
        });

    }

    private void irModificar() {
        Bundle ed = new Bundle();
        ed.putString("nombre",nom);
        ed.putString("id",id);
        ed.putString("precio",pre);
        ed.putString("cantidad",can);
        ed.putString("sMax", max);
        ed.putString("sMin",min);
        ed.putString("imagen", imagen);
        ed.putString("uid",uid);
        ed.putString("cCarA1",cA1);
        ed.putString("nCarA1",nA1);
        ed.putString("cCarA2",cA2);
        ed.putString("nCarA2",nA2);
        ed.putString("cCarA3",cA3);
        ed.putString("nCarA3",nA3);
        ed.putString("cCarA4",cA4);
        ed.putString("nCarA4",nA4);
        ed.putString("cCarA5",cA5);
        ed.putString("nCarA5",nA5);
        Log.d("Nombre44",cA1);
        Log.d("Nombre44",nA1);
        Log.d("Nombre44",cA2);
        Log.d("Nombre44",nA2);
        Intent intent = new Intent(ConsultarProductoActivity.this, ModificarProductoActivity.class);
        intent.putExtras(ed);
        startActivity(intent);
        Log.d("Hola","hola1");
        finish();
    }

    private void irConfirmar() {
        ConfirmarEliminacionProductoFragment exampleDialog= new ConfirmarEliminacionProductoFragment(uid);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }

    public void eliminarProducto(String uid){
        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            database.getReference().child(user.getUid()).child("Productos").child(i).removeValue();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
   /* private void irHome() {
        Log.d("HolaBug4","Holaaa4");
        String max,min,carA1,carA2,carA3,carA4,carA5,nCA1,nCA2,nCA3,nCA4,nCA5;
        if (edCA1.getText().toString().isEmpty()){
            carA1="0";
            nCA1="0";
        } else {
            carA1=edCA1.getText().toString();
            nCA1=edCA1.getHint().toString();
        }
        Log.d("HolaBug5","Holaaa5");
        if(edCA2.getText().toString().isEmpty()){
            carA2="0";
            nCA2="0";
        }else {
            carA2=edCA1.getText().toString();
            nCA2=edCA1.getHint().toString();
        }
        if (edCA3.getText().toString().isEmpty()){
            carA3="0";
            nCA3="0";
        }else {
            carA3=edCA1.getText().toString();
            nCA3=edCA1.getHint().toString();
        }
        if (edCA4.getText().toString().isEmpty()){
            carA4="0";
            nCA4="0";
        }else {
            carA4=edCA4.getText().toString();
            nCA4=edCA4.getHint().toString();
        }
        if (edCA5.getText().toString().isEmpty()){
            carA5="0";
            nCA5="0";
        }else{
            carA5=edCA5.getText().toString();
            nCA5=edCA5.getHint().toString();
        }
        if (edMax.getText().toString().isEmpty()){
            max="1000";
        }else{
            max=edMax.getText().toString();
        }
        if (edMin.getText().toString().isEmpty()){
            min="1";
        }else {
            min=edMin.getText().toString();
        }
        Log.d("HolaBug","Holaaa");
        Log.d("HolaX",edNombre.getText().toString());
        Log.d("Holax2",edId.getText().toString());
        Log.d("Holax3",edPrecio.getText().toString());
        Log.d("Holax4",edCantidad.getText().toString());
        Log.d("HolaX5",max);
        Log.d("HolaX6",min);
        Log.d("HolaX7",uid);
        Log.d("HolaX8"," ");
        modificarDatos(edNombre.getText().toString(),edId.getText().toString(),edPrecio.getText().toString(),edCantidad.getText().toString(),max,min,carA1,nCA1,carA2,nCA2,carA3,nCA3,carA4,nCA4,carA5,nCA5);
        Log.d("HolaBug2","Holaaa2");
        Intent intent = new Intent(ConsultarProductoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }*/

    public void validarCampos(){
        edId.setEnabled(false);
        edCantidad.setEnabled(false);
        edPrecio.setEnabled(false);
        edMax.setEnabled(false);
        edMin.setEnabled(false);
        edCA1.setEnabled(false);
        edCA2.setEnabled(false);
        edCA3.setEnabled(false);
        edCA4.setEnabled(false);
        edCA5.setEnabled(false);
    }
    public void modificarImagen(String img){
        if(img!="0"){

            StorageReference reference = storageReference.child("producto/"+img);
            long Maxbytes=16384*16384;

            reference.getBytes(Maxbytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    edImagen.setImageBitmap(bitmap);
                }
            });
        }
    }
    /*public void modificarDatos(String nom, String id, String pre, String can, String max, String min, String cA1, String nA1, String cA2, String nA2, String cA3, String nA3, String cA4,String nA4, String cA5, String nA5, String uid){
        Log.d("HolaBuga","Holabug");
        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String i= ds.getKey();
                        Log.d("HolaBuga2","Holabug3");
                        if (uid.equals(i)) {
                            Log.d("HolaBuga3","Holabug3");
                            Map<String, Object> producto = new HashMap<>();
                            producto.put("nombre", nom);
                            producto.put("precio", pre);
                            producto.put("cantidad",can);
                            producto.put("id",id);
                            producto.put("sMax",max);
                            producto.put("sMin",min);
                            producto.put("nCaracteristicaA1",nA1);
                            producto.put("cCaracteristicaA1",cA1);
                            producto.put("nCaracteristicaA2",nA2);
                            producto.put("cCaracteristicaA2",cA2);
                            producto.put("nCaracteristicaA3",nA3);
                            producto.put("cCaracteristicaA3",cA3);
                            producto.put("nCaracteristicaA4",nA4);
                            producto.put("cCaracteristicaA4",cA4);
                            producto.put("nCaracteristicaA5",nA5);
                            producto.put("cCaracteristicaA5",cA5);
                            database.getReference().child(user.getUid()).child("Productos").child(uid).updateChildren(producto);

                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/
    public void modificarCampos(String nom, String id, String pre, String can, String max,String min, String imagen){
        tvNombre.setText(nom);
        edId.setText(id);
        edPrecio.setText(pre);
        edCantidad.setText(can);
        edMax.setText(max);
        edMin.setText(min);

        if(imagen!="0"){

            StorageReference reference = storageReference.child("producto/"+imagen);
            long Maxbytes=1024*1024;

            reference.getBytes(Maxbytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    edImagen.setImageBitmap(bitmap);
                }
            });
        }


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
}
