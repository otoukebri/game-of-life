package com.adamnfish.gol

import org.scalatest.{FreeSpec}
import org.scalatest.matchers.ShouldMatchers

class WorldParserTest extends FreeSpec with ShouldMatchers {
  "fromString" - {
    "extracts infinite universe from unconstrained string" in {
      val worldString =
        """
          |+---
          || x
          ||    x
          ||
        """.stripMargin
      val (universe, _) = WorldParser.fromString(worldString)
      universe should equal(InfiniteGol)
      universe.max should equal(None)
      universe.min should equal(None)
    }

    "can extract correctly constrained universe" in {
      val worldString =
        """+-----+
          || x
          ||x
          ||
          |+
        """.stripMargin
      val (universe, world) = WorldParser.fromString(worldString)
      universe.isInstanceOf[ConstrainedGol] should equal(true)
      universe.maxX(world) should equal(4)
      universe.minX(world) should equal(0)
      universe.maxY(world) should equal(2)
      universe.minY(world) should equal(0)
    }

    "can parse constrained worlds" in {
      val world1 =
        """
          |+----+
          ||x x
          || x
          ||
          |+
        """.stripMargin
      val (universe, world) = WorldParser.fromString(world1)
      assertWorld(universe, world, Cell(0, 0), Cell(2, 0), Cell(1, 1))
    }
  }

  def assertWorld(universe: Gol, world: World, liveCells: Cell*): Unit = {
    (universe.minX(world) to universe.maxX(world)) flatMap { x =>
      (universe.minY(world) to universe.maxY(world)) map { y =>
        val cell: Cell = Cell(x, y)
        if (liveCells.contains(cell)) universe.isAlive(cell, world) should equal(true)
        else universe.isAlive(cell, world) should equal(false)
      }
    }
  }
}
