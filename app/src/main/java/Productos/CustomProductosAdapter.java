package Productos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestioninventario.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomProductosAdapter extends BaseAdapter {

    Context context;
    List<Producto> lista_contacto;

    List<Producto> lista_buscador;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();


    public CustomProductosAdapter(Context context, List<Producto> lista_contacto) {
        this.context = context;
        this.lista_contacto = lista_contacto;
        lista_buscador= new ArrayList<>();
        lista_buscador.addAll(lista_contacto);
    }

    @Override
    public int getCount() {
        return lista_contacto.size();
    }

    public interface ItemClickListener{
        void onItemClick(Producto producto);
    }

    @Override
    public Object getItem(int position) {
        return lista_contacto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void filtroBusqueda(String buscar){
        int longitud= buscar.length();
        if(longitud==0){
            lista_contacto.clear();
            lista_contacto.addAll(lista_buscador);
        } else{
            List<Producto> coleccion= lista_contacto.stream().filter(i ->i.getNombre().toLowerCase().contains(buscar.toLowerCase()))
                    .collect(Collectors.toList());
            lista_contacto.clear();
            lista_contacto.addAll(coleccion);
        }
            notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageViewContacto;
        TextView textViewNombre;
        TextView textViewPrecio;
        TextView textViewCantidad;

        Producto c=lista_contacto.get(position);

        if (convertView==null)
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_contacto, null);

        imageViewContacto= convertView.findViewById(R.id.imageViewContacto);
        textViewNombre= convertView.findViewById(R.id.textViewNombre);
        textViewPrecio= convertView.findViewById(R.id.textViewPrecio);
        textViewCantidad= convertView.findViewById(R.id.textViewCantidadC);

        if(c.getImagen()!="noFoto"){

            StorageReference reference = storageReference.child("producto/"+c.getImagen());
            long Maxbytes=16384*16384;

            reference.getBytes(Maxbytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageViewContacto.setImageBitmap(bitmap);
                }
            });
        }

        textViewNombre.setText("Nombre: "+c.getNombre());
        textViewPrecio.setText("Precio: "+c.getPrecio());
        textViewCantidad.setText("Cantidad: "+ c.getCantidad());



        return convertView;
    }


}
