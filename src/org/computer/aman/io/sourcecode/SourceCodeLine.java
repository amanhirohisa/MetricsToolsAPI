package org.computer.aman.io.sourcecode;

/**
 * Class of an object for maintaining the line content of a source code and its line number.
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class SourceCodeLine
{
	/**
	 * Constructs a line of source code model.
	 * 
	 * @param anyContents the content of source code line
	 */
	public SourceCodeLine(final String anyContents)
	{
		contents = anyContents;
		lineNumber = -1;
	}
	
	/**
	 * Retrieves the line content.
	 * 
	 * @return the line content
	 */
	public String getContents()
	{
		return contents;
	}

	/**
	 * Retrieves the line number.
	 * (-1 if the line is comment-only or blank line)
	 * 
	 * @return the line number
	 */
	public int getLineNumber()
	{
		return lineNumber;
	}

	/**
	 * Sets the line number.
	 * 
	 * @param aLineNumber the line number to be set
	 */
	public void setLineNumber(final int aLineNumber)
	{
		if ( aLineNumber > 0 ){
			lineNumber = aLineNumber;
		}
	}
	
	/**
	 * Retrieves the line content together with its line number.
	 * 
	 * @return the line content together with its line number
	 */
	public String toString()
	{
		return (lineNumber > 0 ? lineNumber + ": " : "   ") + contents;
	}
	
	private String contents;
	
	private int lineNumber;
}
