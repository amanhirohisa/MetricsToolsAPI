package org.computer.aman.metrics.size.loc;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceCodeLine;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeLineMap;
import org.computer.aman.metrics.util.CodeMap;
import org.computer.aman.metrics.util.CodeMapFactory;

/**
 * A class for measuring LOC of a source file.
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class LOCCounter
{
    /**
     * Measures the LOC value of the specified source file and returns the result.
     * 
     * @param aSourceFile source file to be measured
     * @return the results of LOC measurement
     * @throws IOException if an error occurred while the source file is opened
     * @throws NotSupportedSourceFileExeption if the specified source file is not supported 
     */
    public static LOC measure(final SourceFile aSourceFile)
    throws NotSupportedSourceFileExeption, IOException
	{
        return measure(aSourceFile, 1, -1);
	}
    
    /**
     * Measures the LOC value of the specified source file and returns the result.
     * 
     * @param aSourceFile source file to be measured
     * @param aBeginLineNumber the line number at which the LOC measurement begins 
     * @return the results of LOC measurement
     * @throws IOException if an error occurred while the source file is opened
     * @throws NotSupportedSourceFileExeption if the specified source file is not supported 
     */
    public static LOC measure(final SourceFile aSourceFile, final int aBeginLineNumber)
    throws NotSupportedSourceFileExeption, IOException
    {
        return measure(aSourceFile,aBeginLineNumber,-1);
    }

    /**
     * Measures the LOC value of the specified source file and returns the result.
     * 
     * @param aSourceFile source file to be measured
     * @param aBeginLineNumber the line number at which the LOC measurement begins 
     * @param anEndLineNumber the line number at which the LOC measurement ends (-1 means the end of the file)
     * @return the results of LOC measurement
     * @throws IOException if an error occurred while the source file is opened
     * @throws NotSupportedSourceFileExeption if the specified source file is not supported 
     */
    public static LOC measure(final SourceFile aSourceFile, final int aBeginLineNumber, final int anEndLineNumber)
    throws NotSupportedSourceFileExeption, IOException
    {
        // check if the specified source file is supported or not
        if ( !aSourceFile.isJavaFile() && !aSourceFile.isCFile() ){
            throw new NotSupportedSourceFileExeption("not supported file type: " + aSourceFile);
        }

        LOC result = new LOC(aSourceFile);
        
        CodeMap map = CodeMapFactory.create(aSourceFile);
        
        LineNumberReader reader = new LineNumberReader(new FileReader(aSourceFile));

        Iterator<CodeLineMap> itr = map.iterator();
        int loc = 0;
        int lineNumber = 0;        
        while ( itr.hasNext() ){
            CodeLineMap lineMap = itr.next();
            lineNumber++;
            if ( lineNumber < aBeginLineNumber ){
                continue;
            }
            if ( anEndLineNumber > 0 && anEndLineNumber < aBeginLineNumber ){
                continue;
            }
            if ( lineNumber-1 == anEndLineNumber ){
                break;
            }

            SourceCodeLine codeLine = new SourceCodeLine(reader.readLine());

            if ( lineMap.getCodeCount() > 0 ){
                loc++;
                codeLine.setLineNumber(loc);
                result.incrementLOC();
            }
            else{                
                codeLine.setLineNumber(-1);
                if ( lineMap.isBlankLine() ){
                    result.incrementBlankCount();
                }
            }
            result.addContents(codeLine);
        }

        reader.close();
        
        return result;        
    }
}
