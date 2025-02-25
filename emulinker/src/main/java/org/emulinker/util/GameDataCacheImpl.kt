package org.emulinker.util

import java.lang.IndexOutOfBoundsException

class GameDataCacheImpl(capacity: Int) : GameDataCache {
  private var lastRetrievedIndex = -1

  private var array: Array<ByteArray?> = arrayOfNulls(capacity)

  // head points to the first logical element in the array, and
  // tail points to the element following the last. This means
  // that the list is empty when head == tail. It also means
  // that the array array has to have an extra space in it.
  private var head = 0
  private var tail = 0

  override var size = 0
    private set

  override fun containsAll(elements: Collection<ByteArray>): Boolean = elements.all { contains(it) }

  override fun isEmpty(): Boolean = size == 0

  override fun iterator(): Iterator<ByteArray> = array.asSequence().filterNotNull().iterator()

  override fun contains(element: ByteArray): Boolean = indexOf(element) >= 0

  override fun indexOf(data: ByteArray): Int {
    // Often the state is exactly the same from call to call, so it helps to check the last index
    // returned first.
    if (lastRetrievedIndex >= 0) {
      if (data.contentEquals(array[convert(lastRetrievedIndex)])) {
        return lastRetrievedIndex
      }
    }
    for (i in size - 1 downTo 0) {
      if (data.contentEquals(array[convert(i)])) {
        lastRetrievedIndex = i
        return i
      }
    }
    lastRetrievedIndex = -1
    return -1
  }

  override operator fun get(index: Int): ByteArray? {
    rangeCheck(index)
    return array[convert(index)]
  }

  override operator fun set(index: Int, data: ByteArray): ByteArray? {
    rangeCheck(index)
    val convertedIndex = convert(index)
    val oldValue = array[convertedIndex]
    array[convertedIndex] = data
    return oldValue
  }

  // This method is the main reason we re-wrote the class.
  // It is optimized for removing first and last elements
  // but also allows you to remove in the middle of the list.
  override fun remove(index: Int): ByteArray? {
    rangeCheck(index)
    val pos = convert(index)
    return try {
      array[pos]
    } finally {
      array[pos] = null // Let gc do its work

      // optimized for FIFO access, i.e. adding to back and
      // removing from front
      if (pos == head) {
        // head = (head + 1) % array.length;
        head++
        if (head == array.size) head = 0
      } else if (pos == tail) {
        tail = Math.floorMod(tail - 1, array.size)
      } else {
        if (pos > head && pos > tail) { // tail/head/pos
          System.arraycopy(array, head, array, head + 1, pos - head)
          // head = (head + 1) % array.length;
          head++
          if (head == array.size) head = 0
        } else {
          System.arraycopy(array, pos + 1, array, pos, tail - pos - 1)
          tail = Math.floorMod(tail - 1, array.size)
        }
      }
      size--
    }
  }

  override fun clear() {
    for (i in 0 until size) {
      array[convert(i)] = null
    }
    size = 0
    tail = size
    head = tail
  }

  override fun add(data: ByteArray): Int {
    if (size == array.size) remove(0)
    val pos = tail
    array[tail] = data

    // tail = ((tail + 1) % array.length);
    tail++
    if (tail == array.size) tail = 0
    size++
    return unconvert(pos)
  }

  /** Maps the external index to the index in [array]. */
  private fun convert(index: Int): Int = (index + head) % array.size

  private fun unconvert(index: Int): Int =
    if (index >= head) index - head else array.size - head + index

  private fun rangeCheck(index: Int) {
    if (index >= size || index < 0) throw IndexOutOfBoundsException("index=$index, size=$size")
  }
}
