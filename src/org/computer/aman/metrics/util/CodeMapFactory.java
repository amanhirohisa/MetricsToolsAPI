package org.computer.aman.metrics.util;

import java.io.IOException;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.c.CodeMapForC;
import org.computer.aman.metrics.util.java.CodeMapForJava;

/**
 * A class designed to produce a CodeMap object.
 * The produced CodeMap is a CodeMapForJava object or a CodeMapForC object;
 * The selection is decided by the file extension of the specified source file. 
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeMapFactory
{
    /**
     * Produces a CodeMap object which is appropriate to the specified source file.
     * 
     * @param aSourceFile source file from which the code map is produced
     * @return CodeMap object
     * @throws NotSupportedSourceFileExeption if the specified source file is in not supported language
     * @throws IOException if an IO exception occurred 
     */
    public static CodeMap create(final SourceFile aSourceFile) 
    throws NotSupportedSourceFileExeption, IOException
    {
        if ( aSourceFile.isJavaFile() ){
            return new CodeMapForJava(aSourceFile);
        }
        if ( aSourceFile.isCFile() ){
            return new CodeMapForC(aSourceFile);
        }
        
        return null;
    }
}
