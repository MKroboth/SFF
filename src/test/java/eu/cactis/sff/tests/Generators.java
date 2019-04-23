package eu.cactis.sff.tests;

import com.mifmif.common.regex.Generex;

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
}
