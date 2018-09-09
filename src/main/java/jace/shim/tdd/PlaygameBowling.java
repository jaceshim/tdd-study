package jace.shim.tdd;

import java.util.Arrays;
import java.util.Objects;

public class PlaygameBowling implements Bowling {
	private BowlingFrame[] bowlingFrames = new BowlingFrame[10];
	private int nextFrameNumber = 1;
	private BowlingFrame currentFrame = null;

	@Override
	public boolean roll(int pin) {
		if (invalidPinCount(pin)) {
			throw new IllegalArgumentException();
		}

		if (isFinishGame()) {
			throw new IllegalStateException();
		}

		if (isNewFrame()) {
			currentFrame = createBowlingFrame(nextFrameNumber);
			this.bowlingFrames[currentFrame.getFrameNumber() - 1] = currentFrame;
		}

		currentFrame.shot(pin);

		setBonusScore();

		if (currentFrame.goNextFrame()) {
			this.nextFrameNumber = currentFrame.getFrameNumber() + 1;
		}

		if (currentFrame.isLastFrame() == false) {
			setNextFrameNumber();
		}

		return isFinishGame();
	}

	private boolean isFinishGame() {
		return currentFrame != null && currentFrame.isLastFrame() && currentFrame.isFinishFrame();
	}

	private void setBonusScore() {
		if (isSpareBonus()) {
			addScore(getBeforeBowlingFrame(-1), currentFrame.getFirstScore());
		}

		if (isDoubleBonus()) {
			addScore(getBeforeBowlingFrame(-2), currentFrame.getFirstScore());
		}

		if (isStrikeBonus()) {
			addScore(getBeforeBowlingFrame(-1), currentFrame.getFrameScore());
		}
	}

	private BowlingFrame getBeforeBowlingFrame(int beforeIndex) {
		return bowlingFrames[getFrameArrayIndex(currentFrame.getFrameNumber()) + beforeIndex];
	}

	private boolean isSpareBonus() {
		if (isNotFirstFrame() == false) {
			return false;
		}
		if (currentFrame.isFinishFirstShot()) {
			return getBeforeBowlingFrame(-1).isSpare();
		}

		return false;
	}

	private boolean isStrikeBonus() {
		if (isNotFirstFrame() == false) {
			return false;
		}

		if (currentFrame.isFinishFrame()) {
			return getBeforeBowlingFrame(-1).isStrike();
		}

		return false;
	}

	private boolean isDoubleBonus() {
		if (currentFrame.getFrameNumber() <= 2) {
			return false;
		}

		if (currentFrame.isFinishFrame()) {
			// 현재 frame 이전 2개의 frame이 strike인 경우 double
			return getBeforeBowlingFrame(-2).isStrike() && getBeforeBowlingFrame(-1).isStrike();
		}

		return false;
	}

	private void addScore(BowlingFrame targetFrame, Integer addScore) {
		targetFrame.addBonusScore(addScore);
	}

	private int getFrameArrayIndex(int frameNumber) {
		if (frameNumber == 0) {
			return frameNumber;
		}
		return frameNumber - 1;
	}

	@Override
	public int score() {
		return Arrays.stream(bowlingFrames).filter(Objects::nonNull)
			.mapToInt(BowlingFrame::getFrameScore).sum();
	}

	@Override
	public BowlingFrame getCurrentFrame() {
		return this.currentFrame;
	}

	private boolean isNewFrame() {
		if (currentFrame == null) {
			return true;
		}
		if (currentFrame.isLastFrame()) {
			return false;
		}

		return currentFrame.getFrameNumber() != nextFrameNumber;
	}

	private void setNextFrameNumber() {
		if (currentFrame.isStrike() || currentFrame.isFinishFrame()) {
			this.nextFrameNumber = currentFrame.getFrameNumber() + 1;
		}
	}

	private BowlingFrame createBowlingFrame(int frameNumber) {
		return new BowlingFrame(frameNumber);
	}

	private boolean isNotFirstFrame() {
		return currentFrame.getFrameNumber() > 1;
	}

	private boolean invalidPinCount(int pin) {
		return pin < 0 || pin > 10;
	}
}