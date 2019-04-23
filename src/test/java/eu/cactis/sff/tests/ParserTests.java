package eu.cactis.sff.tests;

/*-
 * #%L
 * Cactis SFF
 * %%
 * Copyright (C) 2019 Maximilian Kroboth
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.google.common.collect.Streams;
import com.mifmif.common.regex.Generex;
import eu.cactis.sff.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class ParserTests {

    public static final int GENERATOR_LIMIT = 100;

    public static String toHexString(String str) {
        StringBuilder sb = new StringBuilder();
        for (byte b : str.getBytes(StandardCharsets.UTF_8)) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static Stream<String> identifierSource() {
        return Generators.identifierGenerator().limit(GENERATOR_LIMIT);
    }

    public static Stream<String> textSource() {
        return Generators.textContentGenerator().limit(GENERATOR_LIMIT);
    }

    public static Stream<String> textBlockSource() {
        return Generators.textBlockContentGenerator().limit(GENERATOR_LIMIT);
    }

    public static Stream<Arguments> twoIdentifiersSource() {
        return Streams.zip(Generators.identifierGenerator(), Generators.identifierGenerator(), (String a, String b) -> arguments(a, b));
    }

    public static Stream<Arguments> identifierTextArgumentSource() {
        return Streams.zip(identifierSource(), textSource(), (String a, String b) -> arguments(a, b));
    }

    public static Stream<Arguments> identifierBTextArgumentSource() {
        return Streams.zip(identifierSource(), textBlockSource(), (String a, String b) -> arguments(a, b));
    }


    @ParameterizedTest
    @MethodSource("identifierTextArgumentSource")
    void testPropertyParsing(String propertyName, String propertyValue) {
        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append("=");
        content.append(propertyValue);
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();

        assertArrayEquals(propertyName.getBytes(), nd.getName().getBytes());
        assertArrayEquals(propertyValue.getBytes(), nd.getContent().getBytes());
    }

    @ParameterizedTest
    @MethodSource("textSource")
    void testCommentParsing(String propertyValue) {
        StringBuilder content = new StringBuilder();

        content.append("#");
        content.append(propertyValue);
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof CommentNode);
        CommentNode nd = (CommentNode) doc.getNodes().iterator().next();
        assertArrayEquals(propertyValue.getBytes(), nd.getContent().getBytes());

    }

    @ParameterizedTest
    @MethodSource("identifierTextArgumentSource")
    void testProcessingInstructionParsing(String propertyName, String propertyValue) {
        StringBuilder content = new StringBuilder();

        content.append("@");
        content.append(propertyName);
        content.append(" ");
        content.append(propertyValue);
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof ProcessingInstructionNode);
        ProcessingInstructionNode nd = (ProcessingInstructionNode) doc.getNodes().iterator().next();
        assertArrayEquals(propertyName.getBytes(), nd.getName().getBytes());
        assertArrayEquals(propertyValue.getBytes(), nd.getContent().getBytes());


    }

    @ParameterizedTest
    @MethodSource("textSource")
    void testTextParsing(String propertyValue) {
        StringBuilder content = new StringBuilder();

        content.append("<");
        content.append(propertyValue);
        content.append(">");
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof TextNode);
        TextNode nd = (TextNode) doc.getNodes().iterator().next();
        assertArrayEquals(propertyValue.getBytes(), nd.getContent().getBytes());
    }

    @ParameterizedTest
    @MethodSource("identifierBTextArgumentSource")
    void testGroupedTextParsing(String propertyName, String propertyValue) {
        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append("<");
        content.append(propertyValue);
        content.append(">");
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertArrayEquals(propertyName.getBytes(), nd.getName().getBytes());
        assertEquals(1, nd.getChildren().size());
        assertTrue(nd.getChildren().get(0) instanceof TextNode);

        TextNode cnt = (TextNode) nd.getChildren().get(0);

        assertArrayEquals(propertyValue.getBytes(), cnt.getContent().getBytes());
    }

    @ParameterizedTest
    @MethodSource("identifierSource")
    void testEmptyGroupParsing(String propertyName) {
        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append("{}");
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
    }

    @ParameterizedTest
    @MethodSource("identifierSource")
    void testSimpleGroupParsing(String propertyName) {
        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append("{}");
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);
        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
    }

    @Test
    @Disabled("Not implemented")
    void testComplexGroupParsing() {
    }

    @Test
    @Disabled("Not implemented")
    void testCompleteParsing() {
    }
}
