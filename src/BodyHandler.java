import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BodyHandler {

    private ArrayList<Body> bodies = new ArrayList<>();

    public ArrayList<Body> getBodies() {
        return bodies;
    }

    public void tick() {
        for (Body body : bodies) {
            body.prepareTick();
        }
        for (Body body : bodies) {
            body.tick();
        }
    }

    public void render(Graphics graphics) {
        for (Body body : bodies) {
            body.render(graphics);
        }
    }

    public void addBody(Body body) {
        bodies.add(body);
    }

}

