package gr.upatras.Database;

import gr.upatras.Akinita.CustomSearch;
import gr.upatras.Akinita.Entity;
import gr.upatras.Akinita.Location;
import gr.upatras.Akinita.Modifiers;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.StringJoiner;

/**
 * @author Issaris Nikolaos
 */
public class DatabaseUtil {

    private static final Logger log = LoggerFactory.getLogger(DatabaseAccess.class);

    /**
     * Create partially Query from an object for search <BR>
     * e.g. "column1"="value1" and "column2"="value2" ...
     *
     * @param obj object to get data from
     * @param <T> Entity type
     * @return query String for Search
     */
    public static <T extends Entity> String querySearchParamCreator(T obj) {
        StringJoiner query = new StringJoiner("");

        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // if you want to modify private fields
                if (field.get(obj) != null && !String.valueOf(field.getType()).equals("interface java.util.Map")) {
                    query.add("\"");
                    query.add(obj.getFieldsMap().get(field.getName()));
                    query.add("\"=\"");
                    query.add(String.valueOf(field.get(obj)));
                    query.add("\" and ");
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        String qStr = query.toString();
        return qStr.substring(0, qStr.length() - 5);
    }

    /**
     * Create partially Query from an object for search With Modifiers<BR>
     * e.g. "column1"="value1" and "column2"="value2" ...
     *
     * @param obj object to get data from
     * @param <T> Entity type
     * @return query String for Search
     */
    public static <T extends Entity> String querySearchParamCreator(T obj, Modifiers mod, String pre) {
        StringJoiner query = new StringJoiner("");

        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // if you want to modify private fields
                if (field.get(obj) != null && !String.valueOf(field.getType()).equals("interface java.util.Map")) {
                    query.add(pre + ".");
                    query.add(obj.getFieldsMap().get(field.getName()));
                    String modmap =  mod.getFieldsMap().get(field.getName());
                    Field modField = mod.getClass().getDeclaredField(modmap);
                    modField.setAccessible(true);
                    String modStr = String.valueOf(modField.get(mod));
                    query.add(StringEscapeUtils.unescapeJava(modStr));
                    query.add("\"");
                    query.add(String.valueOf(field.get(obj)));
                    query.add("\" and ");
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        String qStr = query.toString();
        return qStr.substring(0, qStr.length() - 5);
    }

    /**
     * Create partially Query from an object to insert new row <BR>
     * e.g. ("column1", "column2", "column3", "column4") VALUES ("value1", "value2", "value3", "value4")
     *
     * @param obj object to get data from
     * @param <T> Entity type
     * @return query String for Insert
     */
    public static <T extends Entity> String queryInsertParamCreator(T obj) {
        StringJoiner tableFields = new StringJoiner("");
        StringJoiner valuesFields = new StringJoiner("");

        tableFields.add("(");
        valuesFields.add("(");

        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // if you want to modify private fields
                if (field.get(obj) != null && !String.valueOf(field.getType()).equals("interface java.util.Map")) {
                    tableFields.add("\"");
                    tableFields.add(obj.getFieldsMap().get(field.getName()));
                    tableFields.add("\", ");

                    valuesFields.add("\"");
                    valuesFields.add(String.valueOf(field.get(obj)));
                    valuesFields.add("\", ");
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        String tableFieldsStr = tableFields.toString();
        String valuesFieldsStr = valuesFields.toString();
        return tableFieldsStr.substring(0, tableFieldsStr.length() - 2) + ") VALUES " + valuesFieldsStr.substring(0, valuesFieldsStr.length() - 2) + ")";
    }


    /**
     * Create partially Query from an object to Update <BR>
     * e.g. "column1"="value1", "column1"="value1"
     *
     * @param obj object to get data from
     * @param <T> Entity type
     * @return query String for Insert
     */
    public static <T extends Entity> String queryUpdateParamCreator(T obj) {
        StringJoiner query = new StringJoiner("");

        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // if you want to modify private fields
                if (field.get(obj) != null && !String.valueOf(field.getType()).equals("interface java.util.Map")) {
                    query.add("\"");
                    query.add(obj.getFieldsMap().get(field.getName()));
                    query.add("\"=\"");
                    query.add(String.valueOf(field.get(obj)));
                    query.add("\", ");
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        String queryStr = query.toString();
        return queryStr.substring(0, queryStr.length() - 2);
    }


    public static String searchQuery(CustomSearch cs) {
        StringJoiner query = new StringJoiner("");
        query.add("SELECT * from ");

        StringJoiner queryWhere = new StringJoiner("");
        queryWhere.add("WHERE ");


        for (Field field : cs.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // if you want to modify private fields

                if (field.get(cs) != null && !String.valueOf(field.getType()).equals("interface java.util.Map") && !String.valueOf(cs.getFieldsMap().get(field.getName())).equals("null")) {

                    query.add(cs.getFieldsMap().get(field.getName()));
                    query.add(",");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        String qStr = query.toString();
        qStr = qStr.substring(0, qStr.length() - 1);

        if (cs.getLocation() != null && cs.getProperty() != null ) {
            queryWhere.add("l.Area_code=p.area_code and ");
        }

        if (cs.getOwner() != null && cs.getProperty() != null ) {
            queryWhere.add("o.AFM=p.owner_afm and ");
        }

        if (cs.getSale() != null && cs.getProperty() != null ) {
            queryWhere.add("s.property_id=p.id and ");
        }

        String wStr = queryWhere.toString();
        wStr = wStr.substring(0, wStr.length() - 5);

        String locStr = "";
        String proStr = "";
        String salStr = "";
        String ownStr = "";
        if (cs.getLocation() != null) {
            locStr = querySearchParamCreator(cs.getLocation(), cs.getModifiers(), "l");
        }

        if (cs.getProperty() != null) {
            proStr = querySearchParamCreator(cs.getProperty(), cs.getModifiers(), "p");
        }
        if (cs.getSale() != null) {
            salStr = querySearchParamCreator(cs.getSale(), cs.getModifiers(), "s");
        }
        if (cs.getOwner() != null) {
            ownStr = querySearchParamCreator(cs.getOwner(), cs.getModifiers(), "o");
        }



        String finalQuery = qStr + " " + wStr + " and " + locStr + " and " + ownStr + " and "+ proStr + " and "+ salStr + ";";
        if (finalQuery.endsWith(" and ;")) {
            finalQuery = finalQuery.substring(0, finalQuery.length() - 6);
            finalQuery += ";";
        }
        while (finalQuery.contains(" and  and ")) {
            finalQuery = finalQuery.replace(" and  and ", " and ");
        }
        return finalQuery;
    }
}
