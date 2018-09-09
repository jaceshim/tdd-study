package jace.shim.tdd;

public interface Bowling {
	boolean roll(int pin);
	int score();
	BowlingFrame getCurrentFrame();
}
