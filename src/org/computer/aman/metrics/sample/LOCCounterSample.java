package org.computer.aman.metrics.sample;

import java.io.IOException;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.size.loc.LOC;
import org.computer.aman.metrics.size.loc.LOCCounter;
import org.computer.aman.metrics.util.CodeMap;
import org.computer.aman.metrics.util.java.CodeMapForJava;

/**
 * Sample code for measuring the LOC value of a Java or C/C++ source file.
 * 
 * @author Hirohisa Aman &lt;aman@computer.org&gt;
 */
public class LOCCounterSample {

	public static void main(String[] args) 
	{
		// Sample 1: measure a source file 
		//
		// open a Java or C/C++ source file specified as the command line parameter,
		// and measure the LOC of it
		try {
			System.out.println(new SourceFile(args[0]));
			LOC results = LOCCounter.measure(new SourceFile(args[0]));
			System.out.println(results);
		} catch (SecurityException | NotSupportedSourceFileExeption | IOException e) {
			e.printStackTrace();
		}
		
		// Sample 2: measure a code fragment
		String[] sampleCode = {
				"int n = 123;",
				"int sum;",
				"",
				"// compute the sum of natural numbers less than 123",
				"sum = 0;",
				"for ( int x = 1; x < n; x++ ){",
				"   sum += x;",
				"}",
				"",
				"System.out.println(sum);"
		};
		CodeMap map = new CodeMapForJava(sampleCode);
		LOC results2 = LOCCounter.measure(sampleCode,map);
		System.out.println(results2);
	}

}
