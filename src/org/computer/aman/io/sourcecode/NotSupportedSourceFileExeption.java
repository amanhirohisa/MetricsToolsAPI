package org.computer.aman.io.sourcecode;

/**
 * Signals that the source file is not supported.
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class NotSupportedSourceFileExeption extends Exception
{
    public NotSupportedSourceFileExeption()
    {
        super();
    }

    public NotSupportedSourceFileExeption(final String aMessage)
    {
        super(aMessage);
    }

    private static final long serialVersionUID = 2008061502L;
}
