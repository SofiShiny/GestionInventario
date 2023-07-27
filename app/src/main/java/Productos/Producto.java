package Productos;

public class Producto {
    private String idByD;
    private String id;
    private String imagen;
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



    public Producto(String id, String imagen, String nombre, String precio, String cantidad, String sMax, String sMin) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.sMax = sMax;
        this.sMin = sMin;
    }
    public Producto(String nombre, String precio, String cantidad,String id,String sMax,String sMin, String idByD, String imagen) {

        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.id = id;
        this.sMax = sMax;
        this.sMin = sMin;
        this.idByD=idByD;
        this.imagen=imagen;
    }
    public Producto(String nombre, String precio, String cantidad,String id,String sMax,String sMin, String idByD, String imagen, String cA1, String nA1,String cA2, String nA2,String cA3, String nA3,String cA4, String nA4,String cA5, String nA5 ) {

        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.id = id;
        this.sMax = sMax;
        this.sMin = sMin;
        this.imagen = imagen;
        this.idByD=idByD;
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
    public String getIdByD(){return idByD;}
    public void setIdByD(String idByD){this.idByD=idByD;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getsMax() {
        return sMax;
    }

    public void setsMax(String sMax) {
        this.sMax = sMax;
    }

    public String getsMin() {
        return sMin;
    }

    public void setsMin(String sMin) {
        this.sMin = sMin;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getcA1() {
        return cA1;
    }

    public void setcA1(String cA1) {
        this.cA1 = cA1;
    }

    public String getnA1() {
        return nA1;
    }

    public void setnA1(String nA1) {
        this.nA1 = nA1;
    }

    public String getcA2() {
        return cA2;
    }

    public void setcA2(String cA2) {
        this.cA2 = cA2;
    }

    public String getnA2() {
        return nA2;
    }

    public void setnA2(String nA2) {
        this.nA2 = nA2;
    }

    public String getcA3() {
        return cA3;
    }

    public void setcA3(String cA3) {
        this.cA3 = cA3;
    }

    public String getnA3() {
        return nA3;
    }

    public void setnA3(String nA3) {
        this.nA3 = nA3;
    }

    public String getcA4() {
        return cA4;
    }

    public void setcA4(String cA4) {
        this.cA4 = cA4;
    }

    public String getnA4() {
        return nA4;
    }

    public void setnA4(String nA4) {
        this.nA4 = nA4;
    }

    public String getcA5() {
        return cA5;
    }

    public void setcA5(String cA5) {
        this.cA5 = cA5;
    }

    public String getnA5() {
        return nA5;
    }

    public void setnA5(String nA5) {
        this.nA5 = nA5;
    }
}

