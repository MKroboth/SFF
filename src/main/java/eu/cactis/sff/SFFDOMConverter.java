package eu.cactis.sff;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.plugin.dom.exception.InvalidStateException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Map;

/**
 * Converts an SFF node tree into xml dom.
 * @author Maximilian Kroboth
 * @version 1.0
 * @since 1.1
 */
public class SFFDOMConverter {
    public eu.cactis.sff.Document fromDOM(Document document) {
        throw new InvalidStateException("Not implemented");
    }

    public Document toDOM(eu.cactis.sff.Document document) {
        if(document.getNodes().stream().filter(x -> x.getClass() != ProcessingInstructionNode.class && x.getClass() != CommentNode.class).count() != 1) {
            throw new IllegalArgumentException("The given document must have exactly one root element.");
        }

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

        for (Node nd : document.getNodes()) {
            generateDOMNode(xmlDocument, nd);
        }
        return xmlDocument;
    }

    private void generateDOMNode(Document document, Node node) {
        if(node instanceof ProcessingInstructionNode) {
            document.appendChild(document.createProcessingInstruction(((ProcessingInstructionNode)node).getName(), ((ProcessingInstructionNode)node).getContent()));
        } else if(node instanceof CommentNode) {
            document.appendChild(document.createComment(((CommentNode)node).getContent()));
        } else if (node instanceof GroupNode) {
            GroupNode gn = (GroupNode)node;
            Element elem = document.createElement(gn.getName());
            document.appendChild(elem);
            elem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:sff", "https://www.cactis.eu/schema/sff");
            if(!gn.getProperties().isEmpty()) {
                elem.setAttributeNS("sff", "properties", String.join(" ", gn.getProperties()));
            }
            for(Map.Entry<String, String> ent : gn.getAttributes().entrySet()) {
                elem.setAttribute(ent.getKey(), ent.getValue());
            }
            for(Node n : gn.getChildren()) {
                fillDOMElement(document, elem, n);
            }
        }
    }

    private void fillDOMElement(Document document, Element element, Node node) {
        if(node instanceof ProcessingInstructionNode) {
            element.appendChild(document.createProcessingInstruction(((ProcessingInstructionNode)node).getName(), ((ProcessingInstructionNode)node).getContent()));
        } else if(node instanceof CommentNode) {
            element.appendChild(document.createComment(((CommentNode)node).getContent()));
        } else if (node instanceof GroupNode) {
            GroupNode gn = (GroupNode)node;
            Element elem = document.createElement(gn.getName());
            element.appendChild(elem);
            if(!gn.getProperties().isEmpty()) {
                elem.setAttributeNS("https://www.cactis.eu/schema/sff", "sff:properties", String.join(" ", gn.getProperties()));
            }
            for(Map.Entry<String, String> ent : gn.getAttributes().entrySet()) {
                elem.setAttribute(ent.getKey(), ent.getValue());
            }
            for(Node n : gn.getChildren()) {
                fillDOMElement(document, elem, n);
            }
        } else if (node instanceof TextNode) {
            element.appendChild(document.createTextNode(((TextNode)node).getContent()));
        } else if (node instanceof PropertyNode) {
            PropertyNode pn = (PropertyNode)node;
            Element elem = document.createElement(pn.getName());
            element.appendChild(elem);
            if(!pn.getProperties().isEmpty()) {
                elem.setAttributeNS("https://www.cactis.eu/schema/sff", "sff:properties", String.join(" ", pn.getProperties()));
            }
            for(Map.Entry<String, String> ent : pn.getAttributes().entrySet()) {
                elem.setAttribute(ent.getKey(), ent.getValue());
            }
            elem.setAttributeNS("https://www.cactis.eu/schema/sff", "sff:content", pn.getContent());
        }

    }
}
