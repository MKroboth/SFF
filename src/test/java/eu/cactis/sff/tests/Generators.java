package eu.cactis.sff.tests;

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import com.mifmif.common.regex.Generex;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class Generators {
    public static Stream<String> identifierGenerator() {
        Generex identifierRegex = new Generex("[A-Za-z\\-][A-Za-z0-9\\-]*");

        return Stream.generate(identifierRegex::random);
    }

    public static Stream<String> textContentGenerator() {
        Generex textContentRegex = new Generex("([^\n\r\uE000-\uF8FF] ?)*");

        return Stream.generate(textContentRegex::random).map(String::trim);
    }

    public static Stream<String> textBlockContentGenerator() {
        Generex textContentRegex = new Generex("([^<>\uE000-\uF8FF] ?)*");

        return Stream.generate(textContentRegex::random).map(String::trim);
    }

    public static Stream<List<String>> propertyGenerator() {
        Generex propertyRegex = new Generex("([^)] ?)*");

        return Stream.generate(propertyRegex::random).map(x -> x.split(" ")).map(Arrays::asList);
    }

    public static Stream<Map<String, String>> attributeGenerator() {
        Generex amapEntryRegex = new Generex("[^),:]");

        return Stream.generate(() -> {
           int amount = (new Random().nextInt() % 10) + 1;
           return Streams.zip(Stream.generate(amapEntryRegex::random),
                    Stream.generate(amapEntryRegex::random), Collections::singletonMap)
                    .limit(amount)
                    .reduce(Collections.emptyMap(), new BinaryOperator<Map<String, String>>() {
                        @Override
                        public Map<String, String> apply(Map<String, String> stringStringMap, Map<String, String> stringStringMap2) {
                            Map<String, String> ret = new Hashtable<>();
                            ret.putAll(stringStringMap);
                            ret.putAll(stringStringMap2);
                            return ret;
                        }
                    });

        });
    }
}
