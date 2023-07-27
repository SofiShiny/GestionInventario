package Productos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import Navegacion.MainActivity;
import Navegacion.SeleccionActivity;
import Proveedores.ProveedoresActivity;
import Ventas.VentasActivity;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ArrayList<Producto> lista_contacto=new ArrayList<Producto>();
    private Producto producto;
    CustomProductosAdapter adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    FloatingActionButton fab_eliminar;
    FloatingActionButton fab_agregar;
    ListView listViewContacto;
    FirebaseAuth mAuth;
    String nom,pre,can,id,idbased,max,min, imagen, cA1,nA1,cA2,nA2,cA3,nA3,cA4,nA4,cA5,nA5;

    SearchView buscadorProductos;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buscadorProductos=findViewById(R.id.searchViewProductos);

        listViewContacto=findViewById(R.id.listViewProducto);
        CustomProductosAdapter adapter= new CustomProductosAdapter(this,GetData());
        listViewContacto.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        listViewContacto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Producto c= lista_contacto.get(position);
                Bundle ed = new Bundle();
                ed.putString("nombre",c.getNombre().toString());
                ed.putString("id",c.getId().toString());
                ed.putString("precio",c.getPrecio().toString());
                ed.putString("cantidad",c.getCantidad().toString());
                ed.putString("sMax",String.valueOf(c.getsMax()));
                ed.putString("sMin",String.valueOf(c.getsMin()));
                ed.putString("imagen", c.getImagen());
                ed.putString("uid",c.getIdByD());
                ed.putString("cCarA1",c.getcA1().toString());
                ed.putString("nCarA1",c.getnA1().toString());
                ed.putString("cCarA2",c.getcA2().toString());
                ed.putString("nCarA2",c.getnA2().toString());
                ed.putString("cCarA3",c.getcA3().toString());
                ed.putString("nCarA3",c.getnA3().toString());
                ed.putString("cCarA4",c.getcA4().toString());
                ed.putString("nCarA4",c.getnA4().toString());
                ed.putString("cCarA5",c.getcA5().toString());
                ed.putString("nCarA5",c.getnA5().toString());
                Intent intent = new Intent(HomeActivity.this, ConsultarProductoActivity.class);
                intent.putExtras(ed);
                startActivity(intent);
                finish();
            }
        });

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth= FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_inventario);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_inventario){
                finish();
                startActivity(getIntent());
                return true;
            }
            else if (item.getItemId() == R.id.bottom_ventas){
                startActivity(new Intent(getApplicationContext(), VentasActivity.class));
                overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
                finish();
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

        fab_agregar=findViewById(R.id.floatingActionButton_Agregar);
        fab_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irSeleccion();
            }
        });
        
        fab_eliminar=findViewById(R.id.floatingActionButton_Eliminar);
        fab_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irEliminarExistencia();
            }
        });

        buscadorProductos.setOnQueryTextListener(this);
    }



    private void irEliminarExistencia() {
        Intent intent = new Intent(HomeActivity.this, ListViewExistencias.class);
        startActivity(intent);
        finish();
    }


    private List<Producto> GetData() {
        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        nom = ds.child("nombre").getValue().toString();
                        pre = ds.child("precio").getValue().toString();
                        can = ds.child("cantidad").getValue().toString();
                        id = ds.child("id").getValue().toString();
                        max = ds.child("sMax").getValue().toString();
                        min = ds.child("sMin").getValue().toString();
                        imagen= ds.child("imagen").getValue().toString();
                        idbased = ds.getKey();
                        cA1 =ds.child("cCaracteristicaA1").getValue().toString();
                        nA1 =ds.child("nCaracteristicaA1").getValue().toString();
                        cA2 =ds.child("cCaracteristicaA2").getValue().toString();
                        nA2 =ds.child("nCaracteristicaA2").getValue().toString();
                        cA3 =ds.child("cCaracteristicaA3").getValue().toString();
                        nA3 =ds.child("nCaracteristicaA3").getValue().toString();
                        cA4 =ds.child("cCaracteristicaA4").getValue().toString();
                        nA4 =ds.child("nCaracteristicaA4").getValue().toString();
                        cA5 =ds.child("cCaracteristicaA5").getValue().toString();
                        nA5 =ds.child("nCaracteristicaA5").getValue().toString();
                        producto=new Producto(nom,pre,can,id,max,min,idbased, imagen,cA1,nA1,cA2,nA2,cA3,nA3,cA4,nA4,cA5,nA5);
                        lista_contacto.add(producto);
                        adapter= new CustomProductosAdapter(HomeActivity.this, lista_contacto);
                        listViewContacto.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lista_contacto;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user== null){
            irMain();
        }
    }

    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void irSeleccion() {
        SeleccionActivity exampleDialog= new SeleccionActivity();
        exampleDialog.show(getSupportFragmentManager(),"Seleccion");
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