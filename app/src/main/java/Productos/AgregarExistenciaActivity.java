package Productos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gestioninventario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AgregarExistenciaActivity extends AppCompatActivity {
    Button boton_aceptar;

    ImageButton boton_salir;
    TextView nombre;
    EditText showValue,cantidad,costo,proveedor;
    int cont=0;
    String nom,uid,id,can,max,min;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_existencia);
        Bundle rd =getIntent().getExtras();
        nom=rd.getString("nombre");
        id=rd.getString("id");
        String pre=rd.getString("precio");
        can=rd.getString("cantidad");
        max=rd.getString("sMax");
        min=rd.getString("sMin");
        uid=rd.getString("uid");
        nombre=findViewById(R.id.textViewNombreAgregar);
        nombre.setText(nom);
        cantidad=findViewById(R.id.editTextCantidadA);
        costo=findViewById(R.id.editTextPrecioA);
        proveedor=findViewById(R.id.editTextProveedor);

        boton_aceptar=findViewById(R.id.buttonGuardarA);

        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!validarInformación())) {
                    ErrorCamposObligatoriosFragment exampleDialog = new ErrorCamposObligatoriosFragment();
                    exampleDialog.show(getSupportFragmentManager(), "Error");
                }else{
                    if ((cantidad.getText().toString().equals("0")) || (!isInteger(cantidad.getText().toString())) || (!isInteger(costo.getText().toString())) || (!validarProveedor(proveedor.getText().toString())) || (!validarNumeros(cantidad.getText().toString(),costo.getText().toString())) || (!validarCantidad(cantidad.getText().toString(),can,max))) {
                        irError();

                    } else {

                        irConfirmar();

                    }
               }
            }

        });

        showValue=(EditText) findViewById(R.id.editTextCantidadA);

        boton_salir=findViewById(R.id.boton_salir_agregar);
        boton_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irHome();
            }
        });

    }
    public boolean validarInformación(){
        Boolean val=true;
        if (proveedor.getText().toString().isEmpty()){
            proveedor.setError("Campo Obligatorio");
            val=false;
        }
        if (cantidad.getText().toString().isEmpty()){
            cantidad.setError("Campo Obligatorio");
            val=false;
        }
        if (costo.getText().toString().isEmpty()){
            costo.setError("Campo Obligatorio");
            val=false;
        }

        return val;
    }
    public boolean validarCantidad(String cantidadn, String cantidadv, String sMax){
        int sumaTotal=Integer.parseInt(cantidadn)+Integer.parseInt(cantidadv);
         if ((sumaTotal>Integer.parseInt(sMax))||(Integer.parseInt(cantidadn)>Integer.parseInt(sMax))){
            return false;
        }
      return true;
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
    public boolean validarNumeros(String can,String costo){
        Boolean val=false;
        if((Integer.parseInt(can)>0)&&(Integer.parseInt(costo)>0)){
            val=true;
        }
        return val;
    }
    public boolean validarVacios(String texto){
        for (int i=0;i<texto.length();i++){
            if (texto.charAt(i)!= ' ')
                return false;
        }
        return true;
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
        ErrorExistenciasFragment exampleDialog= new ErrorExistenciasFragment();
        exampleDialog.show(getSupportFragmentManager(),"Error");
    }

    private void irConfirmar() {
        ConfirmarCompraFragment exampleDialog= new ConfirmarCompraFragment(nom,costo.getText().toString(),cantidad.getText().toString(),proveedor.getText().toString(),uid,id);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }
    public void incrementarCantidad(View view){
        cont++;
        showValue.setText(Integer.toString(cont));
    }

    public void decrementarCantidad(View view){

        if (cont<=0){
            showValue.setText(Integer.toString(cont));
        }else{
            cont--;
            showValue.setText(Integer.toString(cont));
        }

    }
    public void registrarCompra(String producto, String cantidad, String costo, String proveedor, String id){
        Map<String, Object> compra = new HashMap<>();
        compra.put("costo", costo);
        compra.put("producto", producto);
        compra.put("cantidad",cantidad);
        compra.put("proveedor", proveedor);
        compra.put("id",id);
        final DatabaseReference myRef = database.getReference().child(user.getUid()).child("Compras");
        myRef.push().setValue(compra);
    }
    public void agregarExistecia(String can, String uid){
        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {

                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            String canti = ds.child("cantidad").getValue().toString();
                            int suma=Integer.parseInt(canti)+Integer.parseInt(can);
                            Map<String, Object> producto = new HashMap<>();
                            producto.put("nombre",ds.child("nombre").getValue().toString());
                            producto.put("precio", ds.child("precio").getValue().toString());
                            producto.put("cantidad",String.valueOf(suma));
                            producto.put("id",ds.child("id").getValue().toString());
                            producto.put("sMax",ds.child("sMax").getValue().toString());
                            producto.put("sMin",ds.child("sMin").getValue().toString());
                            producto.put("imagen",ds.child("imagen").getValue().toString());
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

    private void irHome() {
        Intent intent = new Intent(AgregarExistenciaActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isNumeric(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}