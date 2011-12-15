/*
 * BleedingMobs - make your monsters and players bleed
 * Copyright (C) 2011  snowleo
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package my.bukkit.plugins.bleedingmobs;

import java.util.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Entity;


public class ParticleStorage
{
	private final transient Queue<Particle> freeParticles = new LinkedList<Particle>();
	private final transient Set<Particle> particles;
	private final transient Map<UUID, Particle> particleItems;
	private final transient Map<Location, Particle> particleBlocks;
	private final transient Random random = new Random();

	public ParticleStorage(final IBleedingMobs plugin, final int maxParticles)
	{
		particles = new HashSet<Particle>(maxParticles);
		particleItems = new HashMap<UUID, Particle>(maxParticles);
		particleBlocks = new HashMap<Location, Particle>(maxParticles);
		for (int i = 0; i < maxParticles; i++)
		{
			freeParticles.add(new Particle(plugin));
		}
	}

	public void clearAllParticles()
	{
		for (Particle particle : particles)
		{
			particle.restore();
		}
		particles.clear();
	}

	public void createParticle(final Location loc, final ParticleType type)
	{
		final int amount = random.nextInt(type.getAmountTo() - type.getAmountFrom()) + type.getAmountFrom();
		for (int i = 0; i < amount; i++)
		{
			final Particle particle = freeParticles.poll();
			if (particle == null)
			{
				return;
			}
			particles.add(particle);
			particle.start(loc, type);
		}
	}

	public void freeParticle(final Particle particle)
	{
		freeParticles.add(particle);
		particles.remove(particle);
	}

	public void addParticleItem(final UUID entityId, final Particle particle)
	{
		particleItems.put(entityId, particle);
	}

	public void removeParticleItem(final UUID entityId)
	{
		particleItems.remove(entityId);
	}

	public void removeParticleItemFromChunk(final Chunk chunk)
	{
		final Entity[] entities = chunk.getEntities();
		for (Entity entity : entities)
		{
			if (entity instanceof CraftItem)
			{
				final CraftItem item = (CraftItem)entity;
				final Particle particle = particleItems.get(item.getUniqueId());
				if (particle != null)
				{
					particle.restore();
				}
			}
		}
	}

	public boolean isParticleItem(final UUID entityId)
	{
		return particleItems.containsKey(entityId);
	}

	public void addUnbreakable(final Location blockLocation, final Particle particle)
	{
		particleBlocks.put(blockLocation, particle);
	}

	public void removeUnbreakable(final Location blockLocation)
	{
		particleBlocks.remove(blockLocation);
	}

	public void removeUnbreakableBeforeExplosion(final Location blockLocation)
	{
		final Particle particle = particleBlocks.get(blockLocation);
		if (particle != null)
		{
			particle.restore();
		}
	}

	public boolean isUnbreakable(final Location blockLocation)
	{
		return particleBlocks.containsKey(blockLocation);
	}

	public void removeUnbreakableFromChunk(final Chunk chunk)
	{
		final Iterator<Map.Entry<Location, Particle>> iterator = particleBlocks.entrySet().iterator();
		while (iterator.hasNext())
		{
			final Map.Entry<Location, Particle> entry = iterator.next();
			if (entry.getKey().getBlock().getChunk().equals(chunk))
			{
				final Particle particle = entry.getValue();
				particle.restore();
				iterator.remove();
			}
		}
	}
}