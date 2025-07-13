package com.example.cookandquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
// import javafx.scene.image.Image;


import javafx.scene.canvas.GraphicsContext;

public class Menu {
    private List<Personaje> pedidos; // Lista dinámica de personajes

    private List<String> comidas = new ArrayList<>();

    private String[] platos = { "n1", "n2", "n3" };
    private Random rand = new Random();
    GraphicsContext gc;
    private int ancho = 94;
    private int alto = 50;
    // private int anchoVentana = 600;

    public Menu(GraphicsContext gc) {
        pedidos = new ArrayList<>();
        this.gc = gc; // Asignar el contexto gráfico
    }

    public void generarPedido() {
        int numeroAleatorio = rand.nextInt(platos.length);
        String ruta = "src/main/resources/" + platos[numeroAleatorio] + ".png";

        // Agregar nombre a lista dinámica
        switch (numeroAleatorio) {
            case 0: comidas.add("hamburguesa"); break;
            case 1: comidas.add("pizza"); break;
            case 2: comidas.add("tequeños"); break;
        }

        int espaciado = 10;
        int x = pedidos.size() * (ancho + espaciado);

        Personaje comida = new Personaje(x, 0, ruta, alto, ancho);
        agregarPedido(comida, x);
        mostrarPedidos();
    }

    public void agregarPedido(Personaje comida, int x) {
        pedidos.add(comida); // Agregar el pedido a la lista
    }

    public void mostrarPedidos() {
        for (int i = 0; i < pedidos.size(); i++) {
            Personaje pedido = pedidos.get(i);
            pedido.dibujar(gc); // Dibujar cada pedido en su posición
        }
    }

    public int getCantidadPedidos() {
        return pedidos.size();
    }

    // Método adicional si quieres acceder a comidas
    public String[] getPedidos() {
        return comidas.toArray(new String[0]);
    }


    public void EliminarPedido() {
        if (!pedidos.isEmpty() && !comidas.isEmpty()) {
            pedidos.remove(0);
            comidas.remove(0); // Elimina el primer nombre

            for (int i = 0; i < pedidos.size(); i++) {
                int nuevoX = i * (ancho + 10);
                pedidos.get(i).setX(nuevoX);
            }
        }
    }

}
