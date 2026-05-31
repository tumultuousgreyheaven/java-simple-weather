import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {

    public static double findDoubleField(String json, String field) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("\"" + field + "\":*,");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String entry = matcher.group();
            String value = entry.split(":")[1];
            return Double.parseDouble(value);
        } else {
            throw new IllegalArgumentException("Cannot find field " + field);
        }
    }

}
