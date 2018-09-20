package jdroplet.util;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuibo on 2018/2/6.
 */
public class QueryStringParser {

    private static class NameValuePair {
        final String key;
        final String value;

        NameValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static final String DEFAUL_CHARSET = "UTF-8";

    private Charset charset;
    private List<NameValuePair> query = new ArrayList<NameValuePair>();

    /**
     * Constructor QueryStringParser initialises a new instance of this class
     * with the given <tt>queryString</tt>. By default it uses the "UTF-8"
     * Character Encoding.
     *
     * @author TapasB
     * @param queryString
     *            The Query String to parse
     * @throws NullPointerException
     *             If the given <tt>queryString</tt> is null
     */
    public QueryStringParser(String queryString) throws NullPointerException {
        this(queryString, DEFAUL_CHARSET);
    }

    /**
     * Constructor QueryStringParser
     *
     * @author TapasB
     * @param queryString
     *            The Query String to parse
     * @param charsetName
     *            The {@link Charset} needs to be used to parse
     * @throws NullPointerException
     * @throws IllegalCharsetNameException
     *             If the given <tt>charsetName</tt> name is illegal
     * @throws IllegalArgumentException
     *             If the given <tt>charsetName</tt> is null
     * @throws UnsupportedCharsetException
     *             If no support for the named <tt>charsetName</tt> is available
     *             in this instance of the Java virtual machine
     */
    public QueryStringParser(String queryString, String charsetName) throws NullPointerException, IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
        if (queryString == null) {
            throw new NullPointerException("Query String is Null.");
        }

        this.charset = Charset.forName(charsetName);
        parse(queryString);
    }

    // This method is used to parse the query string
    private void parse(String queryString) {
        for (String pair : queryString.split("&")) {
            int idxOfEqual = pair.indexOf("=");

            if (idxOfEqual < 0) {
                addElement(pair, "");
            } else {
                String key = pair.substring(0, idxOfEqual);
                String value = pair.substring(idxOfEqual + 1);
                addElement(key, value);
            }
        }
    }

    // This method adds the given key and value into the List query.
    // Before adding it decodes the key and value with the given charset.
    public void addElement(String key, String value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key or Value is Null");
        }

        try {
            String charsetName = charset.name();
            query.add(new NameValuePair(URLDecoder.decode(key, charsetName), URLDecoder.decode(value, charsetName)));
        } catch (Exception ignore) {
        }
    }

    /**
     * Method getParameterValue returns the value associated with the given key
     *
     * @author TapasB
     * @param key
     *            The key for which the value is needed
     * @return The associated value to the given key. If the given key is not
     *         found then it returns null
     */
    public String getParameterValue(String key) {
        for (NameValuePair pair : query) {
            if (pair.key.equals(key)) {
                return pair.value;
            }
        }

        return null;
    }

    /**
     * Method getParameterValues returns the List of values associated with the
     * given key
     *
     * @author TapasB
     * @param key
     *            The key for which the List of values are needed
     * @return The associated List of values to the given key. If the given key
     *         is not found then it returns an empty list
     */
    public List<String> getParameterValues(String key) {
        List<String> list = new ArrayList<String>();

        for (NameValuePair pair : query) {
            if (pair.key.equals(key)) {
                list.add(pair.value);
            }
        }

        return list;
    }

}
