package com.oguzhanozgokce.androidbootcampfinalproject.ui.game

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameResult
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameState
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.CreateGameUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.FlipCardUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetCurrentUserUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.SaveGameScoreUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createGameUseCase: CreateGameUseCase,
    private val flipCardUseCase: FlipCardUseCase,
    private val saveGameScoreUseCase: SaveGameScoreUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    private val difficulty = savedStateHandle.toRoute<Screen.Game>().difficulty
    private var gameState: GameState? = null
    private var gameStartTime: Long = 0L

    private val _isTimerRunning = MutableStateFlow(false)
    private val isTimerRunning = _isTimerRunning.asStateFlow()

    init {
        Log.d("GameViewModel", "GameViewModel initialized with difficulty: $difficulty")
        viewModelScope.launch {
            isTimerRunning.collectLatest { isRunning ->
                if (isRunning) {
                    startTimerFlow()
                }
            }
        }
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.LoadGame -> initializeGame()
                is UiAction.OnCardClicked -> handleCardClick(uiAction.cardId)
                is UiAction.OnGameCompleteDialogDismiss -> dismissGameCompleteDialog()
                is UiAction.OnGameOverDialogDismiss -> dismissGameOverDialog()
                is UiAction.OnRestartGame -> restartGame()
                is UiAction.OnSaveScore -> saveCurrentScore()
                is UiAction.OnBackClicked -> navigateBack()
            }
        }
    }

    private suspend fun initializeGame() {
        updateUiState { copy(isLoading = true) }

        val result = createGameUseCase(difficulty, "Player")

        result.fold(
            onSuccess = { newGameState ->
                gameState = newGameState
                gameStartTime = System.currentTimeMillis()

                updateUiState {
                    copy(
                        isLoading = false,
                        difficulty = difficulty,
                        cards = newGameState.cards,
                        score = newGameState.score,
                        timeRemaining = newGameState.timeRemaining,
                        matchedPairs = newGameState.matchedPairs,
                        totalMoves = newGameState.totalMoves,
                        canFlipCards = true,
                        isGameCompleted = false,
                        isGameOver = false,
                        gameResult = GameResult.InProgress
                    )
                }

                startTimer()
            },
            onFailure = { error ->
                updateUiState { copy(isLoading = false) }
                Log.e("GameViewModel", "Error creating game: ${error.message}")
                emitUiEffect(UiEffect.ShowError("Oyun başlatılamadı: ${error.message}"))
            }
        )
    }

    private suspend fun handleCardClick(cardId: String) {
        val currentGameState = gameState ?: return
        val currentUiState = uiState.value

        if (!currentUiState.canFlipCards || currentUiState.isGameOver || currentUiState.isGameCompleted) {
            return
        }

        val newGameState = flipCardUseCase(currentGameState, cardId)
        gameState = newGameState

        updateUiState {
            copy(
                cards = newGameState.cards,
                score = newGameState.score,
                flippedCards = newGameState.flippedCards,
                matchedPairs = newGameState.matchedPairs,
                totalMoves = newGameState.totalMoves,
                canFlipCards = newGameState.flippedCards.size < 2
            )
        }

        if (newGameState.flippedCards.size == 2) {
            val firstCard = newGameState.flippedCards[0]
            val secondCard = newGameState.flippedCards[1]

            if (firstCard.number != secondCard.number) {
                handleUnmatchedCards(newGameState)
            }
        }

        if (newGameState.isGameCompleted) {
            handleGameComplete()
        }
    }

    private fun handleUnmatchedCards(gameState: GameState) {
        updateUiState { copy(canFlipCards = false) }

        viewModelScope.launch {
            cardFlipDelayFlow().collectLatest {
                val updatedGameState = flipCardUseCase.closeUnmatchedCards(gameState)
                this@GameViewModel.gameState = updatedGameState

                updateUiState {
                    copy(
                        cards = updatedGameState.cards,
                        flippedCards = updatedGameState.flippedCards,
                        canFlipCards = true
                    )
                }
            }
        }
    }

    private fun handleGameComplete() {
        stopTimer()

        val completedTime = System.currentTimeMillis() - gameStartTime
        val currentState = uiState.value

        updateUiState {
            copy(
                isGameCompleted = true,
                gameResult = GameResult.Completed(currentState.score, completedTime),
                showGameCompleteDialog = true,
                canFlipCards = false
            )
        }
    }

    private fun handleGameOver() {
        stopTimer()

        updateUiState {
            copy(
                isGameOver = true,
                gameResult = GameResult.TimeUp,
                showGameOverDialog = true,
                canFlipCards = false
            )
        }
    }

    private fun startTimer() {
        _isTimerRunning.value = true
    }

    private fun stopTimer() {
        _isTimerRunning.value = false
    }

    private suspend fun startTimerFlow() {
        timerFlow().collectLatest { timeLeft ->
            updateUiState { copy(timeRemaining = timeLeft) }

            if (timeLeft <= 0) {
                handleGameOver()
            }
        }
    }

    private fun timerFlow() = flow {
        var timeRemaining = uiState.value.timeRemaining

        while (timeRemaining > 0 &&
            !uiState.value.isGameCompleted &&
            !uiState.value.isGameOver &&
            _isTimerRunning.value
        ) {
            emit(timeRemaining)
            delay(1000)
            timeRemaining--
        }

        emit(0)
    }

    private fun cardFlipDelayFlow() = flow {
        delay(1500)
        emit(Unit)
    }

    private suspend fun saveCurrentScore() {
        val currentState = uiState.value
        val completedTime = System.currentTimeMillis() - gameStartTime

        val userResult = getCurrentUserUseCase()
        val userId = userResult.getOrNull()?.uid ?: "anonymous_user"
        val playerName = userResult.getOrNull()?.displayName ?: "Player"

        val initialGameTime = when (difficulty) {
            else -> 60000L
        }

        val gameScore = GameScore(
            id = "",
            userId = userId,
            playerName = playerName,
            score = currentState.score,
            difficulty = currentState.difficulty,
            gameTime = initialGameTime, // Başlangıç oyun süresi
            completedTime = completedTime, // Gerçek tamamlama süresi
            timestamp = System.currentTimeMillis(),
            completed = currentState.isGameCompleted
        )

        val result = saveGameScoreUseCase(gameScore)

        result.fold(
            onSuccess = {
                emitUiEffect(UiEffect.ShowScoreSaved(currentState.score))
                dismissGameCompleteDialog()
            },
            onFailure = { error ->
                emitUiEffect(UiEffect.ShowError("Skor kaydedilemedi: ${error.message}"))
            }
        )
    }

    private suspend fun restartGame() {
        stopTimer()
        gameState = null

        updateUiState {
            copy(
                showGameCompleteDialog = false,
                showGameOverDialog = false
            )
        }

        initializeGame()
    }

    private fun dismissGameCompleteDialog() {
        updateUiState { copy(showGameCompleteDialog = false) }
    }

    private fun dismissGameOverDialog() {
        updateUiState { copy(showGameOverDialog = false) }
    }

    private suspend fun navigateBack() {
        stopTimer()
        emitUiEffect(UiEffect.NavigateBack)
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}