package mx.unam.ciencias.edd.proyecto2.svg;


import java.lang.reflect.Field;

/**
 * SVG Element
 * 
 * Contain basic attributes and methods that all elements inherit
 */
public abstract class Element {

    protected StringBuffer content;
    protected String tag;
    protected boolean selfClosing;

    // SVG attributes: https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute
    public String fill, stroke, style;
    public float opacity;


    // Initialize content with opening and closing tags
    protected Element(String tag, boolean selfClosing) {
        this.tag = tag; this.selfClosing = selfClosing;
        String initialTag = selfClosing ? "<" + tag + "/>" : "<" + tag + "></" + tag + ">\n";
        this.content = new StringBuffer(initialTag);
    }


    /**
     * Set property
     * 
     * Given a key:value pair, it will update the SVG content string
     * using that parameters and will call updateInstanceAttributes
     * to handle instance properties.
     * 
     * @param attr to be set
     * @param value to be set
     */
    public void setProperty(String attr, Object value) {
        // Construct control strings
        String regex = " " + attr + "=\"(.+)\"";
        String newContent = " " + attr + "=\"" + value.toString() + "\"";

        // Check if property is already part of the content
        boolean isPresent = this.content.toString().split(regex).length > 1;

        // Replace using regex and updating content if property already exists
        if(isPresent) {
            this.content = new StringBuffer(this.content.toString().replaceFirst(regex, newContent));
        } else { // Else, insert at the end
            int offset = this.selfClosing ? -2 : this.content.indexOf(">");
            this.content.insert(offset, newContent);
        }

        this.updateInstanceAttributes(attr, value);
    }


    /**
     * Set content
     * 
     * If element is not of the self closing type, it will place the
     * given content inside the opening an closing tags.
     * 
     * @param content to be placed inside tags
     * @throws IllegalArgumentException when element is of the self-closing type
     */
    public void setContent(String content) {
        if(this.selfClosing) { throw new IllegalArgumentException(); }
        this.content.replace(
            this.content.indexOf(">") + 1,
            this.content.indexOf("</"),
            content
        );
    }


    /**
     * Update instance attributes.
     * 
     * Given a key:value pair, it will try to update the instance property
     * named after key with the given value. If something fails, like there's
     * no property with that name or value type is incorrect, it will do nothing
     * and keep the execution going.
     * 
     * @param attr attribute name
     * @param value to be updated
     */
    private void updateInstanceAttributes(String attr, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(attr);
            field.set(this, value);
        } catch (Exception e) { } // Fail silently
    }

    // Return content's string representation
    @Override
    public String toString() {
        return this.content.toString();
    }

}