package jace.shim.tdd;

public class PlaygameBowling implements Bowling {
	@Override
	public boolean roll(int pin) {
		if (pin < 0) {
			throw new IllegalArgumentException();
		}
		if (pin > 10) {
			throw new IllegalArgumentException();
		}
		return false;
	}
}
