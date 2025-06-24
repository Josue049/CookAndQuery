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

public class JuegoSelector extends Application {
    private Image[] animaciones = new Image[4];
    final int anchoVentana = 800;
    final int altoVentana = 500;

    @Override
    public void start(Stage ventana) {

        // Crear ventana con fondo
        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query: Selector");
        Personaje MantelFondo = new Personaje(0, 0, anchoVentana, altoVentana, "src/main/resources/FondosSeleccion", 4, "fondo", 100);


        // Obtener el contexto gráfico
        GraphicsContext gc = escenaJuego.getGraficos();


        // Cargar animaciones
        Image[] MantelMov = new Image[4];
        cargarAnimacion("src/main/resources/FondosSeleccion", "fondo", MantelMov);
        animaciones = MantelMov; // Inicializar con la animación de abajo

        
        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Limpiar pantalla
                gc.clearRect(0, 0, anchoVentana, altoVentana);

                // Redibujar fondo para que se vea siempre
                
                

                // Actualizar y dibujar personajes
                // chef.dibujar(gc);
                MantelFondo.refrescarAnimacion(now, gc, animaciones);


            }
        }.start();

    }

    public void cargarAnimacion(String carpetaSprites, String nombreArchivos, Image[] listaAnimacion) {
        for (int i = 0; i < animaciones.length; i++) {
            String ruta = String.format("file:%s/%s%d.png", carpetaSprites, nombreArchivos, i + 1);
            listaAnimacion[i] = new Image(ruta, anchoVentana, altoVentana, true, true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
