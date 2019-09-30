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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CSFFFormatterTests {
    private static eu.cactis.sff.test_utility.Generators Generators;

    @BeforeAll
    public static void initializeGenerator() {
        Generators = new Generators();

    }
    public static final int GENERATOR_LIMIT = 10;

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
    void testPropertyFormatting(String propertyName, String propertyValue) {
        PropertyNode node = new PropertyNode(propertyName, propertyValue);

        CSFFFormatter formatter = new CSFFFormatter();
        String result = formatter.formatNode(node);

        Document doc = assertDoesNotThrow(() -> Document.fromString(result));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());

    }

    @Test
    void testCommentNodeDefaultConstructor() {
        CommentNode node = new CommentNode();
        assertEquals("", node.getContent());
    }
    @ParameterizedTest
    @MethodSource("textSource")
    void testCommentFormatting(String propertyValue) {
        CommentNode node = new CommentNode(propertyValue);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof CommentNode);
        CommentNode nd = (CommentNode) doc.getNodes().iterator().next();
        assertEquals(propertyValue, nd.getContent());

    }


    @ParameterizedTest
    @MethodSource("identifierTextArgumentSource")
    void testProcessingInstructionFormatting(String propertyName, String propertyValue) {
        ProcessingInstructionNode node = new ProcessingInstructionNode();
        node.setName(propertyName);
        node.setContent(propertyValue);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof ProcessingInstructionNode);
        ProcessingInstructionNode nd = (ProcessingInstructionNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());
    }

    @ParameterizedTest
    @MethodSource("textSource")
    void testTextFormatting(String propertyValue) {
        TextNode node = new TextNode();
        node.setContent(propertyValue);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof TextNode);
        TextNode nd = (TextNode) doc.getNodes().iterator().next();
        assertEquals(propertyValue, nd.getContent());
    }

    @ParameterizedTest
    @MethodSource("identifierBTextArgumentSource")
    void testGroupedTextFormatting(String propertyName, String propertyValue) {
        GroupNode node = new GroupNode();
        node.setName(propertyName);
        node.setChildren(Collections.singletonList(new TextNode(propertyValue)));

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();
        assertEquals(propertyName, nd.getName());
        assertEquals(1, nd.getChildren().size());
        assertTrue(nd.getChildren().get(0) instanceof TextNode);

        TextNode cnt = (TextNode) nd.getChildren().get(0);

        assertEquals(propertyValue, cnt.getContent());
    }

    @ParameterizedTest
    @MethodSource("identifierSource")
    void testEmptyGroupFormatting(String propertyName) {
        GroupNode node = new GroupNode();
        node.setName(propertyName);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
    }


    @ParameterizedTest
    @MethodSource("identifierTextPropertySource")
    void testPropertyPropertyFormatting(String propertyName, String propertyValue, List<String> propertyProperties) {
        StringBuilder content = new StringBuilder();

        PropertyNode node = new PropertyNode();
        node.setName(propertyName);
        node.setContent(propertyValue);
        node.setProperties(propertyProperties);

        CSFFFormatter formatter = new CSFFFormatter();
        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();
        assertEquals(propertyName, nd.getName());
        assertArrayEquals(propertyValue.getBytes(), nd.getContent().getBytes());

        assertEquals(propertyProperties.size(), nd.getProperties().size());
        assertEquals(propertyProperties, nd.getProperties());
    }

    @ParameterizedTest
    @MethodSource("identifierTextAttributeSource")
    void testPropertyAttributesFormatting(String propertyName, String propertyValue, Map<String, String> propertyAttributes) {
        PropertyNode node = new PropertyNode();
        node.setName(propertyName);
        node.setContent(propertyValue);
        node.setAttributes(propertyAttributes);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();
        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());

        assertEquals(propertyAttributes.size(), nd.getAttributes().size());

        assertEquals(propertyAttributes, nd.getAttributes());
    }

    @ParameterizedTest
    @MethodSource("identifierTextPropertyAttributeSource")
    void testPropertyFullMetadataFormatting(String propertyName, String propertyValue, List<String> propertyProperties, Map<String, String> propertyAttributes) {
        StringBuilder content = new StringBuilder();

        PropertyNode node = new PropertyNode(propertyName, propertyProperties, propertyAttributes, propertyValue);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
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
    void testGroupPropertiesFormatting(String propertyName, List<String> propertyProperties) {
        GroupNode node = new GroupNode();
        node.setName(propertyName);
        node.setProperties(propertyProperties);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
        assertEquals(propertyProperties.size(), nd.getProperties().size());
        assertEquals(propertyProperties, nd.getProperties());
    }

    @ParameterizedTest
    @MethodSource("identifierAttributeSource")
    void testGroupAttributesFormatting(String propertyName, Map<String, String> propertyAttributes) {
        GroupNode node = new GroupNode();
        node.setName(propertyName);
        node.setAttributes(propertyAttributes);

        CSFFFormatter formatter = new CSFFFormatter();
        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());

        assertEquals(propertyAttributes.size(), nd.getAttributes().size());

        assertEquals(propertyAttributes, nd.getAttributes());
    }

    @ParameterizedTest
    @MethodSource("identifierPropertyAttributeSource")
    void testGroupFullMetadataFormatting(String propertyName, List<String> propertyProperties, Map<String, String> propertyAttributes) {
        GroupNode node = new GroupNode(propertyName, propertyProperties, propertyAttributes, Collections.emptyList());
        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
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
        GroupNode node = new GroupNode();
        node.setName("group1");
        {
            GroupNode s1 = new GroupNode();
            node.appendChild(s1);
            s1.setName("some1");
            s1.setChildren(Collections.singletonList(new TextNode("Lets rain")));
            node.appendChild(new CommentNode("now for the real stuff"));

            GroupNode s2 = new GroupNode();
            s2.setName("some2");
            node.appendChild(s2);
            {
                s2.setPropertiesFromString("party is coming");

                {
                    PropertyNode p1 = new PropertyNode();
                    s2.appendChild(p1);
                    p1.setName("everybody");
                    p1.setAttributes(Collections.singletonMap("meaning", "all"));
                    p1.setContent("may come here");
                }

                {
                    PropertyNode p1 = new PropertyNode();
                    s2.appendChild(p1);
                    p1.setName("and-see-the-radiant");
                    p1.setPropertiesFromString("light");
                    p1.setContent("some may not");
                }

                {
                    GroupNode p1 = new GroupNode();
                    s2.appendChild(p1);
                    p1.setName("for-i-shall-say");
                    p1.appendChild(new TextNode("\n" +
                        "        ye will be the men who enter the dark and bring the light\n" +
                        "        and ye will be the hands who will bring the evil to falter\n" +
                        "        and ye will ignore all the $ AND % and =\n" +
                        "        for this is what i say"));
                }

            }
        }

        CSFFFormatter formatter = new CSFFFormatter();
        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.formatNode(node)));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);

        GroupNode group1 = (GroupNode) doc.getNodes().iterator().next();
        assertEquals("group1", group1.getName());

        /*block*/
        {
            assertEquals(3, group1.getChildren().size());
            assertTrue(group1.getChildren().get(0) instanceof GroupNode);

            GroupNode chldrn1 = (GroupNode) group1.getChildren().get(0);
            assertEquals("some1", chldrn1.getName());
            assertEquals(1, chldrn1.getChildren().size());
            assertTrue(chldrn1.getChildren().get(0) instanceof TextNode);
            assertEquals("Lets rain", ((TextNode) chldrn1.getChildren().get(0)).getContent());

            assertTrue(group1.getChildren().get(1) instanceof CommentNode);
            assertEquals("now for the real stuff", ((CommentNode) group1.getChildren().get(1)).getContent());

            assertTrue(group1.getChildren().get(2) instanceof GroupNode);
            GroupNode chldrn3 = (GroupNode) group1.getChildren().get(2);
            assertEquals("some2", chldrn3.getName());
            assertIterableEquals(Arrays.asList("party", "is", "coming"), chldrn3.getProperties());

            assertEquals(3, chldrn3.getChildren().size());
            /*block*/
            {
                assertTrue(chldrn3.getChildren().get(0) instanceof PropertyNode);
                PropertyNode chl1 = (PropertyNode)chldrn3.getChildren().get(0);
                assertEquals("everybody", chl1.getName());
                assertEquals(Collections.singletonMap("meaning", "all"), chl1.getAttributes());
                assertEquals("may come here", chl1.getContent());

                assertTrue(chldrn3.getChildren().get(1) instanceof PropertyNode);
                PropertyNode chl2 = (PropertyNode)chldrn3.getChildren().get(1);
                assertEquals("and-see-the-radiant", chl2.getName());
                assertIterableEquals(Collections.singletonList("light"), chl2.getProperties());
                assertEquals("some may not", chl2.getContent());



                assertTrue(chldrn3.getChildren().get(2) instanceof GroupNode);
                GroupNode chl3 = (GroupNode)chldrn3.getChildren().get(2);
                assertEquals("for-i-shall-say", chl3.getName());
                assertEquals(1, chl3.getChildren().size());
                assertTrue(chl3.getChildren().get(0) instanceof TextNode);
                TextNode tx = (TextNode)chl3.getChildren().get(0);
                assertEquals("\n        ye will be the men who enter the dark and bring the light\n" +
                        "        and ye will be the hands who will bring the evil to falter\n" +
                        "        and ye will ignore all the $ AND % and =\n" +
                        "        for this is what i say", tx.getContent());
            }
        }
    }

    @Test
    void testDocumentFormatting() {
        ProcessingInstructionNode pi = new ProcessingInstructionNode("sff", "version=1.0");
        Document doc = new Document();
        doc.appendChild(pi);
        doc.appendChild(new GroupNode("Test", Collections.singletonList(new TextNode("Test"))));

        assertEquals(pi, doc.getNodes().get(0));

    }

    @Test
    void testUnknownNodeFormatting() {
        Node node = new Node() {

            @Override
            public String getIdentifier() {
                return "unknown-node";
            }
        };

        CSFFFormatter formatter = new CSFFFormatter();

        assertThrows(IllegalStateException.class, () -> formatter.formatNode(node));
    }

    @Test
    @Disabled("Not implemented")
    void testCompleteFormatting() {
        // TODO: Parse left really huge document with all features and many pitfalls.
    }
}
