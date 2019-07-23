package Crosswalking.XML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Stack;

public class XMLStack{
    Stack<Element> stack;
    XMLDocument doc;

    public XMLStack(XMLDocument doc) {
        this.doc = doc;
        stack = new Stack<>();
    }

    public Element pop(){
        return stack.pop();
    }

    public void push(Element e){
        stack.push(e);
    }

    public Element peek(){
        return stack.peek();
    }

    public Boolean empty(){
        return stack.isEmpty();
    }

    public Element zip(Element child) {
        Element parent;
        while (!stack.empty()) {
            parent = stack.pop();
            parent.appendChild(child);
            child = parent;
        }
        return child;
    }
}
