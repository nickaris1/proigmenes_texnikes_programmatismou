package gr.upatras.Akinita;

/**
 * Utility static functions
 */
public class Util {
    /**
     * Return integer if it can be converted from result else return null
     *
     * @param result String
     * @return Integer or null
     */
    public static Integer intResultOrNull(String result) {
        if(result == null || result.equals("null")) {
            return null;
        } else {
            return Integer.parseInt(result);
        }
    }

    /**
     * Return Boolean if it can be converted from result else return null
     *
     * @param result String
     * @return Boolean (true, false)  or null
     */
    public static Boolean boolResultOrNull(String result) {
        if(result == null || result.equals("null")) {
            return null;
        } else {
            return Boolean.parseBoolean(result);
        }
    }
}
