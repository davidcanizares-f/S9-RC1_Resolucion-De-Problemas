package modelo;

import java.time.LocalDate;

public class ProductoNoPerecedero extends Producto{
    private String ubicacionBodega;

    public ProductoNoPerecedero(String codigo, String nombre, String categoria, int cantidad, int stockMinimo, double costoUnitario, double precioVenta, LocalDate fechaIngreso, String ubicacionBodega){
        super(codigo, nombre, categoria, cantidad, stockMinimo, costoUnitario, precioVenta, fechaIngreso);
        this.ubicacionBodega = ubicacionBodega;
    }
    public String getUbicacionBodega(){
        return ubicacionBodega;
    }
    public void setUbicacionBodega(String ubicacionBodega) {
        if (ubicacionBodega != null && !ubicacionBodega.trim().isEmpty()) {
            this.ubicacionBodega = ubicacionBodega.trim();
        }
    }
    @Override
    public boolean estaProximoACaducar() {
        return false;
    }
    @Override
    public double calcularPrecioFinal() {
        return getPrecioVenta();
    }

    @Override
    public String toString(){
        return super.toString() + "\n> Ubicación: " + ubicacionBodega;
    }
}
