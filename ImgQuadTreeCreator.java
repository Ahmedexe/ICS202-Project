import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ImgQuadTreeCreator {
    Scanner input;
    PrintWriter output;

    int dim = 256; // dimension of the array
    int[][] arr = new int[dim][dim]; // Initialize a 2D array to store pixel values
    
    // Constructor to create an instance of ImgQuadTreeCreator
    public ImgQuadTreeCreator(String inputFilename, String outputFileString) {

        try {
            // Open the input file for reading
            File file = new File(inputFilename);
            input = new Scanner(file);
            // Create a PrintWriter object to write to the output file
            output = new PrintWriter("C:\\\\KFUPM\\\\232\\\\ICS202\\\\project\\\\" + outputFileString);
			
        } catch (Exception e) {
            // If an exception occurs, print the error message and exit the program
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Populate the 2D array with pixel values from the input file
        buildArrFromImg(input);    
        // Generate and write the quadtree representation to the output file
        outputQT(dim, 0, 0, output);
        // Close the output file
        output.close();
    }

    // Method to populate the 2D array with pixel values from the input file
    private void buildArrFromImg(Scanner input) {

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                arr[i][j] = input.nextInt(); // Read pixel values from the input file
            }
        }
        input.close(); // Close the input file stream
    }


    // Method to generate the quadtree representation and write it to the output file
    private void outputQT(int dim, int x, int y, PrintWriter out) {
        int firstPixel = arr[y][x]; // Get the pixel value at the top-left corner of the current region
        boolean flag = true;

        // Check if all pixels in the current region have the same value
        for (int i = y; i < (dim + y); i++) {
            for (int j = x; j < (dim + x); j++) {
                if (arr[i][j] != firstPixel) {
                    flag = false;
                }
            }
        }

        // If not all pixels have the same value, recursively divide the region into quadrants
        if (!flag) {
            out.println("-1"); // Write -1 to indicate a division in the quadtree
            outputQT(dim / 2, x, y, out); // Top-left quadrant
            outputQT(dim / 2, x + (dim / 2), y, out); // Top-right quadrant
            outputQT(dim / 2, x, y + (dim / 2), out); // Bottom-left quadrant
            outputQT(dim / 2, x + (dim / 2), y + (dim / 2), out); // Bottom-right quadrant
        } else {
            out.println(firstPixel + " "); // Write the pixel value if all pixels in the region have the same value
        }
    }

    // Main method to execute the program
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input the name of the image text file to load:");
        String filename = scan.nextLine();

        // Generate the output file name by replacing the file extension with "QT.txt"
        String outputFileName = filename.substring(0, filename.length() - 4) + "QT.txt";
        scan.close();

        // Create an instance of ImgQuadTreeCreator to process the image file
        new ImgQuadTreeCreator(filename, outputFileName);
    }
}
