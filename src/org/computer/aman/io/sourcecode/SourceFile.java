package org.computer.aman.io.sourcecode;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * A model of a source file
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class SourceFile 
extends File
{
    /**
     * Constructs a source file model corresponding to the specified path.
     * 
     * @param aPathName path to the source file
     * @throws FileNotFoundException if the specified file is not found
     * @throws SecurityException if the specified file cannot be opened
     */
    public SourceFile(final String aPathName) 
    throws FileNotFoundException, SecurityException
    {
        super(aPathName);
        if ( !isFile() ){
            throw new FileNotFoundException("File not found: " + aPathName);
        }
        if ( !canRead() ){
            throw new SecurityException("can't open file: " + aPathName);
        }
    }

    /**
     * Retrieves the extension of this source file.
     * 
     * @return the extention of this source file
     */
    public String getExtension()
    {
        String fileName = getName().toLowerCase();
        int index = fileName.lastIndexOf('.');

        return fileName.substring(index+1);
    }

    /**
     * Returns true if this file is a Java source file (.java).
     * 
     * @return true if this file is a Java source file (.java)
     */
    public boolean isJavaFile()
    {
        return getExtension().equalsIgnoreCase("java");
    }
    
    /**
     * Returns true if this file is a C/C++ source file or a related file (.c, .cpp, .cc, .cxx, .h, .hpp, .hxx, m, i, ii).
     * 
     * @return true if this file is a C/C++ source file or a related file (.c, .cpp, .cc, .cxx, .h, .hpp, .hxx, m, i, ii)
     */
    public boolean isCFile()
    {
    	final String EXT = getExtension();
        return EXT.equalsIgnoreCase("c") ||
                EXT.equalsIgnoreCase("cpp") ||
                EXT.equalsIgnoreCase("cc") ||
                EXT.equalsIgnoreCase("cxx") ||
                EXT.equalsIgnoreCase("h") ||
                EXT.equalsIgnoreCase("hpp") ||
                EXT.equalsIgnoreCase("hxx") ||
                EXT.equalsIgnoreCase("m") ||
                EXT.equalsIgnoreCase("i") ||
                EXT.equalsIgnoreCase("ii");
    }
    
    private static final long serialVersionUID = 2008061301L;
}
