package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

sealed class GameEvent {
    data class CardFlipped(val cardId: String) : GameEvent()
    data class CardsMatched(val cardIds: List<String>) : GameEvent()
    data class CardsUnmatched(val cardIds: List<String>) : GameEvent()
    data object GameCompleted : GameEvent()
    data object GameTimeUp : GameEvent()
    data object GamePaused : GameEvent()
    data object GameResumed : GameEvent()
    data class ScoreSaved(val score: GameScore) : GameEvent()
    data object GameReset : GameEvent()
}
