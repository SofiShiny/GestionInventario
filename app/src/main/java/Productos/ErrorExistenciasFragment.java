package Productos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gestioninventario.R;

public class ErrorExistenciasFragment extends AppCompatDialogFragment implements View.OnClickListener {


    Button boton_ok;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle bundle){
        View view= inflater.inflate(R.layout.error_existencia_dialog,container,false);
        boton_ok=view.findViewById(R.id.boton_ok);

        boton_ok.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.boton_ok){

            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        }

    }


}
