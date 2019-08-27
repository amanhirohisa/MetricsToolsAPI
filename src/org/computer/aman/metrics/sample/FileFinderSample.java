package org.computer.aman.metrics.sample;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.computer.aman.io.FileFinder;

/**
 * A sample code showing how to use FileFinder.
 * 
 * @author Hirohisa Aman &lt;aman@computer.org&gt;
 */
public class FileFinderSample {

	public static void main(String[] args) 
	{
		FileFinder finder;
		try {
			System.out.println("=== Files in or below the current directory. ===");
			finder = new FileFinder();
			for (Iterator<String> iterator = finder.getList().iterator(); iterator.hasNext();) {
				System.out.println(iterator.next());
			}
			
			System.out.println();
			System.out.println("=== Java files in or below the current directory. ===");
			for (Iterator<String> iterator = finder.getList(".+\\.java").iterator(); iterator.hasNext();) {
				System.out.println(iterator.next());
			}
			
			System.out.println();
			System.out.println("=== C files in or below the current directory. ===");
			for (Iterator<String> iterator = finder.getList(".+\\.c").iterator(); iterator.hasNext();) {
				System.out.println(iterator.next());
			}

			if ( args.length > 0 ){
				finder = new FileFinder(args[0]);
				System.out.println();
				System.out.println("=== Java files in or below " + args[0] + " ===");
				for (Iterator<String> iterator = finder.getList(".+\\.java").iterator(); iterator.hasNext();) {
					System.out.println(iterator.next());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
