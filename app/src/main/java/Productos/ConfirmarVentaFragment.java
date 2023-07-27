package Productos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestioninventario.R;

public class ConfirmarVentaFragment extends AppCompatDialogFragment implements View.OnClickListener {


    Button boton_si,boton_no;
    String nombre,precio,vendidos,ganancia,perdida,uid,id;


    public ConfirmarVentaFragment(String nombre, String precio, String vendidos, String ganancia,String perdida,String uid, String id){
        this.nombre=nombre;
        this.precio=precio;
        this.vendidos=vendidos;
        this.ganancia=ganancia;
        this.perdida=perdida;
        this.uid=uid;
        this.id=id;
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
            EliminarExistenciaActivity m= new EliminarExistenciaActivity();
            if (Integer.parseInt(perdida)>0){
                m.eliminarExistecia(vendidos,perdida,uid);
            }
            if (Integer.parseInt(vendidos)>0){
                m.eliminarExistecia(vendidos,perdida,uid);
                m.registrarVenta(ganancia,nombre,vendidos,precio,id);
            }
            getDialog().cancel();
            ExitoTransaccionFragment exampleDialog= new ExitoTransaccionFragment();
            exampleDialog.show(getActivity().getSupportFragmentManager(),"Exito");

        }
        if (v.getId()==R.id.boton_no){

            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            /*quitar fragment*/

        }
    }


}
