package eu.cactis.sff;

import eu.cactis.sff.parser.SFFParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Document {
    private List<Node> nodes = new LinkedList<>();

    public Collection<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Collection<Node> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }

    public void appendChild(Node node) {
        nodes.add(node);
    }


    public Document() {
    }

    public Document(Collection<Node> nodes) {
        this.nodes.addAll(nodes);
    }

    public static Document fromByteBuffer(ByteBuffer bb) throws Exception {
        byte[] bts = new byte[bb.limit()];
        bb.get(bts);

        InputStream bais = new ByteArrayInputStream(bts);
        SFFParser parser = new SFFParser(bais);
        Document ret = new Document(parser.Start());
        bais.close();

        return ret;
    }
}
