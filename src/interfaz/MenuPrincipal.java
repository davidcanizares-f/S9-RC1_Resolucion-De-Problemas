package interfaz;

import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import negocio.GestorInventario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPrincipal {

    // Atributos
    private GestorInventario gestor;
    private Scanner sc;

    // Constructor
    public MenuPrincipal() {
        this.gestor = new GestorInventario();
        this.sc = new Scanner(System.in);
    }

    // Metodos
    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== INVENTARIO =====");
            System.out.println("1. Registrar producto");
            System.out.println("2. Consultar productos");
            System.out.println("3. Actualizar stock");
            System.out.println("4. Productos con stock bajo");
            System.out.println("5. Productos próximos a caducar");
            System.out.println("6. Valor total del inventario");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            if (opcion == 1) {
                registrarProducto();
            } else if (opcion == 2) {
                consultarProductos();
            } else if (opcion == 3) {
                actualizarStock();
            } else if (opcion == 4) {
                mostrarStockBajo();
            } else if (opcion == 5) {
                mostrarProximosACaducar();
            } else if (opcion == 6) {
                mostrarValorInventario();
            } else if (opcion == 0) {
                System.out.println("Hasta luego.");
            } else {
                System.out.println("Opción no válida.");
            }
        }
    }

    private void registrarProducto() {
        System.out.print("Tipo (1 = Perecedero, 2 = No perecedero): ");
        int tipo = Integer.parseInt(sc.nextLine());
        System.out.print("Código: ");
        String codigo = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Categoría: ");
        String categoria = sc.nextLine();
        System.out.print("Cantidad: ");
        int cantidad = Integer.parseInt(sc.nextLine());
        System.out.print("Stock mínimo: ");
        int stockMinimo = Integer.parseInt(sc.nextLine());
        System.out.print("Costo unitario: ");
        double costo = Double.parseDouble(sc.nextLine());
        System.out.print("Precio de venta: ");
        double precio = Double.parseDouble(sc.nextLine());
        LocalDate fechaIngreso = LocalDate.now();

        Producto p;
        if (tipo == 1) {
            System.out.print("Fecha de caducidad (aaaa-mm-dd): ");
            LocalDate fechaCaducidad = LocalDate.parse(sc.nextLine());
            p = new ProductoPerecedero(codigo, nombre, categoria, cantidad, stockMinimo,
                    costo, precio, fechaIngreso, fechaCaducidad);
        } else {
            System.out.print("Ubicación en bodega: ");
            String ubicacion = sc.nextLine();
            p = new ProductoNoPerecedero(codigo, nombre, categoria, cantidad, stockMinimo,
                    costo, precio, fechaIngreso, ubicacion);
        }

        if (gestor.registrar(p)) {
            System.out.println("Producto registrado.");
        } else {
            System.out.println("No se pudo registrar (código repetido o datos vacíos).");
        }
    }

    private void consultarProductos() {
        ArrayList<Producto> lista = gestor.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay productos registrados.");
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private void actualizarStock() {
        System.out.print("Código del producto: ");
        String codigo = sc.nextLine();
        System.out.print("1 = Entrada, 2 = Salida: ");
        int tipo = Integer.parseInt(sc.nextLine());
        System.out.print("Cantidad: ");
        int cantidad = Integer.parseInt(sc.nextLine());

        boolean ok;
        if (tipo == 1) {
            ok = gestor.registrarEntrada(codigo, cantidad);
        } else {
            ok = gestor.registrarSalida(codigo, cantidad);
        }

        if (ok) {
            System.out.println("Stock actualizado.");
        } else {
            System.out.println("No se pudo actualizar (revisa el código y la cantidad).");
        }
    }

    private void mostrarStockBajo() {
        ArrayList<Producto> lista = gestor.listarStockBajo();
        if (lista.isEmpty()) {
            System.out.println("No hay productos con stock bajo.");
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private void mostrarProximosACaducar() {
        ArrayList<Producto> lista = gestor.listarProximosACaducar();
        if (lista.isEmpty()) {
            System.out.println("No hay productos próximos a caducar.");
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private void mostrarValorInventario() {
        System.out.println("Valor total del inventario: $" + gestor.calcularValorTotal());
    }
}
