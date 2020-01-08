package common;

import java.util.Properties;

import com.google.inject.Injector;

public class Core {

	private static Injector injector = null;
	private static Properties propertyRepository = null;

	public static Injector getInjector() {
		return injector;
	}

	public static void setInjector(Injector injector) {
		Core.injector = injector;
	}

	public static Properties getPropertyRepository() {
		return propertyRepository;
	}

	public static void setPropertyRepository(Properties propertyRepository) {
		Core.propertyRepository = propertyRepository;
	}

}
