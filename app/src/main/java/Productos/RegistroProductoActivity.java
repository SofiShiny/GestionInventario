package Productos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistroProductoActivity extends AppCompatActivity {

    private ArrayList<Producto> lista_contacto=new ArrayList<Producto>();
    private Producto producto;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    EditText editTextNombre;
    EditText editTextID;
    EditText cA1,cA2,cA3,cA4,cA5;
    EditText editTextCantidad;
    EditText editTextPrecio;

    EditText editTextStockMax;
    EditText editTextStockMin;
    Button boton_salir;
    Button boton_aceptar;
    Button boton_caracteristica_nueva;
    String nom,pre,can,id,idbased,max,min, imagen, imagen2;
    int cont=0;
    boolean  val=false;

    private final int Gallery_REQUEST_CODE = 1;
    private Uri image_url = null;
    private ImageView imagenProducto;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_producto);

        cargarInformacion(lista_contacto);
        storageReference = FirebaseStorage.getInstance().getReference();

        cA1=findViewById(R.id.editTextCaracteristica1);
        cA2=findViewById(R.id.editTextCaracteristica2);
        cA3=findViewById(R.id.editTextCaracteristica3);
        cA4=findViewById(R.id.editTextCaracteristica4);
        cA5=findViewById(R.id.editTextCaracteristica5);
        editTextNombre= findViewById(R.id.editTextNombre);
        editTextCantidad= findViewById(R.id.editTextCantidad);
        editTextID=findViewById(R.id.editTextID);
        editTextPrecio=findViewById(R.id.editTextPrecio);
        editTextStockMax=findViewById(R.id.editTextStockMax);
        editTextStockMin=findViewById(R.id.editTextStockMin);

        imagenProducto = findViewById(R.id.imagenProveedor);

        imagenProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();}
        });

        boton_caracteristica_nueva=findViewById(R.id.boton_caracteristica_nueva);
        boton_caracteristica_nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ima;
                if(validarInformación()) {
                    if(image_url==null){
                        ima="noFoto";
                    }else{
                        ima= image_url.getLastPathSegment();
                    }
                    if (editTextStockMax.getText().toString().isEmpty()){
                        max="1000";
                    }else{
                        max=editTextStockMax.getText().toString();
                    }
                    if (editTextStockMin.getText().toString().isEmpty()){
                        min="1";
                    }else {
                        min=editTextStockMin.getText().toString();
                    }
                    CaracteristicaAdicionalActivity exampleDialog = new CaracteristicaAdicionalActivity(editTextNombre.getText().toString(),editTextID.getText().toString(),editTextPrecio.getText().toString(),editTextCantidad.getText().toString(),max,min,ima);
                    exampleDialog.show(getSupportFragmentManager(), "Caracteristica Nueva");

                }
            }
        });

        Intent intent=getIntent();
        int x=intent.getIntExtra("Contador",0);
        boolean val1=intent.getBooleanExtra("val",false);
        String y=Integer.toString(x);
        camposCaracteristicas(x);
        if (val1) {
            modificarCampos();
        }


        boton_salir=findViewById(R.id.boton_salir_registro);
        boton_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irHome();
            }
        });

        boton_aceptar = findViewById(R.id.boton_aceptar_registro);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maxi,mini;
                if (editTextStockMax.getText().toString().isEmpty()){
                    maxi="1000";
                }else{
                    maxi=editTextStockMax.getText().toString();
                }
                if (editTextStockMin.getText().toString().isEmpty()){
                    mini="1";
                }else {
                    mini=editTextStockMin.getText().toString();
                }
                if((!validarInformación()) || (validarVacios(editTextNombre.getText().toString())) || (validarVacios(editTextID.getText().toString())) || (validarVacios(editTextCantidad.getText().toString())) || (validarVacios(editTextPrecio.getText().toString())) ) {
                    irError();
                }else{
                    boolean valId,valNums,valStock,valCantidad,valDouble;
                        if ((!validarId(editTextID.getText().toString()))) {
                            valId = false;
                            ErrorIDRepetidoFragment exampleDialog= new ErrorIDRepetidoFragment();
                            exampleDialog.show(getSupportFragmentManager(),"Error");
                        } else {
                            valId = true;
                        }
                        if (!validarEnteros(editTextCantidad.getText().toString(), maxi, mini)) {
                            valNums = false;
                            ErrorStringsFragment exampleDialog= new ErrorStringsFragment();
                            exampleDialog.show(getSupportFragmentManager(),"Error");

                        } else {
                            valNums = true;
                        }
                        if (!isNumeric(editTextPrecio.getText().toString()) || (Double.parseDouble(editTextPrecio.getText().toString())<0)){
                            valDouble=false;
                            ErrorStringsFragment exampleDialog= new ErrorStringsFragment();
                            exampleDialog.show(getSupportFragmentManager(),"Error");
                        }else{
                            valDouble=true;
                        }
                        if ((isInteger(editTextCantidad.getText().toString())) && (isNumeric(maxi)) && (isNumeric(mini))) {
                         if (!validarStock(maxi, mini)) {
                            valStock = false;
                             ErrorStockFragment exampleDialog= new ErrorStockFragment();
                             exampleDialog.show(getSupportFragmentManager(),"Error");
                          } else {
                            valStock = true;
                          }

                          if (!validarCantidad(maxi, mini, editTextCantidad.getText().toString())) {
                            valCantidad = false;
                              ErrorCantidadFragment exampleDialog= new ErrorCantidadFragment();
                              exampleDialog.show(getSupportFragmentManager(),"Error");
                          } else {
                            valCantidad = true;
                           }


                        if ((valId) && (valNums) && (valStock) && (valCantidad) && (valDouble)) {
                            irConfirmar();
                        }
                        }
                }
            }
        });
    }

    private void irConfirmar() {
        String max,min,ima, carA1,carA2,carA3,carA4,carA5,nCA1,nCA2,nCA3,nCA4,nCA5;


        if(image_url==null){
            ima="noFoto";
        }else{
            ima= image_url.getLastPathSegment().toString();
            cargarFoto();
        }

        if (cA1.getText().toString().isEmpty()){
            carA1="0";
            nCA1="0";
        } else {
            carA1=cA1.getText().toString();
            nCA1=cA1.getHint().toString();
        }
        if(cA2.getText().toString().isEmpty()){
            carA2="0";
            nCA2="0";
        }else {
            carA2=cA2.getText().toString();
            nCA2=cA2.getHint().toString();
        }
        if (cA3.getText().toString().isEmpty()){
            carA3="0";
            nCA3="0";
        }else {
            carA3=cA3.getText().toString();
            nCA3=cA3.getHint().toString();
        }
        if (cA4.getText().toString().isEmpty()){
            carA4="0";
            nCA4="0";
        }else {
            carA4=cA4.getText().toString();
            nCA4=cA4.getHint().toString();
        }
        if (cA5.getText().toString().isEmpty()){
            carA5="0";
            nCA5="0";
        }else{
            carA5=cA5.getText().toString();
            nCA5=cA5.getHint().toString();
        }
        if ((editTextStockMax.getText().toString().isEmpty()) || (validarVacios(editTextStockMax.getText().toString()))) {
            max="1000";
        }else{
            max=editTextStockMax.getText().toString();
        }
        if (editTextStockMin.getText().toString().isEmpty() || (validarVacios(editTextStockMin.getText().toString()))){
            min="1";
        }else {
            min=editTextStockMin.getText().toString();
        }
        Log.d("HolaBuga345",editTextNombre.getText().toString());
        ConfirmarProductoFragment exampleDialog= new ConfirmarProductoFragment(editTextNombre.getText().toString(),editTextID.getText().toString(),editTextPrecio.getText().toString(),editTextCantidad.getText().toString(),max,min, ima, carA1,nCA1,carA2,nCA2,carA3,nCA3,carA4,nCA4,carA5,nCA5);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");

    }
    public boolean validarVacios(String texto){
        for (int i=0;i<texto.length();i++){
            if (texto.charAt(i)!= ' ')
                return false;
        }
       return true;
    }
    private void irError() {
        ErrorCamposObligatoriosFragment exampleDialog= new ErrorCamposObligatoriosFragment();
        exampleDialog.show(getSupportFragmentManager(),"Error");
    }

    private void irHome() {
        Intent intent = new Intent(RegistroProductoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean validarInformación(){
        Boolean val=true;
        if (editTextNombre.getText().toString().isEmpty()){
            editTextNombre.setError("Campo Obligatorio");
            val=false;
        }
        if (editTextID.getText().toString().isEmpty()){
            editTextID.setError("Campo Obligatorio");
            val=false;
        }
        if (editTextPrecio.getText().toString().isEmpty()){
            editTextPrecio.setError("Campo Obligatorio");
            val=false;
        }

        if (editTextCantidad.getText().toString().isEmpty()){
            editTextCantidad.setError("Campo Obligatorio");
            val=false;
        }

        return val;
    }

    public void registrarProducto(String name, String id, String prec, String canti, String max, String min, String imagen, String carA1, String carA2, String carA3, String carA4, String carA5, String nCA1, String nCA2,String nCA3, String nCA4, String nCA5){

        Map<String, Object> producto = new HashMap<>();
        producto.put("nombre", name);
        producto.put("precio", prec);
        producto.put("cantidad",canti);
        producto.put("id",id);
        producto.put("sMax",max);
        producto.put("sMin",min);
        producto.put("imagen", imagen);
        producto.put("nCaracteristicaA1",nCA1);
        producto.put("cCaracteristicaA1",carA1);
        producto.put("nCaracteristicaA2",nCA2);
        producto.put("cCaracteristicaA2",carA2);
        producto.put("nCaracteristicaA3",nCA3);
        producto.put("cCaracteristicaA3",carA3);
        producto.put("nCaracteristicaA4",nCA4);
        producto.put("cCaracteristicaA4",carA4);
        producto.put("nCaracteristicaA5",nCA5);
        producto.put("cCaracteristicaA5",carA5);

        final DatabaseReference myRef = database.getReference().child(user.getUid()).child("Productos");
        myRef.push().setValue(producto);
    }
    public void cargarInformacion(ArrayList<Producto> lista_contacto) {
        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        nom = ds.child("nombre").getValue().toString();
                        pre = ds.child("precio").getValue().toString()+" $";
                        can = ds.child("cantidad").getValue().toString();
                        id = ds.child("id").getValue().toString();
                        max = ds.child("sMax").getValue().toString();
                        min = ds.child("sMin").getValue().toString();
                        imagen= ds.child("imagen").getValue().toString();
                        idbased = ds.getKey();
                        producto=new Producto(nom,pre,can,id,max,min,idbased, imagen);
                        lista_contacto.add(producto);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void modificarCampos(){
        Intent intent=getIntent();
        String nom=intent.getStringExtra("nombre");
        String id=intent.getStringExtra("id");
        String pre=intent.getStringExtra("precio");
        String can=intent.getStringExtra("cantidad");
        String max=intent.getStringExtra("max");
        String min=intent.getStringExtra("min");
        String imagen=intent.getStringExtra("imagen");
        //Uri imagen2=Uri.parse(intent.getStringExtra("imagen"));
        if(imagen!="noFoto"){

            StorageReference reference = storageReference.child("producto/"+imagen);
            long Maxbytes=16384*16384;

            reference.getBytes(Maxbytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imagenProducto.setImageBitmap(bitmap);
                }
            });
        }
        editTextNombre.setText(nom.toString());
        editTextID.setText(id.toString());
        editTextPrecio.setText(pre.toString());
        editTextCantidad.setText(can.toString());
        editTextStockMax.setText(max.toString());
        editTextStockMin.setText(min.toString());
    }
    public boolean validarId(String id){
        boolean val=true;
        for (int i=0; i<lista_contacto.size();i++){
            Log.d("id del productor",lista_contacto.get(i).getId());
            if(id.equals(lista_contacto.get(i).getId())){
                val=false;
            }
        }
        return val;
    }
    public boolean isNumeric(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public boolean validarEnteros(String can,String sMax,String sMin){
        Boolean val=false;
        if(isInteger(can)&&(isInteger(sMax))&&(isInteger(sMin))&&(Integer.parseInt(can)>0)&&(Integer.parseInt(sMax)>0)&&(Integer.parseInt(sMin)>0)){
            val=true;
        }
        return val;
    }

   public void camposCaracteristicas(int cont){
        Intent intent=getIntent();
        String caracteristica1=intent.getStringExtra("caracteristica1");
        String caracteristica2=intent.getStringExtra("caracteristica2");
        String caracteristica3=intent.getStringExtra("caracteristica3");
        String caracteristica4=intent.getStringExtra("caracteristica4");
        String caracteristica5=intent.getStringExtra("caracteristica5");
        if (cont==1){
            cA1.setVisibility(View.VISIBLE);
            cA1.setHint(caracteristica1);
        }else if (cont==2){
            cA1.setVisibility(View.VISIBLE);
            cA1.setHint(caracteristica1);
            cA2.setVisibility(View.VISIBLE);
            cA2.setHint(caracteristica2);
        } else if (cont==3){
            cA1.setVisibility(View.VISIBLE);
            cA1.setHint(caracteristica1);
            cA2.setVisibility(View.VISIBLE);
            cA2.setHint(caracteristica2);
            cA3.setVisibility(View.VISIBLE);
            cA3.setHint(caracteristica3);
        } else if (cont==4){
            cA1.setVisibility(View.VISIBLE);
            cA1.setHint(caracteristica1);
            cA2.setVisibility(View.VISIBLE);
            cA2.setHint(caracteristica2);
            cA3.setVisibility(View.VISIBLE);
            cA3.setHint(caracteristica3);
            cA4.setVisibility(View.VISIBLE);
            cA4.setHint(caracteristica4);
        } else if (cont==5){
            cA1.setVisibility(View.VISIBLE);
            cA1.setHint(caracteristica1);
            cA2.setVisibility(View.VISIBLE);
            cA2.setHint(caracteristica2);
            cA3.setVisibility(View.VISIBLE);
            cA3.setHint(caracteristica3);
            cA4.setVisibility(View.VISIBLE);
            cA4.setHint(caracteristica4);
            cA5.setVisibility(View.VISIBLE);
            cA5.setHint(caracteristica5);
        }

    }
    public boolean validarStock(String max, String min){
       boolean val=false;
       if ((isInteger(max)) && (isInteger(min))) {
           if ((Integer.parseInt(max) > Integer.parseInt(min))) {
               val = true;
           }
       }else{
           return val;
       }
       return val;
    }
    public boolean validarCantidad(String max, String min,String can){
        boolean val=false;
        if ((isNumeric(can)) && (isNumeric(max)) && (isNumeric(min))){
            if ((Integer.parseInt(can)>=Integer.parseInt(min))&&(Integer.parseInt(can)<=Integer.parseInt(max))){
                val=true;
            }
        }else{
            return val;
        }
        return val;

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
            imagenProducto.setImageURI(image_url);
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
