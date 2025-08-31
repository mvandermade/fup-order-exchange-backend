package io.github.mvandermade.fup.`order-exchange`.providers.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EmojiTest {
    @Test
    fun emojisTest() {
        assertThat(emojis).contains("\uD83C\uDFD7\uFE0F") // 🏗️
        assertThat(emojis).contains("\uD83D\uDE0A") // 😊
        assertThat(emojis).contains("\uD83D\uDE02") // 😂
        assertThat(emojis).contains("\uD83D\uDE0C") // 😌
        assertThat(emojis).contains("\uD83D\uDE01") // 😁
        assertThat(emojis).contains("\uD83D\uDE4F") // 🙏
        assertThat(emojis).contains("\uD83D\uDE0E") // 😎
        assertThat(emojis).contains("\uD83D\uDCAA") // 💪
        assertThat(emojis).contains("\uD83D\uDE0B") // 😋
        assertThat(emojis).contains("\uD83D\uDE07") // 😇
        assertThat(emojis).contains("\uD83C\uDF89") // 🎉
        assertThat(emojis).contains("\uD83D\uDE4C") // 🙌
        assertThat(emojis).contains("\uD83E\uDD18") // 🤘
        assertThat(emojis).contains("\uD83D\uDC4D") // 👍
        assertThat(emojis).contains("\uD83E\uDD11") // 🤑
        assertThat(emojis).contains("\uD83E\uDD29") // 🤩
        assertThat(emojis).contains("\uD83E\uDD2A") // 🤪
        assertThat(emojis).contains("\uD83E\uDD20") // 🤠
        assertThat(emojis).contains("\uD83E\uDD73") // 🥳
        assertThat(emojis).contains("\uD83E\uDD24") // 🤤
        assertThat(emojis).contains("\uD83D\uDE0D") // 😍
        assertThat(emojis).contains("\uD83D\uDE00") // 😀

        assertThat(emojis).hasSize(22)
        assertThat(emojis.toSet()).hasSize(22)
    }
}
