package interfaz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import negocio.GestorInventario;

public class MenuPrincipal {

    private static final String[] CATEGORIAS = {"Abarrotes", "Lácteos", "Bebidas", "Snacks", "Limpieza"};

    // Atributos
    private GestorInventario gestor;
    private Scanner sc;

    // Constructor
    public MenuPrincipal() {
        this.gestor = new GestorInventario();
        this.sc = new Scanner(System.in);
    }

    // ==================== MENU ====================

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== MINIMARKET - INVENTARIO =====");
            System.out.println("1. Registrar producto");
            System.out.println("2. Consultar productos");
            System.out.println("3. Actualizar stock (entrada / salida)");
            System.out.println("4. Productos con stock bajo");
            System.out.println("5. Productos próximos a caducar");
            System.out.println("6. Valor total del inventario");
            System.out.println("0. Salir");
            opcion = leerEntero("Elija una opción: ", "opción", 0, 6);

            switch (opcion) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    consultarProductos();
                    break;
                case 3:
                    actualizarStock();
                    break;
                case 4:
                    mostrarStockBajo();
                    break;
                case 5:
                    mostrarProximosACaducar();
                    break;
                case 6:
                    mostrarValorInventario();
                    break;
                default:
                    System.out.println("Hasta pronto...");
                    break;
            }
        }
    }

    // ==================== OPCIONES ====================

    private void registrarProducto() {
        System.out.println("\n--- Registrar producto ---");

        // El codigo no puede estar vacio ni repetirse
        String codigo = leerTexto("Código: ", "código");
        while (gestor.buscar(codigo) != null) {
            System.out.println("Error: ya existe un producto con el código " + codigo + ".");
            codigo = leerTexto("Código: ", "código");
        }

        String nombre = leerTexto("Nombre: ", "nombre");

        System.out.println("Categorías:");
        for (int i = 0; i < CATEGORIAS.length; i++) {
            System.out.println((i + 1) + ") " + CATEGORIAS[i]);
        }
        int numCategoria = leerEntero("Elija la categoría: ", "categoría", 1, CATEGORIAS.length);
        String categoria = CATEGORIAS[numCategoria - 1];

        int cantidad = leerEntero("Cantidad: ", "cantidad", 0, 100000);
        int stockMinimo = leerEntero("Stock mínimo: ", "stock mínimo", 0, 100000);
        double costo = leerDecimal("Costo unitario (USD): ", "costo unitario", 0.01);
        double precio = leerDecimal("Precio de venta (USD): ", "precio de venta", costo);

        int tipo = leerEntero("¿Es perecedero? 1) Sí  2) No: ", "tipo de producto", 1, 2);

        Producto nuevo;
        if (tipo == 1) {
            LocalDate caducidad = leerFechaCaducidad();
            nuevo = new ProductoPerecedero(codigo, nombre, categoria, cantidad, stockMinimo,
                    costo, precio, LocalDate.now(), caducidad);
        } else {
            String ubicacion = leerTexto("Ubicación en bodega (ej. Estante A2): ", "ubicación");
            nuevo = new ProductoNoPerecedero(codigo, nombre, categoria, cantidad, stockMinimo,
                    costo, precio, LocalDate.now(), ubicacion);
        }

        if (gestor.registrar(nuevo)) {
            System.out.println("Producto registrado correctamente.");
        } else {
            System.out.println("Error: no se pudo registrar el producto.");
        }
    }

    private void consultarProductos() {
        System.out.println("\n--- Consultar productos ---");
        System.out.println("1) Listar todos");
        System.out.println("2) Buscar por código");
        int opcion = leerEntero("Elija una opción: ", "opción", 1, 2);

        if (opcion == 1) {
            ArrayList<Producto> lista = gestor.listar();
            if (lista.isEmpty()) {
                System.out.println("No hay productos registrados.");
            }
            for (Producto p : lista) {
                System.out.println(p);
            }
        } else {
            String codigo = leerTexto("Código a buscar: ", "código");
            Producto p = gestor.buscar(codigo);
            if (p == null) {
                System.out.println("Producto no encontrado.");
            } else {
                System.out.println(p);
            }
        }
    }

    private void actualizarStock() {
        System.out.println("\n--- Actualizar stock ---");
        String codigo = leerTexto("Código del producto: ", "código");
        Producto p = gestor.buscar(codigo);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.println("Producto: " + p.getNombre() + " | Stock actual: " + p.getCantidad());

        int tipo = leerEntero("1) Entrada  2) Salida: ", "tipo de movimiento", 1, 2);
        int cantidad = leerEntero("Cantidad: ", "cantidad", 1, 100000);

        if (tipo == 1) {
            gestor.registrarEntrada(codigo, cantidad);
            System.out.println("Entrada registrada. Nuevo stock: " + p.getCantidad());
        } else if (gestor.registrarSalida(codigo, cantidad)) {
            System.out.println("Salida registrada. Nuevo stock: " + p.getCantidad());
            if (p.esStockBajo()) {
                System.out.println("Aviso: el producto quedó con stock bajo (mínimo " + p.getStockMinimo() + ").");
            }
        } else {
            System.out.println("Error: stock insuficiente. Solo hay " + p.getCantidad()
                    + " unidad(es). Operación rechazada.");
        }
    }

    private void mostrarStockBajo() {
        System.out.println("\n--- Productos con stock bajo ---");
        ArrayList<Producto> lista = gestor.listarStockBajo();
        if (lista.isEmpty()) {
            System.out.println("No hay productos con stock bajo.");
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private void mostrarProximosACaducar() {
        System.out.println("\n--- Productos próximos a caducar ---");
        ArrayList<Producto> lista = gestor.listarProximosACaducar();
        if (lista.isEmpty()) {
            System.out.println("No hay productos próximos a caducar.");
        }
        for (Producto p : lista) {
            System.out.println(p);
            System.out.println("   Precio final con descuento: $" + Math.round(p.calcularPrecioFinal() * 100) / 100.0);
        }
    }

    private void mostrarValorInventario() {
        System.out.println("\n--- Valor total del inventario ---");
        System.out.println("Valor total: $" + Math.round(gestor.calcularValorTotal() * 100) / 100.0);
        if (gestor.superaCapitalMaximo()) {
            System.out.println("Alerta: el valor del inventario supera el capital de USD 1500.00.");
        }
    }

    // ==================== LECTURA Y VALIDACION (con while) ====================

    // Pide un texto y lo repite mientras este vacio
    private String leerTexto(String mensaje, String campo) {
        System.out.print(mensaje);
        String texto = sc.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.println("Error: el campo " + campo + " es obligatorio.");
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
        }
        return texto;
    }

    // Pide un entero entre minimo y maximo y lo repite hasta que sea valido
    private int leerEntero(String mensaje, String campo, int minimo, int maximo) {
        int valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Error: el campo " + campo + " es obligatorio.");
            } else if (!esNumero(texto, false)) {
                System.out.println("Error: " + campo + " debe ser un número entero válido, sin letras ni signos.");
            } else {
                valor = Integer.parseInt(texto);
                if (valor < minimo || valor > maximo) {
                    System.out.println("Error: " + campo + " debe estar entre " + minimo + " y " + maximo + ".");
                } else {
                    valido = true;
                }
            }
        }
        return valor;
    }

    // Pide un valor en dolares (maximo 2 decimales) mayor o igual a minimo
    private double leerDecimal(String mensaje, String campo, double minimo) {
        double valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim().replace(',', '.');
            if (texto.isEmpty()) {
                System.out.println("Error: el campo " + campo + " es obligatorio.");
            } else if (!esNumero(texto, true)) {
                System.out.println("Error: " + campo + " debe ser un número válido (ej. 0.95), sin letras ni signos.");
            } else if (texto.indexOf('.') != -1 && texto.length() - texto.indexOf('.') - 1 > 2) {
                System.out.println("Error: " + campo + " admite máximo 2 decimales.");
            } else {
                valor = Double.parseDouble(texto);
                if (valor < minimo) {
                    System.out.println("Error: " + campo + " debe ser como mínimo " + minimo + ".");
                } else {
                    valido = true;
                }
            }
        }
        return valor;
    }

    // Pide anio, mes y dia y repite hasta formar una fecha posterior a hoy
    private LocalDate leerFechaCaducidad() {
        LocalDate hoy = LocalDate.now();
        LocalDate fecha = null;
        while (fecha == null) {
            int anio = leerEntero("Año de caducidad: ", "año", hoy.getYear(), hoy.getYear() + 10);
            int mes = leerEntero("Mes de caducidad (1-12): ", "mes", 1, 12);
            int diasDelMes = LocalDate.of(anio, mes, 1).lengthOfMonth();
            int dia = leerEntero("Día de caducidad: ", "día", 1, diasDelMes);

            LocalDate candidata = LocalDate.of(anio, mes, dia);
            if (candidata.isAfter(hoy)) {
                fecha = candidata;
            } else {
                System.out.println("Error: la fecha de caducidad debe ser posterior a hoy (" + hoy + ").");
            }
        }
        return fecha;
    }

    // Revisa letra por letra que el texto sea un numero (maximo 9 caracteres)
    private boolean esNumero(String texto, boolean permitePunto) {
        if (texto.length() > 9) {
            return false;
        }
        int digitos = 0;
        int puntos = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c >= '0' && c <= '9') {
                digitos++;
            } else if (c == '.' && permitePunto) {
                puntos++;
            } else {
                return false;
            }
        }
        return digitos > 0 && puntos <= 1;
    }
}
