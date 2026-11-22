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

    public boolean registrar(Producto p){
        if (p != null && p.getCodigo() != null && !p.getCodigo().trim().isBlank()
                && p.getNombre() != null && !p.getNombre().trim().isBlank()
                && buscar(p.getCodigo()) == null){
            productos.add(p);
            return true;
        }
        return false;
    }

    public Producto buscar(String codigo){
        if (codigo != null && !codigo.trim().isBlank()){
            for (Producto p : productos){
                if (p.getCodigo().equals(codigo.trim())){
                    return p;
                }
            }
        }
        return null;
    }

    public ArrayList<Producto> listar(){
        return new ArrayList<>(productos);
    }

    public boolean registrarEntrada(String codigo, int cantidad){
        if (codigo != null && !codigo.trim().isBlank() && cantidad > 0){
            Producto p = buscar(codigo);
            if (p != null){
                p.setCantidad(p.getCantidad() + cantidad);
                return true;
            }
        }
        return false;
    }

    public boolean registrarSalida(String codigo, int cantidad){
        if (codigo != null && !codigo.trim().isBlank() && cantidad > 0){
            Producto p = buscar(codigo);
            if (p != null && p.getCantidad() >= cantidad){
                p.setCantidad(p.getCantidad() - cantidad);
                return true;
            }
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
