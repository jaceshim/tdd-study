package jace.shim.tdd;

import java.util.Optional;
import java.util.stream.IntStream;

public class PlaygameBowling implements Bowling {
	private int score = 0;

	private BowlingFrame[] bowlingFrames = new BowlingFrame[10];
	private BowlingFrame currentFrame = createBowlingFrame();

	public PlaygameBowling() {
		this.bowlingFrames[0] = currentFrame;
	}

	@Override
	public boolean roll(int pin) {
		if (invalidPinCount(pin)) {
			throw new IllegalArgumentException();
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

		if (currentFrame.isSecondShot()) {
			currentFrame = createBowlingFrame();
			this.bowlingFrames[currentFrame.getFrameNumber() - 1] = currentFrame;
		}


		return false;
	}

	private BowlingFrame createBowlingFrame() {
		return new BowlingFrame(getStartFrameNumber());
	}

	private BowlingFrame getPrevFrame() {
		int prevFrameNumber = currentFrame.getFrameNumber() - 1;
		return bowlingFrames[prevFrameNumber - 1]; // frameNumber와 array index number를 맞추기 위함.
	}

	@Override
	public int score() {
		return score;
	}

	private boolean isNotFirstFrame() {
		return currentFrame.getFrameNumber() > 1;
	}

	/**
	 * roll을 하게 되는경우 현재 frame 번호를 얻는다.
	 *
	 * @return
	 */
	private int getStartFrameNumber() {
		final int currentFrameNumber = IntStream.range(0, bowlingFrames.length - 1)
			.filter(i -> bowlingFrames[i] != null)
			.findFirst()
			.orElse(0);

		final BowlingFrame tempCurrentFrame = bowlingFrames[currentFrameNumber];
		final Integer culaFrameNumber = Optional.ofNullable(tempCurrentFrame).map(bowlingFrame -> {
			return tempCurrentFrame.isSecondShot() ? currentFrameNumber + 1 : currentFrameNumber;
		}).orElse(0);

		// 배열의 index시작번화와 frame의 시작번호의 차이 때문에 1을 plus한다.
		return culaFrameNumber + 1;
	}

	private boolean invalidPinCount(int pin) {
		return pin < 0 || pin > 10;
	}
}