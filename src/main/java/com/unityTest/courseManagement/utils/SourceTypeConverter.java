package com.unityTest.courseManagement.utils;

import com.unityTest.courseManagement.entity.SourceType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts source types to upper case when used in api calls. Allows use of lower case conventions
 * in path naming.
 */
@Component
public class SourceTypeConverter implements Converter<String, SourceType> {
	@Override
	public SourceType convert(String value) {
		return SourceType.valueOf(value.toUpperCase());
	}
}
