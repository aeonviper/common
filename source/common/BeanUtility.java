package common;

import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

public class BeanUtility {

	protected static BeanUtilsBean instance;

	static {

		ConvertUtilsBean converter = new ConvertUtilsBean() {
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum()) {
					if (value != null && !value.isEmpty()) {
						return Enum.valueOf(clazz, value);
					} else {
						return null;
					}
				} else {
					return super.convert(value, clazz);
				}
			}
		};

		converter.register(new BooleanConverter(null), Boolean.class);
		converter.register(new ByteConverter(null), Byte.class);
		converter.register(new CharacterConverter(null), Character.class);
		converter.register(new DoubleConverter(null), Double.class);
		converter.register(new FloatConverter(null), Float.class);
		converter.register(new IntegerConverter(null), Integer.class);
		converter.register(new LongConverter(null), Long.class);
		converter.register(new ShortConverter(null), Short.class);
		converter.register(new BigDecimalConverter(null), BigDecimal.class);

		instance = new BeanUtilsBean(converter);

	}

	public static BeanUtilsBean instance() {
		return instance;
	}

	public static void setInstance(BeanUtilsBean instance) {
		BeanUtility.instance = instance;
	}

}
