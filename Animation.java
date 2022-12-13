import java.awt.*;

class Animation {
  



  // This method fills a display of the given dimensions with the given Color
  // If your animation isn't smooth, try using this method in place of panel.clear().
  static void clearRect(Graphics g, Color fill, int width, int height) {
    Color current = g.getColor();
    g.setColor(fill);
    g.fillRect(0, 0, width, height);
    g.setColor(current);
  }

  // ^^ code to call clearRect() method could look like so:
  // clearRect(g, Color.WHITE, panel.getWidth(), panel.getHeight());
  // - where panel and g are local variables you defined in the calling
  //   code, and you want the background to be white after clearing.
}