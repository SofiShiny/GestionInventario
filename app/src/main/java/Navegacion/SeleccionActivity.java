package Navegacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import Productos.HomeActivity;
import Productos.ListViewAgregarExistencia;
import com.example.gestioninventario.R;
import Productos.RegistroProductoActivity;

public class SeleccionActivity extends AppCompatDialogFragment implements View.OnClickListener {

    Button buttonRegistro;
    Button buttonAgregarExistencia;
    ImageButton boton_salir;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle bundle){
        View view= inflater.inflate(R.layout.fragment_dialogo_seleccion,container,false);
        buttonRegistro=view.findViewById(R.id.boton_registrar);
        buttonAgregarExistencia=view.findViewById(R.id.boton_agregar_existencia);
        boton_salir=view.findViewById(R.id.boton_salir_seleccion);

        buttonRegistro.setOnClickListener(this);
        buttonAgregarExistencia.setOnClickListener(this);
        boton_salir.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.boton_registrar){
            Intent i= new Intent(getContext(), RegistroProductoActivity.class);
            getContext().startActivity(i);

        }
        if (v.getId()==R.id.boton_agregar_existencia){

            Intent i= new Intent(getContext(), ListViewAgregarExistencia.class);
            getContext().startActivity(i);

        }
        if (v.getId()==R.id.boton_salir_seleccion){

            Intent i= new Intent(getContext(), HomeActivity.class);
            getContext().startActivity(i);

            Toast.makeText(getActivity(),"Volviendo al inventario",Toast.LENGTH_SHORT).show();
            getDialog().cancel();
        }
    }



}
