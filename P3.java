import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class P3 {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("P3.in"));
            int numberOfTests = Integer.parseInt(scanner.nextLine().trim());
            for (int t = 0; t < numberOfTests; t++) {
                int[] arr = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
                List<Integer> flips = pancakeSort(arr);
                if (flips.isEmpty()) {
                    System.out.println("ORDENADO");
                } else {
                    flips.forEach(flip -> System.out.print(flip + " "));
                    System.out.println();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado.");
        }
    }

    public static List<Integer> pancakeSort(int[] arr) {
        int n = arr.length;
        List<Integer> flipPositions = new ArrayList<>();

        // Comprobar si el arreglo ya está en orden decreciente o si está en orden ascendente completo
        if (isDecreasinglySorted(arr)) {
            return flipPositions; // Retornar lista vacía si está "ORDENADO"
        } else if (isIncreasinglySorted(arr)) {
            flip(arr, n);
            flipPositions.add(0);
            return flipPositions;
        }

        for (int size = n; size > 1; size--) {
            int maxIndex = findMaxIndex(arr, size);
            if (maxIndex != size - 1) {
                if (maxIndex != 0) {
                    flip(arr, maxIndex + 1);
                    flipPositions.add(maxIndex);
                }
                flip(arr, size);
                flipPositions.add(size - 1);
            }
        }

        return flipPositions;
    }

    private static boolean isDecreasinglySorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] < arr[i + 1]) {
                return false;
            }
        }
        return true; // Está ordenado decrecientemente
    }

    private static boolean isIncreasinglySorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true; // Está ordenado crecientemente
    }

    private static int findMaxIndex(int[] arr, int size) {
        int maxIndex = 0;
        for (int i = 1; i < size; i++) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static void flip(int[] arr, int size) {
        for (int i = 0; i < size / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[size - i - 1];
            arr[size - i - 1] = temp;
        }
    }
}
