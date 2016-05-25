package preprocessing;

import java.util.*;

import util.Times;

public class DataExtractor {
	
	public static double[][] getValueMatrix(List<String[]>content){
		return getValueMatrix(content, null);
	}
	
	public static double[][] getValueMatrix(List<String[]>content,String timeLimit){
		return getValueMatrix(content, timeLimit, -1);
	}
	
	public static double[][] getValueMatrix(List<String[]>content,String timeLimit,int min){
		List<String> studentIDs = getValuesAt(content, 0);
		List<String> courseIDs = min!=-1 ? getSufficientCourses(content, min) : getValuesAt(content, 1);
		return getValueMatrix(content, studentIDs, courseIDs, timeLimit);
	}

	public static double[][] getValueMatrix(List<String[]> content,
			List<String> studentIDs, List<String> courseIDs, String timeLimit) {
		double[][] matrix = new double[studentIDs.size()][courseIDs.size()];
    // Fill with -1 to keep track of missing grades
    for (int i = 0; i < matrix.length; i++) {
      Arrays.fill(matrix[i], -1);
    }

		int[][] times = new int[studentIDs.size()][courseIDs.size()];
		int limit = timeLimit == null ? -1 : Times.parse(timeLimit);
		for (String[] line : content) {
			if(!courseIDs.contains(line[1]))
				continue;
			int row = studentIDs.indexOf(line[0]);
			int col = courseIDs.indexOf(line[1]);
      double grade = Double.parseDouble(line[2]);
			int time = Times.parse(line[3]);

			if (times[row][col] == 0) {
				times[row][col] = time;
				matrix[row][col] = grade;
				continue;
			}

			if (times[row][col] < time && (time == -1 || time <= limit)) {
				times[row][col] = time;
				matrix[row][col] = grade;
			}
		}
		return matrix;
	}

	public static List<String> getSufficientCourses(List<String[]> content,
			int min) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String[] line : content) {
			String key = line[1];
			if (map.containsKey(key))
				map.put(key, map.get(key) + 1);
			else
				map.put(key, 1);
		}
		List<String> list = new ArrayList<String>();
		for (String key : map.keySet())
			if (map.get(key) >= min)
				list.add(key);

		Collections.sort(list);
		return list;
	}

	public static List<String> getValuesAt(List<String[]> content, int index) {
		List<String> list = new ArrayList<String>();
		for (String[] line : content) {
			String id = line[index];
			if (!list.contains(id))
				list.add(id);
		}
		Collections.sort(list);
		return list;
	}
}
