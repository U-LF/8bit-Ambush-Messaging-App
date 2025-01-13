package ClientShared;

public class MessageUtils {
    public static String[] makeStringArray(String input)
    {
        if(input.isEmpty())
        {
            return new String[0];
        } else {
            return input.split(",\\s*");
        }
    }

    public static String trimString (String input, String PartToCut)
    {
        return input.substring(PartToCut.length()).trim();
    }

    public static void displayStringArray(String[] stringArray)
    {
        for (String msgOnIndex : stringArray) {
            System.out.println(msgOnIndex);
        }
    }
}