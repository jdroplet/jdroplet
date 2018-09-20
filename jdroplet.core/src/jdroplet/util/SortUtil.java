package jdroplet.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import jdroplet.enums.SortOrder;



/**
 * 排序测试类
 * 
 * 排序算法的分类如下： 1.插入排序（直接插入排序、折半插入排序、希尔排序）； 2.交换排序（冒泡泡排序、快速排序）；
 * 3.选择排序（直接选择排序、堆排序）； 4.归并排序； 5.基数排序。
 * 
 * 关于排序方法的选择： (1)若n较小(如n≤50)，可采用直接插入或直接选择排序。
 * 当记录规模较小时，直接插入排序较好；否则因为直接选择移动的记录数少于直接插人，应选直接选择排序为宜。
 * (2)若文件初始状态基本有序(指正序)，则应选用直接插人、冒泡或随机的快速排序为宜；
 * (3)若n较大，则应采用时间复杂度为O(nlgn)的排序方法：快速排序、堆排序或归并排序。
 * 
 */
/**
 * @corporation 北京环亚
 * @author HDS
 * @date Nov 19, 2009 10:43:44 AM
 * @path sort
 * @description JAVA排序汇总(注：代码来自网友HDS，我只将其修改成接口的方法)
 */
public class SortUtil {
	
