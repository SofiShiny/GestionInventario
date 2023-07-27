package Proveedores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gestioninventario.R;

public class ConfirmarModificacionProveedorFragment extends AppCompatDialogFragment implements View.OnClickListener{
    Button boton_si,boton_no;
    String nombre,producto,telefono,direccion,imagen,uid;

    public ConfirmarModificacionProveedorFragment(String nombre, String producto, String telefono, String direccion, String imagen, String uid)  {
        this.nombre = nombre;
        this.producto = producto;
        this.telefono = telefono;
        this.direccion = direccion;
        this.imagen = imagen;
        this.uid=uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle bundle){
        View view= inflater.inflate(R.layout.confirmar_dialog,container,false);
        boton_si=view.findViewById(R.id.boton_si);
        boton_no=view.findViewById(R.id.boton_no);

        boton_si.setOnClickListener(this);
        boton_no.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.boton_si){
            ModificarProveedorActivity m=new ModificarProveedorActivity();
            m.modificarDatos(nombre,producto,telefono,direccion,imagen,uid);

            getDialog().cancel();
            ExitoModificacionProveedorFragment exampleDialog= new ExitoModificacionProveedorFragment();
            exampleDialog.show(getActivity().getSupportFragmentManager(),"Exito");

        }
        if (v.getId()==R.id.boton_no){

            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            /*quitar fragment*/

        }
    }
}
