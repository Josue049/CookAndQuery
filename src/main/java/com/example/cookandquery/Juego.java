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
    private Image[] animaciones = new Image[7];

    @Override
    public void start(Stage ventana) {
        final int anchoVentana = 800;
        final int altoVentana = 500;

        // Crear ventana con fondo
        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query",
                "src/main/resources/EscenarioNivel1.png");

        // Crear personajes
        Personaje chef = new Personaje(100, 100, "src/main/resources/SpriteChefFinal/down/1.png");
        Personaje chefAnimado = new Personaje(300, 200, "src/main/resources/SpriteChefFinal/down", 7, "", 30);
        Personaje NuevoChef = new Personaje(300, 300, "src/main/resources/SpriteChefFinal/up", 7, "up", 30);

        // Obtener el contexto gráfico
        GraphicsContext gc = escenaJuego.getGraficos();
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
        animaciones = chefAbajo; // Inicializar con la animación de abajo

        gc.fillPolygon(
                new double[] { 400, 500, 520, 420 }, // X de las esquinas
                new double[] { 100, 100, 400, 400 }, // Y de las esquinas
                4);

        gc.setFill(Color.BROWN); // por ejemplo, una mesa

        double[] xPoligono = { 400, 500, 520, 420 };
        double[] yPoligono = { 100, 100, 400, 400 };


        
        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Limpiar pantalla
                gc.clearRect(0, 0, anchoVentana, altoVentana);

                // Redibujar fondo (si quieres que se vea siempre)
                escenaJuego.dibujarFondo();

                // Actualizar y dibujar personajes
                // chef.dibujar(gc);
                chefAnimado.refrescarAnimacion(now, gc, animaciones);
                NuevoChef.refrescarAnimacion(now, gc, animaciones);

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
                gc.fillPolygon(
                        new double[] { 400, 500, 520, 420 }, // X de las esquinas
                        new double[] { 100, 100, 400, 400 }, // Y de las esquinas
                        4);

                if (hayColision(xPoligono, yPoligono, chefAnimado)) {
                    System.out.println("¡Colisión detectada con el polígono!");
                    chefAnimado.mover(-5, 0);
                }
            }
        }.start();

    }

    public void cargarAnimacion(String carpetaSprites, String nombreArchivos, Image[] listaAnimacion) {
        for (int i = 0; i < animaciones.length; i++) {
            String ruta = String.format("file:%s/%s%d.png", carpetaSprites, nombreArchivos, i + 1);
            listaAnimacion[i] = new Image(ruta, 70, 70, true, true);
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
