/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author liubo
 * @date:2021/3/25
 *
 */
public class ValidatorUtil {

	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	/**
	 * 验证实体字段信息的有效性，全部检查通过则返回信息为空
	 * @param t 实体
	 * @return 
	 */
	public static <T> String validate(T t) {
		String message = "";
		String lineSeparator = System.getProperty("line.separator");
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			message += constraintViolation.getMessage() + lineSeparator;
		}
		if (message.endsWith(lineSeparator)) {
			message = message.substring(0, message.length() - lineSeparator.length());
		}
		return message;
	}

}
