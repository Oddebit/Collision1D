import java.awt.*;
import java.awt.image.BufferStrategy;
import java.time.Instant;
import java.util.Random;

public class Main extends Canvas implements Runnable {
    private static final long serialVersionUID = 1550691097823471818L;

    private BodyHandler bodyHandler;

    private Thread thread;
    private boolean running = false;

    public static final int WIDTH = 1600, HEIGHT = 800;
    public static final int REAL_WIDTH = WIDTH - 16, REAL_HEIGHT = HEIGHT - 39;
    public static final int WIDTH_CENTER = REAL_WIDTH / 2, HEIGHT_CENTER = REAL_HEIGHT / 2;

    public Main() {

        bodyHandler = new BodyHandler();

        new Window(WIDTH, HEIGHT, "Collision 1D", this);

        generateBodies(10, 100);


    }

    public void generateBodies(int amount, int mass) {

        bodyHandler.addBody(new Body(bodyHandler, Integer.MAX_VALUE, 0, -Integer.MAX_VALUE / 2 + 5));
        bodyHandler.addBody(new Body(bodyHandler, Integer.MAX_VALUE, 0, REAL_WIDTH + Integer.MAX_VALUE / 2 - 5));

        int margin = (REAL_WIDTH - amount * mass) / 2;
        bodyHandler.addBody(new Body(bodyHandler, mass, 20, 100));
        for (int i = 1; i < amount; i++) {
            bodyHandler.addBody(new Body(bodyHandler, new Random().nextInt(mass / 2) + mass / 2, 0, margin + i * (mass + 10)));
        }
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double tickTime = 1_000_000_000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / tickTime;
            lastTime = now;

            while (delta >= 1) {
                tick();
                delta--;
            }

            if (running) {
                render();
            }
            frames++;

            // Affiche les FPS chaque seconde
            if (System.currentTimeMillis() - timer > 1_000) {
                timer += 1_000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        bodyHandler.tick();
    }

    private void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(new Color(0, 0, 20));
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        graphics.setColor(new Color(255, 170, 0));
        graphics.fillRect(0, 0, WIDTH, Main.REAL_HEIGHT - 300);

        bodyHandler.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    public static void main(String[] args) {
        new Main();
    }
}
