package tool.database.sql.annotation.support;

import java.lang.reflect.Field;

import com.JavaPersistence.Annotation.PrimaryKeysAnnotation;



public class PrimaryKey extends VaribleKey{
	
	private PrimaryKeysAnnotation key;
	
	public PrimaryKey(Field field) {
		key = field.getAnnotation(PrimaryKeysAnnotation.class);
	}

	@Override
	public String column() {
		// TODO Auto-generated method stub
		return key.column();
	}

	@Override
	public boolean auto_increment() {
		// TODO Auto-generated method stub
		return key.auto_increment();
	}

	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return key.update();
	}

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return key.key();
	}
	
	
}
