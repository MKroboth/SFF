package eu.cactis.sff.tests;

import com.google.common.collect.Streams;
import eu.cactis.sff.*;
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
    private static Generators Generators;

    @BeforeAll
    public static void initializeGenerator() {
        Generators = new Generators();

    }
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
        return Streams.zip(Streams.zip(identifierSource(), textSource(), Pair::new), propertySource(), (itPair, prop) -> arguments(itPair.a, itPair.b, prop));
    }

    public static Stream<Arguments> identifierTextAttributeSource() {
        return Streams.zip(Streams.zip(identifierSource(), textSource(), Pair::new), attributeSource(), (itPair, prop) -> arguments(itPair.a, itPair.b, prop));
    }

    public static Stream<Arguments> identifierTextPropertyAttributeSource() {
        return Streams.zip(Streams.zip(identifierSource(), textSource(), Pair::new), Streams.zip(propertySource(), attributeSource(), Pair::new), (itPair, paPair) -> arguments(itPair.a, itPair.b, paPair.a, paPair.b));
    }

    public static Stream<Arguments> identifierPropertySource() {
        return Streams.zip(identifierSource(), propertySource(), (id, prop) -> arguments(id, prop));
    }

    public static Stream<Arguments> identifierAttributeSource() {
        return Streams.zip(identifierSource(), attributeSource(), (id, prop) -> arguments(id, prop));
    }

    public static Stream<Arguments> identifierPropertyAttributeSource() {
        return Streams.zip(identifierSource(), Streams.zip(propertySource(), attributeSource(), Pair::new), (id, paPair) -> arguments(id, paPair.a, paPair.b));
    }

    @ParameterizedTest
    @MethodSource("identifierTextArgumentSource")
    void testPropertyFormatting(String propertyName, String propertyValue) {
        PropertyNode node = new PropertyNode();
        node.setName(propertyName);
        node.setContent(propertyValue);

        CSFFFormatter formatter = new CSFFFormatter();
        String result = formatter.formatNode(node);

        Document doc = assertDoesNotThrow(() -> Document.fromString(result));

        assertEquals(1, doc.getNodes().size());
        assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
        PropertyNode nd = (PropertyNode) doc.getNodes().iterator().next();

        assertEquals(propertyName, nd.getName());
        assertEquals(propertyValue, nd.getContent());

    }

    @ParameterizedTest
    @MethodSource("textSource")
    void testCommentFormatting(String propertyValue) {
        CommentNode node = new CommentNode(propertyValue);

        CSFFFormatter formatter = new CSFFFormatter();

        Document doc = assertDoesNotThrow(() -> Document.fromString(formatter.toString()));

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
            node.appendChild(s2);
            {
                s2.setPropertiesFromString("party is coming");

                {
                    PropertyNode p1 = new PropertyNode();
                    s2.appendChild(p1);
                    p1.setName("everynody");
                    p1.setAttributes(Collections.singletonMap("meaning", "all"));
                    p1.setContent("may come here");
                }

                {
                    PropertyNode p1 = new PropertyNode();
                    s2.appendChild(p1);
                    p1.setName("and-see-the-radiant");
                    p1.setPropertiesFromString("light");
                    p1.setContent("some may no");
                }

                {
                    GroupNode p1 = new GroupNode();
                    s2.appendChild(p1);
                    p1.setName("for-i=shall-say");
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
    @Disabled("Not implemented")
    void testCompleteParsing() {
        // TODO: Parse a really huge document with all features and many pitfalls.
    }
}
