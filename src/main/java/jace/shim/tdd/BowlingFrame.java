package jace.shim.tdd;

import java.util.Arrays;

public class BowlingFrame {
	private final int SPARE_SCORE = 10;
	private final int STRIKE_SCORE = 10;
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
		return getFirstScore() == null;
	}

	public boolean isSecondShot() {
		return getSecondScore() != null;
	}

	public boolean isSpare() {
		final int frameSumScore = Arrays.stream(shots).filter(score -> score != STRIKE_SCORE)
			.mapToInt(Integer::intValue).sum();
		return frameSumScore == SPARE_SCORE;
	}

	public boolean isStrike() {
		final Integer findStrikeScore = Arrays.stream(shots)
			.filter(shot -> shot == STRIKE_SCORE).findFirst().orElse(-1);

		return findStrikeScore == -1 ? false : true;
	}

	public int getFrameNumber() {
		return this.frameNumber;
	}

	public Integer getFirstScore() {
		return shots[0];
	}

	public Integer getSecondScore() {
		return shots[1];
	}

	public Integer getFrameScore() {
		return getFirstScore() + getSecondScore();
	}
}
