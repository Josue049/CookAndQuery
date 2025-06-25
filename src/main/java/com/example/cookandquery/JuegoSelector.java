package com.example.cookandquery;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JuegoSelector extends Application {
    private Image[] animaciones = new Image[8];
    private Image[] animacionesP1 = new Image[8];
    private Image[] animacionesP2 = new Image[8];
    final int anchoVentana = 800;
    final int altoVentana = 500;
    final int aumento = 35;
    String path = getClass().getResource("/CriticalTheme.wav").toExternalForm();
    String pathNext = getClass().getResource("/next.mp3").toExternalForm();
    Media media = new Media(path);
    Media mediaEffect = new Media(pathNext);
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    MediaPlayer EffectNext = new MediaPlayer(mediaEffect);
    int i = 0;
    boolean flechaIzqActiva = false;
    boolean flechaDerActiva = false;
    int momentoIzq = -100;
    int momentoDer = -100;
    private boolean Start = true;

    @Override
    public void start(Stage ventana) {

        // Crear ventana con fondo
        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query: Selector");
        Personaje MantelFondo = new Personaje(0, 0, anchoVentana + aumento, altoVentana + aumento,
                "src/main/resources/FondosSeleccion", 8, "fondo", 150);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        Personaje fondoGris = new Personaje(80, 86, "src/main/resources/FondosSeleccion/fondoGris.png", 267, 365);
        Personaje fondoGrisP2 = new Personaje(456, 86, "src/main/resources/FondosSeleccion/fondoGris.png", 267, 365);
        Personaje incognita = new Personaje(483, 189, "src/main/resources/Giro/incognita.png", 208, 256);
        Personaje p1 = new Personaje(189, 120, "src/main/resources/FondosSeleccion/p1.png", 43, 32);
        Personaje p2 = new Personaje(563, 120, "src/main/resources/FondosSeleccion/p2.png", 43, 32);
        Personaje flechader = new Personaje(358, 322, "src/main/resources/FondosSeleccion/flechader.png", 36, 25);
        Personaje flechaizq = new Personaje(50, 322, "src/main/resources/FondosSeleccion/flechaizq.png", 36, 25);
        Personaje normalGiro = new Personaje(109, 181, 209, 263, "src/main/resources/Giro", 8, "normal", 120);

        // Obtener el contexto gráfico
        GraphicsContext gc = escenaJuego.getGraficos();
        Set<KeyCode> teclasActivas = new HashSet<>();

        escenaJuego.getEscena().setOnKeyPressed(e -> teclasActivas.add(e.getCode()));
        escenaJuego.getEscena().setOnKeyReleased(e -> teclasActivas.remove(e.getCode()));

        // Cargar animaciones
        Image[] MantelMov = new Image[8];
        Image[] normalGiroImages = new Image[8];
        Image[] fastGiroImages = new Image[8];
        cargarAnimacion("src/main/resources/FondosSeleccion", "fondo", MantelMov, anchoVentana + aumento,
                altoVentana + aumento);
        cargarAnimacion("src/main/resources/Giro", "normal", normalGiroImages, 209, 263);
        cargarAnimacion("src/main/resources/Giro", "fast", fastGiroImages, 209, 263);

        animacionesP1 = normalGiroImages; // Asignar la animación de normalGiro a animacionesP1

        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, anchoVentana, altoVentana);

                MantelFondo.refrescarAnimacion(now, gc, MantelMov);
                fondoGris.dibujar(gc);
                fondoGrisP2.dibujar(gc);
                p1.dibujar(gc);
                p2.dibujar(gc);
                normalGiro.refrescarAnimacion(now, gc, animacionesP1);

                // LÓGICA FLECHA IZQUIERDA
                if ((teclasActivas.contains(KeyCode.LEFT) || teclasActivas.contains(KeyCode.A)) && !flechaIzqActiva) {
                    MediaPlayer efecto = new MediaPlayer(mediaEffect);
                    efecto.play();

                    flechaizq.setSprite("src/main/resources/FondosSeleccion/flechaizqGrande.png", 48, 70, 27, 305);
                    momentoIzq = i;
                    flechaIzqActiva = true;
                    if (animacionesP1 == normalGiroImages) {
                        animacionesP1 = fastGiroImages; // Cambiar a animación normal
                    } else {
                        animacionesP1 = normalGiroImages; // Cambiar a animación rápida
                    }
                }
                if (flechaIzqActiva && i - momentoIzq >= 10) {
                    flechaizq.setSprite("src/main/resources/FondosSeleccion/flechaizq.png", 36, 25, 50, 322);
                    flechaIzqActiva = false;
                }

                // LÓGICA FLECHA DERECHA
                if ((teclasActivas.contains(KeyCode.RIGHT) || teclasActivas.contains(KeyCode.D)) && !flechaDerActiva) {
                    MediaPlayer efecto = new MediaPlayer(mediaEffect);
                    efecto.play();

                    flechader.setSprite("src/main/resources/FondosSeleccion/flechaderGrande.png", 48, 70, 358, 305);
                    momentoDer = i;
                    flechaDerActiva = true;
                    if (animacionesP1 == normalGiroImages) {
                        animacionesP1 = fastGiroImages; // Cambiar a animación normal
                    } else {
                        animacionesP1 = normalGiroImages; // Cambiar a animación rápida
                    }
                }
                if (flechaDerActiva && i - momentoDer >= 10) {
                    flechader.setSprite("src/main/resources/FondosSeleccion/flechader.png", 36, 25, 358, 322);
                    flechaDerActiva = false;
                }

                if ((teclasActivas.contains(KeyCode.ENTER) )) {
                    if (Start) {
                        try {
                            Start=false;
                            mediaPlayer.stop();
                            new JuegoTablas().start(new Stage());
                            ventana.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                if ((teclasActivas.contains(KeyCode.ENTER) )) {
                    if (Start) {
                        try {
                            Start=false;
                            mediaPlayer.stop();
                            new JuegoTablas().start(new Stage());
                            ventana.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                // DIBUJO FINAL
                flechader.dibujar(gc);
                flechaizq.dibujar(gc);
                incognita.dibujar(gc);

                i++;
            }
        }.start();

    }

    public void cargarAnimacion(String carpetaSprites, String nombreArchivos, Image[] listaAnimacion, int ancho,
            int alto) {
        for (int i = 0; i < animaciones.length; i++) {
            String ruta = String.format("file:%s/%s%d.png", carpetaSprites, nombreArchivos, i + 1);
            listaAnimacion[i] = new Image(ruta, ancho, alto, true, true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
