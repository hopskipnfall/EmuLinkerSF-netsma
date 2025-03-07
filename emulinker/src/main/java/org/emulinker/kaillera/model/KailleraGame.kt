// www.EmuLinker.org",
    //     user);
    // announce("************************", user);
    // announce("Type /p2pon to ignore ALL server activity during gameplay.", user);
    // announce("This will reduce lag that you contribute due to a busy server.", user);
    // announce("If server is greater than 60 users, option is auto set.", user);
    // announce("************************", user);

    /*
    if(autoFireDetector != null)
    {
    	if(autoFireDetector.getSensitivity() > 0)
    	{
    		announce(EmuLang.getString("KailleraGameImpl.AutofireDetectionOn"));
    		announce(EmuLang.getString("KailleraGameImpl.AutofireCurrentSensitivity", autoFireDetector.getSensitivity()));
    	}
    	else
    	{
    		announce(EmuLang.getString("KailleraGameImpl.AutofireDetectionOff"));
    	}
    	announce(EmuLang.getString("KailleraGameImpl.GameHelp"));
    }
    */
    // }

    // new SF MOD - different emulator versions notifications
    if (
      access < AccessManager.ACCESS_ADMIN &&
        user.clientType != owner.clientType &&
        !owner.game!!.romName.startsWith("*")
    )
      addEventForAllPlayers(
        GameInfoEvent(this, user.name + " using different emulator version: " + user.clientType)
      )
    return players.indexOf(user) + 1
  }

  @Synchronized
  @Throws(StartGameException::class)
  fun start(user: KailleraUser) {
    val access = server.accessManager.getAccess(user.socketAddress!!.address)
    if (user != owner && access < AccessManager.ACCESS_ADMIN) {
      logger.atWarning().log("%s start game denied: not the owner of %s", user, this)
      throw StartGameException(
        EmuLang.getString("KailleraGameImpl.StartGameDeniedOnlyOwnerMayStart")
      )
    }
    if (status == GameStatus.SYNCHRONIZING) {
      logger.atWarning().log("%s start game failed: %s status is %s", user, this, status)
      throw StartGameException(EmuLang.getString("KailleraGameImpl.StartGameErrorSynchronizing"))
    } else if (status == GameStatus.PLAYING) {
      logger.atWarning().log("%s start game failed: %s status is %s", user, this, status)
      throw StartGameException(EmuLang.getString("KailleraGameImpl.StartGameErrorStatusIsPlaying"))
    }
    if (access == AccessManager.ACCESS_NORMAL && players.size < 2 && !server.allowSinglePlayer) {
      logger.atWarning().log("%s start game denied: %s needs at least 2 players", user, this)
      throw StartGameException(
        EmuLang.getString("KailleraGameImpl.StartGameDeniedSinglePlayerNotAllowed")
      )
    }

    singleFrameDurationForLagCalculationOnlyNs =
      (1.seconds / user.connectionType.getUpdatesPerSecond(GAME_FPS)).inWholeNanoseconds

    // do not start if not game
    if (owner.game!!.romName.startsWith("*")) return
    for (player in players) {
      if (!player.inStealthMode) {
        if (player.connectionType != owner.connectionType) {
          logger
            .atWarning()
            .log(
              "%s start game denied: %s: All players must use the same connection type",
              user,
              this,
            )
          addEventForAllPlayers(
            GameInfoEvent(
              this,
              EmuLang.getString(
                "KailleraGameImpl.StartGameConnectionTypeMismatchInfo",
                owner.connectionType,
              ),
              null,
            )
          )
          throw StartGameException(
            EmuLang.getString("KailleraGameImpl.StartGameDeniedConnectionTypeMismatch")
          )
        }
        if (player.clientType != clientType) {
          logger
            .atWarning()
            .log("%s start game denied: %s: All players must use the same emulator!", user, this)
          addEventForAllPlayers(
            GameInfoEvent(
              this,
              EmuLang.getString("KailleraGameImpl.StartGameEmulatorMismatchInfo", clientType),
              null,
            )
          )
          throw StartGameException(
            EmuLang.getString("KailleraGameImpl.StartGameDeniedEmulatorMismatch")
          )
        }
      }
    }
    logger.atInfo().log("%s started: %s", user, this)
    status = GameStatus.SYNCHRONIZING
    autoFireDetector.start(players.size)
    val actionQueueBuilder: Array<PlayerActionQueue?> = arrayOfNulls(players.size)
    startTimeout = false
    highestUserFrameDelay = 1
    if (server.usersMap.values.size > 60) {
      ignoringUnnecessaryServerActivity = true
    }
    for (i in players.indices) {
      val player = players[i]
      val playerNumber = i + 1
      if (!swap) player.playerNumber = playerNumber
      player.frameCount = 0
      actionQueueBuilder[i] =
        PlayerActionQueue(
          playerNumber = playerNumber,
          player,
          numPlayers = players.size,
          gameBufferSize = bufferSize,
        )
      val delayVal =
        GAME_FPS.toDouble() / player.connectionType.byteValue *
          (player.ping.toMillisDouble() / 1000.0) + 1.0
      player.frameDelay = delayVal.toInt()
      if (delayVal.toInt() > highestUserFrameDelay) {
        highestUserFrameDelay = delayVal.toInt()
      }
      if (ignoringUnnecessaryServerActivity) {
        player.ignoringUnnecessaryServerActivity = true
        announce("This game is ignoring ALL server activity during gameplay!", player)
      }
      /*else{
      	player.setP2P(false);
      }*/
      logger.atInfo().log("%s: %s is player number %s", this, player, playerNumber)
      autoFireDetector.addPlayer(player, playerNumber)
    }
    playerActionQueues = actionQueueBuilder.map { it!! }.toTypedArray()
    statsCollector?.markGameAsStarted(server, this)

    /*if(user.getConnectionType() > KailleraUser.CONNECTION_TYPE_GOOD || user.getConnectionType() < KailleraUser.CONNECTION_TYPE_GOOD){
    	//sameDelay = true;
    }*/

    // timeoutMillis = highestPing;
    addEventForAllPlayers(GameStartedEvent(this))
  }

  @Synchronized
  @Throws(UserReadyException::class)
  fun ready(user: KailleraUser?, playerNumber: Int) {
    if (!players.contains(user)) {
      logger.atWarning().log("%s ready game failed: not in %s", user, this)
      throw UserReadyException(EmuLang.getString("KailleraGameImpl.ReadyGameErrorNotInGame"))
    }
    if (status != GameStatus.SYNCHRONIZING) {
      logger.atWarning().log("%s ready failed: %s status is %s", user, this, status)
      throw UserReadyException(EmuLang.getString("KailleraGameImpl.ReadyGameErrorIncorrectState"))
    }
    if (playerActionQueues == null) {
      logger.atSevere().log("%s ready failed: %s playerActionQueues == null!", user, this)
      throw UserReadyException(EmuLang.getString("KailleraGameImpl.ReadyGameErrorInternalError"))
    }
    logger.atInfo().log("%s (player %s) is ready to play: %s", user, playerNumber, this)
    playerActionQueues!![playerNumber - 1].markSynced()
    if (synchedCount == players.size) {
      logger.atInfo().log("%s all players are ready: starting...", this)
      status = GameStatus.PLAYING
      isSynched = true
      startTimeoutTime = clock.now()
      addEventForAllPlayers(AllReadyEvent(this))
      var frameDelay = (highestUserFrameDelay + 1) * owner.connectionType.byteValue - 1
      if (sameDelay) {
        announce("This game's delay is: $highestUserFrameDelay ($frameDelay frame delay)")
      } else {
        var i = 0
        while (i < playerActionQueues!!.size && i < players.size) {
          val player = players[i]
          // do not show delay if stealth mode
          if (!player.inStealthMode) {
            frameDelay = (player.frameDelay + 1) * player.connectionType.byteValue - 1
            announce("P${i + 1} Delay = ${player.frameDelay} ($frameDelay frame delay)")
          }
          i++
        }
      }
    }
  }

  @Synchronized
  @Throws(DropGameException::class)
  fun drop(user: KailleraUser, playerNumber: Int) {
    if (!players.contains(user)) {
      logger.atWarning().log("%s drop game failed: not in %s", user, this)
      throw DropGameException(EmuLang.getString("KailleraGameImpl.DropGameErrorNotInGame"))
    }
    if (playerActionQueues == null) {
      logger.atSevere().log("%s drop failed: %s playerActionQueues == null!", user, this)
      throw DropGameException(EmuLang.getString("KailleraGameImpl.DropGameErrorInternalError"))
    }
    logger.atInfo().log("%s dropped: %s", user, this)
    if (playerNumber - 1 < playerActionQueues!!.size) {
      playerActionQueues!![playerNumber - 1].markDesynced()
    }
    if (synchedCount < 2 && isSynched) {
      isSynched = false
      for (q in playerActionQueues!!) {
        q.markDesynced()
      }
      logger.atInfo().log("%s: game desynched: less than 2 players playing!", this)
    }
    autoFireDetector.stop(playerNumber)
    if (playingCount == 0) {
      if (startN != -1) {
        startN = -1
        announce("StartN is now off.")
      }
      status = GameStatus.WAITING
    }
    addEventForAllPlayers(UserDroppedGameEvent(this, user, playerNumber))
    if (user.ignoringUnnecessaryServerActivity) {
      // KailleraUser u = (KailleraUser) user;
      // u.addEvent(ServerACK.create(.getNextMessageNumber());
      // u.addEvent(new ConnectedEvent(server, user));
      // u.addEvent(new UserQuitEvent(server, user, "Rejoining..."));
      // try{user.quit("Rejoining...");}catch(Exception e){}
      announce("Rejoin server to update client of ignored server activity!", user)
    }
    if (waitingOnData) {
      maybeSendData(user)
    }
  }

  @Synchronized
  @Throws(DropGameException::class, QuitGameException::class, CloseGameException::class)
  fun quit(user: KailleraUser, playerNumber: Int) {
    if (!players.remove(user)) {
      logger.atWarning().log("%s quit game failed: not in %s", user, this)
      throw QuitGameException(EmuLang.getString("KailleraGameImpl.QuitGameErrorNotInGame"))
    }
    logger.atInfo().log("%s quit: %s", user, this)
    addEventForAllPlayers(UserQuitGameEvent(this, user))
    user.ignoringUnnecessaryServerActivity = false
    swap = false
    if (status == GameStatus.WAITING) {
      for (i in players.indices) {
        getPlayer(i + 1)!!.playerNumber = i + 1
        logger.atFine().log(getPlayer(i + 1)!!.name + ":::" + getPlayer(i + 1)!!.playerNumber)
      }
    }
    if (user == owner) server.closeGame(this, user)
    else server.addEvent(GameStatusChangedEvent(server, this))
  }

  @Synchronized
  @Throws(CloseGameException::class)
  fun close(user: KailleraUser) {
    if (user != owner) {
      logger.atWarning().log("%s close game denied: not the owner of %s", user, this)
      throw CloseGameException(EmuLang.getString("KailleraGameImpl.CloseGameErrorNotGameOwner"))
    }
    if (isSynched) {
      isSynched = false
      for (q in playerActionQueues!!) {
        q.markDesynced()
      }
      logger.atInfo().log("%s: game desynched: game closed!", this)
    }
    players.forEach {
      it.apply {
        status = UserStatus.IDLE
        isMuted = false
        ignoringUnnecessaryServerActivity = false
        game = null
      }
    }
    autoFireDetector.stop()
    players.clear()
  }

  @Synchronized
  fun droppedPacket(user: KailleraUser) {
    if (!isSynched) return
    val playerNumber = user.playerNumber
    if (user.playerNumber > playerActionQueues!!.size) {
      logger
        .atInfo()
        .log(
          "%s: %s: player desynched: dropped a packet! Also left the game already: KailleraGameImpl -> DroppedPacket",
          this,
          user,
        )
    }
    if (playerActionQueues != null && playerActionQueues!![playerNumber - 1].synced) {
      playerActionQueues!![playerNumber - 1].markDesynced()
      logger.atInfo().log("%s: %s: player desynched: dropped a packet!", this, user)
      addEventForAllPlayers(
        PlayerDesynchEvent(
          this,
          user,
          EmuLang.getString("KailleraGameImpl.DesynchDetectedDroppedPacket", user.name),
        )
      )
      if (synchedCount < 2 && isSynched) {
        isSynched = false
        for (q in playerActionQueues!!) q.markDesynced()
        logger.atInfo().log("%s: game desynched: less than 2 players synched!", this)
      }
    }
  }

  /**
   * Adds data and suspends until all data is available, at which time it returns the sends new data
   * back to the client.
   */
  @Throws(GameDataException::class)
  fun addData(user: KailleraUser, playerNumber: Int, data: ByteArray): Result<Unit> {
    val playerActionQueuesCopy = playerActionQueues ?: return Result.success(Unit)

    // int bytesPerAction = (data.length / actionsPerMessage);
    // int arraySize = (playerActionQueues.length * actionsPerMessage * user.getBytesPerAction());
    if (!isSynched) {
      return Result.failure(
        GameDataException(
          EmuLang.getString("KailleraGameImpl.DesynchedWarning"),
          data,
          actionsPerMessage,
          playerNumber,
          playerActionQueuesCopy.size,
        )
      )
    }
    // Add the data for the user to their own player queue.
    playerActionQueuesCopy[playerNumber - 1].addActions(data)
    autoFireDetector.addData(playerNumber, data, user.bytesPerAction)

    return maybeSendData(user, data)
  }

  /** @param data Only used for logging. */
  fun maybeSendData(user: KailleraUser, data: ByteArray = byteArrayOf()): Result<Unit> {
    val playerActionQueuesCopy = checkNotNull(playerActionQueues)

    // TODO(nue): This works for 2P but what about more? This probably results in unnecessary
    // messages.
    var timeoutCounter = 0
    for (player in players) {
      val playerNumber = player.playerNumber

      // If all are either desynced or have their own information (meaning they have submitted
      // their own data i guess)
      if (
        playerActionQueuesCopy.all {
          !it.synced ||
            it.containsNewDataForPlayer(
              playerIndex = playerNumber - 1,
              actionLength = actionsPerMessage * user.bytesPerAction,
            )
        }
      ) {
        waitingOnData = false
        val response = ByteArray(user.arraySize)
        for (actionCounter in 0 until actionsPerMessage) {
          for (playerActionQueueIndex in playerActionQueuesCopy.indices) {
            // TODO(nue): Consider removing this loop, I'm fairly certain it isn't needed.
            while (isSynched) {
              try {
                playerActionQueuesCopy[playerActionQueueIndex].getActionAndWriteToArray(
                  playerIndex = playerNumber - 1,
                  writeToArray = response,
                  writeAtIndex =
                    actionCounter * (playerActionQueuesCopy.size * user.bytesPerAction) +
                      playerActionQueueIndex * user.bytesPerAction,
                  actionLength = user.bytesPerAction,
                )
                break
              } catch (e: PlayerTimeoutException) {
                // Note: this code only executes when we have data for all users, I think timeouts
                // never happen anymore.
                e.timeoutNumber = ++timeoutCounter
                handleTimeout(e)
              }
            }
          }
        }
        if (!isSynched) {
          return Result.failure(
            GameDataException(
              EmuLang.getString("KailleraGameImpl.DesynchedWarning"),
              data,
              user.bytesPerAction,
              playerNumber,
              playerActionQueuesCopy.size,
            )
          )
        }
        player.queueEvent(GameDataEvent(this, response))
        player.updateUserDrift()
        val firstPlayer = players.firstOrNull()
        if (firstPlayer != null && firstPlayer.id == player.id) {
          updateGameDrift()
        }
      } else {
        waitingOnData = true
        playerActionQueuesCopy.forEach {
          waitingOnPlayerNumber[it.playerNumber - 1] =
            it.synced &&
              !it.containsNewDataForPlayer(
                playerIndex = playerNumber - 1,
                actionLength = actionsPerMessage * user.bytesPerAction,
              )
        }
      }
    }
    return Result.success(Unit)
  }

  fun resetLag() {
    totalDriftCache.clear()
    totalDriftNs = 0
    lastLagReset = clock.now()
  }

  /** Sets the game framerate for lag measuring purposes. */
  fun setGameFps(fps: Double) {
    singleFrameDurationForLagCalculationOnlyNs =
      (1.seconds / players.first().connectionType.getUpdatesPerSecond(fps)).inWholeNanoseconds
    resetLag()
    for (player in players) {
      player.resetLag()
    }
  }

  private fun updateGameDrift() {
    val nowNs = System.nanoTime()
    val delaySinceLastResponseNs = nowNs - lastFrameNs

    lagLeewayNs += singleFrameDurationForLagCalculationOnlyNs - delaySinceLastResponseNs
    if (lagLeewayNs < 0) {
      // Lag leeway fell below zero. Lag occurred!
      totalDriftNs += lagLeewayNs
      lagLeewayNs = 0
    } else if (lagLeewayNs > singleFrameDurationForLagCalculationOnlyNs) {
      // Does not make sense to allow lag leeway to be longer than the length of one frame.
      lagLeewayNs = singleFrameDurationForLagCalculationOnlyNs
    }
    totalDriftCache.update(totalDriftNs, nowNs = nowNs)
    lastFrameNs = nowNs
  }

  // it's very important this method is synchronized
  @Synchronized
  private fun handleTimeout(e: PlayerTimeoutException) {
    if (!isSynched) return
    val playerNumber = e.playerNumber
    val timeoutNumber = e.timeoutNumber
    val playerActionQueue = playerActionQueues!![playerNumber - 1]
    if (!playerActionQueue.synced || e == playerActionQueue.lastTimeout) return
    playerActionQueue.lastTimeout = e
    val player: KailleraUser = e.player!!
    if (timeoutNumber < desynchTimeouts) {
      if (timeoutNumber % 12 == 0) {
        logger.atInfo().log("%s: %s: Timeout #%d", this, player, timeoutNumber / 12)
        addEventForAllPlayers(GameTimeoutEvent(this, player, timeoutNumber / 12))
      }
    } else {
      logger.atInfo().log("%s: %s: Timeout #%d", this, player, timeoutNumber / 12)
      playerActionQueue.markDesynced()
      logger.atInfo().log("%s: %s: player desynched: Lagged!", this, player)
      addEventForAllPlayers(
        PlayerDesynchEvent(
          this,
          player,
          EmuLang.getString("KailleraGameImpl.DesynchDetectedPlayerLagged", player.name),
        )
      )
      if (synchedCount < 2) {
        isSynched = false
        for (q in this.playerActionQueues!!) {
          q.markDesynced()
        }
        logger.atInfo().log("%s: game desynched: less than 2 players synched!", this)
      }
    }
  }

  companion object {
    private val logger = FluentLogger.forEnclosingClass()

    /** Unfortunately Kaillera is built on the assumption that all games run at 60FPS. */
    const val GAME_FPS = 60
  }
}
