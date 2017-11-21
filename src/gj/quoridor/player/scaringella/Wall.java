package gj.quoridor.player.scaringella;

import java.util.LinkedList;
import java.util.List;

public class Wall {

	public static List<Integer> incompatible(int wall) {
		List<Integer> result = new LinkedList<>();

		// int r = wall / 16;
		int c = wall % 16;

		// verticale
		if (c < 8) {
			result.add(wall + 8);
			result.add(wall - 16);
			result.add(wall + 16);
		} else { // orizzontale
			result.add(wall - 8);
			result.add(wall + 1);
			result.add(wall - 1);
		}

		result.removeIf(w -> w < 0 || w > 127);

		// result.removeIf(new Predicate<Integer>() {
		// @Override
		// public boolean test(Integer i) {
		// return i < 0 || i > 127;
		// }
		// });

		return result;
	}

	public static Node[][] fracture(Node[][] board, int wall) {
		Node[][] result = new Node[2][2];

		int r = wall / 16;
		int c = wall % 16;

		// verticale
		if (c < 8) {
			result[0][0] = board[r][c];
			result[0][1] = board[r][c + 1];
			result[1][0] = board[r + 1][c];
			result[1][1] = board[r + 1][c + 1];
			/* orizzontale */ } else {
			c -= 8;
			result[0][0] = board[r][c];
			result[0][1] = board[r + 1][c];
			result[1][0] = board[r][c + 1];
			result[1][1] = board[r + 1][c + 1];
		}

		return result;
	}

}