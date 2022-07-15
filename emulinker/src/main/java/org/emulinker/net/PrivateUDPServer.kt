package org.emulinker.net

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Timer
import com.google.common.flogger.FluentLogger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import kotlinx.coroutines.CoroutineScope
import org.emulinker.util.EmuUtil.formatSocketAddress

private val logger = FluentLogger.forEnclosingClass()

abstract class PrivateUDPServer(
    shutdownOnExit: Boolean, val remoteInetAddress: InetAddress, metrics: MetricRegistry
) : UDPServer(shutdownOnExit, metrics) {

  private val clientRequestTimer: Timer

  lateinit var remoteSocketAddress: InetSocketAddress
    private set

  override suspend fun handleReceived(
      buffer: ByteBuffer, remoteSocketAddress: InetSocketAddress, requestScope: CoroutineScope
  ) {
    if (!this::remoteSocketAddress.isInitialized) {
      this.remoteSocketAddress = remoteSocketAddress
    } else if (remoteSocketAddress != this.remoteSocketAddress) {
      logger
          .atWarning()
          .log(
              "Rejecting packet received from wrong address. Expected=%s but was %s",
              formatSocketAddress(this.remoteSocketAddress),
              formatSocketAddress(remoteSocketAddress))

      return
    }
    clientRequestTimer.time().use { handleReceived(buffer) }
  }

  protected abstract suspend fun handleReceived(buffer: ByteBuffer)

  protected suspend fun send(buffer: ByteBuffer) {
    super.send(buffer, remoteSocketAddress)
  }

  init {
    clientRequestTimer = metrics.timer(MetricRegistry.name(this.javaClass, "clientRequests"))
  }
}
