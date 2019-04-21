package eu.cactis.sff;

import eu.cactis.sff.parser.ParseException;
import eu.cactis.sff.parser.SFFParser;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
public class TMain {
    public static void main(String[] args) {

    Node root;
    GroupNode charac = new GroupNode();
    root = charac;

    charac.setName("character");
    charac.setProperties("essentia/lux-enigma/classical/mains/Vaati-Von-Pendragon");

    do {
            PropertyNode name = new PropertyNode();
            name.setName("name");
            name.setContent("Vaati von Pendragon");
            name.setProperties("%firstname %presurname %surname");

            charac.appendChild(name);

            PropertyNode age = new PropertyNode();
            age.setName("age");
            age.setContent("21");

            charac.appendChild(age);
    } while(false);

    CSFFFormatter formatter = new CSFFFormatter();
    String res = formatter.formatNode(root);
    System.out.println(res);
        SFFParser parser = new SFFParser(new StringReader(res));
        parser.enable_tracing();
        try {
           Node node =  parser.Start();

    String nres = formatter.formatNode(node);
    System.out.println(nres);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
