import kotlin.collections.ArrayDeque

class EmptyBufferException : Exception("Buffer is empty")
class BufferFullException : Exception("Buffer is full")

class CircularBuffer<T>(private val capacity: Int) {
    private val buffer = ArrayDeque<T>(capacity)

    fun read(): T {
        if (buffer.isEmpty()) throw EmptyBufferException()
        return buffer.removeFirst()
    }

    fun write(value: T) {
        if (buffer.size >= capacity) throw BufferFullException()
        buffer.addLast(value)
    }

    fun overwrite(value: T) {
        if (buffer.size >= capacity) {
            buffer.removeFirst() // Remove the oldest element
        }
        buffer.addLast(value)
    }

    fun clear() {
        buffer.clear()
    }
}
