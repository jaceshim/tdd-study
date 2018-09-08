package jace.shim.tdd;

public class PlaygameBowling implements Bowling {
	@Override
	public boolean roll(int pin) {
		if (invalidPinNumber(pin)) {
			throw new IllegalArgumentException();
		}
		return false;
	}

	@Override
	public int score() {
		return 9;
	}

	private boolean invalidPinNumber(int pin) {
		return pin < 0 || pin > 10;
	}
}
