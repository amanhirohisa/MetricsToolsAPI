package org.computer.aman.metrics.sample;

import java.io.IOException;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.size.loc.LOC;
import org.computer.aman.metrics.size.loc.LOCCounter;

/**
 * Sample code for measuring the LOC value of a Java source file.
 * 
 * @author Hirohisa Aman &lt;aman@computer.org&gt;
 */
public class LOCCounterSample {

	public static void main(String[] args) 
	{
		// open a Java source file specified as the command line parameter,
		// and measure the LOC of it
		try {
			System.out.println(new SourceFile(args[0]));
			LOC results = LOCCounter.measure(new SourceFile(args[0]));
			System.out.println(results);
		} catch (SecurityException | NotSupportedSourceFileExeption | IOException e) {
			e.printStackTrace();
		}
	}

}
