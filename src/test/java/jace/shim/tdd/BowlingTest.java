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

	@Test
	@DisplayName("roll()실행시 전달된 pin개수는 개수당 1점의 score를 획득한다.")
	void When_ExecuteRoll_then_getScore() {
		Bowling bowling = new PlaygameBowling();
		bowling.roll(9);
		assertThat(bowling.score()).isEqualTo(9);
	}

	@Test
	@DisplayName("roll()실행하고 score()호출하면 이전에 실행된 roll()를 통해서 획득한 score와 합산된 score를 리턴한다.")
	void When_ExecuteRoll_And_GetScore_Then_Return_Sum_Previous_Score() {
		Bowling bowling = new PlaygameBowling();
		bowling.roll(9);
		assertThat(bowling.score()).isEqualTo(9);

		bowling.roll(3);
		assertThat(bowling.score()).isEqualTo(12);
	}
}
