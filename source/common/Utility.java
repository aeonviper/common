package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtilsBean;

import crypto.BCrypt;

public class Utility {

	public static String[] regexFind(String data, String text) {
		Pattern pattern = Pattern.compile(text);
		Matcher matcher = pattern.matcher(data);
		if (matcher.find()) {
			String[] matches = new String[matcher.groupCount()];
			for (int i = 0; i < matcher.groupCount(); i++) {
				matches[i] = matcher.group(i + 1);
			}
			return matches;
		}
		return null;
	}

	public static String[] regexFind(String data, Pattern pattern) {
		Matcher matcher = pattern.matcher(data);
		if (matcher.find()) {
			String[] matches = new String[matcher.groupCount()];
			for (int i = 0; i < matcher.groupCount(); i++) {
				matches[i] = matcher.group(i + 1);
			}
			return matches;
		}
		return null;
	}

	public static String regexItem(String data, String text) {
		int index = 0;
		String[] matches = regexFind(data, text);
		if (matches != null && index < matches.length) {
			return matches[index];
		}
		return null;
	}

	public static String regexItem(String data, Pattern pattern) {
		int index = 0;
		String[] matches = regexFind(data, pattern);
		if (matches != null && index < matches.length) {
			return matches[index];
		}
		return null;
	}

