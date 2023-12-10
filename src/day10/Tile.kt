package day10

enum class Tile(val symbol :Char) {
    Sand('.') {
        override fun turn(d :Direction) = null
    }, Start('S') {
        override fun turn(d :Direction) = d
    },
    Horiz('-') {
        override fun turn(d :Direction) = when (d) {
            Direction.EAST -> Direction.EAST
            Direction.WEST -> Direction.WEST
            else -> null
        }
    }, Vert('|') {
        override fun turn(d :Direction) = when (d) {
            Direction.NORTH -> Direction.NORTH
            Direction.SOUTH -> Direction.SOUTH
            else -> null
        }
    }, // Bridge('+'),
    NE('L') {
        override fun turn(d :Direction) = when (d) {
            Direction.WEST -> Direction.NORTH
            Direction.SOUTH -> Direction.EAST
            else -> null
        }
    }, NW('J') {
        override fun turn(d :Direction) = when (d) {
            Direction.EAST -> Direction.NORTH
            Direction.SOUTH -> Direction.WEST
            else -> null
        }
    }, SW('7') {
        override fun turn(d :Direction) = when (d) {
            Direction.EAST -> Direction.SOUTH
            Direction.NORTH -> Direction.WEST
            else -> null
        }
    }, SE('F') {
        override fun turn(d :Direction) :Direction? = when (d) {
            Direction.WEST -> Direction.SOUTH
            Direction.NORTH -> Direction.EAST
            else -> null
        }
    };

    override fun toString() = symbol.toString()

    abstract fun turn(d :Direction) :Direction?

    companion object {
        fun of(symbol :Char) = entries.find { it.symbol==symbol }
    }
}