package Productos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.gestioninventario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListViewAgregarExistencia extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Producto> lista_contacto=new ArrayList<Producto>();
    private Producto producto;
    CustomProductosAdapter adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    ListView listViewContacto;
    ImageButton boton_regresar;

    String nom,pre,can,id,idbased,max,min,imagen;

    SearchView buscadorProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_agregar_existencia);

        buscadorProducto=findViewById(R.id.searchViewAgregarExistencia);

        listViewContacto=findViewById(R.id.listViewAgregar);
        CustomProductosAdapter adapter= new CustomProductosAdapter(this,GetData());
        listViewContacto.setAdapter(adapter);


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
                ed.putString("uid",c.getIdByD());
                ed.putString("imagen",c.getImagen());

                Intent intent = new Intent(ListViewAgregarExistencia.this, AgregarExistenciaActivity.class);
                intent.putExtras(ed);
                startActivity(intent);
                finish();
            }
        });


        boton_regresar=findViewById(R.id.boton_salir_seleccion);
        boton_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irHome();
            }
        });

        buscadorProducto.setOnQueryTextListener(this);

    }

    private void irHome() {
        Intent intent = new Intent(ListViewAgregarExistencia.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private List<Producto> GetData() {
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
                        producto=new Producto(nom,pre,can,id,max,min,idbased,imagen);
                        lista_contacto.add(producto);
                        adapter= new CustomProductosAdapter(ListViewAgregarExistencia.this, lista_contacto);
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtroBusqueda(newText);
        return false;
    }
}