package jace.shim.tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Bowling Rule : http://stparkms.tistory.com/62
 *
 * X 처음 roll(쓰러지는 pin개수) 를 실행하면 false를 리턴한다.
 * X roll(쓰러지는 pin개수) 실행시 쓰러지는 pin개수가 0개 이하면 InvalidArgumentException 를 던진다.
 * X roll(쓰러지는 pin개수) 실행시 쓰러지는 pin개수가 10개 이상이면 InvalidArgumentException을 던진다.
 * X roll()실행시 전달된 pin개수는 개수당 1점의 score를 획득한다.
 * X roll()실행하고 score()호출하면 이전에 실행된 roll()를 통해서 획득한 score와 합산된 score를 리턴한다.
 * X 스페어 처리시 현재 프레임의 1구의 쓰러드린 핀 개수(score)는 이전 프레임의 score에 합산된다.
 *
 * X 스트라이크 처리시 현재 프레임의 1구/2구 합산 score를  이전 프레임의 score에 합산된다.
 * - 거터/오픈/스페어 처리시 첫번째, 두번째 shot은 동일한 frame이 된다.
 * - 두번 roll하여 스트라이크 처리시 다음 roll은 다른 frame 으로 처리된다.
 * - 스트라이크 더블 처리시 현재 프레임의 1구/2구 합산 score를 이전전 프레임의 score에 합산된다.
 * - 마지막 roll()을 실행하면 true를 리턴하여 게임종료가 된다.
 *
 * - 게임종료이후 roll()을 실행하면 InvalidStateException을 던진다.
 */
public class BowlingTest {
	 static final int STRIKE_PIN_COUNT = 10;

	@Test
	@DisplayName("처음 roll(쓰러지는 pin개수) 를 실행하면 false를 리턴한다.")
	void WhenFirstRollThenReturnTrue() {
		final Bowling bowling = new PlaygameBowling();
		assertThat(bowling.roll(1)).isFalse();
	}

	@Test
	@DisplayName("roll(쓰러지는 pin개수) 실행시 쓰러지는 pin개수가 0개 이하면 InvalidArgumentException 를 던진다.")
	void When_ExecuteRollWithNegativeNumber_Then_ThrowsIllegalArgumentException() {
		final Bowling bowling = new PlaygameBowling();
		assertThatThrownBy(() -> {
			bowling.roll(-1);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("roll(쓰러지는 pin개수) 실행시 쓰러지는 pin개수가 10개 이상이면 InvalidArgumentException을 던진다.")
	void When_ExecuteRollWithOver10_Then_ThrowsIllegalArgumentException() {
		final Bowling bowling = new PlaygameBowling();
		assertThatThrownBy(() -> {
			bowling.roll(12);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("roll()실행시 전달된 pin개수는 개수당 1점의 score를 획득한다.")
	void When_ExecuteRoll_then_getScore() {
		final Bowling bowling = new PlaygameBowling();
		bowling.roll(9);
		assertThat(bowling.score()).isEqualTo(9);
	}

	@Test
	@DisplayName("roll()실행하고 score()호출하면 이전에 실행된 roll()를 통해서 획득한 score와 합산된 score를 리턴한다.")
	void When_ExecuteRoll_And_GetScore_Then_Return_Sum_Previous_Score() {
		final Bowling bowling = new PlaygameBowling();
		bowling.roll(9);
		bowling.roll(3);
		assertThat(bowling.score()).isEqualTo(12);
	}

	@Test
	@DisplayName("스페어 처리시 현재 프레임의 1구의 쓰러드린 핀 개수(score)는 이전 프레임의 score에 합산된다.")
	void When_Spare_And_ExecuteRoll_And_GetScore_Then_Return_Append_Score() {
		final Bowling bowling = new PlaygameBowling();
		bowling.roll(9);
		bowling.roll(1);
		bowling.roll(8);
		bowling.roll(1);
		assertThat(bowling.score()).isEqualTo(27);

		final Bowling bowling2 = new PlaygameBowling();
		bowling2.roll(6);
		bowling2.roll(4);
		bowling2.roll(6);
		bowling2.roll(0);
		assertThat(bowling2.score()).isEqualTo(22);
	}

	@Test
	@DisplayName("스트라이크 처리시 현재 프레임의 1구/2구 합산 score를  이전 프레임의 score에 합산된다.")
	void When_Strike_And_ExecuteRoll_And_GetScore_Then_Return_Append_Score() {
		final Bowling bowling = new PlaygameBowling();
		bowling.roll(0);
		bowling.roll(STRIKE_PIN_COUNT);
		bowling.roll(8);
		bowling.roll(1);
		assertThat(bowling.score()).isEqualTo(28);

		final Bowling bowling2 = new PlaygameBowling();
		bowling2.roll(STRIKE_PIN_COUNT);
		bowling2.roll(0);
		bowling2.roll(8);
		assertThat(bowling2.score()).isEqualTo(26);
	}

	@Test
	@DisplayName("거터/오픈/스페어 처리시 첫번째, 두번째 shot은 동일한 frame이 된다.")
	void When_Gutter_Open_Spare_And_Then_Same_Frame() {
		final Bowling bowling = new PlaygameBowling();
		bowling.roll(8);
		bowling.roll(1);

		assertThat(bowling.getCurrentFrame().getFrameNumber()).isEqualTo(1);
	}

	@Test
	@DisplayName("두번 roll하여 스트라이크 처리시 다음 roll은 다른 frame 으로 처리된다.")
	void When_Two_ExecuteRoll_Strike_Then_Diff_Frame() {
		final Bowling bowling = new PlaygameBowling();
		bowling.roll(STRIKE_PIN_COUNT);
		bowling.roll(STRIKE_PIN_COUNT);

		assertThat(bowling.getCurrentFrame().getFrameNumber()).isEqualTo(2);
	}
}
