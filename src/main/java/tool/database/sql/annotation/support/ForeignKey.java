package tool.database.sql.annotation.support;

import java.lang.reflect.Field;

import com.JavaPersistence.Annotation.ForeginKeysAnnotation;


public class ForeignKey extends VaribleKey {
	private ForeginKeysAnnotation key;

	public ForeignKey(Field field) {
		key = field.getAnnotation(ForeginKeysAnnotation.class);
	}

	@Override
	public String column() {
		// TODO Auto-generated method stub
		return key.column();
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return key.type();
	}

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return key.key();
	}

}
