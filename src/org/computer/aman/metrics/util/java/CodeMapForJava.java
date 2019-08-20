package org.computer.aman.metrics.util.java;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.LinkedList;

import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeLineMap;
import org.computer.aman.metrics.util.CodeMap;

/**
 * A code map representing the content of a Java source code.
 * <p></p>
 * A code map is a character-level map representing the content of a Java source file or a code fragment,
 * in which each of the elements is an integer meaning one of the followings: 
 * <ul>
 *  <li> 0: a white space </li>
 *  <li> 1: a source code (not blank, not comment)</li>
 *  <li> 2: a EOL comment (// ...) </li>
 *  <li> 3: a traditional comment ( /* .. ) </li>
 *  <li> 4: a Javadoc comment</li>
 * </ul>
 * <p></p>
 * An instance of CodeMapJava contains a list of CodeLineMapForJava which is 
 * a code line map for Java code.
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeMapForJava 
extends CodeMap
{
	private CodeMapForJava()
	{
		status = CODE;
		workingMapList = new LinkedList<String>();
		workingMap = new StringBuilder();
		commentContents = new StringBuilder();
	}
	
    /**
     * Creates a CodeMap object for a Java source file.
     * 
     * @param aSourceFile the source file 
     * @throws IOException if the reading of the source file is failed.
     */
    public CodeMapForJava(final SourceFile aSourceFile) 
    throws IOException
    {
    	this();
        
        LineNumberReader reader = new LineNumberReader(new FileReader(aSourceFile));
        String line = null;
        while ( (line = reader.readLine()) != null ){
        	parseLine(line);
        }

        for (Iterator<String> iterator = workingMapList.iterator(); iterator.hasNext();) {
            add(new CodeLineMapForJava(iterator.next()));            
        }

        reader.close();
    }    

    /**
     * Creates a CodeMap object for a Java code fragment (String array).
     * 
     * @param sourceLines Array of Java code lines
     */
    public CodeMapForJava(final String[] sourceLines)
    {
    	this();

    	for ( int i = 0; i < sourceLines.length; i++  ){
        	parseLine(sourceLines[i]);
        }

        for (Iterator<String> iterator = workingMapList.iterator(); iterator.hasNext();) {
            add(new CodeLineMapForJava(iterator.next()));            
        }
    }

    /**
     * Returns true if the given string seems to be commented code (comment-out code).
     * 
     * @param aLine string to be checked
     * @return true if the given string seems to be commented code
     */
    public boolean isCommentOut(final String aLine)
    {
        // Regard the line to be a commented code if the line ends with "{", "}" or ";",
    	// where "*/" is omitted.
    	char tail = ' ';
    	String str = aLine.trim();
    	if ( str.endsWith("*/") ){
    		int idx = str.length()-3;
    		while ( idx >= 0 ){
    			char ch = str.charAt(idx);
    			if ( ch != '*' && !Character.isWhitespace(ch) ){
    				tail = ch;
    				break;
    			}
    			idx--;
    		}
    	}
        return ( tail == '{' || tail == '}' || tail == ';' );
    }
    
    /**
     * Append the specified type of element to the working map (workingMap)
     * "aCount" times.
     * 
     * @param aTypeOfElement the type of map element
     * @param aCount repeat count
     */
    private void appendToWorkingMap(final String aTypeOfElement, final int aCount)
    {
        for ( int j = 0; j < aCount; j++ ){
            workingMap.append(aTypeOfElement);
        }
    }
    
    /**
     * Parses the specified source line and create the corresponding code map.
     * Since a statement or a comment block can be across two or more lines,
     * this method maintains the state of the parsing with using fields. 
     * 
     * @param aSourceLine source line to be parsed
     */
	private void parseLine( String aSourceLine )
	{
        int idx = 0;
        while ( idx < aSourceLine.length() ){    
            char ch = aSourceLine.charAt(idx);
            
            if ( Character.isWhitespace(ch) ){ 
                if ( status == TRADITIONAL ){
                	appendToWorkingMap(CodeLineMapForJava.TRADITIONAL_COMMENT, 1);
                    commentContents.append(ch);
                }
                else if ( status == STRING_LITERAL || status == CHAR_LITERAL ){
                	appendToWorkingMap(CodeLineMap.CODE, 1);
                }
                else if ( status == JAVADOC ){
                	appendToWorkingMap(CodeLineMapForJava.JAVADOC_COMMENT, 1);
                }
                else{                            
                	appendToWorkingMap(CodeLineMap.BLANK, 1);
                }
                idx++;
                continue;
            }

            if ( status == CODE ){ 
                if ( ch == '/' ){ 
                    String remainedPart = aSourceLine.substring(idx);
                    if ( remainedPart.startsWith("//") ){
                        idx += remainedPart.length();
                        appendToWorkingMap(isCommentOut(trimComment(remainedPart)) ? CodeLineMapForJava.EOL_COMMENT_OUT : CodeLineMapForJava.EOL_COMMENT, remainedPart.length());
                    }
                    else if ( remainedPart.startsWith("/**") ){
                        status = JAVADOC;
                        idx += "/**".length();
                        appendToWorkingMap(CodeLineMapForJava.JAVADOC_COMMENT, "/**".length());
                    }
                    else if ( remainedPart.startsWith("/*") ){
                        status = TRADITIONAL;
                        idx += "/*".length();
                        appendToWorkingMap(CodeLineMapForJava.TRADITIONAL_COMMENT, "/*".length());
                        commentContents.delete(0, commentContents.length());
                        commentContents.append("/*");
                    }
                    else{
                        idx++;
                        appendToWorkingMap(CodeLineMapForJava.CODE, 1);
                    }
                }
                else{
                    if ( ch == '\''){
                        status = CHAR_LITERAL;
                    }
                    else if ( ch == '"' ){
                        status = STRING_LITERAL;
                    }                        
                    idx++;
                    appendToWorkingMap(CodeLineMapForJava.CODE, 1);          
                }
                continue;
            }
            
            if ( status == TRADITIONAL ){
                if ( ch == '*' && idx+1 < aSourceLine.length() && aSourceLine.charAt(idx+1) == '/' ){
                    idx += "*/".length();
                    appendToWorkingMap(CodeLineMapForJava.TRADITIONAL_COMMENT, "*/".length());
                    commentContents.append("*/");
                    status = CODE;
                    if ( isCommentOut(trimComment(new String(commentContents))) ){
                        // replace the consecutive TRADITIONAL_COMMENT elements with TRADITIONAL_COMMENT_OUT elements 
                    	// in the working map
                        int length = commentContents.length();
                        for ( int i = workingMap.length()-1; i >= 0; i-- ){
                            workingMap.replace(i, i+1, CodeLineMapForJava.TRADITIONAL_COMMENT_OUT);
                            length--;
                            if ( length == 0 ){
                                break;
                            }
                        }
                        LinkedList<String> replacedMapList = new LinkedList<String>();
                        while ( length > 0 ){
                            StringBuilder buf = new StringBuilder(workingMapList.removeLast());
                            for ( int i = buf.length()-1; i >= 0; i-- ){
                                buf.replace(i, i+1, CodeLineMapForJava.TRADITIONAL_COMMENT_OUT);
                                length--;
                                if ( length == 0 ){
                                    break;
                                }
                            }
                            replacedMapList.addLast(new String(buf));
                        }
                        if ( replacedMapList.size() > 0 ){
                            workingMapList.addAll(replacedMapList);
                        }
                    }
                }
                else{
                    idx++;
                    appendToWorkingMap(CodeLineMapForJava.TRADITIONAL_COMMENT, 1);
                    commentContents.append(ch);
                }
                continue;
            }
            
            if ( status == JAVADOC ){
                if ( ch == '*' && idx+1 < aSourceLine.length() && aSourceLine.charAt(idx+1) == '/' ){
                    idx += "*/".length();
                    appendToWorkingMap(CodeLineMapForJava.JAVADOC_COMMENT, "*/".length());
                    status = CODE;
                }
                else{
                    idx++;
                    appendToWorkingMap(CodeLineMapForJava.JAVADOC_COMMENT, 1);
                }
                continue;
            }
            
            if ( status == CHAR_LITERAL ){
                idx++;
                appendToWorkingMap(CodeLineMapForJava.CODE, 1);
                if ( ch == '\\' ){ 
                    idx++;
                    appendToWorkingMap(CodeLineMapForJava.CODE, 1);
                }
                else if ( ch == '\'' ){
                    status = CODE;
                }
                continue;
            }
            
            if ( status == STRING_LITERAL ){
                idx++;
                appendToWorkingMap(CodeLineMapForJava.CODE, 1);
                if ( ch == '\\' ){
                    idx++;
                    appendToWorkingMap(CodeLineMapForJava.CODE, 1);
                }
                else if ( ch == '"' ){
                    status = CODE;
                }
                continue;
            }
        }

        workingMapList.add(new String(workingMap));
        workingMap.delete(0, workingMap.length());
	}
    

    /**
     * Erases the comment starting symbols and the ending symbols 
     * from the specified comment string.
     * 
     * @param aComment comment string to be processed
     * @return trimmed comment string
     */
    private String trimComment(final String aComment)
    {
        if ( aComment.startsWith("//") ){
            return aComment.substring(2);
        }
        if ( aComment.startsWith("/*") ){
            return aComment.substring(2, aComment.length()-2);
        }
        return null;
    }
    

    private int status;

    private final int CODE = 1;
    private final int TRADITIONAL = 12;
    private final int JAVADOC = 13;
    private final int STRING_LITERAL = 100;
    private final int CHAR_LITERAL = 200;
    
    private StringBuilder commentContents;
    private LinkedList<String> workingMapList;
    private StringBuilder workingMap;
}
