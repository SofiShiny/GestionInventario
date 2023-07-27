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

public class ConfirmarCompraFragment extends AppCompatDialogFragment implements View.OnClickListener {


    Button boton_si,boton_no;
    private String costo;
    private String producto;
    private String cantidad;
    private String proveedor;
    private String idByd;
    private String id;
    public ConfirmarCompraFragment(String producto, String costo, String cantidad, String proveedor, String idByd, String id) {
        this.costo=costo;
        this.producto = producto;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
        this.idByd = idByd;
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
            AgregarExistenciaActivity m = new AgregarExistenciaActivity();
            m.agregarExistecia(cantidad,idByd);
            m.registrarCompra(producto,cantidad,costo,proveedor,id);

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
