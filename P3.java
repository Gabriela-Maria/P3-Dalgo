import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class P3 {
    public static void main(String[] args) {
        String inputFileName = "P3.in";
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            int numberOfCases = Integer.parseInt(br.readLine().trim());
            List<String> results = new ArrayList<>();
            
            for (int caseIndex = 0; caseIndex < numberOfCases; caseIndex++) {
                String[] pancakeStrings = br.readLine().trim().split("\\s+");
                int[] pancakes = Arrays.stream(pancakeStrings).mapToInt(Integer::parseInt).toArray();
                List<Integer> flips = pancakeSort(pancakes);
                if (flips.isEmpty()) {
                    results.add("ORDENADO");
                } else {
                    results.add(flips.stream().map(Object::toString).collect(Collectors.joining(" ")));
                }
            }

            for (String result : results) {
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> pancakeSort(int[] arr) {
        List<Integer> flips = new ArrayList<>();
        int n = arr.length;

        for (int size = n; size > 1; size--) {
            int maxIndex = findMax(arr, size);
            if (maxIndex != size - 1) {
                if (maxIndex != 0) {
                    flip(arr, maxIndex);
                    flips.add(maxIndex + 1);
                }
                flip(arr, size - 1);
                flips.add(size);
            }
        }
        return flips;
    }

    private static int findMax(int[] arr, int n) {
        int maxIndex = 0;
        for (int i = 1; i < n; i++) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static void flip(int[] arr, int k) {
        int left = 0, right = k;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}
