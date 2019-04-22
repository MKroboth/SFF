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

import eu.cactis.sff.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ParserTests {
    @Test
    void testPropertyParsing() {
        StringBuilder content = new StringBuilder();

        String propertyName = "TESTNAME"; // TODO: generate
        String propertyValue = "TESTVALUE"; // TODO: generate

        content.append(propertyName);
        content.append("=");
        content.append(propertyValue);
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof PropertyNode);
            PropertyNode nd = (PropertyNode)doc.getNodes().iterator().next();

            assertEquals(propertyName, nd.getName());
            assertEquals(propertyValue, nd.getContent());
        });
    }

    @Test
    void testCommentParsing() {
        StringBuilder content = new StringBuilder();

        String propertyValue = "TESTVALUE"; // TODO: generate

        content.append("#");
        content.append(propertyValue);
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof CommentNode);
            CommentNode nd = (CommentNode)doc.getNodes().iterator().next();

            assertEquals(propertyValue, nd.getContent());
        });
    }
    @Test
    void testProcessingInstructionParsing() {
        StringBuilder content = new StringBuilder();

        String propertyName = "TESTNAME"; // TODO: generate
        String propertyValue = "TESTVALUE"; // TODO: generate

        content.append("@");
        content.append(propertyName);
        content.append(" ");
        content.append(propertyValue);
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof ProcessingInstructionNode);
            ProcessingInstructionNode nd = (ProcessingInstructionNode) doc.getNodes().iterator().next();

            assertEquals(propertyName, nd.getName());
            assertEquals(propertyValue, nd.getContent());
        });
    }
    @Test
    void testTextParsing() {
        StringBuilder content = new StringBuilder();

        String propertyValue = "TESTVALUE"; // TODO: generate

        content.append("<");
        content.append(propertyValue);
        content.append(">");
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof TextNode);
            TextNode nd = (TextNode)doc.getNodes().iterator().next();

            assertEquals(propertyValue, nd.getContent());
        });
    }
    @Test
    void testGroupedTExtParsing() {
        StringBuilder content = new StringBuilder();

        String propertyName = "TESTVALUE"; // TODO: generate
        String propertyValue = "TESTVALUE"; // TODO: generate

        content.append(propertyName);
        content.append("<");
        content.append(propertyValue);
        content.append(">");
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);
            GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

            assertEquals(propertyName, nd.getName());

            assertEquals(1, nd.getChildren().size());
            assertTrue(nd.getChildren().get(0) instanceof TextNode);
            TextNode cnt = (TextNode)nd.getChildren().get(0);
            assertEquals(propertyValue, cnt.getContent());
        });
    }
    @Test
    void testEmptyGroupParsing() {
        StringBuilder content = new StringBuilder();

        String propertyName = "TESTNAME"; // TODO: generate

        content.append(propertyName);
        content.append("{}");
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);
            GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

            assertEquals(propertyName, nd.getName());
        });
    }
        @Test
    void testSimpleGroupParsing() {
        StringBuilder content = new StringBuilder();

        String propertyName = "TESTNAME"; // TODO: generate

        content.append(propertyName);
        content.append("{}");
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);
            GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

            assertEquals(propertyName, nd.getName());
        });
    }

    @Test
    void testComplexGroupParsing() {
        StringBuilder content = new StringBuilder();

        String propertyName = "TESTNAME"; // TODO: generate

        content.append(propertyName);
        content.append("{}");
        content.append("\n");


        assertDoesNotThrow(() -> {
            Document doc = Document.fromString(content.toString());

            assertEquals(1, doc.getNodes().size());
            assertTrue(doc.getNodes().iterator().next() instanceof GroupNode);
            GroupNode nd = (GroupNode) doc.getNodes().iterator().next();

            assertEquals(propertyName, nd.getName());
        });
    }

    @Test
    @Disabled("Not implemented")
    void testCompleteParsing() {
    }
}
