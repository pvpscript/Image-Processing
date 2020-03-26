package imageUtils;

import java.util.ArrayList;
import java.util.List;

public class ConvolutionMethods {
	public static List<int[][]> sobel() {
		List<int[][]> matrixes = new ArrayList<int[][]>();
		
		int[][] r1 = new int[3][3];
		int[][] r2 = new int[3][3];
		
		r1[0][0] = -1;
		r1[0][1] = -2;
		r1[0][2] = -1;
		r1[1][0] = 0;
		r1[1][1] = 0;
		r1[1][2] = 0;
		r1[2][0] = 1;
		r1[2][1] = 2;
		r1[2][2] = 1;
		
		r2[0][0] = -1;
		r2[0][1] = 0;
		r2[0][2] = 1;
		r2[1][0] = -2;
		r2[1][1] = 0;
		r2[1][2] = 2;
		r2[2][0] = -1;
		r2[2][1] = 0;
		r2[2][2] = 1;
		
		matrixes.add(r1);
		matrixes.add(r2);
		
		return matrixes;
	}
	
	public static List<int[][]> prewitt() {
		List<int[][]> matrixes = new ArrayList<int[][]>();
		
		int[][] r1 = new int[3][3];
		int[][] r2 = new int[3][3];
		
		r1[0][0] = -1;
		r1[0][1] = -1;
		r1[0][2] = -1;
		r1[1][0] = 0;
		r1[1][1] = 0;
		r1[1][2] = 0;
		r1[2][0] = 1;
		r1[2][1] = 1;
		r1[2][2] = 1;
		
		r2[0][0] = -1;
		r2[0][1] = 0;
		r2[0][2] = 1;
		r2[1][0] = -1;
		r2[1][1] = 0;
		r2[1][2] = 1;
		r2[2][0] = -1;
		r2[2][1] = 0;
		r2[2][2] = 1;
		
		matrixes.add(r1);
		matrixes.add(r2);
		
		return matrixes;
	}
	
	public static List<int[][]> kirsch() {
		List<int[][]> matrixes = new ArrayList<int[][]>();
		
		int[][] r1 = new int[3][3];
		int[][] r2 = new int[3][3];
		int[][] r3 = new int[3][3];
		int[][] r4 = new int[3][3];
		int[][] r5 = new int[3][3];
		int[][] r6 = new int[3][3];
		int[][] r7 = new int[3][3];
		int[][] r8 = new int[3][3];
		
		r1[0][0] = 5;
		r1[0][1] = 5;
		r1[0][2] = 5;
		r1[1][0] = -3;
		r1[1][1] = 0;
		r1[1][2] = -3;
		r1[2][0] = -3;
		r1[2][1] = -3;
		r1[2][2] = -3;
		
		r2[0][0] = -3;
		r2[0][1] = 5;
		r2[0][2] = 5;
		r2[1][0] = -3;
		r2[1][1] = 0;
		r2[1][2] = 5;
		r2[2][0] = -3;
		r2[2][1] = -3;
		r2[2][2] = -3;
		
		r3[0][0] = -3;
		r3[0][1] = -3;
		r3[0][2] = 5;
		r3[1][0] = -3;
		r3[1][1] = 0;
		r3[1][2] = 5;
		r3[2][0] = -3;
		r3[2][1] = -3;
		r3[2][2] = 5;

		r4[0][0] = -3;
		r4[0][1] = -3;
		r4[0][2] = -3;
		r4[1][0] = -3;
		r4[1][1] = 0;
		r4[1][2] = 5;
		r4[2][0] = -3;
		r4[2][1] = 5;
		r4[2][2] = 5;
	
		r5[0][0] = -3;
		r5[0][1] = -3;
		r5[0][2] = -3;
		r5[1][0] = -3;
		r5[1][1] = 0;
		r5[1][2] = -3;
		r5[2][0] = 5;
		r5[2][1] = 5;
		r5[2][2] = 5;
	
		r6[0][0] = -3;
		r6[0][1] = -3;
		r6[0][2] = -3;
		r6[1][0] = 5;
		r6[1][1] = 0;
		r6[1][2] = -3;
		r6[2][0] = 5;
		r6[2][1] = 5;
		r6[2][2] = -3;
	
		r7[0][0] = 5;
		r7[0][1] = -3;
		r7[0][2] = -3;
		r7[1][0] = 5;
		r7[1][1] = 0;
		r7[1][2] = -3;
		r7[2][0] = 5;
		r7[2][1] = -3;
		r7[2][2] = -3;
	
		r8[0][0] = 5;
		r8[0][1] = 5;
		r8[0][2] = -3;
		r8[1][0] = 5;
		r8[1][1] = 0;
		r8[1][2] = -3;
		r8[2][0] = -3;
		r8[2][1] = -3;
		r8[2][2] = -3;
	
		matrixes.add(r1);
		matrixes.add(r2);
		matrixes.add(r3);
		matrixes.add(r4);
		matrixes.add(r5);
		matrixes.add(r6);
		matrixes.add(r7);
		matrixes.add(r8);
		
		return matrixes;
	}
}
