package jace.shim.tdd;

import java.util.Arrays;

public class BowlingFrame {
	private final int SPARE_SCORE = 10;
	private final Integer[] shots = new Integer[2];
	private final int frameNumber;

	public BowlingFrame(int startFrameNumber) {
		this.frameNumber = startFrameNumber;
	}

	public int shot(int pin) {
		int saveIndex = 0;
		if (isFirstShot() == false) {
			saveIndex = 1;
		}
		shots[saveIndex] = pin;
		return pin;
	}

	public boolean isFirstShot() {
		return shots[0] == null;
	}

	public boolean isSecondShot() {
		return shots[1] != null;
	}

	public boolean isSpare() {
		final int frameSumScore = Arrays.stream(shots).mapToInt(Integer::intValue).sum();
		return frameSumScore == SPARE_SCORE;
	}

	public int getFrameNumber() {
		return this.frameNumber;
	}
}
