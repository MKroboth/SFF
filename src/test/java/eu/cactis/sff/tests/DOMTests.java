package eu.cactis.sff.tests;

import eu.cactis.sff.Document;
import eu.cactis.sff.SFFDOMConverter;
import org.junit.jupiter.api.Test;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStreamReader;
import java.io.Reader;

import java.io.StringWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DOMTests {
    @Test
    void testDomStuff() {
        Reader sr = new InputStreamReader(getClass().getResourceAsStream("/db.sff"));
        Scanner sc = new Scanner(sr);

        StringBuffer buf = new StringBuffer();
        while (sc.hasNext()) {
            buf.append(sc.nextLine()).append('\n');
        }

        Document doc = assertDoesNotThrow(() -> Document.fromString(buf.toString()));

        DOMSource src = new DOMSource(new SFFDOMConverter().toDOM(doc));
        StringWriter sw = new StringWriter();

        Transformer t = assertDoesNotThrow(() -> {
            TransformerFactory tf = TransformerFactory.newInstance();
            return tf.newTransformer();
        });
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        try {
            t.transform(src, new StreamResult(sw));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        System.out.println(sw.getBuffer().toString());
        assertEquals(doc, new SFFDOMConverter().fromDOM(new SFFDOMConverter().toDOM(doc)));
    }
}
