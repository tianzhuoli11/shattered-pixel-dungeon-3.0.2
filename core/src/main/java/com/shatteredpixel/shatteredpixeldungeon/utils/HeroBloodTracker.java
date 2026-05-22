/*
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

/** Records hero HP deltas to {@link PlayerEventLogger} as STATE / PLAYER_BLOOD. */
public final class HeroBloodTracker {

	private HeroBloodTracker() {}

	public static void record( Char ch, int hpBefore, int hpAfter, Object cause ) {
		if ( ch != Dungeon.hero || hpBefore == hpAfter ) {
			return;
		}
		int delta = hpAfter - hpBefore;
		if ( delta > 0 ) {
			PlayerEventLogger.playerBlood( true, delta, causeName( cause ) );
		} else {
			PlayerEventLogger.playerBlood( false, -delta, causeName( cause ) );
		}
	}

	/** Apply immediate healing and log (for food, dewdrops, etc.). */
	public static void heal( Hero hero, int amount, Object cause ) {
		if ( amount <= 0 ) {
			return;
		}
		int before = hero.HP;
		hero.HP = Math.min( hero.HT, hero.HP + amount );
		record( hero, before, hero.HP, cause );
	}

	/** Set HP to a value (capped) and log. */
	public static void setHP( Hero hero, int hp, Object cause ) {
		int before = hero.HP;
		hero.HP = Math.min( hero.HT, Math.max( 0, hp ) );
		record( hero, before, hero.HP, cause );
	}

	public static String causeName( Object cause ) {
		if ( cause == null ) {
			return "unknown";
		}
		if ( cause instanceof String ) {
			return (String) cause;
		}
		if ( cause instanceof Class ) {
			return ((Class<?>) cause).getSimpleName();
		}
		return cause.getClass().getSimpleName();
	}
}
