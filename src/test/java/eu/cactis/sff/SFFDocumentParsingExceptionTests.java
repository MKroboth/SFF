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

import eu.cactis.sff.SFFDocumentParsingException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SFFDocumentParsingExceptionTests {
    @Test
    void testConstructors() {
        SFFDocumentParsingException ex = new SFFDocumentParsingException();
        assertEquals("Error during document parsing.", ex.getMessage());
        RuntimeException rex = new RuntimeException();
        ex = new SFFDocumentParsingException(rex);
        assertEquals(rex, ex.getCause());
        ex = new SFFDocumentParsingException("Some err", rex);
        assertEquals(rex, ex.getCause());
        assertEquals("Some err", ex.getMessage());
        ex = new SFFDocumentParsingException("message", rex, false, true);
        assertEquals("message", ex.getMessage());
        assertEquals(rex, ex.getCause());

    }
}
