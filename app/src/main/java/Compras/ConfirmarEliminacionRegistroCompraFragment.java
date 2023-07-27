package Compras;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gestioninventario.R;

import Productos.ConsultarProductoActivity;
import Productos.ExitoTransaccionFragment;

public class ConfirmarEliminacionRegistroCompraFragment extends AppCompatDialogFragment implements View.OnClickListener{
    Button boton_si,boton_no;
    private String idByd;
    private final String cantidad;


    public ConfirmarEliminacionRegistroCompraFragment(String cantidad, String idByd) {

        this.cantidad = cantidad;

        this.idByd = idByd;

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

            ConsultarComprasActivity m= new ConsultarComprasActivity();
            Log.d("hola1234",cantidad);
            m.eliminarCantidad(cantidad,idByd);
            m.eliminarCompra(idByd);


            getDialog().cancel();
            ExitoTransaccionComprasFragment exampleDialog= new ExitoTransaccionComprasFragment();
            exampleDialog.show(getActivity().getSupportFragmentManager(),"Exito");

        }
        if (v.getId()==R.id.boton_no){

            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            /*quitar fragment*/

        }
    }
}
