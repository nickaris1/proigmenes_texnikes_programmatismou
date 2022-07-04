package gr.upatras.Akinita;

import javax.servlet.http.HttpServletRequest;
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
        if (result == null || result.equals("null")) {
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
        if (result == null || result.equals("null")) {
            return null;
        } else {
            return Boolean.parseBoolean(result);
        }
    }

    /**
     * Get Full url from request
     *
     * @param request
     * @return URL String
     */
    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    /**
     * create string to log with IP and request URL
     * @param request HttpServletRequest
     * @return String
     */
    public static String createRequestLogReport(HttpServletRequest request) {
        return "IP: " + request.getRemoteAddr() + " URL: " + Util.getFullURL(request);
    }
}

