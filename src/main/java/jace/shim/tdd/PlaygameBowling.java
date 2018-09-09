package jace.shim.tdd;

public class PlaygameBowling implements Bowling {
	private int score = 0;

	private BowlingFrame[] bowlingFrames = new BowlingFrame[10];
	private int nextFrameNumber = 1;
	private BowlingFrame currentFrame = null;

	public PlaygameBowling() {
		this.bowlingFrames[0] = currentFrame;
	}

	@Override
	public boolean roll(int pin) {
		if (invalidPinCount(pin)) {
			throw new IllegalArgumentException();
		}

		if (isNewFrame()) {
			currentFrame = createBowlingFrame(nextFrameNumber);
			this.bowlingFrames[currentFrame.getFrameNumber() - 1] = currentFrame;
		}

		final boolean isFirstShot = currentFrame.isFirstShot();
		score += currentFrame.shot(pin);

		if (isNotFirstFrame()) {
			final BowlingFrame prevFrame = getPrevFrame();
			if (prevFrame.isStrike() && currentFrame.isSecondShot()) {
				score += currentFrame.getFrameScore();
			} else if (prevFrame.isSpare() && isFirstShot) {
				score += currentFrame.getFirstScore();
			}
		}

		setNextFrameNumber();

		return false;
	}

	private boolean isNewFrame() {
		return currentFrame == null || currentFrame.getFrameNumber() != nextFrameNumber;
	}

	private void setNextFrameNumber() {
		if (currentFrame.isStrike() || currentFrame.isSecondShot()) {
			this.nextFrameNumber = currentFrame.getFrameNumber() + 1;
		}
	}

	@Override
	public int score() {
		return score;
	}

	@Override
	public BowlingFrame getCurrentFrame() {
		return this.currentFrame;
	}

	private BowlingFrame createBowlingFrame(int frameNumber) {
		return new BowlingFrame(frameNumber);
	}

	private BowlingFrame getPrevFrame() {
		int prevFrameNumber = currentFrame.getFrameNumber() - 1;
		return bowlingFrames[prevFrameNumber - 1]; // frameNumber와 array index number를 맞추기 위함.
	}

	private boolean isNotFirstFrame() {
		return currentFrame.getFrameNumber() > 1;
	}

	private boolean invalidPinCount(int pin) {
		return pin < 0 || pin > 10;
	}
}