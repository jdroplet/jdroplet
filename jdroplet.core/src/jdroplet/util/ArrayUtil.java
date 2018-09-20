package jdroplet.util;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by kuibo on 2017/10/13.
 */
public class ArrayUtil {

    public static Integer[] convert2Integer(String[] a) {
        Integer[] na = new Integer[a.length];

        for(int i=0; i<na.length; i++) {
            na[i] = new Integer(a[i]);
        }
        return na;
    }

    public static Integer[] convert2Int(String[] a) {
        Integer[] na = new Integer[a.length];

        for(int i=0; i<na.length; i++) {
            na[i] = Integer.parseInt(a[i]);
        }
        return na;
    }

    public static String[] unique(String[] a) {
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < a.length; i++) {
            if (!list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public static Integer[] unique(Integer[] a) {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < a.length; i++) {
            if (!list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        return list.toArray(new Integer[list.size()]);
    }

    public static boolean inArray(Integer[] list, Integer v) {
        for(Integer i:list) {
            if (i.longValue() == v.longValue())
                return true;
        }

        return false;
    }
}
