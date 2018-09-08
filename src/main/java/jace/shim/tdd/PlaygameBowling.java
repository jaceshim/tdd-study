package jace.shim.tdd;

public class PlaygameBowling implements Bowling {
	int score = 0;
	@Override
	public boolean roll(int pin) {
		if (invalidPinNumber(pin)) {
			throw new IllegalArgumentException();
		}
		score += pin;
		return false;
	}

	@Override
	public int score() {
		return score;
	}

	private boolean invalidPinNumber(int pin) {
		return pin < 0 || pin > 10;
	}
}
