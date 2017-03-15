package tool.database.sql.annotation.support;

import java.lang.reflect.Field;

import com.JavaPersistence.Annotation.ForeginKeysAnnotation;
import com.JavaPersistence.Annotation.PrimaryKeysAnnotation;


public class FieldAnnotationHelper {

	/**
	 * 通过字段获取对应注解上的属性
	 * 
	 * @param field
	 * @return
	 */
	public static AnnotationAttribute getAnnotationAtttibute(Field field) {
		AnnotationAttribute annotationAttribute = new AnnotationAttribute();
		VaribleKey key = new VaribleKey(field);
		boolean flag = false;;
		// 如果同时配置了主键和外键
		if (field.isAnnotationPresent(PrimaryKeysAnnotation.class)
				&& field.isAnnotationPresent(ForeginKeysAnnotation.class)) {
			key = new PrimaryKey(field);
			flag = true;
		}else {
			if (field.isAnnotationPresent(PrimaryKeysAnnotation.class)) {
				key = new PrimaryKey(field);
			} else if (field.isAnnotationPresent(ForeginKeysAnnotation.class)) {
				key = new ForeignKey(field);
			}
		}
		
		annotationAttribute.setAuto_increment(key.auto_increment());
		annotationAttribute.setColumn(key.column());
		annotationAttribute.setKey(key.key());
		annotationAttribute.setLength(key.length());
		annotationAttribute.setType(key.type());
		if(flag) {
			annotationAttribute.setType(new ForeignKey(field).type());
		}
		annotationAttribute.setUpdate(key.update());
		return annotationAttribute;
	}

}
