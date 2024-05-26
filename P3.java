import java.util.Arrays;
import java.util.Random;

public class P3 {

    public static void main(String[] args) {
        // Generar un arreglo aleatorio para la prueba
        int[] arr = randomArray(10);
        int[] result = pancakeSort(arr);
        System.out.println("Arreglo ordenado: " + Arrays.toString(result));
    }

    public static int[] pancakeSort(int[] arr) {
        int n = arr.length;
        int flips = 0;

        for (int size = n; size > 1; size--) {
            int maxIndex = findMaxIndex(arr, size);

            if (maxIndex != size - 1) {
                if (maxIndex != 0) {
                    flip(arr, maxIndex + 1);
                    flips++;
                }
                flip(arr, size);
                flips++;
            }
        }
        
        System.out.println("Número total de flips: " + flips);
        return arr;
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

    private static int[] randomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size)+1;  // Números aleatorios entre -100 y 99
        }
        return array;
    }
}
