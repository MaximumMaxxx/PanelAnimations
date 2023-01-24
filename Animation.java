import java.awt.*;
import java.awt.image.BufferedImage;

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

        AnimParser parser;
//        AnimParser parser = new AnimParser("animations/1.anim");
//        renderParser(panel, gPanel, offscreen, g, parser);
        parser = new AnimParser("animations/2.anim");
        renderParser(panel, gPanel, offscreen, g, parser);


        // I think for timing you'll need stacks and a hashmap
        // Good luck man


    }

    private static void renderParser(DrawingPanel panel, Graphics gPanel, BufferedImage offscreen, Graphics g, AnimParser parser) {
        for (AnimFrame frame : parser.frames) {
            panel.sleep(1000 / FPS);
            clearRect(g, panel.getWidth(), panel.getHeight());
            for (Mogus mogi :
                    frame.mogi) {
                mogi.draw(g);
            }
            gPanel.drawImage(offscreen, 0, 0, panel);
            panel.sleep(frame.delay);
        }
    }


    static void clearRect(Graphics g, int width, int height) {
        Color current = g.getColor();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(current);
    }


    // clearRect(g, Color.WHITE, panel.getWidth(), panel.getHeight());
    // - where panel and g are local variables you defined in the calling
    //   code, and you want the background to be white after clearing.
}