import java.util.HashMap;
import java.util.Map;

public class Translator {

    private static final Map<String, String> brailleToEnglishMap = new HashMap<>();
    private static final Map<String, String> englishToBrailleMap = new HashMap<>();
    private static final String CAPITAL_PREFIX = ".....O";
    private static final String NUMBER_PREFIX = ".O.OOO";
    private static final String SPACE = "......";

    static {
        englishToBrailleMap.put("a", "O.....");
        englishToBrailleMap.put("b", "O.O...");
        englishToBrailleMap.put("c", "OO....");
        englishToBrailleMap.put("d", "OO.O..");
        englishToBrailleMap.put("e", "O..O..");
        englishToBrailleMap.put("f", "OOO...");
        englishToBrailleMap.put("g", "OOOO..");
        englishToBrailleMap.put("h", "O.OO..");
        englishToBrailleMap.put("i", ".OO...");
        englishToBrailleMap.put("j", ".OOO..");
        englishToBrailleMap.put("k", "O...O.");
        englishToBrailleMap.put("l", "O.O.O.");
        englishToBrailleMap.put("m", "OO..O.");
        englishToBrailleMap.put("n", "OO.OO.");
        englishToBrailleMap.put("o", "O..OO.");
        englishToBrailleMap.put("p", "OOO.O.");
        englishToBrailleMap.put("q", "OOOOO.");
        englishToBrailleMap.put("r", "O.OOO.");
        englishToBrailleMap.put("s", ".OO.O.");
        englishToBrailleMap.put("t", ".OOOO.");
        englishToBrailleMap.put("u", "O...OO");
        englishToBrailleMap.put("v", "O.O.OO");
        englishToBrailleMap.put("w", ".OOO.O");
        englishToBrailleMap.put("x", "OO..OO");
        englishToBrailleMap.put("y", "OO.OOO");
        englishToBrailleMap.put("z", "O..OOO");
        englishToBrailleMap.put(" ", SPACE);

        englishToBrailleMap.put("1", "O.....");
        englishToBrailleMap.put("2", "O.O...");
        englishToBrailleMap.put("3", "OO....");
        englishToBrailleMap.put("4", "OO.O..");
        englishToBrailleMap.put("5", "O..O..");
        englishToBrailleMap.put("6", "OOO...");
        englishToBrailleMap.put("7", "OOOO..");
        englishToBrailleMap.put("8", "O.OO..");
        englishToBrailleMap.put("9", ".OO...");
        englishToBrailleMap.put("0", ".OOO..");

        for (Map.Entry<String, String> entry : englishToBrailleMap.entrySet()) {
            brailleToEnglishMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static String detectInputType(String text) {
        if (text.matches("[O.]+")) {
            return "braille";
        } else {
            return "english";
        }
    }

    public static String translateToBraille(String text) {
        StringBuilder result = new StringBuilder();
        boolean isNumber = false;

        for (char c : text.toCharArray()) {
            if (Character.isDigit(c) && !isNumber) {
                result.append(NUMBER_PREFIX);  // Start number context
                isNumber = true;
            } else if (Character.isLetter(c) && Character.isUpperCase(c)) {
                result.append(CAPITAL_PREFIX); // Add capital letter prefix
                result.append(englishToBrailleMap.get(String.valueOf(c).toLowerCase()));
                isNumber = false;  // End number context
            } else if (Character.isLetter(c)) {
                result.append(englishToBrailleMap.get(String.valueOf(c)));
                isNumber = false;  // End number context
            } else if (c == ' ') {
                result.append(SPACE);
                isNumber = false;  // End number context
            }
        }
        return result.toString();
    }

    // Translate from Braille to English
    public static String translateToEnglish(String brailleText) {
        StringBuilder result = new StringBuilder();
        boolean isCapital = false;
        boolean isNumber = false;

        for (int i = 0; i < brailleText.length(); i += 6) {
            String brailleChar = brailleText.substring(i, i + 6);
            if (brailleChar.equals(CAPITAL_PREFIX)) {
                isCapital = true;
            } else if (brailleChar.equals(NUMBER_PREFIX)) {
                isNumber = true;
            } else if (brailleChar.equals(SPACE)) {
                result.append(" ");
                isNumber = false;  // End number context on space
            } else {
                String englishChar = brailleToEnglishMap.get(brailleChar);
                if (isNumber) {
                    // Translate braille a-j into numbers 1-0
                    englishChar = getNumberFromBraille(englishChar);
                }
                if (isCapital) {
                    result.append(englishChar.toUpperCase());
                    isCapital = false;
                } else {
                    result.append(englishChar);
                }
                // Reset number context after one number translation
                if (isNumber) {
                    isNumber = false;
                }
            }
        }
        return result.toString();
    }

    private static String getNumberFromBraille(String letter) {
        switch (letter) {
            case "a": return "1";
            case "b": return "2";
            case "c": return "3";
            case "d": return "4";
            case "e": return "5";
            case "f": return "6";
            case "g": return "7";
            case "h": return "8";
            case "i": return "9";
            case "j": return "0";
            default: return letter;
        }
    }

    public static String translator(String inputText) {
        String inputType = detectInputType(inputText);

        if (inputType.equals("english")) {
            return translateToBraille(inputText);
        } else {
            return translateToEnglish(inputText);
        }
    }

    public static void main(String[] args) {
        String inputTextEnglish = "Hello world 42";
        String inputTextBraille = ".....OO.OO..O..O..O.O.O.O.O.O.O..OO........OOO.OO..OO.O.OOO.O.O.O.OO.O.........O.OOO";

        System.out.println("English to Braille: " + translator(inputTextEnglish));
        System.out.println("Braille to English: " + translator(inputTextBraille));
    }
}