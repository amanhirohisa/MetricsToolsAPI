package org.computer.aman.metrics.size.loc;

import java.util.ArrayList;
import java.util.Iterator;

import org.computer.aman.io.sourcecode.SourceCodeLine;
import org.computer.aman.io.sourcecode.SourceFile;

/**
 * A set of objects related to LOC measurement.
 * <p></p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class LOCResultSet
{
	/**
	 * Initializes LOCResultSet object
	 * 
	 * @param aSourceFile the measurement target source file
	 */
	public LOCResultSet(final SourceFile aSourceFile)
	{
		lines = new ArrayList<SourceCodeLine>();
		sourceFile = aSourceFile;
	}

	/**
	 * Append a SourceCodeLine object to the list maintained in this LOCResultSet;
	 * A SourceCodeLine object is obtained through the LOC measurement.
	 * 
	 * @param aCodeLine SourceCodeLine object to be appended
	 */
	public void addContents(final SourceCodeLine aCodeLine)
	{
		lines.add(aCodeLine);
	}

    /**
     * Returns the number of blank lines.
     * 
     * @return the number of blank lines
     */
    public int getBlankCount()
    {   
        return blank;
    }

    /**
     * Returns the LOC value.
     * 
     * @return LOC value
     */
    public int getLOC()
    {   
        return loc;
    }

	/**
     * Retrieves the SourceFile object corresponding to the measured source file.
     *
     * @return corresponding SourceFile object
     */
    public SourceFile getSourceFile()
    {
        return sourceFile;
    }
	
	/**
	 * Returns the total line count including not only the code lines but also comments and blank lines.
	 * 
	 * @return the total line count
	 */
	public int getTotalLineCount()
	{
		return lines.size();
	}

	/** 
	 * Increments the count of blank lines.
	 */
	public void incrementBlankCount()
	{
	    blank++;
	}
	
	/**
	 * Increments the LOC value.
	 */
	public void incrementLOC()
	{
	    loc++;
	}
	
	/**
	 * Returns an iterator over the elements in the list of SourceCodeLine object in proper sequence.
	 *  
	 * @return an iterator over the elements in the list of SourceCodeLine object
	 */
	public Iterator<SourceCodeLine> iterator()
	{
		return lines.iterator();
	}
	
	/**
	 * Returns the string representation of this LOCResultSet
	 * 
	 * @return the string representation of this LOCResultSet
	 */
	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("[" + sourceFile.getName() + "]\n");
		buf.append("LOC = " + getLOC() + "\n");
		
		buf.append("# of lines = " + getTotalLineCount() + "\n\n");
		
		buf.append("--（source code）--------------------------------------\n");
        for (Iterator<SourceCodeLine> itr = iterator(); itr.hasNext(); ){
        	buf.append(itr.next());
        	buf.append("\n");
        }
		buf.append("--（end of source code）------------------------------\n");

        return new String(buf);
	}

	/** number of blank lines */
	private int blank;
	
    /** list of SourceCodeLine objects */
	private ArrayList<SourceCodeLine> lines;
	
	/** LOC value */
	private int loc;
	
	/** target source file */
	private SourceFile sourceFile;
}