	public static List<String> readFile(File file) {
		FileReader fileReader = null;
		BufferedReader reader = null;
		List<String> lines = new ArrayList<String>();
		try {
			String line = null;
			reader = new BufferedReader(fileReader = new FileReader(file));
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException eio) {
			eio.printStackTrace();
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return lines;
	}

	public static boolean writeFile(File file, List<String> lineList) {
		FileWriter fileWriter = null;
		PrintWriter outputStream = null;
		boolean success = false;
		try {
			outputStream = new PrintWriter(fileWriter = new FileWriter(file));
			for (String line : lineList) {
				outputStream.println(line);
			}
			success = true;
		} catch (IOException eio) {
			eio.printStackTrace();
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public static List<String> readFile(File file, String encoding) {
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		List<String> lines = new ArrayList<String>();
		try {
			String line = null;
			reader = new BufferedReader(inputStreamReader = new InputStreamReader(fileInputStream = new FileInputStream(file), encoding));
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException eio) {
			eio.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return lines;
	}

	public static boolean writeFile(File file, List<String> lineList, String encoding) {
		FileWriter fileWriter = null;
		PrintWriter outputStream = null;
		boolean success = false;
		try {
			outputStream = new PrintWriter(file, encoding);
			for (String line : lineList) {
				outputStream.println(line);
			}
			success = true;
		} catch (IOException eio) {
			eio.printStackTrace();
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	public static boolean same(String text1, String text2) {
		if (text1 == null && text2 == null) {
			return true;
		} else if (text1 != null && text2 != null) {
			return text1.equals(text2);
		}
		return false;
	}

	public static List<String> listify(String... array) {
		List<String> list = new ArrayList<>();
		for (String s : array) {
			list.add(s);
		}
		return list;
	}

	public static Method findMethod(Class clazz, String methodName) {
		if (clazz == null) {
			return null;
		}
		try {
			return clazz.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
			return findMethod(clazz.getSuperclass(), methodName);
		}
	}

	public static <K, T> Map<K, T> mapify(List<T> list, Class<K> clazz, String methodName, boolean useLast) {
		if (list == null) {
			throw new RuntimeException("List is null");
		} else {
			Map<K, T> map = new HashMap<K, T>();
			if (!list.isEmpty()) {
				T entity = list.get(0);
				Method getKey;
				try {
					getKey = findMethod(entity.getClass(), methodName);
					if (getKey != null) {
						for (T element : list) {
							K key = (K) getKey.invoke(element);
							if (useLast || !map.containsKey(key)) {
								map.put(key, element);
							}
						}
					}
				} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return map;
		}
	}

	public static <K, T> Map<K, T> mapify(List<T> list, Class<K> clazz, String methodName) {
		return mapify(list, clazz, methodName, true);
	}

	public static <K, T> Map<Long, T> mapify(List<T> list) {
		return mapify(list, Long.class, "getId", true);
	}

	public static <K, T> List<T> uniquefy(List<T> list, Class<K> clazz, String methodName) {
		if (list == null) {
			throw new RuntimeException("List is null");
		} else {
			List<T> transitList = new ArrayList<>();
			if (!list.isEmpty()) {
				Set<K> seenSet = new HashSet<>();
				T entity = list.get(0);
				Method getKey;
				try {
					getKey = findMethod(entity.getClass(), methodName);
					if (getKey != null) {
						for (T element : list) {
							if (seenSet.add((K) getKey.invoke(element))) {
								transitList.add(element);
							}
						}
					}
				} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return transitList;
		}
	}

	public static <T> List<T> uniquefy(List<T> list) {
		List<T> transitList = new ArrayList<>();
		Set<T> seenSet = new HashSet<>();
		for (T element : list) {
			if (seenSet.add(element)) {
				transitList.add(element);
			}
		}
		return transitList;
	}

	public static <T> T coalesce(T value, T defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public static String stringify(Object object) {
		return object == null ? "" : String.valueOf(object);
	}

	public static String strip(Object value) {
		return stringify(value).strip();
	}

	public static String stripText(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return str.strip();
	}

	public static boolean isNotBlank(Object object) {
		return object != null && object.toString() != null && !object.toString().isBlank();
	}

	public static boolean isNotBlank(Object... array) {
		for (Object object : array) {
			if (!isNotBlank(object)) {
				return false;
			}
		}
		return true;
	}

	public static String escapeSQLLike(String escape, String sql) {
		return sql.replace("%", escape + "%").replace("_", escape + "_");
	}

	public static String findSQLLikeEscape(String s) {
		for (String candidate : new String[] { "!", "^", "#", "|", "~", "$", "-", "_", "?" }) {
			if (!s.contains(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	public static String hashPassword(String plaintext) {
		return BCrypt.hashpw(plaintext, BCrypt.gensalt(12));
	}

	public static boolean checkPassword(String candidate, String hashed) {
		return BCrypt.checkpw(candidate, hashed);
	}

	public static Map<String, Object> makeMap(Object... array) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < array.length; i = i + 2) {
			map.put((String) array[i], array[i + 1]);
		}
		return map;
	}

	public static String enumerationName(Object value) {
		if (value != null) {
			return value.toString().replace("_", " ");
		}
		return "";
	}

	public static void nullifyEmptyField(Object object, Class clazz) throws Exception {
		if (clazz != null && !clazz.equals(Object.class)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getType().equals(List.class)) {
					field.setAccessible(true);
					List value = (List) field.get(object);
					if (value != null && value.isEmpty()) {
						field.set(object, null);
					}
				} else if (field.getType().equals(Map.class)) {
					field.setAccessible(true);
					Map value = (Map) field.get(object);
					if (value != null && value.isEmpty()) {
						field.set(object, null);
					}
				} else if (field.getType().equals(Set.class)) {
					field.setAccessible(true);
					Set value = (Set) field.get(object);
					if (value != null && value.isEmpty()) {
						field.set(object, null);
					}
				}
			}
			nullifyEmptyField(object, clazz.getSuperclass());
		}
	}

	public static void nullifyEmptyField(Object object) {
		try {
			nullifyEmptyField(object, object.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void nullify(Object entity, String... fieldArray) {
		PropertyUtilsBean propertyUtility = BeanUtility.instance().getPropertyUtils();
		for (String field : fieldArray) {
			try {
				Object property = propertyUtility.getProperty(entity, field);
				boolean empty = false;
				if (property instanceof List) {
					empty = ((List) property).isEmpty();
				} else if (property instanceof Map) {
					empty = ((Map) property).isEmpty();
				} else if (property instanceof Set) {
					empty = ((Set) property).isEmpty();
				} else if (property instanceof String) {
					empty = ((String) property).isBlank();
				}
				if (empty) {
					BeanUtility.instance().setProperty(entity, field, null);
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	public static String format(DateTimeFormatter formatter, TemporalAccessor value) {
		return value == null ? null : formatter.format(value);
	}

	public static boolean isTrue(Boolean expression) {
		return Boolean.TRUE.equals(expression);
	}

	public static String csvField(Object object) {
		if (object == null) {
			return "";
		} else {
			String text = String.valueOf(object);
			return "\"" + text.replace("\"", "\"\"") + "\"";
		}
	}

	public static <T> String join(T[] array, String delimiter, String open, String close) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T t : array) {
			if (!first) {
				sb.append(delimiter);
			} else {
				first = false;
			}
			if (open != null) {
				sb.append(open);
			}
			if (t != null) {
				sb.append(t);
			}
			if (close != null) {
				sb.append(close);
			}
		}
		return sb.toString();
	}

	public static <T> String join(T[] array, String delimiter) {
		return join(array, delimiter, null, null);
	}

	public static <T> String join(List<T> list, String delimiter, String open, String close) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T t : list) {
			if (!first) {
				sb.append(delimiter);
			} else {
				first = false;
			}
			if (open != null) {
				sb.append(open);
			}
			if (t != null) {
				sb.append(t);
			}
			if (close != null) {
				sb.append(close);
			}
		}
		return sb.toString();
	}

	public static <T> String join(List<T> list, String delimiter) {
		return join(list, delimiter, null, null);
	}

	public static String repeat(String element, int multiplier, String delimiter) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < multiplier; i++) {
			list.add(element);
		}
		return join(list, ",");
	}

	public static void copy(Object source, Object destination, String... array) throws Exception {
		for (String field : array) {
			BeanUtility.instance().copyProperty(destination, field, BeanUtility.instance().getProperty(source, field));
		}
	}

}
