package inflearn_clone.springboot.util;

public class CommonUtil {
    public static String urlToUri(String url) {
        return url.substring(url.indexOf("://") + 3).substring(url.substring(url.indexOf("://") + 3).indexOf("/"));
    }
}
