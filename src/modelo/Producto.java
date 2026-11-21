package modelo;
import java.time.LocalDate;


public abstract class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private int cantidad;
    private int stockMinimo;
    private double costoUnitario;
    private double precioVenta;
    private LocalDate fechaIngreso;

    public Producto(String codigo, String nombre, String categoria, int cantidad, int stockMinimo, double costoUnitario, double precioVenta, LocalDate fechaIngreso){
        this.codigo = codigo;
        setNombre(nombre);
        setCategoria(categoria);
        setCantidad(cantidad);
        setStockMinimo(stockMinimo);
        setCostoUnitario(costoUnitario);
        setPrecioVenta(precioVenta);
        setFechaIngreso(fechaIngreso);
    }
    // GETTERS Y SETTERS ==========================================================0

    public String getCodigo() {
        return codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public final void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre.trim();
        }
    }

    public String getCategoria() {
        return categoria;
    }

    public final void setCategoria(String categoria) {
        if (categoria != null && !categoria.trim().isEmpty()) {
            this.categoria = categoria.trim();
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public final void setCantidad(int cantidad) {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        }
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public final void setStockMinimo(int stockMinimo) {
        if (stockMinimo >= 0) {
            this.stockMinimo = stockMinimo;
        }
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public final void setCostoUnitario(double costoUnitario) {
        if (costoUnitario > 0) {
            this.costoUnitario = costoUnitario;
        }
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public final void setPrecioVenta(double precioVenta) {
        if (precioVenta > 0) {
            this.precioVenta = precioVenta;
        }
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public final void setFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso != null) {
            this.fechaIngreso = fechaIngreso;
        }
    }

    //METODOS COMUNES =====================================================

    // Indica si la cantidad en bodega es menor o igual al stock minimo
    public boolean esStockBajo() {
        return cantidad <= stockMinimo;
    }
    //Valor del producto en bodega segun su costo: cantidad x costo unitario
    public double calcularValorCosto(){
        return cantidad * costoUnitario;
    }

    //Metodos absractos
    // Precio al que se vende hoy el producto. Cada subclase lo calcula a su manera
    public abstract double calcularPrecioFinal();

    // Indica si el producto esta por caducar. Cada subclase lo decide a su manera
    public abstract boolean estaProximoACaducar();

    @Override
    public String toString() {
        return "[" + codigo + "]: " + nombre +'\n'
                + "> Categoria: " + categoria +'\n'
                + "> Cantidad: " + cantidad +'\n'
                + "> Mínimo: " + stockMinimo +'\n'
                + "> Costo: $" + costoUnitario +'\n'
                + "> Precio venta: $" + precioVenta +'\n'
                + "--------------------------------------------";
    }
}


