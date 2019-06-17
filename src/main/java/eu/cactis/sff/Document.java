package eu.cactis.sff;

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

import eu.cactis.sff.parser.SFFParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Represents a SFF document.
 * @author Maximilian Kroboth
 * @since 1.0
 * @version 1.0
 */
public class Document {

    /**
     * A list of child nodes.
     */
    private final List<Node> nodes = new LinkedList<>();

    /**
     * Returns all child nodes of the document.
     * @return all child nodes of the document.
     */
    public List<Node> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    /**
     * Resets all nodes of the document.
     * @param nodes the new value of the nodes.
     */
    public void resetNodes(Collection<Node> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }

    /**
     * Appends a new node to the documents nodes.
     * @param node the new node.
     */
    public void appendChild(Node node) {
        nodes.add(node);
    }

    /**
     * Default constructor.
     */
    public Document() {
    }

    /**
     * Creates a new document and initializes its nodes.
     * @param nodes the nodes to initialize the documents nodes from.
     */
    public Document(Collection<Node> nodes) {
        resetNodes(nodes);
    }

    /**
     * Creates a new document from a byte buffer.
     * @param bb the byte buffer
     * @return a new document
     * @throws SFFDocumentParsingException if something during parsing fails
     */
    public static Document fromByteBuffer(ByteBuffer bb, Charset encoding) throws SFFDocumentParsingException {
        byte[] bts = new byte[bb.limit()];
        bb.get(bts);

        try {
            InputStream bais = new ByteArrayInputStream(bts);
            SFFParser parser = new SFFParser(bais, encoding);
            Document ret = new Document(parser.Start());
            bais.close();

            return ret;
        } catch (Exception ex) {
            throw new SFFDocumentParsingException(ex);
        }
    }

    public static Document fromString(String str) throws SFFDocumentParsingException {
        if(!str.endsWith("\n")) throw new IllegalArgumentException("A document must end with a line break.");

        try {
            SFFParser parser = new SFFParser(new StringReader(str));
            Document ret = new Document(parser.Start());

            return ret;
        } catch (Exception ex) {
            throw new SFFDocumentParsingException(ex);
        }
    }
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;


        return getNodes().equals(document.getNodes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodes());
    }

}
