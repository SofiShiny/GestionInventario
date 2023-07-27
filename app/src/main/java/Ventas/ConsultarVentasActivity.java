package Ventas;

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

import Compras.ConfirmarEliminacionRegistroCompraFragment;

public class ConsultarVentasActivity extends AppCompatActivity {

    Button boton_aceptar,boton_eliminar;
    TextView tvGanancia,tvProducto,tvCantidad,tvPrecio,tvID;
    String uid,cantidad;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_ventas);

        tvGanancia=findViewById(R.id.textViewGanancia);
        tvProducto=findViewById(R.id.textViewProductoC);
        tvCantidad=findViewById(R.id.textViewCantidadC);
        tvPrecio=findViewById(R.id.textViewPrecio);
        tvID=findViewById(R.id.textViewProductoID);


        Bundle rd =getIntent().getExtras();
        String ganancia=rd.getString("ganancia");
        String producto=rd.getString("producto");
        cantidad=rd.getString("cantidad");
        String precio=rd.getString("precio");
        String id=rd.getString("id");
        uid=rd.getString("uid");

        tvGanancia.setText(ganancia);
        tvProducto.setText(producto);
        tvCantidad.setText(cantidad);
        tvPrecio.setText(precio);
        tvID.setText(id);

        boton_aceptar=findViewById(R.id.boton_aceptar_consulta_venta);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irVentas();
            }
        });
        boton_eliminar=findViewById(R.id.boton_eliminar_venta);
        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irConfirmar();
            }
        });


    }

    private void irConfirmar() {
        Log.d("hola123",cantidad);
        ConfirmarEliminacionRegistroVentaFragment exampleDialog= new ConfirmarEliminacionRegistroVentaFragment(cantidad,uid);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");
    }
    public void regresarCantidad(String cantidad, String uid){

        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {

                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            String canti = ds.child("cantidad").getValue().toString();
                            int sumaTotal=Integer.parseInt(canti)+Integer.parseInt(cantidad);
                                Map<String, Object> producto = new HashMap<>();
                                producto.put("nombre", ds.child("nombre").getValue().toString());
                                producto.put("precio", ds.child("precio").getValue().toString());
                                producto.put("cantidad", String.valueOf(sumaTotal));
                                producto.put("id", ds.child("id").getValue().toString());
                                producto.put("sMax", ds.child("sMax").getValue().toString());
                                producto.put("sMin", ds.child("sMin").getValue().toString());
                                producto.put("imagen", ds.child("imagen").getValue().toString());
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
    public void eliminarVenta(String uid){
        database.getReference().child(user.getUid()).child("Ventas").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            database.getReference().child(user.getUid()).child("Ventas").child(i).removeValue();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void irVentas() {
        Intent intent = new Intent(ConsultarVentasActivity.this, VentasActivity.class);
        startActivity(intent);
        finish();
    }
}
