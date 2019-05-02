package eu.cactis.sff.tests;

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
