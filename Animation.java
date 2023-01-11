import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

class Animation {

    static final int PANEL_HEIGHT = 500;
    static final int PANEL_WIDTH = 500;
    static final int FPS = 5;

    public static void main(String[] args) {
        DrawingPanel panel = new DrawingPanel(PANEL_WIDTH, PANEL_HEIGHT);
        Graphics gPanel = panel.getGraphics();

        BufferedImage offscreen = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = offscreen.getGraphics();


        panel.sleep(200);

        AnimParser parser = new AnimParser("animations/1.anim");
        renderParser(panel, gPanel, offscreen, g, parser);
        parser = new AnimParser("animations/2.anim");
        renderParser(panel, gPanel, offscreen, g, parser);


        // I think for timing you'll need stacks and a hashmap
        // Good luck man


    }

    private static void renderParser(DrawingPanel panel, Graphics gPanel, BufferedImage offscreen, Graphics g, AnimParser parser) {
        for (AnimFrame frame : parser.frames) {
            panel.sleep(1000 / FPS);
            clearRect(g, Color.WHITE, panel.getWidth(), panel.getHeight());
            processFrame(g, frame, frame.type, Color.red);
            gPanel.drawImage(offscreen, 0, 0, panel);
            panel.sleep(frame.delay);
        }
    }


    static void processFrame(Graphics g, AnimFrame frame, String type, Color color)  {
        if (frame.text != null) {
            g.setColor(Color.BLACK);
            g.drawString(frame.text, frame.x + 40, frame.y - 5);
        }
        System.out.println("Type: " + type);
        switch (type) {
            case "LAmogi":
                drawLeftAmongi(g, frame, color);
            case "RAmogi":
                drawAmongi(g, frame, color);
            case "DeadAmgi":
                drawDeadAmogi(g, frame, color);
            default:
                System.out.println("AAAAAAAAAAAAAAAAAAA");
                System.exit(-2049032409);
        }
    }

    private static void drawDeadAmogi(Graphics g, AnimFrame frame, Color color) {
    }

    // This method fills a display of the given dimensions with the given Color
    // If your animation isn't smooth, try using this method in place of panel.clear().
    static void clearRect(Graphics g, Color fill, int width, int height) {
        Color current = g.getColor();
        g.setColor(fill);
        g.fillRect(0, 0, width, height);
        g.setColor(current);
    }

    public static void drawLeftAmongi(Graphics g, AnimFrame frame, Color color) {
        DrawAmogiBase(g, frame.x, frame.y, color);
        g.fillOval(frame.x - 12, frame.y + 15, 30, 10);

        g.setColor(color);
        g.fillRect(frame.x + 40, frame.y + 12, 5, 20);
    }

    public static void drawAmongi(Graphics g, AnimFrame frame, Color color) {
        DrawAmogiBase(g, frame.x, frame.y, color);
        g.fillOval(frame.x + 20, frame.y + 15, 30, 10);

        g.setColor(color);
        g.fillRect(frame.x - 5, frame.y + 12, 5, 20);
    }

    private static void DrawAmogiBase(Graphics g, int x, int y, Color color) {
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

    public static int randint(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    // clearRect(g, Color.WHITE, panel.getWidth(), panel.getHeight());
    // - where panel and g are local variables you defined in the calling
    //   code, and you want the background to be white after clearing.
}