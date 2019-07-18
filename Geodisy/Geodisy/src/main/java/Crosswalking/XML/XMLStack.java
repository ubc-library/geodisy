package Crosswalking.XML;

import org.w3c.dom.Element;

import java.util.Stack;

public class XMLStack extends ElementOrStack {
    Stack<ElementOrStack> stack;

    public XMLStack(XMLDocument doc) {
        super(doc);
        stack = new Stack<>();
    }

    public ElementOrStack pop(){
        return stack.pop();
    }

    public void push(ElementOrStack e){
        stack.push(e);
    }

    public ElementOrStack peek(){
        return stack.peek();
    }

    public Boolean empty(){
        return stack.isEmpty();
    }

    public Element zip(){
        if(stack.peek().getClass().isInstance(XMLStack.class))
            return doc.create_Element("__no elements__");
        XMLElement temp = (XMLElement) stack.pop();
        Element e = temp.getElement();
        while(stack.peek().getClass().isInstance(XMLElement.class)){
            XMLElement parent = (XMLElement) stack.pop();
            Element parentE = parent.getElement();
            parentE.appendChild(e);
            e = parentE;
        }
        return e;
    }
}
