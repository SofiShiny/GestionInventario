package Productos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gestioninventario.R;

import Productos.RegistroProductoActivity;

public class CaracteristicaAdicionalActivity extends AppCompatDialogFragment implements View.OnClickListener {

    Button buttonAgregarCaracteristica;
    ImageButton boton_salir;
    EditText editTextNombreCaracteristica,caracteristica2,caracteristica3,caracteristica4,caracteristica5,numeroCaracteristicas;
    String nombre,id,precio,cantidad,sMax,sMin,imagen;
    Uri image_Url;

    public CaracteristicaAdicionalActivity(String nombre,String id,String precio,String cantidad,String sMax,String sMin, String imagen) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.id = id;
        this.sMax = sMax;
        this.sMin = sMin;
        this.imagen=imagen;
    }

    public CaracteristicaAdicionalActivity(String nombre,String id,String precio,String cantidad,String sMax,String sMin, Uri image_Url) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.id = id;
        this.sMax = sMax;
        this.sMin = sMin;
        this.image_Url=image_Url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle bundle){
        View view= inflater.inflate(R.layout.fragment_caracteristica_nueva,container,false);
        buttonAgregarCaracteristica=view.findViewById(R.id.boton_agregar_caracteristica);
        boton_salir=view.findViewById(R.id.boton_salir_caracteristica);
        editTextNombreCaracteristica=view.findViewById(R.id.editTextNombreCaracteristica);
        caracteristica2=view.findViewById(R.id.editTextNombreCaracteristica2);
        caracteristica3=view.findViewById(R.id.editTextNombreCaracteristica3);
        caracteristica4=view.findViewById(R.id.editTextNombreCaracteristica4);
        caracteristica5=view.findViewById(R.id.editTextNombreCaracteristica5);
        Log.d("nombre",nombre);
        Log.d("nombre",id);
        Log.d("nombre",precio);
        Log.d("nombre",cantidad);
        Log.d("nombre",sMax);
        Log.d("nombre",sMin);


        inhabilitarCampos();
        editTextNombreCaracteristica.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0){
                    caracteristica2.setEnabled(true);
                    caracteristica3.setEnabled(false);
                    caracteristica4.setEnabled(false);
                    caracteristica5.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        caracteristica2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count>0){
                        editTextNombreCaracteristica.setEnabled(true);
                        caracteristica3.setEnabled(true);
                        caracteristica4.setEnabled(false);
                        caracteristica5.setEnabled(false);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        caracteristica3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0){
                    editTextNombreCaracteristica.setEnabled(true);
                    caracteristica2.setEnabled(true);
                    caracteristica4.setEnabled(true);
                    caracteristica5.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        caracteristica4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0){
                    editTextNombreCaracteristica.setEnabled(true);
                    caracteristica2.setEnabled(true);
                    caracteristica3.setEnabled(true);
                    caracteristica5.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        buttonAgregarCaracteristica.setOnClickListener(this);
        boton_salir.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean validar=true;
        if (v.getId()==R.id.boton_agregar_caracteristica){

            Intent i= new Intent(getContext(), RegistroProductoActivity.class);
            i.putExtra("caracteristica1",editTextNombreCaracteristica.getText().toString());
            i.putExtra("caracteristica2",caracteristica2.getText().toString());
            i.putExtra("caracteristica3",caracteristica3.getText().toString());
            i.putExtra("caracteristica4",caracteristica4.getText().toString());
            i.putExtra("caracteristica5",caracteristica5.getText().toString());
            i.putExtra("nombre",nombre);
            i.putExtra("id",id);
            i.putExtra("precio",precio);
            i.putExtra("cantidad",cantidad);
            i.putExtra("max",sMax);
            i.putExtra("min",sMin);
            i.putExtra("imagen",imagen);
            int cont=campos();
            i.putExtra("Contador",cont);
            i.putExtra("val",validar);
            getContext().startActivity(i);

        }
        if (v.getId()==R.id.boton_salir_caracteristica){

            Intent i= new Intent(getContext(),RegistroProductoActivity.class);
            i.putExtra("nombre",nombre);
            i.putExtra("id",id);
            i.putExtra("precio",precio);
            i.putExtra("cantidad",cantidad);
            i.putExtra("max",sMax);
            i.putExtra("min",sMin);
            int cont=campos();
            i.putExtra("Contador",cont);
            i.putExtra("val",validar);
            getContext().startActivity(i);

            Toast.makeText(getActivity(),"Volviendo a registro de producto",Toast.LENGTH_SHORT).show();
            getDialog().cancel();
        }
    }
     public int campos(){
        int cont=0;
        if ((!editTextNombreCaracteristica.getText().toString().isEmpty()) && (caracteristica2.getText().toString().isEmpty()) && (caracteristica3.getText().toString().isEmpty()) && (caracteristica4.getText().toString().isEmpty()) && (caracteristica5.getText().toString().isEmpty())){
            cont=1;
            return cont;
        }else if ((!caracteristica2.getText().toString().isEmpty()) && (!editTextNombreCaracteristica.getText().toString().isEmpty()) && (caracteristica3.getText().toString().isEmpty()) && (caracteristica4.getText().toString().isEmpty()) && (caracteristica5.getText().toString().isEmpty())){
            cont=2;
            return cont;
       }else if ((!caracteristica3.getText().toString().isEmpty()) && (!editTextNombreCaracteristica.getText().toString().isEmpty()) && (!caracteristica2.getText().toString().isEmpty())  && (caracteristica4.getText().toString().isEmpty()) && (caracteristica5.getText().toString().isEmpty())){
            cont=3;
            return cont;
        }else if ((!caracteristica4.getText().toString().isEmpty()) && (!editTextNombreCaracteristica.getText().toString().isEmpty()) && (!caracteristica2.getText().toString().isEmpty()) && (!caracteristica3.getText().toString().isEmpty()) && (caracteristica5.getText().toString().isEmpty()) ){
            cont=4;
            return cont;
        }else if ((!caracteristica5.getText().toString().isEmpty()) && (!editTextNombreCaracteristica.getText().toString().isEmpty()) && (!caracteristica2.getText().toString().isEmpty()) && (!caracteristica3.getText().toString().isEmpty()) && (!caracteristica4.getText().toString().isEmpty())){
            cont=5;
            return cont;
        }
      return 0;
     }
    public void validarCampos(){
        if (editTextNombreCaracteristica.getText().toString().isEmpty()){
            caracteristica2.setEnabled(false);
            caracteristica3.setEnabled(false);
            caracteristica4.setEnabled(false);
            caracteristica5.setEnabled(false);
        } else if (!editTextNombreCaracteristica.getText().toString().isEmpty()){
            caracteristica2.setEnabled(true);
            caracteristica3.setEnabled(false);
            caracteristica4.setEnabled(false);
            caracteristica5.setEnabled(false);
        } else if ((!editTextNombreCaracteristica.getText().toString().isEmpty()) && (!caracteristica2.getText().toString().isEmpty())){
            caracteristica4.setEnabled(false);
            caracteristica5.setEnabled(false);
        } else if ((!editTextNombreCaracteristica.getText().toString().isEmpty()) && (!caracteristica2.getText().toString().isEmpty()) && (!caracteristica3.getText().toString().isEmpty())) {
            caracteristica5.setEnabled(false);
        } else if ((!editTextNombreCaracteristica.getText().toString().isEmpty()) && (!caracteristica2.getText().toString().isEmpty())  && (!caracteristica3.getText().toString().isEmpty())  && (!caracteristica4.getText().toString().isEmpty())){
            caracteristica5.setEnabled(true);
        }
    }
  public void inhabilitarCampos(){
      if (editTextNombreCaracteristica.getText().toString().isEmpty()){
          caracteristica2.setEnabled(false);
          caracteristica3.setEnabled(false);
          caracteristica4.setEnabled(false);
          caracteristica5.setEnabled(false);
      }
  }

}
