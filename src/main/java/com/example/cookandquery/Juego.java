package com.example.cookandquery;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Juego extends Application {
    GraphicsContext gc;
    private Image[] animaciones = new Image[7];
    boolean creacionParedDerecha = true;
    boolean creacionParedArriba = true;
    boolean creacionParedIzquierda = true;
    boolean creacionParedAbajo = true;

    @Override
    public void start(Stage ventana) {
        final int anchoVentana = 600;
        final int altoVentana = 700;

        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query");

        // Crear personajes
        Personaje FondoCocina = new Personaje(0, 0, "src/main/resources/fondoo.png", altoVentana, anchoVentana);
        Personaje chefAnimado = new Personaje(300, 200, 70, 70, "src/main/resources/SpriteChefFinal/down", 7, "", 30);
        Personaje NuevoChef = new Personaje(300, 300, 70, 70, "src/main/resources/SpriteChefFinal/up", 7, "up", 30);

        // Obtener el contexto gráfico
        gc = escenaJuego.getGraficos();
        Set<KeyCode> teclasActivas = new HashSet<>();

        escenaJuego.getEscena().setOnKeyPressed(e -> teclasActivas.add(e.getCode()));
        escenaJuego.getEscena().setOnKeyReleased(e -> teclasActivas.remove(e.getCode()));

        // Cargar animaciones
        Image[] chefArriba = new Image[7];
        Image[] chefAbajo = new Image[7];
        Image[] chefIzquierda = new Image[7];
        Image[] chefDerecha = new Image[7];
        cargarAnimacion("src/main/resources/SpriteChefFinal/up", "up", chefArriba);
        cargarAnimacion("src/main/resources/SpriteChefFinal/down", "", chefAbajo);
        cargarAnimacion("src/main/resources/SpriteChefFinal/left", "left", chefIzquierda);
        cargarAnimacion("src/main/resources/SpriteChefFinal/right", "right", chefDerecha);

        // forzarAnimacion("src/main/resources/Personajes/normal/normal5", chefArriba);
        // forzarAnimacion("src/main/resources/Personajes/normal/normal1", chefAbajo);
        // forzarAnimacion("src/main/resources/Personajes/normal/normal3",
        // chefIzquierda);
        // forzarAnimacion("src/main/resources/Personajes/normal/normal3", chefDerecha);
        animaciones = chefAbajo; // Inicializar con la animación de abajo

        gc.setFill(Color.BROWN); // por ejemplo, una mesa

        double[] xParedDerecha = { 454, 484, 510, 475 };
        double[] yParedDerecha = { 80, 75, 300, 300 };

        double[] xParedIzquierda = { 148, 114, 79, 118 };
        double[] yParedIzquierda = { 67, 67, 310, 310 };

        double[] xParedArriba = { 130, 465, 476, 130 };
        double[] yParedArriba = { 74, 74, 92, 92 };

        double[] xParedAbajo = { 98, 488, 488, 95 };
        double[] yParedAbajo = { 296, 295, 326, 325 };

        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Limpiar pantalla
                gc.clearRect(0, 0, anchoVentana, altoVentana);

                // Redibujar fondo para que se vea siempre
                escenaJuego.dibujarVentanaConFondos();

                // Actualizar y dibujar personajes
                FondoCocina.dibujar(gc);

                chefAnimado.refrescarAnimacion(now, gc, animaciones);
                // NuevoChef.refrescarAnimacion(now, gc, animaciones);

                if (teclasActivas.contains(KeyCode.UP) || teclasActivas.contains(KeyCode.W)) {
                    animaciones = chefArriba;
                    chefAnimado.mover(0, -5);
                }
                if (teclasActivas.contains(KeyCode.DOWN) || teclasActivas.contains(KeyCode.S)) {
                    animaciones = chefAbajo;
                    chefAnimado.mover(0, 5);
                }
                if (teclasActivas.contains(KeyCode.LEFT) || teclasActivas.contains(KeyCode.A)) {
                    animaciones = chefIzquierda;
                    chefAnimado.mover(-5, 0);
                }
                if (teclasActivas.contains(KeyCode.RIGHT) || teclasActivas.contains(KeyCode.D)) {
                    animaciones = chefDerecha;
                    chefAnimado.mover(5, 0);
                }

                colision(xParedDerecha, yParedDerecha, chefAnimado, 2, false);
                colision(xParedIzquierda, yParedIzquierda, chefAnimado, 3, false);
                colision(xParedArriba, yParedArriba, chefAnimado, 1, false);
                colision(xParedAbajo, yParedAbajo, chefAnimado, 0, false);

            }
        }.start();

    }

    public void cargarAnimacion(String carpetaSprites, String nombreArchivos, Image[] listaAnimacion) {
        for (int i = 0; i < animaciones.length; i++) {
            String ruta = String.format("file:%s/%s%d.png", carpetaSprites, nombreArchivos, i + 1);
            listaAnimacion[i] = new Image(ruta, 50, 50, true, true);
        }
    }

    public void forzarAnimacion(String url, Image[] listaAnimacion) {
        for (int i = 0; i < animaciones.length; i++) {
            String ruta = String.format("file:%s.png", url);
            listaAnimacion[i] = new Image(ruta, 70, 70, true, true);
        }
    }

    public void colision(double[] xPoligono, double[] yPoligono, Personaje personaje, int direccion, boolean dibujo) {
        double px = personaje.getX();
        double py = personaje.getY();
        double tamaño = 70;

        // Coordenadas de las 4 esquinas del personaje
        double[][] esquinas = {
                { px, py }, // esquina superior izquierda
                { px + tamaño, py }, // esquina superior derecha
                { px, py + tamaño }, // esquina inferior izquierda
                { px + tamaño, py + tamaño } // esquina inferior derecha
        };

        if (creacionParedDerecha && direccion == 2) {
            creacionParedDerecha = false;
            if (direccion == 2) { // Arriba o Abajo
                for (int i = 0; i < xPoligono.length; i++) {
                    xPoligono[i] += 30; // Mover el polígono hacia la derecha
                }
            }
        }

        if (creacionParedArriba && direccion == 1) { // 1 porque así identificaste la pared de arriba
            creacionParedArriba = false;
            for (int i = 0; i < yPoligono.length; i++) {
                yPoligono[i] -= 40;
            }
        }

        if (creacionParedIzquierda && direccion == 3) { // 1 porque así identificaste la pared de arriba
            creacionParedIzquierda = false;
            for (int i = 0; i < yPoligono.length; i++) {
                xPoligono[i] -= 8;
            }
        }

        if (creacionParedAbajo && direccion == 0) { // 1 porque así identificaste la pared de arriba
            creacionParedAbajo = false;
            for (int i = 0; i < yPoligono.length; i++) {
                yPoligono[i] += 8;
            }
        }

        if (dibujo == true) {
            gc.fillPolygon(xPoligono, yPoligono, 4);
        }

        for (double[] esquina : esquinas) {
            if (puntoDentroPoligono(xPoligono, yPoligono, esquina[0], esquina[1])) {
                switch (direccion) {
                    case 0: // Arriba
                        personaje.mover(0, -5);
                        break;
                    case 1: // Abajo
                        personaje.mover(0, 5);
                        break;
                    case 2: // Izquierda
                        personaje.mover(-5, 0);
                        break;
                    case 3: // Derecha
                        personaje.mover(5, 0);
                        break;
                }
            }
        }

    }

    public boolean hayColision(double[] xPoligono, double[] yPoligono, Personaje personaje) {
        double px = personaje.getX();
        double py = personaje.getY();
        double tamaño = 70;

        // Coordenadas de las 4 esquinas del personaje
        double[][] esquinas = {
                { px, py }, // esquina superior izquierda
                { px + tamaño, py }, // esquina superior derecha
                { px, py + tamaño }, // esquina inferior izquierda
                { px + tamaño, py + tamaño } // esquina inferior derecha
        };

        for (double[] esquina : esquinas) {
            if (puntoDentroPoligono(xPoligono, yPoligono, esquina[0], esquina[1])) {
                return true;
            }
        }

        return false;
    }

    // Algoritmo de punto-en-polígono (ray-casting)
    public boolean puntoDentroPoligono(double[] xPoligono, double[] yPoligono, double x, double y) {
        int n = xPoligono.length;
        boolean dentro = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            boolean intersecta = (yPoligono[i] > y) != (yPoligono[j] > y) &&
                    (x < (xPoligono[j] - xPoligono[i]) * (y - yPoligono[i]) / (yPoligono[j] - yPoligono[i])
                            + xPoligono[i]);

            if (intersecta) {
                dentro = !dentro;
            }
        }

        return dentro;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
