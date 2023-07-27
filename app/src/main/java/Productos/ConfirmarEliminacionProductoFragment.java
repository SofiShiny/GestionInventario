package Productos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gestioninventario.R;

public class ConfirmarEliminacionProductoFragment extends AppCompatDialogFragment implements View.OnClickListener {


    Button boton_si,boton_no;
    String idBd;

    public ConfirmarEliminacionProductoFragment(String idBd){
        this.idBd = idBd;

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
            ConsultarProductoActivity m= new ConsultarProductoActivity();
            m.eliminarProducto(idBd);

            getDialog().cancel();
            ExitoEliminacionProductoFragment exampleDialog= new ExitoEliminacionProductoFragment();
            exampleDialog.show(getActivity().getSupportFragmentManager(),"Exito");

        }
        if (v.getId()==R.id.boton_no){

            getDialog().cancel();
        }
    }


}
