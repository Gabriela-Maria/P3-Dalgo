import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ProblemaP3 {

   public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Por favor, proporcione el nombre del archivo de entrada.");
            return;
        }

        String archivoEntrada = args[0];
        String archivoSalida = "P3.out";

        try {
            Scanner scanner = new Scanner(new File(archivoEntrada));
            PrintWriter writer = new PrintWriter(archivoSalida);
            
            int numCasosPrueba = Integer.parseInt(scanner.nextLine().trim());
            for (int t = 0; t < numCasosPrueba; t++) {
                int[] caso = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
                List<Integer> flips = pancakeSort(caso);
                if (flips.isEmpty()) {
                    writer.println("ORDENADO");
                } else {
                    for (int flip : flips) {
                        writer.print(flip + " ");
                    }
                    writer.println();
                }
            }
            
            scanner.close();
            System.out.println("Se registro la respuesta en el archivo "+archivoSalida);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + archivoEntrada);
        }
    }


    public static List<Integer> pancakeSort(int[] caso) {
        int n = caso.length;
        List<Integer> listaFlips = new ArrayList<>();

        // Comprobar si el arreglo ya está en orden decreciente o si está en orden ascendente completo
        if (esSortDecreciente(caso)) {
            return listaFlips; // Retornar lista vacía si está "ORDENADO"
        } else if (esSortCreciente(caso)) {
            flip(caso, n);
            listaFlips.add(0);
            return listaFlips;
        }

        for (int tamano = n; tamano > 1; tamano--) {
            int maxIndex = indicePancakeGrande(caso, tamano);
            if (maxIndex != tamano - 1) {
                if (maxIndex != 0) {
                    flip(caso, maxIndex + 1);
                    listaFlips.add(maxIndex);
                }
                flip(caso, tamano);
                listaFlips.add(tamano - 1);
            }
        }

        return listaFlips;
    }

    private static boolean esSortDecreciente(int[] caso) {
        for (int i = 0; i < caso.length - 1; i++) {
            if (caso[i] < caso[i + 1]) {
                return false;
            }
        }
        return true; // Está ordenado decrecientemente
    }

    private static boolean esSortCreciente(int[] caso) {
        for (int i = 0; i < caso.length - 1; i++) {
            if (caso[i] > caso[i + 1]) {
                return false;
            }
        }
        return true; // Está ordenado crecientemente
    }

    private static int indicePancakeGrande(int[] caso, int tamano) {
        int maxIndex = 0;
        for (int i = 1; i < tamano; i++) {
            if (caso[i] > caso[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static void flip(int[] caso, int tamano) {
        for (int i = 0; i < tamano / 2; i++) {
            int temp = caso[i];
            caso[i] = caso[tamano - i - 1];
            caso[tamano - i - 1] = temp;
        }
    }
}
/*
 PARA PROBARLO copiar estos comandos:
 
javac ProblemaP3.java
java ProblemaP3 P3.in
  
 */