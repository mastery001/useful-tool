package tool.database.sql.annotation.support;

import java.lang.reflect.Field;

import com.JavaPersistence.Annotation.VaribleAnnotation;


public class VaribleKey {

	private VaribleAnnotation key;

	public VaribleKey() {

	}

	public VaribleKey(Field field) {
		key = field.getAnnotation(VaribleAnnotation.class);
	}
	
	public String column(){
		if(key == null) {
			return "";
		}
		return key.column();
	}
	
	public boolean auto_increment() {
		return false;
	}
	
	public boolean update() {
		return false;
	}
	
	public String type() {
		return "";
	}
	
	public String key() {
		return "";
	}
	
	public int length() {
		return 20;
	}
}
