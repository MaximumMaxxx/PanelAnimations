import java.awt.*;

class Animation {

    static final int PANEL_HEIGHT = 500;
    static final int PANEL_WIDTH = 500;

    public static void main(String[] args) {
        DrawingPanel panel = new DrawingPanel(PANEL_WIDTH, PANEL_HEIGHT);
        Graphics g = panel.getGraphics();


        panel.sleep(200);

        AnimParser parser = new AnimParser("./file.anim");

        for (AnimParser.AnimFrame frame : parser.frames) {
            panel.sleep(100);
            clearRect(g, Color.WHITE, panel.getWidth(), panel.getHeight());
            drawAmongi(g, frame.x, frame.y, Color.red);
            panel.sleep(frame.delay);
        }
    }


    // This method fills a display of the given dimensions with the given Color
    // If your animation isn't smooth, try using this method in place of panel.clear().
    static void clearRect(Graphics g, Color fill, int width, int height) {
        Color current = g.getColor();
        g.setColor(fill);
        g.fillRect(0, 0, width, height);
        g.setColor(current);
    }


    public static void drawAmongi(Graphics g, int x, int y, Color color) {
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
        g.fillOval(x + 20, y + 15, 30, 10);

        g.setColor(color);
        g.fillRect(x - 5, y + 12, 5, 20);
    }

    public static int randint(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    // ^^ code to call clearRect() method could look like so:
    // clearRect(g, Color.WHITE, panel.getWidth(), panel.getHeight());
    // - where panel and g are local variables you defined in the calling
    //   code, and you want the background to be white after clearing.
}