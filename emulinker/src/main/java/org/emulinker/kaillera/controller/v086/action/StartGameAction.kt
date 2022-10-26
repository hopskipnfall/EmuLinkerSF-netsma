package org.emulinker.kaillera.controller.v086.action

import com.google.common.flogger.FluentLogger
import javax.inject.Inject
import javax.inject.Singleton
import org.emulinker.kaillera.controller.messaging.MessageFormatException
import org.emulinker.kaillera.controller.v086.V086ClientHandler
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Notification
import org.emulinker.kaillera.controller.v086.protocol.StartGame_Notification
import org.emulinker.kaillera.controller.v086.protocol.StartGame_Request
import org.emulinker.kaillera.lookingforgame.TwitterBroadcaster
import org.emulinker.kaillera.model.event.GameStartedEvent
import org.emulinker.kaillera.model.exception.StartGameException

@Singleton
class StartGameAction
@Inject
internal constructor(private val lookingForGameReporter: TwitterBroadcaster) :
  V086Action<StartGame_Request>, V086GameEventHandler<GameStartedEvent> {
  override var actionPerformedCount = 0
    private set
  override var handledEventCount = 0
    private set

  override fun toString() = "StartGameAction"

  override suspend fun performAction(message: StartGame_Request, clientHandler: V086ClientHandler) {
    actionPerformedCount++
    try {
      clientHandler.user.startGame()
    } catch (e: StartGameException) {
      logger.atFine().withCause(e).log("Failed to start game")
      try {
        clientHandler.send(
          GameChat_Notification(clientHandler.nextMessageNumber, "Error", e.message!!)
        )
      } catch (ex: MessageFormatException) {
        logger.atSevere().withCause(ex).log("Failed to construct GameChat_Notification message")
      }
    }
  }

  override suspend fun handleEvent(event: GameStartedEvent, clientHandler: V086ClientHandler) {
    handledEventCount++
    try {
      val game = event.game
      clientHandler.user.tempDelay = game.highestUserFrameDelay - clientHandler.user.frameDelay
      val delay: Int =
        if (game.sameDelay) {
          game.highestUserFrameDelay
        } else {
          clientHandler.user.frameDelay
        }
      val playerNumber = game.getPlayerNumber(clientHandler.user)
      clientHandler.send(
        StartGame_Notification(
          clientHandler.nextMessageNumber,
          delay.toShort().toInt(),
          playerNumber.toByte().toShort(),
          game.players.size.toByte().toShort()
        )
      )
    } catch (e: MessageFormatException) {
      logger.atSevere().withCause(e).log("Failed to construct StartGame_Notification message")
    }
    lookingForGameReporter.cancelActionsForGame(event.game.id)
  }

  companion object {
    private val logger = FluentLogger.forEnclosingClass()
  }
}
