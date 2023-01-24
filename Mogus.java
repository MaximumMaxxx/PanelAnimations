import java.awt.*;

public class Mogus {
    public int x;
    public int y;
    public String type;
    public String text;
    public Color color;
    public boolean enabled;


    public Mogus(Color color) {
        // Mogi have a static color
        this.color = color;
        this.enabled = true;
    }


    // ----------------------------------------------

    // Render the mogi onto Graphics provided
    public void draw(Graphics g) {
        if (!enabled) {
            System.out.println("Not printing because I'm disabled");
            return;
        }

        if (text != null) {
            g.setColor(Color.BLACK);
            // The offsets are mostly magical
            // Just looks good enough
            g.drawString(text, x + 40, y - 5);
        }
        text = null;
        switch (type) {
            case "LAmogi" -> drawLeft(g);
            case "RAmogi" -> drawRight(g);
            case "DeadAmgi" -> drawDead(g);
            default -> {
                System.out.println("AAAAAAAAAAAAAAAAAAA");
                System.exit(-2049032409);
            }
        }
    }


    // ----------------------------------------------

    private static void drawDead(Graphics g) {
    }

    private void drawLeft(Graphics g) {
        drawBase(g);
        drawEye(g, -12);

        g.setColor(color);
        g.fillRect(x + 40, y + 12, 5, 20);
    }

    private void drawRight(Graphics g) {
        drawBase(g);
        drawEye(g, 20);

        g.setColor(color);
        g.fillRect(x - 5, y + 12, 5, 20);
    }

    private void drawEye(Graphics g, int xOffset) {
        g.fillOval(x + xOffset, y + 15, 30, 10);
    }

    private void drawBase(Graphics g) {
        g.setColor(color);
        g.fillArc(x, y, 40, 35, 0, 180);
        g.fillPolygon(
                new int[]{
                        x, x, x + 5, x + 5, x + 35, x + 35, x + 40, x + 40
                },
                new int[]{
                        y + 10, y + 40, y + 40, y + 35, y + 35, y + 40, y + 40, y + 10
                },
                8
        );

        g.setColor(Color.cyan);
    }


}
