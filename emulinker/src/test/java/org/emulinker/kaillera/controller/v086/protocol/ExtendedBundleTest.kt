package org.emulinker.kaillera.controller.v086.protocol

import java.nio.charset.Charset
import org.emulinker.kaillera.controller.v086.V086Utils
import org.emulinker.kaillera.pico.AppModule
import org.junit.Test

class ExtendedBundleTest {

  @Test
  fun processBundle() {
    AppModule.charsetDoNotUse = Charset.forName("Shift_JIS")
    val individualTests = TESTDATA.trimIndent().split("\n\n")

    for ((index, data) in individualTests.withIndex()) {
      println("Starting test with index: $index")

      val byteString = data.trim().lines().joinToString(separator = " ") { it.substring(7..54) }

      println(
        "Parsing legacy way for first line ${byteString.split(" ").take(16).joinToString(" ")}"
      )

      val oldBundle = V086Bundle.parse(V086Utils.hexStringToByteBuffer(byteString))
      println(oldBundle)

      //      println("Parsing new way")
      //
      //      val newBundle =
      //        V086Bundle.parse(
      //          ByteReadPacket(V086Utils.hexStringToByteBuffer(byteString)),
      //         // lastMessageID = 0
      //        )
      //
      //      assertThat(oldBundle.numMessages).isEqualTo(newBundle.numMessages)
      //      assertThat(oldBundle.messages.asList())
      //        .containsExactlyElementsIn(newBundle.messages)
      //        .inOrder()
    }
  }
}

