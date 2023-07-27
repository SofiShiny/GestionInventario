package Proveedores;

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

import Proveedores.Proveedor;

public class CustomProveedoresAdapter extends BaseAdapter {
    Context context;
    List<Proveedor> lista_proveedores;

    List<Proveedor> lista_buscador;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    public CustomProveedoresAdapter(Context context, List<Proveedor> lista_proveedores) {
        this.context = context;
        this.lista_proveedores = lista_proveedores;
        lista_buscador= new ArrayList<>();
        lista_buscador.addAll(lista_proveedores);
    }

    @Override
    public int getCount() {
        return lista_proveedores.size();
    }

    

    @Override
    public Object getItem(int position) {
        return lista_proveedores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void filtroBusqueda(String buscar){
        int longitud= buscar.length();
        if(longitud==0){
            lista_proveedores.clear();
            lista_proveedores.addAll(lista_buscador);
        } else{
            List<Proveedor> coleccion= lista_proveedores.stream().filter(i ->i.getNombre().toLowerCase().contains(buscar.toLowerCase()))
                    .collect(Collectors.toList());
            lista_proveedores.clear();
            lista_proveedores.addAll(coleccion);
        }
            notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewNombreProveedor,textViewProductoProveedor;
        ImageView imagenProveedor;

        Proveedor c=lista_proveedores.get(position);

        if (convertView==null)
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_proveedor, null);

        textViewNombreProveedor= convertView.findViewById(R.id.textViewNombreProveedor);
        textViewProductoProveedor= convertView.findViewById(R.id.textViewProductoProveedor);
        imagenProveedor= convertView.findViewById((R.id.imageViewProveedor));

        if(c.getImagen()!="noFoto"){

            StorageReference reference = storageReference.child("proveedores/"+c.getImagen());
            long Maxbytes=16384*16384;

            reference.getBytes(Maxbytes).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imagenProveedor.setImageBitmap(bitmap);
                }
            });
        }

        textViewNombreProveedor.setText("Nombre: "+c.getNombre());
        textViewProductoProveedor.setText("Producto: "+c.getProducto());

        return convertView;
    }
}
