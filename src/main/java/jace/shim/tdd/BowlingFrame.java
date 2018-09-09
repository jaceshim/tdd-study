package jace.shim.tdd;

import java.util.Arrays;
import java.util.Objects;

public class BowlingFrame {
	private static final int LAST_FRAME_NUMBER = 10;
	private final int SPARE_SCORE = 10;
	private final int STRIKE_SCORE = 10;

	private Integer firstScore = null;
	private Integer secondScore = null;
	private Integer thirdScore = null;
	private Integer frameScore = Integer.valueOf(0);

	private final int frameNumber;

	public BowlingFrame(int startFrameNumber) {
		this.frameNumber = startFrameNumber;
	}

	public void shot(int pin) {
		final Integer integerValue = Integer.valueOf(pin);

		if (isFirstShot()) {
			firstScore = integerValue;
		} else if (secondScore == null) {
			secondScore = integerValue;
		} else if (isLastFrame() && isFinishSecondShot()) {
			thirdScore = integerValue;
		}

		frameScore = Arrays.stream(new Integer[]{firstScore, secondScore, thirdScore})
			.filter(Objects::nonNull)
			.mapToInt(Integer::intValue).sum();
	}

	public boolean goNextFrame() {
		if (isLastFrame()) {
			return false;
		}
		return isStrike() || secondScore != null;
	}

	public boolean isFirstShot() {
		return getFirstScore() == null;
	}

	public boolean isFinishFirstShot() {
		return getFirstScore() != null && getSecondScore() == null;
	}

	public boolean isFinishSecondShot() {
		if (getFirstScore() == null) {
			return false;
		}

		return getSecondScore() != null;
	}

	public boolean isFinishFrame() {
		if (isFirstShot()) {
			return false;
		}
		if (isLastFrame() == false) {
			return getFirstScore().intValue() == STRIKE_SCORE || getSecondScore() != null;
		}

		if (getFirstScore().intValue() == STRIKE_SCORE || this.isSpare()) {
			return getThirdScore() != null;
		}
		return getSecondScore() != null;
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

	public Integer getThirdScore() {
		return this.thirdScore;
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

	public boolean isLastFrame() {
		return this.frameNumber == LAST_FRAME_NUMBER;
	}
}
