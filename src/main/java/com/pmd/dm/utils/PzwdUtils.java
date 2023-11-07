package com.pmd.dm.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PzwdUtils {

	public String generatePzwd() {

		return RandomStringUtils.randomAlphanumeric(5);

	}

}
