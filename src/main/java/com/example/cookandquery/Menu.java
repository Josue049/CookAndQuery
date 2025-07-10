package com.example.cookandquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class Menu {
    private List<Personaje> pedidos; // Lista dinámica de personajes
    private String[] platos = { "n1", "n2", "n3" };
    private Random rand = new Random();
    GraphicsContext gc;
    private int ancho = 118;
    private int alto = 63;
    private int anchoVentana = 600; // Ancho de la ventana del juego
    
    public Menu(GraphicsContext gc) {
        pedidos = new ArrayList<>();
        this.gc = gc; // Asignar el contexto gráfico
    }

    public void generarPedido() {
        int numeroAleatorio = rand.nextInt(platos.length); // 0, 1 o 2
        String ruta = "src/main/resources/" + platos[numeroAleatorio] + ".png";

        int espaciado = 10;
        int x = pedidos.size() * (ancho + espaciado); // Espacio entre personajes

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
}
