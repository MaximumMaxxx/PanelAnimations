import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


        ArrayList<String> results = getListOfFiles("./animations/");
        HashMap<String, Image> maps = loadMaps("./maps/");

        renderFiles(
                results.toArray(new String[0]),
                maps,
                panel,
                gPanel,
                offscreen,
                g
        );
    }

    private static HashMap<String, Image> loadMaps(String directory) {
        String[] files = getListOfFiles(directory).toArray(new String[0]);
        HashMap<String, Image> nameImageMap = new HashMap<>();

        for
        (String imageFile :
                files) {
            try {
                Image img = ImageIO.read(
                        new File(imageFile)
                );
                nameImageMap.put(imageFile, img);
            } catch (IOException e) {
                System.out.println("Unable to load map " + imageFile);
                e.printStackTrace();
            }

        }

        return nameImageMap;
    }

    private static ArrayList<String> getListOfFiles(String directory) {
        File[] files = new File(directory + "/").listFiles();
        ArrayList<String> results = new ArrayList<>();

        //If this pathname does not denote a directory, then listFiles() returns null.
        try {
            assert files != null;
        } catch (AssertionError e) {
            System.out.println("./" + directory + "has no files in it");
        }
        for (File file : files) {
            if (file.isFile()) {
                results.add(directory + file.getName());
            }
        }
        return results;
    }

    public static void renderFiles(String[] files, HashMap<String, Image> maps, DrawingPanel panel, Graphics gPanel, BufferedImage offscreen, Graphics g) {
        AnimParser parser;
        for (String file :
                files) {
            parser = new AnimParser(file);
            renderParser(panel, maps, gPanel, offscreen, g, parser);
        }
    }

    private static void renderParser(DrawingPanel panel, HashMap<String, Image> maps, Graphics gPanel, BufferedImage offscreen, Graphics g, AnimParser parser) {
        for (AnimFrame frame : parser.frames) {
            if (!(frame.delay < 0)) {
                panel.sleep(1000 / FPS);
            }
            clearRect(g, panel.getWidth(), panel.getHeight());

            Image img = null;
            if (frame.background.background != null) {
                img = maps.get(frame.background.background);
            }

            for (Mogus mogi :
                    frame.mogi) {
                mogi.draw(g);
            }

            gPanel.drawImage(img, 0, 0, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
            if (!(frame.delay < 0)) {
                panel.sleep(frame.delay);
            }
        }
    }


    // clearRect(g, Color.WHITE, panel.getWidth(), panel.getHeight());
    // - where panel and g are local variables you defined in the calling
    //   code, and you want the background to be white after clearing.
    static void clearRect(Graphics g, int width, int height) {
        Color current = g.getColor();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(current);
    }


}