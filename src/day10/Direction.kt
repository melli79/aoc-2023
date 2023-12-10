package day10

enum class Direction(val dx :Int, val dy :Int) {
    EAST(1,0), NORTH(0,-1), WEST(-1,0), SOUTH(0,1);
}

data class Point(val r :Int, val c :Int) {
    operator fun plus(d :Direction) = Point(r+d.dy, c+d.dx)

    override fun toString() = "($r, $c)"
}
