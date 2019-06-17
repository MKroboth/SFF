package eu.cactis.sff;

import org.w3c.dom.*;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Converts an SFF node tree into xml dom.
 * @author Maximilian Kroboth
 * @version 1.0
 * @since 1.1
 */
public class SFFDOMConverter {
    public final String SFF_NAMESPACE = "https://www.cactis.eu/schema/sff";
    public final String SFF_ATTRIBUTE = "sff";

    public eu.cactis.sff.Document fromDOM(Document document) {
        NodeList rootElems =document.getElementsByTagNameNS(SFF_NAMESPACE, "root");
        if(rootElems.getLength() != 1) {
            throw new IllegalArgumentException();
        }

        Element rootElement = (Element) rootElems.item(0);

        eu.cactis.sff.Document sffDocument = new eu.cactis.sff.Document();

        for(int i = 0; i < rootElement.getChildNodes().getLength(); ++i) {
            fromDOMNode(sffDocument::appendChild, rootElement.getChildNodes().item(i));
        }

        return sffDocument;
    }

    public void fromDOMNode(Consumer<Node> appendChild, org.w3c.dom.Node node) {
        switch (node.getNodeType()) {
            case org.w3c.dom.Node.COMMENT_NODE:
                appendChild.accept(new CommentNode(node.getTextContent()));
                break;
            case org.w3c.dom.Node.ELEMENT_NODE:
            {
                String identifier = null;
                List<String> properties = null;
                Map<String, String> attributes = new Hashtable<>();

                Element e = (Element)node;
                identifier = e.getAttributeNS(SFF_NAMESPACE, "identifier");
                properties = Arrays.asList(e.getAttributeNS(SFF_NAMESPACE, "properties").split(" "));
                for(int i = 0; i < e.getAttributes().getLength(); ++i) {
                    org.w3c.dom.Attr attr = (Attr)e.getAttributes().item(i);

                    if(attr.getNamespaceURI() != SFF_NAMESPACE) {
                        attributes.put(attr.getName(), attr.getValue());
                    }
                }

                switch (identifier) {
                    case "group": {
                        GroupNode gn = new GroupNode(e.getTagName(), properties, attributes, new LinkedList<>());

                          for(int i = 0; i < e.getChildNodes().getLength(); ++i) {
                              fromDOMNode(gn::appendChild, e.getChildNodes().item(i));
                          }

                          appendChild.accept(gn);
                    } break;
                    case "property":
                        appendChild.accept(new PropertyNode(e.getTagName(), properties, attributes, e.getAttributeNS(SFF_NAMESPACE, "content")));
                        break;
                }
            }
                break;
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                appendChild.accept(new ProcessingInstructionNode(((org.w3c.dom.ProcessingInstruction)node).getTarget(), ((org.w3c.dom.ProcessingInstruction)node).getData()));
                break;
            case org.w3c.dom.Node.TEXT_NODE:
                appendChild.accept(new TextNode(node.getTextContent()));
                break;
        }
    }


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
                elem.setAttributeNS(SFF_NAMESPACE, String.format("%s:content", SFF_ATTRIBUTE), ((PropertyNode) node).getContent());
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
