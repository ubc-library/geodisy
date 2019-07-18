package Crosswalking.XML;

import org.w3c.dom.Element;

public class XMLElement extends ElementOrStack {
    Element e;
    public XMLElement(XMLDocument doc, Element e) {
        super(doc);
        this.e = e;
    }

    public Element getElement(){
        return e;
    }
}
