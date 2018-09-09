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

		if (isNewFrame()) {
			currentFrame = createBowlingFrame(nextFrameNumber);
			this.bowlingFrames[currentFrame.getFrameNumber() - 1] = currentFrame;
		}

		currentFrame.shot(pin);

		setBonusScore();

		if (currentFrame.goNextFrame()) {
			this.nextFrameNumber = currentFrame.getFrameNumber() + 1;
		}

		setNextFrameNumber();

		return false;
	}

	private void setBonusScore() {
		if (isSpareBonus()) {
			addScore(bowlingFrames[getFrameArrayIndex(currentFrame.getFrameNumber()) - 1], currentFrame.getFirstScore());
		} else if (isStrikeBonus()) {
			addScore(bowlingFrames[getFrameArrayIndex(currentFrame.getFrameNumber()) - 1], currentFrame.getFrameScore());
		} else if (isDoubleBonus()) {
			addScore(bowlingFrames[getFrameArrayIndex(currentFrame.getFrameNumber() - 2)], currentFrame.getFirstScore());
		}
	}

	private boolean isSpareBonus() {
		if (isNotFirstFrame() == false) {
			return false;
		}
		if (currentFrame.isFinishFirstShot()) {
			return bowlingFrames[getFrameArrayIndex(currentFrame.getFrameNumber()) - 1].isSpare();
		}

		return false;
	}

	private boolean isStrikeBonus() {
		if (isNotFirstFrame() == false) {
			return false;
		}

		if (currentFrame.isFinishFrame()) {
			return bowlingFrames[getFrameArrayIndex(currentFrame.getFrameNumber()) - 1].isStrike();
		}

		return false;
	}

	private boolean isDoubleBonus() {
		if (currentFrame.getFrameNumber() <= 2) {
			return false;
		}

		// 현재 frame 이전 2개의 frame이 strike인 경우 double
		int currentFrameArrayIndex = getFrameArrayIndex(currentFrame.getFrameNumber());
		return bowlingFrames[currentFrameArrayIndex - 2].isStrike() && bowlingFrames[currentFrameArrayIndex - 1].isStrike();
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
		return currentFrame == null || currentFrame.getFrameNumber() != nextFrameNumber;
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