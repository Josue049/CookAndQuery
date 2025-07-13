package com.example.cookandquery;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.scene.text.Font;
import javax.swing.JOptionPane;

public class Juego extends Application {
    GraphicsContext gc;
    private Image[] animaciones = new Image[4];
    boolean creacionParedDerecha = true;
    boolean creacionParedArriba = true;
    boolean creacionParedIzquierda = true;
    boolean creacionParedAbajo = true;
    boolean cuchilloSonido = true;
    boolean motorSonido = true;
    Personaje selectorA = new Personaje(8, 247, "", 70, 84);
    // String mochilaA = "";
    Personaje mesaA = new Personaje(208, 247, "", 70, 84);
    boolean mesaActivado = false;
    private String[] pedidos = new String[3];
    boolean creacionPedidos = true;
    private String disfraz="normal";

    boolean presion = false;
    int numeroCaja = 0;

    private String[] opciones = { "", "", "", "" };
    int fin = 0;

    long tiempoUltimoPedido = 0;
    long intervaloSiguientePedido = 0; // En nanosegundos
    Random random = new Random();

    Personaje opcion1;
    Personaje opcion2;
    Personaje opcion3;
    Personaje opcion4;

    String path = getClass().getResource("/Cocina.wav").toExternalForm();
    Media media = new Media(path);
    MediaPlayer mediaPlayer = new MediaPlayer(media);

    String pathcaja = getClass().getResource("/caja.wav").toExternalForm();
    Media mediaEffectCaja = new Media(pathcaja);
    MediaPlayer EffectCaja = new MediaPlayer(mediaEffectCaja);

    String pathCuchillo = getClass().getResource("/cuchillo.wav").toExternalForm();
    Media mediaEffectCuchillo = new Media(pathCuchillo);
    MediaPlayer EffectCuchillo = new MediaPlayer(mediaEffectCuchillo);

    String pathMotor = getClass().getResource("/motor.wav").toExternalForm();
    Media mediaEffectMotor = new Media(pathMotor);
    MediaPlayer EffectMotor = new MediaPlayer(mediaEffectMotor);

    String tiempo;
    int tiempoEntero = 80;
    long ultimoSegundo = 0;
    int contadorFrames = 0;

    public void setDatos(String disfraz) {
        this.disfraz = disfraz;
    }