private const val TESTDATA =
  """
0000   01 00 00 2f 00 03 6e 75 65 5b 43 46 31 5d 40 74   .../..nue[CF1]@t
0010   65 73 74 00 50 72 6f 6a 65 63 74 20 36 34 6b 20   est.Project 64k
0020   30 2e 31 33 20 28 30 31 20 41 75 67 20 32 30 30   0.13 (01 Aug 200
0030   33 29 00 01                                       3)..

0000   01 00 00 12 00 05 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00                              .......

0000   02 01 00 12 00 06 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00 00 00 2f 00 03 6e 75 65 5b   ........./..nue[
0020   43 46 31 5d 40 74 65 73 74 00 50 72 6f 6a 65 63   CF1]@test.Projec
0030   74 20 36 34 6b 20 30 2e 31 33 20 28 30 31 20 41   t 64k 0.13 (01 A
0040   75 67 20 32 30 30 33 29 00 01                     ug 2003)..

0000   02 01 00 12 00 05 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00 00 00 12 00 05 00 00 00 00   ................
0020   00 01 00 00 00 02 00 00 00 03 00 00 00            .............

0000   03 02 00 12 00 06 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00 01 00 12 00 06 00 00 00 00   ................
0020   00 01 00 00 00 02 00 00 00 03 00 00 00 00 00 2f   .............../
0030   00 03 6e 75 65 5b 43 46 31 5d 40 74 65 73 74 00   ..nue[CF1]@test.
0040   50 72 6f 6a 65 63 74 20 36 34 6b 20 30 2e 31 33   Project 64k 0.13
0050   20 28 30 31 20 41 75 67 20 32 30 30 33 29 00 01    (01 Aug 2003)..

0000   03 02 00 12 00 05 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00 01 00 12 00 05 00 00 00 00   ................
0020   00 01 00 00 00 02 00 00 00 03 00 00 00 00 00 12   ................
0030   00 05 00 00 00 00 00 01 00 00 00 02 00 00 00 03   ................
0040   00 00 00                                          ...

0000   03 03 00 12 00 06 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00 02 00 12 00 06 00 00 00 00   ................
0020   00 01 00 00 00 02 00 00 00 03 00 00 00 01 00 12   ................
0030   00 06 00 00 00 00 00 01 00 00 00 02 00 00 00 03   ................
0040   00 00 00                                          ...

0000   04 03 00 12 00 05 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00 02 00 12 00 05 00 00 00 00   ................
0020   00 01 00 00 00 02 00 00 00 03 00 00 00 01 00 12   ................
0030   00 05 00 00 00 00 00 01 00 00 00 02 00 00 00 03   ................
0040   00 00 00 00 00 12 00 05 00 00 00 00 00 01 00 00   ................
0050   00 02 00 00 00 03 00 00 00                        .........

0000   03 04 00 12 00 06 00 00 00 00 00 01 00 00 00 02   ................
0010   00 00 00 03 00 00 00 03 00 12 00 06 00 00 00 00   ................
0020   00 01 00 00 00 02 00 00 00 03 00 00 00 02 00 12   ................
0030   00 06 00 00 00 00 00 01 00 00 00 02 00 00 00 03   ................
0040   00 00 00                                          ...

0000   05 04 00 19 00 04 00 01 00 00 00 00 00 00 00 82   ................
0010   b6 82 e5 82 8e 00 0a 00 00 00 01 02 00 01 03 00   ................
0020   12 00 05 00 00 00 00 00 01 00 00 00 02 00 00 00   ................
0030   03 00 00 00 02 00 12 00 05 00 00 00 00 00 01 00   ................
0040   00 00 02 00 00 00 03 00 00 00 01 00 12 00 05 00   ................
0050   00 00 00 00 01 00 00 00 02 00 00 00 03 00 00 00   ................
0060   00 00 12 00 05 00 00 00 00 00 01 00 00 00 02 00   ................
0070   00 00 03 00 00 00                                 ......

0000   05 05 00 1c 00 17 73 65 72 76 65 72 00 ea 4b 8e   ......server..K.
0010   49 20 28 6e 75 65 2e 31 32 63 62 2e 64 65 76 29   I (nue.12cb.dev)
0020   00 04 00 19 00 04 00 01 00 00 00 00 00 00 00 82   ................
0030   b6 82 e5 82 8e 00 0a 00 00 00 01 02 00 01 03 00   ................
0040   12 00 05 00 00 00 00 00 01 00 00 00 02 00 00 00   ................
0050   03 00 00 00 02 00 12 00 05 00 00 00 00 00 01 00   ................
0060   00 00 02 00 00 00 03 00 00 00 01 00 12 00 05 00   ................
0070   00 00 00 00 01 00 00 00 02 00 00 00 03 00 00 00   ................

0000   05 06 00 34 00 17 73 65 72 76 65 72 00 8a c7 97   ...4..server....
0010   9d 90 6c 81 46 68 74 74 70 73 3a 2f 2f 74 77 69   ..l.Fhttps://twi
0020   74 74 65 72 2e 63 6f 6d 2f 36 6b 52 74 36 32 72   tter.com/6kRt62r
0030   32 7a 76 4b 70 35 52 68 00 05 00 1c 00 17 73 65   2zvKp5Rh......se
0040   72 76 65 72 00 ea 4b 8e 49 20 28 6e 75 65 2e 31   rver..K.I (nue.1
0050   32 63 62 2e 64 65 76 29 00 04 00 19 00 04 00 01   2cb.dev)........
0060   00 00 00 00 00 00 00 82 b6 82 e5 82 8e 00 0a 00   ................
0070   00 00 01 02 00 01 03 00 12 00 05 00 00 00 00 00   ................
0080   01 00 00 00 02 00 00 00 03 00 00 00 02 00 12 00   ................
0090   05 00 00 00 00 00 01 00 00 00 02 00 00 00 03 00   ................
00a0   00 00                                             ..

0000   05 07 00 49 00 17 73 65 72 76 65 72 00 45 6d 75   ...I..server.Emu
0010   4c 69 6e 6b 65 72 2d 4b 20 76 30 2e 31 31 2e 32   Linker-K v0.11.2
0020   3a 20 68 74 74 70 73 3a 2f 2f 67 69 74 68 75 62   : https://github
0030   2e 63 6f 6d 2f 68 6f 70 73 6b 69 70 6e 66 61 6c   .com/hopskipnfal
0040   6c 2f 45 6d 75 4c 69 6e 6b 65 72 2d 4b 00 06 00   l/EmuLinker-K...
0050   34 00 17 73 65 72 76 65 72 00 8a c7 97 9d 90 6c   4..server......l
0060   81 46 68 74 74 70 73 3a 2f 2f 74 77 69 74 74 65   .Fhttps://twitte
0070   72 2e 63 6f 6d 2f 36 6b 52 74 36 32 72 32 7a 76   r.com/6kRt62r2zv
0080   4b 70 35 52 68 00 05 00 1c 00 17 73 65 72 76 65   Kp5Rh......serve
0090   72 00 ea 4b 8e 49 20 28 6e 75 65 2e 31 32 63 62   r..K.I (nue.12cb
00a0   2e 64 65 76 29 00 04 00 19 00 04 00 01 00 00 00   .dev)...........
00b0   00 00 00 00 82 b6 82 e5 82 8e 00 0a 00 00 00 01   ................
00c0   02 00 01 03 00 12 00 05 00 00 00 00 00 01 00 00   ................
00d0   00 02 00 00 00 03 00 00 00                        .........

0000   03 07 00 02 00 15 00 06 00 06 00 11 00 ff ff ff   ................
0010   ff 05 00 37 00 0a 00 4e 69 6e 74 65 6e 64 6f 20   ...7...Nintendo
0020   41 6c 6c 2d 53 74 61 72 21 20 44 61 69 72 61 6e   All-Star! Dairan
0030   74 6f 75 20 53 6d 61 73 68 20 42 72 6f 74 68 65   tou Smash Brothe
0040   72 73 20 28 4a 29 00 00 ff ff ff ff               rs (J)......

0000   03 05 00 37 00 0a 00 4e 69 6e 74 65 6e 64 6f 20   ...7...Nintendo
0010   41 6c 6c 2d 53 74 61 72 21 20 44 61 69 72 61 6e   All-Star! Dairan
0020   74 6f 75 20 53 6d 61 73 68 20 42 72 6f 74 68 65   tou Smash Brothe
0030   72 73 20 28 4a 29 00 00 ff ff ff ff 04 00 12 00   rs (J)..........
0040   06 00 00 00 00 00 01 00 00 00 02 00 00 00 03 00   ................
0050   00 00 03 00 12 00 06 00 00 00 00 00 01 00 00 00   ................
0060   02 00 00 00 03 00 00 00                           ........

0000   05 09 00 62 00 0a 6e 75 65 5b 43 46 31 5d 40 74   ...b..nue[CF1]@t
0010   65 73 74 00 4e 69 6e 74 65 6e 64 6f 20 41 6c 6c   est.Nintendo All
0020   2d 53 74 61 72 21 20 44 61 69 72 61 6e 74 6f 75   -Star! Dairantou
0030   20 53 6d 61 73 68 20 42 72 6f 74 68 65 72 73 20    Smash Brothers
0040   28 4a 29 00 50 72 6f 6a 65 63 74 20 36 34 6b 20   (J).Project 64k
0050   30 2e 31 33 20 28 30 31 20 41 75 67 20 32 30 30   0.13 (01 Aug 200
0060   33 29 00 09 00 00 00 08 00 16 00 02 6e 75 65 5b   3)..........nue[
0070   43 46 31 5d 40 74 65 73 74 00 10 00 0e 00 00 00   CF1]@test.......
0080   01 07 00 49 00 17 73 65 72 76 65 72 00 45 6d 75   ...I..server.Emu
0090   4c 69 6e 6b 65 72 2d 4b 20 76 30 2e 31 31 2e 32   Linker-K v0.11.2
00a0   3a 20 68 74 74 70 73 3a 2f 2f 67 69 74 68 75 62   : https://github
00b0   2e 63 6f 6d 2f 68 6f 70 73 6b 69 70 6e 66 61 6c   .com/hopskipnfal
00c0   6c 2f 45 6d 75 4c 69 6e 6b 65 72 2d 4b 00 06 00   l/EmuLinker-K...
00d0   34 00 17 73 65 72 76 65 72 00 8a c7 97 9d 90 6c   4..server......l
00e0   81 46 68 74 74 70 73 3a 2f 2f 74 77 69 74 74 65   .Fhttps://twitte
00f0   72 2e 63 6f 6d 2f 36 6b 52 74 36 32 72 32 7a 76   r.com/6kRt62r2zv
0100   4b 70 35 52 68 00 05 00 1c 00 17 73 65 72 76 65   Kp5Rh......serve
0110   72 00 ea 4b 8e 49 20 28 6e 75 65 2e 31 32 63 62   r..K.I (nue.12cb
0120   2e 64 65 76 29 00                                 .dev).

0000   05 0a 00 09 00 0e 00 09 00 00 00 00 01 08 09 00   ................
0010   62 00 0a 6e 75 65 5b 43 46 31 5d 40 74 65 73 74   b..nue[CF1]@test
0020   00 4e 69 6e 74 65 6e 64 6f 20 41 6c 6c 2d 53 74   .Nintendo All-St
0030   61 72 21 20 44 61 69 72 61 6e 74 6f 75 20 53 6d   ar! Dairantou Sm
0040   61 73 68 20 42 72 6f 74 68 65 72 73 20 28 4a 29   ash Brothers (J)
0050   00 50 72 6f 6a 65 63 74 20 36 34 6b 20 30 2e 31   .Project 64k 0.1
0060   33 20 28 30 31 20 41 75 67 20 32 30 30 33 29 00   3 (01 Aug 2003).
0070   09 00 00 00 08 00 16 00 02 6e 75 65 5b 43 46 31   .........nue[CF1
0080   5d 40 74 65 73 74 00 10 00 0e 00 00 00 01 07 00   ]@test..........
0090   49 00 17 73 65 72 76 65 72 00 45 6d 75 4c 69 6e   I..server.EmuLin
00a0   6b 65 72 2d 4b 20 76 30 2e 31 31 2e 32 3a 20 68   ker-K v0.11.2: h
00b0   74 74 70 73 3a 2f 2f 67 69 74 68 75 62 2e 63 6f   ttps://github.co
00c0   6d 2f 68 6f 70 73 6b 69 70 6e 66 61 6c 6c 2f 45   m/hopskipnfall/E
00d0   6d 75 4c 69 6e 6b 65 72 2d 4b 00 06 00 34 00 17   muLinker-K...4..
00e0   73 65 72 76 65 72 00 8a c7 97 9d 90 6c 81 46 68   server......l.Fh
00f0   74 74 70 73 3a 2f 2f 74 77 69 74 74 65 72 2e 63   ttps://twitter.c
0100   6f 6d 2f 36 6b 52 74 36 32 72 32 7a 76 4b 70 35   om/6kRt62r2zvKp5
0110   52 68 00                                          Rh.

0000   05 0b 00 06 00 0d 00 00 00 00 00 0a 00 09 00 0e   ................
0010   00 09 00 00 00 00 01 08 09 00 62 00 0a 6e 75 65   ..........b..nue
0020   5b 43 46 31 5d 40 74 65 73 74 00 4e 69 6e 74 65   [CF1]@test.Ninte
0030   6e 64 6f 20 41 6c 6c 2d 53 74 61 72 21 20 44 61   ndo All-Star! Da
0040   69 72 61 6e 74 6f 75 20 53 6d 61 73 68 20 42 72   irantou Smash Br
0050   6f 74 68 65 72 73 20 28 4a 29 00 50 72 6f 6a 65   others (J).Proje
0060   63 74 20 36 34 6b 20 30 2e 31 33 20 28 30 31 20   ct 64k 0.13 (01
0070   41 75 67 20 32 30 30 33 29 00 09 00 00 00 08 00   Aug 2003).......
0080   16 00 02 6e 75 65 5b 43 46 31 5d 40 74 65 73 74   ...nue[CF1]@test
0090   00 10 00 0e 00 00 00 01 07 00 49 00 17 73 65 72   ..........I..ser
00a0   76 65 72 00 45 6d 75 4c 69 6e 6b 65 72 2d 4b 20   ver.EmuLinker-K
00b0   76 30 2e 31 31 2e 32 3a 20 68 74 74 70 73 3a 2f   v0.11.2: https:/
00c0   2f 67 69 74 68 75 62 2e 63 6f 6d 2f 68 6f 70 73   /github.com/hops
00d0   6b 69 70 6e 66 61 6c 6c 2f 45 6d 75 4c 69 6e 6b   kipnfall/EmuLink
00e0   65 72 2d 4b 00                                    er-K.

0000   05 0c 00 1b 00 0c 00 09 00 00 00 6e 75 65 5b 43   ...........nue[C
0010   46 31 5d 40 74 65 73 74 00 0e 00 00 00 10 00 01   F1]@test........
0020   0b 00 06 00 0d 00 00 00 00 00 0a 00 09 00 0e 00   ................
0030   09 00 00 00 00 01 08 09 00 62 00 0a 6e 75 65 5b   .........b..nue[
0040   43 46 31 5d 40 74 65 73 74 00 4e 69 6e 74 65 6e   CF1]@test.Ninten
0050   64 6f 20 41 6c 6c 2d 53 74 61 72 21 20 44 61 69   do All-Star! Dai
0060   72 61 6e 74 6f 75 20 53 6d 61 73 68 20 42 72 6f   rantou Smash Bro
0070   74 68 65 72 73 20 28 4a 29 00 50 72 6f 6a 65 63   thers (J).Projec
0080   74 20 36 34 6b 20 30 2e 31 33 20 28 30 31 20 41   t 64k 0.13 (01 A
0090   75 67 20 32 30 30 33 29 00 09 00 00 00 08 00 16   ug 2003)........
00a0   00 02 6e 75 65 5b 43 46 31 5d 40 74 65 73 74 00   ..nue[CF1]@test.
00b0   10 00 0e 00 00 00 01                              .......

0000   05 0d 00 47 00 17 73 65 72 76 65 72 00 6e 75 65   ...G..server.nue
0010   5b 43 46 31 5d 40 74 65 73 74 3a 20 4e 69 6e 74   [CF1]@test: Nint
0020   65 6e 64 6f 20 41 6c 6c 2d 53 74 61 72 21 20 44   endo All-Star! D
0030   61 69 72 61 6e 74 6f 75 20 53 6d 61 73 68 20 42   airantou Smash B
0040   72 6f 74 68 65 72 73 20 28 4a 29 00 0c 00 1b 00   rothers (J).....
0050   0c 00 09 00 00 00 6e 75 65 5b 43 46 31 5d 40 74   ......nue[CF1]@t
0060   65 73 74 00 0e 00 00 00 10 00 01 0b 00 06 00 0d   est.............
0070   00 00 00 00 00 0a 00 09 00 0e 00 09 00 00 00 00   ................
0080   01 08 09 00 62 00 0a 6e 75 65 5b 43 46 31 5d 40   ....b..nue[CF1]@
0090   74 65 73 74 00 4e 69 6e 74 65 6e 64 6f 20 41 6c   test.Nintendo Al
00a0   6c 2d 53 74 61 72 21 20 44 61 69 72 61 6e 74 6f   l-Star! Dairanto
00b0   75 20 53 6d 61 73 68 20 42 72 6f 74 68 65 72 73   u Smash Brothers
00c0   20 28 4a 29 00 50 72 6f 6a 65 63 74 20 36 34 6b    (J).Project 64k
00d0   20 30 2e 31 33 20 28 30 31 20 41 75 67 20 32 30    0.13 (01 Aug 20
00e0   30 33 29 00 09 00 00 00                           03).....

0000   05 0e 00 52 00 08 53 65 72 76 65 72 00 82 a0 82   ...R..Server....
0010   c6 32 30 95 62 95 e5 8f 57 83 63 83 43 81 5b 83   .20.b...W.c.C.[.
0020   67 82 f0 91 97 82 e8 82 dc 82 b7 81 42 22 2f 73   g...........B"/s
0030   74 6f 70 22 82 cc 83 52 83 7d 83 93 83 68 82 c5   top"...R.}...h..
0040   82 e2 82 df 82 e9 82 b1 82 c6 82 aa 82 c5 82 ab   ................
0050   82 dc 82 b7 81 42 00 0d 00 47 00 17 73 65 72 76   .....B...G..serv
0060   65 72 00 6e 75 65 5b 43 46 31 5d 40 74 65 73 74   er.nue[CF1]@test
0070   3a 20 4e 69 6e 74 65 6e 64 6f 20 41 6c 6c 2d 53   : Nintendo All-S
0080   74 61 72 21 20 44 61 69 72 61 6e 74 6f 75 20 53   tar! Dairantou S
0090   6d 61 73 68 20 42 72 6f 74 68 65 72 73 20 28 4a   mash Brothers (J
00a0   29 00 0c 00 1b 00 0c 00 09 00 00 00 6e 75 65 5b   )...........nue[
00b0   43 46 31 5d 40 74 65 73 74 00 0e 00 00 00 10 00   CF1]@test.......
00c0   01 0b 00 06 00 0d 00 00 00 00 00 0a 00 09 00 0e   ................
00d0   00 09 00 00 00 00 01 08                           ........

0000   03 06 00 06 00 11 00 ff ff ff ff 05 00 37 00 0a   .............7..
0010   00 4e 69 6e 74 65 6e 64 6f 20 41 6c 6c 2d 53 74   .Nintendo All-St
0020   61 72 21 20 44 61 69 72 61 6e 74 6f 75 20 53 6d   ar! Dairantou Sm
0030   61 73 68 20 42 72 6f 74 68 65 72 73 20 28 4a 29   ash Brothers (J)
0040   00 00 ff ff ff ff 04 00 12 00 06 00 00 00 00 00   ................
0050   01 00 00 00 02 00 00 00 03 00 00 00               ............

0000   05 0f 00 09 00 0e 00 09 00 00 00 01 01 08 0e 00   ................
0010   52 00 08 53 65 72 76 65 72 00 82 a0 82 c6 32 30   R..Server.....20
0020   95 62 95 e5 8f 57 83 63 83 43 81 5b 83 67 82 f0   .b...W.c.C.[.g..
0030   91 97 82 e8 82 dc 82 b7 81 42 22 2f 73 74 6f 70   .........B"/stop
0040   22 82 cc 83 52 83 7d 83 93 83 68 82 c5 82 e2 82   "...R.}...h.....
0050   df 82 e9 82 b1 82 c6 82 aa 82 c5 82 ab 82 dc 82   ................
0060   b7 81 42 00 0d 00 47 00 17 73 65 72 76 65 72 00   ..B...G..server.
0070   6e 75 65 5b 43 46 31 5d 40 74 65 73 74 3a 20 4e   nue[CF1]@test: N
0080   69 6e 74 65 6e 64 6f 20 41 6c 6c 2d 53 74 61 72   intendo All-Star
0090   21 20 44 61 69 72 61 6e 74 6f 75 20 53 6d 61 73   ! Dairantou Smas
00a0   68 20 42 72 6f 74 68 65 72 73 20 28 4a 29 00 0c   h Brothers (J)..
00b0   00 1b 00 0c 00 09 00 00 00 6e 75 65 5b 43 46 31   .........nue[CF1
00c0   5d 40 74 65 73 74 00 0e 00 00 00 10 00 01 0b 00   ]@test..........
00d0   06 00 0d 00 00 00 00 00                           ........

0000   05 10 00 06 00 11 00 01 00 01 01 0f 00 09 00 0e   ................
0010   00 09 00 00 00 01 01 08 0e 00 52 00 08 53 65 72   ..........R..Ser
0020   76 65 72 00 82 a0 82 c6 32 30 95 62 95 e5 8f 57   ver.....20.b...W
0030   83 63 83 43 81 5b 83 67 82 f0 91 97 82 e8 82 dc   .c.C.[.g........
0040   82 b7 81 42 22 2f 73 74 6f 70 22 82 cc 83 52 83   ...B"/stop"...R.
0050   7d 83 93 83 68 82 c5 82 e2 82 df 82 e9 82 b1 82   }...h...........
0060   c6 82 aa 82 c5 82 ab 82 dc 82 b7 81 42 00 0d 00   ............B...
0070   47 00 17 73 65 72 76 65 72 00 6e 75 65 5b 43 46   G..server.nue[CF
0080   31 5d 40 74 65 73 74 3a 20 4e 69 6e 74 65 6e 64   1]@test: Nintend
0090   6f 20 41 6c 6c 2d 53 74 61 72 21 20 44 61 69 72   o All-Star! Dair
00a0   61 6e 74 6f 75 20 53 6d 61 73 68 20 42 72 6f 74   antou Smash Brot
00b0   68 65 72 73 20 28 4a 29 00 0c 00 1b 00 0c 00 09   hers (J)........
00c0   00 00 00 6e 75 65 5b 43 46 31 5d 40 74 65 73 74   ...nue[CF1]@test
00d0   00 0e 00 00 00 10 00 01                           ........

0000   03 07 00 02 00 15 00 06 00 06 00 11 00 ff ff ff   ................
0010   ff 05 00 37 00 0a 00 4e 69 6e 74 65 6e 64 6f 20   ...7...Nintendo
0020   41 6c 6c 2d 53 74 61 72 21 20 44 61 69 72 61 6e   All-Star! Dairan
0030   74 6f 75 20 53 6d 61 73 68 20 42 72 6f 74 68 65   tou Smash Brothe
0040   72 73 20 28 4a 29 00 00 ff ff ff ff               rs (J)......

0000   05 11 00 09 00 0e 00 09 00 00 00 02 01 08 10 00   ................
0010   06 00 11 00 01 00 01 01 0f 00 09 00 0e 00 09 00   ................
0020   00 00 01 01 08 0e 00 52 00 08 53 65 72 76 65 72   .......R..Server
0030   00 82 a0 82 c6 32 30 95 62 95 e5 8f 57 83 63 83   .....20.b...W.c.
0040   43 81 5b 83 67 82 f0 91 97 82 e8 82 dc 82 b7 81   C.[.g...........
0050   42 22 2f 73 74 6f 70 22 82 cc 83 52 83 7d 83 93   B"/stop"...R.}..
0060   83 68 82 c5 82 e2 82 df 82 e9 82 b1 82 c6 82 aa   .h..............
0070   82 c5 82 ab 82 dc 82 b7 81 42 00 0d 00 47 00 17   .........B...G..
0080   73 65 72 76 65 72 00 6e 75 65 5b 43 46 31 5d 40   server.nue[CF1]@
0090   74 65 73 74 3a 20 4e 69 6e 74 65 6e 64 6f 20 41   test: Nintendo A
00a0   6c 6c 2d 53 74 61 72 21 20 44 61 69 72 61 6e 74   ll-Star! Dairant
00b0   6f 75 20 53 6d 61 73 68 20 42 72 6f 74 68 65 72   ou Smash Brother
00c0   73 20 28 4a 29 00                                 s (J).

0000   05 08 00 16 00 02 6e 75 65 5b 43 46 31 5d 40 74   ......nue[CF1]@t
0010   65 73 74 00 10 00 0e 00 00 00 01 07 00 49 00 17   est..........I..
0020   73 65 72 76 65 72 00 45 6d 75 4c 69 6e 6b 65 72   server.EmuLinker
0030   2d 4b 20 76 30 2e 31 31 2e 32 3a 20 68 74 74 70   -K v0.11.2: http
0040   73 3a 2f 2f 67 69 74 68 75 62 2e 63 6f 6d 2f 68   s://github.com/h
0050   6f 70 73 6b 69 70 6e 66 61 6c 6c 2f 45 6d 75 4c   opskipnfall/EmuL
0060   69 6e 6b 65 72 2d 4b 00 06 00 34 00 17 73 65 72   inker-K...4..ser
0070   76 65 72 00 8a c7 97 9d 90 6c 81 46 68 74 74 70   ver......l.Fhttp
0080   73 3a 2f 2f 74 77 69 74 74 65 72 2e 63 6f 6d 2f   s://twitter.com/
0090   36 6b 52 74 36 32 72 32 7a 76 4b 70 35 52 68 00   6kRt62r2zvKp5Rh.
00a0   05 00 1c 00 17 73 65 72 76 65 72 00 ea 4b 8e 49   .....server..K.I
00b0   20 28 6e 75 65 2e 31 32 63 62 2e 64 65 76 29 00    (nue.12cb.dev).
00c0   04 00 19 00 04 00 01 00 00 00 00 00 00 00 82 b6   ................
00d0   82 e5 82 8e 00 0a 00 00 00 01 02 00 01            .............

0000   05 13 00 25 00 08 53 65 72 76 65 72 00 50 31 20   ...%..Server.P1
0010   44 65 6c 61 79 20 3d 20 31 20 28 31 20 66 72 61   Delay = 1 (1 fra
0020   6d 65 20 64 65 6c 61 79 29 00 12 00 02 00 15 00   me delay).......
0030   11 00 09 00 0e 00 09 00 00 00 02 01 08 10 00 06   ................
0040   00 11 00 01 00 01 01 0f 00 09 00 0e 00 09 00 00   ................
0050   00 01 01 08                                       ....

0000   03 08 00 1c 00 12 00 18 00 10 24 00 00 00 00 00   ..........${'$'}.....
0010   00 00 00 00 00 00 00 00 00 00 00 ff 00 00 00 00   ................
0020   00 07 00 02 00 15 00 06 00 06 00 11 00 ff ff ff   ................
0030   ff                                                .

0000   05 14 00 1c 00 12 00 18 00 00 00 00 00 00 00 00   ................
0010   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0020   00 13 00 25 00 08 53 65 72 76 65 72 00 50 31 20   ...%..Server.P1
0030   44 65 6c 61 79 20 3d 20 31 20 28 31 20 66 72 61   Delay = 1 (1 fra
0040   6d 65 20 64 65 6c 61 79 29 00 12 00 02 00 15 00   me delay).......
0050   11 00 09 00 0e 00 09 00 00 00 02 01 08 10 00 06   ................
0060   00 11 00 01 00 01 01                              .......

0000   03 09 00 1c 00 12 00 18 00 10 20 00 00 00 00 00   .......... .....
0010   00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0020   00 08 00 1c 00 12 00 18 00 10 24 00 00 00 00 00   ..........${'$'}.....
0030   00 00 00 00 00 00 00 00 00 00 00 ff 00 00 00 00   ................
0040   00 07 00 02 00 15 00                              .......

0000   05 15 00 03 00 13 00 00 14 00 1c 00 12 00 18 00   ................
0010   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0020   00 00 00 00 00 00 00 00 13 00 25 00 08 53 65 72   ..........%..Ser
0030   76 65 72 00 50 31 20 44 65 6c 61 79 20 3d 20 31   ver.P1 Delay = 1
0040   20 28 31 20 66 72 61 6d 65 20 64 65 6c 61 79 29    (1 frame delay)
0050   00 12 00 02 00 15 00 11 00 09 00 0e 00 09 00 00   ................
0060   00 02 01 08                                       ....

0000   03 0a 00 1c 00 12 00 18 00 10 20 00 00 00 00 00   .......... .....
0010   00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0020   00 09 00 1c 00 12 00 18 00 10 20 00 00 00 00 00   .......... .....
0030   00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0040   00 08 00 1c 00 12 00 18 00 10 24 00 00 00 00 00   ..........${'$'}.....
0050   00 00 00 00 00 00 00 00 00 00 00 ff 00 00 00 00   ................
0060   00                                                .

0000   05 16 00 03 00 13 00 00 15 00 03 00 13 00 00 14   ................
0010   00 1c 00 12 00 18 00 00 00 00 00 00 00 00 00 00   ................
0020   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 13   ................
0030   00 25 00 08 53 65 72 76 65 72 00 50 31 20 44 65   .%..Server.P1 De
0040   6c 61 79 20 3d 20 31 20 28 31 20 66 72 61 6d 65   lay = 1 (1 frame
0050   20 64 65 6c 61 79 29 00 12 00 02 00 15 00          delay).......

0000   03 0b 00 1c 00 12 00 18 00 10 20 00 00 00 00 00   .......... .....
0010   00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0020   00 0a 00 1c 00 12 00 18 00 10 20 00 00 00 00 00   .......... .....
0030   00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0040   00 09 00 1c 00 12 00 18 00 10 20 00 00 00 00 00   .......... .....
0050   00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00   ................
0060   00                                                .

0000   03 49 12 03 00 14 00 00 48 12 1c 00 12 00 18 00   .I......H.......
0010   10 20 00 00 00 00 00 00 01 00 00 00 00 00 00 00   . ..............
0020   00 00 00 00 00 00 00 00 47 12 1c 00 12 00 18 00   ........G.......
0030   10 20 00 00 00 00 00 00 01 00 00 00 00 00 00 00   . ..............
0040   00 00 00 00 00 00 00 00                           ........

0000   05 54 12 03 00 13 00 02 53 12 03 00 13 00 02 52   .T......S......R
0010   12 03 00 13 00 02 51 12 03 00 13 00 02 50 12 03   ......Q......P..
0020   00 13 00 02                                       ....

0000   05 55 12 09 00 0e 00 09 00 00 00 00 01 08 54 12   .U............T.
0010   03 00 13 00 02 53 12 03 00 13 00 02 52 12 03 00   .....S......R...
0020   13 00 02 51 12 03 00 13 00 02                     ...Q......

0000   05 56 12 10 00 14 6e 75 65 5b 43 46 31 5d 40 74   .V....nue[CF1]@t
0010   65 73 74 00 01 55 12 09 00 0e 00 09 00 00 00 00   est..U..........
0020   01 08 54 12 03 00 13 00 02 53 12 03 00 13 00 02   ..T......S......
0030   52 12 03 00 13 00 02                              R......

0000   03 4a 12 02 00 09 00 49 12 03 00 14 00 00 48 12   .J.....I......H.
0010   1c 00 12 00 18 00 10 20 00 00 00 00 00 00 01 00   ....... ........
0020   00 00 00 00 00 00 00 00 00 00 00 00 00 00         ..............

0000   03 4b 12 03 00 14 00 00 4a 12 02 00 09 00 49 12   .K......J.....I.
0010   03 00 14 00 00                                    .....

0000   03 4c 12 04 00 0b 00 ff ff 4b 12 03 00 14 00 00   .L.......K......
0010   4a 12 02 00 09 00                                 J.....

0000   05 57 12 06 00 10 00 09 00 00 00 56 12 10 00 14   .W.........V....
0010   6e 75 65 5b 43 46 31 5d 40 74 65 73 74 00 01 55   nue[CF1]@test..U
0020   12 09 00 0e 00 09 00 00 00 00 01 08 54 12 03 00   ............T...
0030   13 00 02 53 12 03 00 13 00 02                     ...S......

0000   05 58 12 11 00 0b 6e 75 65 5b 43 46 31 5d 40 74   .X....nue[CF1]@t
0010   65 73 74 00 10 00 57 12 06 00 10 00 09 00 00 00   est...W.........
0020   56 12 10 00 14 6e 75 65 5b 43 46 31 5d 40 74 65   V....nue[CF1]@te
0030   73 74 00 01 55 12 09 00 0e 00 09 00 00 00 00 01   st..U...........
0040   08 54 12 03 00 13 00 02                           .T......

0000   03 4d 12 05 00 01 00 ff ff 00 4c 12 04 00 0b 00   .M........L.....
0010   ff ff 4b 12 03 00 14 00 00                        ..K......

0000   05 59 12 24 00 01 6e 75 65 5b 43 46 31 5d 40 74   .Y.${'$'}..nue[CF1]@t
0010   65 73 74 00 10 00 4e 6f 72 6d 61 6c 20 43 6c 69   est...Normal Cli
0020   65 6e 74 20 45 78 69 74 00 58 12 11 00 0b 6e 75   ent Exit.X....nu
0030   65 5b 43 46 31 5d 40 74 65 73 74 00 10 00 57 12   e[CF1]@test...W.
0040   06 00 10 00 09 00 00 00 56 12 10 00 14 6e 75 65   ........V....nue
0050   5b 43 46 31 5d 40 74 65 73 74 00 01 55 12 09 00   [CF1]@test..U...
0060   0e 00 09 00 00 00 00 01 08                        .........
"""
