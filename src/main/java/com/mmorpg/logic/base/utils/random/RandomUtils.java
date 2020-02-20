package com.mmorpg.logic.base.utils.random;

import com.google.common.base.Preconditions;
import com.koloboke.function.Predicate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具
 *
 * @author Ariescat
 * @version 2020/2/20 15:09
 */
public class RandomUtils {

	/**
	 * 在[min, max)区间产生一个随机数，并比较num是否大于该随机数。
	 *
	 * @param num
	 * @param min 大于等于0
	 * @param max 大于min
	 * @return 当num大于在[min, max)区间产生一个随机数时，返回true。
	 * 当num小于等于在[min, max)区间产生一个随机数时，返回false。
	 */
	public static boolean isLarger(int num, int min, int max) {
		int rd = generateBetween(min, max);
		return num > rd;
	}


	/**
	 * 从[min, max]区间随机产生一个数，包括上限
	 */
	public static int generateBetweenIncludeMax(int min, int max) {
		return generateBetween(min, max + 1);
	}

	/**
	 * 在[min, max)区间产生个随机数
	 *
	 * @param min 大于等于0
	 * @param max 大于min
	 */
	public static int generateBetween(int min, int max) {
		if (min == max) {
			return min;
		}
		Preconditions.checkArgument(min >= 0, "argument min[%s] should not be negative ", min);
		Preconditions.checkArgument(min <= max, "argument min[%s] can not larger than max[%s]", min, max);
		return ThreadLocalRandom.current().nextInt(max - min) + min;
	}

	/**
	 * 在[min, max]区间产生个随机数
	 *
	 * @param min 大于等于O
	 * @param max 大于min
	 */
	public static int randomBothInclude(int min, int max) {
		if (min < 0) {
			throw new IllegalArgumentException("argument min should not be positive");
		}
		if (min > max) {
			throw new IllegalArgumentException("argument min can not larger than max.");
		}
		if (min == max) {
			return min;
		}
		return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
	}

	public static int nextInt(int i) {
		return ThreadLocalRandom.current().nextInt(i);
	}

	public static int nextInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	public static float nextFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}

	public static boolean nextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	/**
	 * 随机值是否处在[0, rate)的范围内
	 *
	 * @param rate     概率区间值
	 * @param baseRate 随机值最大值
	 */
	public static boolean isInTheRange(int rate, int baseRate) {
		if (rate >= baseRate) {
			return true;
		}
		int random = generateBetween(0, baseRate);
		return random < rate;
	}

	/**
	 * 单随机是否成功，以10000为基数
	 *
	 * @param pro 概率
	 * @return
	 */
	public static boolean isInTheRange(int pro) {
		return isInTheRange(pro, 10000);
	}

	/**
	 * 返回随机概率的某个下标
	 *
	 * @param rates
	 * @return
	 */
	public static int getRandomIndex(int[] rates) {
		int sum = 0;
		for (int rate : rates) {
			sum += rate;
		}
		int random = ThreadLocalRandom.current().nextInt(sum);
		sum = 0;
		for (int i = 0; i < rates.length; i++) {
			sum += rates[i];
			if (random < sum)
				return i;
		}
		return -1; //不可能发生了
	}

	/**
	 * 返回随机概率的某个下标
	 *
	 * @param rates
	 * @return
	 */
	public static int getRandomIndex(final List<? extends ProEntity> rates) {
		int sum = 0;
		for (ProEntity rate : rates) {
			sum += rate.getPro();
		}

		int random = generateBetweenIncludeMax(1, sum);
		int rateSum = 0;
		for (int i = 0; i < rates.size(); i++) {
			rateSum += rates.get(i).getPro();
			if (random <= rateSum)
				return i;
		}
		return -1; //不可能发生了
	}

	/**
	 * 返回随机概率的某个下标
	 *
	 * @param predicate null 选择所有或者测试通不过的
	 */
	public static <T extends ProEntity> int getRandomIndex(final List<T> rates, Predicate<T> predicate) {
		int sum = 0;
		for (T rate : rates) {
			if (predicate == null || predicate.test(rate)) {
				sum += rate.getPro();
			}
		}

		int random = generateBetweenIncludeMax(1, sum);
		int rateSum = 0;
		for (int i = 0; i < rates.size(); i++) {
			T t = rates.get(i);
			if (predicate == null || predicate.test(t)) {
				rateSum += t.getPro();
				if (random <= rateSum)
					return i;
			}
		}
		return -1;// 不可能发生了
	}

	private static final Comparator<ProEntity> proComparator = new Comparator<ProEntity>() {
		@Override
		public int compare(ProEntity o1, ProEntity o2) {
			return o1.getPro() - o2.getPro();
		}
	};

	/**
	 * 从输入列表中随机个
	 *
	 * @param unSortedList 未按概率排序列表
	 */
	public static <T extends ProEntity> T randUnSortedList(List<T> unSortedList) {
		List<T> sortedList = sort(unSortedList);
		return randSortedList(sortedList);
	}

	/**
	 * 从输入列表中随机一个
	 *
	 * @param sortedList 按概率排序列表
	 */
	public static <T extends ProEntity> T randSortedList(List<T> sortedList) {
		int randValue = ThreadLocalRandom.current().nextInt(10000);
		for (T entity : sortedList) {
			if (randValue < entity.getPro()) {
				return entity;
			}
		}
		return null;
	}

	public static <T extends ProEntity> List<T> sort(List<T> unSortedList) {
		List<T> sortList = new ArrayList<>(unSortedList);
		Collections.sort(sortList, proComparator);
		return sortList;
	}

	/**
	 * 等概率随机一个
	 *
	 * @param randList
	 * @return
	 */
	public static <T> T randEqualPro(List<T> randList) {
		if (randList.size() == 0) {
			return null;
		}
		int index = ThreadLocalRandom.current().nextInt(randList.size());
		return randList.get(index);
	}

	/**
	 * 根据权重随机Key
	 *
	 * @param weightMap value为权重
	 */
	public static Integer randomMapKey(Map<Integer, Integer> weightMap) {
		int sum = 0;
		for (Integer rate : weightMap.values()) {
			sum += rate;
		}

		Integer configID = null;
		int random = generateBetweenIncludeMax(1, sum);
		int rateSum = 0;
		for (Map.Entry<Integer, Integer> entry : weightMap.entrySet()) {
			rateSum += entry.getValue();
			if (random <= rateSum) {
				configID = entry.getKey();
				break;
			}
		}
		return configID;
	}

}
