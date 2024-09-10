package newYearChaos;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MinimumBribesTest extends TestCase {

    @Test
    public void minimumBribes2MinimumBribesNormal() {
        List<Integer> input = Arrays.asList(2, 1, 5, 3, 4);
        String expectedOutput = "3";
        assertEquals(expectedOutput, getOutputFromMinimumBribes(input));
    }

    @Test
    public void minimumBribes2MinimumBribesTooChaotic() {
        List<Integer> input = Arrays.asList(2, 5, 1, 3, 4);
        String expectedOutput = "Too chaotic";
        assertEquals(expectedOutput, getOutputFromMinimumBribes(input));
    }

    @Test
    public void minimumBribes2MinimumBribesTooChaotic2() {
        String string ="5 1 2 3 7 8 6 4";
        List<String> list = Arrays.asList(string.split(" "));
        List<Integer> input = list.stream().map(Integer::parseInt).collect(Collectors.toList());
        String expectedOutput = "Too chaotic";
        assertEquals(expectedOutput, getOutputFromMinimumBribes(input));
    }

    @Test
    public void minimumBribes2MinimumBribesNormal2() {
        String string ="1 2 5 3 7 8 6 4";
//        String string ="1 2 3 4 5 6 7 8";
//        String string ="1 2 5 3 4 6 7 8"=2
//        String string ="1 2 5 3 7 4 6 8"=2
//        String string ="1 2 5 3 7 8 4 6"=2
//        String string ="1 2 5 3 7 8 6 4"=1

        List<String> list = Arrays.asList(string.split(" "));
        List<Integer> input = list.stream().map(Integer::parseInt).collect(Collectors.toList());
        String expectedOutput = "7";
        assertEquals(expectedOutput, getOutputFromMinimumBribes(input));
    }



    private String getOutputFromMinimumBribes(List<Integer> input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        MinimumBribes.minimumBribes2(input);

        return outputStream.toString().trim();
    }
}