import java.awt.*;
import java.util.ArrayList;

public class Body implements Comparable<Body> {

    private BodyHandler bodyHandler;

    private final double diameter;
    private final double mass;

    private double velX;
    private double tempVelX;
    private double x;


    public Body(BodyHandler bodyHandler, int mass, double velX, double x) {
        this.bodyHandler = bodyHandler;

        this.mass = mass;
        this.diameter = mass;

        this.velX = velX;
        tempVelX = velX;
        this.x = x;
    }

    public void prepareTick() {
        ArrayList<Body> collidingBodies = getCollidingBodies();
        if (!collidingBodies.isEmpty()) {
            setVelAfterColliding(collidingBodies);
        }
//        collideBounds();
    }

    public void tick() {
        velX = tempVelX;
        x += velX;
    }

    public void render(Graphics graphics) {
        graphics.setColor(new Color(0, 0, 20));
        graphics.fillRect((int) (x - diameter / 2d), Main.REAL_HEIGHT - 300 - (int) diameter, (int) diameter, (int) diameter);
    }


    public void collideBounds() {
        if (x <= 10 + diameter / 2d || Main.REAL_WIDTH - 10 - diameter / 2d <= x) {
            tempVelX += velX * -1;
        }
    }

    public ArrayList<Body> getCollidingBodies() {
        ArrayList<Body> toCheck = new ArrayList<>(bodyHandler.getBodies());
        return getCollidingBodies(toCheck);
    }

    public ArrayList<Body> getCollidingBodies(ArrayList<Body> toCheck) {
        toCheck.remove(this);

        ArrayList<Body> collidingBodies = new ArrayList<>();
        for (Body body : toCheck) {
            double distance = Math.abs(x - body.getX());
            double min = Math.abs((diameter + body.getDiameter()) / 2d);
            if (distance <= min) {
                collidingBodies.add(body);
                ArrayList<Body> toCheckNext = new ArrayList<>(toCheck);
                collidingBodies.addAll(body.getCollidingBodies(toCheckNext));
            }
        }
        return collidingBodies;
    }

    public void setVelAfterColliding(ArrayList<Body> bodies) {
        double collidingMass = 0;
        double collidingMomentum = 0;
        for (Body body : bodies) {
            collidingMass += body.getMass();
            collidingMomentum += body.getMomentum();
        }
        this.tempVelX = (this.velX * (this.mass - collidingMass) + 2 * collidingMomentum) / (this.mass + collidingMass);
    }

    public double getDiameter() {
        return diameter;
    }

    public double getMass() {
        return mass;
    }

    public double getVelX() {
        return velX;
    }

    public double getX() {
        return x;
    }

    public double getMomentum() {
        return velX * mass;
    }

    @Override
    public int compareTo(Body body) {
        return (int) -(getMomentum() - body.getMomentum());
    }
}
