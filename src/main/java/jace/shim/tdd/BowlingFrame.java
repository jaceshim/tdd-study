package jace.shim.tdd;

import java.util.Arrays;
import java.util.Objects;

public class BowlingFrame {
	private final int SPARE_SCORE = 10;
	private final int STRIKE_SCORE = 10;

	private Integer firstScore = null;
	private Integer secondScore = null;
	private Integer frameScore = Integer.valueOf(0);

	private final int frameNumber;

	public BowlingFrame(int startFrameNumber) {
		this.frameNumber = startFrameNumber;
	}

	public void shot(int pin) {
		final Integer integerValue = Integer.valueOf(pin);
		boolean executeSumFrameScore = false;
		if (isFirstShot()) {
			firstScore = integerValue;
			if (firstScore.intValue() == STRIKE_SCORE) {
				executeSumFrameScore = true;
			}
		} else {
			secondScore = integerValue;
			executeSumFrameScore = true;
		}

		if (executeSumFrameScore) {
			frameScore = Arrays.stream(new Integer[] {firstScore, secondScore}).filter(Objects::nonNull)
				.mapToInt(Integer::intValue).sum();
		}
	}

	public boolean goNextFrame() {
		return isStrike() || secondScore != null;
	}

	public boolean isFirstShot() {
		return getFirstScore() == null;
	}

	public boolean isFinishFirstShot() {
		return getFirstScore() != null && getSecondScore() == null;
	}

	public boolean isFinishFrame() {
		if (isFirstShot()) {
			return false;
		}
		return getFirstScore().intValue() == STRIKE_SCORE || getSecondScore() != null;
	}

	public boolean isSpare() {
		final int frameSumScore = Arrays.stream(new Integer[]{firstScore, secondScore})
			.filter(score -> score != null)
			.filter(score -> score != STRIKE_SCORE)
			.mapToInt(Integer::intValue).sum();

		return frameSumScore == SPARE_SCORE;
	}

	public boolean isStrike() {
		final Integer findStrikeScore = Arrays.stream(new Integer[]{firstScore, secondScore})
			.filter(score -> score != null)
			.filter(score -> score == STRIKE_SCORE).findFirst().orElse(-1);

		return findStrikeScore.equals(-1) ? false : true;
	}

	public int getFrameNumber() {
		return this.frameNumber;
	}

	public Integer getFirstScore() {
		return this.firstScore;
	}

	public Integer getSecondScore() {
		return this.secondScore;
	}

	public Integer getFrameScore() {
		return this.frameScore;
	}

	public void addBonusScore(Integer addScore) {
		if (this.frameScore == null) {
			this.frameScore = Integer.valueOf(0);
		}

		this.frameScore = Integer.sum(this.frameScore.intValue(), addScore.intValue());
	}
}
