package jace.shim.tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BowlingTest {

	@Test
	@DisplayName("처음 roll(쓰러지는 pin개수) 를 실행하면 false를 리턴한다.")
	void WhenFirstRollThenReturnTrue() {
		Bowling bowling = new PlaygameBowling();
		assertThat(bowling.roll(1)).isFalse();
	}

	@Test
	@DisplayName("roll(쓰러지는 pin개수) 실행시 쓰러지는 pin개수가 0개 이하면 InvalidArgumentException 를 던진다.")
	void When_ExecuteRollWithNegativeNumber_Then_ThrowsIllegalArgumentException() {
		Bowling bowling = new PlaygameBowling();
		assertThatThrownBy(() -> {
			bowling.roll(-1);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("roll(쓰러지는 pin개수) 실행시 쓰러지는 pin개수가 10개 이상이면 InvalidArgumentException을 던진다.")
	void When_ExecuteRollWithOver10_Then_ThrowsIllegalArgumentException() {
		Bowling bowling = new PlaygameBowling();
		assertThatThrownBy(() -> {
			bowling.roll(12);
		}).isInstanceOf(IllegalArgumentException.class);
	}
}
