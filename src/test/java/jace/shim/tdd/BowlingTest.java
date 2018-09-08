package jace.shim.tdd;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BowlingTest {

	@Test
	@DisplayName("처음 roll(쓰러지는 pin개수) 를 실행하면 false를 리턴한다.")
	void WhenFirstRollThenReturnTrue() {
		Bowling bowling = new PlaygameBowling();
		Assertions.assertThat(bowling.roll(1)).isTrue();
	}
}
