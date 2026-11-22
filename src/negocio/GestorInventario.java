package negocio;

import modelo.Producto;

import java.util.ArrayList;

public class GestorInventario {

    //Atributos
    private ArrayList<Producto> productos;

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

    public ArrayList<Producto> listar(){
        return new ArrayList<>(productos);
    }

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

    public ArrayList<Producto> listarStockBajo(){
        ArrayList<Producto> productosStockBajo = new ArrayList<>();
        for (Producto p : productos){
            if (p.esStockBajo()){
                productosStockBajo.add(p);
            }
        }
        return productosStockBajo;
    }

    public ArrayList<Producto> listarProximosACaducar(){
        ArrayList<Producto> productosProximosACaducar = new ArrayList<>();
        for (Producto p : productos){
            if (p.estaProximoACaducar()){
                productosProximosACaducar.add(p);
            }
        }
        return productosProximosACaducar;
    }

    public double calcularValorTotal(){
        double valorTotal = 0;
        for (Producto p : productos){
            valorTotal += p.calcularValorCosto();
        }
        return valorTotal;
    }

}
