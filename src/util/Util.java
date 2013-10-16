package util;

public class Util {
	/**
	 * @deprecated
	 * @param array1
	 * @param x_1
	 * @param y_1
	 * @param array2
	 * @param x_2
	 * @param y_2
	 * @param height
	 * @param width
	 * @throws Exception
	 */
	public static <T> void copyArray(T[][] array1, int x_1, int y_1,
			T[][] array2, int x_2, int y_2, int height, int width)
			throws Exception {
		if (!checkInside(x_1, y_1, array1.length, array1[0].length))
			throw new Exception("Bottom left corner is out of array 1");
		if (!checkInside(x_1 + height - 1, y_1 + width - 1, array1.length,
				array1[0].length))
			throw new Exception("Top right corner is out of array 1");
		if (!checkInside(x_2, y_2, array2.length, array2[0].length))
			throw new Exception("Bottom left corner is out of array 2");
		if (!checkInside(x_2 + height - 1, y_2 + width - 1, array2.length,
				array2[0].length))
			throw new Exception("Top right corner is out of array 2");
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				array2[x_2 + i][y_2 + j] = array1[x_1 + i][y_1 + j];
	}

	/**
	 * Check if a position at (x,y) is inside a rectangle size (height, width)
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 * @return
	 */
	public static boolean checkInside(int x, int y, int height, int width) {
		if (x >= height || x < 0 || y >= width || y < 0)
			return false;
		return true;
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] getNZero(int n) {
		byte[] result = new byte[n];
		for (int i = 0; i < n; i++) {
			result[i] = 0;
		}
		return result;
	}
}
