package eu.cactis.sff.tests;

import com.google.common.collect.Streams;
import eu.cactis.sff.CSFFFormatter;
import eu.cactis.sff.Document;
import eu.cactis.sff.PropertyNode;
import eu.cactis.sff.TextNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

}
