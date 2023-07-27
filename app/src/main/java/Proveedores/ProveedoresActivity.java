package Proveedores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import Productos.HomeActivity;
import Navegacion.MainActivity;
import com.example.gestioninventario.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Ventas.VentasActivity;

public class ProveedoresActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Proveedor> lista_proveedores=new ArrayList<Proveedor>();
    private Proveedor proveedor;
    CustomProveedoresAdapter adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    ListView listViewProveedores;
    String nombre,idbased,producto,direccion,telefono, imagen;
    FirebaseAuth mAuth;
    SearchView buscadorProveedores;
    FloatingActionButton fab_agregar;

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
        Intent intent = new Intent(ProveedoresActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_proveedores);

        buscadorProveedores=findViewById(R.id.searchViewProveedores);

        listViewProveedores=findViewById(R.id.listViewProveedor);
        CustomProveedoresAdapter adapter= new CustomProveedoresAdapter(this,GetData());
        listViewProveedores.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listViewProveedores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Proveedor c= lista_proveedores.get(position);
                Bundle ed = new Bundle();
                ed.putString("nombre",c.getNombre().toString());
                ed.putString("producto",c.getProducto().toString());
                ed.putString("direccion",c.getDireccion().toString());
                ed.putString("telefono",c.getTelefono().toString());
                ed.putString("uid",c.getIdByD());
                ed.putString("imagen", c.getImagen());
                Intent intent = new Intent(ProveedoresActivity.this, ConsultarProveedorActivity.class);
                intent.putExtras(ed);
                startActivity(intent);
                finish();
            }
        });

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth= FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_proveedores);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_inventario){
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
                finish();
                return true;

            }
            else if (item.getItemId() == R.id.bottom_ventas){
                startActivity(new Intent(getApplicationContext(), VentasActivity.class));
                overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.bottom_proveedores){

                return true;
            }
            else
                return false;
        });

        fab_agregar=findViewById(R.id.floatingActionButton_Agregar);
        fab_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirRegistro();
            }
        });

        buscadorProveedores.setOnQueryTextListener(this);
    }

    private void abrirRegistro() {
        Intent intent = new Intent(ProveedoresActivity.this, RegistroProveedorActivity.class);
        startActivity(intent);
        finish();
    }

    private List<Proveedor> GetData() {
        database.getReference().child(user.getUid()).child("Proveedores").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        nombre = ds.child("nombre").getValue().toString();
                        producto = ds.child("producto").getValue().toString();
                        direccion = ds.child("direccion").getValue().toString();
                        telefono = ds.child("telefono").getValue().toString();
                        imagen = ds.child("imagen").getValue().toString();
                        idbased = ds.getKey();
                        proveedor=new Proveedor(nombre,producto,telefono,direccion,idbased, imagen);
                        lista_proveedores.add(proveedor);
                        adapter= new CustomProveedoresAdapter(ProveedoresActivity.this, lista_proveedores);
                        listViewProveedores.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lista_proveedores;
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