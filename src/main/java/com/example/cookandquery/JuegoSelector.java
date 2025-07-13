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
    boolean flechaIzqActivaB = false;
    boolean flechaDerActivaB = false;
    int momentoIzq = -100;
    int momentoDer = -100;
    private boolean Start = true;
    int indexDisfraz = 0;
    private String disfraz;

    @Override
    public void start(Stage ventana) {

        // Crear ventana con fondo
        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query: Selector");
        Personaje MantelFondo = new Personaje(0, 0, anchoVentana + aumento, altoVentana + aumento, "src/main/resources/FondosSeleccion", 8, "fondo", 150);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        Personaje fondoGris = new Personaje(280, 86, "src/main/resources/FondosSeleccion/fondoGris.png", 370, 365);

        Personaje key1 = new Personaje(295, 105, "src/main/resources/key1.png", 47, 242);


        // Personaje misterio = new Personaje(483, 189, "src/main/resources/Giro/misterio.png", 245, 209);


        Personaje flechader = new Personaje(558, 322, "src/main/resources/FondosSeleccion/flechader.png", 36, 25);
        Personaje flechaizq = new Personaje(240, 322, "src/main/resources/FondosSeleccion/flechaizq.png", 36, 25);
        Personaje normalGiro = new Personaje(309, 181, 209, 263, "src/main/resources/Giro", 8, "normal", 120);


        // Obtener el contexto gráfico
        GraphicsContext gc = escenaJuego.getGraficos();
        Set<KeyCode> teclasActivas = new HashSet<>();

        escenaJuego.getEscena().setOnKeyPressed(e -> teclasActivas.add(e.getCode()));
        escenaJuego.getEscena().setOnKeyReleased(e -> teclasActivas.remove(e.getCode()));

        // Cargar animaciones
        Image[] MantelMov = new Image[8];
        Image[] normalGiroImages = new Image[8];
        Image[] fastGiroImages = new Image[8];
        Image[] timeGiroImages = new Image[8];
        Image[] fireGiroImages = new Image[8];
        Image[] profeGiroImages = new Image[8];
        cargarAnimacion("src/main/resources/FondosSeleccion", "fondo", MantelMov, anchoVentana + aumento, altoVentana + aumento);
        cargarAnimacion("src/main/resources/Giro", "normal", normalGiroImages, 209, 263);
        cargarAnimacion("src/main/resources/Giro", "fast", fastGiroImages, 209, 263);
        cargarAnimacion("src/main/resources/Giro", "fire", fireGiroImages, 209, 263);
        cargarAnimacion("src/main/resources/Giro", "time", timeGiroImages, 209, 263);
        cargarAnimacion("src/main/resources/Giro", "profe", profeGiroImages, 209, 263);

        Image[][] disfraces = new Image[5][]; // Lista de 4 listas
        disfraces[0] = normalGiroImages;
        disfraces[1] = timeGiroImages;
        disfraces[2] = fireGiroImages;
        disfraces[3] = fastGiroImages;
        disfraces[4] = profeGiroImages;

        animacionesP1 = disfraces[0]; // Asignar la animación de normalGiro a animacionesP1

        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, anchoVentana, altoVentana);

                MantelFondo.refrescarAnimacion(now, gc, MantelMov);
                fondoGris.dibujar(gc);
                normalGiro.refrescarAnimacion(now, gc, animacionesP1);

                // LÓGICA FLECHA IZQUIERDA
                if ((teclasActivas.contains(KeyCode.A) && !flechaIzqActiva) || teclasActivas.contains(KeyCode.LEFT) && !flechaIzqActiva) {
                    MediaPlayer efecto = new MediaPlayer(mediaEffect);
                    efecto.play();

                    flechaizq.setSprite("src/main/resources/FondosSeleccion/flechaizqGrande.png", 48, 70, 217, 305);
                    momentoIzq = i;
                    flechaIzqActiva = true;

                    if (animacionesP1 == disfraces[0]) {
                        indexDisfraz=disfraces.length - 1;
                        animacionesP1 = disfraces[indexDisfraz]; 
                    } else {
                        indexDisfraz--;
                        animacionesP1 = disfraces[indexDisfraz];
                    }
                }
                if (flechaIzqActiva && i - momentoIzq >= 10) {
                    flechaizq.setSprite("src/main/resources/FondosSeleccion/flechaizq.png", 36, 25, 240, 322);
                    flechaIzqActiva = false;
                }

                // LÓGICA FLECHA DERECHA
                if ((teclasActivas.contains(KeyCode.D) && !flechaDerActiva) || teclasActivas.contains(KeyCode.RIGHT) && !flechaDerActiva) {
                    MediaPlayer efecto = new MediaPlayer(mediaEffect);
                    efecto.play();

                    flechader.setSprite("src/main/resources/FondosSeleccion/flechaderGrande.png", 48, 70, 558, 305);
                    momentoDer = i;
                    flechaDerActiva = true;
                    if (animacionesP1 != disfraces[disfraces.length - 1]) {
                        indexDisfraz++;
                        animacionesP1 = disfraces[indexDisfraz]; // Cambiar a animación de fuego
                    } else {
                        indexDisfraz = 0;
                        animacionesP1 = disfraces[indexDisfraz]; // Cambiar a animación normal
                    }
                }
                if (flechaDerActiva && i - momentoDer >= 10) {
                    flechader.setSprite("src/main/resources/FondosSeleccion/flechader.png", 36, 25, 558, 322);
                    flechaDerActiva = false;
                }

                if ((teclasActivas.contains(KeyCode.SPACE) )) {
                    if (Start) {
                        try {
                            if (indexDisfraz == 0) {
                                disfraz = "normal";
                            } else if (indexDisfraz == 1) {
                                disfraz = "time";
                            } else if (indexDisfraz == 2) {
                                disfraz = "fire";
                            } else if (indexDisfraz == 3) {
                                disfraz = "fast";
                            } else if (indexDisfraz == 4) {
                                disfraz = "profe";
                            }
                            Start=false;
                            mediaPlayer.stop();
                            JuegoTablas juego = new JuegoTablas();
                            juego.setDatos(disfraz);
                            juego.start(new Stage());
                            ventana.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                // DIBUJO FINAL
                flechader.dibujar(gc);
                flechaizq.dibujar(gc);
                // misterio.dibujar(gc);

                key1.dibujar(gc);


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
