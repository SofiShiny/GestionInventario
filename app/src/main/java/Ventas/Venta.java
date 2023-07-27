package Ventas;

public class Venta {

    private String ganancia;
    private String idByD;

    private String producto;

    private String cantidad;
    private String precio;
    private String id;

    public Venta(String ganancia, String idByD, String producto, String cantidad, String precio,String id) {
        this.ganancia = ganancia;
        this.idByD = idByD;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.id= id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }


    public String getGanancia() {
        return ganancia;
    }

    public void setGanancia(String ganancia) {
        this.ganancia = ganancia;
    }

    public String getIdByD(){return idByD;}
    public void setIdByD(String idByD){this.idByD=idByD;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
