package Proveedores;

public class Proveedor {

    private String nombre;
    private String producto;
    private String telefono;
    private String direccion;
    private String idByD;
    private String imagen;


    public Proveedor(String nombre, String producto, String telefono, String direccion, String idByD, String imagen)  {
        this.nombre = nombre;
        this.producto = producto;
        this.telefono = telefono;
        this.direccion = direccion;
        this.idByD = idByD;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIdByD() {
        return idByD;
    }

    public void setIdByD(String idByD) {
        this.idByD = idByD;
    }

    public String getImagen() {return imagen;}

    public void setImagen(String imagen) {this.imagen = imagen;}

}
