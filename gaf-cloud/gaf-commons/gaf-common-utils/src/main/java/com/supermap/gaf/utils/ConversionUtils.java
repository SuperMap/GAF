/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author fmq
 * @date:2021/3/25
 * @category 类型转换工具类
 */
public class ConversionUtils {
	private static Logger logger = LoggerFactory.getLogger(ConversionUtils.class);

	/**
	 * 其他类转字符串
	 *
	 * @param value
	 * @return
	 */
	public static String convertToString(Object value) {
		String newValue = StringUtils.EMPTY;
		if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
			newValue = String.valueOf(value);
		} else if (value instanceof Character) {
			newValue = String.valueOf(value);
		} else if (value instanceof Date) {
			newValue = DateUtils.dateToString((Date) value);
		} else if (value instanceof Double) {
			newValue = String.valueOf(BigDecimal.valueOf(Double.parseDouble(value.toString())));
		} else if (value instanceof Long) {
			newValue = String.valueOf(BigDecimal.valueOf(Double.parseDouble(value.toString())));
		} else if (value instanceof Boolean) {
			newValue = String.valueOf(value);
		} else {
			newValue = String.valueOf(value);
		}
		return newValue;
	}

	/**
	 * 转日期类
	 *
	 * @param value
	 * @return
	 */
	public static Date convertToDateTime(Object value) {
		Date date = null;
		if (value instanceof Integer&&(int) value >= 10000000 && (int) value < 100000000) {
				date = DateUtils.stringToDate(String.valueOf(value), "yyyyMMdd");
		} else if (value instanceof Long&&(Long) value >= 10000000000000L && (Long) value < 100000000000000L) {
				date = DateUtils.stringToDate(String.valueOf(value), "yyyyMMddHHmmss");
		} else if (value instanceof String) {
			String v = (String) value;
			date = DateUtils.stringToDate(v);
		}
		return date;
	}

	/**
	 * 其他类型转double
	 *
	 * @param value
	 * @return || value instanceof Short || value instanceof Integer
	 */
	public static Double convertToDouble(Object value) {
		if (value instanceof Byte) {
			byte v = (byte) value;
			double newValue = (double) v;
			return newValue;
		} else if (value instanceof Short) {
			short v = (short) value;
			double newValue = (double) v;
			return newValue;
		} else if (value instanceof Integer) {
			int v = (int) value;
			double newValue = (double) v;
			return newValue;
		} else if (value instanceof Long) {
			long v = (long) value;
			double newValue = (double) v;
			return newValue;
		} else {
			double newValue = Double.valueOf((String) value);
			return newValue;
		}
	}

	/**
	 * 其他类型转int
	 *
	 * @param value
	 * @return
	 */
	public static int convertToInt(Object value) {
		if (value instanceof String) {
			int newValue = Integer.valueOf((String) value);
			return newValue;
		} else if (value instanceof Date) {
			int newValue = Integer.valueOf(DateUtils.dateToString((Date) value, "yyyyMMdd"));
			return newValue;
		}else if (value instanceof Float) {
			int newValue = Math.round((float) value);
			return newValue;
		} else if (value instanceof Boolean) {
			if ((boolean) value == true) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 其他类型转long
	 *
	 * @param value
	 * @return
	 */
	public static Long convertToLong(Object value) {
		if (value instanceof String) {
			Long newValue = Long.valueOf((String) value);
			return newValue;
		} else if (value instanceof Date) {
			Long newValue = Long.valueOf(DateUtils.dateToString((Date) value, "yyyyMMdd"));
			return newValue;
		} else if (value instanceof Boolean) {
			if ((boolean) value == true) {
				return 1L;
			} else {
				return 0L;
			}
		} else if (value instanceof Integer) {
			int v = (int) value;
			Long newValue = (long) v;
			return newValue;
		}
		return null;
	}

	/**
	 * 其他类型转byte
	 *
	 * @param value
	 * @return
	 */
	public static Byte convertToByte(Object value) {
		if (value instanceof String) {
			Byte newValue = Byte.valueOf((String) value);
			return newValue;
		} else if (value instanceof Short) {
			short v = (short) value;
			byte newValue = (byte) v;
			return newValue;
		} else if (value instanceof Integer) {
			int v = (int) value;
			byte newValue = (byte) v;
			return newValue;
		} else if (value instanceof Boolean) {
			if ((boolean) value == true) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * Object转Boolean工具类
	 *
	 * @param value
	 * @return
	 */
	public static boolean convertToBoolean(Object value) {
		try {
			if (value.equals(1) || value.equals("1") || ((String) value).equalsIgnoreCase("t") || value.equals("是")
					|| ((String) value).equalsIgnoreCase("yes") || ((String) value).equalsIgnoreCase("y")) {
				return true;
			} else if (value.equals(0) || value.equals("0") || ((String) value).equalsIgnoreCase("f")
					|| value.equals("否") || ((String) value).equalsIgnoreCase("false")
					|| ((String) value).equalsIgnoreCase("n")) {
				return false;
			}
			return Boolean.parseBoolean(value.toString());
		} catch (Exception e) {
			logger.error("不能正确转换为boolean值类型" + e.getMessage(), e);
			return false;
		}
	}

}
