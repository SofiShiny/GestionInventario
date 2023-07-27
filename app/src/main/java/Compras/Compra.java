package Compras;

public class Compra {
    private String costo;
    private String idByD;

    private String producto;

    private String cantidad;
    private String proveedor;
    private String id;



    public Compra(String costo, String idByD, String producto, String cantidad, String proveedor,String id) {
        this.costo=costo;
        this.idByD = idByD;
        this.producto = producto;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
        this.id=id;
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

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
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
