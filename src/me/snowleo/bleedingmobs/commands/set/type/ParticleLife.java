/*
 * BleedingMobs - make your monsters and players bleed
 *
 * Copyright (C) 2011-2012 snowleo
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.snowleo.bleedingmobs.commands.set.type;

import me.snowleo.bleedingmobs.IBleedingMobs;
import me.snowleo.bleedingmobs.commands.AbstractTypeCommand;
import me.snowleo.bleedingmobs.commands.parser.FromToIntegerParser;
import me.snowleo.bleedingmobs.commands.parser.LowerBoundIntegerParser;
import me.snowleo.bleedingmobs.particles.ParticleType;
import org.bukkit.command.CommandSender;


public class ParticleLife extends AbstractTypeCommand<Integer[]>
{
	public ParticleLife(ParticleType type, IBleedingMobs plugin)
	{
		super(type, plugin, new FromToIntegerParser(new LowerBoundIntegerParser(0)));
	}

	@Override
	public void run(CommandSender sender, Integer[] arg, ParticleType type)
	{
		final int particlelifefrom = arg[0];
		final int particlelifeto = arg[1];
		type.setParticleLifeFrom(particlelifefrom);
		type.setParticleLifeTo(particlelifeto);
		sender.sendMessage("Particle Life set to a random number between " + particlelifefrom + " and " + particlelifeto + ".");
	}
}