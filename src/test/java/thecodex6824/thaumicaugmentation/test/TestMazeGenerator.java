/*
 *  Thaumic Augmentation
 *  Copyright (c) 2022 TheCodex6824.
 *
 *  This file is part of Thaumic Augmentation.
 *
 *  Thaumic Augmentation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Thaumic Augmentation is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Thaumic Augmentation.  If not, see <https://www.gnu.org/licenses/>.
 */

package thecodex6824.thaumicaugmentation.test;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.util.EnumFacing;
import org.junit.Test;
import thecodex6824.thaumicaugmentation.common.util.maze.Maze;
import thecodex6824.thaumicaugmentation.common.util.maze.MazeCell;
import thecodex6824.thaumicaugmentation.common.util.maze.MazeGenerator;

import java.util.ArrayDeque;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestMazeGenerator {

    @Test
    public void testMazeGeneration() {
        for (int i = 0; i < 10000; ++i) {
            Random rand = new Random(i);
            Maze maze = new MazeGenerator().withSize(5, 5).generate(rand);
            
            // maze must have all areas accessible and filled, no openings to OoB
            // OoB openings should be added after generation
            IntOpenHashSet visited = new IntOpenHashSet();
            ArrayDeque<Integer> toCheck = new ArrayDeque<>();
            toCheck.add(0);
            while (!toCheck.isEmpty()) {
                int index = toCheck.pop();
                if (visited.add(index)) {
                    MazeCell cell = maze.getCell(index % maze.getWidth(), index / maze.getWidth());
                    for (EnumFacing f : EnumFacing.HORIZONTALS) {
                        if (!cell.hasWall(f)) {
                            int dir = 0;
                            if (f == EnumFacing.NORTH)
                                dir = -maze.getLength();
                            else if (f == EnumFacing.EAST)
                                dir = 1;
                            else if (f == EnumFacing.SOUTH)
                                dir = maze.getLength();
                            else
                                dir = -1;
                            
                            int check = index + dir;
                            if (dir == -1 || dir == 1) {
                                if (check < 0 || check >= maze.getWidth() * maze.getLength() || check / maze.getLength() != index / maze.getLength())
                                   fail("Maze cell wall has opening to out of bounds area");
                            }
                            else {
                                if (check < 0 || check >= maze.getWidth() * maze.getLength() || check % maze.getLength() != index % maze.getLength())
                                   fail("Maze cell wall has opening to out of bounds area");
                            }
                            
                            toCheck.add(check);
                        }
                    }
                }
            }
            
            assertEquals("Inaccessible maze cell(s), seed: " + i,
                    maze.getWidth() * maze.getLength(), visited.size());
        }
    }
    
}
