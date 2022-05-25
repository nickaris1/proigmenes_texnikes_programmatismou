package gr.upatras.Akinita;

public class Util {

    public static Integer intResultOrNull(String result) {
        if(result == null || result.equals("null")) {
            return null;
        } else {
            return Integer.parseInt(result);
        }
    }
    public static Boolean boolResultOrNull(String result) {
        if(result == null || result.equals("null")) {
            return null;
        } else {
            return Boolean.parseBoolean(result);
        }
    }
}
