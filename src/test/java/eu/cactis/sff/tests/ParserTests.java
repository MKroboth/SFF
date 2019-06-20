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
import eu.cactis.sff.*;
import eu.cactis.sff.test_utility.Generators;
import eu.cactis.sff.test_utility.Pair;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class ParserTests {

    private static eu.cactis.sff.test_utility.Generators Generators;

    @BeforeAll
    public static void initializeGenerator() {
        Generators = new Generators();
    }

    private static final int SINGLE_CHILD = 1;
    public static final int GENERATOR_LIMIT = 500;

    public static Stream<String> identifierSource() {
        return Generators.identifierGenerator().map(String::trim).limit(GENERATOR_LIMIT);
    }

    public static Stream<String> textSource() {
        return Generators.textContentGenerator().map(String::trim).limit(GENERATOR_LIMIT);
    }

    public static Stream<String> textBlockSource() {
        return Generators.textBlockContentGenerator().map(String::trim).limit(GENERATOR_LIMIT);
    }

    public static Stream<List<String>> propertySource() {
        return Generators.propertyGenerator().limit(GENERATOR_LIMIT);
    }

    public static Stream<Map<String, String>> attributeSource() {
        return Generators.attributeGenerator().limit(GENERATOR_LIMIT);
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

    public static Stream<Arguments> identifierTextPropertySource() {
        return Streams.zip(Streams.zip(identifierSource(), textSource(), Pair::of), propertySource(), (itPair, prop) -> arguments(itPair.getLeft(), itPair.getRight(), prop));
    }

    public static Stream<Arguments> identifierTextAttributeSource() {
        return Streams.zip(Streams.zip(identifierSource(), textSource(), Pair::of), attributeSource(), (itPair, prop) -> arguments(itPair.getLeft(), itPair.getRight(), prop));
    }

    public static Stream<Arguments> identifierTextPropertyAttributeSource() {
        return Streams.zip(Streams.zip(identifierSource(), textSource(), Pair::of), Streams.zip(propertySource(), attributeSource(), Pair::of), (itPair, paPair) -> arguments(itPair.getLeft(), itPair.getRight(), paPair.getLeft(), paPair.getRight()));
    }

    public static Stream<Arguments> identifierPropertySource() {
        return Streams.zip(identifierSource(), propertySource(), (id, prop) -> arguments(id, prop));
    }

    public static Stream<Arguments> identifierAttributeSource() {
        return Streams.zip(identifierSource(), attributeSource(), (id, prop) -> arguments(id, prop));
    }

    public static Stream<Arguments> identifierPropertyAttributeSource() {
        return Streams.zip(identifierSource(), Streams.zip(propertySource(), attributeSource(), Pair::of), (id, paPair) -> arguments(id, paPair.getLeft(), paPair.getRight()));
    }

    @ParameterizedTest
    @MethodSource("identifierTextArgumentSource")
    void testPropertyParsing(String propertyName, String propertyValue) {

        String content = propertyName +
                "=" +
                propertyValue +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());
    }

    @ParameterizedTest
    @MethodSource("textSource")
    void testCommentParsing(String propertyValue) {

        String content = "#" +
                propertyValue +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof CommentNode);
        CommentNode nd = (CommentNode) doc.getNodes().iterator().next();
        assertEquals(propertyValue, nd.getContent());

    }

    @ParameterizedTest
    @MethodSource("identifierTextArgumentSource")
    void testProcessingInstructionParsing(String propertyName, String propertyValue) {
        Assumptions.assumeFalse(propertyName == null);
        Assumptions.assumeFalse(propertyValue == null);

        String content = "@" +
                propertyName +
                " " +
                propertyValue +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof ProcessingInstructionNode);
        ProcessingInstructionNode nd = (ProcessingInstructionNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());
    }

    @ParameterizedTest
    @MethodSource("textSource")
    void testTextParsing(String propertyValue) {

        String content = "<" +
                propertyValue +
                ">" +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof TextNode);
        TextNode nd = (TextNode) doc.getNodes().iterator().next();
        assertEquals(propertyValue, nd.getContent());
    }

    @ParameterizedTest
    @MethodSource("identifierBTextArgumentSource")
    void testGroupedTextParsing(String propertyName, String propertyValue) {

        String content = propertyName +
                "<" +
                propertyValue +
                ">" +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();
        assertEquals(propertyName, nd.getName());

        // Our group node should have one child.
        assertEquals(SINGLE_CHILD, nd.getChildren().size());
        assertTrue(nd.getChildren().get(0) instanceof TextNode);

        TextNode cnt = (TextNode) nd.getChildren().get(0);

        assertEquals(propertyValue, cnt.getContent());
    }

    @ParameterizedTest
    @MethodSource("identifierSource")
    void testEmptyGroupParsing(String propertyName) {

        String content = propertyName +
                "{}" +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
    }

    @ParameterizedTest
    @MethodSource("identifierTextPropertySource")
    void testPropertyProperties(String propertyName, String propertyValue, List<String> propertyProperties) {

        String content = propertyName +
                '(' +
                String.join(" ", propertyProperties) +
                ')' +
                "=" +
                propertyValue +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();
        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());
        assertEquals(propertyProperties.size(), nd.getProperties().size());
        assertEquals(propertyProperties, nd.getProperties());
    }

    @ParameterizedTest
    @MethodSource("identifierTextAttributeSource")
    void testPropertyAttributes(String propertyName, String propertyValue, Map<String, String> propertyAttributes) {
        Assumptions.assumeFalse(propertyAttributes.entrySet().isEmpty());

        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append('[');

        for (Map.Entry<String, String> e : propertyAttributes.entrySet()) {
            content.append(e.getKey().trim());
            content.append(':');
            content.append(e.getValue().trim());
            content.append(',');
        }
        content.reverse().delete(0, 1).reverse();
        content.append(']');
        content.append("=");
        content.append(propertyValue);
        content.append("\n");


        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();
        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());

        assertEquals(propertyAttributes.size(), nd.getAttributes().size());

        assertEquals(propertyAttributes, nd.getAttributes());
    }

    @ParameterizedTest
    @MethodSource("identifierTextPropertyAttributeSource")
    void testPropertyFullMetadata(String propertyName, String propertyValue, List<String> propertyProperties, Map<String, String> propertyAttributes) {
        Assumptions.assumeFalse(propertyAttributes.entrySet().isEmpty());

        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append('(');
        content.append(String.join(" ", propertyProperties));
        content.append(')');
        content.append('[');

        for (Map.Entry<String, String> e : propertyAttributes.entrySet()) {
            content.append(e.getKey());
            content.append(':');
            content.append(e.getValue());
            content.append(',');
        }
        content.reverse().delete(0, 1).reverse();
        content.append(']');
        content.append("=");
        content.append(propertyValue);
        content.append("\n");


        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();
        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());

        assertEquals(propertyAttributes.size(), nd.getAttributes().size());

        assertEquals(propertyAttributes, nd.getAttributes());

        assertEquals(propertyProperties.size(), nd.getProperties().size());
        assertEquals(propertyProperties, nd.getProperties());

    }

    @ParameterizedTest
    @MethodSource("identifierPropertySource")
    void testGroupProperties(String propertyName, List<String> propertyProperties) {

        String content = propertyName +
                '(' +
                String.join(" ", propertyProperties) +
                ')' +
                "{}" +
                "\n";
        Document doc = assertDoesNotThrow(() -> Document.fromString(content));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
        assertEquals(propertyProperties.size(), nd.getProperties().size());
        assertEquals(propertyProperties, nd.getProperties());
    }

    @ParameterizedTest
    @MethodSource("identifierAttributeSource")
    void testGroupAttributes(String propertyName, Map<String, String> propertyAttributes) {
        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append('[');

        for (Map.Entry<String, String> e : propertyAttributes.entrySet()) {
            content.append(e.getKey());
            content.append(':');
            content.append(e.getValue());
            content.append(',');
        }
        content.reverse().delete(0, 1).reverse();
        content.append(']');

        content.append("{}");
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());

        assertEquals(propertyAttributes.size(), nd.getAttributes().size());

        assertEquals(propertyAttributes, nd.getAttributes());
    }

    @ParameterizedTest
    @MethodSource("identifierPropertyAttributeSource")
    void testGroupFullMetadata(String propertyName, List<String> propertyProperties, Map<String, String> propertyAttributes) {
        StringBuilder content = new StringBuilder();

        content.append(propertyName);
        content.append('(');
        content.append(String.join(" ", propertyProperties));
        content.append(')');
        content.append('[');

        for (Map.Entry<String, String> e : propertyAttributes.entrySet()) {
            content.append(e.getKey());
            content.append(':');
            content.append(e.getValue());
            content.append(',');
        }
        content.reverse().delete(0, 1).reverse();
        content.append(']');


        content.append("{}");
        content.append("\n");

        Document doc = assertDoesNotThrow(() -> Document.fromString(content.toString()));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());

        assertEquals(propertyAttributes.size(), nd.getAttributes().size());

        assertEquals(propertyAttributes, nd.getAttributes());

        assertEquals(propertyProperties.size(), nd.getProperties().size());
        assertEquals(propertyProperties, nd.getProperties());
    }


    @Test
    void testComplexGroupParsing() {
        String complexSource =
                "group1 {\n" +
                        "some1 <Lets rain>\n" +
                        "  # now for the real stuff\n" +
                        "  some2(party is coming) {\n" +
                        "     everybody[meaning: all] = may come here\n" +
                        "     and-see-the-radiant(light) = some may not\n" +
                        "     for-i-shall-say <\n" +
                        "        ye will be the men who enter the dark and bring the light\n" +
                        "        and ye will be the hands who will bring the evil to falter\n" +
                        "        and ye will ignore all the $ AND % and =\n" +
                        "        for this is what i say>\n" +
                        "    }\n" +
                        "}\n";

        Document doc = assertDoesNotThrow(() -> Document.fromString(complexSource));

        // Assume that we have parsed a single node.
        assertEquals(SINGLE_CHILD, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode group1 = (GroupNode) doc.getNodes().iterator().next();
        assertEquals("group1", group1.getName());

        /*block*/
        {
            // Our group should have 3 children.
            assertEquals(SINGLE_CHILD * 3, group1.getChildren().size());
            assertTrue(group1.getChildren().get(0) instanceof GroupNode);

            GroupNode chldrn1 = (GroupNode) group1.getChildren().get(0);
            assertEquals("some1", chldrn1.getName());
            assertEquals(SINGLE_CHILD, chldrn1.getChildren().size());
            assertTrue(chldrn1.getChildren().get(0) instanceof TextNode);
            assertEquals("Lets rain", ((TextNode) chldrn1.getChildren().get(0)).getContent());

            assertTrue(group1.getChildren().get(1) instanceof CommentNode);
            assertEquals("now for the real stuff", ((CommentNode) group1.getChildren().get(1)).getContent());

            assertTrue(group1.getChildren().get(2) instanceof GroupNode);
            GroupNode chldrn3 = (GroupNode) group1.getChildren().get(2);
            assertEquals("some2", chldrn3.getName());
            assertIterableEquals(Arrays.asList("party", "is", "coming"), chldrn3.getProperties());

            assertEquals(SINGLE_CHILD * 3, chldrn3.getChildren().size());
            /*block*/
            {
                assertTrue(chldrn3.getChildren().get(0) instanceof PropertyNode);
                PropertyNode chl1 = (PropertyNode) chldrn3.getChildren().get(0);
                assertEquals("everybody", chl1.getName());
                assertEquals(Collections.singletonMap("meaning", "all"), chl1.getAttributes());
                assertEquals("may come here", chl1.getContent());

                assertTrue(chldrn3.getChildren().get(1) instanceof PropertyNode);
                PropertyNode chl2 = (PropertyNode) chldrn3.getChildren().get(1);
                assertEquals("and-see-the-radiant", chl2.getName());
                assertIterableEquals(Collections.singletonList("light"), chl2.getProperties());
                assertEquals("some may not", chl2.getContent());


                assertTrue(chldrn3.getChildren().get(2) instanceof GroupNode);
                GroupNode chl3 = (GroupNode) chldrn3.getChildren().get(2);
                assertEquals("for-i-shall-say", chl3.getName());
                assertEquals(SINGLE_CHILD, chl3.getChildren().size());
                assertTrue(chl3.getChildren().get(0) instanceof TextNode);
                TextNode tx = (TextNode) chl3.getChildren().get(0);
                assertEquals("\n        ye will be the men who enter the dark and bring the light\n" +
                        "        and ye will be the hands who will bring the evil to falter\n" +
                        "        and ye will ignore all the $ AND % and =\n" +
                        "        for this is what i say", tx.getContent());
            }
        }
    }

    @Test
    void testDocumentFromByteBuffer() {
        String testString =
                "group1 {\n" +
                        "some1 <Lets rain>\n" +
                        " @test test\n" +
                        "  # now for the real stuff\n" +
                        "  some2(party is coming) {\n" +
                        "     everybody[meaning: all] = may come here\n" +
                        "     and-see-the-radiant(light) = some may not\n" +
                        "     for-i-shall-say <\n" +
                        "        ye will be the men who enter the dark and bring the light\n" +
                        "        and ye will be the hands who will bring the evil to falter\n" +
                        "        and ye will ignore all the $ AND % and =\n" +
                        "        for this is what i say>\n" +
                        "    }\n" +
                        "}\n";

        Document doc1 = assertDoesNotThrow(() -> Document.fromString(testString));
        ByteBuffer bb = ByteBuffer.wrap(testString.getBytes(StandardCharsets.UTF_16));
        Document doc2 = assertDoesNotThrow(() -> Document.fromByteBuffer(bb, StandardCharsets.UTF_16));

        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());
    }

    @Test
    void testInvalidDataParsing() {
        ByteBuffer bb = ByteBuffer.allocate(0);

        assertThrows(SFFDocumentParsingException.class, () -> Document.fromByteBuffer(bb, StandardCharsets.UTF_8));
        assertThrows(SFFDocumentParsingException.class, () -> Document.fromString("invalid data\n"));
        assertThrows(IllegalArgumentException.class, () -> Document.fromString("invalid data no newline"));

    }

    @Test
    void testRealFile() {
        Reader sr = new InputStreamReader(getClass().getResourceAsStream("/db.sff"));
        Scanner sc = new Scanner(sr);

        StringBuffer buf = new StringBuffer();
        while (sc.hasNext()) {
            buf.append(sc.nextLine()).append('\n');
        }

        assertDoesNotThrow(() -> Document.fromString(buf.toString()));
    }

    @Test
    @Disabled("Not implemented")
    void testCompleteParsing() {
        // TODO: Parse left really huge document with all features and many pitfalls.
    }

    @DisplayName("Tests for sff-5")
    static class SFF_5 {
        @Test
        void testEscapesInGroupNames() {
            String testString = "group1\\(\\)\\[\\]\\<\\>\\{\\}(\\)te\\ st)[\\]\\:: te\\,] {}\n";

            Document doc1 = assertDoesNotThrow(() -> Document.fromString(testString));

            // Assume that we have parsed a single node.
            assertEquals(SINGLE_CHILD, doc1.getNodes().size());
            assertTrue(doc1.getNodes().get(0) instanceof GroupNode);
            GroupNode node = (GroupNode) doc1.getNodes().get(0);
            assertEquals("group1()[]<>{}", node.getName());
            assertIterableEquals(Collections.singletonList(")te st"), node.getProperties());
            assertEquals(Collections.singletonMap("]:", "te,"), node.getAttributes());
        }



         @Test
         void testEscapesInProperties() {
            String testString = "property\\(\\)\\[\\]\\<\\>\\{\\}\\=(\\)te\\ st)[\\]\\:: te\\,]=\\=tes\\<t\\>\n";
             Document doc1 = assertDoesNotThrow(() -> Document.fromString(testString));

             // Assume that we have parsed a single node.
            assertEquals(SINGLE_CHILD, doc1.getNodes().size());
            assertTrue(doc1.getNodes().get(0) instanceof PropertyNode);
            PropertyNode node = (PropertyNode)doc1.getNodes().get(0);
            assertEquals("property()[]<>{}=", node.getName());
            assertEquals("=tes<t>", node.getContent());
            assertEquals(Collections.singletonList(")te st"), node.getProperties());
            assertEquals(Collections.singletonMap("]:", "te,"), node.getAttributes());
         }

         @Test
         void testEscapesInTextNodes() {
             String testString = "<\\(\\)\\[\\]\\<\\>\\{\\}\\=>\n";

             Document doc1 = assertDoesNotThrow(() -> Document.fromString(testString));

             // Assume that we have parsed a single node.
             assertEquals(SINGLE_CHILD, doc1.getNodes().size());
             assertTrue(doc1.getNodes().get(0) instanceof TextNode);
             TextNode node = (TextNode) doc1.getNodes().get(0);
             assertEquals("()[]<>{}=", node.getContent());
         }


    }


}
