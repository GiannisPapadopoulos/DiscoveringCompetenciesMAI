package preprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataReader {

	public static List<String[]> readContent() throws FileNotFoundException {
//		InputStream in = ClassLoader.getSystemResourceAsStream(Config
//				.getProperty("file"));
//		System.out.println(Config.getProperty("file"));
    Scanner scanner = new Scanner(new File("input/Parsed.csv"));

		List<String> content = new ArrayList<String>();
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (!content.contains(line))
				content.add(line);
		}
		scanner.close();
		return splitAll(content);
	}

	private static List<String[]> splitAll(List<String> content) {
		List<String[]> splits = new ArrayList<String[]>();
		for (String line : content)
			splits.add(line.split(","));
		return splits;
	}

}
