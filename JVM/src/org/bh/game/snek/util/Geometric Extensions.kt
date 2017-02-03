package org.bh.game.snek.util

import org.bh.tools.base.abstraction.Fraction
import org.bh.tools.base.math.geometry.FractionLineSegment
import org.bh.tools.base.math.geometry.fractionValue

/*
 * To help Snek with geometry
 *
 * @author Kyli Rouge
 * @since 2017-01-27
 */


operator fun FractionLineSegment.times(rhs: Fraction): FractionLineSegment = times(Pair(rhs, rhs))


operator fun FractionLineSegment.times(rhs: Pair<Fraction, Fraction>): FractionLineSegment {
    return FractionLineSegment(start = start.fractionValue * rhs, end = end.fractionValue * rhs)
}
