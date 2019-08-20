package org.computer.aman.metrics.util.c;

import org.computer.aman.metrics.util.CodeLineMap;

/**
 * A code map of a C source line.
 * <p></p>
 * A code map represents each character of a source code 
 * by the type of it; 
 * a type of character is one of the following types:<br>
 * (1) a character in a code, 
 * (2) a white space, 
 * (3a) a character within a line comment ("//" style),
 * (3b) a character within a traditional comment (C style comment),
 * (4a) a character within a commented code (line comment), and
 * (4b) a character within a commented code (C style comment).
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeLineMapForC 
extends CodeLineMap
{
    /** code map code for a line comment (// style) */
    public static final String EOL_COMMENT = "2";
    
    /** code map code for a commented code in a line comment */
    public static final String EOL_COMMENT_OUT = "5";
    
    /** code map code for a traditional (C style) comment */
    public static final String TRADITIONAL_COMMENT = "3";
    
    /** code map code for a commented code in a traditional (C style) comment */
    public static final String TRADITIONAL_COMMENT_OUT = "6";
    
    /**
     * Constructs a code map of a C source line.
     * 
     * @param aMap a code map of a C source line
     */
    public CodeLineMapForC(final String aMap)
    {
        super(aMap);
        
        for (int i = 0; i < aMap.length(); i++ ){
            String ithChar = Character.toString(aMap.charAt(i));
            if ( EOL_COMMENT.equals(ithChar) ){
                eolCommentCount++;
            }
            else if ( TRADITIONAL_COMMENT.equals(ithChar) ){
                traditionalCommentCount++;
            }
            else if ( EOL_COMMENT_OUT.equals(ithChar) ){
                eolCommentOutCount++;
            }
            else if ( TRADITIONAL_COMMENT_OUT.equals(ithChar) ){
                traditionalCommentOutCount++;
            }
        }
    }

    /**
     * Retrieves the number of characters belong to EOL_COMMENT type.
     * 
     * @return the number of characters belong to EOL_COMMENT type
     */
    public int getEolCommentCount()
    {
        return eolCommentCount;
    }
    
    /**
     * Retrieves the number of characters belong to EOL_COMMENT_OUT type.
     * 
     * @return the number of characters belong to EOL_COMMENT_OUT type
     */
    public int getEolCommentOutCount()
    {
        return eolCommentOutCount;
    }

    /**
     * Retrieves the number of characters belong to TRADITIONAL_COMMENT type.
     * 
     * @return the number of characters belong to TRADITIONAL_COMMENT type
     */
    public int getTraditionalCommentCount()
    {
        return traditionalCommentCount;
    }

    /**
     * Retrieves the number of characters belong to TRADITIONAL_COMMENT_OUT type.
     * 
     * @return the number of characters belong to TRADITIONAL_COMMENT_OUT type
     */
    public int getTraditionalCommentOutCount()
    {
        return traditionalCommentOutCount;
    }

    /**
     * Returns the string representation of this CodeLineMapForC in the following format:
     * <br>
     *  (b?,c?,#?:t?,e?,ec?,tc?) ....... 
     * <br>
     * The first three ? parts are the character counts of blanks, code and comments, respectively.
     * The remaining ? parts are the details of the comment counts:
     * the character counts of traditional comments, eol comments, eol-style commented code, 
     * traditional-style commented code.
     * <br>
     * The ...... part corresponds to the code map.
     * 
     * @return the string representation of this CodeLineMapForC
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();        
        buf.append("(b" + getBlankCount() + ",c" + getCodeCount() + ",#" + getCommentCount() + ":");
        buf.append("t" + getTraditionalCommentCount() + ",");
        buf.append("e" + getEolCommentCount() + ",");
        buf.append("ec" + getEolCommentOutCount() + ",");
        buf.append("tc" + getTraditionalCommentOutCount() + ")");
        buf.append(" ");
        buf.append(getMap());
        
        return new String(buf);
    }

    /** the number of characters corresponding to EOL_COMMENT */
    private int eolCommentCount;
    
    /** the number of characters corresponding to EOL_COMMENT_OUT */
    private int eolCommentOutCount;

    /** the number of characters corresponding to TRADITIONAL_COMMENT */
    private int traditionalCommentCount;

    /** the number of characters corresponding to TRADITIONAL_COMMENT_OUT */
    private int traditionalCommentOutCount;
}
