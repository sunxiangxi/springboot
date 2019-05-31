package com.ahgj.community.canal.utils;

import com.ahgj.community.canal.request.exception.GjSystemException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 *
 * 业务异常工具类,语法类似junit断言语法
 * @author:Hohn
 */
public class AssertUtil {
	private AssertUtil() {
	}
	/**
	 * 断定目标值为false.为false则抛出业务异常
	 * @param expression
	 * @param message
	 * @param code
	 * @throws GjSystemException
     */
	public static void isFalseExecution(boolean expression, String message,int code) {
		if (!expression) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 断定目标值为true.为true则抛出业务异常
	 * @param expression
	 * @param message
	 * @param code
	 * @throws GjSystemException
	 */
	public static void isTrueExecution(boolean expression, String message,int code) {
		if (expression) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 断定目标值为null.为null则抛出业务异常
	 * @param obj
	 * @param message
	 * @param code
	 * @throws GjSystemException
	 */
	public static void isNull(Object obj, String message,int code) {
		if (obj == null) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 断定目标值不为null.不为null则抛出业务异常
	 * @param obj
	 * @param message
	 * @param code
	 */
	public static void notNull(Object obj, String message,int code) {
		if (obj != null) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 断定目标list不为空.为空则抛出业务异常
	 * @param collection
	 * @param message
	 * @param code
     */
	public static void isEmpty(Collection<?> collection, String message,int code) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new GjSystemException(message,code);
		}
	}
	/**
	 * 断定目标list为空.不为空则抛出业务异常
	 * @param collection
	 * @param message
	 * @param code
	 */
	public static void isNotEmpty(Collection<?> collection, String message,int code) {
		if (CollectionUtils.isNotEmpty(collection)) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 断定目标字符串不为空.为空则抛出业务异常
	 * @param string
	 * @param message
	 * @param code
     */
	public static void notEmpty(String string, String message,int code) {
		
		if (StringUtils.isBlank(string)) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 断定目标值为不为null.为null则抛出业务异常
	 * @param integer
	 * @param message
	 * @param code
	 * @throws GjSystemException
	 */
	public static void notNull(Integer integer, String message,int code) {
		if (integer == null ) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 断定目标值为不为null并且不等与0.为null则抛出业务异常
	 * @param integer
	 * @param message
	 * @param code
	 * @throws GjSystemException
	 */
	public static void notEmpty(Integer integer, String message,int code) {
		if (integer == null || integer ==0) {
			throw new GjSystemException(message,code);
		}
	}


	/**
	 * 断定目标字符串为空.不为空则抛出业务异常
	 * @param string
	 * @param message
	 * @param code
	 */
	public static void empty(String string, String message,int code) {
		
		if (StringUtils.isNotBlank(string)) {
			throw new GjSystemException(message,code);
		}
	}

	/**
	 * 根据sql返回结果断定新增,修改,删除执行不成功
	 * @param result
	 * @param message
	 * @param code
	 */
	public static void intExecuteFailure(int result, String message,int code) {
		
		if (result <=0) {
			throw new GjSystemException(message,code);
		}
	}

}
