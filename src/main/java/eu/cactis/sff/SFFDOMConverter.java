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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Converts an SFF node tree into xml dom.
 *
 * @author Maximilian Kroboth
 * @version 1.3
 * @since 1.1
 */
public class SFFDOMConverter {
    /** Constant <code>SFF_NAMESPACE="https://www.cactis.eu/schema/sff"</code> */
    public static final String SFF_NAMESPACE = "https://www.cactis.eu/schema/sff";
    /** Constant <code>SFF_ATTRIBUTE="sff"</code> */
    public static final String SFF_ATTRIBUTE = "sff";

    /**
     * <p>fromDOM.</p>
     *
     * @param document a {@link org.w3c.dom.Document} object.
     * @return a {@link eu.cactis.sff.Document} object.
     */
    public eu.cactis.sff.Document fromDOM(Document document) {
        Element rootElement = (Element) document.getDocumentElement();

        eu.cactis.sff.Document sffDocument = new eu.cactis.sff.Document();

        for(int i = 0; i < rootElement.getChildNodes().getLength(); ++i) {
            new SFFDOMConverter().fromDOMNode(sffDocument::appendChild, rootElement.getChildNodes().item(i));
        }

        return sffDocument;
    }

    /**
     * <p>fromDOMNode.</p>
     *
     * @param appendChild a {@link java.util.function.Consumer} object.
     * @param node a {@link org.w3c.dom.Node} object.
     */
    public void fromDOMNode(Consumer<Node> appendChild, org.w3c.dom.Node node) {
        switch (node.getNodeType()) {
            case org.w3c.dom.Node.COMMENT_NODE:
                appendChild.accept(new CommentNode(node.getTextContent()));
                break;
            case org.w3c.dom.Node.ELEMENT_NODE:
            {
                String identifier;
                List<String> properties;
                Map<String, String> attributes = new Hashtable<>();

                Element e = (Element)node;
                identifier = e.getAttributeNS(SFF_NAMESPACE, "identifier");
                properties = Arrays.asList(e.getAttributeNS(SFF_NAMESPACE, "properties").split(" "));
                for(int i = 0; i < e.getAttributes().getLength(); ++i) {
                    org.w3c.dom.Attr attr = (Attr)e.getAttributes().item(i);

                    if(attr.getNamespaceURI() == null || !attr.getNamespaceURI().equals(SFF_NAMESPACE)) {

                        attributes.put(attr.getName(), attr.getValue());
                    }
                }

                switch (identifier) {
                    case "group": {
                        GroupNode gn = new GroupNode(e.getTagName(), properties, attributes, new LinkedList<>());

                        if(e.hasAttributeNS(SFF_NAMESPACE, String.format("%s:uuid", SFF_NAMESPACE))) {
                            gn.setUUID(UUID.fromString(e.getAttributeNS(SFF_NAMESPACE, String.format("%s:uuid", SFF_NAMESPACE))));
                        }

                          for(int i = 0; i < e.getChildNodes().getLength(); ++i) {
                              fromDOMNode(gn::appendChild, e.getChildNodes().item(i));
                          }

                          appendChild.accept(gn);
                    } break;
                    case "property":
                        appendChild.accept(new PropertyNode(e.getTagName(), properties, attributes, e.getAttributeNS(SFF_NAMESPACE, "content")));
                        break;
                    default:
                        throw new IllegalStateException("Unknown identifier: " + identifier);
                }
            }
                break;
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                appendChild.accept(new ProcessingInstructionNode(((org.w3c.dom.ProcessingInstruction)node).getTarget(), ((org.w3c.dom.ProcessingInstruction)node).getData()));
                break;
            case org.w3c.dom.Node.TEXT_NODE:
                if (!node.getTextContent().trim().isEmpty()) {
                    appendChild.accept(new TextNode(node.getTextContent()));
                }
                break;
            default:
                throw new IllegalStateException("Unknown node type.");
        }
    }


    /**
     * <p>toDOM.</p>
     *
     * @param document a {@link eu.cactis.sff.Document} object.
     * @return a {@link org.w3c.dom.Document} object.
     */
    public Document toDOM(eu.cactis.sff.Document document) {
        DocumentBuilder db;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        Document xmlDocument = db.newDocument();

        Element rootElement = xmlDocument.createElementNS(SFF_NAMESPACE, String.format("%s:root", SFF_ATTRIBUTE));
        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, String.format("%s:%s", XMLConstants.XMLNS_ATTRIBUTE, SFF_ATTRIBUTE), SFF_NAMESPACE);
        for (Node nd : document.getNodes()) {
            fillDOMElement(xmlDocument, rootElement, nd);
        }
        xmlDocument.appendChild(rootElement);
        return xmlDocument;
    }

    private Element addMetadataToElement(Element element, List<String> properties, Map<String, String> attributes) {
        if (!properties.isEmpty()) {
            element.setAttributeNS(SFF_NAMESPACE, String.format("%s:properties", SFF_ATTRIBUTE), String.join(" ", properties));
        }
        for (Map.Entry<String, String> ent : attributes.entrySet()) {
            element.setAttribute(ent.getKey(), ent.getValue());
        }

        return element;
    }

    private void fillDOMElement(Document document, Element element, Node node) {
        String name = null;
        List<String> properties = null;
        Map<String, String> attributes = null;

        if(node instanceof NamedNode) {
            name = ((NamedNode)node).getName();
        }

        if(node instanceof NodeWithMetaData) {
            properties = ((NodeWithMetaData)node).getProperties();
            attributes = ((NodeWithMetaData)node).getAttributes();
        }

        org.w3c.dom.Node createdNode = null;

        boolean isElement = false;

        switch (node.getIdentifier()) {
            case ProcessingInstructionNode.IDENTIFIER:
                assert node instanceof ProcessingInstructionNode;
                createdNode = document.createProcessingInstruction(name, ((ProcessingInstructionNode) node).getContent());
                break;
            case CommentNode.IDENTIFER:
                assert node instanceof CommentNode;
                createdNode = document.createComment(((CommentNode) node).getContent());
                break;
            case TextNode.IDENTIFIER:
                assert node instanceof TextNode;
                createdNode = document.createTextNode(((TextNode) node).getContent());
                break;

            case GroupNode.IDENTIFIER: {
                assert node instanceof GroupNode;
                isElement = true;
                Element elem = addMetadataToElement(document.createElement(name), properties, attributes);

                elem.setAttributeNS(SFF_NAMESPACE, String.format("%s:uuid", SFF_ATTRIBUTE),
                        (((GroupNode)node).getUUID().orElse(UUID.randomUUID())).toString());

                for (Node n : ((GroupNode) node).getChildren()) {
                    fillDOMElement(document, elem, n);
                }
                createdNode = elem;
            }
            break;
            case PropertyNode.IDENTIFIER: {
                assert node instanceof PropertyNode;
                isElement = true;
                Element elem = addMetadataToElement(document.createElement(name), properties, attributes);
                elem.setAttributeNS(SFF_NAMESPACE, String.format("%s:content", SFF_ATTRIBUTE),
                        ((PropertyNode) node).getContent());
                createdNode = elem;
            }
            break;
        }

        if(isElement) {
            ((Element)createdNode).setAttributeNS(SFF_NAMESPACE, String.format("%s:identifier", SFF_ATTRIBUTE), node.getIdentifier());
        }

        element.appendChild(createdNode);

    }
}
