package org.emulinker.kaillera.controller.v086.protocol

import java.nio.ByteBuffer
import kotlin.Throws
import org.emulinker.kaillera.controller.messaging.MessageFormatException
import org.emulinker.kaillera.controller.messaging.ParseException
import org.emulinker.util.EmuUtil

const val ID: Byte = 0x06

private const val DESC = "Client to Server ACK"

data class ClientACK
    @Throws(MessageFormatException::class)
    constructor(private val internalMessageNumber: Int) : ACK() {

  init {
    validateMessageNumber(internalMessageNumber, DESC)
  }

  override val val1: Long = 0
  override val val2: Long = 1
  override val val3: Long = 2
  override val val4: Long = 3

  // TODO(nue): Refactor these into vals.
  override fun description() = DESC
  override fun messageId() = ID
  override fun messageNumber() = internalMessageNumber

  companion object {

    @JvmStatic
    @Throws(ParseException::class, MessageFormatException::class)
    fun parse(messageNumber: Int, buffer: ByteBuffer): ClientACK {
      if (buffer.remaining() < 17) throw ParseException("Failed byte count validation!")
      val b = buffer.get()
      if (b.toInt() != 0x00) {
        throw MessageFormatException(
            "Invalid " + DESC + " format: byte 0 = " + EmuUtil.byteToHex(b))
      }

      // long val1 = UnsignedUtil.getUnsignedInt(buffer);
      // long val2 = UnsignedUtil.getUnsignedInt(buffer);
      // long val3 = UnsignedUtil.getUnsignedInt(buffer);
      // long val4 = UnsignedUtil.getUnsignedInt(buffer);

      // if (val1 != 0 || val2 != 1 || val3 != 2 || val4 != 3)
      // throw new MessageFormatException("Invalid " + DESC + " format: bytes do not match
      // acceptable
      // format!");
      return ClientACK(messageNumber)
    }
  }
}
