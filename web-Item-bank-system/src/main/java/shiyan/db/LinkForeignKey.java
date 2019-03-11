package shiyan.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在某个table对象需要外键关联其它类对象时候
 * @author iceki
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface LinkForeignKey {
	/**
	 * 指定该类中用于保存外键值的属性名字
	 * @return
	 */
	public String value() default "";
	
	/**
	 * 外键表的主键名称，如果为空，则默认是id
	 * @return
	 */
	public String pkname() default "";
	
	/**
	 * 外键表的表名，如果为空，则默认是外键属性的类名
	 * @return
	 */
	public String tablename() default "";
}