    @Override
    public void start(Stage ventana) {
        System.out.println("Disfraz seleccionado: " + disfraz);
        tiempo = String.format("%02d", tiempoEntero);

        final int anchoVentana = 600;
        final int altoVentana = 700;

        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query");

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        // Crear personajes
        Personaje FondoCocina = new Personaje(0, 0, "src/main/resources/fondoo.png", altoVentana, anchoVentana);
        Personaje chefAnimado = new Personaje(300, 200, 70, 70, "src/main/resources/SpriteChefFinal/down", 1, "", 80);

        // Personaje selectA = new Personaje(34, 360, "src/main/resources/selectA.png",
        // 260, 212);

        // Personaje NuevoChef = new Personaje(0, 0, "src/main/resources/nuevoChef.png",
        // 70, 70);
        // Personaje menu1 = new Personaje(0, 0, "src/main/resources/n1.png", 118, 63);

        // Obtener el contexto gráfico
        gc = escenaJuego.getGraficos();
        Menu menu = new Menu(gc);
        Set<KeyCode> teclasActivas = new HashSet<>();

        escenaJuego.getEscena().setOnKeyPressed(e -> teclasActivas.add(e.getCode()));
        escenaJuego.getEscena().setOnKeyReleased(e -> teclasActivas.remove(e.getCode()));

        // Cargar animaciones
        Image[] chefArriba = new Image[4];
        Image[] chefAbajo = new Image[4];
        Image[] chefIzquierda = new Image[4];
        Image[] chefDerecha = new Image[4];

        Image[] chefArribaQuieto = new Image[4];
        Image[] chefAbajoQuieto = new Image[4];
        Image[] chefIzquierdaQuieto = new Image[4];
        Image[] chefDerechaQuieto = new Image[4];

        cargarAnimacion("src/main/resources/Movimientos/"+disfraz+"/arriba", "", chefArriba);
        cargarAnimacion("src/main/resources/Movimientos/"+disfraz+"/abajo", "", chefAbajo);
        cargarAnimacion("src/main/resources/Movimientos/"+disfraz+"/izquierda", "", chefIzquierda);
        cargarAnimacion("src/main/resources/Movimientos/"+disfraz+"/derecha", "", chefDerecha);

        forzarAnimacion("src/main/resources/Movimientos/"+disfraz+"/arriba/2", chefArribaQuieto);
        forzarAnimacion("src/main/resources/Movimientos/"+disfraz+"/abajo/2", chefAbajoQuieto);
        forzarAnimacion("src/main/resources/Movimientos/"+disfraz+"/izquierda/2", chefIzquierdaQuieto);
        forzarAnimacion("src/main/resources/Movimientos/"+disfraz+"/derecha/2", chefDerechaQuieto);

        animaciones = chefAbajo; // Inicializar con la animación de abajo

        gc.setFill(Color.BROWN); // por ejemplo, una mesa

        double[] xParedDerecha = { 450, 484, 510, 475 };
        double[] yParedDerecha = { 80, 75, 300, 300 };

        double[] xParedIzquierda = { 148, 114, 79, 118 };
        double[] yParedIzquierda = { 67, 67, 310, 310 };

        double[] xParedArriba = { 130, 465, 476, 130 };
        double[] yParedArriba = { 74, 74, 92, 92 };

        double[] xParedAbajo = { 98, 488, 488, 95 };
        double[] yParedAbajo = { 296, 295, 326, 325 };

        for (int i = 0; i < yParedAbajo.length; i++) {
            yParedAbajo[i] += 15; // Mover el polígono hacia la derecha
        }

        for (int i = 0; i < xParedIzquierda.length; i++) {
            xParedIzquierda[i] += 5; // Mover el polígono hacia la derecha
        }

        for (int i = 0; i < xParedDerecha.length; i++) {
            xParedDerecha[i] += 8; // Mover el polígono hacia la derecha
        }

        for (int i = 0; i < yParedArriba.length; i++) {
            yParedArriba[i] += 12; // Mover el polígono hacia la derecha
        }

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

                if (animaciones == chefArriba && !teclasActivas.contains(KeyCode.UP)
                        && !teclasActivas.contains(KeyCode.W)) {
                    animaciones = chefArribaQuieto;
                }
                if (animaciones == chefAbajo && !teclasActivas.contains(KeyCode.DOWN)
                        && !teclasActivas.contains(KeyCode.S)) {
                    animaciones = chefAbajoQuieto;
                }
                if (animaciones == chefIzquierda && !teclasActivas.contains(KeyCode.LEFT)
                        && !teclasActivas.contains(KeyCode.A)) {
                    animaciones = chefIzquierdaQuieto;
                }
                if (animaciones == chefDerecha && !teclasActivas.contains(KeyCode.RIGHT)
                        && !teclasActivas.contains(KeyCode.D)) {
                    animaciones = chefDerechaQuieto;
                }

                colision(xParedDerecha, yParedDerecha, chefAnimado, 2, false);
                colision(xParedIzquierda, yParedIzquierda, chefAnimado, 3, false);
                colision(xParedArriba, yParedArriba, chefAnimado, 1, false);
                colision(xParedAbajo, yParedAbajo, chefAnimado, 0, false);

                if (menu.getCantidadPedidos() < 5 && creacionPedidos) {
                    if (now - tiempoUltimoPedido > intervaloSiguientePedido) {
                        menu.generarPedido();
                        tiempoUltimoPedido = now;
                        intervaloSiguientePedido = (1 + random.nextInt(5)) * 1_000_000_000L; // Entre 1 y 5 segundos
                        // System.out.println("Nuevo pedido generado");
                    }
                    if (menu.getCantidadPedidos() == 3) {
                        creacionPedidos = false; // Evita generar más pedidos hasta que se elimine uno
                    }
                }

                menu.mostrarPedidos();
                // System.out.println("Pedidos: " + menu.getCantidadPedidos());
                selectorA.dibujar(gc);

                // selectA.dibujar(gc);
                mesaA.dibujar(gc);

                // System.out.println("Posición del Chef: (" + chefAnimado.getX() + ", " +
                // chefAnimado.getY() + ")");

                if (Math.abs(chefAnimado.getX() - 130) < 10 && Math.abs(chefAnimado.getY() - 200) < 10) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/pan.png", 70, 84);
                    int ncaja = 1;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (opciones[fin - 1] != "pan") {
                                opciones[fin] = "pan";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "pan";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 135) < 10 && Math.abs(chefAnimado.getY() - 175) < 10) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/wantan.png", 70, 84);
                    int ncaja = 2;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (opciones[fin - 1] != "wantan") {
                                opciones[fin] = "wantan";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "wantan";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 135) < 10 && Math.abs(chefAnimado.getY() - 150) < 10) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/masaPizza.png", 70, 84);
                    int ncaja = 3;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (opciones[fin - 1] != "masaPizza") {
                                opciones[fin] = "masaPizza";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "masaPizza";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 145) < 10 && Math.abs(chefAnimado.getY() - 105) < 10) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/peperoni.png", 70, 84);
                    int ncaja = 4;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (opciones[fin - 1] != "peperoni") {
                                opciones[fin] = "peperoni";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "peperoni";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 130) < 10 && Math.abs(chefAnimado.getY() - 220) < 10) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/quesoTomate.png", 70, 84);
                    int ncaja = 5;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (opciones[fin - 1] != "quesoTomate") {
                                opciones[fin] = "quesoTomate";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "quesoTomate";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 145) < 10 && Math.abs(chefAnimado.getY() - 85) < 10) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/salchicha.png", 70, 84);
                    int ncaja = 6;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (opciones[fin - 1] != "salchicha") {
                                opciones[fin] = "salchicha";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "salchicha";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 140) < 10 && Math.abs(chefAnimado.getY() - 125) < 10) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/carne.png", 70, 84);
                    int ncaja = 7;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (opciones[fin - 1] != "carne") {
                                opciones[fin] = "carne";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "carne";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 435) < 10 && Math.abs(chefAnimado.getY() - 230) < 20) {
                    selectorA = new Personaje(8, 247, "src/main/resources/ingredientes/tabla_comida.png", 70, 84);
                    int ncaja = 8;
                    if (numeroCaja != ncaja) {
                        MediaPlayer caja = new MediaPlayer(mediaEffectCaja);
                        caja.play();
                        numeroCaja = ncaja;
                    }
                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length && mesaActivado) {
                        if (fin > 0) {
                            if (!opciones[fin - 1].equals("comida")) {
                                opciones[fin] = "comida";
                                fin++;
                            }
                        } else {
                            opciones[fin] = "comida";
                            fin++;
                        }
                    }
                } else if (Math.abs(chefAnimado.getX() - 360) < 50 && Math.abs(chefAnimado.getY() - 70) < 10) {
                    // System.out.println("Listo para entregar");

                    if (motorSonido) {
                        MediaPlayer motor = new MediaPlayer(mediaEffectMotor);
                        motor.play();
                        motorSonido = false;
                    }

                    // for (int i = 0; i < opciones.length; i++) {
                    //     System.out.println("Opción " + (i + 1) + ": " + opciones[i]);
                    // }

                    // pedidos = menu.getPedidos();
                    // for (int i = 0; i < pedidos.length; i++) {
                    //     System.out.println("Pedidos " + (i + 1) + ": " + pedidos[i]);
                    // }

                    if (teclasActivas.contains(KeyCode.SPACE) && fin != opciones.length + 1 && mesaActivado) {
                        pedidos = menu.getPedidos();
                        if (opciones[0].equals("comida")) {
                            if (pedidos[0].equals("hamburguesa")) {
                                if ((opciones[1].equals("pan") && opciones[2].equals("carne"))
                                        || (opciones[2].equals("pan") && opciones[1].equals("carne"))) {
                                    
                                    JOptionPane.showMessageDialog(null, "Pedido de hamburguesa completado");
                                    opciones[0] = "";
                                    opciones[1] = "";
                                    opciones[2] = "";
                                    opciones[3] = "";
                                    fin = 0;
                                    mesaActivado = false;
                                    menu.EliminarPedido();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error en tu SQL, para hamburguesa");
                                }
                            } else if (pedidos[0].equals("tequeños")) {
                                if ((opciones[1].equals("wantan") && opciones[2].equals("salchicha"))
                                        || (opciones[2].equals("salchicha") && opciones[1].equals("wantan"))) {
                                    JOptionPane.showMessageDialog(null, "Pedido de tequeños completado");
                                    opciones[0] = "";
                                    opciones[1] = "";
                                    opciones[2] = "";
                                    opciones[3] = "";
                                    fin = 0;
                                    mesaActivado = false;
                                    menu.EliminarPedido();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error en tu SQL, para tequeños");
                                }
                            } else if (pedidos[0].equals("pizza")) {
                                System.out.println("Pedido de pizza");
                                if ((opciones[1].equals("masaPizza") && opciones[2].equals("quesoTomate")
                                        && opciones[3].equals("peperoni")) ||
                                        (opciones[1].equals("masaPizza")
                                                && opciones[2].equals("peperoni") && opciones[3].equals("quesoTomate"))
                                        ||
                                        (opciones[1].equals("quesoTomate") && opciones[2].equals("masaPizza")
                                                && opciones[3].equals("peperoni"))
                                        ||
                                        (opciones[1].equals("quesoTomate") && opciones[2].equals("peperoni")
                                                && opciones[3].equals("masaPizza"))
                                        ||
                                        (opciones[1].equals("peperoni") && opciones[2].equals("masaPizza")
                                                && opciones[3].equals("quesoTomate"))
                                        ||
                                        (opciones[1].equals("peperoni") && opciones[2].equals("quesoTomate")
                                                && opciones[3].equals("masaPizza"))) {
                                    JOptionPane.showMessageDialog(null, "Pedido de pizza completado");
                                    opciones[0] = "";
                                    opciones[1] = "";
                                    opciones[2] = "";
                                    opciones[3] = "";
                                    fin = 0;
                                    mesaActivado = false;
                                    menu.EliminarPedido();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error en tu SQL, para pizza");
                                }
                            }
                        }
                    }

                } else {
                    selectorA = new Personaje(8, 247, "", 70, 84);
                    numeroCaja = 0;
                    motorSonido = true;
                }
                if ((Math.abs(chefAnimado.getX() - 210) < 20 && Math.abs(chefAnimado.getY() - 240) < 10)
                        || (Math.abs(chefAnimado.getX() - 345) < 20 && Math.abs(chefAnimado.getY() - 240) < 10)) {
                    mesaA = new Personaje(150, 360, "src/main/resources/distinct.png", 167, 292);
                    if (cuchilloSonido) {
                        MediaPlayer cuchillo = new MediaPlayer(mediaEffectCuchillo);
                        cuchillo.play();
                        cuchilloSonido = false;
                    }
                    // System.out.println("Seleccionado:");
                    if (teclasActivas.contains(KeyCode.SPACE)) {
                        if (!mesaActivado) {
                            mesaActivado = true;
                        }
                        // System.out.println("Mesa activada: " + mesaActivado);
                    }

                } else if (!mesaActivado) {
                    mesaA = new Personaje(150, 360, "", 167, 292);
                    cuchilloSonido = true; // Resetea el sonido al salir de la mesa
                }

                if (!teclasActivas.contains(KeyCode.Q)) {
                    presion = false;
                }

                if (teclasActivas.contains(KeyCode.Q)) {
                    if (!presion) {
                        if (fin > 0) {
                            fin--;
                            opciones[fin] = "";
                            // System.out.println("Ingrediente eliminado. Fin ahora es: " + fin);

                        } else {
                            if (mesaActivado) {
                                mesaActivado = false;
                                // System.out.println("Mesa desactivada: " + mesaActivado);
                            }
                        }
                        presion = true; // Evita múltiples borrados por mantener presionado
                    }
                } else {
                    presion = false; // Se resetea al soltar la tecla
                }

                opcion1 = new Personaje(215, 445, "src/main/resources/completar/" + opciones[0] + ".png", 41,
                        174);
                opcion2 = new Personaje(215, 535, "src/main/resources/completar/" + opciones[1] + ".png", 41,
                        174);
                opcion3 = new Personaje(215, 580, "src/main/resources/completar/" + opciones[2] + ".png", 41,
                        174);
                opcion4 = new Personaje(215, 625, "src/main/resources/completar/" + opciones[3] + ".png", 41,
                        174);
                
                opcion1.dibujar(gc);
                opcion2.dibujar(gc);
                opcion3.dibujar(gc);
                opcion4.dibujar(gc);

                Font fuentePersonalizada = Font.loadFont(
                        getClass().getResourceAsStream("/fonts/enter-the-gungeon-small.ttf"), 30);

                    // Simula borde blanco (dibujando alrededor)
                gc.setFill(Color.WHITE);
                gc.fillText(tiempo, 530 - 1, 40); // izquierda
                gc.fillText(tiempo, 530 + 1, 40); // derecha
                gc.fillText(tiempo, 530, 40 - 1); // arriba
                gc.fillText(tiempo, 530, 40 + 1); // abajo
                gc.setFill(Color.BLACK);
                gc.setFont(fuentePersonalizada); // usar fuente personalizada cargada
                gc.fillText(tiempo, 530, 40);

                // posicion del chef
                System.out.println("Posición del selector: (" + chefAnimado.getX() + ", " + chefAnimado.getY() + ")");

                if (ultimoSegundo == 0) {
                    ultimoSegundo = now;
                }

                if (now - ultimoSegundo >= 1_000_000_000L) { // 1 segundo en nanosegundos
                    tiempoEntero--;
                    ultimoSegundo = now;

                    if (tiempoEntero <= 0) {
                        tiempoEntero = 0;
                         JOptionPane.showMessageDialog(null, "¡Tiempo agotado!\nFin del juego.");
                    }

                    tiempo = String.format("%02d", tiempoEntero); // Para siempre tener dos dígitos (ej: 09)
                }

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
            listaAnimacion[i] = new Image(ruta, 50, 50, true, true);
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

        if (creacionParedIzquierda && direccion == 3) {
            creacionParedIzquierda = false;
            for (int i = 0; i < yPoligono.length; i++) {
                xPoligono[i] -= 8;
            }
        }

        if (creacionParedAbajo && direccion == 0) {
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
