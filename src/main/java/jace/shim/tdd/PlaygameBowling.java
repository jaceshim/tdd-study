package jace.shim.tdd;

public class PlaygameBowling implements Bowling {
	@Override
	public boolean roll(int pin) {
		if (invalidPinNumber(pin)) {
			throw new IllegalArgumentException();
		}
		return false;
	}

	private boolean invalidPinNumber(int pin) {
		return pin < 0 || pin > 10;
	}
}
