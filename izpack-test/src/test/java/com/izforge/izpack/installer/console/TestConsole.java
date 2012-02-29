package com.izforge.izpack.installer.console;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * An {@link Console} that takes its input from a list of command strings.
 *
 * @author Tim Anderson
 */
public class TestConsole extends Console
{
    /**
     * The input scripts.
     */
    List<List<String>> scripts = new ArrayList<List<String>>();

    /**
     * The index into {@link #scripts}.
     */
    private int scriptIndex = 0;

    /**
     * The current script.
     */
    List<String> currentScript = null;

    /**
     * The index into {@link #currentScript}.
     */
    private int index = 0;

    /**
     * The no. of reads.
     */
    private int reads = 0;

    /**
     * Constructs a <tt>TestConsole</tt>.
     */
    public TestConsole()
    {
    }

    /**
     * Adds an input script. This is used to simulate keyboard input.
     *
     * @param name   a name for the script, for error reporting purposes
     * @param script the script. Each element corresponds to a line returned by {@link #readLine()}
     */
    public void addScript(String name, String... script)
    {
        List<String> list = new ArrayList<String>();
        list.add(name);
        list.addAll(Arrays.asList(script));
        scripts.add(list);
    }

    /**
     * Returns the current script name.
     *
     * @return the current script name, or <tt>null</tt> if there is no script
     */
    public String getScriptName()
    {
        return currentScript != null ? currentScript.get(0) : null;
    }

    /**
     * Determines if the input script has completed.
     *
     * @return <tt>true</tt> if the script has completed, otherwise <tt>false</tt>
     */
    public boolean scriptCompleted()
    {
        return scriptIndex == scripts.size() && (scripts.isEmpty() || index == scripts.get(scriptIndex - 1).size());
    }

    /**
     * Reads a line of text.  A line is considered to be terminated by any one of a line feed ('\\n'),
     * a carriage return ('\\r'), or a carriage return followed immediately by a linefeed.
     *
     * @return a String containing the contents of the line, not including any line-termination characters, or
     *         null if the end of the stream has been reached
     * @throws IOException if an I/O error occurs
     */
    @Override
    public String readLine() throws IOException
    {
        ++reads;
        String result = null;
        advanceScript();
        while (currentScript != null)
        {
            if (index == 0)
            {
                ++index; // first index is the name of the script
            }
            if (index < currentScript.size())
            {
                result = currentScript.get(index++);
                break;
            }
            else
            {
                advanceScript();
            }
        }
        if (result != null)
        {
            if (result.endsWith("\r\n"))
            {
                result = result.substring(0, result.length() - 2);
            }
            else if (result.endsWith("\r") || result.endsWith("\n"))
            {
                result = result.substring(0, result.length() - 1);
            }
            println(result); // echo it out
        }
        return result;
    }

    /**
     * Returns the no. of times {@link #readLine()}  has been invoked.
     *
     * @return the no. of reads
     */
    public int getReads()
    {
        return reads;
    }

    /**
     * Advances to the next script, if required.
     */
    private void advanceScript()
    {
        if (currentScript == null || index == currentScript.size())
        {
            if (scriptIndex < scripts.size())
            {
                currentScript = scripts.get(scriptIndex++);
                System.out.println("TestConsole running script: " + getScriptName());
                index = 0;
            }
            else
            {
                currentScript = null;
            }
        }
    }
}
