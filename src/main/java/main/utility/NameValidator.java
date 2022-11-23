package main.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NameValidator {
    /**
     * @param namVar String to validate a name
     * @return boolean value, true if it's a correct name, false if not
     */
    public static boolean check(String namVar) {
        String namRegExpVar = "^[A-Z][a-z]{2,}(?: [A-Z][a-z]*)*$";
        Pattern pVar = Pattern.compile(namRegExpVar);
        Matcher mVar = pVar.matcher(namVar);
        return mVar.matches();
    }
}
