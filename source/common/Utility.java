package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	public static String stringify(String text) {
		return text == null ? "" : text;
	}

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

	public static List<String> listify(String... array) {
		List<String> list = new ArrayList<>();
		for (String s : array) {
			list.add(s);
		}
		return list;
	}
	
}
