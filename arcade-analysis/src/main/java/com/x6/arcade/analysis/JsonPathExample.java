package com.x6.arcade.analysis;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class JsonPathExample {
   
    public static Object parseJsonPathValue(String jsonString, String jsonPathExpression) {
        try {
            // 当表达式为空时，直接返回原始字符串
            if (jsonPathExpression.isEmpty()||jsonPathExpression.equals("$")) {
                return jsonString;
            }
            DocumentContext document = JsonPath.parse(jsonString);
            Object result = document.read(jsonPathExpression);
            return result;
        } catch (PathNotFoundException e) {
            // 如果路径不存在，处理特殊情况
            DocumentContext document = JsonPath.parse(jsonString);
            // 截取第一个 "data" 之前的部分作为新的 JSONPath 表达式解析
            String newData = document.read(jsonPathExpression.substring(0, jsonPathExpression.indexOf("data") + 4),String.class);
            // 使用 "data" 后面的表达式解析新的 JSON 对象
            Object result = JsonPath.parse(newData).read("$"+jsonPathExpression.substring(jsonPathExpression.indexOf("data") + 4));
            return result;
        }
    }

    public static boolean compareValues(Object actualValue, String operator, Object expectedValue) {
        switch (operator) {
            case "==":
                return actualValue.toString().equals(expectedValue.toString());
            case "!=":
                return !actualValue.toString().equals(expectedValue.toString());
            case "<":
                return compareComparable(actualValue, expectedValue) < 0;
            case "<=":
                return compareComparable(actualValue, expectedValue) <= 0;
            case ">":
                return compareComparable(actualValue, expectedValue) > 0;
            case ">=":
                return compareComparable(actualValue, expectedValue) >= 0;
            case "contains":
                return containsValue(actualValue, expectedValue);
            case "startsWith":
                return startsWithValue(actualValue, expectedValue);
            case "endsWith":
                return endsWithValue(actualValue, expectedValue);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static int compareComparable(Object value1, Object value2) {
        if (value1 instanceof Comparable && value2 instanceof Comparable) {
            return ((Comparable) value1).compareTo(value2);
        }
        throw new IllegalArgumentException("Cannot compare values of different types");
    }

    private static boolean containsValue(Object actualValue, Object expectedValue) {
        if (actualValue instanceof String && expectedValue instanceof String) {
            return ((String) actualValue).contains((String) expectedValue);
        }
        throw new IllegalArgumentException("Values must be of type String for 'contains' comparison");
    }

    private static boolean startsWithValue(Object actualValue, Object expectedValue) {
        if (actualValue instanceof String && expectedValue instanceof String) {
            return ((String) actualValue).startsWith((String) expectedValue);
        }
        throw new IllegalArgumentException("Values must be of type String for 'startsWith' comparison");
    }

    private static boolean endsWithValue(Object actualValue, Object expectedValue) {
        if (actualValue instanceof String && expectedValue instanceof String) {
            return ((String) actualValue).endsWith((String) expectedValue);
        }
        throw new IllegalArgumentException("Values must be of type String for 'endsWith' comparison");
    }

    // 将json字符串中变量名替换为变量值
    public static String replaceVariable(String json, Map<String, Object> params,Long taskId) {
        // 获取json中的变量名
        Set<String> variableNames = JsonPathExample.findVariables(json);
        if (variableNames == null || variableNames.isEmpty()) {
            return json;
        }
        // 遍历变量名，将变量名替换为变量值
        for (String variableName : variableNames) {
            Object variableValue = params.get(taskId+variableName);
            if (variableValue != null) {
                json = json.replace("${" + variableName + "}", variableValue.toString());
                log.info("替换变量名为变量值：{}={}", variableName, variableValue);
            }
        }
        return json;
    }

    public static Set<String> findVariables(String input) {
        Set<String> variables = new HashSet<>();
        Pattern pattern = Pattern.compile("\\$\\{([^{}]+)}");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String variable = matcher.group(1);
            variables.add(variable);
        }
        return variables;
    }
}
