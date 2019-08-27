package org.computer.aman.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * A class that recursively finds all files in or below the specified directory.   
 * 
 * <p></p>
 * <u><b>(Example 1) getting a list of all files in or below the current directory.</b></u>
 * 
 * <pre>
 * FileFinder <i>finder</i> = new FileFinder(); 
 * List&lt;String&gt; <i>list</i> = <i>finder</i>.getList();
 * </pre>
 * 
 * <p></p>
 * <u><b>(Example 2) getting a list of all Java source files (*.java) in or below "/foo/bar".</b></u>
 * 
 * <pre>
 * FileFinder <i>finder</i> = new FileFinder(<i>/foo/bar</i>); 
 * List&lt;String&gt; <i>list</i> = <i>finder</i>.getList(<i>".+\\.java"</i>);
 * </pre>
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 * @version 1.0
 */
public class FileFinder
{
    /** 
     * Creates a FileFinder object that searches files in or below the current directory.
     * 
     * @throws FileNotFoundException if the current directory is not accessible
     */
    public FileFinder() 
    throws FileNotFoundException
    {
        this(".");	
    }
    
    /**
     * Creates a FileFinder object that searches files in or below the specified directory.
     * 
     * @param aRootDir the root directory of the file search 
     * @throws FileNotFoundException if the specified directory is not accessible
     */
    public FileFinder(final String aRootDir)
    throws FileNotFoundException
    {
        directory = new File(aRootDir);

        if ( !directory.canRead() ){
            throw new FileNotFoundException("could not open : " + aRootDir);
        }
    }
    
    /**
     * Returns the path of the root directory of the file search 
     * 
     * @return the path of the root directory of the file search 
     */
    public String getDirectory()
    {
        return directory.getPath();
    }
    
    /**
     * Returns the list of all files (file paths) in or below the search root directory.
     * 
     * @return the list of all files (file paths) in or below the search root directory
     * @throws FileNotFoundException if there is a unaccessible directory
     */
    public List<String> getList() 
    throws FileNotFoundException
    {
        return getList(".+");
    }
    
    /**
     * Returns the list of all files (file paths) which the specified regular expression matches, 
     * in or below the search root directory.
     * 
     * @param aPattern the regular expression corresponding to the files to be found
     * @return the list of all files (file paths) which the specified regular expression matches
     * @throws FileNotFoundException if there is a unaccessible directory
     */
    public List<String> getList(final String aPattern) 
    throws FileNotFoundException
    {
    	LinkedList<String> list = new LinkedList<String>();

        if ( directory.isFile() ){ 
        	if ( directory.getPath().matches(aPattern) ){
        		list.add(directory.getPath());
        	}
        	return list;
        }
        
        String[] files = directory.list();
        for ( int i = 0; i < files.length; i++ ){
            File f = new File(directory.getPath() + File.separator + files[i]);
            if ( f.isDirectory() ){
                FileFinder subFinder = new FileFinder(f.getPath());
                list.addAll(subFinder.getList(aPattern));
            }
            else if ( f.getPath().matches(aPattern) ) {                
                list.add(f.getPath());
            }
        }
        return list;
    }
            
    /** the root directory of this file search */
    private File directory;
}
