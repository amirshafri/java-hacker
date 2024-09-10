package newYearChaos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MinimumBribes {
    public static void minimumBribes(List<Integer> q) {
//key = person value= numberOfBribe
        HashMap<Integer, Integer> numberOfBribe = new HashMap<>();

        List<Integer> result = new ArrayList<>();
        int n = 1;
        while (n <= q.size()) {
            result.add(n++);
        }
        for (int i = 0; i < result.size(); i++) {
            int countMoved = 0;
            int value = result.get(i);
            int indexQ = q.indexOf(value);
            if (i > indexQ) {
                countMoved = countMoved + (i - indexQ);
            }
            numberOfBribe.put(value, countMoved);

        }
        boolean isBribeMoreThan3 = numberOfBribe.values().stream().anyMatch(value -> value >= 3);

        if (!isBribeMoreThan3) {
            int totalBribes = numberOfBribe.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println(totalBribes);
        } else {
            System.out.println("Too chaotic");
        }
    }


    public static void minimumBribes2(List<Integer> q) {
        List<Integer> result = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        int n = 1;
        while (n <= q.size()) {
            result.add(n++);
        }
        int countMoved = 0;
        boolean isTooChaotic = false;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) != q.get(i)) {
                int value = q.get(i);
                for (int j = i; j < result.size(); j++) {
                    if (result.get(j) != value) {
                        temp.add(result.get(j));
                    } else {
                        int moved = (j - i);
                        countMoved = countMoved + moved;
                        if (moved >= 3) {
                            isTooChaotic = true;
                            break;
                        }
                    }
                }
                int index = (i + 1);
                for (int val : temp) {
                    result.set(index, val);
                    index++;
                }
                result.set(i, value);
                temp = new ArrayList<>();
            }
        }
        if (isTooChaotic) {
            System.out.println("Too chaotic");
        } else {
            System.out.println(countMoved);
        }
    }

    public static void minimumBribes3(List<Integer> q) {
        boolean isTooChaotic =false;
        int countMoved = 0;
        for (int i = 0; i<q.size(); i++) {
            if(q.get(i)-(i+1) > 2){
                isTooChaotic=true;
                break;
            }
        }

        if (isTooChaotic) {
            System.out.println("Too chaotic");
        } else {
            System.out.println(countMoved);
        }
    }

}

class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<Integer> q = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList());
                MinimumBribes.minimumBribes2(q);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }
}



