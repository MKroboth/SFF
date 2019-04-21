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

import eu.cactis.sff.parser.ParseException;
import eu.cactis.sff.parser.SFFParser;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
           List<Node> nodes =  parser.Start();

           for(Node node : nodes) {
               String nres = formatter.formatNode(node);
               System.out.println(nres);
           }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
