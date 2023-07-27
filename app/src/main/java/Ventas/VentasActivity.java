package Ventas;

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
import Compras.ComprasActivity;
import Compras.CustomComprasAdapter;
import Productos.HomeActivity;
import Navegacion.MainActivity;
import Proveedores.ProveedoresActivity;
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

public class VentasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Venta> lista_ventas=new ArrayList<Venta>();
    private Venta venta;
    CustomVentasAdapter adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    ListView listViewVentas;
    String ganancia,idbased,producto,cantidad,precio,idV;
    FirebaseAuth mAuth;
    Button btnCompras,btnVentas;

    SearchView buscadorVentas;

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
        Intent intent = new Intent(VentasActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_ventas);

        btnVentas=findViewById(R.id.boton_ventas);
        btnVentas.setBackgroundColor(Color.LTGRAY);

        buscadorVentas=findViewById(R.id.searchViewVentas);

        btnCompras=findViewById(R.id.boton_compras);
        btnCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VentasActivity.this, ComprasActivity.class);
                startActivity(intent);
                finish();
            }
        });

        listViewVentas=findViewById(R.id.listViewVentas);
        CustomVentasAdapter adapter= new CustomVentasAdapter(this,GetData());
        listViewVentas.setAdapter(adapter);
        
        

       listViewVentas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venta c= lista_ventas.get(position);
                Bundle ed = new Bundle();
                ed.putString("ganancia",c.getGanancia().toString());
                ed.putString("producto",c.getProducto().toString());
                ed.putString("cantidad",c.getCantidad().toString());
                ed.putString("precio",c.getPrecio().toString());
                ed.putString("uid",c.getIdByD());
                ed.putString("id",c.getId());
                Intent intent = new Intent(VentasActivity.this, ConsultarVentasActivity.class);
                intent.putExtras(ed);
                startActivity(intent);
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

        buscadorVentas.setOnQueryTextListener(this);
    }



    private List<Venta> GetData() {
        database.getReference().child(user.getUid()).child("Ventas").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        ganancia = ds.child("ganancia").getValue().toString();
                        producto = ds.child("producto").getValue().toString();
                        cantidad = ds.child("cantidad").getValue().toString();
                        precio = ds.child("precio").getValue().toString();
                        idV=ds.child("id").getValue().toString();

                        idbased = ds.getKey();
                        venta=new Venta(ganancia,idbased,producto,cantidad,precio,idV);
                        lista_ventas.add(venta);
                        adapter= new CustomVentasAdapter(VentasActivity.this, lista_ventas);
                        listViewVentas.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lista_ventas;
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