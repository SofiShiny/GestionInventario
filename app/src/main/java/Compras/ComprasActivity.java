package Compras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import Compras.Compra;
import Compras.CustomComprasAdapter;
import Productos.HomeActivity;
import Navegacion.MainActivity;
import Productos.Producto;
import Proveedores.ProveedoresActivity;
import Ventas.VentasActivity;

import com.example.gestioninventario.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComprasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Compra> lista_compras=new ArrayList<Compra>();
    CustomComprasAdapter adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    ListView listViewCompras;
    String productoC,cantidadC,costoC,proveedorC,idbaseC,idC;
    Compra compra;
    FirebaseAuth mAuth;
    Button btnCompras,btnVentas;

    SearchView buscadorCompras;

    private static final String TAG = "CustomAdapter";

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.opciones, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id= item.getItemId();

        if (id == R.id.item_cerrar){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(ComprasActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user== null){
            irMain();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        btnCompras=findViewById(R.id.boton_compras);
        btnCompras.setBackgroundColor(Color.LTGRAY);

        buscadorCompras=findViewById(R.id.searchViewCompras);

        btnVentas=findViewById(R.id.boton_ventas);
        btnVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComprasActivity.this, VentasActivity.class);
                startActivity(intent);
                finish();
            }
        });

        listViewCompras=findViewById(R.id.listViewCompras);
        CustomComprasAdapter adapter= new CustomComprasAdapter(this,GetDataCompra());
        listViewCompras.setAdapter(adapter);



        listViewCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Compra c= lista_compras.get(position);
                Bundle ed = new Bundle();
                ed.putString("costo",c.getCosto().toString());
                ed.putString("producto",c.getProducto().toString());
                ed.putString("cantidad",c.getCantidad().toString());
                ed.putString("proveedor",c.getProveedor().toString());
                ed.putString("uid",c.getIdByD());
                ed.putString("id",c.getId());
                Log.d("Hola12345","Hola1");
                Intent intent = new Intent(ComprasActivity.this, ConsultarComprasActivity.class);
                Log.d("Hola123456","Hola2");
                intent.putExtras(ed);
                Log.d("Hola1234567","Hola3");
                startActivity(intent);
                Log.d("Hola12345678","Hola4");
                finish();
            }
        });


        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth= FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_ventas);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_inventario){
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
                finish();
                return true;

            }
            else if (item.getItemId() == R.id.bottom_ventas){

                return true;
            }
            else if (item.getItemId() == R.id.bottom_proveedores){
                startActivity(new Intent(getApplicationContext(), ProveedoresActivity.class));
                overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
                finish();
                return true;
            }
            else
                return false;
        });

        buscadorCompras.setOnQueryTextListener(this);
    }
   private List<Compra> GetDataCompra() {
        database.getReference().child(user.getUid()).child("Compras").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                double x;
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        cantidadC = ds.child("cantidad").getValue().toString();
                        productoC = ds.child("producto").getValue().toString();
                        costoC = ds.child("costo").getValue().toString();
                        proveedorC = ds.child("proveedor").getValue().toString();
                        idbaseC = ds.getKey();
                        idC=ds.child("id").getValue().toString();
                        x=Integer.parseInt(costoC)*Integer.parseInt(cantidadC);
                        compra=new Compra(String.valueOf(x),idbaseC,productoC,cantidadC,proveedorC,idC);
                        lista_compras.add(compra);
                        adapter= new CustomComprasAdapter(ComprasActivity.this, lista_compras);
                        listViewCompras.setAdapter(adapter);
                        Log.d("NombreC",productoC);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lista_compras;
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtroBusqueda(newText);
        return false;
    }

}