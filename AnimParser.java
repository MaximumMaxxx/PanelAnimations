import java.io.*;
import java.util.*;

public class AnimParser {
    List<AnimFrame> frames = new ArrayList<>();
    public HashMap<String, String> types = new HashMap<>();
    public String ifilepath = null; // This should be unique for each parser. If it's not then it'll be pretty useless
    public String type;

    public AnimParser(String filepath) {
        ifilepath = filepath;
        System.out.println("Parsing " + filepath);
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

                if (data.startsWith("name")) {
                    String[] split = data.split(" ");
                    try {
                        types.put(split[1], getStringFromQuotes(split[2]));
                    } catch (Exception _e) {
                        System.out.println("Invalid `name` declaraction at line " + lineNumber);
                    }
                    continue;
                }

                if (types.containsKey(data.split(" ")[0].substring(0, data.split(" ")[0].length() - 1))) {
                    String identifier = data.split(" ")[0];
                    if (!identifier.endsWith(":")) {
                        System.out.println("Invalid identifier for section block at line: " + lineNumber);
                        continue;
                    }
                    // Set out type so it works with the legacy code
                    type = types.get(identifier.substring(0, identifier.length() - 2));
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
                frame.type = this.type;

                frames.add(frame);
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error parsing line " + lineNumber);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format at " + lineNumber);
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
