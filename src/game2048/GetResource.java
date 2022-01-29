package game2048;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class GetResource {
    private ResourceBundle res;

    public GetResource() {
        res = ResourceBundle.getBundle("game2048/Resource");
    }

    public GetResource(String path) {
        res = ResourceBundle.getBundle(path);
    }

    public GetResource(String path, Locale locale) {
        res = ResourceBundle.getBundle(path, locale);
    }

    public GetResource(String path, String language) {
        res = ResourceBundle.getBundle(path, new Locale(language));
    }

    public GetResource(ResourceBundle res) {
        this.res = res;
    }

    public String getString(String key) {
        return new String(res.getString(key).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }
}
