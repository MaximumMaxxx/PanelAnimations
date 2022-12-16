import java.io.*;
import java.text.ParseException;
import java.util.*;

public class AnimParser {
    List<AnimFrame> frames = new ArrayList<>();
    public static String type;

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

                if (data.startsWith("type")) {
                    type = getStringFromQuotes(data.split(" ")[1]);
                    continue;
                }


                String[] sliced = data.split(" ", 4);
                int x = Integer.parseInt(sliced[0]);
                int y = Integer.parseInt(sliced[1]);
                int delay = Integer.parseInt(sliced[2]);
                String text = "";
                if (sliced.length == 4) {
                    text = sliced[3];
                    text = getStringFromQuotes(text);
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
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid format at " + lineNumber);
        }
    }

    static String getStringFromQuotes(String input) {
        if (input.chars().toArray()[0] == '"') {
            // Trim the string to not have quotes
            input = input.substring(1, input.length() - 1);
        }
        return input;
    }
}
