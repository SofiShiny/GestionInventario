package Productos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestioninventario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EliminarExistenciaActivity extends AppCompatActivity {
    ArrayList<Producto> lista_contacto=new ArrayList<Producto>();
    private Producto producto;

    ImageButton boton_salir;
    Button boton_aceptar,maxV,maxP;
    EditText showValue,showValue2,perdidos,vendidos;
    String nom,pre,can,id,idbased,max,min,nombre1,idV,imagen;
    String uid;
    int cont=0;
    int cont2=0;
    TextView nombreP;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_existencia);
        Bundle rd =getIntent().getExtras();
        nombre1=rd.getString("nombre");
        idV=rd.getString("id");
        String pre=rd.getString("precio");
        can=rd.getString("cantidad");
        String max=rd.getString("sMax");
        min=rd.getString("sMin");
        uid=rd.getString("uid");
        cargarInformacion();
        nombreP=findViewById(R.id.textViewNombreEliminar);
        nombreP.setText(nombre1);
        maxP=findViewById(R.id.buttonMaxPerdido);
        perdidos=findViewById(R.id.editTextPerdidos);
        vendidos=findViewById(R.id.editTextVendidos);
        maxV=findViewById(R.id.buttonMaxVendido);


        maxV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendidos.setText(can);
                cont=Integer.parseInt(vendidos.getText().toString());
            }
        });
        maxP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perdidos.setText(can);
                cont2=Integer.parseInt(perdidos.getText().toString());
            }
        });
        boton_aceptar=findViewById(R.id.buttonGuardar);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((isInteger(vendidos.getText().toString())) && (isInteger(perdidos.getText().toString()))) {

                    if ((!validarCantidad(vendidos.getText().toString(), perdidos.getText().toString(), can, min))) {
                        ErrorCamposObligatoriosFragment exampleDialog = new ErrorCamposObligatoriosFragment();
                        exampleDialog.show(getSupportFragmentManager(), "Error");
                    }else{
                        if ((Integer.parseInt(perdidos.getText().toString()) > 0) && (Integer.parseInt(perdidos.getText().toString()) < Integer.parseInt(can))) {
                            irConfirmar();
                        } else if ((Integer.parseInt(vendidos.getText().toString()) > 0) && (Integer.parseInt(vendidos.getText().toString()) < Integer.parseInt(can))) {
                            irConfirmar();
                        } else {
                            irError();
                        }
                    }
                } else {
                   irError();
                }
            }
        });

        showValue=(EditText) findViewById(R.id.editTextVendidos);
        showValue2=(EditText) findViewById(R.id.editTextPerdidos);

        boton_salir=findViewById(R.id.boton_salir_eliminar);
        boton_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irHome();
            }
        });
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

    public boolean validarCantidad(String vendidos, String perdidos, String cantidad, String sMin){
        if ((Integer.parseInt(vendidos)>Integer.parseInt(cantidad)) || (Integer.parseInt(perdidos)>Integer.parseInt(cantidad))){
            return false;
        } else if ((Integer.parseInt(cantidad)-Integer.parseInt(vendidos)<Integer.parseInt(sMin))||(Integer.parseInt(cantidad)-Integer.parseInt(perdidos)<Integer.parseInt(sMin))){
            return false;
        }
       return true;
    }

    public boolean validarNumeros(String can,String costo){
        Boolean val=false;
        if((Integer.parseInt(can)>0)&&(Integer.parseInt(costo)>0)){
            val=true;
        }
        return val;
    }
    private void irConfirmar() {
        int m=Integer.parseInt(vendidos.getText().toString());

        double x=retornarPrecio(uid);
        double ganancia= (x*m);

        ConfirmarVentaFragment exampleDialog= new ConfirmarVentaFragment(nombre1,String.valueOf(x),vendidos.getText().toString(),String.valueOf(ganancia),perdidos.getText().toString(), uid,idV);
        exampleDialog.show(getSupportFragmentManager(),"Confirmar");

    }
    private void irError() {
        ErrorExistenciasFragment exampleDialog= new ErrorExistenciasFragment();
        exampleDialog.show(getSupportFragmentManager(),"Error");

    }
    public void incrementar(View view){
        cont++;
        showValue.setText(Integer.toString(cont));
    }

    public void decrementar(View view){
        if (cont<=0){
            showValue.setText(Integer.toString(cont));
        }else{
            cont--;
            showValue.setText(Integer.toString(cont));
        }
    }

    public void incrementarPerdidas(View view){
        cont2++;
        showValue2.setText(Integer.toString(cont2));
    }

    public void decrementarPerdidas(View view){
        if (cont2<=0){
            showValue2.setText(Integer.toString(cont2));
        }else{
            cont2--;
            showValue2.setText(Integer.toString(cont2));
        }
    }
    public void eliminarExistecia(String ven,String per, String uid){

        database.getReference().child(user.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {

                        String i= ds.getKey();

                        if (uid.equals(i)) {
                            String canti = ds.child("cantidad").getValue().toString();

                            int suma=Integer.parseInt(per)+Integer.parseInt(ven);
                            int sumaTotal=Integer.parseInt(canti)-suma;
                            if (suma<Integer.parseInt(canti)) {
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public double retornarPrecio(String uid){
        for (int i=0;i<lista_contacto.size();i++){
            if (uid.equals(lista_contacto.get(i).getIdByD())){
                double x=Double.parseDouble(lista_contacto.get(i).getPrecio());
                return x;
            }
        }
        return 0;
    }
    private void cargarInformacion() {
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
                        imagen=ds.child("imagen").getValue().toString();
                        idbased = ds.getKey();
                        producto=new Producto(nom,pre,can,id,max,min,idbased,imagen);
                        lista_contacto.add(producto);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void registrarVenta(String ganancia, String producto, String cantidad, String precio, String idV){
        Map<String, Object> venta = new HashMap<>();
        venta.put("ganancia", ganancia);
        venta.put("producto", producto);
        venta.put("cantidad",cantidad);
        venta.put("precio", precio);
        venta.put("id",idV);
        final DatabaseReference myRef = database.getReference().child(user.getUid()).child("Ventas");
        myRef.push().setValue(venta);
    }
    private void irHome() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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