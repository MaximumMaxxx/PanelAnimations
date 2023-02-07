import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

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

        renderFiles(new String[]{
                "animations/1.anim",
                "animations/2.anim",
                "animations/3.anim"
        }, panel, gPanel, offscreen, g);

        // I think for timing you'll need stacks and a hashmap
        // Good luck man


    }

    public static void renderFiles(String[] files, DrawingPanel panel, Graphics gPanel, BufferedImage offscreen, Graphics g) {
        AnimParser parser;
        for (String file :
                files) {
            parser = new AnimParser(file);
            renderParser(panel, gPanel, offscreen, g, parser);
        }
    }

    private static void renderParser(DrawingPanel panel, Graphics gPanel, BufferedImage offscreen, Graphics g, AnimParser parser) {
        for (AnimFrame frame : parser.frames) {
            if (!(frame.delay < 0)) {
                panel.sleep(1000 / FPS);
            }
            clearRect(g, panel.getWidth(), panel.getHeight());
            // TODO! Make this function
            Image img = null;
            try {
                img = ImageIO.read(new File("maps/" + frame.background.background + ".png"));
            } catch (Exception e) {
                System.out.println("Unable to load map " + frame.background.background);
            }
            g.drawImage(img, frame.background.x, frame.background.y, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
            for (Mogus mogi :
                    frame.mogi) {
                mogi.draw(g);
            }
            gPanel.drawImage(offscreen, 0, 0, panel);
            if (!(frame.delay < 0)) {
                panel.sleep(frame.delay);
            }
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