import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class AnimParser {
    List<AnimFrame> frames = new ArrayList<>();
    public HashMap<String, String> types = new HashMap<>();
    public String ifilepath; // This should be unique for each parser. If it's not then it'll be pretty useless
    public String type;
    public HashMap<String, Mogus> mogusMap = new HashMap<>();

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

                if (commentOrBlank(data)) {
                    continue;
                }

                // Init a few helper variables
                String[] words = data.split(" ");
                String firstKeyword = words[0];
                String withoutLastChar = data.substring(0, firstKeyword.length() - 1);

                if (firstKeyword.equals("declare")) {
                    if (!(words.length >= 4)) {
                        System.out.println("Not enough parts for a `declare` on line " + lineNumber);
                    }
                    try {
                        types.put(words[1], getStringFromQuotes(words[2]));
                        types.put(getStringFromQuotes(words[2]), words[1]);
                        Mogus mogus = new Mogus(getColorFromString(words[3]));
                        mogus.type = getStringFromQuotes(words[2]);
                        mogusMap.put(words[1], mogus);
                    } catch (Exception e) {
                        System.out.println("Invalid `declare` declaraction at line " + lineNumber + e.getMessage());
                    }
                    continue;

                } else if (firstKeyword.equals("switch")) {
                    // Switch the mapping in types, so you can switch looking directions
                    if (!types.containsKey(words[1])) {
                        System.out.println("Invalid switch keywords at line " + lineNumber);
                        continue;
                    }

                    if (!(words.length >= 4)) {
                        System.out.println("Not enough parts for a `switch` on line " + lineNumber);
                        continue;
                    }

                    types.replace(words[1], words[2]);
                    Mogus mongi = mogusMap.get(words[1]);
                    mongi.type = getStringFromQuotes(words[2]);
                    mongi.color = getColorFromString(words[3]);
                    mogusMap.replace(words[1], mongi);
                    continue;
                }

                // Keyword to show or hide an animation object
                else if (firstKeyword.equals("visb")) {
                    if (!types.containsKey(words[1])) {
                        System.out.println("Can't switch an animation object that doesn't exist " + lineNumber);
                        continue;
                    }

                    if (!(words.length >= 3)) {
                        System.out.println("Not enough parts for a `visb` command on line " + lineNumber);
                        continue;
                    }

                    switch (words[2]) {
                        case "hide":
                        case "h": {
                            Mogus mongi = mogusMap.get(words[1]);
                            mongi.enabled = false;
                            mogusMap.replace(words[1], mongi);
                        }
                        case "show":
                        case "s": {
                            Mogus mongi = mogusMap.get(words[1]);
                            mongi.enabled = true;
                            mogusMap.replace(words[1], mongi);
                        }
                    }
                    continue;

                } else if (types.containsKey(withoutLastChar)) {
                    String identifier = words[0];
                    if (!identifier.endsWith(":")) {
                        System.out.println("Invalid identifier for section block at line: " + lineNumber);
                        continue;
                    }
                    // Set out type so it works with the legacy code
                    type = types.get(withoutLastChar);
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
                Mogus prevMogus = mogusMap.get(types.get(type));
                prevMogus.x = x;
                prevMogus.y = y;
                prevMogus.text = text;
                mogusMap.replace(types.get(type), prevMogus);

                AnimFrame frame = new AnimFrame();
                frame.delay = delay;
                List<Mogus> mappedMogi = mogusMap.values().stream().toList();
                frame.mogi = new ArrayList<>();
                for (Mogus mogi :
                        mappedMogi) {
                    Mogus tempMogi = new Mogus(mogi.color);
                    tempMogi.type = mogi.type;
                    tempMogi.x = mogi.x;
                    tempMogi.y = mogi.y;
                    tempMogi.text = mogi.text;
                    frame.mogi.add(tempMogi);
                }

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
        } catch (NullPointerException e) {
            System.out.println("Something was null and that's bad " + lineNumber);
            e.printStackTrace();
        }
    }

    private static boolean commentOrBlank(String data) {
        return data.startsWith("#") || data.equals("");
    }

    private static String getStringFromQuotes(String input) {
        if (input.chars().toArray()[0] == '"') {
            // Trim the string to not have quotes
            input = input.substring(1, input.length() - 1);
        }
        return input;
    }

    private Color getColorFromString(String colorCode) {
        return switch (colorCode.toLowerCase()) {
            case "green" -> Color.GREEN;
            case "red" -> Color.RED;
            case "black" -> Color.BLACK;
            case "blue" -> Color.BLUE;
            default -> Color.red;
        };
    }
}
