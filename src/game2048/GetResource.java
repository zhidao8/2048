package game2048;

import java.io.UnsupportedEncodingException;
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
		try {
			return new String(res.getString(key).getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
