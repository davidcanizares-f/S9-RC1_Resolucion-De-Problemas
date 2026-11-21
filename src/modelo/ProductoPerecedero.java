package modelo;
import java.time.LocalDate;

public class ProductoPerecedero extends Producto{
    private static final int DIAS_ALERTA = 3;
    private static final double DESCUENTO = 0.30;
    private LocalDate fechaCaducidad;

    public ProductoPerecedero(String codigo, String nombre, String categoria, int cantidad, int stockMinimo, double costoUnitario, double precioVenta, LocalDate fechaIngreso, LocalDate fechaCaducidad){
        super(codigo, nombre, categoria, cantidad, stockMinimo, costoUnitario, precioVenta, fechaIngreso);
        this.fechaCaducidad = fechaCaducidad;
    }
    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }
    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        if (fechaCaducidad != null) {
            this.fechaCaducidad = fechaCaducidad;
        }
    }

    //Dias que faltan desde hoy hasta la caducidad (negativo si ya caduco)
    public int diasParaCaducar() {
        return (int) (fechaCaducidad.toEpochDay() - LocalDate.now().toEpochDay());
    }

    @Override
    public boolean estaProximoACaducar() {
        return diasParaCaducar() <= DIAS_ALERTA;
    }

    @Override
    public double calcularPrecioFinal() {
        if (estaProximoACaducar()) {
            return getPrecioVenta() * (1 - DESCUENTO);
        }
        return getPrecioVenta();
    }

    @Override
    public String toString() {
        int dias = diasParaCaducar();
        String estado;
        if (dias < 0) {
            estado = "VENCIDO";
        } else {
            estado = "en " + dias + " día(s)";
        }
        return super.toString()
                + "\n>Caduca: " + String.format("%02d/%02d/%d",
                fechaCaducidad.getDayOfMonth(),
                fechaCaducidad.getMonthValue(),
                fechaCaducidad.getYear())
                + " (" + estado + ")";
    }

}
