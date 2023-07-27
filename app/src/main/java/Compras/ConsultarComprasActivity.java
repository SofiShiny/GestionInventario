package Compras;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestioninventario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ConsultarComprasActivity extends AppCompatActivity {

    Button boton_aceptar,boton_eliminar;
    TextView tvCosto,tvProducto,tvCantidad,tvId,tvProveedor;
    String uid,cantidad;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_compra);

        tvCosto=findViewById(R.id.textViewCosto);
        tvProducto=findViewById(R.id.textViewProductoC);
        tvCantidad=findViewById(R.id.textViewCantidadC);
        tvProveedor=findViewById(R.id.textViewProveedor);
        tvId=findViewById(R.id.textViewProductoID);


        Bundle rd =getIntent().getExtras();
        String costo=rd.getString("costo");
        String producto=rd.getString("producto");
        cantidad=rd.getString("cantidad");
        String proveedor=rd.getString("proveedor");
        uid=rd.getString("uid");
        String id=rd.getString("id");
        tvCosto.setText(costo);
        tvProducto.setText(producto);
        tvCantidad.setText(cantidad);
        tvProveedor.setText(proveedor);
        tvId.setText(id);

        boton_aceptar=findViewById(R.id.boton_aceptar_consulta_compra);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                irCompras();
            }
        });
        boton_eliminar=findViewById(R.id.boton_eliminar_compra);
        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irConfirmar();
            }
        });
    }
    public void eliminarCantidad(String cantidad, String uid){
        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {

                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            String canti = ds.child("cantidad").getValue().toString();
                            int suma=Integer.parseInt(canti)-Integer.parseInt(cantidad);
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
    private void irConfirmar() {
        Log.d("hola123",cantidad);
        ConfirmarEliminacionRegistroCompraFragment exampleDialog= new ConfirmarEliminacionRegistroCompraFragment(cantidad,uid);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }

    public void eliminarCompra(String uid){
        database.getReference().child(user.getUid()).child("Compras").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            database.getReference().child(user.getUid()).child("Compras").child(i).removeValue();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void irCompras() {
        Intent intent = new Intent(ConsultarComprasActivity.this, ComprasActivity.class);
        startActivity(intent);
        finish();
    }
}
