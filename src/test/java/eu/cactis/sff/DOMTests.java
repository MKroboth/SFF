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

        assertEquals(doc, new SFFDOMConverter().fromDOM(new SFFDOMConverter().toDOM(doc)));
    }
}
