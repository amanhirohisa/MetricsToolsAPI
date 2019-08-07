package org.computer.aman.metrics.util;

/**
 * Class of an object for maintaining a code map of a code line.
 * 
 * <p></p>
 * A code map is an array of integers expressing the type of each character.
 * Type of character: 
 * (a) a part of an executable code (expressed by constant CODE),
 * (b) a white space character (expressed by constant BLANK),
 * (c) a part of a comment (depend on the programming language, defined by the corresponding subclass).
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeLineMap
{
    public static final String BLANK = "0";
    public static final String CODE = "1";
    
    /**
     * Constructs a code map of a source line.
     * 
     * @param aMap a code map of a source line
     */
    public CodeLineMap(final String aMap)
    {
        map = new String(aMap != null ? aMap : "");

        blankCount = 0;
        codeCount = 0;
        commentCount = 0;
        for (int i = 0; i < map.length(); i++ ){
            String ithChar = Character.toString(map.charAt(i));
            if ( BLANK.equals(ithChar) ){
                blankCount++;
            }
            else if ( CODE.equals(ithChar) ){
                codeCount++;
            }
            else{
                commentCount++;
            }
        }
    }
    
    /**
     * Retrieves the number of characters belong to BLANK type.
     * @return the number of characters belong to BLANK type
     */
    public int getBlankCount()
    {
        return blankCount;
    }

    /**
     * Retrieves the number of characters belong to CODE type.
     * @return the number of characters belong to CODE type
     */
    public int getCodeCount()
    {
        return codeCount;
    }

    /**
     * Retrieves the number of characters belong to coment type.
     * @return the number of characters belong to comment type
     */
    public int getCommentCount()
    {
        return commentCount;
    }
    
    /**
     * Retrieves the code map.
     * @return the code map
     */
    public String getMap()
    {
        return map;
    }
    
    
    /**
     * Returns true if the character at the specified index is in a comment.
     * 
     * @param anIndex index of character to be checked
     * @return true if the character at the specified index is in a comment
     */
    public boolean isComment( final int anIndex )
    {
    	final String CH = Character.toString(map.charAt(anIndex));
    	
    	return ( !CH.equals(CODE) ) && ( !CH.equals(BLANK) );
    }
    
    /**
     * Returns true if this line consists of only white spaces.
     * 
     * @return true if this line consists of only white spaces
     */
    public boolean isBlankLine()
    {
        return (codeCount == 0) && (commentCount == 0);
    }
    
    private int blankCount;
    
    private int codeCount;
    
    private int commentCount;
    
    private String map;
}
