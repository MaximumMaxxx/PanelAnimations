import java.awt.*;

public class Mogus {
    public int x;
    public int y;
    public String type;
    public String text;
    public Color color;
    public boolean enabled;

    public Mogus(Color color, String type) {
        this(color);
        this.type = type;
    }

    public Mogus(Color color) {
        // Mogi have a static color
        this.color = color;
        this.enabled = true;
    }


    // ----------------------------------------------

    // Render the mogi onto Graphics provided
    public void draw(Graphics g) {
        if (!enabled) {
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
            case "DLAmogi" -> drawLDead(g);
            case "DRAmogi" -> drawRDead(g);
            default -> {
                System.out.println("Invalid Mongus choice");
                System.exit(-2049032409);
            }
        }
    }


    // ----------------------------------------------

    private void drawRDead(Graphics g) {
        g.setColor(color);
        drawBottomHalf(g);
        g.fillRect(x - 5, y + 12, 5, 12);
        g.setColor(Color.black);
        drawBone(g);
    }

    private void drawLDead(Graphics g) {
        g.setColor(color);
        drawBottomHalf(g);
        g.fillRect(x + 40, y + 12, 5, 12);
        g.setColor(Color.black);
        drawBone(g);
    }

    private void drawBone(Graphics g) {
        int leftLine = 15;
        int rightLine = 25;
        int bonePointOffset = 5;
        int middleOffset = 5;
        g.drawPolyline(new int[]{
                x + leftLine, x + leftLine, x + leftLine - bonePointOffset, x + leftLine, x + leftLine + middleOffset, x + rightLine, x + rightLine + bonePointOffset, x + rightLine, x + rightLine
        }, new int[]{
                y + 10, y + 5, y + 3, y, y + 3, y, y + 3, y + 5, y + 10
        }, 9);
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
        drawBottomHalf(g);

        g.setColor(Color.cyan);
    }

    private void drawBottomHalf(Graphics g) {
        g.fillPolygon(
                new int[]{
                        x, x, x + 5, x + 5, x + 35, x + 35, x + 40, x + 40
                },
                new int[]{
                        y + 10, y + 40, y + 40, y + 35, y + 35, y + 40, y + 40, y + 10
                },
                8
        );
    }


}
