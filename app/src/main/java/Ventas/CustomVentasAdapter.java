package Ventas;

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

public class CustomVentasAdapter extends BaseAdapter {

    Context context;
    List<Venta> lista_ventas;

    List<Venta> lista_buscador;


    public CustomVentasAdapter(Context context, List<Venta> lista_ventas) {
        this.context = context;
        this.lista_ventas = lista_ventas;
        lista_buscador= new ArrayList<>();
        lista_buscador.addAll(lista_ventas);
    }

    @Override
    public int getCount() {
        return lista_ventas.size();
    }

    public interface ItemClickListener{
        void onItemClick(Venta venta);
    }

    @Override
    public Object getItem(int position) {
        return lista_ventas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void filtroBusqueda(String buscar){
        int longitud= buscar.length();
        if(longitud==0){
            lista_ventas.clear();
            lista_ventas.addAll(lista_buscador);
        } else{
            List<Venta> coleccion= lista_ventas.stream().filter(i ->i.getProducto().toLowerCase().contains(buscar.toLowerCase()))
                    .collect(Collectors.toList());
            lista_ventas.clear();
            lista_ventas.addAll(coleccion);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewGanancia,textViewProducto;

        Venta c=lista_ventas.get(position);

        if (convertView==null)
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_ventas, null);

        textViewGanancia= convertView.findViewById(R.id.textViewGanancia);
        textViewProducto= convertView.findViewById(R.id.textViewProductoC);
        textViewGanancia.setText("Ganancia: "+c.getGanancia());
        textViewProducto.setText("Producto: "+c.getProducto());


        return convertView;
    }


}
