package negocio;

import modelo.Producto;

import java.util.ArrayList;
// Maneja la lista de productos y las reglas del inventario.
// No imprime nada: de eso se encarga MenuPrincipal.
public class GestorInventario {

    //Atributos

    private ArrayList<Producto> productos;
    // Capital aproximado del minimarket en USD; se usa para la alerta de RF6
    private static final double CAPITAL_MAXIMO = 1500.0;

    //Metodos
    public GestorInventario(){
        this.productos = new ArrayList<>();
    }

    // Registra el producto si no existe otro con el mismo codigo
    public boolean registrar(Producto p){
        if (p != null && buscar(p.getCodigo()) == null){
            productos.add(p);
            return true;
        }
        return false;
    }

    // Devuelve el producto con ese codigo, o null si no existe
    public Producto buscar(String codigo){
        for (Producto p : productos){
            if (p.getCodigo().equals(codigo)){
                return p;
            }
        }
        return null;
    }
    // Devuelve una copia para que nadie modifique la lista original desde afuera
    public ArrayList<Producto> listar(){
        return new ArrayList<>(productos);
    }
    // Suma unidades al stock. Rechaza si el producto no existe o la cantidad no es positiva
    public boolean registrarEntrada(String codigo, int cantidad){
        Producto p = buscar(codigo);
        if (p != null && cantidad > 0){
            p.setCantidad(p.getCantidad() + cantidad);
            return true;
        }
        return false;
    }

    // Rechaza la salida si no hay stock suficiente
    public boolean registrarSalida(String codigo, int cantidad){
        Producto p = buscar(codigo);
        if (p != null && cantidad > 0 && p.getCantidad() >= cantidad){
            p.setCantidad(p.getCantidad() - cantidad);
            return true;
        }
        return false;
    }
    // Recorre la lista y devuelve los productos con stock bajo (cantidad <= minimo)
    public ArrayList<Producto> listarStockBajo(){
        ArrayList<Producto> productosStockBajo = new ArrayList<>();
        for (Producto p : productos){
            if (p.esStockBajo()){
                productosStockBajo.add(p);
            }
        }
        return productosStockBajo;
    }
    // Recorre la lista y devuelve los perecederos proximos a caducar.
    // Llama a estaProximoACaducar() sin preguntar el tipo (polimorfismo)
    public ArrayList<Producto> listarProximosACaducar(){
        ArrayList<Producto> productosProximosACaducar = new ArrayList<>();
        for (Producto p : productos){
            if (p.estaProximoACaducar()){
                productosProximosACaducar.add(p);
            }
        }
        return productosProximosACaducar;
    }
    // Suma cantidad x costo de todos los products

    public double calcularValorTotal(){
        double valorTotal = 0;
        for (Producto p : productos){
            valorTotal += p.calcularValorCosto();
        }

        return valorTotal;
    }
    // True si el valor del inventario es mayor al capital maximo
    public boolean superaCapitalMaximo() {
        return calcularValorTotal() > CAPITAL_MAXIMO;
    }

}
