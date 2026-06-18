import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {

    public static double findDoubleField(String json, String field) throws Exception {
        Pattern pattern = Pattern.compile("\"" + field + "\":\\-?\\d*\\.\\d*");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String entry = matcher.group();
            String value = entry.split(":")[1];
            return Double.parseDouble(value);
        } else {
            throw new Exception("Cannot find field " + field);
        }
    }

    public static String findObjectField(String json, String field) throws Exception {
        Pattern pattern = Pattern.compile("\"" + field + "\":\\{(.*)\\}");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String entry = matcher.group();
            return entry.split(":")[1];
        } else {
            throw new Exception("Cannot find field " + field);
        }
    }

}
