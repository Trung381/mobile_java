class CustomSet(private val elements: MutableSet<Int> = mutableSetOf()) {

    // Constructor hỗ trợ truyền nhiều số nguyên cùng lúc
    constructor(vararg numbers: Int) : this(numbers.toMutableSet())

    fun isEmpty(): Boolean = elements.isEmpty()

    // Kiểm tra tập hợp hiện tại có phải là tập con của `other` không
    // all – Kiểm tra "tất cả" phần tử có thỏa mãn điều kiện không
    // collection.all { điều kiện }
    fun isSubset(other: CustomSet): Boolean =
        elements.all { it in other.elements }

    // Kiểm tra tập hợp hiện tại và `other` có phần tử chung hay không
    // none – Kiểm tra "không có" phần tử nào thỏa mãn điều kiện
    // Trả về true nếu không có phần tử nào thỏa mãn điều kiện.
    fun isDisjoint(other: CustomSet): Boolean =
        elements.none { it in other.elements }

    // Kiểm tra một phần tử có tồn tại trong tập hợp hay không
    fun contains(value: Int): Boolean =
        value in elements

    // Trả về tập hợp giao giữa hai tập hợp
    fun intersection(other: CustomSet): CustomSet =
        CustomSet(*elements.filter { it in other.elements }.toIntArray())

    // Thêm một phần tử vào tập hợp
    fun add(value: Int) {
        elements.add(value)
    }

    // So sánh hai tập hợp có bằng nhau không (cùng chứa một tập hợp phần tử)
    override fun equals(other: Any?): Boolean =
        other is CustomSet && elements == other.elements

    // Hợp hai tập hợp lại với nhau
    // elements + other.elements Ví dụ: nếu set1 = {1, 2, 3} và set2 = {3, 4, 5}, thì set1 + set2 sẽ thành {1, 2, 3, 4, 5}.
    // .toIntArray() Chuyển tập hợp thành mảng số nguyên (IntArray).
    // CustomSet(*...)
    // Dùng * (spread operator) để giải nén mảng số nguyên thành danh sách tham số riêng lẻ cho constructor của CustomSet.
    // Nếu elements + other.elements tạo {1, 2, 3, 4, 5}, thì * sẽ biến nó thành CustomSet(1, 2, 3, 4, 5), thay vì CustomSet(setOf(1, 2, 3, 4, 5)).
    operator fun plus(other: CustomSet): CustomSet =
        CustomSet(*(elements + other.elements).toIntArray())

    // Lấy hiệu của hai tập hợp
    operator fun minus(other: CustomSet): CustomSet =
        CustomSet(*(elements - other.elements).toIntArray())
}
