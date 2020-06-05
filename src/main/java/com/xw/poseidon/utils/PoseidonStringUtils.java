package com.xw.poseidon.utils;

import org.apache.commons.lang3.StringUtils;

public final class PoseidonStringUtils extends StringUtils {
	
	public static void main(String[] args) {
		String str = "YueWuChou";
		System.out.println(PoseidonStringUtils.capitalize(str));
		System.out.println(PoseidonStringUtils.uncapitalize(str));
	}
}
