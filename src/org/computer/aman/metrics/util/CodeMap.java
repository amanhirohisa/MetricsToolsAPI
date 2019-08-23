package org.computer.aman.metrics.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Code map expressing the types of source code.
 * <p></p>
 * A code map is a map that expresses the content of source code, where the mapping unit is a character.<br>
 * For example, the following code fragment<br>
 * <pre>   public int x; // foo</pre>
 * is expressed as 
 * <pre>00011111101110110220222</pre>
 * where 0, 1, and 2 signify a blank, a code, and a comment, respectively.
 * <p></p>
 * An instance of this class has a list of instances of CodeLineMap class;
 * An instance of CodeLineMap is a code map for a source line.
 * <p></p>
 * The values used in a map are defined in a subclass of CodeLineMap, which is specialized for a programming language.
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public abstract class CodeMap
{    
    /**
     * Returns true if the given string seems to be commented code (comment-out code).
     * 
     * @param aLine string to be checked
     * @return true if the given string seems to be commented code
     */
    public abstract boolean isCommentOut(final String aLine);
    
    
    /**
     * Returns an iterator over the elements (CodeLineMap objects) in this code map in proper sequence. 
     * 
     * @return an iterator over the elements (CodeLineMap objects) in this code map in proper sequence
     */
    public Iterator<CodeLineMap> iterator()
    {
    	if ( lines == null ){
            lines = new ArrayList<CodeLineMap>();
        }
        return lines.iterator();
    }
    
    /**
     * Returns a string representation of this code map.
     * 
     * @return a string representation of this code map
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        int lineNumber = 1;
        for (Iterator<CodeLineMap> iterator = lines.iterator(); iterator.hasNext();) {
            CodeLineMap element = iterator.next();
            buf.append(lineNumber++);
            buf.append(element.toString());
            buf.append("\n");
        }
        
        return new String(buf);
    }
    
    /**
     * Appends the specified CodeLineMap object to the end of this cod map.
     * 
     * @param aLineMap CodeLineMap object to be appended to this list
     */
    protected void add(final CodeLineMap aLineMap)
    {
        if ( lines == null ){
            lines = new ArrayList<CodeLineMap>();
        }
        lines.add(aLineMap);
    }
      
    /** 
     * list of CodeLineMap objects
     */
    private ArrayList<CodeLineMap> lines;
}
