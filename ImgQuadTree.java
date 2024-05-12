import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ImgQuadTree {

	// Scanner to read from input file
	Scanner input;
	// Root node of the quadtree
	QTNode root;
	// PrintWriter to write output to file
	PrintWriter output;
	// Array to store pixel values of the image
	int[][] arr = new int[256][256];

	// Constructor to initialize the quadtree with an input file
	public ImgQuadTree(String filename) {
		
		try {
			File file = new File(filename);
			input = new Scanner(file);
		} catch (Exception e) {
			// Print error message and exit if file opening fails
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		// Build the quadtree recursively
		root = buildQT();
		input.close(); // Close the input file stream
	}

	// Method to recursively build the quadtree
	private QTNode buildQT() {
		int value = input.nextInt();

		if (value == -1) {
			// Internal node with children
			QTNode node = new QTNode();
			node.setChildren(buildQT(), buildQT(), buildQT(), buildQT());
			return node;
		} else {
			// Leaf node with intensity value
			QTNode leafNode = new QTNode();
			leafNode.setIntensity(value);
			return leafNode;
		} 
	}

	// Method to get the total number of nodes in the quadtree
	public int getNumNodes() {
		return getNumNodes(root);
	}

	// Helper method to recursively count the nodes in the quadtree
	private int getNumNodes(QTNode node) {
		if (node != null) {
			return 1 + getNumNodes(node.NW) + getNumNodes(node.NE) + getNumNodes(node.SW) + getNumNodes(node.SE);
		}
		return 0;
	}

	// Method to get the total number of leaf nodes in the quadtree
	public int getNumLeaves() {
		return getNumLeaves(root);
	}

	// Helper method to recursively count the leaf nodes in the quadtree
	private int getNumLeaves(QTNode node) {
		if (node.SW == null) {
			return 1; 
		}
		return getNumLeaves(node.NW) + getNumLeaves(node.NE) + getNumLeaves(node.SW) + getNumLeaves(node.SE);
	}
	
	// Method to convert the quadtree representation back into an image array
	public int[][] getImageArray(){
		getImageArray(arr, root, 0, 0, 256);
		return arr;	
	}

	// Helper method to recursively populate the image array from the quadtree
	private void getImageArray(int[][] arr, QTNode node, int x, int y, int dim) {
		if (node.getIntensity() == -1) {
			// Internal node, recursively process its children
			getImageArray(arr, node.NW, x, y, dim/2);
			getImageArray(arr, node.NE, x + (dim/2), y, dim/2);
			getImageArray(arr, node.SW, x, y + (dim/2), dim/2);
			getImageArray(arr, node.SE, x + (dim/2), y + (dim/2), dim/2);
		} else {
			// Leaf node, set intensity value for corresponding pixels in the array
			int intensity = node.getIntensity();
			for (int i = y; i < y + dim ; i++) {
				for (int j = x; j < x + dim; j++) {
					arr[i][j] = intensity;
				}
			}
		}
	}

	// Inner class representing a node in the quadtree
	private class QTNode {
		QTNode NW, NE, SE, SW; // Children nodes
		int intensity; // Intensity value for leaf nodes

		// Constructor to initialize node with null children and default intensity value
		public QTNode() {
			NW = SE = NE = SW = null;
			intensity = -1;
		}
		
		// Setter method to assign children nodes to the current node
		public void setChildren(QTNode l, QTNode sl, QTNode r, QTNode sr) {
			this.NW = l; this.NE = sl; this.SW = r; this.SE = sr;
		}

		// Getter method to retrieve the intensity value of the node
		public int getIntensity() {
			return intensity;
		}

		// Setter method to assign intensity value to the node
		public void setIntensity(int intensity) {
			this.intensity = intensity;
		}

		// Override toString method to return the intensity value as a string
		@Override
		public String toString() {
			return this.getIntensity() + "";
		}
	}
}
