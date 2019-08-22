package org.computer.aman.metrics.util.c;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.LinkedList;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeLineMap;
import org.computer.aman.metrics.util.CodeMap;

/**
 * A code map representing the content of a C source code.
 * <p></p>
 * A code map is a character-level map representing the content of a C source file or a code fragment,
 * in which each of the elements is an integer meaning one of the followings: 
 * <ul>
 *  <li> 0: a white space </li>
 *  <li> 1: a source code (not blank, not comment)</li>
 *  <li> 2: a EOL comment (// ...) </li>
 *  <li> 3: a traditional comment ( /* .. ) </li>
 * </ul>
 * <p></p>
 * An instance of CodeMapC contains a list of CodeLineMapForC which is 
 * a code line map for C code.
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeMapForC 
extends CodeMap
{
	private CodeMapForC()
	{
		status = CODE;
		workingMapList = new LinkedList<String>();
		workingMap = new StringBuilder();
		commentContents = new StringBuilder();
	}
	
    /**
     * Creates a CodeMap object for a C source file.
     * 
     * @param aSourceFile the source file 
     * @throws IOException if the reading of the source file is failed.
     */
    public CodeMapForC(final SourceFile aSourceFile) 
    throws IOException
    {        
    	this();
        
        LineNumberReader reader = new LineNumberReader(new FileReader(aSourceFile));
        String line = null;
        while ( (line = reader.readLine()) != null ){
        	parseLine(line);
        }

        for (Iterator<String> iterator = workingMapList.iterator(); iterator.hasNext();) {
            add(new CodeLineMapForC(iterator.next()));            
        }

        reader.close();
    }

    /**
     * Creates a CodeMap object for a C code fragment (String array).
     * 
     * @param sourceLines Array of C code lines
     */
    public CodeMapForC(final String[] sourceLines)
    {
    	this();

    	for ( int i = 0; i < sourceLines.length; i++  ){
        	parseLine(sourceLines[i]);
        }

        for (Iterator<String> iterator = workingMapList.iterator(); iterator.hasNext();) {
            add(new CodeLineMapForC(iterator.next()));            
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
                	appendToWorkingMap(CodeLineMapForC.TRADITIONAL_COMMENT, 1);
                    commentContents.append(ch);
				}
				else if ( status == STRING_LITERAL || status == CHAR_LITERAL ){
					appendToWorkingMap(CodeLineMap.CODE, 1);
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
                        appendToWorkingMap(isCommentOut(trimComment(remainedPart)) ? CodeLineMapForC.EOL_COMMENT_OUT : CodeLineMapForC.EOL_COMMENT, remainedPart.length());
                    }
                    else if ( remainedPart.startsWith("/*") ){
                        status = TRADITIONAL;
                        idx += "/*".length();
                        appendToWorkingMap(CodeLineMapForC.TRADITIONAL_COMMENT, "/*".length());
                        commentContents.delete(0, commentContents.length());
                        commentContents.append("/*");
                    }
                    else{
                        idx++;
                        appendToWorkingMap(CodeLineMapForC.CODE, 1);
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
                    appendToWorkingMap(CodeLineMapForC.CODE, 1);          
                }
                continue;
			}
                
			if ( status == TRADITIONAL ){
				if ( ch == '*' && idx+1 < aSourceLine.length() && aSourceLine.charAt(idx+1) == '/' ){
                    idx += "*/".length();
                    appendToWorkingMap(CodeLineMapForC.TRADITIONAL_COMMENT, "*/".length());
                    commentContents.append("*/");
                    status = CODE;
                    if ( isCommentOut(trimComment(new String(commentContents))) ){
                        // replace the consecutive TRADITIONAL_COMMENT elements with TRADITIONAL_COMMENT_OUT elements 
                    	// in the working map
                        int length = commentContents.length();
                        for ( int i = workingMap.length()-1; i >= 0; i-- ){
                            workingMap.replace(i, i+1, CodeLineMapForC.TRADITIONAL_COMMENT_OUT);
                            length--;
                            if ( length == 0 ){
                                break;
                            }
                        }
                        LinkedList<String> replacedMapList = new LinkedList<String>();
                        while ( length > 0 ){
                            StringBuilder buf = new StringBuilder(workingMapList.removeLast());
                            for ( int i = buf.length()-1; i >= 0; i-- ){
                                buf.replace(i, i+1, CodeLineMapForC.TRADITIONAL_COMMENT_OUT);
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
					appendToWorkingMap(CodeLineMapForC.TRADITIONAL_COMMENT, 1);
					commentContents.append(ch);
				}
				continue;
            }

            if ( status == CHAR_LITERAL ){
                idx++;
                appendToWorkingMap(CodeLineMapForC.CODE, 1);
                if ( ch == '\\' ){ 
                    idx++;
                    appendToWorkingMap(CodeLineMapForC.CODE, 1);
                }
                else if ( ch == '\'' ){
                    status = CODE;
                }
                continue;
            }
            
            if ( status == STRING_LITERAL ){
                idx++;
                appendToWorkingMap(CodeLineMapForC.CODE, 1);
                if ( ch == '\\' ){
                    idx++;
                    appendToWorkingMap(CodeLineMapForC.CODE, 1);
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
    private final int STRING_LITERAL = 100;
    private final int CHAR_LITERAL = 200;
    
    private StringBuilder commentContents;
    private LinkedList<String> workingMapList;
    private StringBuilder workingMap;
}
