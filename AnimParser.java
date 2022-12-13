import java.io.*;
import java.text.ParseException;
import java.util.*;

public class AnimParser {
    List<AnimFrame> frames = new ArrayList<>();

    static class AnimFrame {
        public int x;
        public int y;
        public int delay;
        public String text;
    }

    public AnimParser(String filepath) {
        int lineNumber = 0;
        try {
            File myObj = new File(filepath);
            Scanner Reader = new Scanner(myObj);
            while (Reader.hasNextLine()) {
                lineNumber += 1;
                String data = Reader.nextLine().trim();

                if (data.startsWith("#") || data.equals("")) {
                    continue;
                }

                String[] sliced = data.split(" ");
                int x = Integer.parseInt(sliced[0]);
                int y = Integer.parseInt(sliced[1]);
                int delay = Integer.parseInt(sliced[3]);
                String text = "";
                if (sliced.length == 4) {
                    text = sliced[3];
                    if (text.chars().toArray()[0] == '"') {
                        // Trim the string to not have quotes
                        text = text.substring(1, text.length() - 1);
                    }
                }

                AnimFrame frame = new AnimFrame();
                frame.x = x;
                frame.y = y;
                frame.delay = delay;
                frame.text = text;

                frames.add(frame);
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error parsing line " + lineNumber);
        } catch (NumberFormatException e) {
            System.out.println("Invalid format at " + lineNumber);
        }
    }
}
