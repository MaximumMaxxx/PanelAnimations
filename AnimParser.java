import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class AnimParser {
    List<AnimFrame> frames = new ArrayList<>();
    public String ifilepath; // This should be unique for each parser. If it's not then it'll be pretty useless
    public String currentMogusName;
    public String background;
    public int bgx;
    public int bgy;
    public HashMap<String, Mogus> nameToMoogusMap = new HashMap<>();

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

                if (firstKeyword.equals("bg")) {
                    if (!(words.length >= 4)) {
                        System.out.println("Not enough parts to `bg` line" + lineNumber);
                        continue;
                    }
                    this.background = words[1];
                    this.bgx = Integer.parseInt(words[2]);
                    this.bgy = Integer.parseInt(words[3]);
                    continue;
                }

                if (firstKeyword.equals("declare")) {
                    if (!(words.length >= 4)) {
                        System.out.println("Not enough parts for a `declare` on line " + lineNumber);
                        continue;
                    }
                    try {
                        String name = words[1];
                        String variant = getStringFromQuotes(words[2]);
                        Color color = getColorFromString(words[3]);

                        // Initialize the mognu
                        Mogus mogus = new Mogus(color);
                        mogus.type = variant;
                        nameToMoogusMap.put(name, mogus);
                    } catch (Exception e) {
                        System.out.println("Invalid `declare` declaraction at line " + lineNumber + e.getMessage());
                    }
                    continue;

                } else if (firstKeyword.equals("switch")) {
                    // Switch the mapping in types, so you can switch looking directions
                    if (!(words.length >= 4)) {
                        System.out.println("Not enough parts for a `switch` on line " + lineNumber);
                        continue;
                    }

                    String name = words[1];
                    String newType = words[2];
                    Color newColor = getColorFromString(words[3]);
                    if (!nameToMoogusMap.containsKey(name)) {
                        System.out.println("Invalid switch keywords at line " + lineNumber);
                        continue;
                    }


                    Mogus mongi = nameToMoogusMap.get(name);
                    mongi.type = getStringFromQuotes(newType);
                    mongi.color = newColor;
                    nameToMoogusMap.put(name, mongi);
                    continue;
                }

                // Keyword to show or hide an animation object
                else if (firstKeyword.equals("visb")) {
                    if (!(words.length >= 3)) {
                        System.out.println("Not enough parts for a `visb` command on line " + lineNumber);
                        continue;
                    }

                    String name = words[1];
                    String operation = words[2].toLowerCase();

                    if (!nameToMoogusMap.containsKey(name)) {
                        System.out.println("Can't switch an animation object that doesn't exist " + lineNumber);
                        continue;
                    }

                    switch (operation) {
                        case "hide", "h" -> {
                            Mogus mongi = nameToMoogusMap.get(name);
                            mongi.enabled = false;
                            nameToMoogusMap.put(name, mongi);
                        }
                        case "show", "s" -> {
                            Mogus mongi = nameToMoogusMap.get(name);
                            mongi.enabled = true;
                            nameToMoogusMap.put(name, mongi);
                        }
                    }
                    continue;

                } else if (nameToMoogusMap.containsKey(withoutLastChar)) {
                    // Logic for switching which mogus is selected
                    String identifier = words[0];
                    if (!identifier.endsWith(":")) {
                        System.out.println("Invalid identifier for section block at line: " + lineNumber);
                        continue;
                    }
                    currentMogusName = withoutLastChar;
                    continue;
                }


                String[] limitedSliced = data.split(" ", 4);
                int x = Integer.parseInt(words[0]);
                int y = Integer.parseInt(words[1]);
                int delay = Integer.parseInt(words[2]);
                String text = "";
                if (limitedSliced.length == 4) {
                    text = limitedSliced[3];
                    text = getStringFromQuotes(text);
                }

                // Update the mungle
                Mogus prevMogus = nameToMoogusMap.get(currentMogusName);
                prevMogus.x = x;
                prevMogus.y = y;
                prevMogus.text = text;
                nameToMoogusMap.replace(currentMogusName, prevMogus);

                // Clone the amongi into the frame
                AnimFrame frame = new AnimFrame();
                frame.delay = delay;
                List<Mogus> mappedMogi = nameToMoogusMap.values().stream().toList();
                frame.mogi = new ArrayList<>();
                Background bgClass = new Background();
                bgClass.background = background;
                bgClass.x = bgx;
                bgClass.y = bgy;
                frame.background = bgClass;

                for (Mogus mogi :
                        mappedMogi) {
                    Mogus clonedMogus = new Mogus(mogi.color);
                    clonedMogus.type = mogi.type;
                    clonedMogus.x = mogi.x;
                    clonedMogus.y = mogi.y;
                    clonedMogus.text = mogi.text;
                    clonedMogus.enabled = mogi.enabled;
                    frame.mogi.add(clonedMogus);
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
            case "purple" -> new Color(168, 50, 168);
            default -> Color.red;
        };
    }
}