	private static Field getField(Class clazz, String fieldName) {
		Field field = null;
				
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			field = getField(clazz.getSuperclass(), fieldName);
		}
		return field;
	}
	/**
	 * @description 交换相邻两个数
	 * @date Nov 19, 2009
	 * @author HDS
	 * @param data
	 * @param x
	 * @param y
	 */
	public static void swap(Object[] data, int x, int y) {
		Object temp = data[x];
		data[x] = data[y];
		data[y] = temp;
	}

	/**
	 * 冒泡排序----交换排序的一种
	 * 方法：相邻两元素进行比较，如有需要则进行交换，每完成一次循环就将最大元素排在最后（如从小到大排序），下一次循环是将其他的数进行类似操作。
	 * 性能：比较次数O(n^2),n^2/2；交换次数O(n^2),n^2/4
	 * 
	 * @param data
	 *            要排序的数组
	 * @return
	 */
	public static void bubbleSort(Object[] datas, String fieldName, SortOrder sort) {
		Field field = null;
		
		if (datas == null)
			throw new NullPointerException();
		
		if (datas.length == 0)
			throw new IllegalArgumentException();
		
		try {
			field = getField(datas[0].getClass(), fieldName);
			field.setAccessible(true);
			
			for (int i = 1; i < datas.length; i++) { // 数组有多长,轮数就有多长
				// 将相邻两个数进行比较，较大的数往后冒泡
				if (sort == SortOrder.ASC) {
					for (int j = 0; j < datas.length - i; j++) {// 每一轮下来会将比较的次数减少
						if (field.getInt(datas[j]) > field.getInt(datas[j + 1]) ) {
							// 交换相邻两个数
							swap(datas, j, j + 1);
						}
					}
				} else {
					for (int j = 0; j < datas.length - i; j++) {// 每一轮下来会将比较的次数减少
						if (field.getInt(datas[j]) > field.getInt(datas[j + 1]) ) {
							// 交换相邻两个数
							swap(datas, j, j + 1);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Reflect " + fieldName + " error");
		}		
	}
		
	/**
	 * 直接选择排序法----选择排序的一种 方法：每一趟从待排序的数据元素中选出最小（或最大）的一个元素，
	 * 顺序放在已排好序的数列的最后，直到全部待排序的数据元素排完。 性能：比较次数O(n^2),n^2/2 交换次数O(n),n
	 * 交换次数比冒泡排序少多了，由于交换所需CPU时间比比较所需的CUP时间多，所以选择排序比冒泡排序快。
	 * 但是N比较大时，比较所需的CPU时间占主要地位，所以这时的性能和冒泡排序差不太多，但毫无疑问肯定要快些。
	 * 
	 * @param data
	 *            要排序的数组
	 * @param sortType
	 *            排序类型
	 * @return
	 */
	public static void selectSort(Object[] datas, String fieldName, SortOrder sort) {
		int index;
		Field field = null;
		
		if (datas == null)
			throw new NullPointerException();
		
		if (datas.length == 0)
			throw new IllegalArgumentException();
		
		try {
			field = getField(datas[0].getClass(), fieldName);
			field.setAccessible(true);
			for (int i = 1; i < datas.length; i++) {
				index = 0;
				for (int j = 1; j <= datas.length - i; j++) {
					
					if (sort == SortOrder.ASC) {
						if (field.getInt(datas[j]) > field.getInt(datas[index])) {
							index = j;
						}
					} else {
						if (field.getInt(datas[j]) < field.getInt(datas[index])) {
							index = j;
						}
					}
				}
				// 交换在位置data.length-i和index(最大值)两个数
				swap(datas, datas.length - i, index);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Reflect " + fieldName + " error");
		}	
	}
	

	/**
	 * 插入排序 方法：将一个记录插入到已排好序的有序表（有可能是空表）中,从而得到一个新的记录数增1的有序表。 性能：比较次数O(n^2),n^2/4
	 * 复制次数O(n),n^2/4 比较次数是前两者的一般，而复制所需的CPU时间较交换少，所以性能上比冒泡排序提高一倍多，而比选择排序也要快。
	 * 
	 * @param datas
	 *            要排序的数组
	 */
	public static void insertSort(Object[] datas, String fieldName, SortOrder sort) {
		Field field = null;
		
		if (datas == null)
			throw new NullPointerException();
		
		if (datas.length == 0)
			throw new IllegalArgumentException();
		
		try {
			field = datas[0].getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			// 比较的轮数
			for (int i = 1; i < datas.length; i++) {
				// 保证前i+1个数排好序
				for (int j = 0; j < i; j++) {
					if (sort == SortOrder.ASC) {
						if (field.getInt(datas[j]) > field.getInt(datas[i])) {
							// 交换在位置j和i两个数
							swap(datas, i, j);
						}
					} else {
						if (field.getInt(datas[j]) < field.getInt(datas[i])) {
							// 交换在位置j和i两个数
							swap(datas, i, j);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Reflect " + fieldName + " error");
		}
	}
	
	/**
	 * 反转数组的方法
	 * 
	 * @param data
	 *            源数组
	 */
	public static void reverse(Object[] data) {
		int length = data.length;
		Object temp = null;// 临时变量
		for (int i = 0; i < length / 2; i++) {
			temp = data[i];
			data[i] = data[length - 1 - i];
			data[length - 1 - i] = temp;
		}
	}

	/**
	 * 快速排序 快速排序使用分治法（Divide and conquer）策略来把一个序列（list）分为两个子序列（sub-lists）。 步骤为：
	 * 1. 从数列中挑出一个元素，称为 "基准"（pivot）， 2.
	 * 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分割之后，该基准是它的最后位置。这个称为分割（partition）操作。
	 * 3. 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
	 * 递回的最底部情形，是数列的大小是零或一，也就是永远都已经被排序好了。虽然一直递回下去，但是这个算法总会结束，因为在每次的迭代（iteration）中，它至少会把一个元素摆到它最后的位置去。
	 * 
	 * @param data
	 *            待排序的数组
	 * @param low
	 * @param high
	 * @see SortTest#qsort(int[], int, int)
	 * @see SortTest#qsort_desc(int[], int, int)
	 */
	public static void quickSort(Object[] datas, String fieldName, SortOrder sort) {
		if (datas == null)
			throw new NullPointerException();
		
		if (datas.length == 0)
			throw new IllegalArgumentException();
		
		if (sort == SortOrder.ASC)
			qsort_asc(datas, fieldName, 0, datas.length - 1);
		else
			qsort_desc(datas, fieldName, 0, datas.length - 1);
	}
	
	/**
	 * 快速排序的具体实现，排正序
	 * 
	 * @param data
	 * @param low
	 * @param high
	 */
	private static void qsort_asc(Object datas[], String fieldName, int low, int high) {
		int i, j;
		Object obj;
		Field field = null;
		
		try {
			field = getField(datas[0].getClass(), fieldName);
			field.setAccessible(true);
			
			if (low < high) { // 这个条件用来结束递归
				i = low;
				j = high;
				obj = datas[i];
				while (i < j) {
					while (i < j && field.getInt(datas[j]) > field.getInt(obj)) {
						j--; // 从右向左找第一个小于x的数
					}
					if (i < j) {
						datas[i] = datas[j];
						i++;
					}
					while (i < j && field.getInt(datas[i]) < field.getInt(obj)) {
						i++; // 从左向右找第一个大于x的数
					}
					if (i < j) {
						datas[j] = datas[i];
						j--;
					}
				}
				datas[i] = obj;
				qsort_asc(datas, fieldName, low, i - 1);
				qsort_asc(datas, fieldName, i + 1, high);
			}
		
		} catch (Exception ex) {
			throw new IllegalArgumentException("Reflect " + fieldName
					+ " error");
		}
	}

	/**
	 * 快速排序的具体实现，排倒序
	 * 
	 * @param data
	 * @param low
	 * @param high
	 */
	private static void qsort_desc(Object datas[], String fieldName, int low, int high) {
		int i, j;
		Object obj;
		Field field = null;
		
		if (datas == null)
			throw new NullPointerException();
		
		if (datas.length == 0)
			throw new IllegalArgumentException();
		
		try {
			field = getField(datas[0].getClass(), fieldName);
			field.setAccessible(true);
			
			if (low < high) { // 这个条件用来结束递归
				i = low;
				j = high;
				obj = datas[i];
				while (i < j) {
					while (i < j && field.getInt(datas[j]) < field.getInt(obj)) {
						j--; // 从右向左找第一个小于x的数
					}
					if (i < j) {
						datas[i] = datas[j];
						i++;
					}
					while (i < j && field.getInt(datas[i]) > field.getInt(obj)) {
						i++; // 从左向右找第一个大于x的数
					}
					if (i < j) {
						datas[j] = datas[i];
						j--;
					}
				}
				datas[i] = obj;
				qsort_desc(datas, fieldName, low, i - 1);
				qsort_desc(datas, fieldName, i + 1, high);
			}
		
		} catch (Exception ex) {
			throw new IllegalArgumentException("Reflect " + fieldName
					+ " error");
		}
	}

	/**
	 * 二分查找特定整数在整型数组中的位置(递归) 查找线性表必须是有序列表
	 * 
	 * @paramdataset
	 * @paramdata
	 * @parambeginIndex
	 * @paramendIndex
	 * @returnindex
	 */
	public static int binarySearch(Object[] datas, Object data,
			String fieldName, int beginIndex, int endIndex) {
		int midIndex = 0;
		Field field = null;
		
		if (datas == null || data == null)
			throw new NullPointerException();
		
		if (datas.length == 0)
			throw new IllegalArgumentException();
		
		midIndex = (beginIndex + endIndex) >>> 1; // 相当于mid = (low + high) // / 2，但是效率会高些
		
		try {
			field = getField(data.getClass(), fieldName);
			field.setAccessible(true);
			
			if (field.getInt(data) < field.getInt(datas[beginIndex])
					|| field.getInt(data) > field.getInt(datas[endIndex])
					|| beginIndex > endIndex)
				return -1;
			if (field.getInt(data) < field.getInt(datas[midIndex])) {
				return binarySearch(datas, data, fieldName, beginIndex, midIndex - 1);
			} else if (field.getInt(data) > field.getInt(datas[midIndex])) {
				return binarySearch(datas, data, fieldName, midIndex + 1, endIndex);
			} else {
				return midIndex;
			}
		} catch (Exception ex) {
			throw new IllegalArgumentException("Reflect " + fieldName
					+ " error");
		}
	}

	/**
	 * 二分查找特定整数在整型数组中的位置(非递归) 查找线性表必须是有序列表
	 * 
	 * @paramdataset
	 * @paramdata
	 * @returnindex
	 */
	public static int binarySearch(Object[] datas, Object data, String fieldName) {
		int beginIndex = 0;
		int endIndex = datas.length - 1;
		int midIndex = -1;
		Field field = null;
		
		if (datas == null || data == null)
			throw new NullPointerException();
		
		if (datas.length == 0)
			throw new IllegalArgumentException();
		
		try {
			field = getField(data.getClass(), fieldName);
			field.setAccessible(true);
			
			if (field.getInt(data) < field.getInt(datas[beginIndex]) 
					|| field.getInt(data) > field.getInt(datas[endIndex])
					|| beginIndex > endIndex)
				return -1;
			
			while (beginIndex <= endIndex) {
				midIndex = (beginIndex + endIndex) >>> 1; // 相当于midIndex =
															// (beginIndex +
															// endIndex) / 2，但是效率会高些
				if (field.getInt(data) < field.getInt(datas[midIndex])) {
					endIndex = midIndex - 1;
				} else if (field.getInt(data) > field.getInt(datas[midIndex])) {
					beginIndex = midIndex + 1;
				} else {
					return midIndex;
				}
			}
		} catch (Exception ex) {
			throw new IllegalArgumentException("Reflect " + fieldName + " error");
		}
		return -1;
	}
	
	public static void randomSort(List list) {
		Random random = new Random();
		int length = list.size();
		int p = 0;
		Object temp = null;
		
		for (int idx = 0; idx < length; idx++) {
			p = random.nextInt(length);
			temp = list.get(idx);
			list.set(idx, list.get(p));
			list.set(p, temp);
		}
	}
	
	public static void randomSort(Object[] data) {
		Random random = new Random();
		int length = data.length;
		int p = 0;
		Object temp = null;
		
		for (int idx=0; idx<length; idx++) {
			p = random.nextInt(length);
			temp = data[idx];
			data[idx] = data[p];
			data[p] = temp;
		}
	}
}