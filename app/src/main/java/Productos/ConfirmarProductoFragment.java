package Productos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gestioninventario.R;

public class ConfirmarProductoFragment extends AppCompatDialogFragment implements View.OnClickListener {


    Button boton_si,boton_no;
    private String id;
    private String nombre;
    private String precio;
    private String cantidad;
    private String cA1;
    private String nA1;
    private String cA2;
    private String nA2;
    private String cA3;
    private String nA3;
    private String cA4;
    private String nA4;
    private String cA5;
    private String nA5;

    private String sMax;

    private String sMin;
    private String imagen;


    public ConfirmarProductoFragment (String nombre, String id, String precio, String cantidad,String sMax,String sMin, String imagen, String cA1, String nA1,String cA2, String nA2,String cA3, String nA3,String cA4, String nA4,String cA5, String nA5 ){
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.id = id;
        this.sMax = sMax;
        this.sMin = sMin;
        this.imagen= imagen;
        this.cA1=cA1;
        this.nA1=nA1;
        this.cA2=cA2;
        this.nA2=nA2;
        this.cA3=cA3;
        this.nA3=nA3;
        this.cA4=cA4;
        this.nA4=nA4;
        this.cA5=cA5;
        this.nA5=nA5;
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

            RegistroProductoActivity m=new RegistroProductoActivity();
            m.registrarProducto(nombre,id,precio,cantidad,sMax,sMin, imagen, cA1,cA2,cA3,cA4,cA5,nA1,nA2,nA3,nA4,nA5);
            getDialog().cancel();
            ExitoRegistroProductoFragment exampleDialog= new ExitoRegistroProductoFragment();
            exampleDialog.show(getActivity().getSupportFragmentManager(),"Exito");


        }
        if (v.getId()==R.id.boton_no){

            getDialog().cancel();
        }
    }


}
