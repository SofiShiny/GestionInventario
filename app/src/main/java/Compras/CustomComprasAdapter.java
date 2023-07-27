package Compras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gestioninventario.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomComprasAdapter extends BaseAdapter {
    Context context;
    List<Compra> lista_compras;

    List<Compra> lista_buscador;


    public CustomComprasAdapter(Context context, List<Compra> lista_ventas) {
        this.context = context;
        this.lista_compras = lista_ventas;
        lista_buscador= new ArrayList<>();
        lista_buscador.addAll(lista_ventas);
    }

    @Override
    public int getCount() {
        return lista_compras.size();
    }


    @Override
    public Object getItem(int position) {
        return lista_compras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void filtroBusqueda(String buscar){
        int longitud= buscar.length();
        if(longitud==0){
            lista_compras.clear();
            lista_compras.addAll(lista_buscador);
        } else{
            List<Compra> coleccion= lista_compras.stream().filter(i ->i.getProducto().toLowerCase().contains(buscar.toLowerCase()))
                    .collect(Collectors.toList());
            lista_compras.clear();
            lista_compras.addAll(coleccion);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewCosto,textViewProducto;

        Compra c=lista_compras.get(position);

        if (convertView==null)
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_compras, null);

        textViewCosto= convertView.findViewById(R.id.textViewCosto);
        textViewProducto= convertView.findViewById(R.id.textViewProductoC);
        textViewCosto.setText("Costo: "+c.getCosto());
        textViewProducto.setText("Producto: "+c.getProducto());


        return convertView;
    }

}
